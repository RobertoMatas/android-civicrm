package org.upsam.civicrm.contact;

import org.upsam.civicrm.R;
import org.upsam.civicrm.SpiceAwareActivity;
import org.upsam.civicrm.contact.detail.ContactDetailFragmentActivity;
import org.upsam.civicrm.contact.model.ListContacts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactListActivity extends SpiceAwareActivity {

	private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

	private ContactListAdapter contactsAdapter;

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

	private void initUIComponents() {
		ListView contactList = (ListView) findViewById(R.id.listView1);

		contactsAdapter = new ContactListAdapter(getApplicationContext(), new ListContacts());
		contactList.setAdapter(contactsAdapter);

		contactList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ContactDetailFragmentActivity.class);
				intent.putExtra("contactId", contactsAdapter.getItemId(position));
				startActivity(intent);
			}
		});

		performRequest();
	}

	private void performRequest() {
		ContactListActivity.this.setProgressBarIndeterminateVisibility(true);
		showLoadingProgressDialog();
		//contentManager.removeAllDataFromCache();
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

	private class ListContactsRequestListener implements RequestListener<ListContacts> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(ContactListActivity.this, "Error during request: " + e.getMessage(), Toast.LENGTH_LONG).show();

		}

		@Override
		public void onRequestSuccess(ListContacts listContacts) {
			// listContacts could be null just if contentManager.getFromCache(...)
			// doesn't return anything.
			if (listContacts == null) {
				return;
			}			
			contactsAdapter.setContacts(listContacts);
			contactsAdapter.notifyDataSetChanged();
			ContactListActivity.this.setProgressBarIndeterminateVisibility(false);
			dismissProgressDialog();

		}

	}
}
