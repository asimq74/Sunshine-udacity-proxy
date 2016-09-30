package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract.WeatherEntry;

/**
 * Created by U1C306 on 9/30/2016.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int COL_WEATHER_DATE = 1;
	private static final int COL_WEATHER_DESC = 2;
	private static final int COL_WEATHER_ID = 0;
	private static final int COL_WEATHER_MAX_TEMP = 3;
	private static final int COL_WEATHER_MIN_TEMP = 4;
	private static final int COL_WEATHER_HUMIDITY = 5;
	private static final int COL_WEATHER_WIND_SPEED = 6;
	private static final int COL_WEATHER_PRESSURE = 7;
	private static final int DETAIL_LOADER = 0;
	private static final String[] FORECAST_COLUMNS = {
			WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
			WeatherEntry.COLUMN_DATE,
			WeatherEntry.COLUMN_SHORT_DESC,
			WeatherEntry.COLUMN_MAX_TEMP,
			WeatherEntry.COLUMN_MIN_TEMP,
			WeatherEntry.COLUMN_HUMIDITY,
			WeatherEntry.COLUMN_WIND_SPEED,
			WeatherEntry.COLUMN_PRESSURE
	};
	private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
	private static final String LOG_TAG = DetailFragment.class.getSimpleName();
	private String mForecastStr;
	private ShareActionProvider mShareActionProvider;
	public DetailFragment() {
		setHasOptionsMenu(true);
	}

	private Intent createShareForecastIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
		return shareIntent;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		getLoaderManager().initLoader(DETAIL_LOADER, null, this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.v(LOG_TAG, "In onCreateLoader");
		Intent intent = getActivity().getIntent();
		if (intent == null) {
			return null;
		}
		return new CursorLoader(getActivity(), intent.getData(), FORECAST_COLUMNS, null, null, null);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.detailfragment, menu);

		// Retrieve the share menu item
		MenuItem menuItem = menu.findItem(R.id.action_share);

		// Get the provider and hold onto it to set/change the share intent.
		mShareActionProvider =
				(ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

		// Attach an intent to this ShareActionProvider.  You can update this at any time,
		// like when the user selects a new piece of data they might like to share.
		if (mForecastStr != null) {
			mShareActionProvider.setShareIntent(createShareForecastIntent());
		} else {
			Log.d(LOG_TAG, "Share Action Provider is null?");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
		return rootView;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (!data.moveToFirst()) {
			return;
		}
		ViewHolder viewHolder = new ViewHolder(getView());
		getView().setTag(viewHolder);
		String weatherDate = Utility.getFriendlyDayString(getActivity(), data.getLong(ForecastFragment.COL_WEATHER_DATE));
		String weatherDesc = data.getString(COL_WEATHER_DESC);
		boolean isMetric = Utility.isMetric(getActivity());
		String high = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
		String low = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
		String humidity = Utility.formatHumidity(getActivity(), data.getFloat(COL_WEATHER_HUMIDITY));
		String pressure = Utility.formatPressure(getActivity(), data.getFloat(COL_WEATHER_PRESSURE));
		String wind = Utility.formatWind(getActivity(), data.getFloat(COL_WEATHER_WIND_SPEED));
		viewHolder.dateView.setText(weatherDate);
		viewHolder.highTempView.setText(high);
		viewHolder.descriptionView.setText(weatherDesc);
		viewHolder.lowTempView.setText(low);
		viewHolder.iconView.setImageResource(R.drawable.ic_launcher);
		viewHolder.humidityView.setText(humidity);
		viewHolder.pressureView.setText(pressure);
		viewHolder.windView.setText(wind);
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(createShareForecastIntent());
		}
	}

	public static class ViewHolder {
		public final ImageView iconView;
		public final TextView dateView;
		public final TextView descriptionView;
		public final TextView highTempView;
		public final TextView lowTempView;
		public final TextView humidityView;
		public final TextView pressureView;
		public final TextView windView;

		public ViewHolder(View view) {
			iconView = (ImageView) view.findViewById(R.id.detail_item_icon);
			dateView = (TextView) view.findViewById(R.id.detail_item_date_textview);
			descriptionView = (TextView) view.findViewById(R.id.detail_item_forecast_textview);
			highTempView = (TextView) view.findViewById(R.id.detail_item_high_textview);
			lowTempView = (TextView) view.findViewById(R.id.detail_item_low_textview);
			humidityView = (TextView) view.findViewById(R.id.detail_item_humidity);
			pressureView = (TextView) view.findViewById(R.id.detail_item_pressure);
			windView = (TextView) view.findViewById(R.id.detail_item_wind);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {}
}
