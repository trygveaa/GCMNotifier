package com.trygveaa.gcmnotifier;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

public class MessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

			if (action.equals("com.trygveaa.gcmnotifier.CANCEL_NOTIFICATIONS")) {
				cancelNotifications(context);

			} else if (action.equals("com.trygveaa.gcmnotifier.CLICK_NOTIFICATION")) {
				cancelNotifications(context);
				String clickAction = sharedPreferences.getString(context.getString(R.string.intent_click_action_key),
						context.getString(R.string.intent_click_action_default));

				if (!clickAction.isEmpty()) {
					Intent clickIntent;
					String clickUri = sharedPreferences.getString(context.getString(R.string.intent_click_uri_key),
							context.getString(R.string.intent_click_uri_default));

					if (!clickUri.isEmpty()) {
						clickIntent = new Intent(clickAction, Uri.parse(clickUri));
					} else {
						clickIntent = new Intent(clickAction);
					}

					clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(clickIntent);
				}
			}
		}
	}

	private void cancelNotifications(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}
}
