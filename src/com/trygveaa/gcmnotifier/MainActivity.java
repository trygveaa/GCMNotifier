package com.trygveaa.gcmnotifier;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	private static final String TAG = "GCMNotifier";
	private static final String SENDER_ID = "";

	private EditText registeredId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		registeredId = (EditText) findViewById(R.id.registered_id);

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			Log.v(TAG, "Already registered with id: " + regId);
			registeredId.setInputType(InputType.TYPE_NULL);
			registeredId.setText(regId);
		}
	}
}
