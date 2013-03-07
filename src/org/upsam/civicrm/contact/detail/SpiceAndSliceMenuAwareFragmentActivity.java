package org.upsam.civicrm.contact.detail;

import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.app.ProgressDialog;

import com.octo.android.robospice.SpiceManager;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class SpiceAndSliceMenuAwareFragmentActivity extends SlidingFragmentActivity {

	protected SpiceManager contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);

	private ProgressDialog progressDialog;

	private boolean destroyed = false;

	public SpiceAndSliceMenuAwareFragmentActivity() {
		super();
	}
	
	@Override
	public void onStart() {
		contentManager.start(this);
		super.onStart();
	}

	@Override
	public void onStop() {
		contentManager.shouldStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.destroyed = true;
	}

	public void showLoadingProgressDialog() {
		this.showProgressDialog("Loading. Please wait...");
	}

	public void showProgressDialog(CharSequence message) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this);
			this.progressDialog.setIndeterminate(true);
		}

		this.progressDialog.setMessage(message);
		this.progressDialog.show();
	}

	public void dismissProgressDialog() {
		if (this.progressDialog != null && !this.destroyed) {
			this.progressDialog.dismiss();
		}
	}

}