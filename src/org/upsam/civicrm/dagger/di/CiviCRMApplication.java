package org.upsam.civicrm.dagger.di;

import java.util.Arrays;
import java.util.List;

import org.upsam.civicrm.dagger.modules.AndroidModule;

import android.app.Application;
import dagger.ObjectGraph;

public class CiviCRMApplication extends Application implements AppicationInjectionAware {

	private ObjectGraph applicationGraph;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationGraph = ObjectGraph.create(getModules().toArray());

	}

	/**
	 * A list of modules to use for the application graph. Subclasses can
	 * override this method to provide additional modules provided they call
	 * {@code super.getModules()}.
	 */
	protected List<Object> getModules() {
		return Arrays.<Object> asList(new AndroidModule(this));
	}

	/* (non-Javadoc)
	 * @see org.upsam.civicrm.AppicationInjectionAware#getApplicationGraph()
	 */
	@Override
	public ObjectGraph getApplicationGraph() {
		return applicationGraph;
	}

}
