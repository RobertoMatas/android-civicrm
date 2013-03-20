package org.upsam.civicrm.contact.detail.fragments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.groups.Group;
import org.upsam.civicrm.contact.model.groups.ListGroups;
import org.upsam.civicrm.contact.model.tags.ListTags;
import org.upsam.civicrm.contact.model.tags.Tag;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ContactTagsAndGroupsFragment extends AbstractAsyncFragment {

	public class TagListener implements RequestListener<Tag> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(Tag result) {
			if (result == null) {
				Log.d("ContactTagsAndGroupsFragment", "Tag es null");
				return;
			}			
			updateTagsView(result);
		}

	}

	public class ContactTagsListener implements RequestListener<ListTags> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ListTags result) {
			if (result == null) {
				Log.d("ContactTagsAndGroupsFragment", "ListTag es null");
				return;
			}	
			loadTags(result);

		}

	}

	public class ContactGroupsListener implements RequestListener<ListGroups> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRequestSuccess(ListGroups result) {
			if (result == null) {
				Log.d("ContactTagsAndGroupsFragment", "ListGroups es null");
				return;
			}	
			updateGroupsView(result);
		}
	}

	/**
	 * 
	 * @param contentManager
	 */
	public ContactTagsAndGroupsFragment(SpiceManager contentManager) {
		super(contentManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_tags_and_groups_layout, container, false);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showLoadingProgressDialog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ContactTagsAndGroupsFragment", "onCreate()");
		executeRequests();
	}

	private void executeRequests() {
		ContactSummary contactSummary = getArguments().getParcelable("contact");
		final Map<String, String> params = new HashMap<String, String>(1);
		params.put("contact_id", Long.toString(contactSummary.getId()));
		Log.d("ContactTagsAndGroupsFragment", "contact_id=" + Long.toString(contactSummary.getId()));
		CiviCRMAsyncRequest<ListGroups> groupsReq = new CiviCRMAsyncRequest<ListGroups>(ListGroups.class, ACTION.get, ENTITY.GroupContact, params);
		CiviCRMAsyncRequest<ListTags> tagsReq = new CiviCRMAsyncRequest<ListTags>(ListTags.class, ACTION.get, ENTITY.EntityTag, params);
		contentManager.execute(groupsReq, groupsReq.createCacheKey(), DurationInMillis.ONE_MINUTE, new ContactGroupsListener());
		contentManager.execute(tagsReq, tagsReq.createCacheKey(), DurationInMillis.ONE_MINUTE, new ContactTagsListener());
	}

	private void loadTags(ListTags result) {
		List<Tag> tags = result.getValues();
		if (tags != null && ! tags.isEmpty()) {
			Map<String, String> params = null;			
			for (Tag tag : tags) {
				params = new HashMap<String, String>(1);
				params.put("id", Integer.toString(tag.getTagId()));
				CiviCRMAsyncRequest<Tag> tagsReq = new CiviCRMAsyncRequest<Tag>(Tag.class, ACTION.getsingle, ENTITY.Tag, params);
				contentManager.execute(tagsReq, tagsReq.createCacheKey(), DurationInMillis.ONE_HOUR, new TagListener());
			}
		} else {
			dismissProgressDialog();
		}
		
	}

	private void updateTagsView(Tag tag) {
		ViewGroup viewGroup = (ViewGroup) getView();
		LinearLayout layout = (LinearLayout) viewGroup.getChildAt(0);
		View view = getLayoutInflater(null).inflate(android.R.layout.simple_list_item_2, layout, false);
		TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		TextView text2 = (TextView) view.findViewById(android.R.id.text2);
		text1.setText(tag.getName());
		text2.setText(tag.getDescription());
		layout.addView(view);
		dismissProgressDialog();
		
	}

	private void updateGroupsView(ListGroups result) {
		List<Group> groups = result.getValues();
		if (groups != null && !groups.isEmpty()) {
			Log.d(this.getClass().getCanonicalName(), "grupos=" + groups);
			ViewGroup viewGroup = (ViewGroup) getView();
			LinearLayout layout = (LinearLayout) viewGroup.getChildAt(0);
			View view = null;
			TextView text1, text2 = null;
			for (Group group : groups) {
				view = getLayoutInflater(null).inflate(android.R.layout.simple_list_item_2, layout, false);
				text1 = (TextView) view.findViewById(android.R.id.text1);
				text2 = (TextView) view.findViewById(android.R.id.text2);
				text1.setText(group.getTitle());
				text2.setText("Since " + group.getInDate() + " by " + group.getInMethod());
				layout.addView(view);
			}
		}

	}

}
