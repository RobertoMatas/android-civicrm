package org.upsam.civicrm.contact.model.groups;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {
	/**
	 * 
	 */
	private String title;
	/**
	 * 
	 */
	private String visibility;
	/**
	 * Fecha de registro en el grupo
	 */
	@JsonProperty("in_date")
	private String inDate;
	/**
	 * Método de entrada en el grupo
	 */
	@JsonProperty("in_method")
	private String inMethod;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the visibility
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility
	 *            the visibility to set
	 */
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return the inDate
	 */
	public String getInDate() {
		return inDate;
	}

	/**
	 * @param inDate
	 *            the inDate to set
	 */
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	/**
	 * @return the inMethod
	 */
	public String getInMethod() {
		return inMethod;
	}

	/**
	 * @param inMethod
	 *            the inMethod to set
	 */
	public void setInMethod(String inMethod) {
		this.inMethod = inMethod;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Group [title=" + title + ", visibility=" + visibility + ", inDate=" + inDate + ", inMethod=" + inMethod + "]";
	}

}
