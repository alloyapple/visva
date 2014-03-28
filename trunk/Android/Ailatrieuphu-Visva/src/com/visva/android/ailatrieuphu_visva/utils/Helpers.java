package com.visva.android.ailatrieuphu_visva.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.visva.android.ailatrieuphu_visva.R;

public class Helpers {
	public static MediaPlayer playSound(Context _context, int _id_sound,
			boolean _is_loop) {
		MediaPlayer _sound = MediaPlayer.create(_context, _id_sound);
		if (_sound != null) {
			_sound.setLooping(_is_loop);
			_sound.seekTo(0);
			_sound.start();
		}
		Runtime.getRuntime().gc();
		return _sound;
	}

	public static void releaseSound(MediaPlayer _sound) {
		if (_sound != null) {
			_sound.release();
			_sound = null;
			Runtime.getRuntime().gc();
		}
	}

	public static void wait_sound(MediaPlayer _sound) {
		if (_sound != null) {
			while (_sound.isPlaying()) {
				try {
					Thread.sleep(10L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void stop_sound(MediaPlayer _sound) {

	}

	public static String parse_seconds_to_time_string(int s) {
		String t = "";
		int min = s / 60;
		int sec = s % 60;
		t += ((min > 9) ? "" : "0") + min;
		t += " : ";
		t += ((sec > 9) ? "" : "0") + sec;
		return t;
	}

	public static void publishFeedDialog(final Context context) {
		Bundle params = new Bundle();
		String pic = "Test";
		params.putString("name", "Test name");
		params.putString("caption", "");
		params.putString("description","Test decription");
		params.putString("link",
				"https://support.google.com/admob/answer/1307283?hl=en");
		params.putString(
				"picture",
				"http://img3.tamtay.vn/files/photo2/2012/12/22/9/42642/50d51ffc_5a2d6408_anh-dep_anh-nguoi-mau-dep_anh-girl-em-rat-xinh_yuko-ogura.jpg");
		// params.putString("to", "" + friend.getUserId());
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(context,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// User clicked the Cancel button
							Toast.makeText(context, "Publish cancelled",Toast.LENGTH_SHORT).show();
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(context, "Publish cancelled",Toast.LENGTH_SHORT).show();
						} else {
							// Generic, ex: network error
							Toast.makeText(context, "Error posting story",Toast.LENGTH_SHORT).show();
						}
					}
				}).build();
		feedDialog.show();

	}
}
