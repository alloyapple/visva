/**
 * 
 */
package com.visva.android.hangman.ultis;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.visva.android.hangman.definition.GlobalDef;

/**
 * @author kieu.thang
 *
 */
public class GamePreferences implements GlobalDef {
	public static String PREF_NAME = "HANGMAN_PREFERENCES";
	public static int PREF_MODE = Context.MODE_PRIVATE;
	/*
	 * get share preferences
	 * */
	public static SharedPreferences getPreferences(Context content){
		return content.getSharedPreferences(PREF_NAME, PREF_MODE);
	}
	/*
	 * get editor
	 * */
	public static Editor getEditor(Context context){
		return getPreferences(context).edit();
	}
	/*
	 * get int value
	 * */
	public static int getIntVal(Context context,String key,int defValue){
		return getPreferences(context).getInt(key, defValue);
	}
	/*
	 * set int value
	 * */
	public static void setIntVal(Context context,String key,int value){
		getEditor(context).putInt(key, value).commit();
	}
	/*
	 * get string 
	 * */
	public static String getStringVal(Context context,String key,String defValue){
		return getPreferences(context).getString(key, defValue);
	}
	/*
	 * Set string
	 * */
	public static void setStringVal(Context context,String key,String value){
		getEditor(context).putString(key, value).commit();
	}
}
