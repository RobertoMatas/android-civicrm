package org.upsam.civicrm.contact.detail.menu;

import org.upsam.civicrm.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MenuOrganizationFragment extends ListFragment {

	OnMenuItemSelectedListener mCallback;

	// Container Activity must implement this interface
	public interface OnMenuItemSelectedListener {
		public void onMenuOrganizationItemSelected(int position);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_menu_organization, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{		
		super.onActivityCreated(savedInstanceState);				  		
		setListAdapter(new MenuAdapter(getActivity(), getResources().getStringArray(R.array.slide_organization_menu)));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnMenuItemSelectedListener) activity;
			
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnMenuItemSelectedListener");
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onMenuOrganizationItemSelected(position);		
	}
}