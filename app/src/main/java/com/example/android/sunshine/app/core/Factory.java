package com.example.android.sunshine.app.core;

/**
 * Created by U1C306 on 6/24/2016.
 */
public interface Factory<P> {

	/**
	 * Create a new object.
	 *
	 * @return a new instance
	 */
	P create();

}
