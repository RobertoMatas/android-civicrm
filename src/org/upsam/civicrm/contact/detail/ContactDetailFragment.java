package org.upsam.civicrm.contact.detail;

import java.util.List;

import org.springframework.util.StringUtils;
import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.Contact;
import org.upsam.civicrm.contact.model.email.Email;
import org.upsam.civicrm.contact.model.email.ListEmails;
import org.upsam.civicrm.contact.model.telephone.ListPhones;
import org.upsam.civicrm.contact.model.telephone.Phone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactDetailFragment extends AbstractAsyncFragment {
	/**
	 * 
	 */
	private final SpiceManager contentManager;

	/**
	 * 
	 * @param contentManager
	 */
	public ContactDetailFragment(final SpiceManager contentManager) {
		super();
		this.contentManager = contentManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_details_layout, container, false);
		return view;
	}

	/**
	 * 
	 */
	private void executeRequests() {
		long contactId = getArguments().getLong("contactId");
		ContactDetailRequest request = new ContactDetailRequest(contactId);
		contentManager.execute(request, request.createCacheKey(), DurationInMillis.ONE_MINUTE, new ContactDetailListener());
		// peticionar emails y phones
		ContactEmailsRequest emailsReq = new ContactEmailsRequest(contactId);
		ContactPhonesRequest phonesReq = new ContactPhonesRequest(contactId);
		contentManager.execute(emailsReq, emailsReq.createCacheKey(), DurationInMillis.ONE_MINUTE, new ContactEmailListener());
		contentManager.execute(phonesReq, phonesReq.createCacheKey(), DurationInMillis.ONE_MINUTE, new ContactPhoneListener());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		executeRequests();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		showLoadingProgressDialog();
	}

	private void refreshView(Contact result) {
		View view = getView();
		TextView displayName = (TextView) view.findViewById(R.id.display_name);
		displayName.setText(result.getName());
		TextView type = (TextView) view.findViewById(R.id.contact_type);
		type.setText(result.getType());
		String img = result.getImage();
		if (StringUtils.hasText(img)) {
			ContactImageRequest request = new ContactImageRequest(img);
			contentManager.execute(request, img, DurationInMillis.ONE_MINUTE, new ContactImageListener());
		}
	}

	private void refreshImageView(Bitmap result) {
		View view = getView();
		QuickContactBadge img = (QuickContactBadge) view.findViewById(R.id.contac_img);
		img.setImageBitmap(result);
	}

	private void refreshEmailsView(ListEmails result) {
		View view = getView();
		List<Email> emails = result.getValues();
		
		if (emails != null) {
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
			for (Email email : emails) {
				adapter1.add(email.getEmail());
			}
			ListView emailsView = (ListView) view.findViewById(R.id.emails);
			emailsView.setAdapter(adapter1);
		}
		/*
		if (emails != null) {
			TableLayout table = (TableLayout) view.findViewById(R.id.other_data);
			TableRow row = null;
			TextView fieldName = null;
			TextView value = null;
			for (Email email : emails) {
				row = new TableRow(getActivity());
				fieldName = new TextView(getActivity());
				fieldName.setText("Email: ");
				row.addView(fieldName);
				value =  new TextView(getActivity());
				value.setText(email.getEmail());
				row.addView(value);
				table.addView(row);
			}
		}
		*/
	}

	private void refreshPhoneView(ListPhones result) {
		View view = getView();
		List<Phone> phones = result.getValues();
		if (phones != null) {
			
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
			for (Phone phone : phones) {
				adapter1.add(phone.getPhone());
			}
			ListView phonesView = (ListView) view.findViewById(R.id.phones);
			phonesView.setAdapter(adapter1);
			/*
			TableLayout table = (TableLayout) view.findViewById(R.id.other_data);
			TableRow row = null;
			TextView fieldName = null;
			TextView value = null;
			for (Phone phone : phones) {
				row = new TableRow(getActivity());
				fieldName = new TextView(getActivity());
				fieldName.setText("Phone: ");
				row.addView(fieldName);
				value =  new TextView(getActivity());
				value.setText(phone.getPhone());
				row.addView(value);
				table.addView(row);
			}
			*/
		}
		dismissProgressDialog();
	}

	private class ContactDetailListener implements RequestListener<Contact> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Toast.makeText(ContactDetailFragment.this.getActivity().getApplicationContext(), "Error during request: " + spiceException.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestSuccess(Contact result) {
			if (result == null)
				return;
			refreshView(result);
		}

	}

	private class ContactImageListener implements RequestListener<Bitmap> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Toast.makeText(ContactDetailFragment.this.getActivity().getApplicationContext(), "Error during request: " + spiceException.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestSuccess(Bitmap result) {
			if (result == null)
				return;
			refreshImageView(result);
		}
	}

	private class ContactEmailListener implements RequestListener<ListEmails> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Toast.makeText(ContactDetailFragment.this.getActivity().getApplicationContext(), "Error during request: " + spiceException.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestSuccess(ListEmails result) {
			if (result == null)
				return;
			refreshEmailsView(result);
		}

	}

	private class ContactPhoneListener implements RequestListener<ListPhones> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Toast.makeText(ContactDetailFragment.this.getActivity().getApplicationContext(), "Error during request: " + spiceException.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestSuccess(ListPhones result) {
			if (result == null)
				return;
			refreshPhoneView(result);
		}

	}

}
