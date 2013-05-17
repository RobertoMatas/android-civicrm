package org.upsam.civicrm.test;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.ContactDetailFragmentActivity;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.dagger.di.AppicationInjectionAware;
import org.upsam.civicrm.dagger.di.fragment.SpiceDIAwareFragment;
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilder;
import org.upsam.civicrm.test.roboelectric.runner.CustomTestRunner;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.octo.android.robospice.SpiceManager;
import com.xtremelabs.robolectric.Robolectric;

import dagger.ObjectGraph;

@RunWith(CustomTestRunner.class)
@Ignore
public abstract class AbstractContactDetailFragmentTest<FRAGMENT extends SpiceDIAwareFragment> {

	protected static final String TEST_CONTACT_TYPE = "Individual";
	protected static final String TEST_CONTACT_NAME = "Test Name";
	protected static final int TEST_CONTACT_ID = 1;

	protected final String fragmentTagName;

	/**
	 * 
	 * @param fragmentTagName
	 */
	public AbstractContactDetailFragmentTest(final String fragmentTagName) {
		this.fragmentTagName = fragmentTagName;
	}

	/**
	 * 
	 */
	FRAGMENT fragment;
	/**
	 * 
	 */
	View fragmentLayout;
	/**
	 * 
	 */
	ContactDetailFragmentActivity mActivity;

	@Inject
	SpiceManager spySpiceManager;

	@Inject
	CiviCRMContactRequestBuilder spyRequestBuilder;

	protected ContactSummary getParams() {
		ContactSummary data = new ContactSummary();
		data.setId(TEST_CONTACT_ID);
		data.setName(TEST_CONTACT_NAME);
		data.setType(TEST_CONTACT_TYPE);
		return data;
	}

	@Before
	public void init() throws Exception {
		ObjectGraph objectGraph = ((AppicationInjectionAware) Robolectric.application)
				.getApplicationGraph();
		objectGraph.inject(this);
		mActivity = new ContactDetailFragmentActivity();
		Intent intent = new Intent();
		ContactSummary params = getParams();
		intent.putExtra("contact", params);
		mActivity.setIntent(intent);
		Bundle savedInstanceState = new Bundle();
		savedInstanceState.putParcelable("contact", params);
		mActivity.onCreate(savedInstanceState);
		objectGraph.inject(mActivity);
		mActivity.onPostCreate(null);
	}

	protected int indexForItem(int itemResource) {
		Resources resources = mActivity.getResources();
		String[] options = resources
				.getStringArray(R.array.slide_individual_menu);
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals(resources.getString(itemResource))) {
				return i;
			}
		}
		return -1;
	}
}
