package org.upsam.civicrm.contact.model;

import java.util.Collection;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListContacts {

	private Map<String, Contact> values;

	/**
	 * @return the values
	 */
	public Collection<Contact> getValues() {
		return values.values();
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(Map<String, Contact> values) {
		this.values = values;
	}

}
