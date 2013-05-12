package org.upsam.civicrm.dagger.modules;

import javax.inject.Singleton;

import org.upsam.civicrm.contact.add.AddContactActivity;
import org.upsam.civicrm.dagger.annotations.ForActivity;
import org.upsam.civicrm.dagger.di.BaseDIActivity;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilder;
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilderImpl;

import android.content.Context;

import com.octo.android.robospice.SpiceManager;

import dagger.Module;
import dagger.Provides;

/**
 * This module represents objects which exist only for the scope of a single
 * activity. We can safely create singletons using the activity instance because
 * ths entire object graph will only ever exist inside of that activity.
 */
@Module(entryPoints = AddContactActivity.class)
public class ActivityModule {
	private final BaseDIActivity activity;

	public ActivityModule(BaseDIActivity activity) {
		this.activity = activity;
	}

	/**
	 * Allow the activity context to be injected but require that it be
	 * annotated with {@link ForActivity @ForActivity} to explicitly
	 * differentiate it from application context.
	 */
	@Provides
	@Singleton
	@ForActivity
	Context provideActivityContext() {
		return activity;
	}

	@Provides
	@Singleton
	CiviCRMContactRequestBuilder provideCiviCRMContactRequestBuilder() {
		return new CiviCRMContactRequestBuilderImpl(activity);
	}

	@Provides
	@Singleton
	SpiceManager provideSpiceManager() {
		return new SpiceManager(CiviCRMAndroidSpiceService.class);
	}
}
