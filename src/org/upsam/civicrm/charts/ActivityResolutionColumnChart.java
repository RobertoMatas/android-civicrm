package org.upsam.civicrm.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.SpiceAwareActivity;
import org.upsam.civicrm.activity.model.ActivityCounter;
import org.upsam.civicrm.activity.model.ActivityStatus;
import org.upsam.civicrm.activity.model.ListActivityStatus;
import org.upsam.civicrm.charts.model.ActivityResolutionReport;
import org.upsam.civicrm.util.CiviCRMRequestHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

@SuppressLint("SetJavaScriptEnabled")
public class ActivityResolutionColumnChart extends SpiceAwareActivity {

	public class ActivityCounterListener implements
			RequestListener<ActivityCounter> {

		private String statusKey;

		public ActivityCounterListener(String statusKey) {
			this.statusKey = statusKey;
		}

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ActivityCounter result) {
			if (result == null) return;
			numReqCompleted ++;
			chartData.setValue(statusKey, result.getNumber());
			checkForCompletation();
		}
	}

	private class ListActivityStatusListener implements
			RequestListener<ListActivityStatus> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ListActivityStatus result) {
			if (result == null)
				return;
			numReqToExecute = result.getCount();
			Map<String, Integer> map = new HashMap<String, Integer>(
					numReqToExecute);
			List<ActivityStatus> values = result.getValues();
			for (ActivityStatus activityStatus : values) {
				map.put(activityStatus.getLabel(), 0);
			}
			chartData = new ActivityResolutionReport(map);
			executeNumActivitiesRequests(values);
		}

	}

	/**
	 * 
	 */
	private WebView webView;
	private int numReqToExecute = 0;
	private int numReqCompleted = 0;
	private ActivityResolutionReport chartData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_resolution_column_chart);
		webView = (WebView) findViewById(R.id.chartWebView);
		webView.getSettings().setJavaScriptEnabled(true);
		// webView.addJavascriptInterface(mGraphHandler, "testhandler");
		webView.loadUrl("file:///android_asset/charts/columnChart.html");
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		executeRequests();
	}

	private void checkForCompletation() {
		if (numReqCompleted == numReqToExecute) {
			Toast.makeText(this, this.chartData.getDataTable().toString(), Toast.LENGTH_LONG).show();
			Log.d("ActivityResolutionColumnChart", this.chartData.getDataTable().toString());
		}
		
	}

	private void executeNumActivitiesRequests(List<ActivityStatus> values) {
		CiviCRMAsyncRequest<ActivityCounter> req = null;
		for (ActivityStatus activityStatus : values) {
			req = CiviCRMRequestHelper.requestNumberOfActivities(this,
					activityStatus.getValue());
			contentManager.execute(req, req.createCacheKey(),
					DurationInMillis.ONE_HOUR, new ActivityCounterListener(activityStatus.getName()));
		}

	}

	private void executeRequests() {
		CiviCRMAsyncRequest<ListActivityStatus> req = CiviCRMRequestHelper
				.requestActivitiesStatus(this);
		contentManager.execute(req, req.createCacheKey(),
				DurationInMillis.ONE_HOUR, new ListActivityStatusListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.activity_activity_resolution_column_chart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
