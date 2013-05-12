package org.upsam.civicrm.dagger.di.fragment;

import javax.inject.Inject;

import org.upsam.civicrm.dagger.annotations.ForActivity;
import org.upsam.civicrm.dagger.di.activity.BaseDIActivity;
import org.upsam.civicrm.dagger.utilities.ProgressDialogUtilities;
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;

public class SpiceDIAwareFragment extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((BaseDIActivity) getActivity()).inject(this);
	}

	@Inject
	SpiceManager spiceManager;

	@Inject
	CiviCRMContactRequestBuilder requestBuilder;

	@Inject
	ProgressDialogUtilities progressDialogUtilities;

	@Inject
	@ForActivity
	Context activityContext;

	protected ProgressDialog progressDialog;

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

	/**
	 * @return the activityContext
	 */
	protected Context getActivityContext() {
		return activityContext;
	}

}
