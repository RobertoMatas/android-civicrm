package org.upsam.civicrm.charts.model;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public abstract class AbstractReport {

	protected Map<String, Number> report;
	protected WebView webView;

	/**
	 * 
	 * @param report
	 * @param webView
	 */
	public AbstractReport(Map<String, Number> report, WebView webView) {
		this.report = report;
		this.webView = webView;
	}

	public void setValue(String key, int value) {
		this.report.put(key, value);
	}

	/**
	 * @return the report
	 */
	public Map<String, Number> getReport() {
		return report;
	}

	/**
	 * Devuelve la DataTable en formato JSON
	 * 
	 * @return
	 */
	public JSONArray getDataTable() {
		JSONArray data = new JSONArray();
		JSONArray row = new JSONArray();
		// title
		row.put(getTitleForXAxis());
		row.put(getTitleForYAxis());
		data.put(row);
		Set<String> keySet = report.keySet();
		for (String key : keySet) {
			row = new JSONArray();
			row.put(key);
			row.put(report.get(key));
			data.put(row);
		}
		return data;
	}

	@JavascriptInterface
	public abstract String getTitleForXAxis();

	@JavascriptInterface
	public abstract String getTitleForYAxis();

	@JavascriptInterface
	public void loadChart() {
		webView.loadUrl("javascript:drawChart(" + getDataTable().toString()
				+ ")");
	}
}