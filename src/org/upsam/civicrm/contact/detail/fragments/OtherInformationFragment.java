package org.upsam.civicrm.contact.detail.fragments;

import static org.upsam.civicrm.util.CiviCRMRequestHelper.notifyRequestError;

import java.util.List;

import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.custom.CustomField;
import org.upsam.civicrm.contact.model.custom.CustomValue;
import org.upsam.civicrm.contact.model.custom.HumanReadableValue;
import org.upsam.civicrm.contact.model.custom.ListCustomFields;
import org.upsam.civicrm.contact.model.custom.ListCustomValues;
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

public class OtherInformationFragment extends SpiceDIAwareFragment {

	public class CustomHumanReadableValueListener implements
			RequestListener<HumanReadableValue> {

		final String label;

		public CustomHumanReadableValueListener(String label) {
			this.label = label;
		}

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(getActivityContext(), progressDialog);

		}

		@Override
		public void onRequestSuccess(HumanReadableValue result) {
			if (result == null)
				return;
			refreshView(result, label);
		}

	}

	public class CustomValuesListener implements
			RequestListener<ListCustomValues> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(getActivityContext(), progressDialog);

		}

		@Override
		public void onRequestSuccess(ListCustomValues result) {
			if (result == null)
				return;
			loadValues(result);

		}

	}

	public class CustomFieldsListener implements
			RequestListener<ListCustomFields> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(getActivityContext(), progressDialog);

		}

		@Override
		public void onRequestSuccess(ListCustomFields result) {
			if (result == null)
				return;
			loadCustomValues(result);

		}

	}

	/**
	 * Lista de campos custom
	 */
	private ListCustomFields customFields;

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
		View view = inflater.inflate(R.layout.contact_other_information_layout,
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
		executeRequests();
	}

	private void executeRequests() {
		this.progressDialog = getProgressDialogUtilities().showProgressDialog(
				getProgressDialog(),
				getString(R.string.progress_bar_msg_generico));
		CiviCRMSpiceRequest<ListCustomFields> customFieldsReq = getRequestBuilder()
				.requestCustomFields();
		getSpiceManager().execute(customFieldsReq,
				customFieldsReq.createCacheKey(), DurationInMillis.ONE_HOUR,
				new CustomFieldsListener());
	}

	private void loadCustomValues(ListCustomFields result) {
		List<CustomField> customFields = result.getValues();
		if (customFields != null && !customFields.isEmpty()) {
			this.customFields = result;
			ContactSummary contactSummary = getArguments().getParcelable(
					"contact");
			CiviCRMSpiceRequest<ListCustomValues> customValues = getRequestBuilder()
					.requestCustomValuesByContactId(contactSummary.getId());
			getSpiceManager().execute(customValues,
					customValues.createCacheKey(), DurationInMillis.ONE_HOUR,
					new CustomValuesListener());
		}

	}

	private void loadValues(ListCustomValues result) {
		List<CustomValue> customValues = result.getValues();
		if (customValues != null && !customValues.isEmpty()) {
			CiviCRMSpiceRequest<HumanReadableValue> request = null;
			CustomField customField = null;
			putTitle();
			for (CustomValue customValue : customValues) {
				customField = this.customFields.getFieldById(customValue
						.getId());
				if (customField.getOptionGroupId() != 0) {
					request = getRequestBuilder().requestHumanReadableValue(
							customField.getOptionGroupId(),
							customValue.getValue());
					getSpiceManager().execute(
							request,
							request.createCacheKey(),
							DurationInMillis.ONE_HOUR,
							new CustomHumanReadableValueListener(customField
									.getLabel()));
				} else {
					HumanReadableValue value = new HumanReadableValue();
					value.setLabel(customValue.getValue());
					refreshView(value, customField.getLabel());
				}
			}
		}
		getProgressDialogUtilities().dismissProgressDialog(progressDialog);
	}

	private void putTitle() {
		LinearLayout layout = (LinearLayout) getView().findViewById(
				R.id.other_info);
		View view = (TextView) LayoutInflater.from(getActivityContext())
				.inflate(android.R.layout.simple_list_item_1, layout, false);
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setTextAppearance(getActivityContext(), R.style.textoGreen);
		textView.setText(getString(R.string.constituent_information));
		layout.addView(view);
	}

	public void refreshView(HumanReadableValue result, String label) {
		LinearLayout layout = (LinearLayout) getView().findViewById(
				R.id.other_info);
		View view = LayoutInflater.from(getActivityContext()).inflate(
				R.layout.contact_tags_and_groups_row, layout, false);

		TextView textView1 = (TextView) view.findViewById(R.id.textView1);
		TextView textView2 = (TextView) view.findViewById(R.id.textView2);
		textView1.setTextAppearance(getActivityContext(), R.style.textoDefault);
		textView2.setTextAppearance(getActivityContext(), R.style.textoWhite);
		textView1.setText(result.getLabel());
		textView2.setText(label);
		layout.addView(view);
	}

}
