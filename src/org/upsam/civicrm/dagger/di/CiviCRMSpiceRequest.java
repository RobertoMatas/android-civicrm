package org.upsam.civicrm.dagger.di;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public abstract class CiviCRMSpiceRequest<RESULT> extends
		SpringAndroidSpiceRequest<RESULT> {

	public CiviCRMSpiceRequest(Class<RESULT> clazz) {
		super(clazz);
	}

	public abstract String createCacheKey();

	public abstract String getUriReq();

	public abstract RESULT loadDataFromNetwork() throws Exception;

}