package org.upsam.civicrm.login;

import org.upsam.civicrm.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Carga de la pantalla del login
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class FragmentLogin extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.fragmentlogin, container,false);				
	}			
}
