package org.upsam.civicrm.calendar;

import java.util.Calendar;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ListActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class MonthlyCalendarFragment extends CalendarFragment {
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.calendar, container,
				false);
		return view;
	}

	@Override
	protected void initUIComponents() {
		// TODO Auto-generated method stub
		if (getMonth() == null){
			setMonth(Calendar.getInstance());			
		}
		
	    GridView calendar = (GridView) getView().findViewById(R.id.calendar);
	    
	   
	    setAdapter(new CalendarAdapter(this.getActivity(), getMonth()));
		calendar.setAdapter(getAdapter());
		
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
	protected CiviCRMAsyncRequest<ListActivities> buildReq(String type, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
