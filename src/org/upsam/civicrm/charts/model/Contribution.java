package org.upsam.civicrm.charts.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Contribution {
	/**
	 * Fecha en que se recibe la donación
	 */
	@JsonProperty("receive_date")
	private String strReceiveDate;
	/**
	 * Total de la donación
	 */
	@JsonProperty("total_amount")
	private float amount;
	/**
	 * Moneda
	 */
	private String currency;
	/**
	 * Año de la donación
	 */
	@JsonIgnore
	private int year;

	/**
	 * ID de la donación
	 */
	private int id;

	/**
	 * @return the strReceiveDate
	 */
	public String getStrReceiveDate() {
		return strReceiveDate;
	}

	/**
	 * @param strReceiveDate
	 *            the strReceiveDate to set
	 */
	public void setStrReceiveDate(String strReceiveDate) {
		this.strReceiveDate = strReceiveDate;
		if (strReceiveDate != null) {
			this.year = Integer.parseInt(strReceiveDate.split("-")[0]);
		}
	}

	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

}
