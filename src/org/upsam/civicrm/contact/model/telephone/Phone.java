package org.upsam.civicrm.contact.model.telephone;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Phone implements Comparable<Phone> {
	/**
	 * Teléfono
	 */
	private String phone;
	/**
	 * Indica si es el teléfono principal o no
	 */
	@JsonProperty("is_primary")
	private String primaryStr;
	
	@JsonProperty("contact_id")
	private String contactId;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	
	

	/**
	 * @return the contactId
	 */
	public String getContactId() {
		return contactId;
	}

	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	@Override
	public int compareTo(Phone another) {
		if (this.isPrimary)
			return -1;
		if (another.isPrimary)
			return 1;
		return this.phone.compareTo(another.getPhone());
	}

}
