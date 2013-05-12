package org.upsam.civicrm.calendar;

import java.util.List;

import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivityHeader;
import org.upsam.civicrm.activity.model.ActivitySummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class DailyExpandableCalendarAdapter extends BaseExpandableListAdapter {
	
	Context context;
	List<ActivityHeader> headers;
	
	
	
	public DailyExpandableCalendarAdapter() {
		super();
	}

	public DailyExpandableCalendarAdapter(Context context,
			List<ActivityHeader> headers) {
		super();
		this.context = context;
		this.headers = headers;
	}
	
	

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<ActivityHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<ActivityHeader> headers) {
		this.headers = headers;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<ActivitySummary> activitiesPerHour = headers.get(groupPosition).getActivitiesPerHour();
		return activitiesPerHour.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
			   View view, ViewGroup parent) {
		ActivitySummary activitySummary = (ActivitySummary)getChild(groupPosition, childPosition);


		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.daily_calendar_item, parent, false);
		}

				
		TextView displayName = (TextView) view.findViewById(R.id.activitySumaryName);
		displayName.setText(activitySummary.getName());

		TextView dateTime = (TextView) view.findViewById(R.id.activitySumaryDateTime);
		dateTime.setText(activitySummary.getDateTime());
		
		TextView subject = (TextView) view.findViewById(R.id.ActivitySummarySubject);
		subject.setText(activitySummary.getSubject());
		view.setBackgroundResource(R.drawable.item_background);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return headers.get(groupPosition).getActivitiesPerHour().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return headers.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return headers.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			   ViewGroup parent) {
		ActivityHeader group = (ActivityHeader) getGroup(groupPosition);
		if (view == null){
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.daily_expandable_header_calendar, parent, false);
		}
		
		TextView hour = (TextView) view.findViewById(R.id.hour);
		hour.setText(group.getHour());
		  
		TextView numberOfActivitiesPerHour = (TextView) view.findViewById(R.id.numberOfActivitiesPerHour);
		int numberPerHour = group.getActivitiesPerHour().size();
		numberOfActivitiesPerHour.setText(numberPerHour != 0 ?numberPerHour+" Actividades":"");
		if (numberPerHour == 0){
			view.setClickable(false);
		}
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

}
