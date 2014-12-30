package com.visva.android.hangman.ui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.visva.android.hangman.R;
import com.visva.android.hangman.adapter.ImageResultArrayAdapter;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;
import com.visva.android.hangman.ultis.GameSetting;
import com.visva.android.hangman.ultis.HangManSqlite;
import com.visva.android.hangman.ultis.ImageResult;
import com.visva.android.hangman.ultis.SoundEffect;
import com.visva.android.hangman.ultis.Timer;

@SuppressLint("UseSparseArrays")
public class GameBoardScreen extends Activity implements GlobalDef {
	/** Called when the activity is first created. */
	/*
	 * 
	 * */
	private ImageView char_a;
	private ImageView char_b;
	private ImageView char_c;
	private ImageView char_d;
	private ImageView char_e;
	private ImageView char_f;
	private ImageView char_g;
	private ImageView char_h;
	private ImageView char_i;
	private ImageView char_j;
	private ImageView char_k;
	private ImageView char_l;
	private ImageView char_m;
	private ImageView char_n;
	private ImageView char_o;
	private ImageView char_p;
	private ImageView char_q;
	private ImageView char_r;
	private ImageView char_s;
	private ImageView char_t;
	private ImageView char_u;
	private ImageView char_v;
	private ImageView char_w;
	private ImageView char_x;
	private ImageView char_y;
	private ImageView char_z;
	private ImageView imgresult;
	private HashMap<Integer, ImageView> mCharButtons;
	private TextView txt_player1;
	private TextView txt_player2;
	private TextView txt_player1_score;
	private TextView txt_player2_score;
	private ImageView img_player1_turn;
	private ImageView img_player2_turn;
	private ImageButton btn_menu_gameboard;
	private ImageButton btn_menu_new;
	private int mNumberOfTries;
	private String strPlayer1Name;
	private String mSolution;
	private int mGameCompleted = GAME_NOT_STARTED;
	public static final int DIALOG_GAME_FINISHED = 0x01;
	public static final int DIALOG_CONFIRMATION = 0x03;
	public static final int DIALOG_NEW_WORD = 0x04;
	public Dialog confirmationDialog;
	public Dialog newWordDialog;
	public Button btnGameOK;
	public Button btnGameCancel;
	public Dialog gameFinishedDialog;
	public ImageButton gameFinishButtonOK;
	private TextView txt_mess_dialog;
	private TextView txt_word_dialog;
	private ListView listSuggest;
	private String mChallenge;
	private HangManSqlite mDatabase;
	private int word_list;
	private boolean mInSolutionOk;
	private AdView adView;
	private long advShowInterval = 0;
	private GameTimer mGameTimer = new GameTimer();
	LinearLayout wordImageLayout;
	private boolean player1Challenge = false;
	private float textSizeDialog = 0;
	public static final int W_COLOR = 0xffdee66c;
	private Typeface mFont;
	private int mFontDefaultColor;
	private int mFontFoundWordColor;
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNumberOfTries = 0;
		mFont = Typeface.createFromAsset(getAssets(), "fonts/SHOWG.TTF");
		mFontDefaultColor = getResources().getColor(R.color.font_default_color);
		mFontFoundWordColor = getResources().getColor(R.color.found_word);

		if (GameSetting._game_mode == ONE_PLAYER_MODE) {
			setContentView(R.layout.one_player_game_board_screen);
			initControl();
			openHmDatabases();
			getMapCharButtons();
			onOnePlayerModeCreated();
			onImageSearch();
			Log.e("Tag", "One Plzyer created");
		} else if (GameSetting._game_mode == TWO_PLAYER_MODE) {
			setContentView(R.layout.two_player_game_board_screen);
			initControl();
			getMapCharButtons();
			onTwoPlayerModeCreated();
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
			Intent data = new Intent();
			data.putExtra("exit", RESULT_OK);
			finish();
			return;
		}
		updateHighScore();
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		SoundEffect.loadSound(this);
		btn_menu_gameboard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_CONFIRMATION);
			}
		});
		btn_menu_new.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_NEW_WORD);
			}
		});
	}

	public void openHmDatabases() {
		mDatabase = new HangManSqlite(this);
		try {
			mDatabase.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			mDatabase.openDataBase();

		} catch (SQLException sqle) {
			throw sqle;
		}
	}

	public void initControl() {
		char_a = (ImageView) findViewById(R.id.char_a);
		char_b = (ImageView) findViewById(R.id.char_b);
		char_c = (ImageView) findViewById(R.id.char_c);
		char_d = (ImageView) findViewById(R.id.char_d);
		char_e = (ImageView) findViewById(R.id.char_e);
		char_f = (ImageView) findViewById(R.id.char_f);
		char_g = (ImageView) findViewById(R.id.char_g);
		char_h = (ImageView) findViewById(R.id.char_h);
		char_i = (ImageView) findViewById(R.id.char_i);
		char_j = (ImageView) findViewById(R.id.char_j);
		char_k = (ImageView) findViewById(R.id.char_k);
		char_l = (ImageView) findViewById(R.id.char_l);
		char_m = (ImageView) findViewById(R.id.char_m);
		char_n = (ImageView) findViewById(R.id.char_n);
		char_o = (ImageView) findViewById(R.id.char_o);
		char_p = (ImageView) findViewById(R.id.char_p);
		char_q = (ImageView) findViewById(R.id.char_q);
		char_r = (ImageView) findViewById(R.id.char_r);
		char_s = (ImageView) findViewById(R.id.char_s);
		char_t = (ImageView) findViewById(R.id.char_t);
		char_u = (ImageView) findViewById(R.id.char_u);
		char_v = (ImageView) findViewById(R.id.char_v);
		char_w = (ImageView) findViewById(R.id.char_w);
		char_x = (ImageView) findViewById(R.id.char_x);
		char_y = (ImageView) findViewById(R.id.char_y);
		char_z = (ImageView) findViewById(R.id.char_z);

		wordImageLayout = (LinearLayout) findViewById(R.id.word_layout);
		btn_menu_gameboard = (ImageButton) findViewById(R.id.btn_menu_gameboard);
		btn_menu_new = (ImageButton) findViewById(R.id.btn_new_gameboard);

		if (GameSetting._game_mode == ONE_PLAYER_MODE) {
			listSuggest = (ListView) findViewById(R.id.list_suggest);
			imageAdapter = new ImageResultArrayAdapter(this, imageResults);
			listSuggest.setAdapter(imageAdapter);
		} else {
			// txt_category = (SMTextView) findViewById(R.id.txt_category);
			txt_player1 = (TextView) findViewById(R.id.player1);
			txt_player2 = (TextView) findViewById(R.id.player2);
			txt_player1.getTextSize();
			txt_player1_score = (TextView) findViewById(R.id.player1_score);
			txt_player2_score = (TextView) findViewById(R.id.player2_score);
			txt_player1.setTypeface(mFont);
			txt_player1.setTextColor(mFontDefaultColor);
			txt_player2.setTypeface(mFont);
			txt_player2.setTextColor(mFontDefaultColor);
			txt_player1_score.setTypeface(mFont);
			txt_player1_score.setTextColor(mFontDefaultColor);
			txt_player2_score.setTypeface(mFont);
			txt_player2_score.setTextColor(mFontDefaultColor);

			img_player1_turn = (ImageView) findViewById(R.id.img_player1_turn);
			img_player2_turn = (ImageView) findViewById(R.id.img_player2_turn);
		}
	}

	public void getMapCharButtons() {
		mCharButtons = new HashMap<Integer, ImageView>();
		mCharButtons.put(CHAR_A, char_a);
		mCharButtons.put(CHAR_B, char_b);
		mCharButtons.put(CHAR_C, char_c);
		mCharButtons.put(CHAR_D, char_d);
		mCharButtons.put(CHAR_E, char_e);
		mCharButtons.put(CHAR_F, char_f);
		mCharButtons.put(CHAR_G, char_g);
		mCharButtons.put(CHAR_H, char_h);
		mCharButtons.put(CHAR_I, char_i);
		mCharButtons.put(CHAR_J, char_j);
		mCharButtons.put(CHAR_K, char_k);
		mCharButtons.put(CHAR_L, char_l);
		mCharButtons.put(CHAR_M, char_m);
		mCharButtons.put(CHAR_N, char_n);
		mCharButtons.put(CHAR_O, char_o);
		mCharButtons.put(CHAR_P, char_p);
		mCharButtons.put(CHAR_Q, char_q);
		mCharButtons.put(CHAR_R, char_r);
		mCharButtons.put(CHAR_S, char_s);
		mCharButtons.put(CHAR_T, char_t);
		mCharButtons.put(CHAR_U, char_u);
		mCharButtons.put(CHAR_V, char_v);
		mCharButtons.put(CHAR_W, char_w);
		mCharButtons.put(CHAR_X, char_x);
		mCharButtons.put(CHAR_Y, char_y);
		mCharButtons.put(CHAR_Z, char_z);
		for (Integer num : mCharButtons.keySet()) {
			ImageView b = (ImageView) mCharButtons.get(num);
			b.setTag(num);
			b.setOnClickListener(mButtonClick);
		}
	}

	/*
     * 
     * */
	private OnClickListener mButtonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int keyId = (Integer) v.getTag();
			Character c = ALPHABET[keyId - 1];
			mInSolutionOk = onCheckChar(c);
			if (onCheckChar(c) == true) {
				ImageView img = (ImageView) mCharButtons.get(keyId);
				v.setTag(keyId);
				img.setBackgroundResource(R.drawable.letters_bg);
				img.setImageResource(geRightDrawableOfChar(c));
				v.setEnabled(false);
				checkGameProgress(c);
				SoundEffect.playSound_chalkcircle();

			} else {
				ImageView img = (ImageView) mCharButtons.get(keyId);
				v.setTag(keyId);
				img.setBackgroundResource(R.drawable.letters_bg);
				img.setImageResource(geDisabledDrawableOfChar(c));
				v.setEnabled(false);
				checkGameProgress(c);
				SoundEffect.playSound_chalkletter();
				SoundEffect.playSound_chalkline();
			}
		}
	};

	public void resetAlphabetBoard() {
		for (Integer num : mCharButtons.keySet()) {
			Character c = ALPHABET[num - 1];
			ImageView b = (ImageView) mCharButtons.get(num);
			b.setTag(num);
			b.setEnabled(true);
			b.setBackgroundResource(R.drawable.blank_letters_bg);
			b.setImageResource(getNormalDrawableOfChar(c));
		}
	}

	private void checkGameProgress(Character c) {
		if (!mInSolutionOk) {
			mNumberOfTries++;
			drawHangman();
			if (mNumberOfTries == MAX_TRIES) {
				drawHangman();
				mGameCompleted = GAME_OVER;
				onGameFnished();
			}
		} else {
			char[] ch;
			char[] sol;
			ch = mChallenge.toCharArray();
			sol = mSolution.toCharArray();
			for (int i = 0; i < ch.length; i++) {
				if (ch[i] == c) {
					sol[i] = c;
				}
			}
			mSolution = new String(sol);
			if (mSolution.equalsIgnoreCase(mChallenge)) {
				mGameCompleted = GAME_WON;
				try {
					Thread.sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				onGameFnished();
			}
			setRightLetter(mSolution);
		}
	}

	public int geRightDrawableOfChar(Character c) {
		switch (c) {
		case 'A':
			return R.drawable.a_right;
		case 'B':
			return R.drawable.b_right;
		case 'C':
			return R.drawable.c_right;
		case 'D':
			return R.drawable.d_right;
		case 'E':
			return R.drawable.e_right;
		case 'F':
			return R.drawable.f_right;
		case 'G':
			return R.drawable.g_right;
		case 'H':
			return R.drawable.h_right;
		case 'I':
			return R.drawable.i_right;
		case 'J':
			return R.drawable.j_right;
		case 'K':
			return R.drawable.k_right;
		case 'L':
			return R.drawable.l_right;
		case 'M':
			return R.drawable.m_right;
		case 'N':
			return R.drawable.n_right;
		case 'O':
			return R.drawable.o_right;
		case 'P':
			return R.drawable.p_right;
		case 'Q':
			return R.drawable.q_right;
		case 'R':
			return R.drawable.r_right;
		case 'S':
			return R.drawable.s_right;
		case 'T':
			return R.drawable.t_right;
		case 'U':
			return R.drawable.u_right;
		case 'V':
			return R.drawable.v_right;
		case 'W':
			return R.drawable.w_right;
		case 'X':
			return R.drawable.x_right;
		case 'Y':
			return R.drawable.y_right;
		case 'Z':
			return R.drawable.z_right;
		default:
			return 0;
		}
	}

	public int getNormalDrawableOfChar(Character c) {
		switch (c) {
		case 'A':
			return R.drawable.a;
		case 'B':
			return R.drawable.b;
		case 'C':
			return R.drawable.c;
		case 'D':
			return R.drawable.d;
		case 'E':
			return R.drawable.e;
		case 'F':
			return R.drawable.f;
		case 'G':
			return R.drawable.g;
		case 'H':
			return R.drawable.h;
		case 'I':
			return R.drawable.i;
		case 'J':
			return R.drawable.j;
		case 'K':
			return R.drawable.k;
		case 'L':
			return R.drawable.l;
		case 'M':
			return R.drawable.m;
		case 'N':
			return R.drawable.n;
		case 'O':
			return R.drawable.o;
		case 'P':
			return R.drawable.p;
		case 'Q':
			return R.drawable.q;
		case 'R':
			return R.drawable.r;
		case 'S':
			return R.drawable.s;
		case 'T':
			return R.drawable.t;
		case 'U':
			return R.drawable.u;
		case 'V':
			return R.drawable.v;
		case 'W':
			return R.drawable.w;
		case 'X':
			return R.drawable.x;
		case 'Y':
			return R.drawable.y;
		case 'Z':
			return R.drawable.z;
		default:
			return 0;
		}
	}

	public int geDisabledDrawableOfChar(Character c) {
		switch (c) {
		case 'A':
			return R.drawable.a_disabled;
		case 'B':
			return R.drawable.b_disabled;
		case 'C':
			return R.drawable.c_disabled;
		case 'D':
			return R.drawable.d_disabled;
		case 'E':
			return R.drawable.e_disabled;
		case 'F':
			return R.drawable.f_disabled;
		case 'G':
			return R.drawable.g_disabled;
		case 'H':
			return R.drawable.h_disabled;
		case 'I':
			return R.drawable.i_disabled;
		case 'J':
			return R.drawable.j_disabled;
		case 'K':
			return R.drawable.k_disabled;
		case 'L':
			return R.drawable.l_disabled;
		case 'M':
			return R.drawable.m_disabled;
		case 'N':
			return R.drawable.n_disabled;
		case 'O':
			return R.drawable.o_disabled;
		case 'P':
			return R.drawable.p_disabled;
		case 'Q':
			return R.drawable.q_disabled;
		case 'R':
			return R.drawable.r_disabled;
		case 'S':
			return R.drawable.s_disabled;
		case 'T':
			return R.drawable.t_disabled;
		case 'U':
			return R.drawable.u_disabled;
		case 'V':
			return R.drawable.v_disabled;
		case 'W':
			return R.drawable.w_disabled;
		case 'X':
			return R.drawable.x_disabled;
		case 'Y':
			return R.drawable.y_disabled;
		case 'Z':
			return R.drawable.z_disabled;
		default:
			return 0;
		}
	}

	boolean onCheckChar(Character c) {
		boolean ret = false;
		for (int i = 0; i < mChallenge.length(); i++) {
			if (c == mChallenge.charAt(i)) {
				ret = true;
				break;
			} else {
				ret = false;
			}

		}
		return ret;
	}

	public void onOnePlayerModeCreated() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			word_list = extras.getInt(WORD_LIST);
			strPlayer1Name = extras.getString(PLAYER1);
			GamePreferences.setIntVal(this, WORD_LIST, word_list);
			onNewGame();
		}
	}

	public void onTwoPlayerModeCreated() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			strPlayer1Name = extras.getString(PLAYER1);
			String str_player2 = extras.getString(PLAYER2);
			player1Challenge = extras.getBoolean(PLAYER1_CHALLENGE);
			if (player1Challenge) {
				img_player1_turn.setVisibility(View.INVISIBLE);
				img_player2_turn.setVisibility(View.VISIBLE);
			} else {
				img_player1_turn.setVisibility(View.VISIBLE);
				img_player2_turn.setVisibility(View.INVISIBLE);
			}
			mChallenge = extras.getString(CHALLENGE);
			txt_player1.setText(strPlayer1Name);
			txt_player2.setText(str_player2);
			onNewGame();
		}
	}

	public void onNewGame() {
		if (GameSetting._game_mode == ONE_PLAYER_MODE) {
			String categoryName = GameSetting.getCategoryName(word_list);
			mChallenge = mDatabase.GetRandomWord(categoryName.toLowerCase());
		} else {

		}
		mNumberOfTries = 0;
		mChallenge = stringToUper(mChallenge);
		Log.e("tag", mChallenge.toString());
		char[] charArray = new char[mChallenge.length()];
		Arrays.fill(charArray, '_');
		mSolution = new String(charArray);
		mGameTimer.start();
		drawHangman();
		setRightLetter(mSolution);
		updateHighScore();
		resetAlphabetBoard();
		int gall_owns = GamePreferences.getIntVal(this, GALL_OWNS, SHOW);
		if (gall_owns == HIDE) {
			mNumberOfTries = 1;
		}
	}

	public void setRightLetter(String mSolution) {
		wordImageLayout.removeAllViews();
		for (int i = 0; i < mSolution.length(); i++) {
			char c = mSolution.charAt(i);
			c = Character.toLowerCase(c);
			ImageView cImage = new ImageView(getApplicationContext());
			if (c == '_') {
				cImage.setImageResource(R.drawable.blank_char);
			} else {
				cImage.setImageResource(getResouceId(c));
			}
			wordImageLayout.addView(cImage);

		}
		wordImageLayout.postInvalidate();
		wordImageLayout.invalidate();
	}

	private int getResouceId(char c) {
		int drawableId = 0;
		try {
			String drawableName = String.valueOf(c);
			Class res = R.drawable.class;
			Field field = res.getField(drawableName);
			drawableId = field.getInt(null);
		} catch (Exception e) {
			throw new Error("Failure to get drawable id.");
		}
		return drawableId;
	}

	public void drawHangman() {
		switch (mNumberOfTries) {
		case 1:
			drawGallowns();
			break;
		case 2:
			drawHead();
			break;
		case 3:
			drawTorso();
			break;
		case 4:
			drawRightLeg();
			break;
		case 5:
			drawLeftLeg();
			break;
		case 6:
			drawLeftArm();
			break;
		case 7:
			drawRightArm();
			break;
		default:
			ImageView gallowns_layout = (ImageView) findViewById(R.id.gallow);
			gallowns_layout.setBackgroundResource(R.drawable.gallows_anim_0);
			break;
		}
	}

	private void drawGallowns() {
		int gall_owns = GamePreferences.getIntVal(this, GALL_OWNS, SHOW);
		ImageView gallowns_layout = (ImageView) findViewById(R.id.gallow);
		if (gall_owns == SHOW) {
			AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.draw_gallow);
			gallowns_layout.setBackgroundDrawable(drawable);
			gallowns_layout.post(drawable);

		} else {
			// do nothing
		}
		gallowns_layout.invalidate();
	}

	private void drawHead() {
		AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.draw_head);
		ImageView im = (ImageView) findViewById(R.id.gallow);
		im.setBackgroundDrawable(drawable);
		im.post(drawable);
		im.invalidate();
	}

	private void drawTorso() {
		AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.draw_torso);
		ImageView im = (ImageView) findViewById(R.id.gallow);
		im.setBackgroundDrawable(drawable);
		im.post(drawable);
	}

	private void drawRightArm() {
		AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.draw_right_arm);
		ImageView im = (ImageView) findViewById(R.id.gallow);
		im.setBackgroundDrawable(drawable);
		im.post(drawable);
	}

	private void drawLeftArm() {
		AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.draw_left_arm);
		ImageView im = (ImageView) findViewById(R.id.gallow);
		im.setBackgroundDrawable(drawable);
		im.post(drawable);
	}

	private void drawRightLeg() {
		AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.draw_right_leg);
		ImageView im = (ImageView) findViewById(R.id.gallow);
		im.setBackgroundDrawable(drawable);
		im.post(drawable);
		im.invalidate();
	}

	private void drawLeftLeg() {
		AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.draw_left_leg);
		ImageView im = (ImageView) findViewById(R.id.gallow);
		im.setBackgroundDrawable(drawable);
		im.post(drawable);
	}

	public void onGameFnished() {
		scoreCounter();
		updateHighScore();
		if (mGameCompleted == GAME_WON) {
			showDialog(DIALOG_GAME_FINISHED);
			SoundEffect.playSound_win();

		} else if (mGameCompleted == GAME_OVER) {
			showDialog(DIALOG_GAME_FINISHED);
			SoundEffect.playSound_lose();
		} else {
			// do nothing
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DIALOG_GAME_FINISHED:
			gameFinishedDialog = new Dialog(this, R.style.Theme_GameDialog) {
				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
						return true;
					}
					return super.onKeyDown(keyCode, event);
				}
			};
			gameFinishedDialog.setContentView(R.layout.dialog_layout);
			gameFinishedDialog.setCancelable(false);
			txt_mess_dialog = (TextView) gameFinishedDialog.findViewById(R.id.txtresult);
			textSizeDialog = txt_mess_dialog.getTextSize();
			txt_word_dialog = (TextView) gameFinishedDialog.findViewById(R.id.txt_word);
			imgresult = (ImageView) gameFinishedDialog.findViewById(R.id.imgresult);
			txt_mess_dialog.setTypeface(mFont);
			txt_mess_dialog.setTextColor(mFontDefaultColor);
			txt_word_dialog.setTypeface(mFont);
			txt_word_dialog.setTextColor(mFontFoundWordColor);
			gameFinishButtonOK = (ImageButton) gameFinishedDialog.findViewById(R.id.gamecontinue);
			gameFinishButtonOK.setOnClickListener(onGameFinishedDialogOk);
			return gameFinishedDialog;
		case DIALOG_CONFIRMATION:
			confirmationDialog = new Dialog(this, R.style.Theme_CustomDialog) {
				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
						return true;
					}
					return super.onKeyDown(keyCode, event);
				}
			};
			confirmationDialog.setContentView(R.layout.confirmation_dialog);
			confirmationDialog.setCancelable(false);
			btnGameOK = (Button) confirmationDialog.findViewById(R.id.gameok);
			btnGameCancel = (Button) confirmationDialog.findViewById(R.id.gamecancel);
			btnGameOK.setOnClickListener(onGameOk);
			btnGameCancel.setOnClickListener(onGameCancel);
			return confirmationDialog;
		case DIALOG_NEW_WORD:
			newWordDialog = new Dialog(this, R.style.Theme_CustomDialog) {
				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
						return true;
					}
					return super.onKeyDown(keyCode, event);
				}
			};
			newWordDialog.setContentView(R.layout.confirmation_dialog);
			newWordDialog.setCancelable(false);
			btnGameOK = (Button) newWordDialog.findViewById(R.id.gameok);
			btnGameCancel = (Button) newWordDialog.findViewById(R.id.gamecancel);
			btnGameOK.setOnClickListener(onNewWordOk);
			btnGameCancel.setOnClickListener(onNewWordCancel);
			return newWordDialog;
		default:
			break;
		}
		return null;
	}

	private Button.OnClickListener onNewWordOk = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (GameSetting._game_mode == ONE_PLAYER_MODE) {
				newWordDialog.dismiss();
				onNewGame();
			} else {
				newWordDialog.dismiss();
				Intent intentEnterWords = new Intent(GameBoardScreen.this, EnterWordToGuessScreen.class);
				intentEnterWords.putExtra(PLAYER1, txt_player1.getText());
				intentEnterWords.putExtra(PLAYER2, txt_player2.getText());
				intentEnterWords.putExtra(PLAYER1_CHALLENGE, player1Challenge);
				startActivity(intentEnterWords);
				finish();
			}
		}
	};
	//
	private Button.OnClickListener onNewWordCancel = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			newWordDialog.dismiss();
		}
	};
	private Button.OnClickListener onGameOk = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			confirmationDialog.dismiss();
			Intent intentPlaySetting = null;
			if (GameSetting._game_mode == ONE_PLAYER_MODE) {
				intentPlaySetting = new Intent(GameBoardScreen.this, PlayerSettingsScreen.class);
			} else if (GameSetting._game_mode == TWO_PLAYER_MODE) {
				intentPlaySetting = new Intent(GameBoardScreen.this, TwoPlayerSettingScreen.class);
			}
			intentPlaySetting.putExtra(GAME_MODE, GameSetting._game_mode);
			startActivity(intentPlaySetting);
			finish();
		}
	};
	private Button.OnClickListener onGameCancel = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			confirmationDialog.dismiss();
		}
	};

	@Override
	public void onBackPressed() {
		showDialog(DIALOG_CONFIRMATION);
	}

	public boolean checkTextLong(String text) {
		return text.length() > 10;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		super.onPrepareDialog(id, dialog);
		TextView txt_info;
		TextView txt_new_word;
		switch (id) {
		case DIALOG_GAME_FINISHED:
			if (checkTextLong(strPlayer1Name.toString())) {
				txt_mess_dialog.setTextSize((float) (textSizeDialog * 0.5));
				Log.d("textSizeDialog", "xxx = " + (textSizeDialog / 2));
				txt_mess_dialog.setSingleLine();
				txt_mess_dialog.setPadding(4, 0, 4, 0);
			}
			if (mGameCompleted == GAME_WON) {
				if (GameSetting._game_mode == ONE_PLAYER_MODE) {
					txt_mess_dialog.setText(strPlayer1Name + " WINS");
				} else {
					if (player1Challenge) {
						txt_mess_dialog.setText(txt_player2.getText() + " WINS");
					} else {
						txt_mess_dialog.setText(txt_player1.getText() + " WINS");
					}
				}
				txt_word_dialog.setText("");
				imgresult.setBackgroundResource(R.drawable.won_game);
			} else if (mGameCompleted == GAME_OVER) {
				if (GameSetting._game_mode == ONE_PLAYER_MODE) {
					txt_mess_dialog.setText(strPlayer1Name + " LOSES");
				} else {
					if (player1Challenge) {
						txt_mess_dialog.setText(txt_player2.getText() + " LOSES");
					} else {
						txt_mess_dialog.setText(txt_player1.getText() + " LOSES");
					}
				}
				txt_word_dialog.setText("WORD: " + mChallenge);
				Log.e("word_dialog", mChallenge.toString());
				imgresult.setBackgroundResource(R.drawable.loose_game);
			} else {
				// do nothing
			}
			break;
		case DIALOG_CONFIRMATION:
			txt_info = (TextView) confirmationDialog.findViewById(R.id.txtinfo);
			txt_new_word = (TextView) confirmationDialog.findViewById(R.id.txt_new_word);
			txt_info.setVisibility(View.VISIBLE);
			txt_new_word.setVisibility(View.GONE);
			break;
		case DIALOG_NEW_WORD:
			txt_info = (TextView) newWordDialog.findViewById(R.id.txtinfo);
			txt_new_word = (TextView) newWordDialog.findViewById(R.id.txt_new_word);
			txt_info.setVisibility(View.GONE);
			txt_new_word.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private Button.OnClickListener onGameFinishedDialogOk = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (GameSetting._game_mode == ONE_PLAYER_MODE) {
				dismissDialog(DIALOG_GAME_FINISHED);
				onNewGame();
				Log.e("tag", "button dialog OK is clicked");
			} else {
				gameFinishedDialog.dismiss();
				Intent intentEnterWords = new Intent(GameBoardScreen.this, EnterWordToGuessScreen.class);
				intentEnterWords.putExtra(PLAYER1, txt_player1.getText());
				intentEnterWords.putExtra(PLAYER2, txt_player2.getText());
				intentEnterWords.putExtra(PLAYER1_CHALLENGE, !player1Challenge);
				startActivity(intentEnterWords);
			}
		}
	};

	public String stringToUper(String strLower) {
		String str;
		char[] stringArray = strLower.toCharArray();
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = Character.toUpperCase(stringArray[i]);
		}
		str = new String(stringArray);
		return str;

	}

	private void showAdvBanner() {
		adView = new AdView(this, AdSize.BANNER, getString(R.string.ADMOB_PUBLISHER_ID));
		LinearLayout layout = (LinearLayout) findViewById(R.id.adv_layout);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR); // Emulator
		adView.loadAd(adRequest);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	private final class GameTimer extends Timer {

		GameTimer() {
			super(1000);
		}

		@Override
		protected boolean step(int count, long time) {
			if (advShowInterval == 1 * 30 * 1000) {
				showAdvBanner();
				advShowInterval = 0;
			} else {
				advShowInterval += 1000;
			}
			return false;
		}

	}

	@Override
	protected void onPause() {
		mGameTimer.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mGameTimer.start();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		finish();
		super.onDestroy();
	}

	public void updateHighScore() {
		if (GameSetting._game_mode == ONE_PLAYER_MODE) {
			// TODO
		} else {
			txt_player1_score.setText(String.valueOf(GameSetting._two_mode_player1_hscore));
			txt_player2_score.setText(String.valueOf(GameSetting._two_mode_player2_hscore));
		}
	}

	public void scoreCounter() {
		if (GameSetting._game_mode == ONE_PLAYER_MODE) {
			if (mGameCompleted == GAME_WON) {
				GameSetting._one_mode_player1_hscore += 100;
			} else if (mGameCompleted == GAME_OVER) {
				GameSetting._one_mode_player2_hscore += 100;
			} else {
				// do nothing
			}
		} else {
			if (mGameCompleted == GAME_WON) {
				if (player1Challenge) {
					GameSetting._two_mode_player2_hscore += 100;
				} else {
					GameSetting._two_mode_player1_hscore += 100;
				}
			} else if (mGameCompleted == GAME_OVER) {
				if (player1Challenge) {
					GameSetting._two_mode_player1_hscore += 100;
				} else {
					GameSetting._two_mode_player2_hscore += 100;
				}
			} else {
				// do nothing
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			finish();
		}
	}

	/**
	 * 
	 */
	public void onImageSearch() {
		Toast.makeText(this, "Searching for " + mChallenge, Toast.LENGTH_SHORT).show();

		// asyn load the data from Google API
		AsyncHttpClient client = new AsyncHttpClient();

		// Google Image Search
		// @see : https://developers.google.com/image-search/v1/jsondevguide
		// https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android
		// TODO: Handle no data connection
		// TODO: Handle timeout
		// TOOD: Handle empty results
		// TODO: use YQL?
		String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=3" + "&start=" + 0 + "&v=1.0" + "&q=" + Uri.encode(mChallenge) + "&as_sitesearch=" + Uri.encode("google.com") + "&imgcolor=" + Uri.encode("black") + "&imgtype=" + Uri.encode("photo") + "&imgsz="
				+ Uri.encode("medium");

		Log.d("DEBUG", "URL " + url);
		client.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;

				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					ArrayList<ImageResult> imgResults = ImageResult.fromJSONArray(imageJsonResults);
					if (imgResults != null && imgResults.size() > 0) {
						imageResults.clear();
						imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					}

					Log.d("DEBUG", imgResults.toString());
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				Log.d("DEBUG", arg1.toString());
			}
		}

		);
	}
}