package org.upsam.civicrm.dagger.utilities;

import android.app.ProgressDialog;

public interface ProgressDialogUtilities {

	ProgressDialog showProgressDialog(ProgressDialog progressDialog,
			String mensajeInformativo);

	void dismissProgressDialog(ProgressDialog progressDialog);
}
