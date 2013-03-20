package org.upsam.civicrm.contact.list;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

/**
 * This demonstrates the use of action bar tabs and how they interact with other
 * action bar features.
 */
public class ContactListTabs extends Activity {

	private static final String ALL = "All";
	private static final String INDIVIDUAL = "Individual";
	private static final String ORGANIZATION = "Organization";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		Bundle args = new Bundle();
		args.putString("contact_type", ORGANIZATION);
		Tab tab = bar
				.newTab()
				.setText(ORGANIZATION)
				.setTabListener(
						new TabListener<ContactListFragment>(this,
							ORGANIZATION, ContactListFragment.class, args));
		bar.addTab(tab);
		
		args = new Bundle();
		args.putString("contact_type", INDIVIDUAL);
		tab = bar
				.newTab()
				.setText(INDIVIDUAL)
				.setTabListener(
						new TabListener<ContactListFragment>(this,
								INDIVIDUAL, ContactListFragment.class, args));
		bar.addTab(tab);
		
		args = new Bundle();
		args.putString("contact_type", "");
		tab = bar.newTab().setText(ALL)
				.setTabListener(new TabListener<ContactListFragment>(this,
						ALL, ContactListFragment.class, args));
		bar.addTab(tab);

		bar.selectTab(bar.getTabAt(2));

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

	        public void onTabSelected(Tab tab, FragmentTransaction ft) {
	            if (mFragment == null) {
	                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
	                ft.add(android.R.id.content, mFragment, mTag);
	            } else {
	                ft.attach(mFragment);
	            }
	        }

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
