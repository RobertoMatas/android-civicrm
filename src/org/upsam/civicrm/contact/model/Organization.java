package org.upsam.civicrm.contact.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Organization extends Contact {
	/**
	 * Sic code
	 */
	@JsonProperty("sic_code")
	private String sicCode;

	/**
	 * @return the sicCode
	 */
	public String getSicCode() {
		return sicCode;
	}

	/**
	 * @param sicCode
	 *            the sicCode to set
	 */
	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}

}
