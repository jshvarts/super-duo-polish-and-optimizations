package barqsoft.footballscores.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;

/**
 * Created by shvartsy on 3/6/16.
 */
public class TodaysScoresAppWidgetProvider extends AppWidgetProvider {
    private static String LOG_TAG = TodaysScoresAppWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "entered onUpdate");

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.todays_scores_appwidget);

            Intent intent = new Intent(context, TodaysScoresAppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(appWidgetId, R.id.todays_scores_list, intent);

            views.setEmptyView(R.id.todays_scores_list, R.id.todays_scores_empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
