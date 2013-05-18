package org.upsam.civicrm.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivityHeader;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.util.CiviCRMRequestHelper;
import org.upsam.civicrm.util.FilterUtilities;
import org.upsam.civicrm.util.Utilities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;

public class DailyExpandableCalendarFragment extends CalendarFragment {
	
	private DailyExpandableCalendarAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.daily_expandable_calendar, container, false);
		setProgressBar((ProgressBar) view.findViewById(R.id.progressBarDailyExpandableCalendar));
		return view;
	}
	
	@Override
	protected void initUIComponents() {
		setMonth(Calendar.getInstance());
		
		ExpandableListView dailyCalendar = (ExpandableListView) getView().findViewById(
				R.id.dailyExpandableActivities);

		adapter = new DailyExpandableCalendarAdapter(getActivity(),
				new ArrayList<ActivityHeader>());
		dailyCalendar.setAdapter(adapter);
		dailyCalendar.setOnGroupClickListener(groupClicked);
		
	    TextView title  = (TextView) this.getView().findViewById(R.id.dailyExpandableTitle);
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras!= null){
			String selectedDay = extras.getString("selectedDay");
			if (selectedDay != null){
				int day = Integer.valueOf(selectedDay.substring(6, 8));
				int month = Integer.valueOf(selectedDay.substring(4, 6))-1;
				int year = Integer.valueOf(selectedDay.substring(0, 4));
				getMonth().set(year,month,day);
			}
		}
	    title.setText(android.text.format.DateFormat.format("EEEE, dd MMMM yyyy", getMonth()));

	}

	@Override
	protected void performRequest() {
		getProgressBar().setVisibility(View.VISIBLE);
		CiviCRMAsyncRequest<ListActivtiesSummary> request = CiviCRMRequestHelper.requestActivitiesForContact(Integer.valueOf(Utilities.getContactId(getActivity())), this.getActivity());
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
				Log.e("ERROR --------->", "Actividades vac�as!!!");
				return;
			}
			setActivitiesPerDay(FilterUtilities.filterScheduledActivitiesByDates(activities, Utilities.getContactId(getActivity())));
			Bundle extras = getActivity().getIntent().getExtras();
			String selectedDay = extras != null?extras.getString("selectedDay"):null;
			List<ActivitySummary> todayActivities = null;
			if (selectedDay == null){
				Calendar selectedDate = getMonth();
				String year = ""+selectedDate.get(Calendar.YEAR);
				String month = ""+(selectedDate.get(Calendar.MONTH)+1);
				if (month.length() == 1) {
					month = "0"+month;
				}
				String day = ""+selectedDate.get(Calendar.DAY_OF_MONTH);
				todayActivities = getActivitiesPerDay().get(year+month+day);				
			}
			else {
				todayActivities = getActivitiesPerDay().get(selectedDay);
			}

			if (todayActivities !=null && !todayActivities.isEmpty()){
				Collections.sort(todayActivities);
			}
			else {

			}
			adapter.setHeaders(FilterUtilities.filterScheduledActivitiesByHour(todayActivities));
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
	
	//our group listener
	 private OnGroupClickListener groupClicked =  new OnGroupClickListener() {
	 
	  public boolean onGroupClick(ExpandableListView parent, View v,
	    int groupPosition, long id) {
		  	if (parent.isGroupExpanded(groupPosition)){
		    	return  parent.expandGroup(groupPosition);
		    }
		    else {
		    	return parent.collapseGroup(groupPosition);
		    }
		
	    
	  }
	   
	 };
}
