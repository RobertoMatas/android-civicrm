package org.upsam.civicrm.test;

import static android.test.ViewAsserts.assertOnScreen;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.fragments.OtherInformationFragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactOtherInfoFragmentTests extends
		AbstractContactDetailFragmentTest<OtherInformationFragment> {

	/**
	 * 
	 */
	public ContactOtherInfoFragmentTests() {
		super("contactOtherInformation");
	}

	@Before
	public void setUp() {
		mActivity
				.onMenuIndividualItemSelected(indexForItem(R.string.constituent_information));
		fragment = (OtherInformationFragment) mActivity
				.getSupportFragmentManager().findFragmentByTag(fragmentTagName);
		fragmentLayout = fragment.getView();
	}

	@Test
	public void contactOtherInformationFragmentPresence() {
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
		
		inOrder.verify(spyRequestBuilder).requestCustomFields();
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));

		inOrder.verify(spyRequestBuilder).requestCustomValuesByContactId(TEST_CONTACT_ID);
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));

		inOrder.verify(spyRequestBuilder).requestHumanReadableValue(eq(1), eq("Value 1"));
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));
		
		inOrder.verify(spyRequestBuilder).requestHumanReadableValue(eq(2), eq("Value 2"));
		inOrder.verify(spySpiceManager).execute(notNull(SpiceRequest.class),
				notNull(String.class), anyLong(),
				notNull(RequestListener.class));
	}

	@Test
	public void contactOtherInformationArePaintedCorrectly() {
		LinearLayout otherInfoLayout = (LinearLayout) fragmentLayout
				.findViewById(R.id.other_info);
		TextView  title = (TextView) otherInfoLayout.getChildAt(0);
		ViewGroup row1 = (ViewGroup) otherInfoLayout.getChildAt(1);
		ViewGroup row2 = (ViewGroup) otherInfoLayout.getChildAt(2);
		assertThat(otherInfoLayout) 					//
				.isVisible() 							//
				.hasChildCount(rowsForTestPlusTitle());	//
		assertThat(title) 								//
				.isVisible()
				.containsText(R.string.constituent_information);
		assertThat(row1) 								//
				.isVisible() 							//
				.hasChildCount(otherInfoPlusIcon()); 	//
		assertThat(row2) 								//
				.isVisible() 							//
				.hasChildCount(otherInfoPlusIcon()); 	//

	}

	private int otherInfoPlusIcon() {
		return 3;
	}

	private int rowsForTestPlusTitle() {
		return 3;
	}
}
