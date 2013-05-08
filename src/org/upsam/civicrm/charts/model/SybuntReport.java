package org.upsam.civicrm.charts.model;

import java.util.Map;

import android.webkit.WebView;

public class SybuntReport extends AbstractReport {

	/**
	 * 
	 */
	private final String currency;

	/**
	 * 
	 * @param report
	 * @param webView
	 * @param currency
	 */
	public SybuntReport(Map<String, Number> report, WebView webView,
			String currency) {
		super(report, webView);
		this.currency = currency;
	}

	@Override
	public String getTitleForXAxis() {
		return "Year";
	}

	@Override
	public String getTitleForYAxis() {
		return "Amount (" + this.currency + ")";
	}

}
