package org.upsam.civicrm;

import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.app.Activity;
import android.app.ProgressDialog;

import com.octo.android.robospice.SpiceManager;

/**
 * Clase padre de todas las actividades
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class SpiceAwareActivity extends Activity {

	protected SpiceManager contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);

	protected ProgressDialog progressDialog;

	protected boolean destroyed = Boolean.FALSE;

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
		this.destroyed = Boolean.TRUE;
	}
	
}