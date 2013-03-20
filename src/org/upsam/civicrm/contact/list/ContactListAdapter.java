package org.upsam.civicrm.contact.list;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.contact.ListContacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter {
	private final LayoutInflater layoutInflater;
	private List<ContactSummary> contacts;

	public ContactListAdapter(Context context, ListContacts contacts) {
		super();
		List<ContactSummary> cs = contacts.getValues();
		this.contacts = cs != null ? cs : new ArrayList<ContactSummary>(0);
		this.layoutInflater = LayoutInflater.from(context);
	}

	/**
	 * @param contacts
	 *            the contacts to set
	 */
	public void setContacts(ListContacts contacts) {
		List<ContactSummary> cs = contacts.getValues();
		this.contacts = cs != null ? cs : new ArrayList<ContactSummary>(0);
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ((ContactSummary) contacts.get(position)).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactSummary contact = (ContactSummary) getItem(position);

		View view = convertView;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.contact_list_item, parent, false);
		}

		TextView displayName = (TextView) view.findViewById(R.id.textView1);
		displayName.setText(contact.getName());

		TextView type = (TextView) view.findViewById(R.id.textView2);
		String strType = contact.getType();
		String strSubType = contact.getSubType();
		if (StringUtils.hasText(strSubType))
			strType += " - " + strSubType;
		type.setText(strType);

		return view;
	}

}
