package org.upsam.civicrm.contact.add;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.CiviCRMAsyncRequest.METHOD;
import org.upsam.civicrm.R;
import org.upsam.civicrm.SpiceAwareActivity;
import org.upsam.civicrm.contact.model.contact.ListContactType;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.contact.model.email.ListEmails;
import org.upsam.civicrm.contact.model.telephone.ListPhones;
import org.upsam.civicrm.util.Utilities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class AddContactActivity extends SpiceAwareActivity {

	private static final String emailRegExp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
			+ "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	/**
	 * 
	 */
	private TextView contactNameTextView;
	/**
	 * 
	 */
	private TextView contactPhoneTextView;
	/**
	 * 
	 */
	private TextView contactEmailTextView;
	/**
	 * 
	 */
	private Spinner contactTypeSpinner;
	/**
	 * 
	 */
	private ContactTypesAdapter spinnerAdapter;
	/**
	 * 
	 */
	private Button button;
	/**
	 * 
	 */
	private String contactTypeSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		this.contactNameTextView = (TextView) findViewById(R.id.contact_name);
		this.contactPhoneTextView = (TextView) findViewById(R.id.contact_phone);
		this.contactEmailTextView = (TextView) findViewById(R.id.contact_email);
		this.button = (Button) findViewById(R.id.add_contact_button);
		this.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String name = contactNameTextView.getText().toString();
				final String phone = contactPhoneTextView.getText().toString();
				final String mail = contactEmailTextView.getText().toString();
				boolean isValid = validate(name, phone, mail);
				if (isValid) {
					createContact(name);
				}
			}

			private boolean validate(final String name, final String phone,
					final String mail) {
				if (StringUtils.isNotBlank(name)) {
					boolean valid = true;
					if (StringUtils.isNotBlank(mail)
							&& !mail.matches(emailRegExp)) {
						contactEmailTextView
								.setError(getString(R.string.mail_no_valido));
						valid = false;
					}
					if (StringUtils.isNotBlank(phone)
							&& !StringUtils.isNumeric(phone)) {
						contactPhoneTextView
								.setError(getString(R.string.telefono_invalido));
						valid = false;
					}

					return valid;

				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.sin_informar_nombre),
							Toast.LENGTH_LONG).show();
					return false;
				}
			}
		});
		this.contactTypeSpinner = (Spinner) findViewById(R.id.contact_type);
		this.spinnerAdapter = new ContactTypesAdapter(getApplicationContext(),
				null);
		this.spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.contactTypeSpinner.setAdapter(spinnerAdapter);
		this.contactTypeSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						contactTypeSelected = spinnerAdapter.getItem(position)
								.getName();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		requestContactTypes();
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void requestContactTypes() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(1);
		params.add("is_reserved", "1");
		CiviCRMAsyncRequest<ListContactType> req = new CiviCRMAsyncRequest<ListContactType>(
				getApplicationContext(), ListContactType.class, ACTION.get,
				ENTITY.ContactType, params);
		contentManager.execute(req, req.createCacheKey(),
				DurationInMillis.ONE_DAY, new ListContactTypesListener());

	}
	
	private void createContact(String name) {
		progressDialog = Utilities.showLoadingProgressDialog(progressDialog,
				this, getString(R.string.progress_bar_creando));
		MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>(
				2);
		fields.add("contact_type", String.valueOf(this.contactTypeSelected));
		if ("Individual".equalsIgnoreCase(contactTypeSelected))
			fields.add("first_name", name);
		else
			fields.add(contactTypeSelected.toLowerCase() + "_name", name);
		CiviCRMAsyncRequest<ListContacts> createContactReq = new CiviCRMAsyncRequest<ListContacts>(
				getApplicationContext(), ListContacts.class, ACTION.create,
				ENTITY.Contact, METHOD.post, fields);
		contentManager.execute(createContactReq, new ContactCreateListener());

	}

	private void createEmailAndPhone(ListContacts result) {
		final String phone = contactPhoneTextView.getText().toString();
		final String mail = contactEmailTextView.getText().toString();
		int newContactId = result.getValues().get(0).getId();
		if (StringUtils.isNotBlank(phone)) {
			CiviCRMAsyncRequest<ListPhones> createPhonesReq = buildPhoneCreateRequest(
					newContactId, phone);
			contentManager.execute(createPhonesReq, new PhoneCreateListener());
		}
		if (StringUtils.isNotBlank(mail)) {
			CiviCRMAsyncRequest<ListEmails> createPhonesReq = buildEmailCreateRequest(
					newContactId, mail);
			contentManager.execute(createPhonesReq, new EmailCreateListener());
		}
		finishContactCreation(newContactId);

	}

	private void finishContactCreation(int newContactId) {
		Utilities.dismissProgressDialog(progressDialog);
		this.contactEmailTextView.setText("");
		this.contactNameTextView.setText("");
		this.contactPhoneTextView.setText("");
		Toast.makeText(getApplicationContext(),
				getString(R.string.contacto_creado) + newContactId, Toast.LENGTH_LONG)
				.show();
	}

	private void notifyErrorOnCreateContact() {
		Utilities.dismissProgressDialog(progressDialog);
		Toast.makeText(getApplicationContext(),
				getString(R.string.error_creando_contacto),
				Toast.LENGTH_LONG).show();
	}

	private CiviCRMAsyncRequest<ListEmails> buildEmailCreateRequest(
			int contactId, String mail) {
		MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>(
				2);
		fields.add("contact_id", String.valueOf(contactId));
		fields.add("email", mail);
		return new CiviCRMAsyncRequest<ListEmails>(this, ListEmails.class,
				ACTION.create, ENTITY.Email, METHOD.post, fields);
	}

	private CiviCRMAsyncRequest<ListPhones> buildPhoneCreateRequest(
			int contactId, String phone) {
		MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>(
				2);
		fields.add("contact_id", String.valueOf(contactId));
		fields.add("phone", phone);
		return new CiviCRMAsyncRequest<ListPhones>(this, ListPhones.class,
				ACTION.create, ENTITY.Phone, METHOD.post, fields);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class ListContactTypesListener implements
			RequestListener<ListContactType> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ListContactType result) {
			if (result == null)
				return;
			spinnerAdapter.clear();
			spinnerAdapter.addAll(result.getValues());
			spinnerAdapter.notifyDataSetChanged();
		}

	}

	public class ContactCreateListener implements RequestListener<ListContacts> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyErrorOnCreateContact();
		}

		@Override
		public void onRequestSuccess(ListContacts result) {
			if (result == null)
				return;
			//contentManager.removeAllDataFromCache();
			createEmailAndPhone(result);
		}

	}

	public class EmailCreateListener implements RequestListener<ListEmails> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyErrorOnCreateContact();

		}

		@Override
		public void onRequestSuccess(ListEmails result) {
			Log.d("AddContactActivity", "Email del contacto creado");
		}

	}

	public class PhoneCreateListener implements RequestListener<ListPhones> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyErrorOnCreateContact();

		}

		@Override
		public void onRequestSuccess(ListPhones result) {
			Log.d("AddContactActivity", "Phone del contacto creado");
		}

	}

}
