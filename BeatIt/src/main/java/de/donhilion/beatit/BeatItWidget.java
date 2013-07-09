package de.donhilion.beatit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Benn on 07.07.13.
 */
public class BeatItWidget extends AppWidgetProvider {

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, BeatItWidget.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BeatItWidget.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //every 30 seconds
        am.setRepeating(AlarmManager.RTC, 0, 30000, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        long seconds = ((System.currentTimeMillis() / 1000) + 3600) % 86400; // get seconds of the day in CET
        int beats = (int) (seconds / 86.4);

        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.beatit_widget);

        rv.setTextViewText(R.id.textView, "@" + beats);

        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        ComponentName thisWidget = new ComponentName(context, BeatItWidget.class);
        appWidgetManager.updateAppWidget(thisWidget, rv);
    }
}
