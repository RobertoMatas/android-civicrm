package org.upsam.civicrm.test.roboelectric.runner;

import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;
import org.upsam.civicrm.test.fake.CiviMockApplication;
import org.upsam.civicrm.test.roboelectric.shadow.CustomShadowArrayAdapter;
import org.upsam.civicrm.test.roboelectric.shadow.CustomShadowTypedArray;

import android.app.Application;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class CustomTestRunner extends RobolectricTestRunner {

	public CustomTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xtremelabs.robolectric.RobolectricTestRunner#createApplication()
	 */
	@Override
	protected Application createApplication() {
		return new CiviMockApplication();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xtremelabs.robolectric.RobolectricTestRunner#beforeTest(java.lang
	 * .reflect.Method)
	 */
	@Override
	public void beforeTest(Method method) {
		super.beforeTest(method);
		Robolectric.bindShadowClass(CustomShadowArrayAdapter.class);
		Robolectric.bindShadowClass(CustomShadowTypedArray.class);
	}

}
