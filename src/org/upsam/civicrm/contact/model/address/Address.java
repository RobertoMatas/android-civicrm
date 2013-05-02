package org.upsam.civicrm.contact.model.address;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Address implements Comparable<Address> {
	/**
	 * Dirección completa
	 */
	@JsonProperty("street_address")
	private String address;
	/**
	 * Ciudad
	 */
	private String city;
	/**
	 * Datos suplementarios
	 */
	@JsonProperty("supplemental_address_1")
	private String supplementalAddress;
	/**
	 * Indica si es la dirección principal o no
	 */
	@JsonProperty("is_primary")
	private String primaryStr;
	/**
	 * Código postal
	 */
	@JsonProperty("postal_code")
	private String zipCode;
	/**
	 * ID del país
	 */
	@JsonProperty("country_id")
	private String countryId;
	/**
	 * Indica si es el principal
	 */
	@JsonIgnore
	private boolean isPrimary;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the supplementalAddress
	 */
	public String getSupplementalAddress() {
		return supplementalAddress;
	}

	/**
	 * @param supplementalAddress
	 *            the supplementalAddress to set
	 */
	public void setSupplementalAddress(String supplementalAddress) {
		this.supplementalAddress = supplementalAddress;
	}

	/**
	 * @return the primaryStr
	 */
	public String getPrimaryStr() {
		return primaryStr;
	}

	/**
	 * @param primaryStr
	 *            the primaryStr to set
	 */
	public void setPrimaryStr(String primaryStr) {
		this.primaryStr = primaryStr;
		this.isPrimary = primaryStr != null && primaryStr.equals("1") ? true
				: false;
	}

	/**
	 * @return the isPrimary
	 */
	public boolean isPrimary() {
		return isPrimary;
	}

	/**
	 * @param isPrimary
	 *            the isPrimary to set
	 */
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	@Override
	public int compareTo(Address another) {
		if (this.isPrimary)
			return -1;
		if (another.isPrimary)
			return 1;
		return this.address.compareTo(another.getAddress());
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the countryId
	 */
	public String getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId
	 *            the countryId to set
	 */
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

}
