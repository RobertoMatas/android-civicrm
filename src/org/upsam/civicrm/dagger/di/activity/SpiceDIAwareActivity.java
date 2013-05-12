package org.upsam.civicrm.dagger.di;

import javax.inject.Inject;

import org.upsam.civicrm.dagger.utilities.ProgressDialogUtilities;
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilder;

import android.app.ProgressDialog;

import com.octo.android.robospice.SpiceManager;

public class SpiceDIAwareActivity extends BaseDIActivity {

	@Inject
	SpiceManager spiceManager;

	@Inject
	CiviCRMContactRequestBuilder requestBuilder;

	@Inject
	ProgressDialogUtilities progressDialogUtilities;

	protected ProgressDialog progressDialog;

	@Override
	protected void onStart() {
		spiceManager.start(this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}

	/**
	 * @return the spiceManager
	 */
	protected SpiceManager getSpiceManager() {
		return spiceManager;
	}

	/**
	 * @return the requestBuilder
	 */
	protected CiviCRMContactRequestBuilder getRequestBuilder() {
		return requestBuilder;
	}

	/**
	 * @return the progressDialog
	 */
	protected ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	/**
	 * @return the progressDialogUtilities
	 */
	protected ProgressDialogUtilities getProgressDialogUtilities() {
		return progressDialogUtilities;
	}

}
