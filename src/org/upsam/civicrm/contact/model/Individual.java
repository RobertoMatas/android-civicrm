package org.upsam.civicrm.contact.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Individual extends Contact {
	/**
	 * 
	 */
	@JsonProperty("current_employer")
	private String currentEmployer;

	/**
	 * @return the currentEmployer
	 */
	public String getCurrentEmployer() {
		return currentEmployer;
	}

	/**
	 * @param currentEmployer the currentEmployer to set
	 */
	public void setCurrentEmployer(String currentEmployer) {
		this.currentEmployer = currentEmployer;
	}
	
}
