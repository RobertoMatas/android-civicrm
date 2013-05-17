package org.upsam.civicrm.dagger.modules;

import javax.inject.Singleton;

import org.upsam.civicrm.dagger.annotations.ForApplication;
import org.upsam.civicrm.dagger.di.CiviCRMApplication;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module(library = true)
public class AndroidModule {
	private final CiviCRMApplication application;

	public AndroidModule(CiviCRMApplication application) {
		this.application = application;
	}

	/**
	 * Allow the application context to be injected but require that it be
	 * annotated with {@link ForApplication @Annotation} to explicitly
	 * differentiate it from an activity context.
	 */
	@Provides
	@Singleton
	@ForApplication
	Context provideApplicationContext() {
		return application;
	}
}