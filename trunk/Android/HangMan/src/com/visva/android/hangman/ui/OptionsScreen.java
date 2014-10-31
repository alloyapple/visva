package com.visva.android.hangman.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;

public class OptionsScreen extends Activity implements GlobalDef {
    /** Called when the activity is first created. */
    private ImageButton btn_back;
    private TextView img_soundOn;
    private TextView img_soundOff;
    private TextView img_sound;
    private TextView img_share;
    private Button btn_share;
    private int soundEnabled = ON;
    private Typeface mFont;
    private int mFontDefaultColor;
    private int mFontPressedColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);
        initControl();
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        loadPref();
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent _mainMenuIntent = new Intent(OptionsScreen.this,
                        MainMenuScreen.class);
                startActivity(_mainMenuIntent);
                finish();
                finish();
            }
        });
        img_soundOn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                img_soundOn.setTextColor(mFontPressedColor);
                img_soundOff.setTextColor(mFontDefaultColor);
                GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, ON);
            }
        });
        img_soundOff.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                img_soundOn.setTextColor(mFontDefaultColor);
                img_soundOff.setTextColor(mFontPressedColor);
                GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, OFF);
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(OptionsScreen.this, "Shared", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * init controls
     */
    public void initControl() {
        mFont = Typeface.createFromAsset(getAssets(), "fonts/SHOWG.TTF");
        mFontDefaultColor = getResources().getColor(R.color.font_default_color);
        mFontPressedColor = getResources().getColor(R.color.font_pressed_color2);
        btn_back = (ImageButton) findViewById(R.id.back_options);
        img_soundOn = (TextView) findViewById(R.id.sound_on);
        btn_share = (Button) findViewById(R.id.btn_share_via);
        img_soundOn.setTypeface(mFont);
        img_soundOn.setTextColor(mFontDefaultColor);
        img_soundOff = (TextView) findViewById(R.id.sound_off);
        img_soundOff.setTypeface(mFont);
        img_soundOff.setTextColor(mFontDefaultColor);
        img_share = (TextView) findViewById(R.id.lbl_share_via);
        img_share.setTypeface(mFont);
        img_share.setTextColor(mFontDefaultColor);
        img_sound = (TextView) findViewById(R.id.lbl_sound);
        img_sound.setTypeface(mFont);
        img_sound.setTextColor(mFontDefaultColor);
    }

    /*
     * Load pref
     */
    public void loadPref() {
        soundEnabled = GamePreferences.getIntVal(this, SOUND_ON, ON);
        if (soundEnabled == ON) {
            img_soundOn.setTextColor(mFontPressedColor);
        } else {
            img_soundOff.setTextColor(mFontDefaultColor);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        Intent _mainMenuIntent = new Intent(OptionsScreen.this,
                MainMenuScreen.class);
        startActivity(_mainMenuIntent);
        finish();
        super.onBackPressed();
    }

}