package org.upsam.civicrm.test.dagger.modules;

import javax.inject.Singleton;

import org.upsam.civicrm.contact.add.AddContactActivity;
import org.upsam.civicrm.rest.CiviCRMAndroidSpiceService;
import org.upsam.civicrm.rest.req.CiviCRMContactRequestBuilder;
import org.upsam.civicrm.test.AddContactActivityTests;
import org.upsam.civicrm.test.fake.CiviCRMContactRequestBuilderFake;
import org.upsam.civicrm.test.fake.SpiceManagerFake;

import com.octo.android.robospice.SpiceManager;

import dagger.Module;
import dagger.Provides;

@Module( //
		entryPoints = { 
				AddContactActivityTests.class, //
				AddContactActivity.class //
		}, //
		overrides = true //
)
public class TestModule {
	@Provides
	@Singleton
	CiviCRMContactRequestBuilder provideCiviCRMContactRequestBuilder() {
		return new CiviCRMContactRequestBuilderFake();
	}

	@Provides
	@Singleton
	SpiceManager provideSpiceManager() {
		return new SpiceManagerFake(CiviCRMAndroidSpiceService.class);
	}
}