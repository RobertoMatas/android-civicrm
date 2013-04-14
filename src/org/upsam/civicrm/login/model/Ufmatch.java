package org.upsam.civicrm.login.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * 
 * @author Equipo 7 Universidad Pontificia de Salamanca v1.0
 * 
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ufmatch {

	@JsonProperty("is_error")
	private int idError;

	@JsonProperty("contact_id")
	private String contactid;

	@JsonProperty("uf_id")
	private String ufid;

	@JsonProperty("uf_name")
	private String name;

	/**
	 * @return the idError
	 */
	public int getIdError() {
		return idError;
	}

	/**
	 * @param idError
	 *            the idError to set
	 */
	public void setIdError(int idError) {
		this.idError = idError;
	}

	/**
	 * @return the contactid
	 */
	public String getContactid() {
		return contactid;
	}

	/**
	 * @param contactid
	 *            the contactid to set
	 */
	public void setContactid(String contactid) {
		this.contactid = contactid;
	}

	/**
	 * @return the ufid
	 */
	public String getUfid() {
		return ufid;
	}

	/**
	 * @param ufid
	 *            the ufid to set
	 */
	public void setUfid(String ufid) {
		this.ufid = ufid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
