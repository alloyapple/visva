package com.visva.voicerecorder.view.recording;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.gc.materialdesign.widgets.Dialog;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.note.ActivityNoteEditor;
import com.visva.voicerecorder.note.NoteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;
import com.visva.voicerecorder.view.activity.ActivityPlayRecording;
import com.visva.voicerecorder.view.common.FragmentBasic;

public class FragmentAllRecord extends FragmentBasic implements OnMenuItemClickListener {
    // ======================Constant Define=====================

    // ======================Control Define =====================
    private SwipeMenuListView           mLvRecords;
    private RecordingAdapter            mRecordingAdapter;
    private TextView                    mTextNoRecord;
    // =======================Class Define ======================
    private ProgramHelper               mProgramHelper;
    // ======================Variable Define=====================
    private ArrayList<RecordingSession> mSessions = new ArrayList<RecordingSession>();
    public View                         mLastClickedView;
    private ActionMode                  mActionMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_all_record, container);

        mProgramHelper = MyCallRecorderApplication.getInstance().getProgramHelper();
        mSessions = mProgramHelper.getRecordingSessionsFromFile(getActivity());
        initLayout(root);
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mActionMode != null)
            mActionMode.finish();
    }

    private void initLayout(View root) {
        mTextNoRecord = (TextView) root.findViewById(R.id.text_no_record_found);
        mLvRecords = (SwipeMenuListView) root.findViewById(R.id.lv_all_recorder);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                Resources res = getActivity().getResources();
                deleteItem.setBackground(new ColorDrawable(res.getColor(R.color.material_design_color_orange_action_delete)));
                deleteItem.setWidth(Utils.dp2px(getActivity(), 80));
                deleteItem.setIcon(R.drawable.btn_delete);
                menu.addMenuItem(deleteItem);

                SwipeMenuItem noteItem = new SwipeMenuItem(getActivity());
                noteItem.setBackground(new ColorDrawable(res.getColor(R.color.material_design_color_orange_action_normal)));
                noteItem.setWidth(Utils.dp2px(getActivity(), 80));
                noteItem.setIcon(R.drawable.btn_note);
                menu.addMenuItem(noteItem);

                SwipeMenuItem shareItem = new SwipeMenuItem(getActivity());
                shareItem.setBackground(new ColorDrawable(res.getColor(R.color.material_design_color_orange_action_normal)));
                shareItem.setWidth(Utils.dp2px(getActivity(), 80));
                shareItem.setIcon(R.drawable.btn_favorite);
                menu.addMenuItem(shareItem);

                SwipeMenuItem callItem = new SwipeMenuItem(getActivity());
                callItem.setBackground(new ColorDrawable(res.getColor(R.color.material_design_color_orange_action_normal)));
                callItem.setWidth(Utils.dp2px(getActivity(), 80));
                callItem.setIcon(R.drawable.btn_share);
                menu.addMenuItem(callItem);
            }
        };

        mLvRecords.setMenuCreator(creator);
        mLvRecords.setOnMenuItemClickListener(this);

        mRecordingAdapter = new RecordingAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, mSessions);
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

        mLvRecords.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click
        mLvRecords.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = mLvRecords.getCheckedItemCount();
                Resources res = getResources();
                String title = res.getString(R.string.selected, checkedCount);
                // Set the CAB title according to total checked items
                mode.setTitle(title);
                // Calls toggleSelection method from ListViewAdapter Class
                mRecordingAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                case R.id.delete:
                    // Calls getSelectedIds method from ListViewAdapter Class
                    SparseBooleanArray selectedList = mRecordingAdapter.getSelectedIds();
                    if (selectedList == null || selectedList.size() == 0)
                        return false;
                    onClickDeleteActionMode(selectedList);
                    return true;
                case R.id.share:
                    selectedList = mRecordingAdapter.getSelectedIds();
                    if (selectedList == null || selectedList.size() == 0)
                        return false;
                    ArrayList<RecordingSession> recordingSessions = new ArrayList<RecordingSession>();
                    for (int i = 0; i < selectedList.size(); i++) {
                        int position = selectedList.keyAt(i);
                        RecordingSession recordingSession = mSessions.get(position);
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
                mRecordingAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }
        });

        if (mSessions.size() == 0) {
            mLvRecords.setVisibility(View.GONE);
            mTextNoRecord.setVisibility(View.VISIBLE);
        } else {
            mLvRecords.setVisibility(View.VISIBLE);
            mTextNoRecord.setVisibility(View.GONE);
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
                    RecordingSession session = mSessions.get(position);
                    Utils.deleteRecordingSesstionAction(getActivity(), session);
                    mRecordingAdapter.removeRecord(position);
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

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
        case 0:
            deleteRecordingSessionAction(position);
            break;
        case 1:
            updateRecordSessionNote(position);
            break;
        case 2:
            updateThisContactFavourite(position);
            break;
        case 3:
            RecordingSession recordingSession = mSessions.get(position);
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

    // This method will check this contact is favourite contact or not first,
    // the checking action includes checking in the contact app. After that, it will add or 
    // remove (if the contact is already existed in favourites) in the contact and my call recorder database application
    private void updateThisContactFavourite(int selectedPosition) {
        RecordingSession recordingSession = mSessions.get(selectedPosition);
        if (recordingSession == null) {
            return;
        }
        Uri contactIdUri = Utils.getContactUriTypeFromPhoneNumber(getActivity().getContentResolver(), recordingSession.phoneNo, 0);
        String contactId = (contactIdUri == null ? "" : contactIdUri.toString());
        FavouriteItem favouriteItem = new FavouriteItem(recordingSession.phoneNo, recordingSession.phoneName, 1, contactId);
        if (Utils.isCheckFavouriteContactByPhoneNo(getActivity(), recordingSession.phoneNo) > 0) {
            String removedFavouriteContact = getActivity().getString(R.string.removed_from_favourite, recordingSession.phoneName);
            mSQLiteHelper.deleteFavouriteItem(favouriteItem);
            Toast.makeText(getActivity(), removedFavouriteContact, Toast.LENGTH_SHORT).show();
        }
        else {
            String addFavouriteContact = getActivity().getString(R.string.added_to_favourite, recordingSession.phoneName);
            mSQLiteHelper.addNewFavoriteItem(favouriteItem);
            Toast.makeText(getActivity(), addFavouriteContact, Toast.LENGTH_SHORT).show();
        }
        mRecordingAdapter.notifyDataSetChanged();
        
        if(MyCallRecorderApplication.getInstance().getActivity()!=null){
            Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_CONTACT);
            Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_FAVOURITE);
        }
    }

    private void updateRecordSessionNote(int position) {
        RecordingSession recordingSession = mSessions.get(position);
        if (recordingSession == null) {
            return;
        }
        int state = MyCallRecorderConstant.STATE_INSERT;
        NoteItem noteItem = Utils.getNoteItemFromRecordSession(getActivity(), recordingSession.dateCreated);
        if (noteItem == null || (StringUtility.isEmpty(noteItem.note) && StringUtility.isEmpty(noteItem.title))) {
            state = MyCallRecorderConstant.STATE_INSERT;
        } else {
            state = MyCallRecorderConstant.STATE_EDIT;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(MyCallRecorderConstant.EXTRA_STATE, state);
        bundle.putString(MyCallRecorderConstant.EXTRA_CREATED_DATE, recordingSession.dateCreated);
        bundle.putString(MyCallRecorderConstant.EXTRA_PHONE_NAME, recordingSession.phoneName);
        bundle.putString(MyCallRecorderConstant.EXTRA_PHONE_NO, recordingSession.phoneNo);

        Intent updateNoteIntent = new Intent(getActivity(), ActivityNoteEditor.class);
        updateNoteIntent.setAction(MyCallRecorderConstant.MAKE_NOTE_INTENT);
        updateNoteIntent.putExtras(bundle);
        updateNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(updateNoteIntent);
    }

    private void deleteRecordingSessionAction(final int position) {
        final RecordingSession recordingSession = mSessions.get(position);
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
                mRecordingAdapter.removeRecord(position);
            }
        });
        dialog.show();
    }

    public void onAllRecordTabClick(View v) {

    }

    public void onContactTabClick(View v) {

    }

    public void onTextSearchChanged(CharSequence s) {
        mRecordingAdapter.onTextSearchChanged(s);
    }

    public void refreshUI() {
        if (mRecordingAdapter == null)
            return;
        mRecordingAdapter.notifyDataSetChanged();
    }

    public void addNewRecord(RecordingSession recordingSession) {
        mRecordingAdapter.addNewRecord(recordingSession);
    }

    public void updateRecordList() {
        Log.d("KieuThang", "updateRecordList");
        mProgramHelper = MyCallRecorderApplication.getInstance().getProgramHelper();
        mSessions = mProgramHelper.getRecordingSessionsFromFile(getActivity());
        mRecordingAdapter.updateRecordingSession(mSessions);
    }
}
