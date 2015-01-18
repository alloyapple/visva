/**
 * 
 */
package com.gurusolution.android.hangman2.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.gurusolution.android.hangman2.R;
import com.gurusolution.android.hangman2.constant.GlobalDef;

/**
 * @author kieu.thang
 * 
 */
public class SoundEffect implements GlobalDef {
	private static SoundPool sounds;
	private static int chalkcircle;
	private static int chalkletter;
	private static int chalkline;
	private static int eraser;
	private static int lose;
	private static int win;
	private static int wrongletter;
	private static int sound = ON;
	private static AudioManager mAudioManager;

	/*
	 * Load Context
	 */
	public static void loadSound(Context context) {
		sound = GamePreferences.getIntVal(context, SOUND_ON, ON);
		sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		// three ref. to the sounds I need in the application
		chalkcircle = sounds.load(context, R.raw.chalkcircle, 1);
		chalkletter = sounds.load(context, R.raw.chalkletter, 1);
		chalkline = sounds.load(context, R.raw.chalkline, 1);
		eraser = sounds.load(context, R.raw.eraser, 1);
		lose = sounds.load(context, R.raw.lose, 1);
		win = sounds.load(context, R.raw.win, 1);
		wrongletter = sounds.load(context, R.raw.wrongletter, 1);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	public static void playSound_chalkcircle() {
		if (sound == OFF) {
			return;
		} else {
			sounds.play(chalkcircle, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public static void playSound_chalkletter() {
		if (sound == OFF) {
			return;
		} else {
			sounds.play(chalkletter, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public static void playSound_chalkline() {
		if (sound == OFF) {
			return;
		} else {
			sounds.play(chalkline, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public static void playSound_eraser() {
		if (sound == OFF) {
			return;
		} else {
			sounds.play(eraser, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public static void playSound_lose() {
		if (sound == OFF) {
			return;
		} else {
			sounds.play(lose, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public static void playSound_win() {
		if (sound == OFF) {
			return;
		} else {
			sounds.play(win, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public static void playSound_wrongletter() {
		if (sound == OFF) {
			return;
		} else {
			sounds.play(wrongletter, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public static float getVolume() {
		// Getting the user sound settings
		float actualVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		return volume;
	}
}
