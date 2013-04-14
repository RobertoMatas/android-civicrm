package org.upsam.civicrm.contact.add;

import java.util.ArrayList;

import org.upsam.civicrm.contact.model.contact.ContactType;
import org.upsam.civicrm.contact.model.contact.ListContactType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactTypesAdapter extends ArrayAdapter<ContactType> {
	private final LayoutInflater layoutInflater;

	public ContactTypesAdapter(Context context, ListContactType contactsTypes) {
		super(
				context,
				android.R.layout.simple_spinner_item,
				contactsTypes != null && contactsTypes.getValues() != null ? contactsTypes
						.getValues() : new ArrayList<ContactType>(0));
		this.layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactType contactType = getItem(position);
		View view = convertView;
		if (view == null) {
			view = layoutInflater.inflate(android.R.layout.simple_spinner_item,
					parent, false);
		}
		if (contactType != null) {
			TextView labelTxt = (TextView) view.findViewById(android.R.id.text1);
			labelTxt.setText(contactType.getLabel());
		}
		return view;
	}

}
