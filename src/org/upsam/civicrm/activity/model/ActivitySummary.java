package org.upsam.civicrm.activity.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class ActivitySummary implements Comparable<ActivitySummary>{
	/**
	 * Nombre de la actividad
	 */
	@JsonProperty("activity_name")
	private String name;
	/**
	 * Estado en que se encuentra la actividad
	 */
	private String status;
	/**
	 * Resumen de la actividad
	 */
	private String subject;
	/**
	 * Fecha y hora de creación
	 */
	@JsonProperty("activity_date_time")
	private String dateTime;
	/**
	 * Contactos objetivo
	 */
	private Map<String, String> targets;
	/**
	 * Contactos asignados
	 */
	private Map<String, String> asignees;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * @return the targets
	 */
	public Map<String, String> getTargets() {
		return targets;
	}

	/**
	 * @return the asignees
	 */
	public Map<String, String> getAsignees() {
		return asignees;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public int compareTo(ActivitySummary another) {
		return this.dateTime.compareTo(another.getDateTime());
	}


	

}
