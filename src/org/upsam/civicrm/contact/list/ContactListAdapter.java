package org.upsam.civicrm.contact.list;

import java.util.ArrayList;

import org.springframework.util.StringUtils;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.contact.ListContacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactListAdapter extends ArrayAdapter<ContactSummary> {
	private final LayoutInflater layoutInflater;

	public ContactListAdapter(Context context, ListContacts contacts) {
		super(context, R.id.textView1, contacts.getValues() != null ? contacts.getValues() : new ArrayList<ContactSummary>(0));
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactSummary contact = getItem(position);

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
