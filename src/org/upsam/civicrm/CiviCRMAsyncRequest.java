package org.upsam.civicrm;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.net.Uri;
import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

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
	final String key = "ad98d9cd2d3a364e3364b50f1db52c3c"; // heliohost -> f56bad924425184e0dd5c562f953a87b
	/**
	 * 
	 */
	final String api_key = "test";
	/**
	 * 
	 */
	final String baseUri = "http://www.proyectofinal.es/drupal7/sites/all/modules/civicrm/extern/rest.php?json=1&sequential=1";
	/**
	 * 
	 */
	final String uriReq;
	
	/**
	 * 
	 * @param clazz
	 * @param action
	 * @param entity
	 * @param params
	 */
	public CiviCRMAsyncRequest(Class<RESULT> clazz, ACTION action, ENTITY entity, Map<String, String> params) {
		super(clazz);
		this.uriReq = buildRequest(action, entity, params);
	}
	
	/**
	 * 
	 * @param clazz
	 * @param action
	 * @param entity
	 */
	public CiviCRMAsyncRequest(Class<RESULT> clazz, ACTION action, ENTITY entity) {
		this(clazz, action, entity, null);
	}

	private String buildRequest(ACTION action, ENTITY entity, Map<String, String> params) {
		Uri.Builder uriBuilder = Uri.parse(baseUri).buildUpon();
		uriBuilder.appendQueryParameter("entity", entity.name());
		uriBuilder.appendQueryParameter("action", action.name());
		uriBuilder.appendQueryParameter("key", key);
		uriBuilder.appendQueryParameter("api_key", api_key);
		StringBuilder uri = new StringBuilder(uriBuilder.build().toString());
		if (params != null && ! params.isEmpty()) {
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				uri.append("&" + entry.getKey() + "=" + Uri.encode(entry.getValue(), "+"));
			}	
		}
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
