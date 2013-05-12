package org.upsam.civicrm.activity.model;

import java.util.List;

public class ActivityHeader implements Comparable<ActivityHeader>{
	
	private String hour;
	private List<ActivitySummary> activitiesPerHour;
	
	
	
	public ActivityHeader() {
		super();
	}
	
	public ActivityHeader(String hour, List<ActivitySummary> activitiesPerHour) {
		super();
		this.hour = hour;
		this.activitiesPerHour = activitiesPerHour;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public List<ActivitySummary> getActivitiesPerHour() {
		return activitiesPerHour;
	}
	public void setActivitiesPerHour(List<ActivitySummary> activitiesPerHour) {
		this.activitiesPerHour = activitiesPerHour;
	}

	@Override
	public int compareTo(ActivityHeader another) {
		return this.hour.compareTo(another.hour);
	}
	
	
}
