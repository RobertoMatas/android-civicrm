package org.upsam.civicrm.contact;

import java.util.Collection;

import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.Contact;
import org.upsam.civicrm.contact.model.ListContacts;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;

public class ContactListActivity extends Activity {

	private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

	private SpiceManager contentManager = new SpiceManager(CiviCRMAndroidSpiceService.class);

	private ArrayAdapter<String> contactsAdapter;

	private String lastRequestCacheKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_contact_list);
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		initUIComponents();
	}

	@Override
	protected void onStart() {
		contentManager.start(this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		contentManager.shouldStop();
		super.onStop();
	}

	private void initUIComponents() {
		ListView contactList = (ListView) findViewById(R.id.listView1);

		contactsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
		contactList.setAdapter(contactsAdapter);

		contactList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
			}
		});

		performRequest();
	}

	private void performRequest() {
		ContactListActivity.this.setProgressBarIndeterminateVisibility(true);
		ContactsRequest request = new ContactsRequest();
		lastRequestCacheKey = request.createCacheKey();
		contentManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListContactsRequestListener());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contact_list, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (!TextUtils.isEmpty(lastRequestCacheKey)) {
			outState.putString(KEY_LAST_REQUEST_CACHE_KEY, lastRequestCacheKey);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(KEY_LAST_REQUEST_CACHE_KEY)) {
			lastRequestCacheKey = savedInstanceState.getString(KEY_LAST_REQUEST_CACHE_KEY);
			contentManager.addListenerIfPending(ListContacts.class, lastRequestCacheKey, new ListContactsRequestListener());
			contentManager.getFromCache(ListContacts.class, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListContactsRequestListener());
		}
	}

	private class ListContactsRequestListener implements RequestListener<ListContacts>, RequestProgressListener {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(ContactListActivity.this, "Error during request: " + e.getMessage(), Toast.LENGTH_LONG).show();

		}

		@Override
		public void onRequestSuccess(ListContacts listContacts) {
			// listTweets could be null just if contentManager.getFromCache(...)
			// doesn't return anything.
			if (listContacts == null) {
				return;
			}

			contactsAdapter.clear();
			final Collection<Contact> contacts = listContacts.getValues().values();
			for (Contact contact : contacts) {
				contactsAdapter.add(contact.getName());
			}
			contactsAdapter.notifyDataSetChanged();
			ContactListActivity.this.setProgressBarIndeterminateVisibility(false);

		}

		@Override
		public void onRequestProgressUpdate(RequestProgress progress) {
			RequestStatus status = progress.getStatus();
			if (RequestStatus.LOADING_FROM_NETWORK.equals(status)) {
				Log.e("ContactListActivity", "Loading from network");
			} else if (RequestStatus.READING_FROM_CACHE.equals(status)) {
				Log.e("ContactListActivity", "Reading from cache");
			} else if (RequestStatus.WRITING_TO_CACHE.equals(status)) {
				Log.e("ContactListActivity", "Writing from cache");
			}

		}

	}
}
