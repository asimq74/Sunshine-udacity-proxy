package com.example.android.sunshine.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SunshineSyncService extends Service {

	private static SunshineSyncAdapter sSunshineSyncAdapter = null;
	private static final Object sSyncAdapterLock = new Object();

	@Override
	public IBinder onBind(Intent intent) {
		return sSunshineSyncAdapter.getSyncAdapterBinder();
	}

	@Override
	public void onCreate() {
		Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
		synchronized (sSyncAdapterLock) {
			if (sSunshineSyncAdapter == null) {
				sSunshineSyncAdapter = new SunshineSyncAdapter(getApplicationContext(), true);
			}
		}
	}
}