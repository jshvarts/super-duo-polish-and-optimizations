package barqsoft.footballscores;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Set;

import barqsoft.footballscores.service.myFetchService;

/**
 * Created by shvartsy on 2/15/16.
 */
public class FootballScoresApplication extends Application implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
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
        Intent intent = new Intent(getApplicationContext(), myFetchService.AlarmReceiver.class);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        alarmManager.cancel(pendingAlarmIntent);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // start AlarmManager to enable notifications while the app is in background
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), myFetchService.AlarmReceiver.class);
        PendingIntent pendingAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        // repeat alarm every 10 minutes
        long repeatingTime = 10 * 60 * 1000;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                repeatingTime,
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
