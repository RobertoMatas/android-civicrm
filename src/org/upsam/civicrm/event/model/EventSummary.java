package org.upsam.civicrm.event.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class EventSummary implements Comparable<EventSummary>{
	
	@JsonProperty("event_title")
	private String title;
	
	@JsonProperty("event_type")
	private String type;
	
	@JsonProperty("event_start_date")
	private String startDate;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the start_date
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param start_date the start_date to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Override
	public int compareTo(EventSummary another) {
		// TODO Auto-generated method stub
		return startDate.compareTo(another.getStartDate());
	}
	
	
	
}
