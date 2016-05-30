package com.audioguides.sunshine.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.StringDef;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE = 1;

    /*
        Remember that these views are reused as needed.
     */

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());

        int layoutID = -1;

        if (viewType == VIEW_TYPE_TODAY) layoutID = R.layout.list_item_forecast_today;
        else layoutID = R.layout.list_item_forecast;

        //the view is created and the viewholder assigned to it as a tag
        View view = LayoutInflater.from(context).inflate(layoutID, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        ViewHolder holder = (ViewHolder) view.getTag();

        // Read weather icon ID from cursor
        int weatherID = cursor.getInt(ForecastFragment.COL_WEATHER_ID);
        holder.iconView.setImageResource(R.mipmap.ic_launcher);

        //Read date from cursor
        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        //Find TextView and set formatted date on it
        holder.dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        // Read weather forecast from cursor
        String weatherDesc = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        holder.descriptionView.setText(weatherDesc);

        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double highTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        holder.highView.setText(Utility.formatTemperature(highTemp, isMetric));

        // Read low temperature from cursor
        double lowTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        holder.lowView.setText(Utility.formatTemperature(lowTemp, isMetric));
    }


    /*
             VIEWHOLDER
             this allows a quicker scrolling and refreshing of the layout.
             The views are recycled and by using a viewholder, the ids are stored here and they don't
             need to be searched everytime
     */
    public static class ViewHolder{
        ImageView iconView;
        TextView dateView;
        TextView descriptionView;
        TextView highView;
        TextView lowView;

        public ViewHolder (View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView)view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView)view.findViewById(R.id.list_item_forecast_textview);
            highView = (TextView)view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView)view.findViewById(R.id.list_item_low_textview);
        }

    }

}
