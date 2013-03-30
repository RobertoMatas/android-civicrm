package org.upsam.civicrm.login.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Objeto recuperamos al autenticarnos con llamada rest
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Login {
	
	@JsonProperty("is_error")
	private int idError;
	
	@JsonProperty("api_key")
	private String apiKey;
	
	@JsonProperty("key")
	private String userKey;

	/**
	 * @return the idError
	 */
	public int getIdError() {
		return idError;
	}

	/**
	 * @param idError the idError to set
	 */
	public void setIdError(int idError) {
		this.idError = idError;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the userKey
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * @param userKey the userKey to set
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
		
}
