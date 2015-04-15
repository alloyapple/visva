/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.visva.voicerecorder.note;

import java.util.Calendar;

import mirko.android.datetimepicker.date.DatePickerDialog;
import mirko.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import mirko.android.datetimepicker.time.RadialPickerLayout;
import mirko.android.datetimepicker.time.TimePickerDialog;
import mirko.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.visva.android.visvasdklibrary.remind.IReminder;
import com.visva.android.visvasdklibrary.remind.ReminderItem;
import com.visva.android.visvasdklibrary.remind.ReminderProvider;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.TimeUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;

/**
 * This Activity handles "editing" a note, where editing is responding to
 * {@link Intent#ACTION_VIEW} (request to view data), edit a note
 * {@link Intent#ACTION_EDIT}, create a note {@link Intent#ACTION_INSERT}, or
 * create a new note from the current contents of the clipboard
 * {@link Intent#ACTION_PASTE}.
 * 
 * NOTE: Notice that the provider operations in this Activity are taking place
 * on the UI thread. This is not a good practice. It is only done here to make
 * the code more readable. A real application should use the
 * {@link android.content.AsyncQueryHandler} or {@link android.os.AsyncTask}
 * object to perform operations asynchronously on a separate thread.
 */
public class ActivityNoteEditor extends Activity implements IReminder {
    /*==================================Contant Define======================*/
    /*
     * Creates a projection that returns the note ID and the note contents.
     */
    private static final String[] PROJECTION       = new String[] {
                                                   NotePad.Notes._ID,
                                                   NotePad.Notes.COLUMN_NAME_TITLE,
                                                   NotePad.Notes.COLUMN_NAME_NOTE,
                                                   NotePad.Notes.COLUMN_NAME_REMIND_TIME };

    // A label for the saved state of the activity
    private static final String   ORIGINAL_CONTENT = "origContent";

    /*==================================Control Define======================*/
    private TextView              mTextPhoneName;
    private TextView              mTextPhoneNo;
    private TextView              mTextDateTime;
    private EditText              mEdittextSubject;
    private Button                mBtnDeleteNote;
    private RelativeLayout        mLayoutHeader;
    private View                  mDivider;
    /*==================================Variable Define======================*/
    // Global mutable variables
    private int                   mState;
    private Uri                   mUri;
    private Cursor                mCursor;
    private LinedEditText         mTextNote;
    private RelativeLayout        mLayoutRemindDateTime;
    private LinearLayout          mLayoutRemind;
    private EditText              mEditTextDate;
    private EditText              mEditTextTime;
    private String                mOriginalContent;
    private long                  mDateByMiliSeconds;
    private long                  mTimeByMiliSeconds;
    private long                  mCreatedTime;
    private long                  mReminderTime;
    private long                  mPreviousReminderTime;
    private String                mPhoneName;
    private String                mPhoneNo;
    private int                   mThemeColor;
    private boolean               isDeleteRemind   = false;

    /**
     * This method is called by Android when the Activity is first started. From
     * the incoming Intent, it determines what kind of editing is desired, and
     * then does it.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        intent.setData(NotePad.Notes.CONTENT_URI);
        final String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        String selection = null;

        if (MyCallRecorderConstant.MAKE_NOTE_INTENT.equals(action)) {
            mPhoneName = bundle.getString(MyCallRecorderConstant.EXTRA_PHONE_NAME);
            String dateTime = bundle.getString(MyCallRecorderConstant.EXTRA_CREATED_DATE);
            mCreatedTime = Long.valueOf(dateTime);
            mPhoneNo = bundle.getString(MyCallRecorderConstant.EXTRA_PHONE_NO);
            mState = bundle.getInt(MyCallRecorderConstant.EXTRA_STATE);

            if (mState == MyCallRecorderConstant.STATE_INSERT) {
                mUri = getContentResolver().insert(intent.getData(), null);

                if (mUri == null) {
                    AIOLog.e(MyCallRecorderConstant.TAG, "Failed to insert new note into " + getIntent().getData());
                    finish();
                    return;
                }

                // Since the new entry was created, this sets the result to be
                // returned set the result to be returned.
                setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));
            } else {
                mUri = intent.getData();
                selection = NotePad.Notes.COLUMN_NAME_CREATE_DATE + " = " + dateTime;
            }

        } else {
            Toast.makeText(this, "Testing", Toast.LENGTH_SHORT).show();
            mPhoneName = "S2";
            mCreatedTime = System.currentTimeMillis();
            mPhoneNo = "0977372932";
            mState = MyCallRecorderConstant.STATE_INSERT;
            mUri = getContentResolver().insert(intent.getData(), null);
            setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));
        }

        /*
         * Using the URI passed in with the triggering Intent, gets the note or
         * notes in the provider. Note: This is being done on the UI thread. It
         * will block the thread until the query completes. In a sample app,
         * going against a simple provider based on a local database, the block
         * will be momentary, but in a real app you should use
         * android.content.AsyncQueryHandler or android.os.AsyncTask.
         */
        mCursor = managedQuery(mUri, // The URI that gets multiple notes from the provider.
                PROJECTION, // A projection that returns the note ID and note content for each note.
                selection, // No "where" clause selection criteria.
                null, // No "where" clause selection values.
                NotePad.Notes.DEFAULT_SORT_ORDER // Use the default sort order (modification date, descending)
        );

        // Sets the layout for this Activity. See res/layout/note_editor.xml
        setContentView(R.layout.activity_note_layout);

        initLayout();

        initData();

        /*
         * If this Activity had stopped previously, its state was written the
         * ORIGINAL_CONTENT location in the saved Instance state. This gets the
         * state.
         */
        if (savedInstanceState != null) {
            mOriginalContent = savedInstanceState.getString(ORIGINAL_CONTENT);
        }
    }

    private void initData() {
        mThemeColor = MyCallRecorderApplication.getInstance().getApplicationTheme();
        if (StringUtility.isEmpty(mPhoneName)) {
            mTextPhoneName.setVisibility(View.GONE);
        } else {
            mTextPhoneName.setVisibility(View.VISIBLE);
            mTextPhoneName.setText(mPhoneName);
        }
        mTextPhoneNo.setText(mPhoneNo);
        String createdDate = Utils.getTextDate(this, mCreatedTime) + " " + Utils.getTextTime(this, mCreatedTime);
        mTextDateTime.setText(createdDate);

        /*theme*/
        mLayoutHeader.setBackgroundColor(mThemeColor);
        mDivider.setBackgroundColor(mThemeColor);
    }

    private void initLayout() {
        // Gets a handle to the EditText in the the layout.
        mLayoutHeader = (RelativeLayout) findViewById(R.id.layout_header);
        mDivider = (View) findViewById(R.id.divider);
        mTextNote = (LinedEditText) findViewById(R.id.note);
        mTextPhoneName = (TextView) findViewById(R.id.phone_name);
        mTextPhoneNo = (TextView) findViewById(R.id.phone_number);
        mTextDateTime = (TextView) findViewById(R.id.date_time);
        mEdittextSubject = (EditText) findViewById(R.id.subject);
        mLayoutRemind = (LinearLayout) findViewById(R.id.layout_remind_me);
        mLayoutRemindDateTime = (RelativeLayout) findViewById(R.id.layout_date_time);
        mBtnDeleteNote = (Button) findViewById(R.id.btn_delete);

        mEditTextDate = (EditText) findViewById(R.id.txtDate);
        mEditTextTime = (EditText) findViewById(R.id.txtTime);
        mEditTextDate.setOnClickListener(new View.OnClickListener() {
            private String tag;

            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), tag);
            }
        });

        mEditTextTime.setOnClickListener(new View.OnClickListener() {
            private String tag;

            @Override
            public void onClick(View v) {
                timePickerDialog24h.show(getFragmentManager(), tag);
            }
        });
        if (mState == MyCallRecorderConstant.STATE_EDIT) {
            mBtnDeleteNote.setVisibility(View.VISIBLE);
        } else {
            mBtnDeleteNote.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * mCursor is initialized, since onCreate() always precedes onResume for
         * any running process. This tests that it's not null, since it should
         * always contain data.
         */
        if (mCursor != null && mCursor.getCount() > 0) {
            // Requery in case something changed while paused (such as the title)
            mCursor.requery();
            mCursor.moveToFirst();

            // Modifies the window title for the Activity according to the current Activity state.
            if (mState == MyCallRecorderConstant.STATE_EDIT) {
                // Set the title of the Activity to include the note title
                int colTitleIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE);
                String title = mCursor.getString(colTitleIndex);
                Resources res = getResources();
                String text = String.format(res.getString(R.string.title_edit), title);
                setTitle(text);
                // Sets the title to "create" for inserts
            } else if (mState == MyCallRecorderConstant.STATE_INSERT) {
                setTitle(getText(R.string.title_create));
            }

            // Gets the note text from the Cursor and puts it in the TextView,
            // but doesn't change  the text cursor's position.
            int colNoteIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_NOTE);
            int colTitleIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE);
            int colRemindTimeIndex = mCursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_REMIND_TIME);
            String note = mCursor.getString(colNoteIndex);
            String title = mCursor.getString(colTitleIndex);
            String remindTimeStr = mCursor.getString(colRemindTimeIndex);
            mPreviousReminderTime = Long.valueOf(remindTimeStr);
            Log.d("KieuThang", "title:" + title);
            mTextNote.setTextKeepState(note);
            mEdittextSubject.setText(title);
            Log.d("KieuThang", "mPreviousReminderTime:" + mPreviousReminderTime + ",mReminderTime:" + mReminderTime);
            if (mPreviousReminderTime > 0) {
                mReminderTime = mPreviousReminderTime;
                mLayoutRemind.setVisibility(View.GONE);
                mLayoutRemindDateTime.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(mReminderTime);

                String date = Utils.getTextDate(ActivityNoteEditor.this, mReminderTime);
                SpannableString content = new SpannableString(date);
                content.setSpan(new UnderlineSpan(), 0, date.length(), 0);
                mEditTextDate.setText(content);
                mDateByMiliSeconds = TimeUtility.getDateInMilisFromTime(mReminderTime);

                String time = Utils.getTextTime(ActivityNoteEditor.this, mReminderTime);
                SpannableString timeContent = new SpannableString(time);
                timeContent.setSpan(new UnderlineSpan(), 0, time.length(), 0);
                mEditTextTime.setText(timeContent);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                mTimeByMiliSeconds = hour * TimeUtility.HOUR_IN_MILIS + minute * TimeUtility.MINUTE_IN_MILIS;
            } else {
                onClickCloseRemindMe(null);
            }

            // Stores the original note text, to allow the user to revert changes.
            if (mOriginalContent == null) {
                mOriginalContent = note;
            }

            /*
             * Something is wrong. The Cursor should always contain data. Report
             * an error in the note.
             */
        } else {
            mEdittextSubject.setText(getText(R.string.error_title));
            mTextNote.setText(getText(R.string.error_message));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
        outState.putString(ORIGINAL_CONTENT, mOriginalContent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Replaces the current note contents with the text and title provided as
     * arguments.
     * 
     * @param text
     *            The new note contents to use.
     * @param title
     *            The new note title to use
     */
    private final void updateNote(String text, String title, long createdTime, long reminderTime) {
        Log.d("KieuThang", "text:" + text + ",title:" + title + ",reminderTime:" + reminderTime);
        // Sets up a map to contain values to be updated in the provider.
        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_REMIND_TIME, reminderTime);

        // If the action is to insert a new note, this creates an initial title  for it.
        if (mState == MyCallRecorderConstant.STATE_INSERT) {

            // If no title was provided as an argument, create one from the note text.
            if (title == null) {

                // Get the note's length
                int length = text.length();

                // Sets the title by getting a substring of the text that is 31 characters long
                // or the number of characters in the note plus one, whichever is smaller.
                title = text.substring(0, Math.min(30, length));

                // If the resulting length is more than 30 characters, chops off any trailing spaces
                if (length > 30) {
                    int lastSpace = title.lastIndexOf(' ');
                    if (lastSpace > 0) {
                        title = title.substring(0, lastSpace);
                    }
                }
            }
            // In the values map, sets the value of the title
            values.put(NotePad.Notes.COLUMN_NAME_TITLE, title);
        } else if (title != null) {
            // In the values map, sets the value of the title
            values.put(NotePad.Notes.COLUMN_NAME_TITLE, title);
        }

        // This puts the desired notes text into the map.
        values.put(NotePad.Notes.COLUMN_NAME_NOTE, text);

        //this value put to determind the created time. This value is the foreign key also,
        //related /*custom_id*/ column in remind table

        values.put(NotePad.Notes.COLUMN_NAME_CREATE_DATE, createdTime);
        /*
         * Updates the provider with the new values in the map. The ListView is
         * updated automatically. The provider sets this up by setting the
         * notification URI for query Cursor objects to the incoming URI. The
         * content resolver is thus automatically notified when the Cursor for
         * the URI changes, and the UI is updated. Note: This is being done on
         * the UI thread. It will block the thread until the update completes.
         * In a sample app, going against a simple provider based on a local
         * database, the block will be momentary, but in a real app you should
         * use android.content.AsyncQueryHandler or android.os.AsyncTask.
         */
        if (mState == MyCallRecorderConstant.STATE_INSERT) {
            getContentResolver().insert(mUri, values);/*(mUri, // The URI for the record to update.
                                                      values, // The map of column names and new values to apply to them.
                                                      null, // No selection criteria are used, so no where columns are necessary.
                                                      null // No where columns are used, so no where arguments are necessary.
                                                      );*/
        } else {
            String selection = NotePad.Notes.COLUMN_NAME_CREATE_DATE + " = " + mCreatedTime;
            getContentResolver().update(mUri, // The URI for the record to update.
                    values, // The map of column names and new values to apply to them.
                    selection, // No selection criteria are used, so no where columns are necessary.
                    null // No where columns are used, so no where arguments are necessary.
                    );
        }
    }

    /**
     * This helper method cancels the work done on a note. It deletes the note
     * if it was newly created, or reverts to the original text of the note i
     */
    private final void cancelNote() {
        if (mCursor != null) {
            if (mState == MyCallRecorderConstant.STATE_EDIT) {
                // Put the original note text back into the database
                mCursor.close();
                mCursor = null;
                ContentValues values = new ContentValues();
                values.put(NotePad.Notes.COLUMN_NAME_NOTE, mOriginalContent);
                getContentResolver().update(mUri, values, null, null);
            } else if (mState == MyCallRecorderConstant.STATE_INSERT) {
                // We inserted an empty note, make sure to delete it
                deleteNote();
            }
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Take care of deleting a note. Simply deletes the entry.
     */
    private final void deleteNote() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
            String where = NotePad.Notes.COLUMN_NAME_CREATE_DATE + " = " + mCreatedTime;
            getContentResolver().delete(mUri, where, null);
            mTextNote.setText("");
            mEdittextSubject.setText("");
        }
    }

    public void onClickRemindMe(View v) {
        mLayoutRemind.setVisibility(View.GONE);
        mLayoutRemindDateTime.setVisibility(View.VISIBLE);

        mReminderTime = System.currentTimeMillis() + TimeUtility.HOUR_IN_MILIS;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mReminderTime);

        String date = Utils.getTextDate(ActivityNoteEditor.this, mReminderTime);
        SpannableString content = new SpannableString(date);
        content.setSpan(new UnderlineSpan(), 0, date.length(), 0);
        mEditTextDate.setText(content);
        mDateByMiliSeconds = TimeUtility.getDateInMilisFromTime(mReminderTime);

        String time = Utils.getTextTime(ActivityNoteEditor.this, mReminderTime);
        SpannableString timeContent = new SpannableString(time);
        timeContent.setSpan(new UnderlineSpan(), 0, time.length(), 0);
        mEditTextTime.setText(timeContent);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        mTimeByMiliSeconds = hour * TimeUtility.HOUR_IN_MILIS + minute * TimeUtility.MINUTE_IN_MILIS;
    }

    public void onClickCloseRemindMe(View v) {
        mReminderTime = 0;
        if (mPreviousReminderTime > 0)
            isDeleteRemind = true;
        mLayoutRemind.setVisibility(View.VISIBLE);
        mLayoutRemindDateTime.setVisibility(View.GONE);
    }

    @Override
    public boolean onRemind(Context context, ReminderItem reminderItem) {
        Log.d("KieuThang", "reminderItem:" + reminderItem.id);
        Resources resources = context.getResources();
        String title = null;
        String message = null;
        if (reminderItem != null) {
            title = Utils.getPhoneNameFromCreatedTime(context, reminderItem.id);
            message = Utils.getNoteMessageFromCreatedTime(context, reminderItem.id);
        }
        if (StringUtility.isEmpty(title))
            title = resources.getString(R.string.app_name_reminder);
        if (StringUtility.isEmpty(message))
            message = resources.getString(R.string.you_have_new_reminder);
        String content[] = reminderItem.id.split(MyCallRecorderConstant.NOTIFICATION_REMINDER_ID + "");
        if (content != null && content.length > 1) {
            title = content[0];
            message = content[1];
        }
        Utils.showNotificationAtReminderTime(context, title, message);
        return true;
    }

    public void onClickSaveNote(View v) {
        String remindId = String.valueOf(mCreatedTime);
        // update values to note database
        String subject = mEdittextSubject.getText().toString();
        String text = mTextNote.getText().toString();
        if (StringUtility.isEmpty(subject) && StringUtility.isEmpty(text)) {
            AIOLog.e(MyCallRecorderConstant.TAG, "text and subject is empty!");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        //setup reminder time and data for remind notification
        mReminderTime = mDateByMiliSeconds + mTimeByMiliSeconds;
        if (isDeleteRemind) {
            mReminderTime = 0;
            ReminderProvider.getInstance(ActivityNoteEditor.this).deleteReminder(ActivityNoteEditor.class, remindId);
        }
        //update note to database
        updateNote(text, subject, mCreatedTime, mReminderTime);

        String message = mEdittextSubject.getText().toString();

        if (StringUtility.isEmpty(message)) {
            message = mTextNote.getText().toString();
        }
        if (mReminderTime == 0 || mReminderTime <= mPreviousReminderTime) {
            AIOLog.e(MyCallRecorderConstant.TAG, "reminder time is not set!!");
            if (MyCallRecorderApplication.getInstance().getActivity() != null) {
                Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_ALL_RECORDING);
                Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_FAVOURITE);
            }
            setResult(RESULT_OK);
            finish();
            return;
        }

        ReminderProvider.getInstance(ActivityNoteEditor.this).addReminder(ActivityNoteEditor.class, remindId, mReminderTime,
                AlarmManager.INTERVAL_DAY, 1);
        mReminderTime = 0;

        if (MyCallRecorderApplication.getInstance().getActivity() != null) {
            Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_ALL_RECORDING);
            Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_FAVOURITE);
        }
        setResult(RESULT_OK);
        finish();
    }

    public void onClickBackBtn(View v) {
        finish();
    }

    public void onClickDeleteNoteBtn(View v) {
        String title = getResources().getString(R.string.delete);
        String contentMsg = getResources().getString(R.string.are_you_sure_to_delete_note);
        String cancel = getResources().getString(R.string.cancel);
        Dialog dialog = new Dialog(this, title, contentMsg);
        dialog.addCancelButton(cancel, new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteNote();
                String deleted = getResources().getString(R.string.deleted);
                Toast.makeText(ActivityNoteEditor.this, deleted, Toast.LENGTH_SHORT).show();
                ActivityNoteEditor.this.finish();
                if (MyCallRecorderApplication.getInstance().getActivity() != null) {
                    Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_ALL_RECORDING);
                    Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_FAVOURITE);
                }
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {

        }
    }

    private final Calendar mCalendar           = Calendar.getInstance();
    final TimePickerDialog timePickerDialog12h = TimePickerDialog.newInstance(new OnTimeSetListener() {

                                                   @Override
                                                   public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                                                       Object c = TimeUtility.pad3(hourOfDay);

                                                       mEditTextTime.setText(new StringBuilder().append(TimeUtility.pad2(hourOfDay)).append(":")
                                                               .append(TimeUtility.pad(minute)).append(c));
                                                   }
                                               }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

    final TimePickerDialog timePickerDialog24h = TimePickerDialog.newInstance(new OnTimeSetListener() {

                                                   @Override
                                                   public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                                       mTimeByMiliSeconds = hourOfDay * TimeUtility.HOUR_IN_MILIS + minute
                                                               * TimeUtility.MINUTE_IN_MILIS;
                                                       StringBuilder builder = new StringBuilder();
                                                       builder.append(TimeUtility.pad(hourOfDay)).append(":").append(TimeUtility.pad(minute));
                                                       SpannableString content = new SpannableString(builder.toString());
                                                       content.setSpan(new UnderlineSpan(), 0, builder.toString().length(), 0);
                                                       mEditTextTime.setText(content);
                                                   }
                                               }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

    final DatePickerDialog datePickerDialog    = DatePickerDialog.newInstance(new OnDateSetListener() {

                                                   public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                                                       Calendar calendar = Calendar.getInstance();
                                                       calendar.set(year, month, day, 0, 0, 0);
                                                       mDateByMiliSeconds = calendar.getTimeInMillis();

                                                       String date = Utils.getTextDate(ActivityNoteEditor.this, mDateByMiliSeconds);
                                                       SpannableString content = new SpannableString(date);
                                                       content.setSpan(new UnderlineSpan(), 0, date.length(), 0);
                                                       mEditTextDate.setText(content);
                                                       Log.d("KieuThang", "mDateByMiliSeconds:" + mDateByMiliSeconds);
                                                   }

                                               }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
}
