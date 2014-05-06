package com.visva.android.hangman.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;


public class OptionsScreen extends Activity implements GlobalDef {
    /** Called when the activity is first created. */
	private ImageButton btn_back;
	private ImageView img_soundOn;
	private ImageView img_soundOff;
	private ImageView img_gallownsShow;
	private ImageView img_gallownsHide;
	private int soundEnabled = ON;
	private int gallownsShow = SHOW;
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
				// TODO Auto-generated method stub
				Intent _mainMenuIntent = new Intent(OptionsScreen.this,MainMenuScreen.class);
				startActivity(_mainMenuIntent);
				finish();
				finish();
			}
		});
        img_soundOn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img_soundOn.setImageResource(R.drawable.on_select);
				img_soundOff.setImageResource(R.drawable.off);
				GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, ON);
			}
		});
        img_soundOff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img_soundOn.setImageResource(R.drawable.on);
				img_soundOff.setImageResource(R.drawable.off_select);
				GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, OFF);
			}
		});
        img_gallownsShow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img_gallownsShow.setImageResource(R.drawable.show_select);
				img_gallownsHide.setImageResource(R.drawable.hide);
				GamePreferences.setIntVal(OptionsScreen.this,GALL_OWNS, SHOW);
			}
		});
        img_gallownsHide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img_gallownsShow.setImageResource(R.drawable.show);
				img_gallownsHide.setImageResource(R.drawable.hide_select);
				GamePreferences.setIntVal(OptionsScreen.this,GALL_OWNS, HIDE);
			}
		});
    }
    /*
     * init controls
     * */
    public void initControl(){
    	btn_back = (ImageButton)findViewById(R.id.back_options);
    	img_soundOn = (ImageView)findViewById(R.id.sound_on);
    	img_soundOff = (ImageView)findViewById(R.id.sound_off);
    	img_gallownsShow = (ImageView)findViewById(R.id.show_gallows);
    	img_gallownsHide = (ImageView)findViewById(R.id.hide_gallows);
    }
    /*
     * Load pref
     * */
    public void loadPref(){
    	soundEnabled = GamePreferences.getIntVal(this, SOUND_ON, ON);
    	gallownsShow = GamePreferences.getIntVal(this, GALL_OWNS, SHOW);
    	if(soundEnabled == ON){
    		img_soundOn.setImageResource(R.drawable.on_select);
    	}else{
    		img_soundOff.setImageResource(R.drawable.off_select);
    	}
    	if(gallownsShow == SHOW){
    		img_gallownsShow.setImageResource(R.drawable.show_select);
    	}else{
    		img_gallownsHide.setImageResource(R.drawable.hide_select);
    	}
    }
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent _mainMenuIntent = new Intent(OptionsScreen.this,MainMenuScreen.class);
		startActivity(_mainMenuIntent);
		finish();
		super.onBackPressed();
	}
    
}