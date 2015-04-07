package com.visva.voicerecorder.view.favourite;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.gc.materialdesign.widgets.Dialog;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityPlayRecording;
import com.visva.voicerecorder.view.common.FragmentBasic;
import com.visva.voicerecorder.view.widget.DotsTextView;

public class FragmentFavourite extends FragmentBasic implements OnMenuItemClickListener {
    // ======================Control Define =====================
    private FeatureCoverFlow            mFavouriteList;
    private SwipeMenuListView           mRecordingFavouriteList;
    private Button                      mBtnOptionMenu;
    private TextView                    mTextRecord;
    private TextSwitcher                mTextSwitcherPhoneName;
    private TextView                    mTextNoFavoriteFound;
    private TextView                    mTextNoRecordFound;
    private RelativeLayout              mLayoutFavorite;
    private DotsTextView                mDotsTextView;
    // ======================Variable Define=====================
    private FavouriteAdapter            mFavouriteAdapter;
    private DetailFavouriteAdapter      mRecordingFavouriteAdapter;
    private ArrayList<FavouriteItem>    mFavouriteItems             = new ArrayList<FavouriteItem>();
    private ArrayList<RecordingSession> mRecordingSessions          = new ArrayList<RecordingSession>();
    private ArrayList<RecordingSession> mFavouriteRecordingSessions = new ArrayList<RecordingSession>();
    private boolean                     mIsLongClickFavouriteItem   = false;
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
        if (mFavouriteItems == null || mFavouriteItems.size() == 0)
            return;
        if (mProgramHelper == null) {
            mProgramHelper = MyCallRecorderApplication.getInstance().getProgramHelper();
        }
        mRecordingSessions = mProgramHelper.getRecordingSessionsFromFile(getActivity());
    }

    private void initLayout(View root) {
        Log.d("KieuThang", "initLayout");
        mDotsTextView = (DotsTextView) root.findViewById(R.id.dots);
        mTextNoRecordFound = (TextView) root.findViewById(R.id.text_no_record_found);
        mLayoutFavorite = (RelativeLayout) root.findViewById(R.id.layout_favorite);
        mTextNoFavoriteFound = (TextView) root.findViewById(R.id.text_no_favorite_found);
        mTextSwitcherPhoneName = (TextSwitcher) root.findViewById(R.id.text_switch_favourite_name);
        //     
        mTextRecord = (TextView) root.findViewById(R.id.text_record_title);
        mBtnOptionMenu = (Button) root.findViewById(R.id.btn_favourite_menu);
        mFavouriteList = (FeatureCoverFlow) root.findViewById(R.id.coverflow);
        mFavouriteAdapter = new FavouriteAdapter(getActivity(), mFavouriteItems);
        mFavouriteList.setAdapter(mFavouriteAdapter);
        mFavouriteList.setReflectionBackgroundColor(0);
        mFavouriteList.setShouldRepeat(true);

        mBtnOptionMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickFavouriteMenu(v);
            }
        });
        mFavouriteList.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                if (mFavouritePosition == position && mFavouriteItems.size() > 1)
                    return;
                mFavouritePosition = position;
                mRecordingFavouriteList.setVisibility(View.GONE);
                mTextNoRecordFound.setVisibility(View.GONE);
                mDotsTextView.setVisibility(View.VISIBLE);
                mDotsTextView.start();

                AsyncUpdateRecordList asyncUpdateRecordList = new AsyncUpdateRecordList(getActivity(), position);
                asyncUpdateRecordList.execute();
            }

            @Override
            public void onScrolling() {
            }
        });

        mFavouriteList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = mFavouriteItems.get(position).phoneNo;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        mRecordingFavouriteList = (SwipeMenuListView) root.findViewById(R.id.list_detail_favourite);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                Resources res = getResources();
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(res.getColor(R.color.material_design_color_orange_action_delete)));
                deleteItem.setWidth(Utils.dp2px(getActivity(), 100));
                deleteItem.setIcon(R.drawable.btn_delete);
                menu.addMenuItem(deleteItem);

                SwipeMenuItem shareItem = new SwipeMenuItem(getActivity());
                shareItem.setBackground(new ColorDrawable(res.getColor(R.color.material_design_color_orange_action_normal)));
                shareItem.setWidth(Utils.dp2px(getActivity(), 100));
                shareItem.setIcon(R.drawable.btn_share);
                menu.addMenuItem(shareItem);
            }
        };

        mRecordingFavouriteList.setMenuCreator(creator);
        mRecordingFavouriteList.setOnMenuItemClickListener(this);
        mRecordingFavouriteList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecordingSession session = mFavouriteRecordingSessions.get(position);
                Intent intent = new Intent(getActivity(), ActivityPlayRecording.class);
                intent.putExtra("recording_session", session);
                startActivity(intent);
            }
        });
        mRecordingFavouriteList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        if (mFavouriteItems.size() == 0) {
            mLayoutFavorite.setVisibility(View.GONE);
            mTextNoFavoriteFound.setVisibility(View.VISIBLE);
        } else {
            mTextSwitcherPhoneName.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
                    return textView;
                }
            });
            Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
            Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
            mTextSwitcherPhoneName.setInAnimation(in);
            mTextSwitcherPhoneName.setOutAnimation(out);

            mLayoutFavorite.setVisibility(View.VISIBLE);
            mTextNoFavoriteFound.setVisibility(View.GONE);
            mRecordingFavouriteAdapter = new DetailFavouriteAdapter(getActivity(), mFavouriteRecordingSessions);
            mRecordingFavouriteList.setAdapter(mRecordingFavouriteAdapter);

//            AsyncUpdateRecordList asyncUpdateRecordList = new AsyncUpdateRecordList(getActivity(), 0);
//            asyncUpdateRecordList.execute();
        }
    }

    private ArrayList<RecordingSession> getFavouriteFromList(int position) {
        ArrayList<RecordingSession> list = new ArrayList<RecordingSession>();
        if (mFavouriteItems.size() == 0)
            return list;
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
    }

    private void onClickDetailFavouriteItem(View view, int position) {

    }

    // action get long click item listener from home activity,
    // listviewType is favourite and favourite detail list
    public void onLongClickItemListener(View view, int position, int listViewType) {
        switch (listViewType) {
        case 0:
            onLongClickFavouriteItem(view, position);
            break;
        case 1:
            onLongClickDetailFavouriteItem(view, position);
            break;
        default:
            break;
        }
    }

    private void onLongClickDetailFavouriteItem(View view, int position) {

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
        mRecordingFavouriteAdapter.setLongClickStateView(mIsLongClickFavouriteItem);
    }

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        Log.d("KieuThang", "onMenuItemClick:" + position);
        switch (index) {
        // when user click on delete action, dialog showed to confirm to delete recording file
        case 0:
            deleteRecordingSessionAction(position);
            break;
        case 1:
            RecordingSession recordingSession = mFavouriteRecordingSessions.get(position);
            if (recordingSession == null) {
                return false;
            }
            Utils.shareRecordingSessionAction(getActivity(), recordingSession.fileName);
            break;
        default:
            break;
        }
        return false;
    }

    private void deleteRecordingSessionAction(final int position) {
        final RecordingSession recordingSession = mFavouriteRecordingSessions.get(position);
        String title = recordingSession.phoneName;
        if (StringUtility.isEmpty(title)) {
            title = recordingSession.phoneNo;
        }
        String contentMsg = getActivity().getString(R.string.are_you_sure_to_delete_record);
        String cancel = getActivity().getString(R.string.cancel);
        Dialog dialog = new Dialog(getActivity(), title, contentMsg);
        dialog.addCancelButton(cancel, new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Delete:" + position, Toast.LENGTH_SHORT).show();
                Utils.deleteRecordingSesstionAction(getActivity(), recordingSession);
                mRecordingFavouriteAdapter.removeRecord(position);
            }
        });
        dialog.show();
    }

    public void onClickLayoutCall(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mFavouriteItems.get(mFavouritePosition).phoneNo));
        startActivity(intent);
    }

    public void onClickLayoutMsg(View v) {
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", mFavouriteItems.get(mFavouritePosition).phoneNo);
        startActivity(smsIntent);
    }

    public void onClickFavouriteMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.inflate(R.menu.contact_detail_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.remove_from_favourite:
                    Log.d("KieuThang", "remove_from_favourite");
                    break;
                case R.id.view_favorite_contact_detail:
                    Log.d("KieuThang", "view_favorite_contact_detail");
                    break;
                case R.id.share_this_contact_info:
                    Log.d("KieuThang", "share_this_contact_info");
                    break;
                }
                return true;

            }
        });
        popup.show();
    }

    @Override
    public void refreshUI() {
        mFavouriteAdapter.notifyDataSetChanged();
    }

    private class AsyncUpdateRecordList extends AsyncTask<Void, Void, Integer> {
        private int _position;

        public AsyncUpdateRecordList(Context context, int position) {
            _position = position;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (_position < 0)
                return 0;
            Log.d("KieuThang", "onScrolledToPosition:" + (mFavouriteItems.get(_position).phoneName));
            if (mFavouriteItems == null || mFavouriteItems.size() < _position || StringUtility.isEmpty(mFavouriteItems.get(_position).phoneName)) {
                return 0;
            } else {
                mFavouriteRecordingSessions = getFavouriteFromList(_position);
                return 1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mDotsTextView.stop();
            mDotsTextView.setVisibility(View.GONE);
            mTextRecord.setVisibility(View.VISIBLE);
            if (result == 0) {
                mTextNoRecordFound.setVisibility(View.VISIBLE);
            } else if (result == 1) {
                mRecordingFavouriteList.setVisibility(View.VISIBLE);
                mTextSwitcherPhoneName.setText(mFavouriteItems.get(_position).phoneName);
                mTextRecord.setText(getResources().getString(R.string.record_withs, mFavouriteItems.get(_position).phoneName));
                 mRecordingFavouriteAdapter.updateDetailRecordingSession(mFavouriteRecordingSessions);
            }
        }
    }
}
