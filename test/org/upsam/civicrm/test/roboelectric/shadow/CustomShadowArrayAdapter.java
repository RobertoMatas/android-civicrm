package org.upsam.civicrm.test.roboelectric.shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowBaseAdapter;

@Implements(ArrayAdapter.class)
public class CustomShadowArrayAdapter<T> extends ShadowBaseAdapter {

	private Context context;
	private List<T> list;

	public void __constructor__(Context context, int textViewResourceId) {
		init(context, textViewResourceId, 0, new ArrayList<T>());
	}

	public void __constructor__(Context context, int resource,
			int textViewResourceId) {
		init(context, resource, textViewResourceId, new ArrayList<T>());
	}

	public void __constructor__(Context context, int textViewResourceId,
			T[] objects) {
		init(context, textViewResourceId, 0, Arrays.asList(objects));
	}

	public void __constructor__(Context context, int resource,
			int textViewResourceId, T[] objects) {
		init(context, resource, textViewResourceId, Arrays.asList(objects));
	}

	public void __constructor__(Context context, int textViewResourceId,
			List<T> objects) {
		init(context, textViewResourceId, 0, objects);
	}

	public void __constructor__(Context context, int resource,
			int textViewResourceId, List<T> objects) {
		init(context, resource, textViewResourceId, objects);
	}

	private void init(Context context, int resource, int textViewResourceId,
			List<T> objects) {
		this.context = context;
		this.list = objects;
	}

	@Implementation
	public void add(T object) {
		list.add(object);
	}

	@Implementation
	public Context getContext() {
		return context;
	}

	@Implementation
	public int getCount() {
		return list.size();
	}

	@Implementation
	public T getItem(int position) {
		return list.get(position);
	}

	@Implementation
	public int getPosition(T item) {
		return list.indexOf(item);
	}

	@Implementation
	public void addAll(T... items) {
		list.addAll(Arrays.asList(items));
	}

	@Implementation
	public void addAll(Collection<? extends T> collection) {
		list.addAll(collection);
	}
}
