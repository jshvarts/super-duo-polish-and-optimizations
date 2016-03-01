package barqsoft.footballscores;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static barqsoft.footballscores.service.myFetchService.AlarmReceiver;

import com.facebook.stetho.Stetho;

import java.util.Calendar;

/**
 * Created by shvartsy on 2/15/16.
 */
public class FootballScoresApplication extends Application implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // do nothing
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // do nothing
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // cancel alarm manager to avoid notifications while the app is in foreground
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        alarmManager.cancel(pendingAlarmIntent);

        NotificationManager notificationManager = (NotificationManager) activity
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmReceiver.NEW_SCORE_NOTIFICATION_ID);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // start AlarmManager to enable notifications while the app is in background
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        // Set the alarm to start at approximately 1:00 a.m. every day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingAlarmIntent);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // do nothing
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // do nothing
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // do nothing
    }
}
