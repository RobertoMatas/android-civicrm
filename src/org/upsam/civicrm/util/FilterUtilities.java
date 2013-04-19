package org.upsam.civicrm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;

import android.util.Log;

public class FilterUtilities {
	
	public static MultiValueMap<String, ActivitySummary> filterScheduledActivitiesByDates(ListActivtiesSummary listActivitiesSummary, String contactId){
		MultiValueMap<String, ActivitySummary> activitiesFiltered = new LinkedMultiValueMap<String, ActivitySummary>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
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
}
