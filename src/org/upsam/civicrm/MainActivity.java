package org.upsam.civicrm;

import java.util.HashMap;
import java.util.Map;

import org.upsam.civicrm.beans.DataCivi;
import org.upsam.civicrm.charts.ActivityResolutionColumnChart;
import org.upsam.civicrm.contact.list.ContactListTabs;
import org.upsam.civicrm.login.FragmentHome;
import org.upsam.civicrm.login.FragmentLogin;
import org.upsam.civicrm.login.model.Login;
import org.upsam.civicrm.preference.SetPreferenceActivity;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;
import org.upsam.civicrm.util.Utilities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Actividad de entrada a la aplicacion
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */

public class MainActivity extends FragmentActivity  {

    private static final int REQUEST_PREFERENCES = 1234;		
	private SharedPreferences prefs;	
	private FragmentManager fragmentManager;	
	private FragmentTransaction fragmentTransaction;		
	private EditText prefUrl;
	private EditText prefSiteKey;
	private EditText prefUser;
	private EditText prefPassword;			
	private Map<String,String> mapaSaveStates;	
			
	private SpiceManager contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);
	private ProgressDialog progressDialog;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		   super.onCreate(savedInstanceState);
		   fragmentManager = getSupportFragmentManager();		   
		   fragmentTransaction =fragmentManager.beginTransaction();		   
		   prefs = PreferenceManager.getDefaultSharedPreferences(this);
			
			if (Utilities.isInformationLoad(this))
			{			   				
				FragmentHome fragmentHome = new FragmentHome();				
				fragmentTransaction.replace(
				android.R.id.content, fragmentHome);
			}
			else
			{											
				FragmentLogin fragmentLogin = new FragmentLogin();
				fragmentTransaction.replace(
				android.R.id.content, fragmentLogin);			
			}
			fragmentTransaction.commit();
			
			if (!Utilities.isInformationLoad(this))
			{
				if(savedInstanceState!=null)
				{
					mapaSaveStates = new HashMap<String, String>();
					if(savedInstanceState.getString("prefUrl")!=null && !"".equalsIgnoreCase(savedInstanceState.getString("prefUrl")))
					{
						mapaSaveStates.put("prefUrl", savedInstanceState.getString("prefUrl"));						
					}
					if(savedInstanceState.getString("prefSiteKey")!=null && !"".equalsIgnoreCase(savedInstanceState.getString("prefSiteKey")))
					{
						mapaSaveStates.put("prefSiteKey", savedInstanceState.getString("prefSiteKey"));	
					}
					if(savedInstanceState.getString("prefUser")!=null && !"".equalsIgnoreCase(savedInstanceState.getString("prefUser")))
					{
						mapaSaveStates.put("prefUser", savedInstanceState.getString("prefUser"));	
					}
					if(savedInstanceState.getString("prefPassword")!=null && !"".equalsIgnoreCase(savedInstanceState.getString("prefPassword")))
					{
						mapaSaveStates.put("prefPassword", savedInstanceState.getString("prefPassword"));	
					}
										
				}
			}
	}

	@Override
	public void onStart() {
		contentManager.start(this);
		super.onStart();
		if(mapaSaveStates!=null && mapaSaveStates.size()>0)
		{
			if(mapaSaveStates.get("prefUrl")!=null && !"".equalsIgnoreCase( mapaSaveStates.get("prefUrl")))
			{			
				prefUrl = (EditText)findViewById(R.id.prefURL);
				prefUrl.setText(mapaSaveStates.get("prefUrl"));
			}
			if(mapaSaveStates.get("prefSiteKey")!=null && !"".equalsIgnoreCase( mapaSaveStates.get("prefSiteKey")))
			{
				prefSiteKey = (EditText)findViewById(R.id.prefKeySite);
				prefSiteKey.setText(mapaSaveStates.get("prefSiteKey"));
			}
			if(mapaSaveStates.get("prefUser")!=null && !"".equalsIgnoreCase( mapaSaveStates.get("prefUser")))
			{
				prefUser = (EditText)findViewById(R.id.prefUser);
				prefUser.setText(mapaSaveStates.get("prefUser"));
			}
			if(mapaSaveStates.get("prefPassword")!=null && !"".equalsIgnoreCase( mapaSaveStates.get("prefPassword")))
			{
				prefPassword = (EditText)findViewById(R.id.prefPassword);
				prefPassword.setText(mapaSaveStates.get("prefPassword"));
			}
						
		}
	}
	
	@Override
	public void onStop() {
		contentManager.shouldStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		
		super.onSaveInstanceState(outState);
				
		
		prefUrl = (EditText)findViewById(R.id.prefURL);
		if(prefUrl!=null && prefUrl.getText()!=null && !"".equalsIgnoreCase(prefUrl.getText().toString()))
		{
		  outState.putString("prefUrl", prefUrl.getText().toString());
		}
		prefSiteKey = (EditText)findViewById(R.id.prefKeySite);
		if(prefSiteKey!=null && prefSiteKey.getText()!=null  && !"".equalsIgnoreCase(prefSiteKey.getText().toString()))
		{
		  outState.putString("prefSiteKey", prefSiteKey.getText().toString());
		}
		prefUser = (EditText)findViewById(R.id.prefUser);
		if(prefUser!=null && prefUser.getText()!=null  && !"".equalsIgnoreCase(prefUser.getText().toString()))
		{
		  outState.putString("prefUser", prefUser.getText().toString());
		}
		prefPassword = (EditText)findViewById(R.id.prefPassword);
		if(prefPassword!=null && prefPassword.getText()!=null  && !"".equalsIgnoreCase(prefPassword.getText().toString()))
		{
		  outState.putString("prefPassword", prefPassword.getText().toString());
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		return Boolean.FALSE.booleanValue();
	}
	
	public void loadPreference(View view)
	{
		    Intent intent = new Intent();
	        intent.setClass(MainActivity.this, SetPreferenceActivity.class);
	        startActivityForResult(intent, REQUEST_PREFERENCES); 
	}
	
	public void acercaDe(View view)
	{
		Utilities.createCustomDialog(this, getString(R.string.acerca_de_label), getString(R.string.acerca_de_texto), getString(R.string.acerca_de_boton));
	}

	public void showContacts(View view) 
	{
		Intent intent = new Intent(this, ContactListTabs.class);
		startActivity(intent);
	}	
	
	public void showEvents(View view) {
		Toast.makeText(getApplicationContext(),"En construcción EVENTOS", Toast.LENGTH_LONG).show();
	}
	
	public void showActivities(View view) {
		Toast.makeText(getApplicationContext(),"En construcción ACTIVIDADES", Toast.LENGTH_LONG).show();
	}
	
	public void showInformes(View view) {
		Intent intent = new Intent(this, ActivityResolutionColumnChart.class);
		startActivity(intent);
	}	
	
	/**
	 * el usuario da boton acceder sistema y recogemos los datos a introducido y le autenticamos
	 * @param view
	 */
	public void saveSettings(View view) 
	{
		
		prefUrl = (EditText)findViewById(R.id.prefURL);
		prefSiteKey = (EditText)findViewById(R.id.prefKeySite);
		prefUser = (EditText)findViewById(R.id.prefUser);
		prefPassword = (EditText)findViewById(R.id.prefPassword);
		Boolean isValidationOk = Boolean.TRUE;
		
	    if(prefUrl!=null && prefUrl.getText()!=null && prefUrl.getText().toString().length()==0)
		{
			prefUrl.setError(getString(R.string.login_error_url_obligatoria));
			isValidationOk = Boolean.FALSE;
		}
	    else if(prefUrl!=null && prefUrl.getText()!=null && !URLUtil.isValidUrl(prefUrl.getText().toString()))
		{
			prefUrl.setError(getString(R.string.login_error_url_novalida));
			isValidationOk = Boolean.FALSE;
		}
		
	    if(prefSiteKey!=null && prefSiteKey.getText()!=null && prefSiteKey.getText().toString().length()==0)
	    {
	    	prefSiteKey.setError(getString(R.string.login_error_sitekey_obligatoria));
	    	isValidationOk = Boolean.FALSE;
	    }
	    
	    if(prefUser!=null && prefUser.getText()!=null && prefUser.getText().toString().length()==0)
	    {
	    	prefUser.setError(getString(R.string.login_error_user_obligatoria));
	    	isValidationOk = Boolean.FALSE;
	    }
	    
	    if(prefPassword!=null && prefPassword.getText()!=null && prefPassword.getText().toString().length()==0)
	    {
	    	prefPassword.setError(getString(R.string.login_error_password_obligatoria));
	    	isValidationOk = Boolean.FALSE;
	    }
	                   	
	    if(isValidationOk)
	    {	    	
	    	DataCivi datos = new DataCivi();
	    	datos.setBase_url(prefUrl.getText().toString());
	    	datos.setUser_name(prefUser.getText().toString());
	    	datos.setPassword(prefPassword.getText().toString());
	    	datos.setSite_key(prefSiteKey.getText().toString());
	    	executeRequests(datos);	    		    		    	
	    }
    	        	        	                 
	}		
	
	/**
	 * Peticion rest - autenticacion en civicrm
	 * @param datos
	 */
	private void executeRequests(DataCivi datos) 
	{
		this.progressDialog = Utilities.showLoadingProgressDialog(this.progressDialog,this,getString(R.string.login_validando_msg));
		CiviCRMAsyncRequest<Login> request = new CiviCRMAsyncRequest<Login>(Login.class,datos);
		contentManager.execute(request, request.createCacheKey(), DurationInMillis.NEVER, new AutenticacionListener());		
	}
	
	/**
	 * Tratar los errores de la peticion rest
	 */
	private void tratarErrorRequest()
	{
		Utilities.dismissProgressDialog(progressDialog);
		
		Utilities.createCustomDialog(MainActivity.this, 
		            getString(R.string.login_validando_error_title), 
		  			getString(R.string.login_validando_error_msg), 
		  			getString(R.string.login_validando_error_button));
		 		 
	}
	
	/**
	 * Tratar el resultado de la peticion rest
	 * @param result
	 */
	public void tratarRequest(Login result)
	{
		if(result!=null)
		{
			 if(0 == result.getIdError())
			 {					
				  Editor editor = prefs.edit();
				  editor.putString("PREF_URL", Utilities.cleanUrl(prefUrl.getText().toString()));
				  editor.putString("PREF_SITE_KEY", prefSiteKey.getText().toString());
				  editor.putString("PREF_USER", prefUser.getText().toString());
				  editor.putString("PREF_PASSWORD", prefPassword.getText().toString());
				  editor.putString("PREF_USER_KEY", result.getUserKey());						  
				  editor.putString("PREF_API_KEY", result.getApiKey());
				  editor.commit();	
				  
				  Utilities.dismissProgressDialog(progressDialog);
				  
				  //redireccion a la home
				  fragmentTransaction =fragmentManager.beginTransaction();
				  FragmentHome fragmentHome = new FragmentHome();				
				  fragmentTransaction.replace(android.R.id.content, fragmentHome);
				  fragmentTransaction.commit();		
				  							
			  }
			  else 
			  {
				  Utilities.dismissProgressDialog(progressDialog);
				  
				  Utilities.createCustomDialog(MainActivity.this, 
						            getString(R.string.login_validando_ko_title), 
						  			getString(R.string.login_validando_ko_msg), 
			  			  			getString(R.string.login_validando_ko_button));
			  }
		}
		else
		{
			Utilities.dismissProgressDialog(progressDialog);
			
			Utilities.createCustomDialog(MainActivity.this, 
		            getString(R.string.login_validando_error_title), 
		  			getString(R.string.login_validando_error_msg), 
		  			getString(R.string.login_validando_error_button));
		}						
	}
	
	/**
	 * Listener recoge la llamada al servicio rest de autenticacion
	 * 
	 *
	 */
	private class AutenticacionListener implements RequestListener<Login> {

		@Override
		public void onRequestFailure(SpiceException spiceException) 
		{						
			tratarErrorRequest();
		}

		@Override
		public void onRequestSuccess(Login result) 
		{						
			tratarRequest(result);		
		}
	}	
	
}