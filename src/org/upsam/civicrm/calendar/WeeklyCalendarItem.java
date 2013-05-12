package org.upsam.civicrm.calendar;

public class WeeklyCalendarItem {
	
	private String hour;
	private String dayOfMonth;
	private Integer numOfActivities;
	private boolean isFirstRow;
	private boolean isFirstColumn;
	
	
	
	public WeeklyCalendarItem() {
		super();
	}
	public WeeklyCalendarItem(String hour, String dayOfMonth,
			Integer numOfActivities, boolean isFirstRow, boolean isFirstColumn) {
		super();
		this.hour = hour;
		this.dayOfMonth = dayOfMonth;
		this.numOfActivities = numOfActivities;
		this.isFirstRow = isFirstRow;
		this.isFirstColumn = isFirstColumn;
	}
	/**
	 * @return the hour
	 */
	public String getHour() {
		return hour;
	}
	/**
	 * @param hour the hour to set
	 */
	public void setHour(String hour) {
		this.hour = hour;
	}
	/**
	 * @return the dayOfMonth
	 */
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	/**
	 * @param dayOfMonth the dayOfMonth to set
	 */
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	/**
	 * @return the numOfActivities
	 */
	public Integer getNumOfActivities() {
		return numOfActivities;
	}
	/**
	 * @param numOfActivities the numOfActivities to set
	 */
	public void setNumOfActivities(Integer numOfActivities) {
		this.numOfActivities = numOfActivities;
	}
	/**
	 * @return the isFirstRow
	 */
	public boolean isFirstRow() {
		return isFirstRow;
	}
	/**
	 * @param isFirstRow the isFirstRow to set
	 */
	public void setFirstRow(boolean isFirstRow) {
		this.isFirstRow = isFirstRow;
	}
	/**
	 * @return the isFirstColumn
	 */
	public boolean isFirstColumn() {
		return isFirstColumn;
	}
	/**
	 * @param isFirstColumn the isFirstColumn to set
	 */
	public void setFirstColumn(boolean isFirstColumn) {
		this.isFirstColumn = isFirstColumn;
	}
	
	
}
