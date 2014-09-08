package com.sharebravo.bravo.view.fragment;

import com.sharebravo.bravo.sdk.util.network.AsyncUI;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class FragmentBasic extends Fragment {
    protected Object mData      = null;
    private boolean  dataChange = false;
    public AsyncUI   asyncUI;
    public FragmentBasic() {
        // TODO Auto-generated constructor stub
        asyncUI = new AsyncUI(getActivity());
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
