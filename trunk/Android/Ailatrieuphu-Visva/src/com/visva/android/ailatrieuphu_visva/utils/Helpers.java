package com.visva.android.ailatrieuphu_visva.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService.Session;
import android.widget.Toast;

import com.visva.android.ailatrieuphu_visva.R;

public class Helpers {
	public static MediaPlayer playSound(Context _context, int _id_sound, boolean _is_loop) {
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
	
//	public void publishFeedDialog(Context context) {
//		Bundle params = new Bundle();
//		String pic = context.getString(R.string.fb_picture);
//		params.putString("name", "Shoppie");
//		params.putString("caption", "");
//		params.putString("description",context.getString(R.string.invitation_content, custId));
//		params.putString("link", "http://www.shoppie.com.vn/");
//		params.putString("picture", pic);
////		params.putString("to", "" + friend.getUserId());
//		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mContext,Session.getActiveSession(), params)).setOnCompleteListener(
//				new OnCompleteListener() {
//
//					@Override
//					public void onComplete(Bundle values,
//							FacebookException error) {
//						if (error == null) {
//							// When the story is posted, echo the success
//							// and the post Id.
//							final String name = friend.getUserName();
//							if (name != null) {
//								Toast.makeText(mContext, "Invited " + name,
//										Toast.LENGTH_SHORT).show();
//							} else {
//								// User clicked the Cancel button
//								Toast.makeText(mContext, "Publish cancelled",
//										Toast.LENGTH_SHORT).show();
//							}
//						} else if (error instanceof FacebookOperationCanceledException) {
//							// User clicked the "x" button
//							Toast.makeText(mContext, "Publish cancelled",
//									Toast.LENGTH_SHORT).show(); 
//						} else {
//							// Generic, ex: network error
//							Toast.makeText(mContext, "Error posting story",
//									Toast.LENGTH_SHORT).show();
//						}
//					}
//				}).build();
//		feedDialog.show();
//
//	}
}
