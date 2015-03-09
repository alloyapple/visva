package com.visva.voicerecorder.view.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.SQLiteHelper;
import com.visva.voicerecorder.view.activity.ActivityHome;

public class FragmentBasic extends Fragment {
    private boolean backStatus = false;
    protected Object mData = null;
    protected IHomeActionListener iHomeActionListener;
    private boolean dataChange = false;
    public int mNumLoading = 0;
    protected SQLiteHelper mSQLiteHelper;
    protected ProgramHelper mProgramHelper;

    public FragmentBasic() {
        iHomeActionListener = (ActivityHome) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSQLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(getActivity());
        mProgramHelper = MyCallRecorderApplication.getInstance().getProgramHelper();
        iHomeActionListener = (ActivityHome) getActivity();
    }

    public void refreshUI() {

    }

    public interface OnFragmentBasic {
        public void fragmentFinish();
    }

    public void setObject(Object object) {
        this.mData = object;
        this.dataChange = true;
    }

    protected Object getObject() {
        dataChange = false;
        return mData;
    }

    protected boolean isDataChanged() {
        return dataChange;
    }

    protected void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    protected void onClickGoToFragment() {

    }

    public boolean isBackStatus() {
        return backStatus;
    }

    public void setBackStatus(boolean backStatus) {
        this.backStatus = backStatus;
    }
}
