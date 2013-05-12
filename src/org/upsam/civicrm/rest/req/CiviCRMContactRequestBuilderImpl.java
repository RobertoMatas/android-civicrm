package org.upsam.civicrm.rest.req;

import javax.inject.Inject;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.CiviCRMAsyncRequest.METHOD;
import org.upsam.civicrm.contact.model.address.ListAddresses;
import org.upsam.civicrm.contact.model.constant.Constant;
import org.upsam.civicrm.contact.model.contact.Contact;
import org.upsam.civicrm.contact.model.contact.ListContactType;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.contact.model.custom.HumanReadableValue;
import org.upsam.civicrm.contact.model.custom.ListCustomFields;
import org.upsam.civicrm.contact.model.custom.ListCustomValues;
import org.upsam.civicrm.contact.model.email.ListEmails;
import org.upsam.civicrm.contact.model.lang.PreferredLanguage;
import org.upsam.civicrm.contact.model.tags.ListTags;
import org.upsam.civicrm.contact.model.tags.Tag;
import org.upsam.civicrm.contact.model.telephone.ListPhones;
import org.upsam.civicrm.dagger.di.CiviCRMSpiceRequest;

import android.content.Context;

public class CiviCRMContactRequestBuilderImpl implements
		CiviCRMContactRequestBuilder {

	private final Context ctx;

	/**
	 * @param ctx
	 */
	@Inject
	public CiviCRMContactRequestBuilderImpl(Context ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public CiviCRMSpiceRequest<Contact> requestContactById(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListEmails> requestEmailsByContactId(
			int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListPhones> requestPhonesByContactId(
			Context ctx, int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<PreferredLanguage> requestCommunicationPreferencesByContactId(
			int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListTags> requestTagsByContactId(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<Tag> requestTagById(int tagId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<Constant> requestCountries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<Constant> requestLocationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListAddresses> requestContactAddresses(
			int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListCustomFields> requestCustomFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListCustomValues> requestCustomValuesByContactId(
			int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<HumanReadableValue> requestHumanReadableValue(
			int optionGroupId, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListContacts> requestListContact(int page,
			String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListContactType> requestContactTypes() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("is_reserved", "1");
		return new CiviCRMAsyncRequest<ListContactType>(ctx,
				ListContactType.class, ACTION.get, ENTITY.ContactType, params);
	}

	@Override
	public CiviCRMSpiceRequest<ListContacts> postRequestCreateContact(
			String contactTypeSelected, String name) {
		MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>(
				2);
		fields.add("contact_type", String.valueOf(contactTypeSelected));
		if ("Individual".equalsIgnoreCase(contactTypeSelected))
			fields.add("first_name", name);
		else
			fields.add(contactTypeSelected.toLowerCase() + "_name", name);
		return new CiviCRMAsyncRequest<ListContacts>(ctx, ListContacts.class,
				ACTION.create, ENTITY.Contact, METHOD.post, fields);
	}

	@Override
	public CiviCRMSpiceRequest<ListPhones> postRequestCreatePhone(
			int contactId, String phone) {
		MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>(
				2);
		fields.add("contact_id", String.valueOf(contactId));
		fields.add("phone", phone);
		return new CiviCRMAsyncRequest<ListPhones>(ctx, ListPhones.class,
				ACTION.create, ENTITY.Phone, METHOD.post, fields);
	}

	@Override
	public CiviCRMSpiceRequest<ListEmails> postRequestCreateEmail(
			int contactId, String email) {
		MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>(
				2);
		fields.add("contact_id", String.valueOf(contactId));
		fields.add("email", email);
		return new CiviCRMAsyncRequest<ListEmails>(ctx, ListEmails.class,
				ACTION.create, ENTITY.Email, METHOD.post, fields);
	}
}
