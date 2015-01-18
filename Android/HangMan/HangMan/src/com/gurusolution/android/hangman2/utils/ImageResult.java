package com.gurusolution.android.hangman2.utils;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Data model for the results coming from Google Image Search API
 * 
 */
public class ImageResult implements Serializable {
	// TODO
	private static final long serialVersionUID = 111111111;
			 
	private String fullUrl;
	private String thumbUrl;
	
	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl= json.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl = null;
			this.thumbUrl = null;
		}
		
	}
	
	/*
	 * Full size image
	 */
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	
	/*
	 * Thumb nail drive
	 */
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	
	public String toString() {
		return this.thumbUrl;
	}
	
	/*
	 * convert JSAON to ArrayList
	 */
	public static ArrayList <ImageResult> fromJSONArray(
			JSONArray array) {
		
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int x = 0; x < array.length(); x++) {
			try {
				results.add(new ImageResult(array.getJSONObject(x)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	
	
}
