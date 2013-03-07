package org.upsam.civicrm.contact.detail;

import org.upsam.civicrm.contact.model.telephone.ListPhones;

import android.net.Uri;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ContactPhonesRequest extends SpringAndroidSpiceRequest<ListPhones> {
	/**
	 * ID del contacto a solicitar
	 */
	private final String contactId;
	/**
	 * 
	 */
	final String key = "96783019a4bef4dd8541b176c828ff5f";
	/**
	 * 
	 */
	final String api_key = "81579f0a8cd7ba2e002267459d100476";
	/**
	 * 
	 */
	final String baseUri = "http://civicrm-upsam.btnhost.net/drupal/sites/all/modules/civicrm/extern/rest.php?json=1&sequential=1";

	public ContactPhonesRequest(long contactId) {
		super(ListPhones.class);
		this.contactId = Long.toString(contactId);
	}

	@Override
	public ListPhones loadDataFromNetwork() throws Exception {
		Uri.Builder uriBuilder = Uri.parse(baseUri).buildUpon();
		uriBuilder.appendQueryParameter("entity", "Phone");
		uriBuilder.appendQueryParameter("action", "get");
		uriBuilder.appendQueryParameter("key", key);
		uriBuilder.appendQueryParameter("api_key", api_key);
		uriBuilder.appendQueryParameter("contact_id", contactId);

		String url = uriBuilder.build().toString();
		ListPhones phones = getRestTemplate().getForObject(url, ListPhones.class);
		return phones;
	}

	public String createCacheKey() {
		return "ContactPhonesRequest.get_" + contactId;
	}
}
