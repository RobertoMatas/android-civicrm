package org.upsam.civicrm.charts.model;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class ActivityResolutionReport {

	private Map<String, Integer> report;
	private WebView webView;

	/**
	 * @param report
	 */
	public ActivityResolutionReport(Map<String, Integer> report, WebView webView) {
		super();
		this.report = report;
		this.webView = webView;
	}

	public void setValue(String key, int value) {
		this.report.put(key, value);
	}

	/**
	 * @return the report
	 */
	public Map<String, Integer> getReport() {
		return report;
	}

	/**
	 * 
	 * @return
	 */
	public String getDataTable() {
		JSONArray data = new JSONArray();
		JSONArray row = new JSONArray();
		// title
		row.put("Status");
		row.put("Activities");
		data.put(row);
		Set<String> keySet = report.keySet();
		for (String key : keySet) {
			row = new JSONArray();
			row.put(key);
			row.put(report.get(key));
			data.put(row);
		}
		return data.toString();
	}

	@JavascriptInterface
	public void loadChart() {
		webView.loadUrl("javascript:drawChart(" + getDataTable() + ")");
	}

}
