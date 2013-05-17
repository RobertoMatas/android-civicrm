package org.upsam.civicrm.dagger.modules;

import javax.inject.Singleton;

import org.upsam.civicrm.contact.add.AddContactActivity;
import org.upsam.civicrm.contact.add.AddContactValidator;
import org.upsam.civicrm.contact.add.AddContactValidatorImpl;

import dagger.Module;
import dagger.Provides;

@Module(injects = AddContactActivity.class, includes = ActivityModule.class)
public class AddContactActivityModule {

	@Provides
	@Singleton
	AddContactValidator provideValidator() {
		return new AddContactValidatorImpl();
	}
}
