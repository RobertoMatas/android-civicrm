package org.upsam.civicrm.calendar;

import java.util.Calendar;

import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.activity.model.ActivitySummary;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.octo.android.robospice.SpiceManager;

public abstract class CalendarFragment extends Fragment {

	
	private String lastRequestCacheKey;

	private ProgressBar progressBar;	
	
	private SpiceManager contentManager;
	
	private MultiValueMap<String, ActivitySummary> activitiesPerDay; 
	
	private Calendar month;
	


	@Override
	public void onStart() {
		contentManager.start(this.getActivity());
		super.onStart();
	}

	@Override
	public void onStop() {
		contentManager.shouldStop();
		super.onStop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);
		initUIComponents();
	}

	@Override
	public void onResume() {
		super.onResume();
		//Bundle arguments = getArguments();
		//this.type = arguments.getString("calendar_type");
		performRequest();
	}

	protected abstract void initUIComponents(); 
	
	   
/*		ListView contactList = (ListView) getView().findViewById(
				R.id.listResults);*/

/*		contactsAdapter = new ContactListAdapter(getActivity(),
				new ListContacts());
		contactList.setAdapter(contactsAdapter);

		contactList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						ContactDetailFragmentActivity.class);
				intent.putExtra("contact",
						(ContactSummary) contactsAdapter.getItem(position));
				startActivity(intent);
			}
		});

		contactList.setOnScrollListener(new EndlessScrollListener(
				new onScrollEndListener() {

					@Override
					public void onEnd(int page) {
						Log.d("ContactAutoCompleteListAdapter",
								"Hemos llegado al final del scroll, pagina a solicitar:"
										+ page);
						performRequest(type, page);
					}
				}));*/
	

	protected abstract void performRequest(); 

	public String getLastRequestCacheKey() {
		return lastRequestCacheKey;
	}

	public void setLastRequestCacheKey(String lastRequestCacheKey) {
		this.lastRequestCacheKey = lastRequestCacheKey;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public SpiceManager getContentManager() {
		return contentManager;
	}

	public void setContentManager(SpiceManager contentManager) {
		this.contentManager = contentManager;
	}

	public MultiValueMap<String, ActivitySummary> getActivitiesPerDay() {
		return activitiesPerDay;
	}

	public void setActivitiesPerDay(
			MultiValueMap<String, ActivitySummary> activitiesPerDay) {
		this.activitiesPerDay = activitiesPerDay;
	}

/*	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}*/
	
	public Calendar getMonth() {
		return month;
	}

	public void setMonth(Calendar month) {
		this.month = month;
	}
	
}
