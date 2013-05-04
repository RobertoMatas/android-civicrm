package org.upsam.civicrm.sync.dao;

import java.util.Date;

public class ContactValue {

	private int id;
	private Date fecha;
	private long contacId;
	private String idAndroid;
	private String uriAndroid;
	private String lookupAndroid;

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
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the contacId
	 */
	public long getContacId() {
		return contacId;
	}

	/**
	 * @param contacId
	 *            the contacId to set
	 */
	public void setContacId(long contacId) {
		this.contacId = contacId;
	}

	/**
	 * @return the idAndroid
	 */
	public String getIdAndroid() {
		return idAndroid;
	}

	/**
	 * @param idAndroid
	 *            the idAndroid to set
	 */
	public void setIdAndroid(String idAndroid) {
		this.idAndroid = idAndroid;
	}

	/**
	 * @return the uriAndroid
	 */
	public String getUriAndroid() {
		return uriAndroid;
	}

	/**
	 * @param uriAndroid
	 *            the uriAndroid to set
	 */
	public void setUriAndroid(String uriAndroid) {
		this.uriAndroid = uriAndroid;
	}

	/**
	 * @return the lookupAndroid
	 */
	public String getLookupAndroid() {
		return lookupAndroid;
	}

	/**
	 * @param lookupAndroid the lookupAndroid to set
	 */
	public void setLookupAndroid(String lookupAndroid) {
		this.lookupAndroid = lookupAndroid;
	}

	
}
