package org.upsam.civicrm.charts.model;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

public class ActivityResolutionReport {

	private Map<String, Integer> report;

	/**
	 * @param report
	 */
	public ActivityResolutionReport(Map<String, Integer> report) {
		super();
		this.report = report;
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
	public JSONArray getDataTable() {
		JSONArray data = new JSONArray();
		JSONArray row = new JSONArray();
		// title
		row.put("Status");
		row.put("Activities");
		Set<String> keySet = report.keySet();
		for (String key : keySet) {
			row = new JSONArray();
			row.put(key);
			row.put(report.get(key));
			data.put(row.toString());
		}
		return data;
	}
}
