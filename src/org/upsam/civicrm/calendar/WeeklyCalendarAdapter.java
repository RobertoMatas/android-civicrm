package org.upsam.civicrm.calendar;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.upsam.civicrm.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeeklyCalendarAdapter extends ArrayAdapter<WeeklyCalendarItem> {
	
    private Context mContext;
	private final LayoutInflater layoutInflater;


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
        	v.setEnabled(false);
        }
        dayOfWeekView.setVisibility(View.INVISIBLE);        	
        
        TextView hourOfDayView = (TextView)v.findViewById(R.id.hourOfDay);
        hourOfDayView.setText(item.getHour());
       if ((item.isFirstColumn())){
        	hourOfDayView.setVisibility(View.VISIBLE);
        }
        else {
        	hourOfDayView.setVisibility(View.INVISIBLE);
        }
        
        ImageView numEventsHourView = (ImageView)v.findViewById(R.id.num_events_per_hour);
        int numOfActivities = item.getNumOfActivities();
    	if (numOfActivities>0){
            numEventsHourView.setVisibility(View.VISIBLE);
 		
    	}
    	else{
            numEventsHourView.setVisibility(View.INVISIBLE);
  		
    	}
    	v.setBackgroundResource(R.drawable.item_background_focused);
        

        
        return v;
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		return !getItem(position).isFirstColumn();
	}


	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}



}
