package com.samsung.android.alwayssocial.activity;

import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.adapter.DrawerListAdapter;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.fragment.TwitterFragment;
import com.samsung.android.alwayssocial.fragment.TwitterFlipFragment;
import com.samsung.android.alwayssocial.object.DrawerSlideMenuItem;
import com.samsung.android.alwayssocial.service.IAlwaysService;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class TwitterMainActivity extends Activity implements IOnChangeFragment {

    private static final String TAG = "TwitterMainActivity";
    private FragmentTransaction mTransaction;
    private TwitterFragment mFragment;
    private TwitterFlipFragment mFlipFragment;
    private TextView mDetailFunctionTxt;

    // Slider menu control
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayList<DrawerSlideMenuItem> navDrawerItems;
    private DrawerListAdapter adapter;
    private TypedArray navMenuIcons;
    // slide menu items
    private String[] navMenuTitles;
    static boolean isOpen = false;
    private IAlwaysService mService = null;
    private int mViewMode = GlobalConstant.VIEW_TYPE_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_twitter_main_activity);
        // Load view type
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mViewMode = bundle.getInt(GlobalConstant.VIEW_MODE);
            }
        }
        //action bar
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_home);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        mDetailFunctionTxt = (TextView) findViewById(R.id.function_txt);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_right_slide_menu);
        navMenuIcons = getResources().obtainTypedArray(R.array.tw_function_icons);
        navMenuTitles = getResources().getStringArray(R.array.tw_fuction_items);

        navDrawerItems = new ArrayList<DrawerSlideMenuItem>();

        // adding nav drawer items to array
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
        navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));

        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new DrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.performItemClick(mDrawerList, 0, mDrawerList.getItemIdAtPosition(0));
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);

        ImageButton menuBtn = (ImageButton) findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOpen == false) {
                    mDrawerLayout.openDrawer(mDrawerList);
                    isOpen = true;
                } else {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    isOpen = false;
                }
            }
        });
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        DBManager.getInstance(this).filterDatabase(GlobalConstant.SOCIAL_TYPE_TWITTER);
    }

    private void showFragment(int type) {
        if (mViewMode == GlobalConstant.VIEW_TYPE_FLIP && (isTwitterTimelineType(type))) {
            // it is for debug flip animation
            mFlipFragment = new TwitterFlipFragment();
            mFlipFragment.setType(type);
            mTransaction = getFragmentManager().beginTransaction();
            mTransaction.replace(R.id.twitter_fragment, mFlipFragment);
            mTransaction.disallowAddToBackStack();
            mTransaction.commit();
        } else {
            mFragment = new TwitterFragment();
            mFragment.setType(type);
            mTransaction = getFragmentManager().beginTransaction();
            mTransaction.replace(R.id.twitter_fragment, mFragment);
            mTransaction.disallowAddToBackStack();
            mTransaction.commit();
        }
    }

    boolean isTwitterTimelineType(int type) {
        return (type == TwitterConstant.TW_DATA_TYPE_TIMELINE) || (type == TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS)
                || (type == TwitterConstant.TW_DATA_TYPE_TWEETS) || (type == TwitterConstant.TW_DATA_TYPE_FAVORITES)
                || (type == TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS);
    }

    private void displayView(int position) {
        mDrawerLayout.closeDrawer(mDrawerList);
        // update the main content by replacing fragments
        switch (position) {
        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
        case TwitterConstant.TW_DATA_TYPE_TWEETS:
        case TwitterConstant.TW_DATA_TYPE_FAVORITES:
        case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
        case TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES:
        case TwitterConstant.TW_DATA_TYPE_LIST:
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED:
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME:
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED:
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME:
            mDetailFunctionTxt.setText(navMenuTitles[position]);
            showFragment(position);
            break;
        case 11: // Log out case
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterConstant.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TwitterConstant.TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            Twitter twitter = factory.getInstance();
            twitter.setOAuthAccessToken(null);
            twitter.shutdown();
            AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putBooleanValue(TwitterConstant.PREF_KEY_TWITTER_LOGIN, false);
            AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(TwitterConstant.PREF_KEY_TWITTER_OAUTH_TOKEN);
            AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(TwitterConstant.PREF_KEY_TWITTER_OAUTH_SECRET);
            DBManager.getInstance(TwitterMainActivity.this).deleteAllStoryItem(GlobalConstant.SOCIAL_TYPE_TWITTER);
            Log.e(TAG, "count feed of twitter " + DBManager.getInstance(TwitterMainActivity.this).countFeedOfASocial(GlobalConstant.SOCIAL_TYPE_TWITTER));
            try {
                mService.logoutSocial();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            finish();

            finish();
            break;
        default:
            break;
        }

    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //        // toggle nav drawer on selecting action bar app icon/title
        //        if (mDrawerToggle.onOptionsItemSelected(item)) {
        //            return true;
        //        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.menu_btn:
            mDrawerLayout.openDrawer(mDrawerList);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestViewFinish(int fragmentType) {
        // TODO Auto-generated method stub
        showFragment(fragmentType);
    }

    @Override
    public void onRequestChangeTitle(String title) {
        // TODO Auto-generated method stub
        mDetailFunctionTxt.setText(title);
    }

}
