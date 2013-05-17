package org.upsam.civicrm.dagger.utilities;

import org.upsam.civicrm.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

public class ProgressDialogUtilitiesImpl implements ProgressDialogUtilities {

	final Context ctx;
	
	/**
	 * 
	 * @param ctx
	 */
	public ProgressDialogUtilitiesImpl(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public ProgressDialog showProgressDialog(ProgressDialog progressDialog,
			String mensajeInformativo) {
		if (progressDialog == null) {
			progressDialog = createCustomProgressDialog(mensajeInformativo);
		}
		progressDialog.show();

		return progressDialog;
	}

	private ProgressDialog createCustomProgressDialog(String body) {
		ProgressDialog progressDialog = new ProgressDialog(ctx,
				R.style.progressDialogTheme);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(body);
		progressDialog.setIndeterminate(Boolean.TRUE);
		progressDialog.setCancelable(false);
		return progressDialog;
	}

	@Override
	public void dismissProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

	}

}
