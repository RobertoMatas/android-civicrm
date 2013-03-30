package org.upsam.civicrm.contact.detail;

import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.app.ProgressDialog;

import com.octo.android.robospice.SpiceManager;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class SpiceAndSliceMenuAwareFragmentActivity extends SlidingFragmentActivity {

	protected SpiceManager contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);

	protected ProgressDialog progressDialog;

	protected boolean destroyed = Boolean.FALSE;

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
		this.destroyed = Boolean.TRUE;
	}

}