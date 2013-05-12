package org.upsam.civicrm.rest.req;

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

public interface CiviCRMContactRequestBuilder {

	CiviCRMSpiceRequest<Contact> requestContactById(int contactId);

	CiviCRMSpiceRequest<ListEmails> requestEmailsByContactId(int contactId);

	CiviCRMSpiceRequest<ListPhones> requestPhonesByContactId(Context ctx,
			int contactId);

	CiviCRMSpiceRequest<PreferredLanguage> requestCommunicationPreferencesByContactId(
			int contactId);

	CiviCRMSpiceRequest<ListTags> requestTagsByContactId(int contactId);

	CiviCRMSpiceRequest<Tag> requestTagById(int tagId);

	CiviCRMSpiceRequest<Constant> requestCountries();

	CiviCRMSpiceRequest<Constant> requestLocationTypes();

	CiviCRMSpiceRequest<ListAddresses> requestContactAddresses(int contactId);

	CiviCRMSpiceRequest<ListCustomFields> requestCustomFields();

	CiviCRMSpiceRequest<ListCustomValues> requestCustomValuesByContactId(
			int contactId);

	CiviCRMSpiceRequest<HumanReadableValue> requestHumanReadableValue(
			int optionGroupId, String value);

	CiviCRMSpiceRequest<ListContacts> requestListContact(int page, String type);

	CiviCRMSpiceRequest<ListContactType> requestContactTypes();

	CiviCRMSpiceRequest<ListContacts> postRequestCreateContact(
			String contactTypeSelected, String name);

	CiviCRMSpiceRequest<ListPhones> postRequestCreatePhone(int contactId,
			String phone);

	CiviCRMSpiceRequest<ListEmails> postRequestCreateEmail(int contactId,
			String email);
}
