package org.upsam.civicrm.calendar;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.upsam.civicrm.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeeklyCalendarAdapter extends ArrayAdapter<WeeklyCalendarItem> {
	
    private Context mContext;
	private final LayoutInflater layoutInflater;

    private Calendar month;
    private Calendar selectedDate;

    public WeeklyCalendarAdapter(Context context,List<WeeklyCalendarItem> items){
    	super(context, R.layout.weekly_calendar_item, items);
    	this.layoutInflater = LayoutInflater.from(context);
	 }


	@Override
	public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {  // if it's not recycled, initialize some attributes
        	//LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	v = layoutInflater.inflate(R.layout.weekly_calendar_item, parent, false);
        }
        WeeklyCalendarItem item = getItem(position);
        
        TextView dayOfWeekView = (TextView)v.findViewById(R.id.dayOfWeek);
        String day = item.getDayOfMonth();
        dayOfWeekView.setText(item.getDayOfMonth());
        if (StringUtils.isEmpty(day)){
        	v.setClickable(false);
        	v.setFocusable(false);       	
        }
        if (item.isFirstRow()){
        	dayOfWeekView.setVisibility(View.VISIBLE);
        }
        else{
        	dayOfWeekView.setVisibility(View.INVISIBLE);        	
        }
        
        TextView hourOfDayView = (TextView)v.findViewById(R.id.hourOfDay);
        hourOfDayView.setText(item.getHour());
       if ((item.isFirstColumn())){
        	hourOfDayView.setVisibility(View.VISIBLE);
        }
        else {
        	hourOfDayView.setVisibility(View.INVISIBLE);
        }
        
        TextView numEventsHourView = (TextView)v.findViewById(R.id.num_events_per_hour);
        int numOfActivities = item.getNumOfActivities();
    	numEventsHourView.setText(numOfActivities != 0?numOfActivities+" Act.":"");
    	v.setBackgroundResource(R.drawable.item_background);
        
/*        TextView dayOfWeekView = (TextView)v.findViewById(R.id.dayOfWeek);
        dayOfWeekView.setText(hours[position]);
        if (position>=0 && position<=6){
        	dayOfWeekView.setVisibility(View.VISIBLE);
        }
        if(hours[position].equals("")) {
        	v.setClickable(false);
        	v.setFocusable(false);
        }
        else {
        	v.setBackgroundResource(R.drawable.item_background);
        }
        
        int iHour = position/7;
        String strHour= iHour<10?"0"+iHour:""+iHour;
        
        TextView hourOfDayView = (TextView)v.findViewById(R.id.hourOfDay);
        hourOfDayView.setText(strHour);
        if (position%7 == 0){
        	hourOfDayView.setVisibility(View.VISIBLE);
        }
        
    	String monthStr = ""+(month.get(Calendar.MONTH)+1);
    	if(monthStr.length()==1) {
    		monthStr = "0"+monthStr;
    	}
    	String yearStr = ""+month.get(Calendar.YEAR);
    	String day = hours[position].length()==1?"0"+hours[position]:hours[position];
        TextView numEventsHourView = (TextView)v.findViewById(R.id.num_events_per_hour);
        List<ActivitySummary> listActivitiesPerDay = activitiesPerDay.get(yearStr+monthStr+day);
        if (listActivitiesPerDay != null){
        	int num = FilterUtilities.getNumberOfActivitiesByHour(listActivitiesPerDay, strHour);
        	numEventsHourView.setText(num != 0?num+" Act.":"");
        }
        else{
        	numEventsHourView.setText("");        	
        }*/
        
        return v;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}



}
