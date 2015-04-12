package com.visva.voicerecorder.view.favourite;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.widgets.Dialog;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;
import com.visva.voicerecorder.view.activity.ActivityPlayRecording;
import com.visva.voicerecorder.view.common.FragmentBasic;
import com.visva.voicerecorder.view.widget.DotsTextView;

//import android.widget.Button;

public class FragmentFavourite extends FragmentBasic implements OnMenuItemClickListener {
    // ======================Control Define =====================
    private FeatureCoverFlow            mFavouriteList;
    private SwipeMenuListView           mRecordingFavouriteList;
    private LayoutRipple                mBtnOptionMenu;
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
    private ActionMode                  mActionMode;
    private ServiceHandler              mServiceHandler;

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

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceHandler = new ServiceHandler(getActivity().getMainLooper());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mActionMode != null)
            mActionMode.finish();
    }

    private void initLayout(View root) {
        Log.d("KieuThang", "initLayout");
        mDotsTextView = (DotsTextView) root.findViewById(R.id.dots);
        mTextNoRecordFound = (TextView) root.findViewById(R.id.text_no_record_found);
        mLayoutFavorite = (RelativeLayout) root.findViewById(R.id.layout_favorite);
        mTextNoFavoriteFound = (TextView) root.findViewById(R.id.text_no_favorite_found);
        mTextSwitcherPhoneName = (TextSwitcher) root.findViewById(R.id.text_switch_favourite_name);
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

        //     
        mTextRecord = (TextView) root.findViewById(R.id.text_record_title);
        mBtnOptionMenu = (LayoutRipple) root.findViewById(R.id.btn_favourite_menu);
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
        mRecordingFavouriteList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click
        mRecordingFavouriteList.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = mRecordingFavouriteList.getCheckedItemCount();
                Resources res = getResources();
                String title = res.getString(R.string.selected, checkedCount);
                // Set the CAB title according to total checked items
                mode.setTitle(title);
                // Calls toggleSelection method from ListViewAdapter Class
                mRecordingFavouriteAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                case R.id.delete:
                    // Calls getSelectedIds method from ListViewAdapter Class
                    SparseBooleanArray selectedList = mRecordingFavouriteAdapter.getSelectedIds();
                    if (selectedList == null || selectedList.size() == 0)
                        return false;
                    onClickDeleteActionMode(selectedList);
                    return true;
                case R.id.share:
                    selectedList = mRecordingFavouriteAdapter.getSelectedIds();
                    if (selectedList == null || selectedList.size() == 0)
                        return false;
                    ArrayList<RecordingSession> recordingSessions = new ArrayList<RecordingSession>();
                    for (int i = 0; i < selectedList.size(); i++) {
                        int position = selectedList.keyAt(i);
                        RecordingSession recordingSession = mFavouriteRecordingSessions.get(position);
                        recordingSessions.add(recordingSession);
                    }
                    Utils.shareMultiFileByShareActionMode(getActivity(), recordingSessions);
                    mode.finish();
                    return true;
                default:
                    return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                mActionMode = mode;
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mRecordingFavouriteAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }
        });

        mRecordingFavouriteAdapter = new DetailFavouriteAdapter(getActivity(), mFavouriteRecordingSessions);

        if (mFavouriteItems.size() == 0) {
            mLayoutFavorite.setVisibility(View.GONE);
            mTextNoFavoriteFound.setVisibility(View.VISIBLE);
        } else {
            mLayoutFavorite.setVisibility(View.VISIBLE);
            mTextNoFavoriteFound.setVisibility(View.GONE);
            mRecordingFavouriteList.setAdapter(mRecordingFavouriteAdapter);

            AsyncUpdateRecordList asyncUpdateRecordList = new AsyncUpdateRecordList(getActivity(), 0);
            asyncUpdateRecordList.execute();
        }
    }

    private void onClickDeleteActionMode(final SparseBooleanArray selected) {
        int size = selected.size();
        String title = getResources().getString(R.string.one_selected_record, size);
        String contentMsg = getResources().getString(R.string.are_you_sure_to_selected_record);
        if (size > 1) {
            title = getResources().getString(R.string.multi_selected_record, size);
            contentMsg = getResources().getString(R.string.are_you_sure_to_selected_records);
        }
        String cancel = getResources().getString(R.string.cancel);
        Dialog dialog = new Dialog(getActivity(), title, contentMsg);
        dialog.addCancelButton(cancel, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mActionMode != null)
                    mActionMode.finish();
            }
        });
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mActionMode != null)
                    mActionMode.finish();
                for (int i = 0; i < selected.size(); i++) {
                    Log.d("KieuThang", "selected.keyAt:" + selected.keyAt(i));
                    int position = selected.keyAt(i);
                    RecordingSession session = mFavouriteRecordingSessions.get(position);
                    Utils.deleteRecordingSesstionAction(getActivity(), session);
                    mRecordingFavouriteAdapter.removeRecord(position);
                }
                String deleted = getResources().getString(R.string.deleted);
                Toast.makeText(getActivity(), deleted, Toast.LENGTH_SHORT).show();
                if (MyCallRecorderApplication.getInstance().getActivity() != null) {
                    MyCallRecorderApplication.getInstance().getActivity().requestToRefreshView(ActivityHome.FRAGMENT_ALL_RECORDING);
                }
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {

        }
    }

    private ArrayList<RecordingSession> getFavouriteFromList(int position) {
        ArrayList<RecordingSession> list = new ArrayList<RecordingSession>();
        if (mFavouriteItems.size() == 0)
            return list;
        String phoneNo = mFavouriteItems.get(position).phoneNo;
        mRecordingSessions = mSQLiteHelper.getAllRecordItem();
        for (RecordingSession recordingSession : mRecordingSessions) {
            if (Utils.isSamePhoneNo(getActivity(), recordingSession.phoneNo, phoneNo)) {
                list.add(recordingSession);
            }
        }
        /* Sort statement*/
        Collections.sort(list);
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
    private void onLongClickFavouriteItem(View view, int position) {
        mIsLongClickFavouriteItem = true;
        mRecordingFavouriteAdapter.setLongClickStateView(mIsLongClickFavouriteItem);
    }

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
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
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
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
                    removeThisContactFromFavorites();
                    break;
                case R.id.view_favorite_contact_detail:
                    viewContactDetail();
                    break;
                case R.id.share_this_contact_info:
                    shareThisContactAction();
                    break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void shareThisContactAction() {
        FavouriteItem favouriteItem = mFavouriteItems.get(mFavouritePosition);
        if (favouriteItem == null)
            return;
        String displayName = favouriteItem.phoneName;
        Resources res = getActivity().getResources();
        StringBuilder builder = new StringBuilder();
        if (!StringUtility.isEmpty(displayName))
            builder.append(res.getString(R.string.name)).append(displayName + "\n");

        builder.append(res.getString(R.string.phone_no)).append(favouriteItem.phoneNo);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, res.getString(R.string.share_contact));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, builder.toString());
        startActivity(Intent.createChooser(sharingIntent, displayName));
    }

    private void viewContactDetail() {
        final FavouriteItem favouriteItem = mFavouriteItems.get(mFavouritePosition);
        if (favouriteItem == null) {
            String message = getActivity().getString(R.string.this_contact_not_in_your_contact);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtility.isEmpty(favouriteItem.phoneName)) {
            return;
        }
        String phoneNo = favouriteItem.phoneNo;
        Uri idUri = Utils.getContactUriTypeFromPhoneNumber(getActivity().getContentResolver(), phoneNo, 0);
        Uri lookupKey = Utils.getContactUriTypeFromPhoneNumber(getActivity().getContentResolver(), phoneNo, 4);
        if (idUri == null || lookupKey == null)
            return;
        final Uri uri = Contacts.getLookupUri(Long.valueOf(idUri.toString()), lookupKey.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void removeThisContactFromFavorites() {
        final FavouriteItem favouriteItem = mFavouriteItems.get(mFavouritePosition);
        if (favouriteItem == null)
            return;
        String title = favouriteItem.phoneName;
        if (StringUtility.isEmpty(title)) {
            title = favouriteItem.phoneNo;
        }
        String contentMsg = getActivity().getString(R.string.are_you_sure_to_remove_this_contact_from_favorite);
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
                String phoneName = favouriteItem.phoneName == null ? favouriteItem.phoneNo : favouriteItem.phoneName;
                String removedFavouriteContact = getActivity().getString(R.string.removed_from_favourite, phoneName);
                mSQLiteHelper.deleteFavouriteItem(favouriteItem);
                refreshUI();
                Toast.makeText(getActivity(), removedFavouriteContact, Toast.LENGTH_SHORT).show();
                if (MyCallRecorderApplication.getInstance().getActivity() != null) {
                    MyCallRecorderApplication.getInstance().getActivity().requestToRefreshView(ActivityHome.FRAGMENT_ALL_RECORDING);
                }
            }
        });
        dialog.show();
    }

    @Override
    public void refreshUI() {
        mFavouriteItems = mSQLiteHelper.getAllFavouriteItem();
        if (mFavouriteAdapter == null)
            return;
        if (mFavouriteItems.size() == 0) {
            mLayoutFavorite.setVisibility(View.GONE);
            mTextNoFavoriteFound.setVisibility(View.VISIBLE);
        } else {
            mLayoutFavorite.setVisibility(View.VISIBLE);
            mTextNoFavoriteFound.setVisibility(View.GONE);
            mFavouriteList.clearCache();
            mFavouriteAdapter = new FavouriteAdapter(getActivity(), mFavouriteItems);
            mFavouriteList.setAdapter(mFavouriteAdapter);
        }
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
            if (mFavouriteItems == null || mFavouriteItems.size() - 1 < _position || StringUtility.isEmpty(mFavouriteItems.get(_position).phoneName)) {
                return 0;
            } else {
                mFavouriteRecordingSessions = getFavouriteFromList(_position);
                if (mFavouriteRecordingSessions.size() == 0)
                    return 0;
                return 1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            Message message = new Message();
            message.arg1 = result;
            message.arg2 = _position;
            if (mServiceHandler == null)
                mServiceHandler = new ServiceHandler(getActivity().getMainLooper());
            mServiceHandler.sendMessage(message);
        }
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            int _position = msg.arg2;
            Log.d("KieuThang", "_position > mFavouriteItems.size()" + (_position > mFavouriteItems.size()));
            Log.d("KieuThang", "msg.arg1" + msg.arg1);
            Log.d("KieuThang", "mFavouriteRecordingSessions" + mFavouriteRecordingSessions.size());
            if (_position > mFavouriteItems.size() - 1)
                return;
            mDotsTextView.stop();
            mDotsTextView.setVisibility(View.GONE);
            mTextRecord.setVisibility(View.VISIBLE);
            mTextSwitcherPhoneName.setText(mFavouriteItems.get(_position).phoneName);
            if (msg.arg1 == 0) {
                mTextNoRecordFound.setVisibility(View.VISIBLE);
                mRecordingFavouriteList.setVisibility(View.GONE);
            } else if (msg.arg1 == 1) {
                mTextNoRecordFound.setVisibility(View.GONE);
                mRecordingFavouriteList.setVisibility(View.VISIBLE);
                mTextRecord.setText(getResources().getString(R.string.record_withs, mFavouriteItems.get(_position).phoneName));
                mRecordingFavouriteAdapter.updateDetailRecordingSession(mFavouriteRecordingSessions);
            }
        }
    }
}
