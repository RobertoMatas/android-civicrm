package org.upsam.civicrm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.upsam.civicrm.CiviCRMAsyncRequest.METHOD;
import org.upsam.civicrm.beans.DataCivi;
import org.upsam.civicrm.calendar.CalendarTabs;
import org.upsam.civicrm.charts.ReportSelectorActivity;
import org.upsam.civicrm.contact.list.ContactListTabs;
import org.upsam.civicrm.event.list.EventsListActivity;
import org.upsam.civicrm.login.ControlTesting;
import org.upsam.civicrm.login.FragmentHome;
import org.upsam.civicrm.login.FragmentLogin;
import org.upsam.civicrm.login.FragmentNoInternet;
import org.upsam.civicrm.login.model.Drupal;
import org.upsam.civicrm.login.model.Login;
import org.upsam.civicrm.login.model.Ufmatch;
import org.upsam.civicrm.preference.SetPreferenceActivity;
import org.upsam.civicrm.rest.CiviCRMAndroidPostSpiceService;
import org.upsam.civicrm.sync.ContactSyncService;
import org.upsam.civicrm.util.Utilities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
 * @author Equipo 7 Universidad Pontificia de Salamanca v1.0
 * 
 */

public class MainActivity extends FragmentActivity {

	private static final int REQUEST_PREFERENCES = 1234;
	private SharedPreferences prefs;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private EditText prefUrl;
	private EditText prefSiteKey;
	private EditText prefUser;
	private EditText prefPassword;
	private Map<String, String> mapaSaveStates;

	private DataCivi dataCiviResults;

	private SpiceManager contentManager = new SpiceManager(
			CiviCRMAndroidPostSpiceService.class);
	// private SpiceManager contentManager = new
	// SpiceManager(CiviCRMAndroidSpiceService.class);
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (Utilities.isOnline(this)) {
			if (Utilities.isInformationLoad(this)) {
				FragmentHome fragmentHome = new FragmentHome();
				fragmentTransaction.replace(android.R.id.content, fragmentHome);
			} else {
				FragmentLogin fragmentLogin = new FragmentLogin();
				fragmentTransaction
						.replace(android.R.id.content, fragmentLogin);
			}
			fragmentTransaction.commit();

			if (!Utilities.isInformationLoad(this)) {
				if (savedInstanceState != null) {
					mapaSaveStates = new HashMap<String, String>();
					if (savedInstanceState.getString("prefUrl") != null
							&& !"".equalsIgnoreCase(savedInstanceState
									.getString("prefUrl"))) {
						mapaSaveStates.put("prefUrl",
								savedInstanceState.getString("prefUrl"));
					}
					if (savedInstanceState.getString("prefSiteKey") != null
							&& !"".equalsIgnoreCase(savedInstanceState
									.getString("prefSiteKey"))) {
						mapaSaveStates.put("prefSiteKey",
								savedInstanceState.getString("prefSiteKey"));
					}
					if (savedInstanceState.getString("prefUser") != null
							&& !"".equalsIgnoreCase(savedInstanceState
									.getString("prefUser"))) {
						mapaSaveStates.put("prefUser",
								savedInstanceState.getString("prefUser"));
					}
					if (savedInstanceState.getString("prefPassword") != null
							&& !"".equalsIgnoreCase(savedInstanceState
									.getString("prefPassword"))) {
						mapaSaveStates.put("prefPassword",
								savedInstanceState.getString("prefPassword"));
					}

				}
			}
		} else {
			// sin internet
			FragmentNoInternet fragmentNoInternet = new FragmentNoInternet();
			fragmentTransaction.replace(android.R.id.content,
					fragmentNoInternet);
			fragmentTransaction.commit();
		}
	}

	@Override
	public void onStart() {
		contentManager.start(this);
		super.onStart();

		// syncronize contacts
		Intent intent = new Intent(this, ContactSyncService.class);
		startService(intent);

		if (mapaSaveStates != null && mapaSaveStates.size() > 0) {
			if (mapaSaveStates.get("prefUrl") != null
					&& !"".equalsIgnoreCase(mapaSaveStates.get("prefUrl"))) {
				prefUrl = (EditText) findViewById(R.id.prefURL);
				prefUrl.setText(mapaSaveStates.get("prefUrl"));
			}
			if (mapaSaveStates.get("prefSiteKey") != null
					&& !"".equalsIgnoreCase(mapaSaveStates.get("prefSiteKey"))) {
				prefSiteKey = (EditText) findViewById(R.id.prefKeySite);
				prefSiteKey.setText(mapaSaveStates.get("prefSiteKey"));
			}
			if (mapaSaveStates.get("prefUser") != null
					&& !"".equalsIgnoreCase(mapaSaveStates.get("prefUser"))) {
				prefUser = (EditText) findViewById(R.id.prefUser);
				prefUser.setText(mapaSaveStates.get("prefUser"));
			}
			if (mapaSaveStates.get("prefPassword") != null
					&& !"".equalsIgnoreCase(mapaSaveStates.get("prefPassword"))) {
				prefPassword = (EditText) findViewById(R.id.prefPassword);
				prefPassword.setText(mapaSaveStates.get("prefPassword"));
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_exit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exitOut: {
			ControlTesting ct = new ControlTesting(this);
			if (ct.isTesting()) {
				ct.setOffTesting();
				Editor editor = prefs.edit();
				editor.putString("PREF_URL", "");
				editor.putString("PREF_SITE_KEY", "");
				editor.putString("PREF_USER", "");
				editor.putString("PREF_PASSWORD", "");
				editor.putString("PREF_USER_KEY", "");
				editor.putString("PREF_API_KEY", "");
				editor.putString("PREF_MAIL", "");
				editor.putString("PREF_CONTACTID", "");
				editor.commit();
			}
			moveTaskToBack(Boolean.TRUE);
			this.finish();
		}
		}
		return super.onMenuItemSelected(featureId, item);
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
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

		prefUrl = (EditText) findViewById(R.id.prefURL);
		if (prefUrl != null && prefUrl.getText() != null
				&& !"".equalsIgnoreCase(prefUrl.getText().toString())) {
			outState.putString("prefUrl", prefUrl.getText().toString());
		}
		prefSiteKey = (EditText) findViewById(R.id.prefKeySite);
		if (prefSiteKey != null && prefSiteKey.getText() != null
				&& !"".equalsIgnoreCase(prefSiteKey.getText().toString())) {
			outState.putString("prefSiteKey", prefSiteKey.getText().toString());
		}
		prefUser = (EditText) findViewById(R.id.prefUser);
		if (prefUser != null && prefUser.getText() != null
				&& !"".equalsIgnoreCase(prefUser.getText().toString())) {
			outState.putString("prefUser", prefUser.getText().toString());
		}
		prefPassword = (EditText) findViewById(R.id.prefPassword);
		if (prefPassword != null && prefPassword.getText() != null
				&& !"".equalsIgnoreCase(prefPassword.getText().toString())) {
			outState.putString("prefPassword", prefPassword.getText()
					.toString());
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return Boolean.FALSE.booleanValue();
	}

	public void loadPreference(View view) {
		ControlTesting ct = new ControlTesting(this);
		if (ct.isTesting()) {
			Utilities.createCustomDialog(this,
					getString(R.string.pref_testingcontrol_label),
					getString(R.string.pref_testingcontrol_texto),
					getString(R.string.pref_testingcontrol_boton));
		} else {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SetPreferenceActivity.class);
			startActivityForResult(intent, REQUEST_PREFERENCES);
		}
	}

	public void acercaDe(View view) {
		Utilities.createCustomDialog(this, getString(R.string.acerca_de_label),
				getString(R.string.acerca_de_texto),
				getString(R.string.acerca_de_boton));
	}

	public void showContacts(View view) {
		Intent intent = new Intent(this, ContactListTabs.class);
		startActivity(intent);
	}

	public void showEvents(View view) {

		Intent intent = new Intent(this,EventsListActivity.class);
		startActivity(intent);

	}

	public void showActivities(View view) {
		Intent intent = new Intent(this, CalendarTabs.class);
		startActivity(intent);
	}

	public void showInformes(View view) {
		Intent intent = new Intent(this, ReportSelectorActivity.class);
		startActivity(intent);
	}

	public void enterToTesting(View view) {
		this.progressDialog = Utilities.showLoadingProgressDialog(
				this.progressDialog, this,
				getString(R.string.login_validando_msg));

		Editor editor = prefs.edit();

		editor.putString("PREF_URL", "http://www.proyectofinal.es/drupal7");
		editor.putString("PREF_SITE_KEY", "ad98d9cd2d3a364e3364b50f1db52c3c");
		editor.putString("PREF_USER", "admin");
		editor.putString("PREF_PASSWORD", "LnguZfXs");
		editor.putString("PREF_USER_KEY",
				"8b2ff408788c4baaa95147f4e16fd7a7ae96c49");
		editor.putString("PREF_API_KEY", "test");
		editor.putString("PREF_MAIL", "postmaster@proyectofinal.es");
		editor.putString("PREF_CONTACTID", "102");

		/**
		 * editor.putString("PREF_URL",
		 * "http://civicrm-upsam.heliohost.org/drupal");
		 * editor.putString("PREF_SITE_KEY",
		 * "f56bad924425184e0dd5c562f953a87b"); editor.putString("PREF_USER",
		 * "civicrm_admin"); editor.putString("PREF_PASSWORD", "4rf5tg7uj");
		 * editor.putString("PREF_USER_KEY",
		 * "f56bad924425184e0dd5c562f953a87b"); editor.putString("PREF_API_KEY",
		 * "test"); editor.putString("PREF_MAIL", "robertomatas@gmail.com");
		 * editor.putString("PREF_CONTACTID", "102");
		 */

		editor.commit();

		// controlar el usuario entro para pruebas
		ControlTesting ct = new ControlTesting(this);
		ct.setOnTesting();

		Utilities.dismissProgressDialog(progressDialog);

		// syncronize contacts
		Intent intent = new Intent(this, ContactSyncService.class);
		startService(intent);

		// redireccion a la home
		fragmentTransaction = fragmentManager.beginTransaction();
		FragmentHome fragmentHome = new FragmentHome();
		fragmentTransaction.replace(android.R.id.content, fragmentHome);
		fragmentTransaction.commit();
	}

	/**
	 * el usuario da boton acceder sistema y recogemos los datos a introducido y
	 * le autenticamos
	 * 
	 * @param view
	 */
	public void saveSettings(View view) {

		prefUrl = (EditText) findViewById(R.id.prefURL);
		prefSiteKey = (EditText) findViewById(R.id.prefKeySite);
		prefUser = (EditText) findViewById(R.id.prefUser);
		prefPassword = (EditText) findViewById(R.id.prefPassword);
		Boolean isValidationOk = Boolean.TRUE;

		if (prefUrl != null && prefUrl.getText() != null
				&& prefUrl.getText().toString().length() == 0) {
			prefUrl.setError(getString(R.string.login_error_url_obligatoria));
			isValidationOk = Boolean.FALSE;
		} else if (prefUrl != null && prefUrl.getText() != null
				&& !URLUtil.isValidUrl(prefUrl.getText().toString())) {
			prefUrl.setError(getString(R.string.login_error_url_novalida));
			isValidationOk = Boolean.FALSE;
		}

		if (prefSiteKey != null && prefSiteKey.getText() != null
				&& prefSiteKey.getText().toString().length() == 0) {
			prefSiteKey
					.setError(getString(R.string.login_error_sitekey_obligatoria));
			isValidationOk = Boolean.FALSE;
		}

		if (prefUser != null && prefUser.getText() != null
				&& prefUser.getText().toString().length() == 0) {
			prefUser.setError(getString(R.string.login_error_user_obligatoria));
			isValidationOk = Boolean.FALSE;
		}

		if (prefPassword != null && prefPassword.getText() != null
				&& prefPassword.getText().toString().length() == 0) {
			prefPassword
					.setError(getString(R.string.login_error_password_obligatoria));
			isValidationOk = Boolean.FALSE;
		}

		if (isValidationOk) {
			DataCivi datos = new DataCivi();
			datos.setBase_url(prefUrl.getText().toString());
			datos.setUser_name(prefUser.getText().toString());
			datos.setPassword(prefPassword.getText().toString());
			datos.setSite_key(prefSiteKey.getText().toString());
			executeRequests(datos);
		}

	}

	/**
	 * Peticiones rest
	 * 
	 * @param datos
	 */
	private void executeRequests(DataCivi datos) {
		this.progressDialog = Utilities.showLoadingProgressDialog(
				this.progressDialog, this,
				getString(R.string.login_validando_msg));

		dataCiviResults = new DataCivi();

		// la autenticacion es correcta vamos a drupal a por el mail
		CiviCRMAsyncRequest<Drupal> requestDrupal = new CiviCRMAsyncRequest<Drupal>(
				Drupal.class, datos, METHOD.post);
		contentManager.execute(requestDrupal, requestDrupal.createCacheKey(),
				DurationInMillis.NEVER, new AutenticacionDrupalListener());

		// autenticando en civi
		CiviCRMAsyncRequest<Login> requestCivicrm = new CiviCRMAsyncRequest<Login>(
				Login.class, datos);
		contentManager.execute(requestCivicrm, requestCivicrm.createCacheKey(),
				DurationInMillis.NEVER, new AutenticacionListener());

	}

	/**
	 * Tratar los errores autenticacion en civi
	 */
	private void tratarErrorRequest() {
		Utilities.dismissProgressDialog(progressDialog);

		Utilities.createCustomDialog(MainActivity.this,
				getString(R.string.login_validando_error_title),
				getString(R.string.login_validando_error_msg),
				getString(R.string.login_validando_error_button));

	}

	/**
	 * Tratar los errores autenticacion en drupal
	 */
	private void tratarErrorDrupalRequest() {
		Utilities.dismissProgressDialog(progressDialog);

		Utilities.createCustomDialog(MainActivity.this,
				getString(R.string.login_validando_error_title),
				getString(R.string.login_validando_error_drupal_msg),
				getString(R.string.login_validando_error_button));

	}

	/**
	 * Tratar los errores autenticacion en drupal
	 */
	private void tratarErrorUfMatchRequest() {
		Utilities.dismissProgressDialog(progressDialog);

		Utilities.createCustomDialog(MainActivity.this,
				getString(R.string.login_validando_error_title),
				getString(R.string.login_validando_error_ufmatch_msg),
				getString(R.string.login_validando_error_button));

	}

	/**
	 * Tratar el resultado de la peticion rest de autenticacion sobre civicrm
	 * 
	 * @param result
	 */
	public void tratarRequest(Login result) {
		if (result != null) {
			if (0 == result.getIdError()) {

				dataCiviResults.setBase_url(Utilities.cleanUrl(prefUrl
						.getText().toString()));
				dataCiviResults.setSite_key(prefSiteKey.getText().toString());
				dataCiviResults.setKey(result.getUserKey());
				dataCiviResults.setApi_key(result.getApiKey());
				dataCiviResults.setUser_name(prefUser.getText().toString());
				dataCiviResults.setPassword(prefPassword.getText().toString());

			} else {
				Utilities.dismissProgressDialog(progressDialog);

				Utilities.createCustomDialog(MainActivity.this,
						getString(R.string.login_validando_ko_title),
						getString(R.string.login_validando_ko_msg),
						getString(R.string.login_validando_ko_button));
			}
		} else {
			Utilities.dismissProgressDialog(progressDialog);

			Utilities.createCustomDialog(MainActivity.this,
					getString(R.string.login_validando_error_title),
					getString(R.string.login_validando_error_msg),
					getString(R.string.login_validando_error_button));
		}
	}

	/**
	 * Fin de la autenticacion cargamos los datos y entramos
	 * 
	 * @param result
	 */
	public void tratarFin(Ufmatch result) {

		if (result != null && result.getIdError() != 1) {
			Editor editor = prefs.edit();
			editor.putString("PREF_URL",
					Utilities.cleanUrl(prefUrl.getText().toString()));
			editor.putString("PREF_SITE_KEY", prefSiteKey.getText().toString());
			editor.putString("PREF_USER", prefUser.getText().toString());
			editor.putString("PREF_PASSWORD", prefPassword.getText().toString());
			editor.putString("PREF_USER_KEY", dataCiviResults.getKey());
			editor.putString("PREF_API_KEY", dataCiviResults.getApi_key());
			editor.putString("PREF_MAIL", dataCiviResults.getMail());
			editor.putString("PREF_CONTACTID", result.getContactid());
			editor.commit();

			Utilities.dismissProgressDialog(progressDialog);

			// redireccion a la home
			fragmentTransaction = fragmentManager.beginTransaction();
			FragmentHome fragmentHome = new FragmentHome();
			fragmentTransaction.replace(android.R.id.content, fragmentHome);
			fragmentTransaction.commit();
		} else {
			Utilities.dismissProgressDialog(progressDialog);

			Utilities.createCustomDialog(MainActivity.this,
					getString(R.string.login_validando_error_title),
					getString(R.string.login_validando_error_ufmatch_msg),
					getString(R.string.login_validando_error_button));
		}
	}

	/**
	 * Tratar el resultado de la peticion rest de autenticacion sobre drupal
	 * 
	 * @param result
	 */
	public void tratarRequest(Drupal result) {
		if (result != null) {
			if (result.getUser() != null
					&& StringUtils.isNotBlank(result.getUser().getMail())) {

				dataCiviResults.setMail(result.getUser().getMail());

				while (dataCiviResults.getApi_key() == null
						|| "".equalsIgnoreCase(dataCiviResults.getApi_key())) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ie) {
					}
				}
				// obtener contact id
				CiviCRMAsyncRequest<Ufmatch> requestUfMatch = new CiviCRMAsyncRequest<Ufmatch>(
						Ufmatch.class, dataCiviResults, METHOD.get);

				contentManager.execute(requestUfMatch,
						requestUfMatch.createCacheKey(),
						DurationInMillis.NEVER,
						new AutenticacionUfMatchListener());

			} else {
				Utilities.dismissProgressDialog(progressDialog);

				Utilities.createCustomDialog(MainActivity.this,
						getString(R.string.login_validando_ko_title),
						getString(R.string.login_validando_drupal_ko_msg),
						getString(R.string.login_validando_ko_button));
			}
		} else {
			Utilities.dismissProgressDialog(progressDialog);

			Utilities.createCustomDialog(MainActivity.this,
					getString(R.string.login_validando_error_title),
					getString(R.string.login_validando_error_drupal_msg),
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
		public void onRequestFailure(SpiceException spiceException) {
			tratarErrorRequest();
		}

		@Override
		public void onRequestSuccess(Login result) {
			tratarRequest(result);
		}
	}

	private class AutenticacionDrupalListener implements
			RequestListener<Drupal> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {

			if (spiceException != null
					&& spiceException instanceof com.octo.android.robospice.exception.NetworkException) {
				// org.springframework.web.client.ResourceAccessException: I/O
				// error: null; nested exception is java.io.EOFException
				// a pesar solucion dada en
				// https://groups.google.com/forum/?fromgroups#!topic/google-http-java-client/sGnxaoJDFy8
				// sigue pasando a veces. en caso ocurra haremos la peticion de
				// otra forma
				ejecutarAsyncTask();
			} else {
				tratarErrorDrupalRequest();
			}
		}

		@Override
		public void onRequestSuccess(Drupal result) {
			tratarRequest(result);
		}
	}

	private class AutenticacionUfMatchListener implements
			RequestListener<Ufmatch> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			tratarErrorUfMatchRequest();
		}

		@Override
		public void onRequestSuccess(Ufmatch result) {
			tratarFin(result);
		}
	}

	// solo se ejecuta en caso de errores eofexception
	private void ejecutarAsyncTask() {
		Map<String, String> mapaPrefs = new HashMap<String, String>();
		Uri.Builder uriBuilder = Uri.parse(
				Utilities.cleanUrl(prefUrl.getText().toString())
						+ "/api/rest/user/login").buildUpon();
		mapaPrefs.put("username", prefUser.getText().toString());
		mapaPrefs.put("password", prefPassword.getText().toString());
		new CallLoginTask(prefs, mapaPrefs).execute(uriBuilder.build()
				.toString());
	}

	// solo se ejecuta en caso de errores eofexception
	private class CallLoginTask extends AsyncTask<String, Void, String> {
		private Map<String, String> mapaPrefs;

		public CallLoginTask(SharedPreferences prefs,
				Map<String, String> mapaPrefs) {
			this.mapaPrefs = mapaPrefs;
		}

		@Override
		protected String doInBackground(String... uriBuilder) {
			StringBuilder builder = new StringBuilder();
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(uriBuilder[0]);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("username", mapaPrefs
						.get("username")));
				nameValuePairs.add(new BasicNameValuePair("password", mapaPrefs
						.get("password")));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = client.execute(httpPost);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line = null;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} else {
					Log.e(CallLoginTask.class.toString(),
							"Failed to call rest autentication");
				}
			} catch (Exception e) {
				Log.i(CallLoginTask.class.getName(),
						"ERROR OBTENIENDO EL LOGIN:" + e);
			}
			return builder.toString();
		}

		// despues llamada por ejemplo pintar los resultados
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject object = (JSONObject) new JSONTokener(result)
						.nextValue();

				if (StringUtils.isNotBlank(object.getString("sessid"))) {

					JSONObject objectUser = (JSONObject) object
							.getJSONObject("user");

					if (StringUtils.isNotBlank(objectUser.getString("mail"))) {
						dataCiviResults.setMail(objectUser.getString("mail"));

						// obtener contact id
						CiviCRMAsyncRequest<Ufmatch> requestUfMatch = new CiviCRMAsyncRequest<Ufmatch>(
								Ufmatch.class, dataCiviResults, METHOD.get);

						contentManager.execute(requestUfMatch,
								requestUfMatch.createCacheKey(),
								DurationInMillis.NEVER,
								new AutenticacionUfMatchListener());

					} else {
						tratarErrorDrupalRequest();
					}
				} else {
					tratarErrorDrupalRequest();
				}
			} catch (Exception e) {
				tratarErrorDrupalRequest();
			}

		}
	}

}
