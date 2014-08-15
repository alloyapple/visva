package com.samsung.android.alwayssocial.fragment;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.view.PullAndLoadListView;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.facebook.SessionState;

public class FragmentBase extends Fragment {
    // ============================Control Define =====================
    protected PullAndLoadListView mListView;
    protected RelativeLayout mLinearProgressBar;
    protected View mLayoutEmpty;

    // ============================Class Define =======================
    protected UiLifecycleHelper mLifecycleHelper;
    protected static int mFragmentType = -1;

    public FragmentBase() {

    }

    public void setType(int type) {
        mFragmentType = type;
    }

    protected View initializeView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_list_type, null);
        mListView = (PullAndLoadListView) root.findViewById(R.id.always_fragment_list);
        mLinearProgressBar = (RelativeLayout) root.findViewById(R.id.layout_progressBar);
        mLayoutEmpty = root.findViewById(R.id.layout_empty);        
        mLinearProgressBar.setVisibility(View.VISIBLE);

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleHelper = new UiLifecycleHelper(getActivity(),
                new Session.StatusCallback() {
                    @Override
                    public void call(Session session, SessionState state,
                            Exception exception) {
                        onSessionStateChanged(session, state, exception);
                    }

                    private void onSessionStateChanged(Session session,
                            SessionState state, Exception exception) {
                        // TODO Auto-generated method stub
                    }
                });
        mLifecycleHelper.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshUI() {

    }

    public interface OnFragmentBasic {
        public void fragmentFinish();
    }

}
