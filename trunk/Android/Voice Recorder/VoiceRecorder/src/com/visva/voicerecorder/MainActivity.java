package com.visva.voicerecorder;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.visva.voicerecorder.dialogs.HelpDialogFragment;
import com.visva.voicerecorder.dialogs.SettingDialogFragment;
import com.visva.voicerecorder.fragments.AllRecordingFragment;
import com.visva.voicerecorder.fragments.ContactFragment;
import com.visva.voicerecorder.fragments.SettingFragment;
import com.visva.voicerecorder.record.RecordingManager;
import com.visva.voicerecorder.record.RecordingSession;

public class MainActivity extends FragmentActivity {
    // ======================Constant Define===============
    private static final int NUMBER_OF_FRAGMENTS = 3;
    public static final int FRAGMENT_BASE_ID = 1000;
    public static final int FRAGMENT_CONTACT_ID = FRAGMENT_BASE_ID + 1;
    public static final int FRAGMENT_ALL_ID = FRAGMENT_CONTACT_ID + 1;
    public static final int FRAGMENT_FAVOURITE_ID = FRAGMENT_ALL_ID + 1;
    // ======================Class Define===================

    //=======================Control Define=================
    //=======================Variable Define================
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
    public Fragment currentFragment = null;
    public static String toDeleteFilePath = null;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mDrawerLinearLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mPlanetTitles;

    public MainActivity() {
        super();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.helper = new ProgramHelper();
        ProgramHelper.activity = this;
        this.helper.prepare();
    }

    public void updateMainActivity() {
        this.onRestart();
        Toast.makeText(this, "do not append to the list", Toast.LENGTH_LONG).show();
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
        MainActivity.recordingManager.setSessions(sessions);
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
        MainActivity.recordingManager.setSessions(sessions);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
        MainActivity.recordingManager.setSessions(sessions);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        ArrayList<RecordingSession> sessions = this.helper.getRecordingSessionsFromFile();
        MainActivity.recordingManager = new RecordingManager(this, sessions);
        
        this.overrideUI(savedInstanceState);
    }

    public void overrideUI(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Log.d("GHIAM", "active true");
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            public void onPageSelected(int idx) {
                currentFragment = mSectionsPagerAdapter.fragments[idx];
                if (currentFragment instanceof AllRecordingFragment && MainActivity.toDeleteFilePath != null) {
                    // update view
                    currentFragment.onStart();
                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mViewPager.setCurrentItem(1);
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.layout_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
                ) {
                    public void onDrawerClosed(View view) {
                        //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    public void onDrawerOpened(View drawerView) {
                        //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
        case R.id.menu_settings:
            SettingDialogFragment sdf = new SettingDialogFragment();
            sdf.show(getSupportFragmentManager(), "SETTING_DIALOG");
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public Fragment fragments[] = { null, null, null };

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            Bundle args = new Bundle();
            switch (i) {
            case 0:
                fragment = new ContactFragment();
                fragments[0] = fragment;
                break;
            case 1:
                fragment = new AllRecordingFragment();
                fragments[1] = fragment;
                break;
            case 2:
                fragment = new SettingFragment();
                fragments[2] = fragment;
                break;
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_FRAGMENTS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return getString(R.string.title_section0).toUpperCase(Locale.ENGLISH);
            case 1:
                return getString(R.string.title_section1).toUpperCase(Locale.ENGLISH);
            case 2:
                return getString(R.string.title_section2).toUpperCase(Locale.ENGLISH);
            }
            return null;
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerLinearLayout);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}