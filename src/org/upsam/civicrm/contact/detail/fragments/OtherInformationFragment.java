package org.upsam.civicrm.contact.detail.fragments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.custom.CustomField;
import org.upsam.civicrm.contact.model.custom.CustomValue;
import org.upsam.civicrm.contact.model.custom.HumanReadableValue;
import org.upsam.civicrm.contact.model.custom.ListCustomFields;
import org.upsam.civicrm.contact.model.custom.ListCustomValues;

import android.os.Bundle;
import android.util.Log;
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
	
	public class CustomHumanReadableValueListener implements RequestListener<HumanReadableValue> {
		
		final String label;
		
		public CustomHumanReadableValueListener(String label) {
			this.label = label;
		}

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(HumanReadableValue result) {
			if (result == null) return;
			refreshView(result, label);
		}

	}

	public class CustomValuesListener implements RequestListener<ListCustomValues> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ListCustomValues result) {
			if (result == null) return;
			loadValues(result);

		}

	}

	public class CustomFieldsListener implements RequestListener<ListCustomFields> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ListCustomFields result) {
			if (result == null) return;
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
	public OtherInformationFragment(SpiceManager contentManager) {
		super(contentManager);
		this.customFields = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_tags_and_groups_layout, container, false);
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
		showLoadingProgressDialog();
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
		CiviCRMAsyncRequest<ListCustomFields> customFieldsReq = new CiviCRMAsyncRequest<ListCustomFields>(ListCustomFields.class, ACTION.get, ENTITY.CustomField);
		contentManager.execute(customFieldsReq, customFieldsReq.createCacheKey(), DurationInMillis.ONE_HOUR, new CustomFieldsListener());	
	}
	
	private void loadCustomValues(ListCustomFields result) {
		List<CustomField> customFields = result.getValues();
		if (customFields != null && ! customFields.isEmpty()) {
			Log.d("ContactTagsAndGroupsFragment", "customFields = " + customFields);
			this.customFields = result;
			ContactSummary contactSummary = getArguments().getParcelable("contact");
			final Map<String, String> params = new HashMap<String, String>(1);
			params.put("entity_id", Long.toString(contactSummary.getId()));
			Log.d("ContactTagsAndGroupsFragment", "entity_id = " + contactSummary.getId());
			CiviCRMAsyncRequest<ListCustomValues> customValues = new CiviCRMAsyncRequest<ListCustomValues>(ListCustomValues.class, ACTION.get, ENTITY.CustomValue, params);
			Log.d("ContactTagsAndGroupsFragment", "request = " + customValues.createCacheKey());
			contentManager.execute(customValues, customValues.createCacheKey(), DurationInMillis.ONE_HOUR, new CustomValuesListener());
		}
		
	}
	
	private void loadValues(ListCustomValues result) {
		List<CustomValue> customValues = result.getValues();
		if (customValues != null && ! customValues.isEmpty()) {
			Log.d("ContactTagsAndGroupsFragment", "customValues = " + customValues);
			CiviCRMAsyncRequest<HumanReadableValue> request = null;
			Map<String, String> params = null;
			CustomField customField = null;
			for (CustomValue customValue : customValues) {
				customField = this.customFields.getFieldById(customValue.getId());
				if (customField.getOptionGroupId() != 0) {
					params = new HashMap<String, String>(2);
					params.put("option_group_id", Integer.toString(customField.getOptionGroupId()));
					params.put("value", customValue.getValue());
					request = new CiviCRMAsyncRequest<HumanReadableValue>(HumanReadableValue.class, ACTION.getsingle, ENTITY.OptionValue, params);
					contentManager.execute(request, request.createCacheKey(), DurationInMillis.ONE_HOUR, new CustomHumanReadableValueListener(customField.getLabel()));
				} else {
					HumanReadableValue value = new HumanReadableValue();
					value.setLabel(customValue.getValue());
					refreshView(value, customField.getLabel());
				}
			}
		}
		dismissProgressDialog();
	}
	
	public void refreshView(HumanReadableValue result, String label) {
		Log.d("ContactTagsAndGroupsFragment", "label = " + label + ", valor = " + result);
		ViewGroup viewGroup = (ViewGroup) getView();
		LinearLayout layout = (LinearLayout) viewGroup.getChildAt(0);
		View view = getLayoutInflater(null).inflate(android.R.layout.simple_list_item_2, layout, false);
		TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		TextView text2 = (TextView) view.findViewById(android.R.id.text2);
		text1.setText(result.getLabel());
		text2.setText(label);
		layout.addView(view);
	}

}
