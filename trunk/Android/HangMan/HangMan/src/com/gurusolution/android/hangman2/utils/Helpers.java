package com.gurusolution.android.hangman2.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.gurusolution.android.hangman2.constant.GlobalDef;

public class Helpers implements GlobalDef{
	public static MediaPlayer playSound(Context _context, int _id_sound, boolean _is_loop) {
		MediaPlayer _sound = MediaPlayer.create(_context, _id_sound);
		int musicEnable = GamePreferences.getIntVal(_context, MUSIC_ON, ON);
		if (_sound != null && musicEnable == ON) {
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
}
