package course.examples.Notification.StatusBarWithCustomView;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;

public class NotificationStatusBarWithCustomViewActivity extends Activity {

	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;
	private static final int MY_NOTIFICATION_2_ID = 2;

	// Notification Count
	private int mNotificationCount;

	// Notification Text Elements
	private final CharSequence tickerText = "This is a Really, Really, Super Long Notification Message!";
	private final CharSequence contentTitle = "Notification";
	private final CharSequence contentText = "You've Been Notified!";

	// Notification Action Elements
	private Intent mNotificationIntent;
	private Intent mNotificationIntentClickable;
	
	private PendingIntent mContentIntent;
	private PendingIntent mContentIntentClickable;

	// Notification Sound and Vibration on Arrival
	private Uri soundURI = Uri
			.parse("android.resource://course.examples.Notification.StatusBarWithCustomView/"
					+ R.raw.alarm_rooster);
	private long[] mVibratePattern = { 0, 200, 200, 300 };

	RemoteViews mContentView = new RemoteViews(
			"course.examples.Notification.StatusBarWithCustomView",
			R.layout.custom_notification);
	
	RemoteViews mContentViewClickable = new RemoteViews(
			"course.examples.Notification.StatusBarWithCustomView",
			R.layout.custom_notification_clickable);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mNotificationIntent = new Intent(getApplicationContext(),
				NotificationSubActivity.class);
		mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		mNotificationIntentClickable = new Intent(getApplicationContext(),
				NotificationClickableActivity.class);		
		mContentIntentClickable = PendingIntent.getActivity(getApplicationContext(), 0,
				mNotificationIntentClickable, Intent.FLAG_ACTIVITY_NEW_TASK);		

		// notification view can be used as click view
		mContentViewClickable.setOnClickPendingIntent(R.id.btnClickableNotification, mContentIntentClickable);
		
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Define the Notification's expanded message and Intent:

				mContentView.setTextViewText(R.id.textBaseNotification, contentText + " ("
						+ ++mNotificationCount + ")");

				// Build the Notification

				Notification.Builder notificationBuilder = new Notification.Builder(
						getApplicationContext())
					.setTicker(tickerText)
					.setSmallIcon(android.R.drawable.stat_sys_warning)
					.setAutoCancel(true)
					.setContentIntent(mContentIntent)
					.setSound(soundURI)
					.setVibrate(mVibratePattern)
					.setContent(mContentView);

				// Pass the Notification to the NotificationManager:
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(MY_NOTIFICATION_ID,
						notificationBuilder.build());

			}
		});

		final Button buttonClickable = (Button)findViewById(R.id.btnClickable);
		buttonClickable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContentViewClickable.setTextViewText(R.id.textClickableNotification, "Here it is a clickable notification!");
				
				// Build the Notification

				Notification.Builder notificationBuilder = new Notification.Builder(
						getApplicationContext())
					.setTicker(tickerText)
					.setSmallIcon(android.R.drawable.stat_sys_warning)
					.setAutoCancel(true)
					.setSound(soundURI)
					.setVibrate(mVibratePattern)
					.setContent(mContentViewClickable);

				// Pass the Notification to the NotificationManager:
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(MY_NOTIFICATION_2_ID,
						notificationBuilder.build());
				
			}
		});
		
	}
}