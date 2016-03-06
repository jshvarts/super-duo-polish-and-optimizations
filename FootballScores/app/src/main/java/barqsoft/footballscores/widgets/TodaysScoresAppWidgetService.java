package barqsoft.footballscores.widgets;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;

/**
 * Created by shvartsy on 3/6/16.
 */
public class TodaysScoresAppWidgetService extends RemoteViewsService {
    private static final String LOG_TAG = TodaysScoresAppWidgetService.class.getSimpleName();

    private Context serviceContext;
    private ContentResolver mContentResolver;
    private int appWidgetId;
    private Cursor cursor = null;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodaysScoresAppWidgetRemoteViewsFactory(getApplicationContext(), intent);
    }

    public class TodaysScoresAppWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        public TodaysScoresAppWidgetRemoteViewsFactory(Context context, Intent intent) {
            serviceContext = context;
            mContentResolver = serviceContext.getContentResolver();
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (cursor != null) {
                cursor.close();
                Log.d(LOG_TAG, "cursor is not null in dataset changed");
            }
            final long identityToken = Binder.clearCallingIdentity();
            cursor = mContentResolver.query(
                    DatabaseContract.scores_table.buildScoreWithDate(),
                    null,
                    null,
                    new String[]{new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))},
                    null);

            Log.d(LOG_TAG, "cursor is : " + cursor.toString());
            Log.d(LOG_TAG, "cursor count is " + cursor.getCount());
        }

        @Override
        public void onDestroy() {
            if (cursor != null) {
                cursor.close();
            }
        }

        @Override
        public int getCount() {
            Log.d(LOG_TAG, "getCount of cursor is : " + cursor.getCount());
            return cursor == null ? 0 : cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(serviceContext.getPackageName(), R.layout.todays_scores_appwidget_list_item);
            assert cursor != null;

            if (cursor.moveToPosition(position)) {

                remoteViews.setTextViewText(R.id.home_name, cursor.getString(ScoresAdapter.COL_HOME));
                remoteViews.setContentDescription(R.id.home_name, cursor.getString(ScoresAdapter.COL_HOME));

                if (cursor.getString(ScoresAdapter.COL_HOME_GOALS).equals("-1")) {
                    remoteViews.setTextViewText(R.id.home_score_textview, "");
                    remoteViews.setContentDescription(R.id.home_score_textview, serviceContext.getString(R.string.score_not_available));
                } else {
                    remoteViews.setTextViewText(R.id.home_score_textview, cursor.getString(ScoresAdapter.COL_HOME_GOALS));
                }

                if (cursor.getString(ScoresAdapter.COL_AWAY_GOALS).equals("-1")) {
                    remoteViews.setTextViewText(R.id.away_score_textview, "");
                    remoteViews.setContentDescription(R.id.away_score_textview, serviceContext.getString(R.string.score_not_available));
                } else {
                    remoteViews.setTextViewText(R.id.away_score_textview, cursor.getString(ScoresAdapter.COL_AWAY_GOALS));
                }

                remoteViews.setTextViewText(R.id.away_name, cursor.getString(ScoresAdapter.COL_AWAY));
                remoteViews.setContentDescription(R.id.away_name, cursor.getString(ScoresAdapter.COL_AWAY));
            }
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
