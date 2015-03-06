package com.visva.voicerecorder.view.fragments;

import java.util.ArrayList;
import java.util.Comparator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.adapter.DetailFavouriteAdapter;
import com.visva.voicerecorder.view.adapter.FavouriteAdapter;
import com.visva.voicerecorder.view.widget.HorizontalListView;

public class FragmentFavourite extends FragmentBasic {
    private HorizontalListView          mFavouriteList;
    private ListView                    mDetailFavouriteList;
    private FavouriteAdapter            mFavouriteAdapter;
    private DetailFavouriteAdapter      mDetailFavouriteAdapter;
    private TextView                    mTextTitleFavourite;
    private ArrayList<FavouriteItem>    mFavouriteItems             = new ArrayList<FavouriteItem>();
    private ArrayList<RecordingSession> mRecordingSessions          = new ArrayList<RecordingSession>();
    private ArrayList<RecordingSession> mFavouriteRecordingSessions = new ArrayList<RecordingSession>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_favourite, container);

        initData();

        initLayout(root);
        return root;
    }

    private void initData() {
        if (mSQLiteHelper == null) {
            mSQLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(getActivity());
        }
        mFavouriteItems = mSQLiteHelper.getAllFavouriteItem();
        if (mProgramHelper == null) {
            mProgramHelper = MyCallRecorderApplication.getInstance().getProgramHelper();
        }
        mRecordingSessions = mProgramHelper.getRecordingSessionsFromFile(getActivity());
    }

    private void initLayout(View root) {
        mTextTitleFavourite = (TextView) root.findViewById(R.id.text_title_favourite);
        mFavouriteList = (HorizontalListView) root.findViewById(R.id.favourite_list);
        mFavouriteAdapter = new FavouriteAdapter(getActivity(), mFavouriteItems);
        mFavouriteList.setAdapter(mFavouriteAdapter);

        mDetailFavouriteList = (ListView) root.findViewById(R.id.list_detail_favourite);

        if (mFavouriteItems.size() == 0) {
            mFavouriteList.setVisibility(View.GONE);
            mDetailFavouriteList.setVisibility(View.GONE);
            mTextTitleFavourite.setVisibility(View.VISIBLE);
            return;
        } else {
            mFavouriteList.setVisibility(View.VISIBLE);
            mDetailFavouriteList.setVisibility(View.VISIBLE);
            mTextTitleFavourite.setVisibility(View.GONE);
        }

        mFavouriteRecordingSessions = getFavouriteFromList();
        Log.d("KieuThang", "mFavouriteRecordingSessions:" + mFavouriteRecordingSessions);
        mDetailFavouriteAdapter = new DetailFavouriteAdapter(getActivity(), mFavouriteRecordingSessions);
        mDetailFavouriteList.setAdapter(mDetailFavouriteAdapter);
    }

    private ArrayList<RecordingSession> getFavouriteFromList() {
        ArrayList<RecordingSession> list = new ArrayList<RecordingSession>();
        String phoneNo = mFavouriteItems.get(0).phoneNo;
        for (RecordingSession recordingSession : mRecordingSessions) {
            Log.d("KieuThang", "recordingSession.phoneNo:" + recordingSession.phoneNo + ",phoneNo:" + phoneNo);
            if (Utils.isSamePhoneNo(getActivity(), recordingSession.phoneNo, phoneNo)) {
                list.add(recordingSession);
            }
        }
        return list;
    }
}
