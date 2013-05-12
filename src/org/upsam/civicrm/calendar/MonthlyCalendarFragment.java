package org.upsam.civicrm.calendar;

import java.util.Calendar;

import org.springframework.util.LinkedMultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.activity.model.ListActivtiesSummary;
import org.upsam.civicrm.util.CiviCRMRequestHelper;
import org.upsam.civicrm.util.FilterUtilities;
import org.upsam.civicrm.util.Utilities;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MonthlyCalendarFragment extends CalendarFragment {
	
	private CalendarAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.calendar, container, false);
		setProgressBar((ProgressBar) view.findViewById(R.id.progressBarCalendar));
		return view;
	}

	@Override
	protected void initUIComponents() {
		
		setMonth(Calendar.getInstance());			
		GridView calendar = (GridView) getView().findViewById(R.id.calendar);
	    
	    adapter = new CalendarAdapter(this.getActivity(), getMonth(),new LinkedMultiValueMap<String, ActivitySummary>());
		calendar.setAdapter(adapter);
		
		calendar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView dayView = (TextView)view.findViewById(R.id.date);
				String day = dayView.getText().toString();
				if (day.length()==1){
					day = "0"+day;
				}
				getActivity().getIntent().putExtra("selectedDay",""+android.text.format.DateFormat.format("yyyyMM", getMonth())+day);
				ActionBar bar = getActivity().getActionBar();
				bar.selectTab(bar.getTabAt(2));
			}
			
			
		});
		
	    TextView title  = (TextView) this.getView().findViewById(R.id.title);
	    title.setText(android.text.format.DateFormat.format("MMMM yyyy", getMonth()));
		
	    TextView previous = (TextView) getView().findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(getMonth().get(Calendar.MONTH)== getMonth().getActualMinimum(Calendar.MONTH)) {				
					getMonth().set((getMonth().get(Calendar.YEAR)-1),getMonth().getActualMaximum(Calendar.MONTH),1);
				} else {
					getMonth().set(Calendar.MONTH,getMonth().get(Calendar.MONTH)-1);
				}
				refreshCalendar();
			}
		});
		TextView next = (TextView) getView().findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(getMonth().get(Calendar.MONTH)== getMonth().getActualMaximum(Calendar.MONTH)) {				
					getMonth().set((getMonth().get(Calendar.YEAR)+1),getMonth().getActualMinimum(Calendar.MONTH),1);
				} else {
					getMonth().set(Calendar.MONTH,getMonth().get(Calendar.MONTH)+1);
				}
				refreshCalendar();

			}
		});
	}
	
	@Override
	protected void performRequest() {
		getProgressBar().setVisibility(View.VISIBLE);
		CiviCRMAsyncRequest<ListActivtiesSummary> request = CiviCRMRequestHelper.requestActivitiesForContact(Integer.valueOf(Utilities.getContactId(getActivity())), this.getActivity());
		setLastRequestCacheKey(request.createCacheKey());
		getContentManager().execute(request, getLastRequestCacheKey(),
					DurationInMillis.ONE_MINUTE, new ListActivitiesRequestListener());
	}
	
	private void refreshCalendar()
	{
		TextView title  = (TextView) this.getView().findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();				

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", getMonth()));
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
			adapter.setActivitiesPerDay(getActivitiesPerDay());
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
