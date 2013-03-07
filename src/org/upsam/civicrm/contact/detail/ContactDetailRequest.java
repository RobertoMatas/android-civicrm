package org.upsam.civicrm.contact.detail;

import org.upsam.civicrm.contact.model.Contact;

import android.net.Uri;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ContactDetailRequest extends SpringAndroidSpiceRequest<Contact> {
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
	
	public ContactDetailRequest(long contactId) {
		super(Contact.class);
		this.contactId = Long.toString(contactId);
	}

	@Override
	public Contact loadDataFromNetwork() throws Exception {
		Uri.Builder uriBuilder = Uri.parse(baseUri).buildUpon();
		uriBuilder.appendQueryParameter("entity", "Contact");
		uriBuilder.appendQueryParameter("action", "getsingle");
		uriBuilder.appendQueryParameter("key", key);
		uriBuilder.appendQueryParameter("api_key", api_key);
		uriBuilder.appendQueryParameter("contact_id", contactId);

		String url = uriBuilder.build().toString();
		Contact contact = getRestTemplate().getForObject(url, Contact.class);
		return contact;
	}

	public String createCacheKey() {
		return "ContactDetailRequest.get_" + contactId;
	}
}
