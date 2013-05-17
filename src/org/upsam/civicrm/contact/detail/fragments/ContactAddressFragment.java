package org.upsam.civicrm.contact.detail.fragments;

import static org.upsam.civicrm.util.CiviCRMRequestHelper.notifyRequestError;

import java.util.List;

import org.springframework.util.StringUtils;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.address.Address;
import org.upsam.civicrm.contact.model.address.ListAddresses;
import org.upsam.civicrm.contact.model.constant.Constant;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.dagger.di.CiviCRMSpiceRequest;
import org.upsam.civicrm.dagger.di.fragment.SpiceDIAwareFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactAddressFragment extends SpiceDIAwareFragment {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		this.progressDialog = getProgressDialogUtilities().showProgressDialog(
				getProgressDialog(),
				getString(R.string.progress_bar_msg_generico));
		getCountries();
	}

	private void getCountries() {
		CiviCRMSpiceRequest<Constant> reqCountries = getRequestBuilder()
				.requestCountries();
		getSpiceManager().execute(reqCountries, reqCountries.createCacheKey(),
				DurationInMillis.ONE_HOUR, new CountriesListener());

	}

	private void getLocationTypes(Constant countries) {
		CiviCRMSpiceRequest<Constant> reqLocations = getRequestBuilder()
				.requestLocationTypes();
		getSpiceManager()
				.execute(reqLocations, reqLocations.createCacheKey(),
						DurationInMillis.ONE_HOUR,
						new LocationTypesListener(countries));
	}

	private void executeRequests(Constant countries, Constant locationTypes) {
		ContactSummary contactSummary = getArguments().getParcelable("contact");
		CiviCRMSpiceRequest<ListAddresses> request = getRequestBuilder()
				.requestContactAddresses(contactSummary.getId());
		getSpiceManager().execute(request, request.createCacheKey(),
				DurationInMillis.ONE_MINUTE,
				new ContactAddressListener(countries, locationTypes));
	}

	private void refreshView(ListAddresses result, Constant countries,
			Constant locationTypes) {
		List<Address> addresses = result.getValues();
		if (addresses != null && !addresses.isEmpty()) {
			for (Address address : addresses) {
				paintAddressRow(address, countries, locationTypes);
			}
		}
		getProgressDialogUtilities().dismissProgressDialog(progressDialog);

	}

	private void paintAddressRow(Address address, Constant countries,
			Constant locationTypes) {
		LinearLayout listLayout = (LinearLayout) getView().findViewById(
				R.id.addressList);
		View row = LayoutInflater.from(getActivityContext()).inflate(
				R.layout.row_address_layout, listLayout, false);
		TextView displayAddressType = (TextView) row
				.findViewById(R.id.addressType);
		TextView displayAddress = (TextView) row
				.findViewById(R.id.display_address);
		TextView displaySuppAddress = (TextView) row
				.findViewById(R.id.display_supp_address);
		TextView displayCity = (TextView) row.findViewById(R.id.display_city);
		TextView displayCountry = (TextView) row
				.findViewById(R.id.display_country);
		displayAddressType.setTextAppearance(getActivityContext(),
				R.style.textoGreen);
		displayAddress.setTextAppearance(getActivityContext(),
				R.style.textoDefault);
		displaySuppAddress.setTextAppearance(getActivityContext(),
				R.style.textoWhite);
		displayCity.setTextAppearance(getActivityContext(), R.style.textoWhite);
		displayCountry.setTextAppearance(getActivityContext(),
				R.style.textoWhite);

		displayAddressType.setText(locationTypes.getValues().get(
				address.getLocationTypeId())
				+ " " + getString(R.string.address_literal));
		displayAddress.setText(address.getAddress());
		displaySuppAddress.setText(address.getSupplementalAddress());
		String city = StringUtils.hasText(address.getZipCode()) ? address
				.getCity() + ", " + address.getZipCode() : address.getCity();
		displayCity.setText(city);
		displayCountry.setText(countries.getValues()
				.get(address.getCountryId()));
		listLayout.addView(row);
	}

	public class CountriesListener implements RequestListener<Constant> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(getActivityContext(), progressDialog);
		}

		@Override
		public void onRequestSuccess(Constant result) {
			getLocationTypes(result);
		}

	}

	public class LocationTypesListener implements RequestListener<Constant> {

		private final Constant countries;

		/**
		 * @param countries
		 */
		public LocationTypesListener(Constant countries) {
			super();
			this.countries = countries;
		}

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(getActivityContext(), progressDialog);

		}

		@Override
		public void onRequestSuccess(Constant result) {
			executeRequests(countries, result);

		}

	}

	public class ContactAddressListener implements
			RequestListener<ListAddresses> {

		private final Constant countries;

		private final Constant locationTypes;

		public ContactAddressListener(final Constant countries,
				final Constant locationTypes) {
			this.countries = countries;
			this.locationTypes = locationTypes;
		}

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(getActivityContext(), progressDialog);

		}

		@Override
		public void onRequestSuccess(ListAddresses result) {
			if (result == null)
				return;
			refreshView(result, countries, locationTypes);

		}
	}
}
