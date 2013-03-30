package org.upsam.civicrm.preference;



import org.upsam.civicrm.R;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Actividad de las preferencias
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class SetPreferenceActivity extends Activity
{	
	@Override
	 protected void onCreate(Bundle savedInstanceState) 
	{	 
	  super.onCreate(savedInstanceState);	
	  getWindow().setBackgroundDrawableResource(R.drawable.preferencias_background);
	  getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefsFragment()).commit();//CARGAMOS EL FRAGMENTO
	 }

	public static class PrefsFragment extends PreferenceFragment
	{		
		public static final String PREF_URL = "PREF_URL";
							
		@Override
		public void onCreate(Bundle savedInstanceState) 
		{
			 super.onCreate(savedInstanceState);			 
			 addPreferencesFromResource(R.xml.preferencias);       
		}	
		
		
	}

}
