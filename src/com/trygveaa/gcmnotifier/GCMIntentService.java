package com.trygveaa.gcmnotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService implements SensorEventListener {

	private static final String TAG = "GCMNotifier";

	private static final Object syncObject = new Object();

	private static int notificationId = 1;

	private float[] accelerometerValues;

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(TAG, "Error occured: " + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Bundle data = intent.getExtras();
		String action = data.getString("action");

		if (action != null) {
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			if (action.equals("notify")) {
				String title = data.getString("title");
				String body = data.getString("body");
				String subtext = data.getString("subtext");
				String type = data.getString("type");
				boolean vibration = true;

				SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

				if (sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_NORMAL)) {

					synchronized (syncObject) {
						try {
							syncObject.wait();
						} catch (InterruptedException e) {
						}
					}

					sensorManager.unregisterListener(this);

					if (accelerometerValues[0] < 1 && accelerometerValues[0] > -1 && accelerometerValues[1] < 1
							&& accelerometerValues[1] > -1) {
						vibration = false;
					}
				}

				Intent notificationIntent = new Intent("com.trygveaa.gcmnotifier.CLICK_NOTIFICATION");
				PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);

				Intent cancelIntent = new Intent("com.trygveaa.gcmnotifier.CANCEL_NOTIFICATIONS");
				PendingIntent deleteIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);

				NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
					.setTicker(title + " " + body)
					.setContentTitle(title)
					.setContentText(body)
					.setSubText(subtext)
					.setContentIntent(contentIntent)
					.setAutoCancel(true)
					.setDeleteIntent(deleteIntent)
					.setStyle(new NotificationCompat.BigTextStyle()
						.bigText(body));

				if ("mail".equals(type)) {
					notificationBuilder
						.setSmallIcon(R.drawable.content_email)
						.setLights(0xffff0000, 1000, 1000);
				} else {
					notificationBuilder
						.setSmallIcon(R.drawable.social_chat)
						.setLights(0xff0000ff, 1000, 1000);
				}

				if (vibration) {
					notificationBuilder
						.setVibrate(new long[] { 0, 200, 400, 200 })
						.setDefaults(Notification.DEFAULT_SOUND);
				}

				notificationManager.notify(notificationId++, notificationBuilder.build());
			} else if (action.equals("cancel")) {
				notificationManager.cancelAll();
			}
		}
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.v(TAG, "Device registered: " + registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.v(TAG, "Device unregistered: " + registrationId);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			accelerometerValues = event.values;

			synchronized (syncObject) {
				syncObject.notifyAll();
			}
		}
	}
}
