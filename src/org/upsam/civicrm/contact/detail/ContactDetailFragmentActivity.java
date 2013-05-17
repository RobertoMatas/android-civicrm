package org.upsam.civicrm.contact.detail;

import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.fragments.ContactAddressFragment;
import org.upsam.civicrm.contact.detail.fragments.ContactDetailFragment;
import org.upsam.civicrm.contact.detail.fragments.ContactTagsAndGroupsFragment;
import org.upsam.civicrm.contact.detail.fragments.ListContactsActivitiesFragment;
import org.upsam.civicrm.contact.detail.fragments.OtherInformationFragment;
import org.upsam.civicrm.contact.detail.menu.MenuOrganizationFragment.OnMenuItemSelectedListener;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.dagger.di.activity.SpiceDIAwareActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.slidingmenu.lib.SlidingMenu;

public class ContactDetailFragmentActivity extends SpiceDIAwareActivity
		implements
		OnMenuItemSelectedListener,
		org.upsam.civicrm.contact.detail.menu.MenuIndividualFragment.OnMenuItemSelectedListener {

	private static final String CONTACT_KEY = "contact";
	private ContactSummary contact;
	private SlidingMenu menu;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(CONTACT_KEY)) {
			this.contact = (ContactSummary) savedInstanceState.get(CONTACT_KEY);
		} else {
			this.contact = (ContactSummary) getIntent().getExtras().get(
					CONTACT_KEY);
		}
		setContentView(R.layout.activity_contact_details);

		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		if (contact != null
				&& contact.getType() != null
				&& (contact.getType().startsWith("Org") || contact.getType()
						.startsWith("org"))) {
			menu.setMenu(R.layout.slide_menu_organization);
		} else {
			menu.setMenu(R.layout.slide_menu_individual);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(false);
				actionBar.setDisplayShowCustomEnabled(true);
				actionBar.setIcon(R.drawable.ic_slide);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (findViewById(R.id.FrameLayout1) != null) {
			if (savedInstanceState != null) {
				return;
			}
			ContactDetailFragment contactDetailFragment = new ContactDetailFragment();
			Bundle params = new Bundle();
			params.putParcelable("contact", this.contact);
			contactDetailFragment.setArguments(params);
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.FrameLayout1, contactDetailFragment,
							"contactDetails").commit();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (contact != null) {
			outState.putParcelable(CONTACT_KEY, contact);
		}
		super.onSaveInstanceState(outState);
	}

	private void addAddressFragment() {
		ContactAddressFragment addressFragment = new ContactAddressFragment();
		addressFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FrameLayout2, addressFragment, "contactAddress")
				.commit();
		menu.toggle();
	}

	private void addOtherInfoFragment() {
		OtherInformationFragment otherInformationFragment = new OtherInformationFragment();
		otherInformationFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.FrameLayout2, otherInformationFragment,
						"contactOtherInformation").commit();
		menu.toggle();
	}

	private void updateCommunicationPreferences() {
		ContactDetailFragment fragment = (ContactDetailFragment) getSupportFragmentManager()
				.findFragmentByTag("contactDetails");
		fragment.showCommunicationPreferences();
		menu.toggle();
	}

	private void updateDemographics() {
		ContactDetailFragment fragment = (ContactDetailFragment) getSupportFragmentManager()
				.findFragmentByTag("contactDetails");
		fragment.updateDemographics();
		menu.toggle();
	}

	private void addActivitiesFragment() {
		ListContactsActivitiesFragment activitiesFragment = new ListContactsActivitiesFragment();
		activitiesFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FrameLayout2, activitiesFragment,
						"contactActivities").commit();
		menu.toggle();

	}

	private void addGroupsAndTagsFragment() {
		ContactTagsAndGroupsFragment tagsAndGroupsFragment = new ContactTagsAndGroupsFragment();
		tagsAndGroupsFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FrameLayout2, tagsAndGroupsFragment,
						"contactTagsAndGroups").commit();
		menu.toggle();
	}

	@Override
	public void onMenuOrganizationItemSelected(int position) {
		Resources resources = getResources();
		String[] options = resources
				.getStringArray(R.array.slide_organization_menu);
		String optionAddress = resources.getString(R.string.address);
		String optionCommPreferences = resources
				.getString(R.string.communication_preferences);
		String optionTagsAndGroup = resources
				.getString(R.string.tags_and_groups);
		String activities = resources.getString(R.string.contact_activities);
		if (options[position].equals(optionAddress)) {
			addAddressFragment();
		} else if (options[position].equals(optionCommPreferences)) {
			updateCommunicationPreferences();
		} else if (options[position].equals(optionTagsAndGroup)) {
			addGroupsAndTagsFragment();
		} else if (options[position].equals(activities)) {
			addActivitiesFragment();
		}
	}

	@Override
	public void onMenuIndividualItemSelected(int position) {
		Resources resources = getResources();
		String[] options = resources
				.getStringArray(R.array.slide_individual_menu);
		String optionAddress = resources.getString(R.string.address);
		String optionCommPreferences = resources
				.getString(R.string.communication_preferences);
		String optionTagsAndGroup = resources
				.getString(R.string.tags_and_groups);
		String optionDemographics = resources.getString(R.string.demographics);
		String optionOtherInfo = resources
				.getString(R.string.constituent_information);
		String activities = resources.getString(R.string.contact_activities);
		if (options[position].equals(optionAddress)) {
			addAddressFragment();
		} else if (options[position].equals(optionCommPreferences)) {
			updateCommunicationPreferences();
		} else if (options[position].equals(optionTagsAndGroup)) {
			addGroupsAndTagsFragment();
		} else if (options[position].equals(optionDemographics)) {
			updateDemographics();
		} else if (options[position].equals(optionOtherInfo)) {
			addOtherInfoFragment();
		} else if (options[position].equals(activities)) {
			addActivitiesFragment();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
