package org.upsam.civicrm.contact;

import org.upsam.civicrm.contact.model.ListContacts;

import android.net.Uri;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ContactsRequest extends SpringAndroidSpiceRequest<ListContacts> {
	/**
	 * 
	 */
	final String key = "b57107ccbf4d37a2d19e6c456c027add";
	/**
	 * 
	 */
	final String baseUri = "http://192.168.146.142/sites/all/modules/civicrm/extern/rest.php?json=1";

	/**
	 * 
	 */
	public ContactsRequest() {
		super(ListContacts.class);
	}

	@Override
	public ListContacts loadDataFromNetwork() throws Exception {
		Uri.Builder uriBuilder = Uri.parse(baseUri).buildUpon();
		uriBuilder.appendQueryParameter("entity", "Contact");
		uriBuilder.appendQueryParameter("action", "get");
		uriBuilder.appendQueryParameter("key", key);
		uriBuilder.appendQueryParameter("api_key", "test");

		String url = uriBuilder.build().toString();

		ListContacts contacts = getRestTemplate().getForObject(url, ListContacts.class);
		return contacts;
	}

	public String createCacheKey() {
		return "org.upsam.civicrm.contact.model.cache";
	}

}
