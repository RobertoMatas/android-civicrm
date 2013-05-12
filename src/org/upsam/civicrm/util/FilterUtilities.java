package org.upsam.civicrm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.activity.model.ActivityHeader;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;

import android.util.Log;

public class FilterUtilities {
	
	public static MultiValueMap<String, ActivitySummary> filterScheduledActivitiesByDates(ListActivtiesSummary listActivitiesSummary, String contactId){
		MultiValueMap<String, ActivitySummary> activitiesFiltered = new LinkedMultiValueMap<String, ActivitySummary>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",new Locale("es_ES"));
		
		Date dateKey = null;
		List<ActivitySummary> activitiesSummary = listActivitiesSummary.getValues();
		for (ActivitySummary activitySummary : activitiesSummary) {
			if (activitySummary.getAsignees()!= null && activitySummary.getAsignees().containsKey(contactId) && "scheduled".equalsIgnoreCase(activitySummary.getStatus())){
				try {
					dateKey = sdf.parse(activitySummary.getDateTime());
				} catch (ParseException e) {
					Log.e("ERROR", "Error de parseo de la fecha de una actividad");
				}
				activitiesFiltered.add(android.text.format.DateFormat.format("yyyyMMdd",dateKey).toString(),activitySummary);
			
			}
		}
		return activitiesFiltered;
	}
	
	public static List<ActivityHeader> filterScheduledActivitiesByHour(List<ActivitySummary> todayActivities){
		MultiValueMap<String, ActivitySummary> activitiesFiltered = new LinkedMultiValueMap<String, ActivitySummary>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",new Locale("es_ES"));
		Date dateKey = null;
		List<ActivityHeader> headers = new ArrayList<ActivityHeader>();
		ActivityHeader header = null;
		List<ActivitySummary> sumaries = null;
		if (todayActivities != null){
			for (ActivitySummary activitySummary : todayActivities) {
				try {
					dateKey = sdf.parse(activitySummary.getDateTime());
				} catch (ParseException e) {
					Log.e("ERROR", "Error de parseo de la fecha de una actividad");
				}	
				activitiesFiltered.add(android.text.format.DateFormat.format("kk",dateKey).toString(),activitySummary);
			}
		}
		String hour = null;
		for (int i=0; i<24;i++){
			sumaries = new ArrayList<ActivitySummary>();
			header = new ActivityHeader();
			if (i<10){
				hour = "0"+i;
			}
			else {
				hour = ""+i;
			}
			header.setHour(hour);

			if (activitiesFiltered.containsKey(hour)){
				sumaries = activitiesFiltered.get(hour);
			}
			Collections.sort(sumaries);
			header.setActivitiesPerHour(sumaries);
			headers.add(header);
		}
		return headers;
	}
	
	public static int getNumberOfActivitiesByHour(List<ActivitySummary> todayActivities, String hour){
		MultiValueMap<String, ActivitySummary> activitiesFiltered = new LinkedMultiValueMap<String, ActivitySummary>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",new Locale("es_ES"));
		Date dateKey = null;
		int numberOfActivities = 0;
		if (todayActivities != null){
			String key = null;
			for (ActivitySummary activitySummary : todayActivities) {
				try {
					dateKey = sdf.parse(activitySummary.getDateTime());
				} catch (ParseException e) {
					Log.e("ERROR", "Error de parseo de la fecha de una actividad");
				}
				key = android.text.format.DateFormat.format("kk",dateKey).toString();
				activitiesFiltered.add(key,activitySummary);
				
			}
		}
		if (activitiesFiltered.containsKey(hour)){
			numberOfActivities = activitiesFiltered.get(hour).size();
		}
		return numberOfActivities;
	}
}
