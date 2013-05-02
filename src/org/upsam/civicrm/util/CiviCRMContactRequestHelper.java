package org.upsam.civicrm.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.contact.model.constant.Constant;

import android.content.Context;

public class CiviCRMContactRequestHelper {

	public static CiviCRMAsyncRequest<Constant> requestCountries(Context ctx) {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(
				1);
		params.add("name", "country");
		return new CiviCRMAsyncRequest<Constant>(ctx, Constant.class,
				ACTION.get, ENTITY.Constant, params, false);
	}
}
