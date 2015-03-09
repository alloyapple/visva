package com.visva.voicerecorder.view.recording;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.record.RecordingManager;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityPlayRecording;
import com.visva.voicerecorder.view.common.FragmentBasic;

public class FragmentAllRecord extends FragmentBasic implements OnMenuItemClickListener {
    // ======================Constant Define=====================

    // ======================Control Define =====================
    private SwipeMenuListView           mLvRecords;
    private RecordingAdapter            mRecordingAdapter;

    // =======================Class Define ======================
    private RecordingManager            mRecordingManager;
    private ProgramHelper               mProgramHelper;
    // ======================Variable Define=====================
    private ArrayList<RecordingSession> mSessions = new ArrayList<RecordingSession>();
    public View                         mLastClickedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_all_record, container);

        mProgramHelper = MyCallRecorderApplication.getInstance().getProgramHelper();
        mSessions = mProgramHelper.getRecordingSessionsFromFile(getActivity());
        mRecordingManager = MyCallRecorderApplication.getInstance().getRecordManager(getActivity(), mSessions);
        initLayout(root);
        return root;
    }

    private void initLayout(View root) {
        mLvRecords = (SwipeMenuListView) root.findViewById(R.id.lv_all_recorder);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem callItem = new SwipeMenuItem(getActivity());
                callItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                callItem.setWidth(Utils.dp2px(getActivity(), 100));
                callItem.setIcon(R.drawable.ic_call_while);
                menu.addMenuItem(callItem);

                SwipeMenuItem messageItem = new SwipeMenuItem(getActivity());
                messageItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                messageItem.setWidth(Utils.dp2px(getActivity(), 100));
                messageItem.setIcon(R.drawable.ic_message_while);
                menu.addMenuItem(messageItem);

                SwipeMenuItem logItem = new SwipeMenuItem(getActivity());
                logItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                logItem.setWidth(Utils.dp2px(getActivity(), 100));
                logItem.setIcon(R.drawable.ic_note_while);
                menu.addMenuItem(logItem);
            }
        };

        mLvRecords.setMenuCreator(creator);
        mLvRecords.setOnMenuItemClickListener(this);

        AIOLog.d(MyCallRecorderConstant.TAG, "HomeActivity.recordingManager:" + mRecordingManager);
        mRecordingAdapter = new RecordingAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, mRecordingManager.showAll());
        mLvRecords.setAdapter(mRecordingAdapter);
        mLvRecords.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecordingSession session = mSessions.get(position);
                Intent intent = new Intent(getActivity(), ActivityPlayRecording.class);
                intent.putExtra("recording_session", session);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
        case 0:
            if (mSessions.size() > 0 && isTelephonyEnabled()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mSessions.get(position).phoneNo));
                startActivity(intent);
            }
            break;
        case 1:
            if (mSessions.size() > 0) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", mSessions.get(position).phoneNo);
                startActivity(smsIntent);
            }
            break;
        case 2:

            break;
        default:
            break;
        }
        return false;
    }

    private boolean isTelephonyEnabled() {
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    public void onAllRecordTabClick(View v) {

    }

    public void onContactTabClick(View v) {

    }

    public void onTextSearchChanged(CharSequence s) {
        mRecordingAdapter.onTextSearchChanged(s);
    }

}
