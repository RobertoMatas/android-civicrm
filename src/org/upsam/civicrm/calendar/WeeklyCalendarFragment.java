package org.upsam.civicrm.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.util.CiviCRMRequestHelper;
import org.upsam.civicrm.util.FilterUtilities;
import org.upsam.civicrm.util.Utilities;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;



public class WeeklyCalendarFragment extends CalendarFragment {
	
	private WeeklyCalendarAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.weekly_calendar, container, false);
		setProgressBar((ProgressBar) view.findViewById(R.id.progressBarWeeklyCalendar));
		return view;
	}
	
	@Override
	protected void initUIComponents() {
		setMonth(GregorianCalendar.getInstance());			
		GridView calendar = (GridView) getView().findViewById(R.id.weeklyCalendar);
	    
	    adapter = new WeeklyCalendarAdapter(this.getActivity(), new ArrayList<WeeklyCalendarItem>(0));
		calendar.setAdapter(adapter);
		
		calendar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView dayView = (TextView)view.findViewById(R.id.dayOfWeek);
				String day = dayView.getText().toString();
				if (day.length()==1){
					day = "0"+day;
				}
				getActivity().getIntent().putExtra("selectedDay",""+android.text.format.DateFormat.format("yyyyMM", getMonth())+day);
				ActionBar bar = getActivity().getActionBar();
				bar.selectTab(bar.getTabAt(2));
			}
			
			
		});

		
		
		
	    TextView title  = (TextView) this.getView().findViewById(R.id.weekTitle);
	    title.setText(getMonth().get(Calendar.WEEK_OF_MONTH) + " "+getString(R.string.weekOf)+android.text.format.DateFormat.format("MMMM yyyy", getMonth()));
		

		
	}

	@Override
	protected void performRequest() {
		getProgressBar().setVisibility(View.VISIBLE);
		CiviCRMAsyncRequest<ListActivtiesSummary> request = CiviCRMRequestHelper.requestActivitiesForContact(Integer.valueOf(Utilities.getContactId(getActivity())), this.getActivity());
		setLastRequestCacheKey(request.createCacheKey());
		getContentManager().execute(request, getLastRequestCacheKey(),
					DurationInMillis.ONE_MINUTE, new ListActivitiesRequestListener());
	}


	
	private void initializeWeeklyCalendar(){
		List<WeeklyCalendarItem> items = new ArrayList<WeeklyCalendarItem>(24*7);
	    String[] weekDays = new String[7];
		int dayOfWeek= getMonth().get(Calendar.DAY_OF_WEEK);
		int firstDayOfWeek = getMonth().getFirstDayOfWeek();
		if (firstDayOfWeek==2){
			if (dayOfWeek == 1){
				dayOfWeek = 6;
			}
			else{
				dayOfWeek = dayOfWeek - 2;
			}
		}
		else {
			if (dayOfWeek == 1){
				dayOfWeek = 0;
			}
			else{
				dayOfWeek = dayOfWeek - 1;
			}
		}

		int dayOfMonth = getMonth().get(Calendar.DAY_OF_MONTH);
		int daysOfMonth = getMonth().getMaximum(Calendar.DAY_OF_MONTH);
		weekDays[dayOfWeek] = ""+dayOfMonth;
		for (int i = dayOfWeek+1;i<weekDays.length;i++){
			if ("".equals(weekDays[i-1]) || Integer.valueOf(weekDays[i-1])+1 > daysOfMonth){
				weekDays[i] = "";
			}
			else {
				weekDays[i]=""+(Integer.valueOf(weekDays[i-1])+1);
			}
		}
		for (int i = dayOfWeek-1;i>=0;i--){
			if ("".equals(weekDays[i+1]) || Integer.valueOf(weekDays[i+1])+1<3){
				weekDays[i] = "";
			}
			else {
				weekDays[i]=""+(Integer.valueOf(weekDays[i+1])-1);
			}
		}
    	String monthStr = ""+(getMonth().get(Calendar.MONTH)+1);
    	if(monthStr.length()==1) {
    		monthStr = "0"+monthStr;
    	}
    	String yearStr = ""+getMonth().get(Calendar.YEAR);
        List<ActivitySummary> listActivitiesPerDay = null;
        

		WeeklyCalendarItem item = null;
		String day = null;
		String hour = null;
		Integer numOfActivities;
		int fila = 0;
		int columna = 0;
		for(int i = 0 ; i<24*8; i++){
				numOfActivities = 0;
				columna = i%8;
				fila = i/8;
				if (columna==0){
					day="d";
				}
				else{
					day = new String(weekDays[columna-1].length()==1?"0"+weekDays[columna-1]:weekDays[columna-1]);
				}
				
				hour = new String(fila<10?"0"+(fila):""+fila);
				listActivitiesPerDay = getActivitiesPerDay().get(yearStr+monthStr+day);
		        if (listActivitiesPerDay != null){
		        	numOfActivities = FilterUtilities.getNumberOfActivitiesByHour(listActivitiesPerDay, hour);
		        }
		        item = new WeeklyCalendarItem(hour, day, numOfActivities, fila==0, columna==0);
				items.add(item);
		}
		adapter.addAll(items);
		adapter.notifyDataSetChanged();
		initializeDaysRow(weekDays);

	}
	
    private void initializeDaysRow(String[] days){
    	int firstDayOfWeek = getMonth().getFirstDayOfWeek();
		TextView one  = (TextView) this.getView().findViewById(R.id.day1W);
		TextView two  = (TextView) this.getView().findViewById(R.id.day2W);
		TextView three  = (TextView) this.getView().findViewById(R.id.day3W);
		TextView four  = (TextView) this.getView().findViewById(R.id.day4W);
		TextView five  = (TextView) this.getView().findViewById(R.id.day5W);
		TextView six  = (TextView) this.getView().findViewById(R.id.day6W);
		TextView seven  = (TextView) this.getView().findViewById(R.id.day7W);
    	if (firstDayOfWeek==1){
    		one.setText(getString(R.string.sunday)+days[0]);
    		two.setText(getString(R.string.monday)+days[1]);
    		three.setText(getString(R.string.tuesday)+days[2]);
    		four.setText(getString(R.string.wednesday)+days[3]);
    		five.setText(getString(R.string.thursday)+days[4]);
    		six.setText(getString(R.string.friday)+days[5]);
    		seven.setText(getString(R.string.saturday)+days[6]);

    	}
    	else{
    		one.setText(getString(R.string.monday)+days[0]);
    		two.setText(getString(R.string.tuesday)+days[1]);
    		three.setText(getString(R.string.wednesday)+days[2]);
    		four.setText(getString(R.string.thursday)+days[3]);
    		five.setText(getString(R.string.friday)+days[4]);
    		six.setText(getString(R.string.saturday)+days[5]); 
    		seven.setText(getString(R.string.sunday)+days[6]);
    	}
    	this.getView().findViewById(R.id.daysWTableLayout).setVisibility(View.VISIBLE);
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
			setActivitiesPerDay(FilterUtilities.filterScheduledActivitiesByDates(activities, Utilities.getContactId(getActivity())));
			initializeWeeklyCalendar();
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
