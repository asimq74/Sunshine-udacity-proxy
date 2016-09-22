package com.example.android.sunshine.app.task;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

import com.example.android.sunshine.app.BuildConfig;
import com.example.android.sunshine.app.core.Factory;

/**
 * Created by U1C306 on 6/24/2016.
 */
public class ProxyUrlConnection implements Factory<HttpURLConnection> {

	private final String urlString;

	public ProxyUrlConnection(String urlString) {
		this.urlString = urlString;
	}

	protected HttpURLConnection getHttpURLConnection() throws IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(BuildConfig.PROXY_SERVER, BuildConfig.PROXY_PORT));
		Authenticator authenticator = new Authenticator() {

			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication(BuildConfig.PROXY_USER, BuildConfig.PROXY_PASSWORD.toCharArray()));
			}

		};
		Authenticator.setDefault(authenticator);
		URL url = new URL(urlString);

		// Create the request to OpenWeatherMap, and open the connection
		return (HttpURLConnection) url.openConnection(proxy);
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
