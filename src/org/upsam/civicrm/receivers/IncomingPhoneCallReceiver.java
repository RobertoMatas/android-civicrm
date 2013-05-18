package org.upsam.civicrm.receivers;

import org.upsam.civicrm.activity.services.AddActivityService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class IncomingPhoneCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			String state = extras.getString(TelephonyManager.EXTRA_STATE);
			Log.w("DEBUG-------->", state);
			if (TelephonyManager.EXTRA_STATE_RINGING.equalsIgnoreCase(state)||
				extras.getString(Intent.EXTRA_PHONE_NUMBER)!=null) {
				Intent activityServiceIntent = new Intent(context,
						AddActivityService.class);
				activityServiceIntent.putExtras(extras);
				context.startService(activityServiceIntent);
			}
		}
	}

}
