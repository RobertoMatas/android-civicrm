package org.upsam.civicrm.charts.model;

import java.util.Map;

import android.webkit.WebView;

public class ActivityResolutionReport extends AbstractReport {

	/**
	 * 
	 * @param report
	 * @param webView
	 */
	public ActivityResolutionReport(Map<String, Number> report, WebView webView) {
		super(report, webView);
	}

	@Override
	public String getTitleForXAxis() {
		return "Status";
	}

	@Override
	public String getTitleForYAxis() {
		return "Activities";
	}

}
