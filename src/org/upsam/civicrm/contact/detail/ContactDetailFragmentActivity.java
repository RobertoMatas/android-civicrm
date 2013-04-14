package org.upsam.civicrm.contact.detail;

import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.fragments.ContactAddressFragment;
import org.upsam.civicrm.contact.detail.fragments.ContactDetailFragment;
import org.upsam.civicrm.contact.detail.fragments.ContactTagsAndGroupsFragment;
import org.upsam.civicrm.contact.detail.fragments.ListContactsActivitiesFragment;
import org.upsam.civicrm.contact.detail.fragments.OtherInformationFragment;
import org.upsam.civicrm.contact.detail.menu.MenuOrganizationFragment.OnMenuItemSelectedListener;
import org.upsam.civicrm.contact.model.contact.ContactSummary;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.slidingmenu.lib.SlidingMenu;

public class ContactDetailFragmentActivity extends
		SpiceAndSliceMenuAwareFragmentActivity
		implements
		OnMenuItemSelectedListener,
		org.upsam.civicrm.contact.detail.menu.MenuIndividualFragment.OnMenuItemSelectedListener {

	int contactId = 0;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContactSummary contact = (ContactSummary) getIntent().getExtras().get(
				"contact");
		this.contactId = contact.getId();
		setContentView(R.layout.activity_contact_details);

		if (contact != null
				&& contact.getType() != null
				&& (contact.getType().startsWith("Org") || contact.getType()
						.startsWith("org"))) {
			// if ("Organization".equalsIgnoreCase(contact.getType())) {
			setBehindContentView(R.layout.slide_menu_organization);
		} else {
			setBehindContentView(R.layout.slide_menu_individual);
		}
		setSlidingActionBarEnabled(false);
		SlidingMenu menu = getSlidingMenu();
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setDisplayShowCustomEnabled(true);
			getActionBar().setIcon(R.drawable.ic_slide);
		}
		if (findViewById(R.id.FrameLayout1) != null) {
			if (savedInstanceState != null) {
				return;
			}
			ContactDetailFragment contactDetailFragment = new ContactDetailFragment(
					contentManager, this);
			contactDetailFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.FrameLayout1, contactDetailFragment,
							"contactDetails").commit();
		}

	}

	private void addAddressFragment() {
		ContactAddressFragment addressFragment = new ContactAddressFragment(
				contentManager, this);
		addressFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FrameLayout2, addressFragment).commit();
		getSlidingMenu().toggle();
	}

	private void addOtherInfoFragment() {
		OtherInformationFragment otherInformationFragment = new OtherInformationFragment(
				contentManager, this);
		otherInformationFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FrameLayout2, otherInformationFragment).commit();
		getSlidingMenu().toggle();
	}

	private void updateCommunicationPreferences() {
		ContactDetailFragment fragment = (ContactDetailFragment) getSupportFragmentManager()
				.findFragmentByTag("contactDetails");
		fragment.showCommunicationPreferences();
		getSlidingMenu().toggle();
	}

	private void updateDemographics() {
		ContactDetailFragment fragment = (ContactDetailFragment) getSupportFragmentManager()
				.findFragmentByTag("contactDetails");
		fragment.updateDemographics();
		getSlidingMenu().toggle();
	}

	private void addActivitiesFragment() {
		ListContactsActivitiesFragment activitiesFragment = new ListContactsActivitiesFragment(
				contentManager, this);
		activitiesFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FrameLayout2, activitiesFragment).commit();
		getSlidingMenu().toggle();
		
	}

	private void addGroupsAndTagsFragment() {
		ContactTagsAndGroupsFragment tagsAndGroupsFragment = new ContactTagsAndGroupsFragment(
				contentManager, this);
		tagsAndGroupsFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FrameLayout2, tagsAndGroupsFragment).commit();
		getSlidingMenu().toggle();
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
