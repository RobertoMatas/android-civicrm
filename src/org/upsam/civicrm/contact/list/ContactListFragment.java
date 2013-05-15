package org.upsam.civicrm.contact.list;

import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.ContactDetailFragmentActivity;
import org.upsam.civicrm.contact.list.EndlessScrollListener.onScrollEndListener;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.dagger.di.CiviCRMSpiceRequest;
import org.upsam.civicrm.dagger.di.fragment.SpiceDIAwareFragmentNewApi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactListFragment extends SpiceDIAwareFragmentNewApi {

	private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

	private ContactListAdapter contactsAdapter;

	private String lastRequestCacheKey;

	private ProgressBar progressBar;

	private String type = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_contact_list, container,
				false);
		this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		return view;
	}

	/* (non-Javadoc)
	 * @see org.upsam.civicrm.dagger.di.fragment.SpiceDIAwareFragmentNewApi#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUIComponents();
	}



	private void initUIComponents() {
		ListView contactList = (ListView) getView().findViewById(R.id.listResults);

		contactsAdapter = new ContactListAdapter(getActivityContext(),
				new ListContacts());
		contactList.setAdapter(contactsAdapter);

		contactList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivityContext(),
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
						performRequest(type, page);
					}
				}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		Bundle arguments = getArguments();
		this.type = arguments.getString("contact_type");
		performRequest(this.type, 1);
	}

	private void performRequest(String type, int page) {
		this.progressBar.setVisibility(View.VISIBLE);
		CiviCRMSpiceRequest<ListContacts> request = getRequestBuilder()
				.requestListContact(page, type);
		lastRequestCacheKey = request.createCacheKey();
		getSpiceManager().execute(request, lastRequestCacheKey,
				DurationInMillis.ONE_MINUTE, new ListContactsRequestListener());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (!TextUtils.isEmpty(lastRequestCacheKey)) {
			outState.putString(KEY_LAST_REQUEST_CACHE_KEY, lastRequestCacheKey);
		}
		super.onSaveInstanceState(outState);
	}

	private class ListContactsRequestListener implements
			RequestListener<ListContacts> {

		@Override
		public void onRequestFailure(SpiceException e) {
			progressBar.setVisibility(View.GONE);
			Toast.makeText(getActivityContext(),
					getString(R.string.general_http_error), Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onRequestSuccess(ListContacts listContacts) {
			if (listContacts == null) {
				return;
			}
			contactsAdapter.addAll(listContacts.getValues());
			contactsAdapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);

		}
	}
}
