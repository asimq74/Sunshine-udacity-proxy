package com.example.android.sunshine.app.task;

import java.net.HttpURLConnection;

import com.example.android.sunshine.app.BuildConfig;
import com.example.android.sunshine.app.core.Factory;


/**
 * Created by U1C306 on 9/20/2016.
 */

public class HttpUrlConnectionFactory implements Factory<HttpURLConnection> {

	private final String urlString;

	public HttpUrlConnectionFactory(String urlString) {
		this.urlString = urlString;
	}

	@Override
	public HttpURLConnection create() {
		return BuildConfig.SHOULD_USE_PROXY ? new ProxyUrlConnection(urlString).create() : new StandardUrlConnection(urlString).create();
	}
}
