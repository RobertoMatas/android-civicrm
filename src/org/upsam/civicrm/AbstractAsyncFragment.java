package org.upsam.civicrm;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;

/**
 * Clase padre de la que extenderan todos los fragmentos
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public abstract class AbstractAsyncFragment extends Fragment {

	protected ProgressDialog progressDialog;
	
	protected final SpiceManager contentManager;
	
	protected Context activityContext;

	protected boolean destroyed = Boolean.FALSE;

	public AbstractAsyncFragment(SpiceManager contentManager,Context activityContext) {
		super();
		this.contentManager = contentManager;
		this.activityContext = activityContext;
	}

}