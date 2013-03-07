package org.upsam.civicrm.contact.model.email;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Email implements Comparable<Email> {
	/**
	 * Email
	 */
	private String email;
	/**
	 * Indica si es el email principal o no
	 */
	@JsonProperty("is_primary")
	private String primaryStr;
	/**
	 * Indica si es el principal
	 */
	@JsonIgnore
	private boolean isPrimary;

	/**
	 * @return the isPrimary
	 */
	public boolean isPrimary() {
		return isPrimary;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the primaryStr
	 */
	public String getPrimaryStr() {
		return primaryStr;
	}

	/**
	 * @param primaryStr the primaryStr to set
	 */
	public void setPrimaryStr(String primaryStr) {
		this.primaryStr = primaryStr;
		this.isPrimary = primaryStr != null && primaryStr.equals("1") ? true : false;
	}
	
	@Override
	public int compareTo(Email another) {
		if (this.isPrimary)
			return -1;
		if (another.isPrimary)
			return 1;
		return this.email.compareTo(another.getEmail());
	}

}
