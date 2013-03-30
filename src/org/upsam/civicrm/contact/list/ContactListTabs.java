package org.upsam.civicrm.contact.list;

import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.ContactDetailFragmentActivity;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

/**
 * This demonstrates the use of action bar tabs and how they interact with other
 * action bar features.
 */
public class ContactListTabs extends Activity {

	private static final String ALL = "All";
	private static final String INDIVIDUAL = "Individual";
	private static final String ORGANIZATION = "Organization";
	private Context activityContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configureTabs(savedInstanceState);
		this.activityContext = this;
	}

	private void configureTabs(Bundle savedInstanceState) {
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
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 2));
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

		
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_contact_list, menu);		
		MenuItem menuItem = menu.findItem(R.id.menu_search);
		menuItem.setOnActionExpandListener(new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				final AutoCompleteTextView autoComTextView = (AutoCompleteTextView) item.getActionView().findViewById(R.id.ab_Search);
	    		final ContactAutoCompleteListAdapter adapter = new ContactAutoCompleteListAdapter(
	    				new CiviCRMAndroidSpiceService().createRestTemplate(), 
	    				ContactListTabs.this, new ListContacts(),activityContext);
				autoComTextView.setAdapter(adapter);
	    		autoComTextView.setThreshold(3);
	    		autoComTextView.setMaxLines(1);
	    		autoComTextView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						autoComTextView.setText("");
						Intent intent = new Intent(ContactListTabs.this, ContactDetailFragmentActivity.class);
						intent.putExtra("contact", (ContactSummary) adapter.getItem(position));
						startActivity(intent);
						
					}
				});
	    		return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
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
