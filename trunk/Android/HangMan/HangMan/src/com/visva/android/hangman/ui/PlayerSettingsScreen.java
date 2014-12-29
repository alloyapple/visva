package com.visva.android.hangman.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;
import com.visva.android.hangman.ultis.GameSetting;
import com.visva.android.hangman.ultis.SoundEffect;

public class PlayerSettingsScreen extends Activity implements GlobalDef {
	/** Called when the activity is first created. */

	private ImageButton btn_back;
	private ImageButton btn_start;
	private EditText edt_player1;
	private EditText edt_player2;
	private TextView img_easy;
	private TextView img_standard;
	private TextView img_hard;
	private TextView img_animals;
	private TextView img_food;
	private TextView img_geography;
	private TextView img_holidays;
	private TextView img_sat;
	private TextView img_toefl;
	private TextView lbl_player1;
	private TextView lbl_player2;
	private TextView lbl_word_list;

	private Typeface mFont;
	private int mFontDefaultColor;
	private int mFontPressedColor;
	/*
	 * Word type
	 */
	private int word_list = STANDARD;

	/**
	 * @return the word_list
	 */
	public int getWord_list() {
		return word_list;
	}

	/**
	 * @param word_list
	 *            the word_list to set
	 */
	public void setWord_list(int word_list) {
		this.word_list = word_list;
	}

	private String str_player1;

	/**
	 * @return the str_player1
	 */
	public String getStr_player1() {
		return str_player1;
	}

	/**
	 * @param str_player1
	 *            the str_player1 to set
	 */
	public void setStr_player1(String str_player1) {
		if (str_player1.equals("")) {
			this.str_player1 = DEF_PLAYER1;// GamePreferences.getStringVal(this,
											// PLAYER1, DEF_PLAYER1);
		} else {
			this.str_player1 = str_player1;
		}

	}

	private String str_player2;

	/**
	 * @return the str_player2
	 */
	public String getStr_player2() {
		return str_player2;
	}

	/**
	 * @param str_player2
	 *            the str_player2 to set
	 */
	public void setStr_player2(String str_player2) {
		if (str_player2.equals("")) {
			if (GameSetting._game_mode == ONE_PLAYER_MODE) {
				this.str_player2 = DEF_OPPONENT;// GamePreferences.getStringVal(this,
												// OPPONENT, DEF_OPPONENT);
			} else {
				this.str_player2 = DEF_PLAYER2;// GamePreferences.getStringVal(this,
												// PLAYER2, DEF_PLAYER2);
			}
		} else {
			this.str_player2 = str_player2;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_player_settings);
		// do nothing
		initControl();
		// load sound
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		SoundEffect.loadSound(this);
		// Load pref
		loadPref();
		// reset high score
		GameSetting.resetStatistics();
		// limit text of name player 1, player 2
		int maxLength = 10;
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(maxLength);
		edt_player1.setFilters(FilterArray);
		edt_player2.setFilters(FilterArray);
		// BUTTON CLICKED NOTIFICATION
		/*
		 * BACK BUTTON
		 */
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent _mainMenuIntent = new Intent(PlayerSettingsScreen.this, MainMenuScreen.class);
				startActivity(_mainMenuIntent);
				finish();
			}
		});
		/*
		 * START BUTTON
		 */
		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GameSetting._game_mode = ONE_PLAYER_MODE;
				Intent intentGameBoard = new Intent(PlayerSettingsScreen.this, GameBoardScreen.class);
				setStr_player1(edt_player1.getText().toString());
				setStr_player2(edt_player2.getText().toString());
				intentGameBoard.putExtra(WORD_LIST, getWord_list());
				intentGameBoard.putExtra(PLAYER1, getStr_player1());
				intentGameBoard.putExtra(PLAYER2, getStr_player2());

				GamePreferences.setStringVal(PlayerSettingsScreen.this, PLAYER1, getStr_player1());
				GamePreferences.setStringVal(PlayerSettingsScreen.this, OPPONENT, getStr_player2());

				startActivity(intentGameBoard);
				finish();
			}
		});
		/*
		 * EASY
		 */
		img_easy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(EASY);
				onUserSelectWordList(getWord_list());
			}
		});
		/*
		 * STANDARD
		 */
		img_standard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(STANDARD);
				onUserSelectWordList(getWord_list());
			}
		});
		/*
		 * HARD
		 */
		img_hard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(HARD);
				onUserSelectWordList(getWord_list());
			}
		});
		/*
		 * ANIMALS
		 */
		img_animals.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(ANIMALS);
				onUserSelectWordList(getWord_list());
			}
		});
		/*
		 * FOOD
		 */
		img_food.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(FOOD);
				onUserSelectWordList(getWord_list());
			}
		});
		/*
		 * GEOGRAPHY
		 */
		img_geography.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(GEOGRAPHY);
				onUserSelectWordList(word_list);
			}
		});
		img_holidays.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(HOLIDAYS);
				onUserSelectWordList(getWord_list());
			}
		});
		/*
		 * SAT
		 */
		img_sat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(SAT);
				onUserSelectWordList(getWord_list());
			}
		});
		/*
		 * TOEFL
		 */
		img_toefl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setWord_list(TOEFL);
				onUserSelectWordList(getWord_list());
			}
		});

	}

	/*
	 * init control
	 */
	public void initControl() {
		mFont = Typeface.createFromAsset(getAssets(), "fonts/SHOWG.TTF");
		mFontDefaultColor = getResources().getColor(R.color.font_default_color);
		mFontPressedColor = getResources().getColor(R.color.font_pressed_color2);
		btn_back = (ImageButton) findViewById(R.id.imgback);
		btn_start = (ImageButton) findViewById(R.id.imgstart);
		lbl_player1 = (TextView) findViewById(R.id.lbl_player1);
		lbl_player1.setTypeface(mFont);
		lbl_player1.setTextColor(mFontDefaultColor);
		lbl_player2 = (TextView) findViewById(R.id.lbl_player2);
		lbl_player2.setTypeface(mFont);
		lbl_player2.setTextColor(mFontDefaultColor);
		lbl_word_list = (TextView) findViewById(R.id.lbl_word_list);
		lbl_word_list.setTypeface(mFont);
		lbl_word_list.setTextColor(mFontDefaultColor);

		edt_player1 = (EditText) findViewById(R.id.txt_player_one);
		int txtColor = getResources().getColor(R.color.txt_player_name_color);
		int txtHintColor = getResources().getColor(R.color.txt_player_name_hint_color);
		edt_player1.setTextColor(txtColor);
		edt_player1.setHintTextColor(txtHintColor);
		edt_player2 = (EditText) findViewById(R.id.txt_player_two);
		edt_player2.setTextColor(txtColor);
		edt_player2.setHintTextColor(txtHintColor);
		img_easy = (TextView) findViewById(R.id.easy);
		img_easy.setTypeface(mFont);
		img_easy.setTextColor(mFontDefaultColor);
		img_standard = (TextView) findViewById(R.id.standard);
		img_standard.setTypeface(mFont);
		img_standard.setTextColor(mFontDefaultColor);
		img_hard = (TextView) findViewById(R.id.hard);
		img_hard.setTypeface(mFont);
		img_hard.setTextColor(mFontDefaultColor);
		img_animals = (TextView) findViewById(R.id.animal);
		img_animals.setTypeface(mFont);
		img_animals.setTextColor(mFontDefaultColor);
		img_food = (TextView) findViewById(R.id.food);
		img_food.setTypeface(mFont);
		img_food.setTextColor(mFontDefaultColor);
		img_geography = (TextView) findViewById(R.id.geography);
		img_geography.setTypeface(mFont);
		img_geography.setTextColor(mFontDefaultColor);
		img_holidays = (TextView) findViewById(R.id.holidays);
		img_holidays.setTypeface(mFont);
		img_holidays.setTextColor(mFontDefaultColor);
		img_sat = (TextView) findViewById(R.id.sat);
		img_sat.setTypeface(mFont);
		img_sat.setTextColor(mFontDefaultColor);
		img_toefl = (TextView) findViewById(R.id.toefl);
		img_toefl.setTypeface(mFont);
		img_toefl.setTextColor(mFontDefaultColor);
	}

	/*
	 * Load game preference for ONE PLAYER MODE
	 */
	public void loadPref() {
		// Player name
		String player;
		// Opponent
		String opponent;
		// Player 2 name
		String player2 = "";
		player = GamePreferences.getStringVal(this, PLAYER1, DEF_PLAYER1);
		opponent = GamePreferences.getStringVal(this, OPPONENT, DEF_OPPONENT);
		player2 = GamePreferences.getStringVal(this, PLAYER2, DEF_PLAYER2);

		if (GameSetting._game_mode == ONE_PLAYER_MODE) {
			// Word list
			if (player.equalsIgnoreCase(DEF_PLAYER1)) {
				edt_player1.setHint(player);
			} else {
				edt_player1.setText(player);
				edt_player1.setSelection(edt_player1.getText().length());
			}
			if (opponent.equalsIgnoreCase(DEF_OPPONENT)) {
				edt_player2.setHint(opponent);
			} else {
				edt_player2.setText(opponent);
				edt_player2.setSelection(edt_player2.getText().length());
			}
			word_list = GamePreferences.getIntVal(this, WORD_LIST, STANDARD);
			switch (word_list) {
			case EASY:
				img_easy.setTextColor(mFontPressedColor);
				break;
			case STANDARD:
				img_standard.setTextColor(mFontPressedColor);
				break;
			case HARD:
				img_hard.setTextColor(mFontPressedColor);
				break;
			case ANIMALS:
				img_animals.setTextColor(mFontPressedColor);
				break;
			case FOOD:
				img_food.setTextColor(mFontPressedColor);
				break;
			case GEOGRAPHY:
				img_geography.setTextColor(mFontPressedColor);
				break;
			case HOLIDAYS:
				img_holidays.setTextColor(mFontPressedColor);
				break;
			case SAT:
				img_sat.setTextColor(mFontPressedColor);
				break;
			case TOEFL:
				img_toefl.setTextColor(mFontPressedColor);
				break;
			default:
				break;
			}
		} else {
			if (player.equalsIgnoreCase(DEF_PLAYER1)) {
				edt_player1.setHint(player);
			} else {
				edt_player1.setText(player);
				edt_player1.setSelection(edt_player1.getText().length());

			}
			if (player2.equalsIgnoreCase(DEF_PLAYER2)) {
				edt_player2.setHint(player2);
			} else {
				edt_player2.setText(player2);
				edt_player2.setSelection(edt_player2.getText().length());
			}
		}
	}

	/*
	 * On User select select word list type
	 */
	public void onUserSelectWordList(int word_list) {
		img_easy.setTextColor(mFontDefaultColor);
		img_standard.setTextColor(mFontDefaultColor);
		img_hard.setTextColor(mFontDefaultColor);
		img_animals.setTextColor(mFontDefaultColor);
		img_food.setTextColor(mFontDefaultColor);
		img_geography.setTextColor(mFontDefaultColor);
		img_holidays.setTextColor(mFontDefaultColor);
		img_sat.setTextColor(mFontDefaultColor);
		img_toefl.setTextColor(mFontDefaultColor);
		switch (word_list) {
		case EASY:
			img_easy.setTextColor(mFontPressedColor);
			break;
		case STANDARD:
			img_standard.setTextColor(mFontPressedColor);
			break;
		case HARD:
			img_hard.setTextColor(mFontPressedColor);
			break;
		case ANIMALS:
			img_animals.setTextColor(mFontPressedColor);
			break;
		case FOOD:
			img_food.setTextColor(mFontPressedColor);
			break;
		case GEOGRAPHY:
			img_geography.setTextColor(mFontPressedColor);
			break;
		case HOLIDAYS:
			img_holidays.setTextColor(mFontPressedColor);
			break;
		case SAT:
			img_sat.setTextColor(mFontPressedColor);
			break;
		case TOEFL:
			img_toefl.setTextColor(mFontPressedColor);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent _mainMenuIntent = new Intent(PlayerSettingsScreen.this, MainMenuScreen.class);
		startActivity(_mainMenuIntent);
		finish();
		super.onBackPressed();
	}

}