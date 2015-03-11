package com.visva.voicerecorder.view.favourite;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.widgets.Dialog;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;
import com.visva.voicerecorder.view.activity.ActivityPlayRecording;
import com.visva.voicerecorder.view.common.FragmentBasic;
import com.visva.voicerecorder.view.widget.HorizontalListView;

public class FragmentFavourite extends FragmentBasic implements OnMenuItemClickListener {
    // ======================Constant Define=====================
    // ======================Control Define =====================
    private HorizontalListView          mFavouriteList;
    private SwipeMenuListView           mDetailFavouriteList;
    private TextView                    mTextPhoneNo;
    private RelativeLayout              mLayoutConversation;
    private RelativeLayout              mLayoutCallMsg;
    private TextView                    mTextDetail;
    private TextView                    mTextRecord;
    private LayoutRipple                mLayoutCall;
    private LayoutRipple                mLayoutMsg;
    // ======================Variable Define=====================
    private FavouriteAdapter            mFavouriteAdapter;
    private DetailFavouriteAdapter      mDetailFavouriteAdapter;
    private ArrayList<FavouriteItem>    mFavouriteItems                 = new ArrayList<FavouriteItem>();
    private ArrayList<RecordingSession> mRecordingSessions              = new ArrayList<RecordingSession>();
    private ArrayList<RecordingSession> mFavouriteRecordingSessions     = new ArrayList<RecordingSession>();
    private boolean                     mIsLongClickFavouriteItem       = false;
    private boolean                     mIsLongClickDetailFavouriteItem = false;
    private int                         mFavouritePosition;

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
        mTextPhoneNo = (TextView) root.findViewById(R.id.text_phone_no);
        mTextDetail = (TextView) root.findViewById(R.id.text_detail);
        mTextRecord = (TextView) root.findViewById(R.id.text_record);
        mLayoutCall = (LayoutRipple) root.findViewById(R.id.layout_call);
        mLayoutMsg = (LayoutRipple) root.findViewById(R.id.layout_msg);
        mLayoutCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickLayoutCall(v);
            }
        });
        mLayoutMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickLayoutMsg(v);
            }
        });
        mTextDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickTextDetail(v);
            }
        });
        mTextRecord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickTextRecord(v);
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

        mDetailFavouriteList = (SwipeMenuListView) root.findViewById(R.id.list_detail_favourite);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                deleteItem.setWidth(Utils.dp2px(getActivity(), 100));
                deleteItem.setIcon(R.drawable.delete_image);
                menu.addMenuItem(deleteItem);

                SwipeMenuItem shareItem = new SwipeMenuItem(getActivity());
                shareItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                shareItem.setWidth(Utils.dp2px(getActivity(), 100));
                shareItem.setIcon(R.drawable.ic_share);
                menu.addMenuItem(shareItem);
            }
        };

        mDetailFavouriteList.setMenuCreator(creator);
        mDetailFavouriteList.setOnMenuItemClickListener(this);

        mDetailFavouriteList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecordingSession session = mFavouriteRecordingSessions.get(position);
                Intent intent = new Intent(getActivity(), ActivityPlayRecording.class);
                intent.putExtra("recording_session", session);
                startActivity(intent);
            }
        });
        mDetailFavouriteList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
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
            onClickFavouriteItem(view, position);
            break;
        case 1:
            onClickDetailFavouriteItem(view, position);
            break;
        default:
            break;
        }
    }

    /**
     * this method handler action item click at favorite list view
     * 
     * @param view
     * @param position
     */
    private void onClickFavouriteItem(View view, int position) {
        Log.d("KieuThang", "onClickFavouriteItemListener:" + position);
        mFavouritePosition = position;
        if (mIsLongClickFavouriteItem) {
            mDetailFavouriteAdapter.setSelectedPosition(position);
        } else
            refreshListViewData(position);
    }

    private void onClickDetailFavouriteItem(View view, int position) {
        // TODO Auto-generated method stub

    }

    // action get long click item listener from home activity,
    // listviewType is favourite and favourite detail list
    public void onLongClickItemListener(View view, int position, int listViewType) {
        switch (listViewType) {
        case 0:
            onLongClickFavouriteItem(view, position);
            break;
        case 1: 
            onLongClickDetailFavouriteItem(view,position);
            break;
        default:
            break;
        }
    }

    private void onLongClickDetailFavouriteItem(View view, int position) {
        // TODO Auto-generated method stub
        
    }

    /**
     * this method handler action long item click at favourite list view
     * 
     * @param view
     * @param position
     */
    private void onLongClickFavouriteItem(View view, int position) {
        Log.d("KieuThang", "onLongClickItemListener:" + position);
        mIsLongClickFavouriteItem = true;
        mDetailFavouriteAdapter.setLongClickStateView(mIsLongClickFavouriteItem);
    }

    public void onClickTextRecord(View v) {
        Log.d("KieuThang", "mTextRecord");
        mLayoutCallMsg.setVisibility(View.GONE);
        mLayoutConversation.setVisibility(View.VISIBLE);
    }

    public void onClickTextDetail(View v) {
        Log.d("KieuThang", "mTextDetail");
        mLayoutCallMsg.setVisibility(View.VISIBLE);
        mLayoutConversation.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        Log.d("KieuThang", "onMenuItemClick:" + position);
        switch (index) {
        // when user click on delete action, dialog showed to confirm to delete recording file
        case 0:
            String deleteTitle = getActivity().getString(R.string.delete);
            String contentMsg = getActivity().getString(R.string.are_you_sure_to_delete_contact);
            String cancel = getActivity().getString(R.string.cancel);
            Dialog dialog = new Dialog(getActivity(), deleteTitle, contentMsg);
            dialog.addCancelButton(cancel, new OnClickListener() {

                @Override
                public void onClick(View v) {
                }
            });
            dialog.setOnAcceptButtonClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Delete:" + position, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
            break;
        case 1:
            shareRecordingSessionAction(mFavouriteRecordingSessions.get(position));
            break;
        case 2:
            break;
        default:
            break;
        }
        return false;
    }

    private void shareRecordingSessionAction(RecordingSession recordingSession) {
        if (recordingSession == null)
            return;
        File file = new File(recordingSession.fileName);
        if (!file.exists()) {
            AIOLog.e(MyCallRecorderConstant.TAG, "file not found");
        }
        Uri uri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("*/*");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public void onClickLayoutCall(View v) {
        Log.d("KieuThang", "mLayoutCall");
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mFavouriteItems.get(mFavouritePosition).phoneNo));
        startActivity(intent);
    }

    public void onClickLayoutMsg(View v) {
        Log.d("KieuThang", "onClickLayoutMsg");
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", mFavouriteItems.get(mFavouritePosition).phoneNo);
        startActivity(smsIntent);
    }
}
