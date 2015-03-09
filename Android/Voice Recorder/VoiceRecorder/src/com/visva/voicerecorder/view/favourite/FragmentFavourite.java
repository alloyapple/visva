package com.visva.voicerecorder.view.favourite;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.LayoutRipple;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;
import com.visva.voicerecorder.view.common.FragmentBasic;
import com.visva.voicerecorder.view.widget.HorizontalListView;

public class FragmentFavourite extends FragmentBasic {
    // ======================Constant Define=====================
    // ======================Control Define =====================
    private HorizontalListView          mFavouriteList;
    private ListView                    mDetailFavouriteList;
    private TextView                    mTextTitleFavourite;
    private TextView                    mTextRecord;
    private TextView                    mTextDetail;
    private TextView                    mTextPhoneNo;
    private RelativeLayout              mLayoutConversation;
    private RelativeLayout              mLayoutCallMsg;
    private LayoutRipple                mLayoutCall;
    private LayoutRipple                mLayoutMsg;
    // ======================Variable Define=====================
    private FavouriteAdapter            mFavouriteAdapter;
    private DetailFavouriteAdapter      mDetailFavouriteAdapter;
    private ArrayList<FavouriteItem>    mFavouriteItems             = new ArrayList<FavouriteItem>();
    private ArrayList<RecordingSession> mRecordingSessions          = new ArrayList<RecordingSession>();
    private ArrayList<RecordingSession> mFavouriteRecordingSessions = new ArrayList<RecordingSession>();
    private boolean                     mIsLongClickFavouriteItem   = false;

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
        Log.d("KieuThang", "iHomeActionListener:" + iHomeActionListener);
        mLayoutConversation = (RelativeLayout) root.findViewById(R.id.layout_conversation);
        mLayoutCallMsg = (RelativeLayout) root.findViewById(R.id.layout_call_msg);
        mTextTitleFavourite = (TextView) root.findViewById(R.id.text_title_favourite);
        mTextPhoneNo = (TextView) root.findViewById(R.id.text_phone_no);
        mTextRecord = (TextView) root.findViewById(R.id.text_record);
        mTextDetail = (TextView) root.findViewById(R.id.text_detail);
        mLayoutCall = (LayoutRipple) root.findViewById(R.id.layout_call);
        mLayoutMsg = (LayoutRipple) root.findViewById(R.id.layout_msg);
        mLayoutMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("KieuThang", "mLayoutMsg");
            }
        });
        mLayoutCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("KieuThang", "mLayoutCall");
            }
        });
        mTextDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("KieuThang", "mTextDetail");
                mLayoutCallMsg.setVisibility(View.VISIBLE);
                mLayoutConversation.setVisibility(View.GONE);
            }
        });
        mFavouriteList = (HorizontalListView) root.findViewById(R.id.favourite_list);
        mFavouriteAdapter = new FavouriteAdapter(getActivity(), mFavouriteItems);
        mFavouriteAdapter.setListener(iHomeActionListener);
        mFavouriteList.setAdapter(mFavouriteAdapter);
        mFavouriteList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iHomeActionListener.onClickItemListener(view, position, ActivityHome.FRAGMENT_FAVOURITE, 0);
            }
        });
        mFavouriteList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                iHomeActionListener.onLongClickItemListener(view, position, ActivityHome.FRAGMENT_FAVOURITE, 0);
                return false;
            }
        });

        mDetailFavouriteList = (ListView) root.findViewById(R.id.list_detail_favourite);
        if (mFavouriteItems.size() == 0) {
            mFavouriteList.setVisibility(View.GONE);
            mDetailFavouriteList.setVisibility(View.GONE);
            return;
        } else {
            mFavouriteList.setVisibility(View.VISIBLE);
            mDetailFavouriteList.setVisibility(View.VISIBLE);
        }

        mFavouriteRecordingSessions = getFavouriteFromList(0);
        mFavouriteList.setSelection(0);
        mTextPhoneNo.setText(mFavouriteItems.get(0).phoneNo + "");
        mDetailFavouriteAdapter = new DetailFavouriteAdapter(getActivity(), mFavouriteRecordingSessions);
        mDetailFavouriteList.setAdapter(mDetailFavouriteAdapter);
    }

    private void refreshListViewData(int position) {
        Log.d("KieuThang", "refreshListViewData:position:" + position + ",mFavouriteItems.get(position).phoneNo :"
                + mFavouriteItems.get(position).phoneNo);
        mFavouriteRecordingSessions = getFavouriteFromList(position);
        mFavouriteList.setSelection(position);
        mTextPhoneNo.setText(mFavouriteItems.get(position).phoneNo + "");
        mDetailFavouriteAdapter.updateDetailRecordingSession(mFavouriteRecordingSessions);

    }

    private ArrayList<RecordingSession> getFavouriteFromList(int position) {
        ArrayList<RecordingSession> list = new ArrayList<RecordingSession>();
        String phoneNo = mFavouriteItems.get(position).phoneNo;
        for (RecordingSession recordingSession : mRecordingSessions) {
            if (Utils.isSamePhoneNo(getActivity(), recordingSession.phoneNo, phoneNo)) {
                list.add(recordingSession);
            }
        }
        return list;
    }

    // action get click item listener from home activity,
    // listviewType is favourite and favourite detail list
    public void onClickItemListener(View view, int position, int listViewType) {
        switch (listViewType) {
        case 0:
            onClickFavouriteItemListener(view, position);
            break;

        default:
            break;
        }

    }

    // action get long click item listener from home activity,
    // listviewType is favourite and favourite detail list
    public void onLongClickItemListener(View view, int position, int listViewType) {
        switch (listViewType) {
        case 0:
            onLongClickItemListener(view, position);
            break;
        default:
            break;
        }
    }

    /**
     * this method handler action long item click at favourite list view
     * 
     * @param view
     * @param position
     */
    private void onLongClickItemListener(View view, int position) {
        Log.d("KieuThang", "onLongClickItemListener:" + position);
        mIsLongClickFavouriteItem = true;
    }

    /**
     * this method handler action item click at favourite list view
     * 
     * @param view
     * @param position
     */
    private void onClickFavouriteItemListener(View view, int position) {
        Log.d("KieuThang", "onClickFavouriteItemListener:" + position);
        if (mIsLongClickFavouriteItem) {
            mIsLongClickFavouriteItem = false;
            return;
        }
        refreshListViewData(position);
    }

    public void onClickTextRecord(View v) {
        Log.d("KieuThang", "mTextRecord");
        mLayoutCallMsg.setVisibility(View.GONE);
        mLayoutConversation.setVisibility(View.VISIBLE);
    }
    
    public void onClickTextDetail(View v){
        Log.d("KieuThang", "mTextDetail");
        mLayoutCallMsg.setVisibility(View.VISIBLE);
        mLayoutConversation.setVisibility(View.GONE);
    }
}
