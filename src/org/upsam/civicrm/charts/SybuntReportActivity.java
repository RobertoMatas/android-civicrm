package org.upsam.civicrm.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.SpiceAwareActivity;
import org.upsam.civicrm.charts.model.Contribution;
import org.upsam.civicrm.charts.model.ListOfContribution;
import org.upsam.civicrm.charts.model.SybuntReport;
import org.upsam.civicrm.util.CiviCRMRequestHelper;
import org.upsam.civicrm.util.Utilities;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class SybuntReportActivity extends SpiceAwareActivity {

	public class ListOfContributionListener implements
			RequestListener<ListOfContribution> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			notifyError();

		}

		@Override
		public void onRequestSuccess(ListOfContribution result) {
			if (result == null) return;
			generateAndShowChart(result);
		}

	}

	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sybunt_report);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		webView = (WebView) findViewById(R.id.chartWebView);
		webView.getSettings().setJavaScriptEnabled(true);
		requestChartData();
	}

	private void generateAndShowChart(ListOfContribution result) {
		List<Contribution> values = result.getValues();
		Map<String, Number> data = new HashMap<String, Number>();
		String key, currency = null;
		float value = 0;
		for (Contribution contribution : values) {
			key = Integer.toString(contribution.getYear());
			if (data.containsKey(key)) {
				value = data.get(key).floatValue() + contribution.getAmount();
				data.put(key, value);
			} else {
				data.put(key, contribution.getAmount());
			}
			if (currency == null) currency = contribution.getCurrency();
		}
		SybuntReport report = new SybuntReport(data, webView, currency);
		webView.addJavascriptInterface(report, "chartHandler");
		webView.loadUrl("file:///android_asset/charts/sybunt_report.html");
		Utilities.dismissProgressDialog(progressDialog);
		webView.setVisibility(View.VISIBLE);
	}

	private void requestChartData() {
		webView.setVisibility(View.GONE);
		CiviCRMAsyncRequest<ListOfContribution> req = CiviCRMRequestHelper
				.requestContributions(this);
		contentManager.execute(req, req.createCacheKey(),
				DurationInMillis.ONE_HOUR, new ListOfContributionListener());

	}

	private void notifyError() {
		Utilities.dismissProgressDialog(progressDialog);
		Toast.makeText(
				getApplicationContext(),
				getString(R.string.error_cargar_informe),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sybunt_report, menu);
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
