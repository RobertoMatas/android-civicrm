package org.upsam.civicrm.contact.detail.menu;

import org.upsam.civicrm.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adaptador para controlar el pintado de los elementos
 * 
 * @author Equipo 7
 * Universidad Pontificia de Salamanca
 * v1.0
 *
 */
public class MenuAdapter extends ArrayAdapter<String>{

	private final LayoutInflater layoutInflater;
	private Context activityContext;

	public MenuAdapter(Context context, String[] itemsMenu) {
		super(context, android.R.layout.simple_list_item_1, itemsMenu);
		this.layoutInflater = LayoutInflater.from(context);
		this.activityContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		String item = getItem(position);
		
		View view = convertView;
		if (view == null) {
			view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}
		TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		text1.setTextAppearance(activityContext, R.style.textoDefault);	
		text1.setText(item);
		return view;
	}
}



