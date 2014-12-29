package com.visva.android.hangman.ui;

import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;
import com.visva.android.hangman.ultis.GameSetting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TwoPlayerSettingScreen extends Activity implements GlobalDef {
	private ImageButton btn_back;
	private ImageButton btn_start;
	private EditText edt_player1;
	private EditText edt_player2;
	private TextView lbl_player1;
	private TextView lbl_player2;
	private Typeface mFont;
	private int mFontDefaultColor;
	private int mFontPressedColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.two_players_settings);
		// do nothing
		initControl();
		// BUTTON CLICKED NOTIFICATION
		/*
		 * BACK BUTTON
		 */
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent _mainMenuIntent = new Intent(TwoPlayerSettingScreen.this, MainMenuScreen.class);
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
				GameSetting._game_mode = TWO_PLAYER_MODE;
				Intent intentEnterWords = new Intent(TwoPlayerSettingScreen.this, EnterWordToGuessScreen.class);
				setStr_player1(edt_player1.getText().toString());
				setStr_player2(edt_player2.getText().toString());
				intentEnterWords.putExtra(PLAYER1, getStr_player1());
				intentEnterWords.putExtra(PLAYER2, getStr_player2());
				// Player2 is always challenge first
				intentEnterWords.putExtra(PLAYER1_CHALLENGE, false);

				GamePreferences.setStringVal(TwoPlayerSettingScreen.this, PLAYER1, getStr_player1());
				GamePreferences.setStringVal(TwoPlayerSettingScreen.this, PLAYER2, getStr_player2());

				startActivity(intentEnterWords);
				finish();

			}
		});
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

	private void initControl() {
		mFont = Typeface.createFromAsset(getAssets(), "fonts/SHOWG.TTF");
		mFontDefaultColor = getResources().getColor(R.color.font_default_color);
		mFontPressedColor = getResources().getColor(R.color.font_pressed_color);
		btn_back = (ImageButton) findViewById(R.id.imgback);
		btn_start = (ImageButton) findViewById(R.id.imgstart);
		lbl_player1 = (TextView) findViewById(R.id.lbl_player1);
		lbl_player1.setTypeface(mFont);
		lbl_player1.setTextColor(mFontDefaultColor);
		lbl_player2 = (TextView) findViewById(R.id.lbl_player2);
		lbl_player2.setTypeface(mFont);
		lbl_player2.setTextColor(mFontDefaultColor);

		edt_player1 = (EditText) findViewById(R.id.txt_player_one);
		int txtColor = getResources().getColor(R.color.txt_player_name_color);
		int txtHintColor = getResources().getColor(R.color.txt_player_name_hint_color);
		edt_player1.setTextColor(txtColor);
		edt_player1.setHintTextColor(txtHintColor);
		edt_player2 = (EditText) findViewById(R.id.txt_player_two);
		edt_player2.setTextColor(txtColor);
		edt_player2.setHintTextColor(txtHintColor);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Intent _mainMenuIntent = new Intent(TwoPlayerSettingScreen.this, MainMenuScreen.class);
		startActivity(_mainMenuIntent);
		finish();
		super.onBackPressed();
	}
}
