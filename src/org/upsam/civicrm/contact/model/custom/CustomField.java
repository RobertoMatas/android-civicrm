package org.upsam.civicrm.contact.model.custom;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomField {
	/**
	 * ID del campo custom
	 */
	private int id;
	/**
	 * Nombre del campo
	 */
	private String label;
	/**
	 * ID del grupo de opciones de campo a las que pertenece
	 */
	@JsonProperty("option_group_id")
	private int optionGroupId;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the optionGroupId
	 */
	public int getOptionGroupId() {
		return optionGroupId;
	}

	/**
	 * @param optionGroupId
	 *            the optionGroupId to set
	 */
	public void setOptionGroupId(int optionGroupId) {
		this.optionGroupId = optionGroupId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomField [id=" + id + ", label=" + label + ", optionGroupId=" + optionGroupId + "]";
	}

}
