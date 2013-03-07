package org.upsam.civicrm.contact.model;

import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfComparableElements<ELEM extends Comparable<ELEM>> {
	/**
	 * Lista de valores comparables
	 */
	@JsonProperty("values")
	private List<ELEM> values;

	/**
	 * @return the values
	 */
	public List<ELEM> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<ELEM> values) {
		if (values != null && !values.isEmpty()) {
			Collections.sort(values);
		}
		this.values = values;
	}

}
