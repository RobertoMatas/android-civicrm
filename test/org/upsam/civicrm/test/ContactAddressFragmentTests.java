package org.upsam.civicrm.test;

import static android.test.ViewAsserts.assertOnScreen;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.notNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.fragments.ContactAddressFragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactAddressFragmentTests extends
		AbstractContactDetailFragmentTest<ContactAddressFragment> {

	/**
	 * 
	 */
	public ContactAddressFragmentTests() {
		super("contactAddress");
	}

	@Before
	public void setUp() {
		mActivity.onMenuIndividualItemSelected(indexForItem(R.string.address));
		fragment = (ContactAddressFragment) mActivity
				.getSupportFragmentManager().findFragmentByTag(fragmentTagName);
		fragmentLayout = fragment.getView();
	}

	@Test
	public void contactAddressFragmentPresence() {
		assertThat(fragment).isNotNull();
	}

	@Test
	public void viewsAreOnScreen() {
		final View origin = mActivity.getWindow().getDecorView();
		assertOnScreen(origin, fragmentLayout);
		assertOnScreen(origin, mActivity.findViewById(R.id.FrameLayout2));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void allRequestAreExecutedInCorrectOrder() {
		InOrder inOrder = Mockito.inOrder(spyRequestBuilder, spySpiceManager);
		inOrder.verify(spyRequestBuilder).requestCountries();
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));

		inOrder.verify(spyRequestBuilder).requestLocationTypes();
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));

		inOrder.verify(spyRequestBuilder).requestContactAddresses(
				TEST_CONTACT_ID);
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));
	}

	@Test
	public void contactAddressesArePaintedCorrectly() {
		LinearLayout addressListLayout = (LinearLayout) fragmentLayout
				.findViewById(R.id.addressList);
		ViewGroup row1 = (ViewGroup) addressListLayout.getChildAt(0);
		ViewGroup row2 = (ViewGroup) addressListLayout.getChildAt(1);
		assertThat(addressListLayout) //
				.isVisible() //
				.hasChildCount(emailsForContactUnderTRest()); //
		assertThat(row1) //
				.isVisible() //
				.hasChildCount(numberOfAddresDataPainted()); //
		assertThat(row2) //
				.isVisible() //
				.hasChildCount(numberOfAddresDataPainted()); //

	}

	private int numberOfAddresDataPainted() {
		return 6;
	}

	private int emailsForContactUnderTRest() {
		return 2;
	}
}
