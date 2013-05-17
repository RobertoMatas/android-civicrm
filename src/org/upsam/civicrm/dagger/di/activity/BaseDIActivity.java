package org.upsam.civicrm.dagger.di.activity;

import java.util.Arrays;
import java.util.List;

import org.upsam.civicrm.dagger.di.AppicationInjectionAware;
import org.upsam.civicrm.dagger.modules.ActivityModule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import dagger.ObjectGraph;

public class BaseDIActivity extends FragmentActivity {
	private ObjectGraph activityGraph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the activity graph by .plus-ing our modules onto the
		// application graph.
		AppicationInjectionAware application = (AppicationInjectionAware) getApplication();
		ObjectGraph applicationGraph = application.getApplicationGraph();
		if (applicationGraph != null) {
			activityGraph = applicationGraph.plus(getModules().toArray());
		} else {
			activityGraph = ObjectGraph.create(getModules().toArray());
		}

		// Inject ourselves so subclasses will have dependencies fulfilled when
		// this method returns.
		activityGraph.inject(this);
	}

	@Override
	protected void onDestroy() {
		// Eagerly clear the reference to the activity graph to allow it to be
		// garbage collected as
		// soon as possible.
		activityGraph = null;

		super.onDestroy();
	}

	/**
	 * A list of modules to use for the individual activity graph. Subclasses
	 * can override this method to provide additional modules provided they call
	 * and include the modules returned by calling {@code super.getModules()}.
	 */
	protected List<Object> getModules() {
		return Arrays.<Object> asList(new ActivityModule(this));
	}

	/** Inject the supplied {@code object} using the activity-specific graph. */
	public void inject(Object object) {
		activityGraph.inject(object);
	}

}
