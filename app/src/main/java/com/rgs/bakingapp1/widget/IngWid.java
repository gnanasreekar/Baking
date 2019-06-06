package com.rgs.bakingapp1.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.rgs.bakingapp1.R;

/**
 * Implementation of App Widget functionality.
 */
public class IngWid extends AppWidgetProvider {
    static String ings;
    TextView ing_tv;

    public String ing(String stringBuilder)
    {
        ings = stringBuilder;
        Log.d("inges" , ings);
        return stringBuilder;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Toast.makeText(context, "updates", Toast.LENGTH_SHORT).show();
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Toast.makeText(context, ings, Toast.LENGTH_SHORT).show();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ing_wid);
        views.setTextViewText(R.id.appwidget_text, ings);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

