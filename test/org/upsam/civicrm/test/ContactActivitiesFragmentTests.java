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
import org.upsam.civicrm.contact.detail.fragments.ListContactsActivitiesFragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactActivitiesFragmentTests extends
		AbstractContactDetailFragmentTest<ListContactsActivitiesFragment> {

	/**
	 * 
	 */
	public ContactActivitiesFragmentTests() {
		super("contactActivities");
	}

	@Before
	public void setUp() {
		mActivity
				.onMenuIndividualItemSelected(indexForItem(R.string.contact_activities));
		fragment = (ListContactsActivitiesFragment) mActivity
				.getSupportFragmentManager().findFragmentByTag(fragmentTagName);
		fragmentLayout = fragment.getView();
	}

	@Test
	public void contactActivitiesFragmentPresence() {
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
		inOrder.verify(spyRequestBuilder).requestActivitiesForContact(
				TEST_CONTACT_ID);
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));
	}

	@Test
	public void contactActivitiesArePaintedCorrectly() {
		LinearLayout activitiesListLayout = (LinearLayout) fragmentLayout
				.findViewById(R.id.addressList);
		assertThat(activitiesListLayout) //
				.isVisible() //
				.hasChildCount(activitiesForContactUnderTest()); //
		for (int i = 0; i < activitiesForContactUnderTest(); i++) {
			assertThat((ViewGroup) activitiesListLayout.getChildAt(i)) //
					.isVisible() //
					.hasChildCount(numberOfLinesforActivityPainted()); //
		}
	}

	private int numberOfLinesforActivityPainted() {
		return 6;
	}

	private int activitiesForContactUnderTest() {
		return 3;
	}
}
