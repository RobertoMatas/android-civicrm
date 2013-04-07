package org.upsam.civicrm.activity.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListActivities {

	@JsonProperty("is_error")
	private String isError;
	@JsonProperty("error_message")
	private String error_message;
	@JsonProperty("error_code")
	private String error_code;

	private List<Activity> values;

	/**
	 * @return the isError
	 */
	public String getIsError() {
		return isError;
	}

	/**
	 * @param isError
	 *            the isError to set
	 */
	public void setIsError(String isError) {
		this.isError = isError;
	}

	/**
	 * 
	 * @return
	 */
	public String getError_message() {
		return error_message;
	}

	/**
	 * 
	 * @param error_message
	 */
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	/**
	 * 
	 * @return
	 */
	public String getError_code() {
		return error_code;
	}

	/**
	 * 
	 * @param error_code
	 */
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	/**
	 * @return the values
	 */
	public List<Activity> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<Activity> values) {
		this.values = values;
	}
}
