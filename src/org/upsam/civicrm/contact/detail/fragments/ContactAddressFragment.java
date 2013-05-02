package org.upsam.civicrm.contact.detail.fragments;

import static org.upsam.civicrm.util.CiviCRMContactRequestHelper.requestCountries;
import static org.upsam.civicrm.util.CiviCRMRequestHelper.notifyRequestError;

import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.address.Address;
import org.upsam.civicrm.contact.model.address.ListAddresses;
import org.upsam.civicrm.contact.model.constant.Constant;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.util.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

//@SuppressLint("ValidFragment")
public class ContactAddressFragment extends AbstractAsyncFragment {

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

		return view;
	}

	private void paintAddressRow(Address address, Constant countries) {
		LinearLayout listLayout = (LinearLayout) getView().findViewById(
				R.id.addressList);
		View row = getLayoutInflater(null).inflate(R.layout.row_address_layout,
				listLayout, false);
		TextView displayAddress = (TextView) row
				.findViewById(R.id.display_address);
		TextView displaySuppAddress = (TextView) row
				.findViewById(R.id.display_supp_address);
		TextView displayCity = (TextView) row.findViewById(R.id.display_city);
		TextView displayCountry = (TextView) row
				.findViewById(R.id.display_country);
		displayAddress.setTextAppearance(activityContext, R.style.textoDefault);
		displaySuppAddress.setTextAppearance(activityContext,
				R.style.textoWhite);
		displayCity.setTextAppearance(activityContext, R.style.textoWhite);
		displayCountry.setTextAppearance(activityContext, R.style.textoWhite);

		displayAddress.setText(address.getAddress());
		displaySuppAddress.setText(address.getSupplementalAddress());
		String city = StringUtils.hasText(address.getZipCode()) ? address
				.getCity() + ", " + address.getZipCode() : address.getCity();
		displayCity.setText(city);
		displayCountry.setText(countries.getValues()
				.get(address.getCountryId()));
		listLayout.addView(row);
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
		getCountries();
	}

	private void getCountries() {
		CiviCRMAsyncRequest<Constant> req = requestCountries(activityContext);
		contentManager.execute(req, req.createCacheKey(),
				DurationInMillis.ONE_HOUR, new CountriesListener());

	}

	private void executeRequests(Constant countries) {
		ContactSummary contactSummary = getArguments().getParcelable("contact");
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", Long.toString(contactSummary.getId()));
		CiviCRMAsyncRequest<ListAddresses> request = new CiviCRMAsyncRequest<ListAddresses>(
				activityContext, ListAddresses.class, ACTION.get,
				ENTITY.Address, params);
		contentManager.execute(request, request.createCacheKey(),
				DurationInMillis.ONE_MINUTE, new ContactAddressListener(
						countries));
	}

	private void refreshView(ListAddresses result, Constant countries) {
		List<Address> addresses = result.getValues();
		if (addresses != null && !addresses.isEmpty()) {
			for (Address address : addresses) {
				paintAddressRow(address, countries);
			}
		}
		Utilities.dismissProgressDialog(progressDialog);

	}

	public class CountriesListener implements RequestListener<Constant> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(activityContext, progressDialog);
		}

		@Override
		public void onRequestSuccess(Constant result) {
			executeRequests(result);

		}

	}

	public class ContactAddressListener implements
			RequestListener<ListAddresses> {

		private final Constant countries;

		public ContactAddressListener(Constant countries) {
			this.countries = countries;
		}

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(activityContext, progressDialog);

		}

		@Override
		public void onRequestSuccess(ListAddresses result) {
			if (result == null)
				return;
			refreshView(result, countries);

		}
	}
}
