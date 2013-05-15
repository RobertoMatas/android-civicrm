package org.upsam.civicrm.contact.detail.fragments;

import static org.upsam.civicrm.util.CiviCRMRequestHelper.notifyRequestError;

import java.util.List;
import java.util.Map;

import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.dagger.di.CiviCRMSpiceRequest;
import org.upsam.civicrm.dagger.di.fragment.SpiceDIAwareFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ListContactsActivitiesFragment extends SpiceDIAwareFragment {

	public class ListActivtiesSummaryListener implements
			RequestListener<ListActivtiesSummary> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyRequestError(getActivityContext(), progressDialog);

		}

		@Override
		public void onRequestSuccess(ListActivtiesSummary result) {
			if (result == null)
				return;
			refreshView(result);
		}

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
		View view = inflater.inflate(
				org.upsam.civicrm.R.layout.contact_address_layout, container,
				false);
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
		executeRequest();
	}

	private void executeRequest() {
		progressDialog = getProgressDialogUtilities().showProgressDialog(
				progressDialog, getString(R.string.progress_bar_msg_generico));
		ContactSummary contactSummary = getArguments().getParcelable("contact");
		if (contactSummary != null) {
			CiviCRMSpiceRequest<ListActivtiesSummary> req = getRequestBuilder()
					.requestActivitiesForContact(contactSummary.getId());
			getSpiceManager().execute(req, req.createCacheKey(),
					DurationInMillis.ONE_HOUR,
					new ListActivtiesSummaryListener());
		}

	}

	private void refreshView(ListActivtiesSummary result) {
		List<ActivitySummary> values = result.getValues();
		for (ActivitySummary activitySummary : values) {
			paintActivityRow(activitySummary);
		}
		getProgressDialogUtilities().dismissProgressDialog(progressDialog);
	}

	private void paintActivityRow(ActivitySummary activitySummary) {
		LinearLayout listLayout = (LinearLayout) getView().findViewById(
				R.id.addressList);
		View row = LayoutInflater.from(getActivityContext()).inflate(
				R.layout.row_address_layout, listLayout, false);
		ImageView imag = (ImageView) row.findViewById(R.id.imageView1);
		TextView title = (TextView) row.findViewById(R.id.addressType);
		TextView text1 = (TextView) row.findViewById(R.id.display_address);
		TextView text2 = (TextView) row.findViewById(R.id.display_supp_address);
		TextView text3 = (TextView) row.findViewById(R.id.display_city);
		TextView text4 = (TextView) row.findViewById(R.id.display_country);
		imag.setImageResource(android.R.drawable.ic_menu_agenda);
		title.setTextAppearance(getActivityContext(), R.style.textoGreen);
		text1.setTextAppearance(getActivityContext(), R.style.textoDefault);
		text2.setTextAppearance(getActivityContext(), R.style.textoWhite);
		text3.setTextAppearance(getActivityContext(), R.style.textoWhite);
		text4.setTextAppearance(getActivityContext(), R.style.textoWhite);

		title.setText(activitySummary.getName());
		text1.setText(activitySummary.getSubject());
		text2.setText(activityAssignatedInfo(activitySummary));
		text3.setText(activitySummary.getDateTime());
		text4.setText(activitySummary.getStatus());

		listLayout.addView(row);

	}

	private String activityAssignatedInfo(ActivitySummary activitySummary) {
		String assInfo = null;
		Map<String, String> asignees = activitySummary.getAsignees();
		Map<String, String> targets = activitySummary.getTargets();
		if (asignees != null && !asignees.isEmpty()) {
			assInfo = getString(R.string.activity_assigned_literal);
		} else if (targets != null && !targets.isEmpty()) {
			assInfo = getString(R.string.activity_target_literal);
		} else {
			assInfo = getString(R.string.activity_created_literal);
		}
		return assInfo;
	}
}
