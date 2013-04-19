package org.upsam.civicrm.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.util.CiviCRMRequestHelper;
import org.upsam.civicrm.util.FilterUtilities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;



public class DailyCalendarFragment extends CalendarFragment{
	
	private DailyCalendarAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.daily_calendar, container, false);
		setProgressBar((ProgressBar) view.findViewById(R.id.progressBarDailyCalendar));
		return view;
	}
	
	@Override
	protected void initUIComponents() {
		
		setMonth(Calendar.getInstance());
		
		ListView dailyCalendar = (ListView) getView().findViewById(
				R.id.dailyActivities);

		adapter = new DailyCalendarAdapter(getActivity(),
				new ArrayList<ActivitySummary>());
		dailyCalendar.setAdapter(adapter);
		
	}

	@Override
	protected void performRequest() {
		getProgressBar().setVisibility(View.VISIBLE);
		CiviCRMAsyncRequest<ListActivtiesSummary> request = CiviCRMRequestHelper.requestActivitiesForContact(102, this.getActivity());
		setLastRequestCacheKey(request.createCacheKey());
		getContentManager().execute(request, getLastRequestCacheKey(),
					DurationInMillis.ONE_MINUTE, new ListActivitiesRequestListener());
	}
	
	private class ListActivitiesRequestListener implements
	RequestListener<ListActivtiesSummary>, RequestProgressListener {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.e("DEBUG -------->", "Error during request: " + e.getMessage());
			getProgressBar().setVisibility(View.INVISIBLE);
		}
		
		@Override
		public void onRequestSuccess(ListActivtiesSummary activities) {
			// listTweets could be null just if contentManager.getFromCache(...)
			// doesn't return anything.
			
			if (activities == null || activities.getValues() == null) {
				Log.e("ERROR --------->", "Actividades vac’as!!!");
				return;
			}
			setActivitiesPerDay(FilterUtilities.filterScheduledActivitiesByDates(activities, "102"));
			Calendar selectedDate = getMonth();
			String year = ""+selectedDate.get(Calendar.YEAR);
			String month = ""+(selectedDate.get(Calendar.MONTH)+1);
			if (month.length() == 1) {
				month = "0"+month;
			}
			String day = ""+selectedDate.get(Calendar.DAY_OF_MONTH);
			List<ActivitySummary> todayActivities = getActivitiesPerDay().get(year+month+day);
			if (todayActivities !=null && !todayActivities.isEmpty()){
				Collections.sort(todayActivities);
			}
			else {
				ActivitySummary noActivitiesForToday = new ActivitySummary();
				noActivitiesForToday.setName("No tiene actividades asignadas hoy");
				noActivitiesForToday.setDateTime("15-03-2013 10:22:25");
				noActivitiesForToday.setSubject("No tiene actividades asignadas hoy");
				todayActivities = new ArrayList<ActivitySummary>(1);
				todayActivities.add(noActivitiesForToday);
			}
			adapter.addAll(todayActivities);
			adapter.notifyDataSetChanged();
			getProgressBar().setVisibility(View.INVISIBLE);
		}
		
		@Override
		public void onRequestProgressUpdate(RequestProgress progress) {
			RequestStatus status = progress.getStatus();
			if (RequestStatus.LOADING_FROM_NETWORK.equals(status)) {
				Log.e("Activity ------>", "Loading from network");
			} else if (RequestStatus.READING_FROM_CACHE.equals(status)) {
				Log.e("Activity ------>", "Reading from cache");
			} else if (RequestStatus.WRITING_TO_CACHE.equals(status)) {
				Log.e("Activity ------>", "Writing from cache");
			}
	}
}

}
