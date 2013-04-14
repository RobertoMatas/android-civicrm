package org.upsam.civicrm.contact.list;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.detail.ContactDetailFragmentActivity;
import org.upsam.civicrm.contact.list.EndlessScrollListener.onScrollEndListener;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactListFragment extends Fragment {

	private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

	private static final int OFFSET = 25;

	private ContactListAdapter contactsAdapter;

	private String lastRequestCacheKey;
	
	private ProgressBar progressBar;	

	private SpiceManager contentManager;

	private String type = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_contact_list, container, false);
		this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		return view;
	}

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
		this.type = arguments.getString("contact_type");
		performRequest(this.type, 1);
	}

	private void initUIComponents() {
		ListView contactList = (ListView) getView().findViewById(
				R.id.listResults);

		contactsAdapter = new ContactListAdapter(getActivity(),
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
				}));
	}

	private void performRequest(String type, int page) {		
		this.progressBar.setVisibility(View.VISIBLE);
		Log.d("ContactAutoCompleteListAdapter", "Pagina solicitada:" + page);
		CiviCRMAsyncRequest<ListContacts> request = buildReq(type, page);
		lastRequestCacheKey = request.createCacheKey();
		contentManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ONE_MINUTE, new ListContactsRequestListener());
	}

	protected CiviCRMAsyncRequest<ListContacts> buildReq(String type, int page) {
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
		CiviCRMAsyncRequest<ListContacts> req = new CiviCRMAsyncRequest<ListContacts>(
				this.getActivity(), ListContacts.class, ACTION.get,
				ENTITY.Contact, params);
		Log.d("ContactAutoCompleteListAdapter", "Request:" + req.getUriReq());
		return req;
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
			Toast.makeText(getActivity(),
					"Error during request: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onRequestSuccess(ListContacts listContacts) {
			// listContacts could be null just if
			// contentManager.getFromCache(...)
			// doesn't return anything.
			if (listContacts == null) {
				return;
			}
			contactsAdapter.addAll(listContacts.getValues());
			contactsAdapter.notifyDataSetChanged();			
			progressBar.setVisibility(View.GONE);

		}
	}
}
