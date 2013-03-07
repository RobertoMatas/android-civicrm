package org.upsam.civicrm;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public abstract class AbstractAsyncFragment extends Fragment {

	private ProgressDialog progressDialog;

	private boolean destroyed = false;

	public AbstractAsyncFragment() {
		super();
	}


	public void showLoadingProgressDialog() {
		this.showProgressDialog("Loading. Please wait...");
	}

	public void showProgressDialog(CharSequence message) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this.getView().getContext(), ProgressDialog.STYLE_SPINNER);
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