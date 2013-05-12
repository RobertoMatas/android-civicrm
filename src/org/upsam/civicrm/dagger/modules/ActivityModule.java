package org.upsam.civicrm.dagger.modules;

import javax.inject.Singleton;

import org.upsam.civicrm.contact.add.AddContactActivity;
import org.upsam.civicrm.contact.detail.ContactDetailFragmentActivity;
import org.upsam.civicrm.contact.detail.fragments.ContactAddressFragment;
import org.upsam.civicrm.contact.detail.fragments.ContactDetailFragment;
import org.upsam.civicrm.contact.detail.fragments.ContactTagsAndGroupsFragment;
import org.upsam.civicrm.contact.detail.fragments.ListContactsActivitiesFragment;
import org.upsam.civicrm.contact.detail.fragments.OtherInformationFragment;
import org.upsam.civicrm.dagger.annotations.ForActivity;
import org.upsam.civicrm.dagger.di.activity.BaseDIActivity;
import org.upsam.civicrm.dagger.utilities.ProgressDialogUtilities;
import org.upsam.civicrm.dagger.utilities.ProgressDialogUtilitiesImpl;
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
@Module(injects = { AddContactActivity.class,
		ContactDetailFragmentActivity.class, ContactDetailFragment.class,
		ContactAddressFragment.class, ContactTagsAndGroupsFragment.class,
		ListContactsActivitiesFragment.class, OtherInformationFragment.class })
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

	@Provides
	@Singleton
	ProgressDialogUtilities provideProgressDialogUtilities() {
		return new ProgressDialogUtilitiesImpl(activity);
	}
}
