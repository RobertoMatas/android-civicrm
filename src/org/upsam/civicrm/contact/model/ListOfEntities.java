package org.upsam.civicrm.contact.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfEntities<ENTITY> {
	/**
	 * Lista de valores
	 */
	private List<ENTITY> values;
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
	public List<ENTITY> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<ENTITY> values) {
		this.values = values;
	}

}
