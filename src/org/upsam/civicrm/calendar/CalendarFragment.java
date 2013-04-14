package org.upsam.civicrm.calendar;

import java.util.Calendar;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.activity.model.ListActivities;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;
import org.upsam.civicrm.util.Utilities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;

public abstract class CalendarFragment extends Fragment {
	protected static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

	protected static final int OFFSET = 25;
	
	private String lastRequestCacheKey;

	private ProgressDialog progressDialog;

	private SpiceManager contentManager;
	
	private int page = 1;

	private String type = null;
	
	private Calendar month;
	
	private CalendarAdapter adapter;

	
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
		Bundle arguments = getArguments();
		this.type = arguments.getString("calendar_type");
		//performRequest(this.type, page);
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
	

	protected void performRequest(String type, int page) {
		this.progressDialog = Utilities.showLoadingProgressDialog(
				this.progressDialog, this.getActivity(),
				getString(R.string.progress_bar_msg_generico));
		CiviCRMAsyncRequest<ListActivities> request = buildReq(type, page);
		lastRequestCacheKey = request.createCacheKey();
		contentManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ONE_MINUTE, new ListActivitiesRequestListener());
	}
	
	protected abstract CiviCRMAsyncRequest<ListActivities> buildReq(String type, int page); /*{
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				5);
		if (page != 1) {
			params.add("offset", Integer.toString(((page - 1) * OFFSET)));
		}
		params.add("return[display_name]", "1");
		params.add("return[contact_type]", "1");
		params.add("return[contact_sub_type]", "1");
		if (type != null && !"".equals(type)) {
			params.add("contact_type", type);
		}
		CiviCRMAsyncRequest<ListActivities> req = new CiviCRMAsyncRequest<ListActivities>(
				this.getActivity(), ListActivities.class, ACTION.get,
				ENTITY.Activity, params);
		Log.d("ContactAutoCompleteListAdapter", "Request:" + req.getUriReq());
		return req;
	}*/
	
	public void refreshCalendar()
	{
		TextView title  = (TextView) this.getView().findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();				

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public String getLastRequestCacheKey() {
		return lastRequestCacheKey;
	}

	public void setLastRequestCacheKey(String lastRequestCacheKey) {
		this.lastRequestCacheKey = lastRequestCacheKey;
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public SpiceManager getContentManager() {
		return contentManager;
	}

	public void setContentManager(SpiceManager contentManager) {
		this.contentManager = contentManager;
	}

	public int getPage() {
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
	}
	
	public Calendar getMonth() {
		return month;
	}

	public void setMonth(Calendar month) {
		this.month = month;
	}

	public CalendarAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(CalendarAdapter adapter) {
		this.adapter = adapter;
	}



	private class ListActivitiesRequestListener implements
	RequestListener<ListActivities>, RequestProgressListener {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.e("DEBUG -------->", "Error during request: " + e.getMessage());
			Utilities.dismissProgressDialog(progressDialog);
		}
		
		@Override
		public void onRequestSuccess(ListActivities activities) {
			// listTweets could be null just if contentManager.getFromCache(...)
			// doesn't return anything.
			Utilities.dismissProgressDialog(progressDialog);
			if (activities == null) {
				Log.e("ERROR --------->", "Actividad creada!!!!");
				return;
			}
			Log.d("DEBUG --------->", "Actividad creada!!!!");
			Log.d("DEBUG --------->", activities.getIsError());
		
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
