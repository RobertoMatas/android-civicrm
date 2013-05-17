package org.upsam.civicrm.dagger.di;

import dagger.ObjectGraph;

public interface AppicationInjectionAware {

	public abstract ObjectGraph getApplicationGraph();

}