package org.upsam.civicrm.beans;

/**
 * Objeto guarda los datos de autenticación
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class DataCivi 
{	
	private String api_key;
	
	private String key;
	
	private String user_name;
	
	private String password;
	
	private String site_key;
	
	private String base_url;

	/**
	 * @return the api_key
	 */
	public String getApi_key() {
		return api_key;
	}

	/**
	 * @param api_key the api_key to set
	 */
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the site_key
	 */
	public String getSite_key() {
		return site_key;
	}

	/**
	 * @param site_key the site_key to set
	 */
	public void setSite_key(String site_key) {
		this.site_key = site_key;
	}

	/**
	 * @return the base_url
	 */
	public String getBase_url() {
		return base_url;
	}

	/**
	 * @param base_url the base_url to set
	 */
	public void setBase_url(String base_url) {
		this.base_url = base_url;
	}

}
