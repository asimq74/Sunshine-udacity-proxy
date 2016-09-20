package com.example.android.sunshine.app.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.android.sunshine.app.core.Factory;

/**
 * Created by U1C306 on 6/24/2016.
 */
public class StandardUrlConnection implements Factory<HttpURLConnection> {

	private final String urlString;

	public StandardUrlConnection(String urlString) {
		this.urlString = urlString;
	}

	protected HttpURLConnection getHttpURLConnection() throws IOException {
		URL url = new URL(urlString);
		return (HttpURLConnection) url.openConnection();
	}

	@Override
	public HttpURLConnection create() {
		try {
			return getHttpURLConnection();
		} catch(IOException e) {
			return null;
		}
	}
}
