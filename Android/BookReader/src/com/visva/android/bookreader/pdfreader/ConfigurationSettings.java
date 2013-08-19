package com.visva.android.bookreader.pdfreader;

import com.samsung.svmc.pdfreader.R;
import com.visva.android.bookreader.io.FileSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ConfigurationSettings extends Activity {

	private Intent mIntent;
	private int position;
	private final int DEFAULT_POSITION = -1;
	private FileSettings mFileSettings;
	private String newLineConfig = "";
	private int percentBrightness;
	private int levelBrightness;
	
	private LinearLayout[] linears;
	private EditText[] mEdtLower;
	private EditText[] mEdtUpper;
	private EditText[] mEdtLevel;
	private TextView mTxtTitleDialog;
	
	private SeekBar brightnessSeekBar;
	
	private ImageView imgSaveDialog;
	private ImageView imgCancelDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_activity);
		
		mTxtTitleDialog = (TextView) findViewById(R.id.title_dialog);
		brightnessSeekBar = (SeekBar) findViewById(R.id.brightness_seekbar);
		brightnessSeekBar.setMax(255 - 20);
		brightnessSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				levelBrightness = progress + 20;
				percentBrightness = Math.round((float) levelBrightness * 100 / 255);
				mEdtLevel[position].setText("" + percentBrightness + " %");
			}
		});
		
		// Declare array LinearLayout
		linears = new LinearLayout[9];
		linears[0] = (LinearLayout) findViewById(R.id.linear_mode0);
		linears[1] = (LinearLayout) findViewById(R.id.linear_mode1);
		linears[2] = (LinearLayout) findViewById(R.id.linear_mode2);
		linears[3] = (LinearLayout) findViewById(R.id.linear_mode3);
		linears[4] = (LinearLayout) findViewById(R.id.linear_mode4);
		linears[5] = (LinearLayout) findViewById(R.id.linear_mode5);
		linears[6] = (LinearLayout) findViewById(R.id.linear_mode6);
		linears[7] = (LinearLayout) findViewById(R.id.linear_mode7);
		linears[8] = (LinearLayout) findViewById(R.id.linear_mode8);
		
		// Declare array EditText
		mEdtLower = new EditText[9];
		mEdtLower[0] = (EditText) findViewById(R.id.lower_mode0);
		mEdtLower[1] = (EditText) findViewById(R.id.lower_mode1);
		mEdtLower[2] = (EditText) findViewById(R.id.lower_mode2);
		mEdtLower[3] = (EditText) findViewById(R.id.lower_mode3);
		mEdtLower[4] = (EditText) findViewById(R.id.lower_mode4);
		mEdtLower[5] = (EditText) findViewById(R.id.lower_mode5);
		mEdtLower[6] = (EditText) findViewById(R.id.lower_mode6);
		mEdtLower[7] = (EditText) findViewById(R.id.lower_mode7);
		mEdtLower[8] = (EditText) findViewById(R.id.lower_mode8);
		
		mEdtUpper = new EditText[9];
		mEdtUpper[0] = (EditText) findViewById(R.id.upper_mode0);
		mEdtUpper[1] = (EditText) findViewById(R.id.upper_mode1);
		mEdtUpper[2] = (EditText) findViewById(R.id.upper_mode2);
		mEdtUpper[3] = (EditText) findViewById(R.id.upper_mode3);
		mEdtUpper[4] = (EditText) findViewById(R.id.upper_mode4);
		mEdtUpper[5] = (EditText) findViewById(R.id.upper_mode5);
		mEdtUpper[6] = (EditText) findViewById(R.id.upper_mode6);
		mEdtUpper[7] = (EditText) findViewById(R.id.upper_mode7);
		mEdtUpper[8] = (EditText) findViewById(R.id.upper_mode8);
		
		mEdtLevel = new EditText[9];
		mEdtLevel[0] = (EditText) findViewById(R.id.level_mode0);
		mEdtLevel[1] = (EditText) findViewById(R.id.level_mode1);
		mEdtLevel[2] = (EditText) findViewById(R.id.level_mode2);
		mEdtLevel[3] = (EditText) findViewById(R.id.level_mode3);
		mEdtLevel[4] = (EditText) findViewById(R.id.level_mode4);
		mEdtLevel[5] = (EditText) findViewById(R.id.level_mode5);
		mEdtLevel[6] = (EditText) findViewById(R.id.level_mode6);
		mEdtLevel[7] = (EditText) findViewById(R.id.level_mode7);
		mEdtLevel[8] = (EditText) findViewById(R.id.level_mode8);
		
		mIntent = getIntent();
		position = mIntent.getIntExtra(BrightnessSettings.CONFIGURATION_DIALOG_CODE, DEFAULT_POSITION);
		mEdtLevel[position].setEnabled(false);
		
		// Fill Configuration Setting to EditText
		mFileSettings = new FileSettings(BrightnessSettings.FOLDER_PATH, BrightnessSettings.FILE_PATH);
		mFileSettings.readFileSetting(position + 1, mEdtLower[position], mEdtUpper[position], mEdtLevel[position]);
		
		int progress = Integer.parseInt(mEdtLevel[position].getText().toString());
		brightnessSeekBar.setProgress(progress - 20);
		
		int percentBrightness = Math.round((float) progress * 100 / 255);
		mEdtLevel[position].setText("" + percentBrightness + " %");
		
		switch (position) {
		case 0:
			linears[0].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light in the night");
			break;
			
		case 1:
			linears[1].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light in the office - Lowest");
			break;
			
		case 2:
			linears[2].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light in the office - Low");
			break;
			
		case 3:
			linears[3].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light in the office - Medium");
			break;
			
		case 4:
			linears[4].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light in the office - High");
			break;

		case 5:
			linears[5].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light under an overcast sky");
			break;
			
		case 6:
			linears[6].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light under a cloudy sky");
			break;
			
		case 7:
			linears[7].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light under a cloudy and sunny sky");
			break;
			
		case 8:
			linears[8].setVisibility(View.VISIBLE);
			mTxtTitleDialog.setText("The light of sunlight");
			break;

		default:
			break;
		}
		
		imgSaveDialog = (ImageView) findViewById(R.id.imgSaveDialog);
		imgSaveDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
//					newLineConfig = mEdtLower[0].getText() + " " + mEdtUpper[0].getText() + " " + mEdtLevel[0].getText();
					newLineConfig = mEdtLower[0].getText() + " " + mEdtUpper[0].getText() + " " + levelBrightness;
					break;
					
				case 1:
//					newLineConfig = mEdtLower[1].getText() + " " + mEdtUpper[1].getText() + " " + mEdtLevel[1].getText(); 
					newLineConfig = mEdtLower[1].getText() + " " + mEdtUpper[1].getText() + " " + levelBrightness;
					break;
					
				case 2:
//					newLineConfig = mEdtLower[2].getText() + " " + mEdtUpper[2].getText() + " " + mEdtLevel[2].getText();
					newLineConfig = mEdtLower[2].getText() + " " + mEdtUpper[2].getText() + " " + levelBrightness;
					break;
					
				case 3:
//					newLineConfig = mEdtLower[3].getText() + " " + mEdtUpper[3].getText() + " " + mEdtLevel[3].getText();
					newLineConfig = mEdtLower[3].getText() + " " + mEdtUpper[3].getText() + " " + levelBrightness;
					break;
					
				case 4:
//					newLineConfig = mEdtLower[4].getText() + " " + mEdtUpper[4].getText() + " " + mEdtLevel[4].getText();
					newLineConfig = mEdtLower[4].getText() + " " + mEdtUpper[4].getText() + " " + levelBrightness;
					break;

				case 5:
//					newLineConfig = mEdtLower[5].getText() + " " + mEdtUpper[5].getText() + " " + mEdtLevel[5].getText();
					newLineConfig = mEdtLower[5].getText() + " " + mEdtUpper[5].getText() + " " + levelBrightness;
					break;
					
				case 6:
//					newLineConfig = mEdtLower[6].getText() + " " + mEdtUpper[6].getText() + " " + mEdtLevel[6].getText(); 
					newLineConfig = mEdtLower[6].getText() + " " + mEdtUpper[6].getText() + " " + levelBrightness;
					break;
					
				case 7:
//					newLineConfig = mEdtLower[7].getText() + " " + mEdtUpper[7].getText() + " " + mEdtLevel[7].getText();
					newLineConfig = mEdtLower[7].getText() + " " + mEdtUpper[7].getText() + " " + levelBrightness;
					break;
					
				case 8:
//					newLineConfig = mEdtLower[8].getText() + " " + mEdtUpper[8].getText() + " " + mEdtLevel[8].getText(); 
					newLineConfig = mEdtLower[8].getText() + " " + mEdtUpper[8].getText() + " " + levelBrightness;
					break;

				default:
					break;
				}
				
				mFileSettings.writeNewConfigToFileSettings(newLineConfig, position+1);
				finish();
			}
		});
		
		imgCancelDialog = (ImageView) findViewById(R.id.imgCancelDialog);
		imgCancelDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
}
