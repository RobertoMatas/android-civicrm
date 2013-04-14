package org.upsam.civicrm.contact.detail.fragments;

import java.util.List;
import java.util.Map;

import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.util.CiviCRMRequestHelper;
import org.upsam.civicrm.util.Utilities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ListContactsActivitiesFragment extends AbstractAsyncFragment {

	public ListContactsActivitiesFragment(SpiceManager contentManager,
			Context activityContext) {
		super(contentManager, activityContext);
	}

	public class ListActivtiesSummaryListener implements
			RequestListener<ListActivtiesSummary> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Utilities.dismissProgressDialog(progressDialog);
			Toast.makeText(getActivity(),
					"Error al recuperar la lista de actividades",
					Toast.LENGTH_LONG).show();

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
				org.upsam.civicrm.R.layout.contact_tags_and_groups_layout,
				container, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		executeRequest();
	}

	private void executeRequest() {
		progressDialog = Utilities.showLoadingProgressDialog(progressDialog,
				this.getActivity(), "Cargando...");
		ContactSummary contactSummary = getArguments().getParcelable("contact");
		if (contactSummary != null) {
			CiviCRMAsyncRequest<ListActivtiesSummary> req = CiviCRMRequestHelper
					.requestActivitiesForContact(contactSummary.getId(),
							this.getActivity());
			contentManager.execute(req, req.createCacheKey(),
					DurationInMillis.ONE_HOUR,
					new ListActivtiesSummaryListener());
		}

	}

	private void refreshView(ListActivtiesSummary result) {
		List<ActivitySummary> values = result.getValues();
		ViewGroup viewGroup = (ViewGroup) getView();
		LinearLayout layout = (LinearLayout) viewGroup.getChildAt(0);
		View view = null;
		for (ActivitySummary activitySummary : values) {
			view = getLayoutInflater(null).inflate(
					android.R.layout.simple_list_item_2, layout, false);
			TextView text1 = (TextView) view.findViewById(android.R.id.text1);
			TextView text2 = (TextView) view.findViewById(android.R.id.text2);
			text1.setTextAppearance(activityContext, R.style.textoDefault);
			text2.setTextAppearance(activityContext, R.style.textoWhite);
			text1.setText(activitySummary.getName());
			text2.setText(activityInfo(activitySummary));
			layout.addView(view);
		}
		Utilities.dismissProgressDialog(progressDialog);
	}

	private String activityInfo(ActivitySummary activitySummary) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> asignees = activitySummary.getAsignees();
		Map<String, String> targets = activitySummary.getTargets();
		if (asignees != null && !asignees.isEmpty()) {
			sb.append("Asigned to me\n");
		} else if (targets != null && !targets.isEmpty()) {
			sb.append("Target to me\n");
		} else {
			sb.append("Created by me\n");
		}
		sb.append(activitySummary.getSubject() + "\n");
		sb.append(activitySummary.getDateTime() + "\n");
		sb.append(activitySummary.getStatus());
		return sb.toString();
	}
}
