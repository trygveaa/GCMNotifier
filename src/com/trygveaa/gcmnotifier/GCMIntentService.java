package com.trygveaa.gcmnotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMNotifier";

	private static int notificationId = 1;

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

				Intent notificationIntent = new Intent("com.trygveaa.gcmnotifier.CLICK_NOTIFICATION");
				PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);

				Intent cancelIntent = new Intent("com.trygveaa.gcmnotifier.CANCEL_NOTIFICATIONS");
				PendingIntent deleteIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);

				Notification notification = new NotificationCompat.Builder(context)
				.setTicker(title + " " + body)
				.setContentTitle(title)
				.setContentText(body)
				.setSmallIcon(R.drawable.ic_stat_notification)
				.setLights(0xff0000ff, 1000, 1000)
				.setVibrate(new long[] { 0, 200, 400, 200 })
				.setDefaults(Notification.DEFAULT_SOUND)
				.setContentIntent(contentIntent)
				.setAutoCancel(true)
				.setDeleteIntent(deleteIntent)
				.getNotification();

				notificationManager.notify(notificationId++, notification);
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
}
