package org.upsam.civicrm.contact.detail.fragments;

import static org.upsam.civicrm.util.CiviCRMContactRequestHelper.requestGroupByContactId;
import static org.upsam.civicrm.util.CiviCRMContactRequestHelper.requestTagById;
import static org.upsam.civicrm.util.CiviCRMContactRequestHelper.requestTagsByContactId;
import static org.upsam.civicrm.util.CiviCRMRequestHelper.notifyRequestError;

import java.util.List;

import org.upsam.civicrm.AbstractAsyncFragment;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.groups.Group;
import org.upsam.civicrm.contact.model.groups.ListGroups;
import org.upsam.civicrm.contact.model.tags.ListTags;
import org.upsam.civicrm.contact.model.tags.Tag;
import org.upsam.civicrm.util.Utilities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
			notifyRequestError(activityContext, progressDialog);

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
			notifyRequestError(activityContext, progressDialog);

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
			notifyRequestError(activityContext, progressDialog);

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

	public ContactTagsAndGroupsFragment(SpiceManager contentManager,
			Context activityContext) {
		super(contentManager, activityContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_tags_and_groups_layout,
				container, false);
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
		this.progressDialog = Utilities.showLoadingProgressDialog(
				this.progressDialog, activityContext,
				getString(R.string.progress_bar_msg_generico));
		ContactSummary contactSummary = getArguments().getParcelable("contact");
		int contactId = contactSummary.getId();
		CiviCRMAsyncRequest<ListGroups> groupsReq = requestGroupByContactId(
				activityContext, contactId);
		CiviCRMAsyncRequest<ListTags> tagsReq = requestTagsByContactId(
				activityContext, contactId);
		contentManager.execute(groupsReq, groupsReq.createCacheKey(),
				DurationInMillis.ONE_MINUTE, new ContactGroupsListener());
		contentManager.execute(tagsReq, tagsReq.createCacheKey(),
				DurationInMillis.ONE_MINUTE, new ContactTagsListener());
	}

	private void loadTags(ListTags result) {
		List<Tag> tags = result.getValues();
		if (tags != null && !tags.isEmpty()) {
			putTitle(R.id.tagsList, getString(R.string.tags_literal));
			for (Tag tag : tags) {
				CiviCRMAsyncRequest<Tag> tagsReq = requestTagById(
						activityContext, tag.getTagId());
				contentManager.execute(tagsReq, tagsReq.createCacheKey(),
						DurationInMillis.ONE_HOUR, new TagListener());
			}
		} else {
			Utilities.dismissProgressDialog(progressDialog);
		}

	}

	private void updateTagsView(Tag tag) {
		paint(R.id.tagsList, android.R.drawable.star_big_off, tag.getName(),
				tag.getDescription());
		Utilities.dismissProgressDialog(progressDialog);

	}

	private void updateGroupsView(ListGroups result) {
		List<Group> groups = result.getValues();
		if (groups != null && !groups.isEmpty()) {
			putTitle(R.id.groupList, getString(R.string.groups_literal));
			for (Group group : groups) {
				paint(R.id.groupList,
						android.R.drawable.star_big_on,
						group.getTitle(),
						getString(R.string.since_detail) + " "
								+ group.getInDate() + "\n"
								+ getString(R.string.by_detail) + " "
								+ group.getInMethod());
			}
		}

	}

	private void putTitle(int layoutId, String title) {
		LinearLayout layout = (LinearLayout) getView().findViewById(layoutId);
		View view = (TextView) getLayoutInflater(null).inflate(
				android.R.layout.simple_list_item_1, layout, false);
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setTextAppearance(activityContext, R.style.textoGreen);
		textView.setText(title);
		layout.addView(view);
	}

	private void paint(int layoutId, int imgId, String text1, String text2) {
		LinearLayout layout = (LinearLayout) getView().findViewById(layoutId);
		View view = getLayoutInflater(null).inflate(
				R.layout.contact_tags_and_groups_row, layout, false);

		ImageView img = (ImageView) view.findViewById(R.id.imageView1);
		TextView textView1 = (TextView) view.findViewById(R.id.textView1);
		TextView textView2 = (TextView) view.findViewById(R.id.textView2);

		img.setImageResource(imgId);
		textView1.setTextAppearance(activityContext, R.style.textoDefault);
		textView2.setTextAppearance(activityContext, R.style.textoWhite);

		textView1.setText(text1);
		textView2.setText(text2);
		layout.addView(view);
	}

}
