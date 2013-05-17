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

public class CiviCRMContactRequestHelper {

	private static final int OFFSET = 25;

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

	public static CiviCRMAsyncRequest<ListGroups> requestGroupByContactId(
			Context ctx, int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactId));
		return new CiviCRMAsyncRequest<ListGroups>(ctx, ListGroups.class,
				ACTION.get, ENTITY.GroupContact, params);
	}

	public static CiviCRMAsyncRequest<ListTags> requestTagsByContactId(
			Context ctx, int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactId));
		return new CiviCRMAsyncRequest<ListTags>(ctx, ListTags.class,
				ACTION.get, ENTITY.EntityTag, params);
	}

	public static CiviCRMAsyncRequest<Tag> requestTagById(Context ctx, int tagId) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("id", Integer.toString(tagId));
		return new CiviCRMAsyncRequest<Tag>(ctx, Tag.class, ACTION.getsingle,
				ENTITY.Tag, params);
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

	public static CiviCRMAsyncRequest<ListCustomFields> requestCustomFields(
			Context ctx) {
		return new CiviCRMAsyncRequest<ListCustomFields>(ctx,
				ListCustomFields.class, ACTION.get, ENTITY.CustomField);
	}

	public static CiviCRMAsyncRequest<ListCustomValues> requestCustomValuesByContactId(
			Context ctx, int contactId) {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("entity_id", Long.toString(contactId));
		return new CiviCRMAsyncRequest<ListCustomValues>(ctx,
				ListCustomValues.class, ACTION.get, ENTITY.CustomValue, params);
	}

	public static CiviCRMAsyncRequest<HumanReadableValue> requestHumanReadableValue(
			Context ctx, int optionGroupId, String value) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				2);
		params.add("option_group_id", Integer.toString(optionGroupId));
		params.add("value", value);
		return new CiviCRMAsyncRequest<HumanReadableValue>(ctx,
				HumanReadableValue.class, ACTION.getsingle, ENTITY.OptionValue,
				params);
	}

	public static CiviCRMAsyncRequest<ListContacts> requestListContact(
			Context ctx, int page, String type) {
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

	public static CiviCRMSpiceRequest<ListContactType> requestContactTypes(
			Context ctx) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("is_reserved", "1");
		return new CiviCRMAsyncRequest<ListContactType>(ctx,
				ListContactType.class, ACTION.get, ENTITY.ContactType, params);
	}
}
