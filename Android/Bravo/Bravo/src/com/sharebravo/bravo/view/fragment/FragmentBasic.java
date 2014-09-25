package com.sharebravo.bravo.view.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.sdk.util.VisvaDialog;

public class FragmentBasic extends Fragment implements IUISync {
    private boolean              backStatus  = false;
    protected Object             mData       = null;
    protected HomeActionListener mHomeActionListener;
    private boolean              dataChange  = false;
    private VisvaDialog          mProgressDialog;
    public int                   mNumLoading = 0;

    public FragmentBasic() {
        mHomeActionListener = (HomeActivity) getActivity();
    }

    public void before() {
        // Show waiting dialog during connection
        if (mNumLoading == 0) {
            try {
                Log.d(getClass().toString(), "progress");
                mProgressDialog = new VisvaDialog(getActivity());
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
            } catch (Exception e) {

            }
        }

        mNumLoading++;
    }

    public void after() {
        // Process server response
        mNumLoading--;

        if (mNumLoading == 0) {
            if (mProgressDialog != null)
            {
                try {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                } catch (Exception e) {

                }
            }
        }

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
