package org.upsam.civicrm.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivityCounter;
import org.upsam.civicrm.activity.model.ListActivityStatus;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.charts.model.ListOfContribution;
import org.upsam.civicrm.event.model.ListEventsSummary;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class CiviCRMRequestHelper {

	public static void notifyRequestError(Context ctx,
			ProgressDialog progressDialog) {
		Utilities.dismissProgressDialog(progressDialog);
		Toast.makeText(ctx, ctx.getString(R.string.general_http_error),
				Toast.LENGTH_LONG).show();
	}

	public static CiviCRMAsyncRequest<ListActivtiesSummary> requestActivitiesForContact(
			int contactId, Context ctx) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", String.valueOf(contactId));
		return new CiviCRMAsyncRequest<ListActivtiesSummary>(ctx,
				ListActivtiesSummary.class, ACTION.get, ENTITY.Activity, params);
	}
	
	public static CiviCRMAsyncRequest<ListEventsSummary> requestEventsForContact(
			int contactId, Context ctx) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("contact_id", String.valueOf(contactId));
		return new CiviCRMAsyncRequest<ListEventsSummary>(ctx,
				ListEventsSummary.class, ACTION.get, ENTITY.Participant, params);
	}

	public static CiviCRMAsyncRequest<ListActivityStatus> requestActivitiesStatus(
			Context ctx) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("option_group_id", "25");
		return new CiviCRMAsyncRequest<ListActivityStatus>(ctx,
				ListActivityStatus.class, ACTION.get, ENTITY.OptionValue,
				params);
	}

	public static CiviCRMAsyncRequest<ActivityCounter> requestNumberOfActivities(
			Context ctx, int statusId) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				2);
		params.add("status_id", String.valueOf(statusId));
		params.add("rowCount", "10000000");
		return new CiviCRMAsyncRequest<ActivityCounter>(ctx,
				ActivityCounter.class, ACTION.getcount, ENTITY.Activity, params);
	}

	public static CiviCRMAsyncRequest<ListOfContribution> requestContributions(
			Context ctx) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				5);
		params.add("rowCount", "500");
		params.add("return[total_amount]", "1");
		params.add("return[receive_date]", "1");
		params.add("return[currency]", "1");
		params.add("sort", "receive_date");
		return new CiviCRMAsyncRequest<ListOfContribution>(ctx,
				ListOfContribution.class, ACTION.get, ENTITY.Contribution,
				params);
	}

}
