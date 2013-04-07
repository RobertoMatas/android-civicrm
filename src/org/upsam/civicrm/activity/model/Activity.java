package org.upsam.civicrm.activity.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
	@JsonProperty("source_contact_id")
	private String sourceContactId;
	@JsonProperty("activity_type_id")
	private String activityTypeId;
	@JsonProperty("subject")
	private String subject;
	@JsonProperty("status_id")
	private String statusId;
	@JsonProperty("phone_number")
	private String phoneNumber;

	/**
	 * @return the sourceContactId
	 */
	public String getSourceContactId() {
		return sourceContactId;
	}

	/**
	 * @param sourceContactId
	 *            the sourceContactId to set
	 */
	public void setSourceContactId(String sourceContactId) {
		this.sourceContactId = sourceContactId;
	}

	/**
	 * @return the activityTypeId
	 */
	public String getActivityTypeId() {
		return activityTypeId;
	}

	/**
	 * @param activityTypeId
	 *            the activityTypeId to set
	 */
	public void setActivityTypeId(String activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the statusId
	 */
	public String getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId
	 *            the statusId to set
	 */
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String nuevaLinea = System.getProperty("line.separator");
		sb.append("activityTypeId: " + activityTypeId + nuevaLinea);
		sb.append("subject: " + subject + nuevaLinea);
		sb.append("statusId: " + statusId + nuevaLinea);
		sb.append("phoneNumber: " + phoneNumber + nuevaLinea);
		return sb.toString();
	}

}
