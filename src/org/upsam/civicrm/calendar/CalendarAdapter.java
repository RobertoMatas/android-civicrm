package org.upsam.civicrm.calendar;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ActivitySummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	static final int FIRST_DAY_OF_WEEK =0; // Sunday = 0, Monday = 1
	
    private Calendar month;
    private Calendar selectedDate;
    private String[] days;
    private Context mContext;
    private MultiValueMap<String, ActivitySummary> activitiesPerDay;
    
	public CalendarAdapter(Context context, Calendar monthCalendar,MultiValueMap<String, ActivitySummary> activitiesPerDay) {
		month = monthCalendar;
    	selectedDate = (Calendar)monthCalendar.clone();
    	month.set(Calendar.DAY_OF_MONTH, 1);
    	mContext = context;
    	this.activitiesPerDay = activitiesPerDay;
    	//days = new String[month.get];
        //refreshDays();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
    	TextView dayView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);
        	
        }
        dayView = (TextView)v.findViewById(R.id.date);
        
        // disable empty days from the beginning
        if(days[position].equals("")) {
        	dayView.setClickable(false);
        	dayView.setFocusable(false);
        	dayView.setEnabled(false);
        	v.setClickable(false);
        	v.setFocusable(false);
        	v.setEnabled(false);
        	v.setBackgroundResource(R.drawable.item_background_focused);
        }
        else {
        	// mark current day as focused
        	if(month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) && month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) && days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
        		//v.setBackgroundColor(R.color.background);
        		v.setBackgroundResource(R.drawable.item_background_focused_green);
        	}
        	else {
        		v.setBackgroundResource(R.drawable.item_background_focused);
        	}
        }
        dayView.setText(days[position]);
        
        // create date string for comparison
        String date = days[position];
    	
        if(date.length()==1) {
    		date = "0"+date;
    	}
    	String monthStr = ""+(month.get(Calendar.MONTH)+1);
    	if(monthStr.length()==1) {
    		monthStr = "0"+monthStr;
    	}
    	String yearStr = ""+month.get(Calendar.YEAR);
        // show icon if date is not empty and it exists in the items array
        ImageView numberOfActivities = (ImageView)v.findViewById(R.id.activities);
        List<ActivitySummary> listActivitiesPerDay = activitiesPerDay.get(yearStr+monthStr+date);
        if (listActivitiesPerDay != null){
        	numberOfActivities.setVisibility(View.VISIBLE);
        }
        else{
        	numberOfActivities.setVisibility(View.INVISIBLE);       	
        }
        
        return v;
	}

	

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return days!=null?!StringUtils.EMPTY.equals(days[position]):false;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return days!=null?days.length:0;
	}


	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public MultiValueMap<String, ActivitySummary> getActivitiesPerDay() {
		return activitiesPerDay;
	}


	public void setActivitiesPerDay(
			MultiValueMap<String, ActivitySummary> activitiesPerDay) {
		this.activitiesPerDay = activitiesPerDay;
	}


	public void refreshDays()
    {

    	
    	int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = month.getFirstDayOfWeek();
        int multiplo = firstDayOfWeek==1?0:1;
        // figure size of the array
        if(firstDay==1){
        	days = new String[lastDay+(multiplo*6)];
        }
        else {
        	days = new String[lastDay+firstDay-(multiplo+1)];
        }
        
        int j=multiplo;
        
        // populate empty days before first real day
        if(firstDay>1) {
	        for(j=0;j<firstDay-multiplo;j++) {
	        	days[j] = "";
	        }
        }
	    else {
	    	for(j=0;j<multiplo*6;j++) {
	        	days[j] = "";
	        }
	    	j=multiplo*6+1; // sunday => 1, monday => 7
	    }
        
        // populate days
        int dayNumber = 1;
        for(int i=j-1;i<days.length;i++) {
        	days[i] = ""+dayNumber;
        	dayNumber++;
        }

    }
	
}
