package org.upsam.civicrm.contact.add;

import static org.upsam.civicrm.util.CiviCRMRequestHelper.notifyRequestError;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ListContactType;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.contact.model.email.ListEmails;
import org.upsam.civicrm.contact.model.telephone.ListPhones;
import org.upsam.civicrm.dagger.di.CiviCRMSpiceRequest;
import org.upsam.civicrm.dagger.di.SpiceDIAwareActivity;
import org.upsam.civicrm.dagger.modules.ActivityModule;
import org.upsam.civicrm.dagger.modules.AddContactActivityModule;

import android.app.ActionBar;
import android.os.Build;
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

public class AddContactActivity extends SpiceDIAwareActivity {

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

	@Inject
	AddContactValidator validator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.upsam.civicrm.dagger.di.BaseDIActivity#getModules()
	 */
	@Override
	protected List<Object> getModules() {
		return Arrays.asList(new ActivityModule(this),
				new AddContactActivityModule());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
				boolean isValid = true;
				if (!validator.isValidName(name)) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.sin_informar_nombre),
							Toast.LENGTH_LONG).show();
					isValid = false;

				} else {
					if (!validator.isValidEmail(mail)) {
						contactEmailTextView
								.setError(getString(R.string.mail_no_valido));
						isValid = false;
					}

					if (!validator.isValidPhone(phone)) {
						contactPhoneTextView
								.setError(getString(R.string.telefono_invalido));
						isValid = false;
					}
				}
				if (isValid) {
					createContact(name);
				}

			}

		});
		this.contactTypeSpinner = (Spinner) findViewById(R.id.contact_type);
		this.spinnerAdapter = new ContactTypesAdapter(this, null);
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
						contactTypeSelected = spinnerAdapter.getItem(0)
								.getName();
					}
				});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			ActionBar actionBar = getActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.upsam.civicrm.contact.add.SpiceDIAwareActivity#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		requestContactTypes();
	}

	private void requestContactTypes() {
		CiviCRMSpiceRequest<ListContactType> req = getRequestBuilder()
				.requestContactTypes();
		getSpiceManager().execute(req, req.createCacheKey(),
				DurationInMillis.ONE_DAY, new ListContactTypesListener());

	}

	private void createContact(String name) {
		progressDialog = getProgressDialogUtilities().showProgressDialog(
				progressDialog, getString(R.string.progress_bar_creando));
		CiviCRMSpiceRequest<ListContacts> createContactReq = getRequestBuilder()
				.postRequestCreateContact(
						contactTypeSelected != null ? contactTypeSelected
								: spinnerAdapter.getItem(0).getName(), name);
		getSpiceManager()
				.execute(createContactReq, new ContactCreateListener());
	}

	private void createEmailAndPhone(ListContacts result) {
		final String phone = contactPhoneTextView.getText().toString();
		final String mail = contactEmailTextView.getText().toString();
		int newContactId = result.getValues().get(0).getId();
		if (StringUtils.isNotBlank(phone)) {
			CiviCRMSpiceRequest<ListPhones> createPhonesReq = getRequestBuilder()
					.postRequestCreatePhone(newContactId, phone);
			getSpiceManager().execute(createPhonesReq,
					new PhoneCreateListener());
		}
		if (StringUtils.isNotBlank(mail)) {
			CiviCRMSpiceRequest<ListEmails> createPhonesReq = getRequestBuilder()
					.postRequestCreateEmail(newContactId, mail);
			getSpiceManager().execute(createPhonesReq,
					new EmailCreateListener());
		}
		finishContactCreation(newContactId);

	}

	private void finishContactCreation(int newContactId) {
		getProgressDialogUtilities().dismissProgressDialog(progressDialog);
		this.contactEmailTextView.setText("");
		this.contactNameTextView.setText("");
		this.contactPhoneTextView.setText("");
		Toast.makeText(this,
				getString(R.string.contacto_creado) + newContactId,
				Toast.LENGTH_LONG).show();
	}

	private void notifyErrorOnCreateContact() {
		getProgressDialogUtilities().dismissProgressDialog(progressDialog);
		Toast.makeText(getApplicationContext(),
				getString(R.string.error_creando_contacto), Toast.LENGTH_LONG)
				.show();
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

	public class ListContactTypesListener implements
			RequestListener<ListContactType> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(AddContactActivity.this, progressDialog);

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
			// getSpiceManager().removeAllDataFromCache();
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
