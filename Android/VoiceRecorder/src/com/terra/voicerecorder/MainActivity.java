package com.terra.voicerecorder;

import java.util.ArrayList;

import com.terra.voicerecorder.dialogs.ActivationDialogFragment;
import com.terra.voicerecorder.dialogs.ActivationDialogFragment.ActivationDialogListener;
import com.terra.voicerecorder.dialogs.HelpDialogFragment;
import com.terra.voicerecorder.dialogs.SettingDialogFragment;
import com.terra.voicerecorder.fragment.AllRecordingFragment;
//import com.terra.voicerecorder.fragment.ArrangeByTimeRecordingFragment;
import com.terra.voicerecorder.fragment.SettingFragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActivationDialogListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;
    /**
     * recordingManger is the data-center of this application
     */
    public static RecordingManager recordingManager;
    public ProgramHelper helper;
    //TextView editText;
    TextView phoneText;
    TelephonyManager tm;
    public Fragment currentFragment = null;
    public static String toDeleteFilePath = null;
    
    public MainActivity(){
    	super();
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	this.helper = new ProgramHelper();
    	ProgramHelper.activity = this;
    	this.helper.prepare(); 
    }
    
    public void updateMainActivity(){
    	this.onRestart();
    	Toast.makeText(this, "do not append to the list", Toast.LENGTH_LONG).show();
    	Intent refresh = new Intent(this, MainActivity.class);
    	startActivity(refresh);
    	this.finish();
    }
    
    @Override
    public void onRestart(){
    	super.onRestart();
    	ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
    	MainActivity.recordingManager.setSessions(sessions);
    	//Log.d("GHIAM","RECORDER restart");
    }
    @Override
    public void onStart(){
    	super.onStart();
    	ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
    	MainActivity.recordingManager.setSessions(sessions);
    	//Log.d("GHIAM","RECORDER restart");
    }
    @Override
    public void onResume(){
    	super.onResume();
    	ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
    	MainActivity.recordingManager.setSessions(sessions);
    	//Log.d("GHIAM","RECORDER resume");
    }
    @Override
    public void onPause(){
    	super.onPause();
    	//Log.d("GHIAM","RECORDER pause");
    }
    @Override
    public void onStop(){
    	super.onStop();
    	//Log.d("GHIAM","RECORDER stop");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
    	MainActivity.recordingManager = new RecordingManager(this,sessions);
        //Log.d("GHIAM","before onCreate");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.overrideUI();
        //TODO: play beep_start and beep_end in raw file
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.beep_start);
        mPlayer.start();
        //Log.d("GHIAM","onCreate");
    }
    
    public void overrideUI(){
    	tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
    	Log.d("GHIAM","on override , active: "+helper.checkActivated(tm.getDeviceId())
    			+"; num of use: "+helper.getNumOfUsed(this));
    	if( helper.checkActivated(tm.getDeviceId()) 
    			|| helper.getNumOfUsed(this) < ProgramHelper.NUM_OF_USE ){
    		Log.d("GHIAM","active true");
    		setContentView(R.layout.activity_main);
            // Create the adapter that will return a fragment for each of the three primary sections
            // of the app.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				public void onPageSelected(int idx) {
					// TODO Auto-generated method stub
					currentFragment = mSectionsPagerAdapter.fragments[idx];
					if(currentFragment instanceof AllRecordingFragment && MainActivity.toDeleteFilePath != null){
						// update view
						currentFragment.onStart();
					}
				}
				
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
        }else{
        	Log.d("GHIAM","active false");
        	setContentView(R.layout.activation);
        	
        	phoneText = (EditText)findViewById(R.id.editTextPhone);
        	Button btnActivation = (Button)findViewById(R.id.buttonActivate);
            btnActivation.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				EditText editTextPart1 = (EditText)findViewById(R.id.editTextActivationCodePart1);
    	        	EditText editTextPart2 = (EditText)findViewById(R.id.editTextActivationCodePart2);
    	        	EditText editTextPart3 = (EditText)findViewById(R.id.editTextActivationCodePart3);
    	        	EditText editTextPart4 = (EditText)findViewById(R.id.editTextActivationCodePart4);
    				String activationCode = editTextPart1.getText().toString()+"-"
    						+editTextPart2.getText().toString()+"-"
    						+editTextPart3.getText().toString()+"-"
    						+editTextPart4.getText().toString();
    				String phoneNo = tm.getLine1Number();
    				String uuid = tm.getDeviceId();
    				if(phoneNo.equals("")){
    					phoneNo = phoneText.getText().toString();
    				}
    				editTextPart1.setText("");
    				editTextPart2.setText("");
    				editTextPart3.setText("");
    				editTextPart4.setText("");
    				phoneText.setText("");
    				if(helper.processActivation(uuid, activationCode, phoneNo)){
    					Toast.makeText(getApplication(), "Kích hoạt thành công!", Toast.LENGTH_LONG).show();
    					overrideUI();
    				}else{
    					Toast.makeText(getApplication(), "Kích hoạt không thành công!", Toast.LENGTH_LONG).show();
    				}
    			}
    			
    		});
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        if(helper.checkActivated(tm.getDeviceId())){
        	menu.getItem(0).setVisible(false);
        }
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_help:
                HelpDialogFragment hdf = new HelpDialogFragment();
                hdf.show(getSupportFragmentManager(), "HELP_DIALOG");
                return true;
            case R.id.menu_active:
            	ActivationDialogFragment adf = new ActivationDialogFragment();
                adf.show(getSupportFragmentManager(), "ACTIVATION_DIALOG");
                return true;
            case R.id.menu_settings:
            	SettingDialogFragment sdf = new SettingDialogFragment();
            	sdf.show(getSupportFragmentManager(), "SETTING_DIALOG");
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed(){
    	/*
    	if(this.currentFragment instanceof ArrangeByTimeRecordingFragment){
    		ArrangeByTimeRecordingFragment timeFragment = (ArrangeByTimeRecordingFragment)this.currentFragment;
    		timeFragment.handleBackButtonClick();
    	}else{
    		this.finish();
    	}*/
    }
    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	public Fragment fragments[] = {null,null};
    	
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	Fragment fragment = null;
        	Bundle args = new Bundle();
        	switch(i){
        		case 0:
        			fragment = new AllRecordingFragment();
        			fragments[0] = fragment;
        			break;
        		case 1:
        			//fragment = new ArrangeByTimeRecordingFragment();
        			fragment = new SettingFragment();
        			fragments[1] = fragment;
        			break;
        	}
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            	case 0: return getString(R.string.title_section0).toUpperCase();
                case 1: return getString(R.string.title_section1).toUpperCase();
            }
            return null;
        }
    }

	public void onActivationSuccess(DialogFragment dialog) {
		// TODO Auto-generated method stub
		overrideUI();
	}
}