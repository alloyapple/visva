package vn.com.shoppie;

import java.util.List;

import vn.com.shoppie.activity.ActivityWelcome;
import vn.com.shoppie.activity.HomeActivity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {
	private final String NOTI = "Nhiều điểm Pie may mắn đang chờ bạn. Kiểm tra ngay";
    private Context context;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        
        int icon = R.drawable.icon_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, NOTI, when);

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, ActivityWelcome.class);
		notificationIntent
				.setFlags(/* Intent.FLAG_ACTIVITY_CLEAR_TOP | */Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, NOTI, pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		notification.sound = Uri.parse("android.resource://vn.com.shoppie"
				+ "/" + R.raw.ting_ting);

		// Vibrate if vibrate is enabled
		notification.defaults = Notification.DEFAULT_LIGHTS
				| Notification.DEFAULT_VIBRATE;
		int notiId = 0;
		notificationManager.notify(notiId, notification);

    }
    
    private void setDefault(int defaults , String content , String uid) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        
        Intent i = new Intent(context, HomeActivity.class);
        
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                i , 0);
        final Notification notification = new Notification(
                R.drawable.icon_launcher,       // the icon for the status bar
                content,                        // the text to display in the ticker
                System.currentTimeMillis()); // the timestamp for the notification

        notification.setLatestEventInfo(
                context,                        // the context to use
                "Slicktasks",
                                             // the title for the notification
                content,                        // the details to display in the notification
                null);              // the contentIntent (see above)

        notification.defaults = defaults;
        
        mNotificationManager.notify(
                1, 
                notification);
    }
    
    /*
     * Check app is running (in background)
     */
    private static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(100);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().startsWith("com.slicktasks")) {
                return true;
            }
        }
        return false;
    }
}