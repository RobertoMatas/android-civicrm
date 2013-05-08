package org.upsam.civicrm.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * control del acceso para pruebas
 * 
 * @author Equipo 7 Universidad Pontificia de Salamanca v1.0
 * 
 */
public class ControlTesting  {
	
	private Context context;
	private SharedPreferences prefs;
	
	private String namePreference = "PREF_TESTING";
	private String valuePreference = "TESTING";
	private String valueSinPreference = "SINTESTING";
	

	public ControlTesting(Context context) 
	{
		this.context = context;
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
	}

	
	public void setOffTesting()
	{
		Editor editor = prefs.edit();
		editor.putString(namePreference,valueSinPreference);
		editor.commit();	
	}
	
	public void setOnTesting()
	{						
		Editor editor = prefs.edit();
		editor.putString(namePreference,valuePreference);
		editor.commit();			
	}
		
    public boolean isTesting()
    {
    	String value = prefs.getString(namePreference, null);
    	if(valuePreference.equalsIgnoreCase(value))
    	{
    		return Boolean.TRUE;
    	}
    	else
    	{
    		return Boolean.FALSE;
    	}
    }
	

}
