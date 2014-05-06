/**
 * 
 */
package com.visva.android.hangman.ultis;

import com.visva.android.hangman.definition.GlobalDef;

/**
 * @author kieu.thang
 *
 */
public class GameSetting implements GlobalDef{
	public static int _game_mode = 0xFF;
	public static int _one_mode_player1_hscore;
	public static int _one_mode_player2_hscore;
	public static int _two_mode_player1_hscore;
	public static int _two_mode_player2_hscore;
	public static String getCategoryName(int word_list){
		String ret = "";
		switch (word_list) {
		case EASY:
			ret = "EASY";
			break;
		case STANDARD:
			ret = "STANDARD";
			break;
		case HARD:
			ret = "HARD";
			break;
		case ANIMALS:
			ret = "ANIMALS";
			break;
		case FOOD:
			ret = "FOOD";
			break;
		case GEOGRAPHY:
			ret = "GEOGRAPHY";
			break;
		case HOLIDAYS:
			ret = "HOLIDAYS";
			break;
		case SAT:
			ret = "SAT";
			break;
		case TOEFL:
			ret = "TOEFL";
			break;
		default:
			ret = "STANDARD";
			break;
		}
		return ret;
	}
	public static void resetStatistics(){
		_one_mode_player1_hscore = 0;
		_one_mode_player2_hscore = 0;
		_two_mode_player1_hscore = 0;
		_two_mode_player2_hscore = 0;
	}
}
