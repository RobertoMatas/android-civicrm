package org.upsam.civicrm.beans;

public class ContactSync {

	private String contact_id;
	private String contact_type;
	private String display_name;
	private String primary_mail;
	private String primary_phone;

	/**
	 * @return the contact_id
	 */
	public String getContact_id() {
		return contact_id;
	}

	/**
	 * @param contact_id
	 *            the contact_id to set
	 */
	public void setContact_id(String contact_id) {
		this.contact_id = contact_id;
	}

	/**
	 * @return the contact_type
	 */
	public String getContact_type() {
		return contact_type;
	}

	/**
	 * @param contact_type
	 *            the contact_type to set
	 */
	public void setContact_type(String contact_type) {
		this.contact_type = contact_type;
	}

	/**
	 * @return the display_name
	 */
	public String getDisplay_name() {
		return display_name;
	}

	/**
	 * @param display_name
	 *            the display_name to set
	 */
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	/**
	 * @return the primary_mail
	 */
	public String getPrimary_mail() {
		return primary_mail;
	}

	/**
	 * @param primary_mail
	 *            the primary_mail to set
	 */
	public void setPrimary_mail(String primary_mail) {
		this.primary_mail = primary_mail;
	}

	/**
	 * @return the primary_phone
	 */
	public String getPrimary_phone() {
		return primary_phone;
	}

	/**
	 * @param primary_phone
	 *            the primary_phone to set
	 */
	public void setPrimary_phone(String primary_phone) {
		this.primary_phone = primary_phone;
	}

}
