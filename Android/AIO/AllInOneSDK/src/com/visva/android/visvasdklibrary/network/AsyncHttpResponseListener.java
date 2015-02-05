package com.visva.android.visvasdklibrary.network;

import org.apache.http.HttpResponse;

/**
 * Predefine some http listener methods
 * 
 * @author Kane
 */
public interface AsyncHttpResponseListener {
	/**
	 * Before get http response
	 */
	public void before();

	/**
	 * After get http response
	 * 
	 * @param statusCode
	 * @param response
	 */
	public void after(int statusCode, HttpResponse response);

}
