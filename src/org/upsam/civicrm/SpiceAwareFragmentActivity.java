package org.upsam.civicrm;

import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

import com.octo.android.robospice.SpiceManager;

/**
 * Clase padre de todas las actividades Fragment
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class SpiceAwareFragmentActivity extends FragmentActivity {

	protected SpiceManager contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);

	protected ProgressDialog progressDialog;

	protected boolean destroyed = Boolean.FALSE;

	public SpiceAwareFragmentActivity() {
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
		this.destroyed = Boolean.TRUE;;
	}
}