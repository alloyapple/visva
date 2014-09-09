package com.sharebravo.bravo.view.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.sharebravo.bravo.sdk.util.VisvaDialog;

public class FragmentBasic extends Fragment {
    protected Object    mData      = null;
    private boolean     dataChange = false;
    private VisvaDialog progressDialog;
    public int          numLoading = 0;

    public FragmentBasic() {
        // TODO Auto-generated constructor stub
    }

    public void before() {
        // Show waiting dialog during connection

        if (numLoading == 0) {
            try {
                Log.d(getClass().toString(), "progress");
                progressDialog = new VisvaDialog(getActivity());
                progressDialog.show();
                progressDialog.setCancelable(false);
            } catch (Exception e) {

            }
        }

        numLoading++;
    }

    public void after() {
        // Process server response
        numLoading--;

        if (numLoading == 0) {
            if (progressDialog != null)
            {
                progressDialog.dismiss();
                progressDialog = null;
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
}
