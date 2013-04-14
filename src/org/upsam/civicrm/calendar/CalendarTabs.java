package org.upsam.civicrm.calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

public class CalendarTabs extends Activity{
	
	private static final String MONTH = "Mes";
	private static final String WEEK  = "Semana";
	private static final String DAY   = "Dia";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		configureTabs(savedInstanceState);

	}
	
	private void configureTabs(Bundle savedInstanceState) {
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		Bundle args = new Bundle();
		args.putString("calendar_type", MONTH);
		Tab tab = bar
				.newTab()
				.setText(MONTH)
				.setTabListener(
						new TabListener<MonthlyCalendarFragment>(this,
							MONTH, MonthlyCalendarFragment.class, args));
		bar.addTab(tab);
		
		args = new Bundle();
		args.putString("calendar_type", WEEK);
		tab = bar
				.newTab()
				.setText(WEEK)
				.setTabListener(
						new TabListener<WeeklyCalendarFragment>(this,
								WEEK, WeeklyCalendarFragment.class, args));
		bar.addTab(tab);
		
		args = new Bundle();
		args.putString("calendar_type", DAY);
		tab = bar.newTab().setText(DAY)
				.setTabListener(new TabListener<DailyCalendarFragment>(this,
						DAY, DailyCalendarFragment.class, args));
		bar.addTab(tab);

		bar.selectTab(bar.getTabAt(0));

		if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}
	}
	
	
	
	   @Override
	protected void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}



	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
	        private final Activity mActivity;
	        private final String mTag;
	        private final Class<T> mClass;
	        private final Bundle mArgs;
	        private Fragment mFragment;

	        public TabListener(Activity activity, String tag, Class<T> clz) {
	            this(activity, tag, clz, null);
	        }

	        @SuppressLint("NewApi")
			public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
	            mActivity = activity;
	            mTag = tag;
	            mClass = clz;
	            mArgs = args;

	            // Check to see if we already have a fragment for this tab, probably
	            // from a previously saved state.  If so, deactivate it, because our
	            // initial state is that a tab isn't shown.
	            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
	            if (mFragment != null && !mFragment.isDetached()) {
	                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
	                ft.detach(mFragment);
	                ft.commit();
	            }
	        }

	        @SuppressLint("NewApi")
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
	            if (mFragment == null) {
	                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
	                ft.add(android.R.id.content, mFragment, mTag);
	            } else {
	                ft.attach(mFragment);
	            }
	        }

	        @SuppressLint("NewApi")
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	            if (mFragment != null) {
	                ft.detach(mFragment);
	            }
	        }

	        public void onTabReselected(Tab tab, FragmentTransaction ft) {
	            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
	        }
	    }
	
}
