package org.upsam.civicrm.event.list;

import java.util.List;

import org.upsam.civicrm.R;
import org.upsam.civicrm.event.model.EventSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
			view = layoutInflater.inflate(R.layout.event_item, parent, false);
		}

				
		TextView displayName = (TextView) view.findViewById(R.id.eventSumaryName);
		displayName.setText(eventSummary.getType());
		displayName.setTextAppearance(getContext(), R.style.textoGreen);

		TextView dateTime = (TextView) view.findViewById(R.id.eventSummarySubject);
		dateTime.setText(eventSummary.getStartDate());
		dateTime.setTextAppearance(getContext(), R.style.textoWhite);

		TextView subject = (TextView) view.findViewById(R.id.eventSumaryDateTime);
		subject.setText(eventSummary.getTitle());
		subject.setTextAppearance(getContext(), R.style.textoWhite);
		ImageView imageActivity = (ImageView) view.findViewById(R.id.imageEvent);
		String name = eventSummary.getType();
		if ("Fundraiser".equals(name)){
			imageActivity.setImageResource(R.drawable.fundraiser);
		}
		else if("Conference".equals(name)){
			imageActivity.setImageResource(R.drawable.conference);
		}
		else if("Exhibition".equals(name)){
			imageActivity.setImageResource(R.drawable.exhibition);
		}
		else if("Meeting".equals(name)){
			imageActivity.setImageResource(R.drawable.meeting);
		}
		else if("Performance".equals(name)){
			imageActivity.setImageResource(R.drawable.performance);
		}
		else if("Workshop".equals(name)){
			imageActivity.setImageResource(R.drawable.workshop);
		}
		return view;
	}
	
	
	
}
