package org.upsam.civicrm.test;

import static android.test.ViewAsserts.assertOnScreen;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.fragments.ContactDetailFragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactDetailFragmentTests extends
		AbstractContactDetailFragmentTest<ContactDetailFragment> {

	private static final int EMAILS_PLUS_PHONES = 4;
	private static final int COMMUNICATION_PREFERENCES_DATA = 3;
	private static final int DEMOGRAPHICS_DATA = 3;

	/**
	 * Vista nombre de contacto
	 */
	private TextView displayName;
	/**
	 * Vista tipo de contacto
	 */
	private TextView type;
	/**
	 * Vista foto de contacto
	 */
	private QuickContactBadge badge;
	/**
	 * 
	 */
	private LinearLayout contactData;

	/**
	 * 
	 */
	public ContactDetailFragmentTests() {
		super("contactDetails");
	}

	@Before
	public void setUp() throws Exception {
		fragment = (ContactDetailFragment) mActivity.getSupportFragmentManager()
				.findFragmentByTag(fragmentTagName);
		fragmentLayout = fragment.getView();
		this.displayName = (TextView) fragmentLayout
				.findViewById(R.id.display_name);
		this.type = (TextView) fragmentLayout.findViewById(R.id.contact_type);
		this.badge = (QuickContactBadge) fragmentLayout
				.findViewById(R.id.contac_img);
		this.contactData = (LinearLayout) fragmentLayout
				.findViewById(R.id.contact_data);

	}

	@Test
	public void testContactDetailFragmentPresence() {
		assertThat(fragment).isNotNull();

	}

	@Test
	public void testViewsAreOnScreen() {
		final View origin = mActivity.getWindow().getDecorView();
		assertOnScreen(origin, fragmentLayout);
		assertOnScreen(origin, displayName);
		assertOnScreen(origin, type);
		assertOnScreen(origin, badge);
		assertOnScreen(origin, contactData);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testAllInitialRequestAreExecutedWithCorrectParameters() {
		verify(spyRequestBuilder).requestContactById(TEST_CONTACT_ID);
		verify(spyRequestBuilder).requestEmailsByContactId(TEST_CONTACT_ID);
		verify(spyRequestBuilder).requestPhonesByContactId(TEST_CONTACT_ID);

		verify(spySpiceManager, times(3)).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));
	}

	@Test
	public void testInitialDetailsShowNameTypeEmailsAndPhones() {
		assertThat(displayName).containsText(TEST_CONTACT_NAME);
		assertThat(type).containsText(TEST_CONTACT_TYPE);
		assertThat(contactData).hasChildCount(EMAILS_PLUS_PHONES);
	}

	@Test
	public void testShowCommunicationPreferencesWhenUserSelectItOnSlideMenu() {
		mActivity
				.onMenuIndividualItemSelected(indexForItem(R.string.communication_preferences));

		verify(spyRequestBuilder).requestCommunicationPreferencesByContactId(
				TEST_CONTACT_ID);
		assertThat(contactData).hasChildCount(
				EMAILS_PLUS_PHONES + COMMUNICATION_PREFERENCES_DATA);
	}

	@Test
	public void testShowDemographicsDataWhenUserSelectItOnSlideMenu() {
		mActivity
				.onMenuIndividualItemSelected(indexForItem(R.string.demographics));

		assertThat(contactData).hasChildCount(
				EMAILS_PLUS_PHONES + DEMOGRAPHICS_DATA);
	}

}
