package com.visva.voicerecorder;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public abstract class VisvaAbstractFragmentActivity extends FragmentActivity {
    private LinearLayout container;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Class strictModeClass = Class.forName("android.os.StrictMode");
            Class strictModeThreadPolicyClass = Class.forName("android.os.StrictMode$ThreadPolicy");
            Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(null);
            Method method_setThreadPolicy = strictModeClass.getMethod("setThreadPolicy", strictModeThreadPolicyClass);
            method_setThreadPolicy.invoke(null, laxPolicy);
        } catch (Exception e) {

        }
        setContentView(R.layout.abstract_activity);
        init();
        onCreate();
    }

    private void init() {
        container = (LinearLayout) findViewById(R.id.container);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(contentView(), container, false);

        container.addView(view, -1, -1);
    }

    public void changeToActivity(Intent intent, boolean isFinish) {
        startActivity(intent);
        if (isFinish) {
            finish();
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void goBack() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public abstract int contentView();

    public abstract void onCreate();

    protected void hideKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Go to other activity
     * 
     * @param context
     * @param cla
     */
    public void gotoActivity(Context context, Class<?> cla) {
        Intent intent = new Intent(context, cla);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void gotoActivityBySlideUp(Context context, Class<?> cla) {
        Intent intent = new Intent(context, cla);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public void gotoActivity(Context context, Class<?> cla, int flag) {
        Intent intent = new Intent(context, cla);
        intent.setFlags(flag);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Go to other activity
     * 
     * @param context
     * @param cla
     */
    public void gotoActivityForResult(Context context, Class<?> cla,
            int requestCode) {
        Intent intent = new Intent(context, cla);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Goto activity with bundle
     * 
     * @param context
     * @param cla
     * @param bundle
     */
    public void gotoActivity(Context context, Class<?> cla, Bundle bundle) {
        Intent intent = new Intent(context, cla);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Goto activity with bundle
     * 
     * @param context
     * @param cla
     * @param bundle
     * @param requestCode
     */
    public void gotoActivityForResult(Context context, Class<?> cla,
            Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, cla);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    // ======================= TOAST MANAGER =======================

    /**
     * @param str
     *            : alert message
     * 
     *            Show toast message
     */
    public void showToastMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param strId
     *            : alert message
     * 
     *            Show toast message
     */
    public void showToastMessage(int strId) {
        Toast.makeText(this, getString(strId), Toast.LENGTH_LONG).show();
    }

    /**
     * @param str
     *            : alert message
     * 
     *            Show toast message
     */
    public void showShortToastMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param str
     *            : alert message
     * 
     *            Show toast message
     */
    public void showToastMessage(String str, int time) {
        Toast.makeText(this, str, time).show();
    }

    /**
     * @param str
     *            : alert message
     * 
     *            Show toast message
     */
    public void showToastMessage(int resId, int time) {
        Toast.makeText(this, resId, time).show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

}
