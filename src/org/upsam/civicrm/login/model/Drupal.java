package org.upsam.civicrm.login.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Objeto recuperamos al autenticarnos con llamada rest
 * 
 * @author Equipo 7 Universidad Pontificia de Salamanca v1.0
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Drupal {

	@JsonProperty("sessid")
	private String sessid;

	@JsonProperty("session_name")
	private String session_name;

	@JsonProperty("user")
	private User user;

	public String getSessid() {
		return sessid;
	}

	public void setSessid(String sessid) {
		this.sessid = sessid;
	}

	public String getSession_name() {
		return session_name;
	}

	public void setSession_name(String session_name) {
		this.session_name = session_name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
