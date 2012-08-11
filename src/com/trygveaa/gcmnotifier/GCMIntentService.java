package com.trygveaa.gcmnotifier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMNotifier";

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(TAG, "Error occured: " + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Bundle data = intent.getExtras();
		Log.d(TAG, "Message received: " + data.getString("title") + " " + data.getString("body"));
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.v(TAG, "Device registered: " + registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.v(TAG, "Device unregistered: " + registrationId);
	}
}
