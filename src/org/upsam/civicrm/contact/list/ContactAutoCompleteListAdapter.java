package org.upsam.civicrm.contact.list;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.R;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.contact.ListContacts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ContactAutoCompleteListAdapter extends BaseAdapter implements Filterable {
	private final LayoutInflater layoutInflater;
	private List<ContactSummary> contacts;
	private final RestTemplate restTemplate;
	
	private Context activityContext;

	static class ViewHolder {
	    public TextView displayName;
	  }
	
	public ContactAutoCompleteListAdapter(RestTemplate restTemplate, Context context, ListContacts contacts,Context activityContext) {
		super();
		this.activityContext = activityContext;
		this.restTemplate = restTemplate;
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
			view = layoutInflater.inflate(R.layout.search_item_layout, parent, false);							
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.displayName = (TextView) view.findViewById(R.id.searchItem);
			view.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		if (contact != null) {			
			holder.displayName.setText(contact.getName());
		}
		return view;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence query) {
				FilterResults filterResults = new FilterResults();
				if (query != null && query.length() > 2) {
					Map<String, String> params = new HashMap<String, String>(5);
					params.put("display_name", query.toString().replace(" ", "+"));
					params.put("sort", "sort_name");
					params.put("return[display_name]", "1");
					params.put("return[contact_type]", "1");
					params.put("return[contact_sub_type]", "1");
					Log.d("ContactAutoCompleteListAdapter", "query: " + query.toString().replace(" ", "+"));
					CiviCRMAsyncRequest<ListContacts> req = new CiviCRMAsyncRequest<ListContacts>(activityContext,ListContacts.class, ACTION.get, ENTITY.Contact, params);
					Log.d("ContactAutoCompleteListAdapter", "Lanzamos request: " + req.getUriReq());
					ListContacts result = restTemplate.getForObject(URI.create(req.getUriReq()), ListContacts.class);
					setContacts(result);
					filterResults.values = contacts;
					filterResults.count = contacts.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence query, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}

			}

		};
		return filter;
	}

}