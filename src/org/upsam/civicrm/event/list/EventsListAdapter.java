package org.upsam.civicrm.event.list;

import java.util.List;

import org.upsam.civicrm.R;
import org.upsam.civicrm.event.model.EventSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventsListAdapter extends ArrayAdapter<EventSummary> {
	
	private final LayoutInflater layoutInflater;

	
	public EventsListAdapter(Context context,List<EventSummary> eventos) {
		super(context, R.layout.daily_calendar_item, eventos);
		this.layoutInflater = LayoutInflater.from(context);
	}


	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		EventSummary eventSummary = (EventSummary)getItem(position);


		if (view == null) {
			view = layoutInflater.inflate(R.layout.daily_calendar_item, parent, false);
		}

				
		TextView displayName = (TextView) view.findViewById(R.id.activitySumaryName);
		displayName.setText(eventSummary.getType());

		TextView dateTime = (TextView) view.findViewById(R.id.activitySumaryDateTime);
		dateTime.setText(eventSummary.getStartDate());
		
		TextView subject = (TextView) view.findViewById(R.id.ActivitySummarySubject);
		subject.setText(eventSummary.getTitle());
		return view;
	}
	
	
	
}
