package org.upsam.civicrm.contact.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListContacts {
	/**
	 * Lista de contactos
	 */
	private List<ContactSummary> values;
	/**
	 * Número de elementos
	 */
	private int count;
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the values
	 */
	public List<ContactSummary> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<ContactSummary> values) {
		this.values = values;
	}

}
