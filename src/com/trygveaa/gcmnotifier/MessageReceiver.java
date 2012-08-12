package com.trygveaa.gcmnotifier;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action != null) {
			if (action.equals("com.trygveaa.gcmnotifier.CANCEL_NOTIFICATIONS")) {
				cancelNotifications(context);
			} else if (action.equals("com.trygveaa.gcmnotifier.CLICK_NOTIFICATION")) {
				cancelNotifications(context);
			}
		}
	}

	private void cancelNotifications(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}
}
