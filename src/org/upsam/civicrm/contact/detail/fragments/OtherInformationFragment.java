package org.upsam.civicrm.contact.detail.fragments;

import static org.upsam.civicrm.util.CiviCRMContactRequestHelper.requestCustomFields;
import static org.upsam.civicrm.util.CiviCRMContactRequestHelper.requestCustomValuesByContactId;
import static org.upsam.civicrm.util.CiviCRMContactRequestHelper.requestHumanReadableValue;
import static org.upsam.civicrm.util.CiviCRMRequestHelper.notifyRequestError;

import java.util.List;

import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.custom.CustomField;
import org.upsam.civicrm.contact.model.custom.CustomValue;
import org.upsam.civicrm.contact.model.custom.HumanReadableValue;
import org.upsam.civicrm.contact.model.custom.ListCustomFields;
import org.upsam.civicrm.contact.model.custom.ListCustomValues;
import org.upsam.civicrm.util.Utilities;

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

public class OtherInformationFragment extends AbstractAsyncFragment {

	public class CustomHumanReadableValueListener implements
			RequestListener<HumanReadableValue> {

		final String label;

		public CustomHumanReadableValueListener(String label) {
			this.label = label;
		}

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(activityContext, progressDialog);

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
			notifyRequestError(activityContext, progressDialog);

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
			notifyRequestError(activityContext, progressDialog);

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

	/**
	 * 
	 * @param contentManager
	 */
	public OtherInformationFragment(SpiceManager contentManager,
			Context activityContext) {
		super(contentManager, activityContext);
		this.customFields = null;
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
		View view = inflater.inflate(R.layout.contact_other_information_layout,
				container, false);
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
		CiviCRMAsyncRequest<ListCustomFields> customFieldsReq = requestCustomFields(activityContext);
		contentManager.execute(customFieldsReq,
				customFieldsReq.createCacheKey(), DurationInMillis.ONE_HOUR,
				new CustomFieldsListener());
	}

	private void loadCustomValues(ListCustomFields result) {
		List<CustomField> customFields = result.getValues();
		if (customFields != null && !customFields.isEmpty()) {
			this.customFields = result;
			ContactSummary contactSummary = getArguments().getParcelable(
					"contact");
			CiviCRMAsyncRequest<ListCustomValues> customValues = requestCustomValuesByContactId(
					activityContext, contactSummary.getId());
			contentManager.execute(customValues, customValues.createCacheKey(),
					DurationInMillis.ONE_HOUR, new CustomValuesListener());
		}

	}

	private void loadValues(ListCustomValues result) {
		List<CustomValue> customValues = result.getValues();
		if (customValues != null && !customValues.isEmpty()) {
			CiviCRMAsyncRequest<HumanReadableValue> request = null;
			CustomField customField = null;
			putTitle();
			for (CustomValue customValue : customValues) {
				customField = this.customFields.getFieldById(customValue
						.getId());
				if (customField.getOptionGroupId() != 0) {
					request = requestHumanReadableValue(activityContext,
							customField.getOptionGroupId(),
							customValue.getValue());
					contentManager.execute(
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
		Utilities.dismissProgressDialog(progressDialog);
	}

	private void putTitle() {
		LinearLayout layout = (LinearLayout) getView().findViewById(
				R.id.other_info);
		View view = (TextView) getLayoutInflater(null).inflate(
				android.R.layout.simple_list_item_1, layout, false);
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setTextAppearance(activityContext, R.style.textoGreen);
		textView.setText(getString(R.string.constituent_information));
		layout.addView(view);
	}

	public void refreshView(HumanReadableValue result, String label) {
		LinearLayout layout = (LinearLayout) getView().findViewById(
				R.id.other_info);
		View view = getLayoutInflater(null).inflate(
				R.layout.contact_tags_and_groups_row, layout, false);

		TextView textView1 = (TextView) view.findViewById(R.id.textView1);
		TextView textView2 = (TextView) view.findViewById(R.id.textView2);
		textView1.setTextAppearance(activityContext, R.style.textoDefault);
		textView2.setTextAppearance(activityContext, R.style.textoWhite);
		textView1.setText(result.getLabel());
		textView2.setText(label);
		layout.addView(view);
	}

}
