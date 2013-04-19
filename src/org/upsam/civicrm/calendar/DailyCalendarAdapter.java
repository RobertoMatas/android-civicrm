package org.upsam.civicrm.calendar;

import java.util.ArrayList;
import java.util.List;

import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivitySummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DailyCalendarAdapter extends ArrayAdapter<ActivitySummary> {

	private final LayoutInflater layoutInflater;

	public DailyCalendarAdapter(Context context, List<ActivitySummary> activitiesPerDay) {
		super(context, R.layout.daily_calendar_item, activitiesPerDay != null ? activitiesPerDay : new ArrayList<ActivitySummary>(0));
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ActivitySummary activitySummary = getItem(position);

		View view = convertView;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.daily_calendar_item, parent, false);
		}

				
		TextView displayName = (TextView) view.findViewById(R.id.activitySumaryName);
		displayName.setText(activitySummary.getName());

		TextView dateTime = (TextView) view.findViewById(R.id.activitySumaryDateTime);
		dateTime.setText(activitySummary.getDateTime());
		
		TextView subject = (TextView) view.findViewById(R.id.ActivitySummarySubject);
		subject.setText(activitySummary.getSubject());
		
		return view;
	}
	
	
}
