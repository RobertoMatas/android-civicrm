package org.upsam.civicrm.test.fake;

import java.util.ArrayList;
import java.util.List;

import org.upsam.civicrm.CiviCRMSpiceRequest;
import org.upsam.civicrm.contact.model.address.ListAddresses;
import org.upsam.civicrm.contact.model.constant.Constant;
import org.upsam.civicrm.contact.model.contact.Contact;
import org.upsam.civicrm.contact.model.contact.ContactType;
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
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilder;

import android.content.Context;

public class CiviCRMContactRequestBuilderFake implements
		CiviCRMContactRequestBuilder {

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
		final String[] types = { "Individual", "Organization", "Household" };
		final List<ContactType> values = new ArrayList<ContactType>(3);
		ContactType ct = null;
		for (int i = 0; i < types.length; i++) {
			ct = new ContactType();
			ct.setId(i + 1);
			ct.setLabel(types[i]);
			ct.setName(types[i]);
			values.add(ct);
		}
		return new CiviCRMAsyncRequestFake<ListContactType>(
				ListContactType.class) {

			@Override
			public ListContactType loadDataFromNetwork() throws Exception {
				ListContactType result = new ListContactType();
				result.setValues(values);
				return result;
			}
		};
	}

	@Override
	public CiviCRMSpiceRequest<ListContacts> postRequestCreateContact(
			String contactTypeSelected, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListPhones> postRequestCreatePhone(
			int contactId, String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CiviCRMSpiceRequest<ListEmails> postRequestCreateEmail(
			int contactId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
