package org.upsam.civicrm.contact.detail.fragments;

import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.address.Address;
import org.upsam.civicrm.contact.model.address.ListAddresses;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.util.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

@SuppressLint("ValidFragment")
public class ContactAddressFragment extends AbstractAsyncFragment {

	/**
	 * Vista de la dirección completa
	 */
	private TextView displayAddress;
	/**
	 * Vista de la información suplementaria
	 */
	private TextView displaySuppAddress;
	/**
	 * Vista de la ciudad
	 */
	private TextView displayCity;

	/**
	 * 
	 * @param contentManager
	 */
	public ContactAddressFragment(SpiceManager contentManager,
			Context activityContext) {
		super(contentManager, activityContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_address_layout,
				container, false);
		this.displayAddress = (TextView) view
				.findViewById(R.id.display_address);
		this.displaySuppAddress = (TextView) view
				.findViewById(R.id.display_supp_address);
		this.displayCity = (TextView) view.findViewById(R.id.display_city);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.progressDialog = Utilities.showLoadingProgressDialog(
				this.progressDialog, activityContext,
				getString(R.string.progress_bar_msg_generico));
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

	private void executeRequests() {
		ContactSummary contactSummary = getArguments().getParcelable("contact");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactSummary.getId()));
		CiviCRMAsyncRequest<ListAddresses> request = new CiviCRMAsyncRequest<ListAddresses>(
				activityContext, ListAddresses.class, ACTION.get,
				ENTITY.Address, params);
		contentManager.execute(request, request.createCacheKey(),
				DurationInMillis.ONE_MINUTE, new ContactAddressListener());
	}

	private void refreshView(ListAddresses result) {
		List<Address> addresses = result.getValues();
		if (addresses != null && !addresses.isEmpty()) {
			Address address = addresses.get(0);

			this.displayAddress.setTextAppearance(activityContext,
					R.style.textoDefault);
			this.displaySuppAddress.setTextAppearance(activityContext,
					R.style.textoWhite);
			this.displayCity.setTextAppearance(activityContext,
					R.style.textoWhite);

			this.displayAddress.setText(address.getAddress());
			this.displaySuppAddress.setText(address.getSupplementalAddress());
			this.displayCity.setText(address.getCity());
		}
		Utilities.dismissProgressDialog(progressDialog);

	}

	public class ContactAddressListener implements
			RequestListener<ListAddresses> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ListAddresses result) {
			if (result == null)
				return;
			refreshView(result);

		}
	}
}
