package org.upsam.civicrm;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.beans.DataCivi;
import org.upsam.civicrm.dagger.di.CiviCRMSpiceRequest;
import org.upsam.civicrm.util.Utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Peticiones Rest
 * 
 * 
 * Entornos:
 * 
 * baseUri =
 * "http://www.proyectofinal.es/drupal7/sites/all/modules/civicrm/extern/rest.php?json=1&sequential=1"
 * ; key = "ad98d9cd2d3a364e3364b50f1db52c3c"; // heliohost ->
 * f56bad924425184e0dd5c562f953a87b api_key = "test";
 * 
 * @author Equipo 7 Universidad Pontificia de Salamanca v1.0
 * 
 */
public class CiviCRMAsyncRequest<RESULT> extends CiviCRMSpiceRequest<RESULT> {
	/**
	 * 
	 */
	public static enum ACTION {
		get, getsingle, create, getcount
	};

	/**
	 * 
	 */
	public static enum ENTITY {
		Phone, Contact, ContactType, Email, Address, GroupContact, EntityTag, Tag, CustomField, CustomValue, OptionValue, Activity, Contribution, Constant, Participant
	};

	/**
	 *
	 */
	public static enum METHOD {
		get, post
	};

	/**
	 * 
	 */
	final String uriReq;

	private METHOD method = METHOD.get;

	private final MultiValueMap<String, String> params;

	/**
	 * 
	 * @param context
	 * @param clazz
	 * @param action
	 * @param entity
	 * @param params
	 */
	public CiviCRMAsyncRequest(Context applicationContext, Class<RESULT> clazz,
			ACTION action, ENTITY entity, MultiValueMap<String, String> params) {
		super(clazz);
		this.params = params;
		this.uriReq = buildRequest(applicationContext, action, entity, true);
	}

	/**
	 * 
	 * @param applicationContext
	 * @param clazz
	 * @param action
	 * @param entity
	 * @param params
	 * @param sequential
	 */
	public CiviCRMAsyncRequest(Context applicationContext, Class<RESULT> clazz,
			ACTION action, ENTITY entity, MultiValueMap<String, String> params,
			boolean sequential) {
		super(clazz);
		this.params = params;
		this.uriReq = buildRequest(applicationContext, action, entity,
				sequential);
	}

	/**
	 * 
	 * @param applicationContext
	 * @param clazz
	 * @param action
	 * @param entity
	 * @param method
	 * @param params
	 */
	public CiviCRMAsyncRequest(Context applicationContext, Class<RESULT> clazz,
			ACTION action, ENTITY entity, METHOD method,
			MultiValueMap<String, String> params) {
		super(clazz);
		this.params = params;
		this.method = method;
		this.uriReq = buildRequest(applicationContext, action, entity, true);
	}

	/**
	 * Autenticacion POST en drupal
	 * 
	 * @param clazz
	 * @param datacivi
	 * @param method
	 */
	public CiviCRMAsyncRequest(Class<RESULT> clazz, DataCivi datacivi,
			METHOD method) {
		super(clazz);

		switch (method) {
		case post: {
			MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>(
					2);
			fields.add("username", datacivi.getUser_name());
			fields.add("password", datacivi.getPassword());
			this.params = fields;
			this.method = method;
			this.uriReq = buildRequestPostLogin(datacivi);
			break;
		}
		case get:
		default: {
			this.params = null;
			this.uriReq = buildRequestUfmatch(datacivi);
			break;
		}
		}
	}

	/**
	 * Utilizado la primera vez se autentica
	 * 
	 * @param clazz
	 * @param action
	 * @param entity
	 * @param datacivi
	 */
	public CiviCRMAsyncRequest(Class<RESULT> clazz, DataCivi datacivi) {
		super(clazz);
		this.params = null;
		this.uriReq = buildRequest(datacivi);
	}

	/**
	 * 
	 * @param context
	 * @param clazz
	 * @param action
	 * @param entity
	 */
	public CiviCRMAsyncRequest(Context applicationContext, Class<RESULT> clazz,
			ACTION action, ENTITY entity) {
		this(applicationContext, clazz, action, entity, null);
	}

	/**
	 * 
	 * @param applicationContext
	 * @param action
	 * @param entity
	 * @return
	 */
	private String buildRequest(Context applicationContext, ACTION action,
			ENTITY entity, boolean sequential) {
		Uri.Builder uriBuilder = Uri.parse(
				Utilities.getDataCivi(applicationContext, sequential)
						.getBase_url()).buildUpon();
		uriBuilder.appendQueryParameter("json", "1");
		uriBuilder.appendQueryParameter("entity", entity.name());
		uriBuilder.appendQueryParameter("action", action.name());
		uriBuilder.appendQueryParameter("key",
				Utilities.getDataCivi(applicationContext, sequential)
						.getSite_key());
		uriBuilder.appendQueryParameter("api_key",
				Utilities.getDataCivi(applicationContext, sequential)
						.getApi_key());
		StringBuilder uri = new StringBuilder(uriBuilder.build().toString());
		if (params != null && !params.isEmpty()
				&& METHOD.get.ordinal() == method.ordinal()) {
			Set<Entry<String, List<String>>> entrySet = params.entrySet();
			for (Entry<String, List<String>> entry : entrySet) {
				uri.append("&" + entry.getKey() + "="
						+ Uri.encode(entry.getValue().get(0), "+"));
			}
		}
		return uri.toString();
	}

	/**
	 * peticion post para autenticacion
	 * 
	 * @param applicationContext
	 * @return
	 */
	private String buildRequestPostLogin(DataCivi datacivi) {
		Uri.Builder uriBuilder = Uri.parse(
				Utilities.cleanUrl(datacivi.getBase_url())
						+ "/api/rest/user/login").buildUpon();

		StringBuilder uri = new StringBuilder(uriBuilder.build().toString());
		return uri.toString();
	}

	/**
	 * Llamada al autenticarse en civicrm
	 * 
	 * @param datacivi
	 * @return
	 */
	private String buildRequest(DataCivi datacivi) {
		Uri.Builder uriBuilder = Uri
				.parse(Utilities.cleanUrl(datacivi.getBase_url())
						+ "/sites/all/modules/civicrm/extern/rest.php?q=civicrm/login")
				.buildUpon();
		uriBuilder.appendQueryParameter("json", "1");
		uriBuilder.appendQueryParameter("name", datacivi.getUser_name());
		uriBuilder.appendQueryParameter("pass", datacivi.getPassword());
		uriBuilder.appendQueryParameter("key", datacivi.getSite_key());
		StringBuilder uri = new StringBuilder(uriBuilder.build().toString());
		return uri.toString();
	}

	/**
	 * construir url para obtener el contactid en la autenticacion
	 * 
	 * @param datacivi
	 * @return
	 */
	private String buildRequestUfmatch(DataCivi datacivi) {

		Uri.Builder uriBuilder = Uri.parse(
				Utilities.cleanUrl(datacivi.getBase_url())
						+ "/sites/all/modules/civicrm/extern/rest.php")
				.buildUpon();
		uriBuilder.appendQueryParameter("json", "1");
		uriBuilder.appendQueryParameter("sequential", "1");
		uriBuilder.appendQueryParameter("debug", "1");
		uriBuilder.appendQueryParameter("key", datacivi.getSite_key());
		uriBuilder.appendQueryParameter("api_key", datacivi.getApi_key());
		uriBuilder.appendQueryParameter("entity", "UFMatch");
		uriBuilder.appendQueryParameter("action", "getsingle");
		// uriBuilder.appendQueryParameter("uf_name", datacivi.getMail());
		StringBuilder uri = new StringBuilder(uriBuilder.build().toString());
		return uri.toString() + "&uf_name=" + datacivi.getMail();
	}

	/* (non-Javadoc)
	 * @see org.upsam.civicrm.CiviCRMSpiceRequest#createCacheKey()
	 */
	@Override
	public String createCacheKey() {
		return Integer.toString(this.uriReq.hashCode());
	}

	/* (non-Javadoc)
	 * @see org.upsam.civicrm.CiviCRMSpiceRequest#getUriReq()
	 */
	@Override
	public String getUriReq() {
		return this.uriReq;
	}

	/* (non-Javadoc)
	 * @see org.upsam.civicrm.CiviCRMSpiceRequest#loadDataFromNetwork()
	 */
	@Override
	public RESULT loadDataFromNetwork() throws Exception {
		Log.d("ContactTagsAndGroupsFragment", "loadDataFromNetwork() -> "
				+ this.uriReq);
		switch (method) {
		case post: {
			MultiValueMap<String, String> headers = getHeadersForPost();
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					params, headers);
			return getRestTemplate().exchange(this.uriReq, HttpMethod.POST,
					requestEntity, getResultType()).getBody();
			/*
			 * return getRestTemplate().postForObject(this.uriReq, params,
			 * getResultType());
			 */
		}
		case get:
		default:
			return getRestTemplate().getForObject(this.uriReq, getResultType());
		}

	}

	private MultiValueMap<String, String> getHeadersForPost() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		/*
		 * headers.set("Connection", "Close"). Hay un bug que se corrige con
		 * esta cabecera
		 * http://stackoverflow.com/questions/13182519/spring-rest-
		 * template-usage-causes-eofexception
		 */
		headers.set("Connection", "Close");
		return headers;
	}
}
