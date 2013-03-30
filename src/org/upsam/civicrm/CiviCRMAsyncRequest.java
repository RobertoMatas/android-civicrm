package org.upsam.civicrm;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.upsam.civicrm.beans.DataCivi;
import org.upsam.civicrm.util.Utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * Peticiones Rest
 * 
 * 
 * Entornos:
 * 
 * baseUri = "http://www.proyectofinal.es/drupal7/sites/all/modules/civicrm/extern/rest.php?json=1&sequential=1";
 * key = "ad98d9cd2d3a364e3364b50f1db52c3c"; // heliohost -> f56bad924425184e0dd5c562f953a87b
 * api_key = "test";
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class CiviCRMAsyncRequest<RESULT> extends SpringAndroidSpiceRequest<RESULT> {
	/**
	 * 
	 */
	public static enum ACTION {get, getsingle};
	/**
	 * 
	 */
	public static enum ENTITY {Phone, Contact, Email, Address, GroupContact, EntityTag, Tag, CustomField, CustomValue, OptionValue};
	
	/**
	 * 
	 */
	final String uriReq;
	
		
	/**
	 * 
	 * @param context
	 * @param clazz
	 * @param action
	 * @param entity
	 * @param params
	 */
	public CiviCRMAsyncRequest(Context applicationContext,Class<RESULT> clazz, ACTION action, ENTITY entity, Map<String, String> params) {
		super(clazz);
		this.uriReq = buildRequest(applicationContext,action, entity, params);
	}
	
	/**
	 * Utilizado la primera vez se autentica
	 * @param clazz
	 * @param action
	 * @param entity
	 * @param datacivi
	 */
	public CiviCRMAsyncRequest(Class<RESULT> clazz, DataCivi datacivi) {
		super(clazz);
		this.uriReq = buildRequest(datacivi);
	}
	
	/**
	 * 
	 * @param context
	 * @param clazz
	 * @param action
	 * @param entity
	 */
	public CiviCRMAsyncRequest(Context applicationContext,Class<RESULT> clazz, ACTION action, ENTITY entity) {
		this(applicationContext,clazz, action, entity, null);
	}

	private String buildRequest(Context applicationContext,ACTION action, ENTITY entity, Map<String, String> params) {
		Uri.Builder uriBuilder = Uri.parse(Utilities.getDataCivi(applicationContext).getBase_url()).buildUpon();
		uriBuilder.appendQueryParameter("entity", entity.name());
		uriBuilder.appendQueryParameter("action", action.name());
		uriBuilder.appendQueryParameter("key", Utilities.getDataCivi(applicationContext).getSite_key());
		uriBuilder.appendQueryParameter("api_key", Utilities.getDataCivi(applicationContext).getApi_key());
		StringBuilder uri = new StringBuilder(uriBuilder.build().toString());
		if (params != null && ! params.isEmpty()) {
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				uri.append("&" + entry.getKey() + "=" + Uri.encode(entry.getValue(), "+"));
			}	
		}
		return uri.toString();
	}

	/**
	 * Llamada al autenticarse
	 * @param datacivi
	 * @return
	 */
	private String buildRequest(DataCivi datacivi) {
		Uri.Builder uriBuilder = Uri.parse(Utilities.cleanUrl(datacivi.getBase_url())+"/sites/all/modules/civicrm/extern/rest.php?q=civicrm/login").buildUpon();
		uriBuilder.appendQueryParameter("json", "1");
		uriBuilder.appendQueryParameter("name", datacivi.getUser_name());
		uriBuilder.appendQueryParameter("pass", datacivi.getPassword());
		uriBuilder.appendQueryParameter("key", datacivi.getSite_key());
		StringBuilder uri = new StringBuilder(uriBuilder.build().toString());		
		return uri.toString();
	}
		
	public String createCacheKey() {
		return Integer.toString(this.uriReq.hashCode());
	}

	public String getUriReq() {
		return this.uriReq;
	}

	@Override
	public RESULT loadDataFromNetwork() throws Exception {
		Log.d("ContactTagsAndGroupsFragment", "loadDataFromNetwork() -> " + this.uriReq);
		return getRestTemplate().getForObject(this.uriReq, getResultType());
	}
}
