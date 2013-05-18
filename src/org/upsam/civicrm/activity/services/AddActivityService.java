package org.upsam.civicrm.activity.services;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.upsam.civicrm.CiviCRMAsyncRequest;
import org.upsam.civicrm.CiviCRMAsyncRequest.ACTION;
import org.upsam.civicrm.CiviCRMAsyncRequest.ENTITY;
import org.upsam.civicrm.CiviCRMAsyncRequest.METHOD;
import org.upsam.civicrm.activity.model.ListActivities;
import org.upsam.civicrm.contact.model.contact.ContactSummary;
import org.upsam.civicrm.contact.model.contact.ListContacts;
import org.upsam.civicrm.rest.CiviCRMAndroidPostSpiceService;
import org.upsam.civicrm.util.Utilities;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;

public class AddActivityService extends Service {

	private SpiceManager contentManager = new SpiceManager(
			CiviCRMAndroidPostSpiceService.class);

	MultiValueMap<String, String> fields = new LinkedMultiValueMap<String, String>();
	boolean entrante = true;
	String phoneNumber = null;
	ContactSummary contact = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		contentManager.start(this);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.w("DEBUG-------->", "Dentro de onStartCommand");
		
		performContactRequest(intent);
		//performActivityRequest(intent);

		return Service.START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {

		contentManager.shouldStop();
		super.onDestroy();

		Log.w("DEBUG------->", "Dentro de onDestroy()");
	}

	private void initializeActivityFields() {
		initializePhoneCallParams();
	}

	private void initializeContactFields(Intent intent) {
		String phoneNumber = null;
		Bundle extras = intent.getExtras();
		if (extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)!=null){
			phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		}
		else if (extras.getString(Intent.EXTRA_PHONE_NUMBER)!=null){
			entrante = false;
			phoneNumber = extras.getString(Intent.EXTRA_PHONE_NUMBER);
		}
		fields.add("phone_number",phoneNumber);
	}

	private void initializePhoneCallParams() {
		if (entrante){
			fields.add("subject", "Llamada del cliente: " + contact.getName());
		}
		else{
			fields.add("subject", "Llamada al cliente: " + contact.getName());
		}
		fields.add("activity_name", "Phone Call");
		fields.add("status_id", "2");
		fields.add("phone_number", phoneNumber);
		fields.add("source_contact_id", Utilities.getContactId(this));

	}

	private void performContactRequest(Intent intent) {
		initializeContactFields(intent);
		CiviCRMAsyncRequest<ListContacts> request = new CiviCRMAsyncRequest<ListContacts>(
				this, ListContacts.class, ACTION.get, ENTITY.Contact,
				METHOD.get, fields);
		contentManager.execute(request, request.createCacheKey(),
				DurationInMillis.ONE_MINUTE, new ContactRequestListener());
	}

	private void performActivityRequest() {
		initializeActivityFields();
		CiviCRMAsyncRequest<ListActivities> request = new CiviCRMAsyncRequest<ListActivities>(
				this, ListActivities.class, ACTION.create, ENTITY.Activity,
				METHOD.post, fields);
		contentManager.execute(request, request.createCacheKey(),
				DurationInMillis.ONE_MINUTE, new AddActivityRequestListener());

	}

	private class AddActivityRequestListener implements
			RequestListener<ListActivities>, RequestProgressListener {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.e("DEBUG -------->", "Error during request: " + e.getMessage());
			stopSelf();
		}

		@Override
		public void onRequestSuccess(ListActivities activities) {
			// listTweets could be null just if contentManager.getFromCache(...)
			// doesn't return anything.
			if (activities == null) {
				stopSelf();
				Log.e("ERROR --------->", "Actividad no creada!!!!");
				return;
			}
			stopSelf();
			Log.d("DEBUG --------->", "Actividad creada!!!!");
			Log.d("DEBUG --------->", activities.getIsError());

		}

		@Override
		public void onRequestProgressUpdate(RequestProgress progress) {
			RequestStatus status = progress.getStatus();
			if (RequestStatus.LOADING_FROM_NETWORK.equals(status)) {
				Log.e("Activity ------>", "Loading from network");
			} else if (RequestStatus.READING_FROM_CACHE.equals(status)) {
				Log.e("Activity ------>", "Reading from cache");
			} else if (RequestStatus.WRITING_TO_CACHE.equals(status)) {
				Log.e("Activity ------>", "Writing from cache");
			}

		}

	}

	private class ContactRequestListener implements
			RequestListener<ListContacts>, RequestProgressListener {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.e("DEBUG -------->", "Error during request: " + e.getMessage());
			stopSelf();
		}

		@Override
		public void onRequestSuccess(ListContacts contacts) {
			// listTweets could be null just if contentManager.getFromCache(...)
			// doesn't return anything.
			if (contacts == null) {
				Log.e("ERROR --------->", "Contacto recuperado!!!!");
				return;
			}
			if (contacts.getValues()!=null && !contacts.getValues().isEmpty()){
				
				contact = contacts.getValues().get(0);
				fields = new LinkedMultiValueMap<String, String>();
				performActivityRequest();

			}


		}

		@Override
		public void onRequestProgressUpdate(RequestProgress progress) {
			RequestStatus status = progress.getStatus();
			if (RequestStatus.LOADING_FROM_NETWORK.equals(status)) {
				Log.e("Activity ------>", "Loading from network");
			} else if (RequestStatus.READING_FROM_CACHE.equals(status)) {
				Log.e("Activity ------>", "Reading from cache");
			} else if (RequestStatus.WRITING_TO_CACHE.equals(status)) {
				Log.e("Activity ------>", "Writing from cache");
			}

		}

	}

}
