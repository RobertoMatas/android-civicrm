package org.upsam.civicrm.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.contact.model.address.ListAddresses;
import org.upsam.civicrm.contact.model.constant.Constant;
import org.upsam.civicrm.contact.model.contact.Contact;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.email.ListEmails;
import org.upsam.civicrm.contact.model.lang.PreferredLanguage;
import org.upsam.civicrm.contact.model.telephone.ListPhones;

import android.content.Context;

public class CiviCRMContactRequestHelper {

	public static CiviCRMAsyncRequest<Contact> requestContactById(Context ctx,
			int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Integer.toString(contactId));
		return new CiviCRMAsyncRequest<Contact>(ctx, Contact.class,
				ACTION.getsingle, ENTITY.Contact, params);
	}

	public static CiviCRMAsyncRequest<ListEmails> requestEmailsByContactId(
			Context ctx, int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Integer.toString(contactId));
		return new CiviCRMAsyncRequest<ListEmails>(ctx, ListEmails.class,
				ACTION.get, ENTITY.Email, params);
	}

	public static CiviCRMAsyncRequest<ListPhones> requestPhonesByContactId(
			Context ctx, int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Integer.toString(contactId));
		return new CiviCRMAsyncRequest<ListPhones>(ctx, ListPhones.class,
				ACTION.get, ENTITY.Phone, params);
	}

	public static CiviCRMAsyncRequest<PreferredLanguage> requestCommunicationPreferencesByContactId(
			Context ctx, int contactId) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				2);
		params.add("contact_id", Integer.toString(contactId));
		params.add("return[preferred_language]", "1");
		return new CiviCRMAsyncRequest<PreferredLanguage>(ctx,
				PreferredLanguage.class, ACTION.getsingle, ENTITY.Contact,
				params);
	}

	public static CiviCRMAsyncRequest<Constant> requestCountries(Context ctx) {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("name", "country");
		return new CiviCRMAsyncRequest<Constant>(ctx, Constant.class,
				ACTION.get, ENTITY.Constant, params, false);
	}

	public static CiviCRMAsyncRequest<Constant> requestLocationTypes(Context ctx) {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("name", "locationType");
		return new CiviCRMAsyncRequest<Constant>(ctx, Constant.class,
				ACTION.get, ENTITY.Constant, params, false);
	}

	public static CiviCRMAsyncRequest<ListAddresses> requestContactAddresses(
			Context ctx, ContactSummary contactSummary) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactSummary.getId()));
		CiviCRMAsyncRequest<ListAddresses> request = new CiviCRMAsyncRequest<ListAddresses>(
				ctx, ListAddresses.class, ACTION.get, ENTITY.Address, params);
		return request;
	}
}
