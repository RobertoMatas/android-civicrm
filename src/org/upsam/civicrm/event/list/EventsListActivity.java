package org.upsam.civicrm.event.list;

import java.util.ArrayList;

import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.SpiceAwareActivity;
import org.upsam.civicrm.event.model.EventSummary;
import org.upsam.civicrm.event.model.ListEventsSummary;
import org.upsam.civicrm.util.CiviCRMRequestHelper;
import org.upsam.civicrm.util.Utilities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class EventsListActivity extends SpiceAwareActivity {

	private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

	private String lastRequestCacheKey;

	private ProgressBar progressBar;
	
	private EventsListAdapter eventsListAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar1);
		ListView listView = (ListView) this.findViewById(R.id.listResults);
		eventsListAdapter = new EventsListAdapter(getApplicationContext(), new ArrayList<EventSummary>());
		listView.setAdapter(eventsListAdapter);
		performRequest(1);
	}

	private void performRequest(int page) {
		this.progressBar.setVisibility(View.VISIBLE);
		CiviCRMAsyncRequest<ListEventsSummary> request = CiviCRMRequestHelper.requestEventsForContact(Integer.valueOf(Utilities.getContactId(this)),this);
		lastRequestCacheKey = request.createCacheKey();
		getContentManager().execute(request, lastRequestCacheKey,DurationInMillis.ONE_MINUTE, new ListEventsRequestListener());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (!TextUtils.isEmpty(lastRequestCacheKey)) {
			outState.putString(KEY_LAST_REQUEST_CACHE_KEY, lastRequestCacheKey);
		}
		super.onSaveInstanceState(outState);
	}

	private class ListEventsRequestListener implements
			RequestListener<ListEventsSummary> {

		@Override
		public void onRequestFailure(SpiceException e) {
			progressBar.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(),
					getString(R.string.general_http_error), Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onRequestSuccess(ListEventsSummary listEvents) {
			if (listEvents == null || listEvents.getValues() == null || listEvents.getValues().isEmpty()) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.sin_eventos), Toast.LENGTH_LONG)
						.show();
				progressBar.setVisibility(View.GONE);
				return;
			}
			eventsListAdapter.addAll(listEvents.getValues());
			eventsListAdapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);

		}
	}
}
