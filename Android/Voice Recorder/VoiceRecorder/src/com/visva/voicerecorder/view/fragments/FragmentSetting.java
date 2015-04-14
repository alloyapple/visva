package com.visva.voicerecorder.view.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.views.Switch.OnCheckListener;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.utils.MyCallRecorderSharePrefs;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.common.FragmentBasic;

public class FragmentSetting extends FragmentBasic {
    //========================Control Define==============
    private Switch                   mSwitchAutoSavedRecordCall;
    private Switch                   mSwitchIncomingCalls;
    private Switch                   mSwitchOutGoingCalls;
    private Switch                   mSwitchNotification;
    private LayoutRipple             mLayoutTheme;
    private TextView                 mTextTheme;
    private LayoutRipple             mLayoutAboutUs;
    private TextView                 mTextVersionApp;
    /*theme*/
    private TextView                 mTextCallArrived;
    private TextView                 mTextSavedCalls;
    private TextView                 mTextTitleTheme;
    private TextView                 mTextNotification;
    private TextView                 mTextAbout;
    private View                     mDivider1;
    private View                     mDivider2;
    private View                     mDivider3;
    private View                     mDivider4;
    //========================Variable Define=============
    private MyCallRecorderSharePrefs mMyRecorderCallSharePrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_setting, container);

        initData();
        initLayout(root);
        return root;
    }

    private void initData() {
        if (mMyRecorderCallSharePrefs == null)
            mMyRecorderCallSharePrefs = MyCallRecorderApplication.getInstance().getMyCallRecorderSharePref(getActivity());
    }

    private void initLayout(View root) {
        //theme
        mTextTheme = (TextView) root.findViewById(R.id.text_theme);
        mTextCallArrived = (TextView) root.findViewById(R.id.txt_when_call_arrived);
        mTextNotification = (TextView) root.findViewById(R.id.txt_notification);
        mTextSavedCalls = (TextView) root.findViewById(R.id.saved_calls);
        mTextTitleTheme = (TextView) root.findViewById(R.id.txt_theme);
        mDivider1 = (View) root.findViewById(R.id.divider1);
        mDivider2 = (View) root.findViewById(R.id.divider2);
        mDivider3 = (View) root.findViewById(R.id.divider3);
        mDivider4 = (View) root.findViewById(R.id.divider4);
        mTextAbout = (TextView) root.findViewById(R.id.txt_about);

        mTextVersionApp = (TextView) root.findViewById(R.id.text_app_version);
        mLayoutAboutUs = (LayoutRipple) root.findViewById(R.id.layout_about_us);
        mSwitchAutoSavedRecordCall = (Switch) root.findViewById(R.id.switch_auto_saved_record_call);
        mSwitchIncomingCalls = (Switch) root.findViewById(R.id.switch_incoming_calls);
        mSwitchOutGoingCalls = (Switch) root.findViewById(R.id.switch_ongoing_calls);
        mSwitchNotification = (Switch) root.findViewById(R.id.switch_notification);
        mLayoutTheme = (LayoutRipple) root.findViewById(R.id.layout_themes);
        mSwitchAutoSavedRecordCall.setOncheckListener(new OnCheckListener() {

            @Override
            public void onCheck(boolean check) {
                mMyRecorderCallSharePrefs.putBooleanValue(MyCallRecorderConstant.KEY_AUTO_SAVED, check);
            }
        });
        mSwitchIncomingCalls.setOncheckListener(new OnCheckListener() {

            @Override
            public void onCheck(boolean check) {
                mMyRecorderCallSharePrefs.putBooleanValue(MyCallRecorderConstant.KEY_SAVED_INCOMING_CALL, check);
            }
        });
        mSwitchOutGoingCalls.setOncheckListener(new OnCheckListener() {

            @Override
            public void onCheck(boolean check) {
                mMyRecorderCallSharePrefs.putBooleanValue(MyCallRecorderConstant.KEY_SAVED_OUTGOING_CALL, check);
            }
        });
        mSwitchNotification.setOncheckListener(new OnCheckListener() {

            @Override
            public void onCheck(boolean check) {
                mMyRecorderCallSharePrefs.putBooleanValue(MyCallRecorderConstant.KEY_SHOW_NOTIFICATION, check);
            }
        });
        mLayoutTheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogChooseTheme();
            }

        });
        mLayoutAboutUs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Utils.getUrlAboutUs()));
                } catch (ActivityNotFoundException activityNotFoundException1) {
                    AIOLog.e(MyCallRecorderConstant.TAG, "Market Intent not found");
                }
            }
        });
        boolean isAutoSavedCallRecord = mMyRecorderCallSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_AUTO_SAVED);
        mSwitchAutoSavedRecordCall.setChecked(isAutoSavedCallRecord);
        boolean isSavedIncomingCall = mMyRecorderCallSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SAVED_INCOMING_CALL);
        mSwitchIncomingCalls.setChecked(isSavedIncomingCall);
        boolean isSavedOutGoingCall = mMyRecorderCallSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SAVED_OUTGOING_CALL);
        mSwitchOutGoingCalls.setChecked(isSavedOutGoingCall);
        boolean isShowNotification = mMyRecorderCallSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SHOW_NOTIFICATION);
        mSwitchNotification.setChecked(isShowNotification);

        String appVersion = getActivity().getResources().getString(R.string.about_app_version, Utils.getApplicationVersion(getActivity()));
        mTextVersionApp.setText(appVersion);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mMyRecorderCallSharePrefs == null)
                mMyRecorderCallSharePrefs = MyCallRecorderApplication.getInstance().getMyCallRecorderSharePref(getActivity());
        }
    }

    private void showDialogChooseTheme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_theme).setItems(R.array.Themes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mMyRecorderCallSharePrefs.putIntValue(MyCallRecorderConstant.KEY_THEME, which);
                MyCallRecorderApplication.getInstance().updateTheme(which);
            }
        });
        builder.show();
    }

    public void updateTheme(int themeColor, int pressThemeColor) {
        int themeType = mMyRecorderCallSharePrefs.getIntValue(MyCallRecorderConstant.KEY_THEME);
        switch (themeType) {
        case MyCallRecorderConstant.THEME_ORANGE:
            mTextTheme.setText(getActivity().getResources().getString(R.string.theme_orage));
            break;
        case MyCallRecorderConstant.THEME_RED:
            mTextTheme.setText(getActivity().getResources().getString(R.string.theme_red));
            break;
        case MyCallRecorderConstant.THEME_GREEN:
            mTextTheme.setText(getActivity().getResources().getString(R.string.theme_green));
            break;
        case MyCallRecorderConstant.THEME_BLUE:
            mTextTheme.setText(getActivity().getResources().getString(R.string.theme_blue));
            break;
        default:
            mTextTheme.setText(getActivity().getResources().getString(R.string.theme_orage));
            break;
        }
        mTextCallArrived.setTextColor(themeColor);
        mTextNotification.setTextColor(themeColor);
        mTextSavedCalls.setTextColor(themeColor);
        mTextAbout.setTextColor(themeColor);
        mTextTitleTheme.setTextColor(themeColor);
        mDivider1.setBackgroundColor(themeColor);
        mDivider2.setBackgroundColor(themeColor);
        mDivider3.setBackgroundColor(themeColor);
        mDivider4.setBackgroundColor(themeColor);
    }
}
