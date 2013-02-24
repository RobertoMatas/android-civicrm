package org.upsam.civicrm.contact.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListContacts {

	private Map<String, Contact> values;

	/**
	 * @return the values
	 */
	public Map<String, Contact> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(Map<String, Contact> values) {
		this.values = values;
	}

}
