package com.samsung.android.alwayssocial.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.ProfilePictureView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.adapter.DrawerListAdapter;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.fragment.FacebookFeedFlipFragment;
import com.samsung.android.alwayssocial.fragment.FacebookFragment;
import com.samsung.android.alwayssocial.object.DrawerSlideMenuItem;
import com.samsung.android.alwayssocial.object.SocialUserObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;

public class FacebookMainActivity extends Activity implements IOnChangeFragment {
    public static final String TAG = "MainSocialActivity";
    //private ArrayList<String> mBackstack = new ArrayList<String>();
    private FragmentTransaction mTransaction;
    private FacebookFeedFlipFragment mFlipFragment;
    private FacebookFragment mFragment;
    private TextView mDetailFunctionTxt;
    private UiLifecycleHelper mUiHelper;

    // Slider menu control
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinear;
    // Profile info
    private RelativeLayout mMyProfileLayout;
    private ProfilePictureView mMyProfilePicture;
    private TextView mMyProfileName;

    // List function
    private ListView mDrawerListChild;
    private ArrayList<DrawerSlideMenuItem> navDrawerItems;
    private DrawerListAdapter adapter;
    private TypedArray navMenuIcons;
    // slide menu items
    private String[] navMenuTitles;
    static boolean isOpen = false;
    private IAlwaysService mService = null;
    private GraphUser mLoggedInUser;
    private int mViewMode = GlobalConstant.VIEW_TYPE_LIST;

    // **********************************************************************
    // resume,destroy activity
    // **********************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiHelper = new UiLifecycleHelper(this, callback);
        mUiHelper.onCreate(savedInstanceState);
        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        setContentView(R.layout.page_facebook_main_activity);
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

        mDetailFunctionTxt = (TextView) findViewById(R.id.function_txt);
        mDetailFunctionTxt.setSelected(true);
        /*
         * start implement DrawerLayout
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinear = (LinearLayout) findViewById(R.id.right_slide_menu);
        // Profile information
        mMyProfileLayout = (RelativeLayout) findViewById(R.id.right_slide_menu_profile);
        mMyProfilePicture = (ProfilePictureView) findViewById(R.id.profile_icon);
        mMyProfileName = (TextView) findViewById(R.id.profile_name);

        // List of function
        mDrawerListChild = (ListView) findViewById(R.id.right_slide_menu_list);
        navMenuIcons = getResources().obtainTypedArray(R.array.fb_function_icons);
        navMenuTitles = getResources().getStringArray(R.array.fb_fuction_items);

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
        //navDrawerItems.add(new DrawerSlideMenuItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));

        navMenuIcons.recycle();
        mDrawerListChild.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new DrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerListChild.setAdapter(adapter);
        // Select first function as default
        mDrawerListChild.performItemClick(mDrawerListChild, 0, mDrawerListChild.getItemIdAtPosition(0));
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);

        ImageButton menuBtn = (ImageButton) findViewById(R.id.menu_btn);

        menuBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOpen == false) {
                    mDrawerLayout.openDrawer(mDrawerLinear);
                    isOpen = true;
                } else {
                    mDrawerLayout.closeDrawer(mDrawerLinear);
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
        // Load information of facbook user
        SocialUserObject loggedInUser = AlwaysSocialAppImpl.getInstance().getLoggedInUser(GlobalConstant.SOCIAL_TYPE_FACEBOOK);
        if (loggedInUser != null) {
            Log.d(TAG, "Update User profile name = " + loggedInUser.mName);
            mMyProfileLayout.setVisibility(View.VISIBLE);
            mMyProfilePicture.setProfileId(loggedInUser.mId);
            mMyProfileName.setText(loggedInUser.mName);
        } else {
            mMyProfileLayout.setVisibility(View.GONE);
        }
        super.onResume();
    }

    private void showFragment(int type) {

        if (mViewMode == GlobalConstant.VIEW_TYPE_FLIP && isFacebookFeedType(type)) {
            // it is for debug flip animation
            mFlipFragment = new FacebookFeedFlipFragment();
            mFlipFragment.setType(type);
            mTransaction = getFragmentManager().beginTransaction();
            mTransaction.replace(R.id.facebook_fragment, mFlipFragment);
            mTransaction.disallowAddToBackStack();
            mTransaction.commit();
        } else {
            mFragment = new FacebookFragment();
            mFragment.setType(type);
            mTransaction = getFragmentManager().beginTransaction();
            mTransaction.replace(R.id.facebook_fragment, mFragment);
            mTransaction.disallowAddToBackStack();
            mTransaction.commit();
        }
    }

    private boolean isFacebookFeedType(int type) {
        return (type == FacebookConstant.FB_DATA_TYPE_FEED) || (type == FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS)
                || (type == FacebookConstant.FB_DATA_TYPE_FEED_LINKS) || (type == FacebookConstant.FB_DATA_TYPE_TIMELINE);
    }

    public void onClickBackPersonal(View v) {
        finish();
    }

    //    private void addToSBackStack(String tag) {
    //        int index = backstack.lastIndexOf(tag);
    //        if (index == -1) {
    //            backstack.add(tag);
    //            return;
    //        }
    //        try {
    //            if (!backstack.get(index - 1).equals(
    //                    backstack.get(backstack.size() - 1))) {
    //                backstack.add(tag);
    //                return;
    //            }
    //        } catch (IndexOutOfBoundsException e) {
    //
    //        }
    //        try {
    //            ArrayList<String> subStack = new ArrayList<String>(backstack);
    //            for (int i = 0; i < subStack.size(); i++) {
    //                if (i > index) {
    //                    backstack.remove(index);
    //                }
    //            }
    //        } catch (IndexOutOfBoundsException e) {
    //        }
    //    }

    private void logoutFB() {
        final Session openSession = Session.getActiveSession();
        if (openSession != null) {
            // If the Session is currently open, it must mean we need to
            // log out
            if (mLoggedInUser != null) {
                // Create a confirmation dialog
                String logout = getResources().getString(R.string.com_facebook_loginview_log_out_action);
                String cancel = getResources().getString(R.string.com_facebook_loginview_cancel_action);
                String message;
                if (mLoggedInUser != null && mLoggedInUser.getName() != null) {
                    message = String.format(getResources().getString(R.string.com_facebook_loginview_logged_in_as), mLoggedInUser.getName());
                } else {
                    message = getResources().getString(R.string.com_facebook_loginview_logged_in_using_facebook);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(logout,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        openSession.closeAndClearTokenInformation();
                                        /**delete all story item when user log out*/
                                        DBManager.getInstance(FacebookMainActivity.this).deleteAllStoryItem(GlobalConstant.SOCIAL_TYPE_FACEBOOK);
                                        finish();
                                    }
                                }).setNegativeButton(cancel, null);
                builder.create().show();
            } else {
                /**delete all story item when user log out*/
                DBManager.getInstance(FacebookMainActivity.this).deleteAllStoryItem(GlobalConstant.SOCIAL_TYPE_FACEBOOK);
                openSession.closeAndClearTokenInformation();
                try {
                    mService.logoutSocial();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                finish();
            }

            AlwaysSocialAppImpl.getInstance().setLoggedInUser(GlobalConstant.SOCIAL_TYPE_FACEBOOK, null);
        }
    }

    public void onChangeFragmnet(int type, String objectFeed) {
        showFragment(type);
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);

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
        try {
            mService.logoutSocial();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        DBManager.getInstance(this).filterDatabase(GlobalConstant.SOCIAL_TYPE_FACEBOOK);
    }

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

    private void displayView(int position) {
        // update the main content by replacing fragments
        mDrawerLayout.closeDrawer(mDrawerLinear);

        switch (position) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
        case FacebookConstant.FB_DATA_TYPE_GROUPS:
        case FacebookConstant.FB_DATA_TYPE_PAGES:
        case FacebookConstant.FB_DATA_TYPE_FRIENDS:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
        //case FacebookConstant.FB_DATA_TYPE_FRIEND_ALBUMS:
            mDetailFunctionTxt.setText(navMenuTitles[position]);
            showFragment(position);
            break;
        case 9: // Log out case
            logoutFB();
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
            mDrawerLayout.openDrawer(mDrawerLinear);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestChangeTitle(String title) {
        mDetailFunctionTxt.setText(title);
    }

    @Override
    public void onRequestViewFinish(int fragmentType) {
        showFragment(fragmentType);
    }

}
