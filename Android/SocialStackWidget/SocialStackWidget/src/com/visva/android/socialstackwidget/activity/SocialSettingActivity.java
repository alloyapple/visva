package com.visva.android.socialstackwidget.activity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.visva.android.socialstackwidget.R;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.fragment.AboutFragment;
import com.visva.android.socialstackwidget.fragment.AccountSettingFragment;
import com.visva.android.socialstackwidget.fragment.RefreshItemsSettingFragment;

public class SocialSettingActivity extends FragmentActivity {
    public static final String TAG = GlobalContstant.PRE_TAG + "SocialSettingActivity";
    private static final String PENDING_ACTION_BUNDLE_KEY = "com.samsung.android.always.activity.AlwaysActivity:PendingAction";
    private static final int ACC_FRAGMENT_SETTING_ID = 0;
    private static final int ABOUT_FRAGMENT_SETTING_ID = 2;
    private static final int REFRESH_TIME_FRAGMENT_SETTING_ID = 1;
    private PendingAction mFacebookPendingAction = PendingAction.NONE;
    private UiLifecycleHelper mUiHelper;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private AccountSettingFragment mAccountSettingFragment;
    private AboutFragment mAboutFragment;
    private RefreshItemsSettingFragment mRefreshItemsSettingFragment;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        intiFacebook(savedInstanceState);
        initControl(savedInstanceState);
    }

    private void initControl(Bundle savedInstanceState) {
        String[] mListFuntions = getResources().getStringArray(R.array.function_list);

        mFragmentManager = getSupportFragmentManager();
        mAboutFragment = (AboutFragment) mFragmentManager.findFragmentById(R.id.about_setting_fragment);
        mAccountSettingFragment = (AccountSettingFragment) mFragmentManager.findFragmentById(R.id.acc_setting_fragment);
        mRefreshItemsSettingFragment = (RefreshItemsSettingFragment) mFragmentManager.findFragmentById(R.id.refresh_time_setting_fragment);
        showFragment(ACC_FRAGMENT_SETTING_ID);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mListFuntions));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
                ) {
                    public void onDrawerClosed(View view) {
                        getActionBar().setTitle(mTitle);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    public void onDrawerOpened(View drawerView) {
                        getActionBar().setTitle(mDrawerTitle);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void intiFacebook(Bundle savedInstanceState) {
        mUiHelper = new UiLifecycleHelper(this, callback);
        mUiHelper.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            mFacebookPendingAction = PendingAction.valueOf(name);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d(TAG, "onError()-" + String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d(TAG, "onComplete()-Success!");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mUiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, mFacebookPendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mUiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        Log.d(TAG, "onSessionStateChange");
        if (mFacebookPendingAction != PendingAction.NONE && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(SocialSettingActivity.this).setTitle(R.string.btn_cancel).setMessage(R.string.permission_not_granted).setPositiveButton(R.string.btn_ok, null).show();
            mFacebookPendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            Log.e(TAG, "OPEN_TOKEN_UPDATE");
        }
    }

    private void showFragment(int fragment) {
        mTransaction = hideFragment();
        switch (fragment) {
        case ACC_FRAGMENT_SETTING_ID:
            mTransaction.show(mAccountSettingFragment);
            break;
        case ABOUT_FRAGMENT_SETTING_ID:
            mTransaction.show(mAboutFragment);
            break;
        case REFRESH_TIME_FRAGMENT_SETTING_ID:
            mTransaction.show(mRefreshItemsSettingFragment);
            break;
        default:
            mTransaction.show(mAccountSettingFragment);
            break;
        }
        mTransaction.commit();
    }

    public FragmentTransaction hideFragment() {
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.hide(mAccountSettingFragment);
        mTransaction.hide(mAboutFragment);
        mTransaction.hide(mRefreshItemsSettingFragment);
        return mTransaction;
    }

    private void showToast(String string) {
        Toast.makeText(SocialSettingActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
//        mDrawerLayout.closeDrawer(position);
        showFragment(position);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
