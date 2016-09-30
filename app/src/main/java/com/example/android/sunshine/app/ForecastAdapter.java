package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

	public ForecastAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	private static final int VIEW_TYPE_TODAY = 0;
	private static final int VIEW_TYPE_FUTURE_DAY = 1;
	private static final int VIEW_TYPE_COUNT = 2;

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	/*
			This is where we fill-in the views with the contents of the cursor.
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView date = (TextView)view.findViewById(R.id.list_item_date_textview);
		TextView forecast = (TextView)view.findViewById(R.id.list_item_forecast_textview);
		TextView high = (TextView)view.findViewById(R.id.list_item_high_textview);
		TextView low = (TextView)view.findViewById(R.id.list_item_low_textview);
		boolean isMetric = Utility.isMetric(mContext);
		final String DEGREE  = "\u00b0";
		high.setText(String.format("%s %s", Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP), isMetric), DEGREE));
		low.setText(String.format("%s %s", Utility.formatTemperature(cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP), isMetric), DEGREE));
		forecast.setText(cursor.getString(ForecastFragment.COL_WEATHER_DESC));
		date.setText(Utility.getFriendlyDayString(context, cursor.getLong(ForecastFragment.COL_WEATHER_DATE)));
	}

	/*
			This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
			string.
	 */
	private String convertCursorRowToUXFormat(Cursor cursor) {
		String highAndLow = formatHighLows(
				cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
				cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
		return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
				" - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
				" - " + highAndLow;
	}

	/**
	 * Prepare the weather high/lows for presentation.
	 */
	private String formatHighLows(double high, double low) {
		boolean isMetric = Utility.isMetric(mContext);
		String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
		return highLowStr;
	}

	/*
			Remember that these views are reused as needed.
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// Choose the layout type
		int viewType = getItemViewType(cursor.getPosition());
		int layoutId = (viewType == VIEW_TYPE_TODAY) ? R.layout.list_item_forecast_today:R.layout.list_item_forecast;
		View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

		return view;
	}
}