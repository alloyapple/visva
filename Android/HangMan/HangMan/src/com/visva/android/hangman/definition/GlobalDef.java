/**
 * 
 */
package com.visva.android.hangman.definition;

/**
 * @author iRocker
 * 
 */
public interface GlobalDef {
	public final static int ONE_PLAYER_MODE = 0x00;
	public final static int TWO_PLAYER_MODE = 0x01;
	public static final String GAME_MODE = "GAME_MODE";
	/*
	 * PREFERENCES KEY NAME
	 */
	public static String PLAYER1 = "player_1";
	public static String PLAYER2 = "player_2";
	public static String OPPONENT = "opponent";
	public static String SOUND_ON = "sound_on";
	public static String MUSIC_ON = "music_on";
	public static String GALL_OWNS = "gall_owns";
	public static String WORD_LIST = "word_list";
	public static String CHALLENGE = "challenge";
	public static String PLAYER1_CHALLENGE = "player_1_challenge";
	/*
	 * OPTIONS VALUE
	 */
	public static final int ON = 1;
	public static final int OFF = 0;
	public static final int SHOW = 1;
	public static final int HIDE = 0;
	/*
	 * WORDS LIST VALUE
	 */
	public static final String DEF_PLAYER1 = "Player 1";
	public static final String DEF_PLAYER2 = "Player 2";
	public static final String DEF_OPPONENT = "Opponent";
	public static final int EASY = 0x00;
	public static final int STANDARD = 0x01;
	public static final int HARD = 0x02;
	public static final int ANIMALS = 0x03;
	public static final int FOOD = 0x04;
	public static final int GEOGRAPHY = 0x05;
	public static final int HOLIDAYS = 0x06;
	public static final int SAT = 0x07;
	public static final int TOEFL = 0x08;
	/*
	 * 
	 * */
	public static final int MAX_WORDS = 0x07;
	public static final int MAX_CHAR = 26;
	public static final int MAX_TRIES = 7;
	public static final int MAX_ENTRY_CHAR = 10;
	/*
	 * CHAR
	 */
	public static int DISABLE = -1;
	public static int ENABLE = 1;
	public static int NORMAL = 0;
	public static int GAME_WON = 0x00;
	public static int GAME_OVER = 0x01;
	public static int GAME_NOT_STARTED = 0x03;
	public static final int CHAR_A = 0x01;
	public static final int CHAR_B = 0x02;
	public static final int CHAR_C = 0x03;
	public static final int CHAR_D = 0x04;
	public static final int CHAR_E = 0x05;
	public static final int CHAR_F = 0x06;
	public static final int CHAR_G = 0x07;
	public static final int CHAR_H = 0x08;
	public static final int CHAR_I = 0x09;
	public static final int CHAR_J = 0x0A;
	public static final int CHAR_K = 0x0B;
	public static final int CHAR_L = 0x0C;
	public static final int CHAR_M = 0x0D;
	public static final int CHAR_N = 0x0E;
	public static final int CHAR_O = 0x0F;
	public static final int CHAR_P = 0x10;
	public static final int CHAR_Q = 0x11;
	public static final int CHAR_R = 0x12;
	public static final int CHAR_S = 0x13;
	public static final int CHAR_T = 0x14;
	public static final int CHAR_U = 0x15;
	public static final int CHAR_V = 0x16;
	public static final int CHAR_W = 0x17;
	public static final int CHAR_X = 0x18;
	public static final int CHAR_Y = 0x19;
	public static final int CHAR_Z = 0x1A;
	public static final int CHAR_DEL = 0x00;
	public static final Character[] ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

}
