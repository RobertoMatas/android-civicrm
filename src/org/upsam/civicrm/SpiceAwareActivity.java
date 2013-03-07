package org.upsam.civicrm;

import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import com.octo.android.robospice.SpiceManager;

import android.app.Activity;
import android.app.ProgressDialog;

public class SpiceAwareActivity extends Activity {

	protected SpiceManager contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);

	private ProgressDialog progressDialog;

	private boolean destroyed = false;

	public SpiceAwareActivity() {
		super();
	}

	@Override
	protected void onStart() {
		contentManager.start(this);
		super.onStart();
	}

	@Override
	protected void onStop() {
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