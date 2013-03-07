package org.upsam.civicrm.contact.detail;

import org.upsam.civicrm.contact.model.email.ListEmails;

import android.net.Uri;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ContactEmailsRequest extends SpringAndroidSpiceRequest<ListEmails> {
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
	
	public ContactEmailsRequest(long contactId) {
		super(ListEmails.class);
		this.contactId = Long.toString(contactId);
	}

	@Override
	public ListEmails loadDataFromNetwork() throws Exception {
		Uri.Builder uriBuilder = Uri.parse(baseUri).buildUpon();
		uriBuilder.appendQueryParameter("entity", "Email");
		uriBuilder.appendQueryParameter("action", "get");
		uriBuilder.appendQueryParameter("key", key);
		uriBuilder.appendQueryParameter("api_key", api_key);
		uriBuilder.appendQueryParameter("contact_id", contactId);

		String url = uriBuilder.build().toString();
		ListEmails emails = getRestTemplate().getForObject(url, ListEmails.class);
		return emails;
	}

	public String createCacheKey() {
		return "ContactEmailsRequest.get_" + contactId;
	}
}
