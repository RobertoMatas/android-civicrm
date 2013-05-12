package org.upsam.civicrm.rest.req;

import javax.inject.Inject;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.CiviCRMAsyncRequest.METHOD;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.contact.model.address.ListAddresses;
import org.upsam.civicrm.contact.model.constant.Constant;
import org.upsam.civicrm.contact.model.contact.Contact;
import org.upsam.civicrm.contact.model.contact.ListContactType;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.contact.model.custom.HumanReadableValue;
import org.upsam.civicrm.contact.model.custom.ListCustomFields;
import org.upsam.civicrm.contact.model.custom.ListCustomValues;
import org.upsam.civicrm.contact.model.email.ListEmails;
import org.upsam.civicrm.contact.model.groups.ListGroups;
import org.upsam.civicrm.contact.model.lang.PreferredLanguage;
import org.upsam.civicrm.contact.model.tags.ListTags;
import org.upsam.civicrm.contact.model.tags.Tag;
import org.upsam.civicrm.contact.model.telephone.ListPhones;
import org.upsam.civicrm.dagger.di.CiviCRMSpiceRequest;

import android.content.Context;

public class CiviCRMContactRequestBuilderImpl implements
		CiviCRMContactRequestBuilder {

	private static final int OFFSET = 25;
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
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Integer.toString(contactId));
		return new CiviCRMAsyncRequest<Contact>(ctx, Contact.class,
				ACTION.getsingle, ENTITY.Contact, params);
	}

	@Override
	public CiviCRMSpiceRequest<ListEmails> requestEmailsByContactId(
			int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Integer.toString(contactId));
		return new CiviCRMAsyncRequest<ListEmails>(ctx, ListEmails.class,
				ACTION.get, ENTITY.Email, params);
	}

	@Override
	public CiviCRMSpiceRequest<ListPhones> requestPhonesByContactId(
			int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Integer.toString(contactId));
		return new CiviCRMAsyncRequest<ListPhones>(ctx, ListPhones.class,
				ACTION.get, ENTITY.Phone, params);
	}

	@Override
	public CiviCRMSpiceRequest<PreferredLanguage> requestCommunicationPreferencesByContactId(
			int contactId) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				2);
		params.add("contact_id", Integer.toString(contactId));
		params.add("return[preferred_language]", "1");
		return new CiviCRMAsyncRequest<PreferredLanguage>(ctx,
				PreferredLanguage.class, ACTION.getsingle, ENTITY.Contact,
				params);
	}

	@Override
	public CiviCRMSpiceRequest<ListTags> requestTagsByContactId(int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactId));
		return new CiviCRMAsyncRequest<ListTags>(ctx, ListTags.class,
				ACTION.get, ENTITY.EntityTag, params);
	}

	@Override
	public CiviCRMSpiceRequest<Tag> requestTagById(int tagId) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("id", Integer.toString(tagId));
		return new CiviCRMAsyncRequest<Tag>(ctx, Tag.class, ACTION.getsingle,
				ENTITY.Tag, params);
	}

	@Override
	public CiviCRMSpiceRequest<Constant> requestCountries() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("name", "country");
		return new CiviCRMAsyncRequest<Constant>(ctx, Constant.class,
				ACTION.get, ENTITY.Constant, params, false);
	}

	@Override
	public CiviCRMSpiceRequest<Constant> requestLocationTypes() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("name", "locationType");
		return new CiviCRMAsyncRequest<Constant>(ctx, Constant.class,
				ACTION.get, ENTITY.Constant, params, false);
	}

	@Override
	public CiviCRMSpiceRequest<ListAddresses> requestContactAddresses(
			int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactId));
		CiviCRMAsyncRequest<ListAddresses> request = new CiviCRMAsyncRequest<ListAddresses>(
				ctx, ListAddresses.class, ACTION.get, ENTITY.Address, params);
		return request;
	}

	@Override
	public CiviCRMSpiceRequest<ListCustomFields> requestCustomFields() {
		return new CiviCRMAsyncRequest<ListCustomFields>(ctx,
				ListCustomFields.class, ACTION.get, ENTITY.CustomField);
	}

	@Override
	public CiviCRMSpiceRequest<ListCustomValues> requestCustomValuesByContactId(
			int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("entity_id", Long.toString(contactId));
		return new CiviCRMAsyncRequest<ListCustomValues>(ctx,
				ListCustomValues.class, ACTION.get, ENTITY.CustomValue, params);
	}

	@Override
	public CiviCRMSpiceRequest<HumanReadableValue> requestHumanReadableValue(
			int optionGroupId, String value) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				2);
		params.add("option_group_id", Integer.toString(optionGroupId));
		params.add("value", value);
		return new CiviCRMAsyncRequest<HumanReadableValue>(ctx,
				HumanReadableValue.class, ACTION.getsingle, ENTITY.OptionValue,
				params);
	}

	@Override
	public CiviCRMSpiceRequest<ListContacts> requestListContact(int page,
			String type) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				5);
		if (page != 1) {
			params.add("offset", Integer.toString(((page - 1) * OFFSET)));
		}
		params.add("return[display_name]", "1");
		params.add("return[contact_type]", "1");
		params.add("return[contact_sub_type]", "1");
		if (type != null && !"".equals(type)) {
			params.add("contact_type", type);
		}
		return new CiviCRMAsyncRequest<ListContacts>(ctx, ListContacts.class,
				ACTION.get, ENTITY.Contact, params);
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

	@Override
	public CiviCRMSpiceRequest<ListGroups> requestGroupByContactId(int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactId));
		return new CiviCRMAsyncRequest<ListGroups>(ctx, ListGroups.class,
				ACTION.get, ENTITY.GroupContact, params);
	}

	@Override
	public CiviCRMSpiceRequest<ListActivtiesSummary> requestActivitiesForContact(
			int id) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", String.valueOf(id));
		return new CiviCRMAsyncRequest<ListActivtiesSummary>(ctx,
				ListActivtiesSummary.class, ACTION.get, ENTITY.Activity, params);
	}
}
