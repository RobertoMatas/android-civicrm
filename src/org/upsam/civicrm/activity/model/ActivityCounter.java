package org.upsam.civicrm.activity.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class ActivityCounter {

	@JsonProperty("result")
	private int number;

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
}
