/**
 * 
 */
package org.upsam.civicrm.test;

import static android.test.ViewAsserts.assertOnScreen;
import static junit.framework.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.add.AddContactActivity;
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilder;
import org.upsam.civicrm.test.dagger.modules.TestModule;
import org.upsam.civicrm.test.roboelectric.runner.CustomTestRunner;

import android.view.View;
import android.widget.Spinner;

import com.octo.android.robospice.SpiceManager;

import dagger.ObjectGraph;

/**
 * @author pepin_000
 * 
 */
@RunWith(CustomTestRunner.class)
public class AddContactActivityTests {

	@Inject
	AddContactActivity mActivity;

	@Inject
	CiviCRMContactRequestBuilder reqBuilder;

	@Inject
	SpiceManager mockSpiceManager;

	private Spinner contactTypeSpinner;

	@Before
	public void setUp() {
		ObjectGraph objectGraph = ObjectGraph.create(new TestModule());
		objectGraph.inject(this);
		mActivity.onCreate(null);
		objectGraph.inject(mActivity);
		this.contactTypeSpinner = (Spinner) mActivity
				.findViewById(R.id.contact_type);
		mActivity.onStart();
	}

	@Test
	public void spinnerShowContactTypes() {
		final View origin = mActivity.getWindow().getDecorView();
		assertOnScreen(origin, contactTypeSpinner);
		assertEquals(contactTypeSpinner.getAdapter().getCount(), 3);
	}
}
