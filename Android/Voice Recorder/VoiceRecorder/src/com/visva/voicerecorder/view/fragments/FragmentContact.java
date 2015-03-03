/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.visva.voicerecorder.view.fragments;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;
import com.visva.voicerecorder.view.widget.CircleImageView;

/**
 * This fragment displays a list of contacts stored in the Contacts Provider. Each item in the list
 * shows the contact's thumbnail photo and display name. On devices with large screens, this
 * fragment's UI appears as part of a two-pane layout, along with the UI of
 * {@link FragmentContactDetail}. On smaller screens, this fragment's UI appears as a single pane.
 *
 * This Fragment retrieves contacts based on a search string. If the user doesn't enter a search
 * string, then the list contains all the contacts in the Contacts Provider. If the user enters a
 * search string, then the list contains only those contacts whose data matches the string. The
 * Contacts Provider itself controls the matching algorithm, which is a "substring" search: if the
 * search string is a substring of any of the contacts data, then there is a match.
 *
 * On newer API platforms, the search is implemented in a SearchView in the ActionBar; as the user
 * types the search string, the list automatically refreshes to display results ("type to filter").
 * On older platforms, the user must enter the full string and trigger the search. In response, the
 * trigger starts a new Activity which loads a fresh instance of this fragment. The resulting UI
 * displays the filtered list and disables the search feature to prevent furthering searching.
 */
public class FragmentContact extends FragmentBasic implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemLongClickListener,
        OnMenuItemClickListener {
    // ======================Constant Define=====================
    // Defines a tag for identifying log entries
    private static final String           TAG                           = "ContactsListFragment";

    // Bundle key for saving previously selected search result item
    private static final String           STATE_PREVIOUSLY_SELECTED_KEY =
                                                                                "com.example.android.contactslist.ui.SELECTED_ITEM";
    // ======================Control Define =====================
    private SwipeMenuListView             mListContact;
    private TextView                      mTextNoContact;
    // =======================Class Define ======================
    // ======================Variable Define=====================
    private ContactsAdapter               mAdapter;                                                                                 // The main query adapter
    private String                        mSearchTerm;                                                                              // Stores the current search query term

    // Contact selected listener that allows the activity holding this fragment to be notified of
    // a contact being selected
    private OnContactsInteractionListener mOnContactSelectedListener;

    // Stores the previously selected search item so that on a configuration change the same item
    // can be reselected again
    private int                           mPreviouslySelectedSearchItem = 0;

    // Whether or not the search query has changed since the last time the loader was refreshed
    private boolean                       mSearchQueryChanged;

    // Whether or not this fragment is showing in a two-pane layout
    private boolean                       mIsTwoPaneLayout;

    /**
     * Fragments require an empty constructor.
     */
    public FragmentContact() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if this fragment is part of a two-pane set up or a single pane by reading a
        // boolean from the application resource directories. This lets allows us to easily specify
        // which screen sizes should use a two-pane layout by setting this boolean in the
        // corresponding resource size-qualified directory.
        mIsTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);

        // Let this fragment contribute menu items
        setHasOptionsMenu(true);

        // Create the main contacts adapter
        mAdapter = new ContactsAdapter(getActivity());

        if (savedInstanceState != null) {
            // If we're restoring state after this fragment was recreated then
            // retrieve previous search term and previously selected search
            // result.
            mSearchTerm = savedInstanceState.getString(SearchManager.QUERY);
            mPreviouslySelectedSearchItem =
                    savedInstanceState.getInt(STATE_PREVIOUSLY_SELECTED_KEY, 0);
        }

        mHomeActionListener = (ActivityHome) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the list fragment layout
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        mListContact = (SwipeMenuListView) view.findViewById(R.id.list_contacts);
        mTextNoContact = (TextView) view.findViewById(R.id.text_no_contact);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up ListView, assign adapter and set some listeners. The adapter was previously
        // created in onCreate().
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

        mListContact.setMenuCreator(creator);
        mListContact.setAdapter(mAdapter);
        mListContact.setOnItemClickListener(this);
        mListContact.setOnMenuItemClickListener(this);
        mListContact.setOnItemLongClickListener(this);

        if (mIsTwoPaneLayout) {
            // In a two-pane layout, set choice mode to single as there will be two panes
            // when an item in the ListView is selected it should remain highlighted while
            // the content shows in the second pane.
            mListContact.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

        // If there's a previously selected search item from a saved state then don't bother
        // initializing the loader as it will be restarted later when the query is populated into
        // the action bar search view (see onQueryTextChange() in onCreateOptionsMenu()).
        if (mPreviouslySelectedSearchItem == 0) {
            // Initialize the loader, and create a loader identified by ContactsQuery.QUERY_ID
            getLoaderManager().initLoader(ContactsQuery.QUERY_ID, null, this);
        }
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        Log.d("KieuThang", "position:" + position + ",index:" + index);
        switch (index) {
        case 0:
            mAdapter.onClickCallContact(position);
            break;
        case 1:
            mAdapter.onClickSMSContact(position);
            break;
        case 2:

            break;
        default:
            break;
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            // Assign callback listener which the holding activity must implement. This is used
            // so that when a contact item is interacted with (selected by the user) the holding
            // activity will be notified and can take further action such as populating the contact
            // detail pane (if in multi-pane layout) or starting a new activity with the contact
            // details (single pane layout).
            // mOnContactSelectedListener = (OnContactsInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnContactsInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Gets the Cursor object currently bound to the ListView
        Log.d("KieuThang", "onItemClick");
        final Cursor cursor = mAdapter.getCursor();

        // Moves to the Cursor row corresponding to the ListView item that was clicked
        cursor.moveToPosition(position);

        // Creates a contact lookup Uri from contact ID and lookup_key
        final Uri uri = Contacts.getLookupUri(cursor.getLong(ContactsQuery.ID), cursor.getString(ContactsQuery.LOOKUP_KEY));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        if (mIsTwoPaneLayout) {
            mListContact.setItemChecked(position, true);
        }
    }

    /**
     * Called when ListView selection is cleared, for example
     * when search mode is finished and the currently selected
     * contact should no longer be selected.
     */
    private void onSelectionCleared() {
        // Uses callback to notify activity this contains this fragment
        mOnContactSelectedListener.onSelectionCleared();

        // Clears currently checked item
        mListContact.clearChoices();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("KieuThang", "onSaveInstanceState:" + mSearchTerm);
        if (!TextUtils.isEmpty(mSearchTerm)) {
            // Saves the current search string
            outState.putString(SearchManager.QUERY, mSearchTerm);

            // Saves the currently selected contact
            outState.putInt(STATE_PREVIOUSLY_SELECTED_KEY, mListContact.getCheckedItemPosition());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("KieuThang", "onCreateLoader:" + mSearchTerm);
        // If this is the loader for finding contacts in the Contacts Provider
        // (the only one supported)
        if (id == ContactsQuery.QUERY_ID) {
            Uri contentUri;

            // There are two types of searches, one which displays all contacts and
            // one which filters contacts by a search query. If mSearchTerm is set
            // then a search query has been entered and the latter should be used.

            if (mSearchTerm == null) {
                // Since there's no search string, use the content URI that searches the entire
                // Contacts table
                contentUri = ContactsQuery.CONTENT_URI;
            } else {
                // Since there's a search string, use the special content Uri that searches the
                // Contacts table. The URI consists of a base Uri and the search string.
                contentUri =
                        Uri.withAppendedPath(ContactsQuery.FILTER_URI, Uri.encode(mSearchTerm));
            }

            // Returns a new CursorLoader for querying the Contacts table. No arguments are used
            // for the selection clause. The search string is either encoded onto the content URI,
            // or no contacts search string is used. The other search criteria are constants. See
            // the ContactsQuery interface.
            return new CursorLoader(getActivity(),
                    contentUri,
                    ContactsQuery.PROJECTION,
                    ContactsQuery.SELECTION,
                    null,
                    ContactsQuery.SORT_ORDER);
        }

        Log.e(TAG, "onCreateLoader - incorrect ID provided (" + id + ")");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // This swaps the new cursor into the adapter.
        if (loader.getId() == ContactsQuery.QUERY_ID) {
            mAdapter.swapCursor(data);

            // If this is a two-pane layout and there is a search query then
            // there is some additional work to do around default selected
            // search item.
            if (mIsTwoPaneLayout && !TextUtils.isEmpty(mSearchTerm) && mSearchQueryChanged) {
                // Selects the first item in results, unless this fragment has
                // been restored from a saved state (like orientation change)
                // in which case it selects the previously selected search item.
                if (data != null && data.moveToPosition(mPreviouslySelectedSearchItem)) {
                    // Creates the content Uri for the previously selected contact by appending the
                    // contact's ID to the Contacts table content Uri
                    final Uri uri = Uri.withAppendedPath(
                            Contacts.CONTENT_URI, String.valueOf(data.getLong(ContactsQuery.ID)));
                    mOnContactSelectedListener.onContactSelected(uri);
                    mListContact.setItemChecked(mPreviouslySelectedSearchItem, true);
                } else {
                    // No results, clear selection.
                    onSelectionCleared();
                }
                // Only restore from saved state one time. Next time fall back
                // to selecting first item. If the fragment state is saved again
                // then the currently selected item will once again be saved.
                mPreviouslySelectedSearchItem = 0;
                mSearchQueryChanged = false;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == ContactsQuery.QUERY_ID) {
            // When the loader is being reset, clear the cursor from the adapter. This allows the
            // cursor resources to be freed.
            mAdapter.swapCursor(null);
        }
    }

    /**
     * This is a subclass of CursorAdapter that supports binding Cursor columns to a view layout.
     * If those items are part of search results, the search string is marked by highlighting the
     * query text. An {@link AlphabetIndexer} is used to allow quicker navigation up and down the
     * ListView.
     */
    private class ContactsAdapter extends CursorAdapter implements SectionIndexer {
        private LayoutInflater     mInflater;        // Stores the layout inflater
        private AlphabetIndexer    mAlphabetIndexer; // Stores the AlphabetIndexer instance
        private TextAppearanceSpan highlightTextSpan; // Stores the highlight text appearance style

        /**
         * Instantiates a new Contacts Adapter.
         * @param context A context that has access to the app's layout.
         */
        public ContactsAdapter(Context context) {
            super(context, null, 0);

            // Stores inflater for use later
            mInflater = LayoutInflater.from(context);
            final String alphabet = context.getString(R.string.alphabet);
            mAlphabetIndexer = new AlphabetIndexer(getCursor(), ContactsQuery.SORT_KEY, alphabet);
            highlightTextSpan = new TextAppearanceSpan(getActivity(), R.style.searchTextHiglight);
        }

        public void onClickSMSContact(int position) {
            Cursor cursor = mAdapter.getCursor();

            // Moves to the Cursor row corresponding to the ListView item that was clicked
            cursor.moveToPosition(position);

            String contactId = cursor.getString(ContactsQuery.ID);
            if (StringUtility.isEmpty(contactId))
                return;

            Log.d("KieuThang", " getCursor().getPosition:" + cursor.getPosition());
            Log.d("KieuThang", " getCursor().getString(ContactsQuery.ID):" + cursor.getString(ContactsQuery.ID));
            Log.d("KieuThang", " getCursor().getString(ContactsQuery.DISPLAY_NAME):" + cursor.getString(ContactsQuery.DISPLAY_NAME));

            ArrayList<String> phones = Utils.getContactUriTypeFromContactId(getActivity().getContentResolver(), contactId);
            if (phones == null || phones.size() == 0) {
                Toast.makeText(getActivity(), "No phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phones.get(0)));
            startActivity(intent);
        }

        public void onClickCallContact(int position) {
            Cursor cursor = mAdapter.getCursor();

            // Moves to the Cursor row corresponding to the ListView item that was clicked
            cursor.moveToPosition(position);

            String contactId = cursor.getString(ContactsQuery.ID);
            if (StringUtility.isEmpty(contactId))
                return;

            Log.d("KieuThang", " getCursor().getPosition:" + cursor.getPosition());
            Log.d("KieuThang", " getCursor().getString(ContactsQuery.ID):" + cursor.getString(ContactsQuery.ID));
            Log.d("KieuThang", " getCursor().getString(ContactsQuery.DISPLAY_NAME):" + cursor.getString(ContactsQuery.DISPLAY_NAME));

            ArrayList<String> phones = Utils.getContactUriTypeFromContactId(getActivity().getContentResolver(), contactId);
            if (phones == null || phones.size() == 0) {
                Toast.makeText(getActivity(), "No phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phones.get(0)));
            startActivity(intent);

        }

        /**
         * Identifies the start of the search string in the display name column of a Cursor row.
         * E.g. If displayName was "Adam" and search query (mSearchTerm) was "da" this would
         * return 1.
         *
         * @param displayName The contact display name.
         * @return The starting position of the search string in the display name, 0-based. The
         * method returns -1 if the string is not found in the display name, or if the search
         * string is empty or null.
         */
        private int indexOfSearchQuery(String displayName) {
            if (!TextUtils.isEmpty(mSearchTerm)) {
                return displayName.toLowerCase(Locale.getDefault()).indexOf(
                        mSearchTerm.toLowerCase(Locale.getDefault()));
            }
            return -1;
        }

        /**
         * Overrides newView() to inflate the list item views.
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            final View itemLayout = mInflater.inflate(R.layout.contact_list_item, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            holder.textAlphabet = (TextView) itemLayout.findViewById(R.id.text_alphabet_character);
            holder.text1 = (TextView) itemLayout.findViewById(R.id.text_phone_name);
            holder.text1.setSelected(true);
            holder.text2 = (TextView) itemLayout.findViewById(R.id.text_match_other_field);
            holder.icon = (CircleImageView) itemLayout.findViewById(android.R.id.icon);
            holder.divider = (View) itemLayout.findViewById(R.id.divider);
            holder.btnCall = (ImageButton) itemLayout.findViewById(R.id.btn_call);
            holder.btnCall.setSelected(false);
            itemLayout.setTag(holder);
            // Returns the item layout view
            return itemLayout;
        }

        /**
         * Binds data from the Cursor to the provided view.
         */
        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            // Gets handles to individual view resources
            final ViewHolder holder = (ViewHolder) view.getTag();

            // For Android 3.0 and later, gets the thumbnail image Uri from the current Cursor row.
            // For platforms earlier than 3.0, this isn't necessary, because the thumbnail is
            // generated from the other fields in the row.
            if (getCount() == 0) {
                mTextNoContact.setVisibility(View.VISIBLE);
            } else
                mTextNoContact.setVisibility(View.GONE);
            final String photoUri = cursor.getString(ContactsQuery.PHOTO_THUMBNAIL_DATA);

            final String displayName = cursor.getString(ContactsQuery.DISPLAY_NAME);

            final int startIndex = indexOfSearchQuery(displayName);
            final int position = cursor.getPosition();
            Log.d("KieuThang", "getSesseion:" + mAlphabetIndexer.getSectionForPosition(cursor.getPosition()));
            final int sessionPosition = mAlphabetIndexer.getSectionForPosition(cursor.getPosition());
            int positionOfSession = mAlphabetIndexer.getPositionForSection(sessionPosition);
            if (positionOfSession == cursor.getPosition()) {
                holder.textAlphabet.setVisibility(View.VISIBLE);
                holder.divider.setVisibility(View.VISIBLE);
                final String alphabet = context.getString(R.string.alphabet);
                char alphabetCharacter = alphabet.charAt(sessionPosition);
                Log.d("KieuThang", "alphabetCharacter:" + alphabetCharacter);
                holder.textAlphabet.setText(alphabetCharacter + "");

            } else {
                holder.divider.setVisibility(View.GONE);
                holder.textAlphabet.setVisibility(View.GONE);
            }
            if (startIndex == -1) {
                holder.text1.setText(displayName);

                if (TextUtils.isEmpty(mSearchTerm)) {
                    holder.text2.setVisibility(View.GONE);
                } else {
                    holder.text2.setVisibility(View.VISIBLE);
                }
            } else {
                final SpannableString highlightedName = new SpannableString(displayName);
                highlightedName.setSpan(highlightTextSpan, startIndex, startIndex + mSearchTerm.length(), 0);

                holder.text1.setText(highlightedName);

                holder.text2.setVisibility(View.GONE);
            }

            final Uri contactUri = Contacts.getLookupUri(cursor.getLong(ContactsQuery.ID), cursor.getString(ContactsQuery.LOOKUP_KEY));
            if (photoUri != null) {
                holder.icon.setImageURI(Uri.parse(photoUri));
            } else {
                holder.icon.setImageResource(R.drawable.ic_contact_picture_holo_light);
            }
            holder.icon.assignContactUri(contactUri);

            holder.btnCall.setFocusable(false);
            holder.btnCall.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickCallContact(position);
                }
            });
        }

        /**
         * Overrides swapCursor to move the new Cursor into the AlphabetIndex as well as the
         * CursorAdapter.
         */
        @Override
        public Cursor swapCursor(Cursor newCursor) {
            // Update the AlphabetIndexer with new cursor as well
            mAlphabetIndexer.setCursor(newCursor);
            return super.swapCursor(newCursor);
        }

        /**
         * An override of getCount that simplifies accessing the Cursor. If the Cursor is null,
         * getCount returns zero. As a result, no test for Cursor == null is needed.
         */
        @Override
        public int getCount() {
            if (getCursor() == null) {
                return 0;
            }
            return super.getCount();
        }

        /**
         * Defines the SectionIndexer.getSections() interface.
         */
        @Override
        public Object[] getSections() {
            return mAlphabetIndexer.getSections();
        }

        /**
         * Defines the SectionIndexer.getPositionForSection() interface.
         */
        @Override
        public int getPositionForSection(int i) {
            if (getCursor() == null) {
                return 0;
            }
            return mAlphabetIndexer.getPositionForSection(i);
        }

        /**
         * Defines the SectionIndexer.getSectionForPosition() interface.
         */
        @Override
        public int getSectionForPosition(int i) {
            if (getCursor() == null) {
                return 0;
            }
            return mAlphabetIndexer.getSectionForPosition(i);
        }

        /**
         * A class that defines fields for each resource ID in the list item layout. This allows
         * ContactsAdapter.newView() to store the IDs once, when it inflates the layout, instead of
         * calling findViewById in each iteration of bindView.
         */
        private class ViewHolder {
            TextView        textAlphabet;
            TextView        text1;
            TextView        text2;
            CircleImageView icon;
            View            divider;
            ImageButton     btnCall;
        }
    }

    /**
     * This interface must be implemented by any activity that loads this fragment. When an
     * interaction occurs, such as touching an item from the ListView, these callbacks will
     * be invoked to communicate the event back to the activity.
     */
    public interface OnContactsInteractionListener {
        /**
         * Called when a contact is selected from the ListView.
         * @param contactUri The contact Uri.
         */
        public void onContactSelected(Uri contactUri);

        /**
         * Called when the ListView selection is cleared like when
         * a contact search is taking place or is finishing.
         */
        public void onSelectionCleared();
    }

    /**
     * This interface defines constants for the Cursor and CursorLoader, based on constants defined
     * in the {@link android.provider.ContactsContract.Contacts} class.
     */
    public interface ContactsQuery {

        // An identifier for the loader
        final static int      QUERY_ID             = 1;

        // A content URI for the Contacts table
        final static Uri      CONTENT_URI          = Contacts.CONTENT_URI;

        // The search/filter query Uri
        final static Uri      FILTER_URI           = Contacts.CONTENT_FILTER_URI;

        // The selection clause for the CursorLoader query. The search criteria defined here
        // restrict results to contacts that have a display name and are linked to visible groups.
        // Notice that the search on the string provided by the user is implemented by appending
        // the search string to CONTENT_FILTER_URI.
        @SuppressLint("InlinedApi")
        final static String   SELECTION            =
                                                           (Utils.hasHoneycomb() ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME) +
                                                                   "<>''" + " AND " + Contacts.IN_VISIBLE_GROUP + "=1";

        // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
        // sort key allows for localization. In earlier versions. use the display name as the sort
        // key.
        @SuppressLint("InlinedApi")
        final static String   SORT_ORDER           =
                                                           Utils.hasHoneycomb() ? Contacts.SORT_KEY_PRIMARY : Contacts.DISPLAY_NAME;

        // The projection for the CursorLoader query. This is a list of columns that the Contacts
        // Provider should return in the Cursor.
        @SuppressLint("InlinedApi")
        final static String[] PROJECTION           = {

                                                           // The contact's row id
                                                           Contacts._ID,

                                                           // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
                                                           // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
                                                           // a "permanent" contact URI.
                                                           Contacts.LOOKUP_KEY,

                                                           // In platform version 3.0 and later, the Contacts table contains
                                                           // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
                                                           // some other useful identifier such as an email address. This column isn't
                                                           // available in earlier versions of Android, so you must use Contacts.DISPLAY_NAME
                                                           // instead.
                                                           Utils.hasHoneycomb() ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME,

                                                           // In Android 3.0 and later, the thumbnail image is pointed to by
                                                           // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
                                                           // you generate the pointer from the contact's ID value and constants defined in
                                                           // android.provider.ContactsContract.Contacts.
                                                           Utils.hasHoneycomb() ? Contacts.PHOTO_THUMBNAIL_URI : Contacts._ID,

                                                           // The sort order column for the returned Cursor, used by the AlphabetIndexer
                                                           SORT_ORDER,

                                                   };

        // The query column numbers which map to each value in the projection
        final static int      ID                   = 0;
        final static int      LOOKUP_KEY           = 1;
        final static int      DISPLAY_NAME         = 2;
        final static int      PHOTO_THUMBNAIL_DATA = 3;
        final static int      SORT_KEY             = 4;
    }

    public void onQueryTextChange(CharSequence s) {
        String searchText = s == null ? "" : s.toString();
        mSearchTerm = searchText;
        // In version 3.0 and later, sets up and configures the ActionBar SearchView
        if (Utils.hasHoneycomb()) {
            String newFilter = !TextUtils.isEmpty(mSearchTerm) ? mSearchTerm : null;

            // Don't do anything if the filter is empty
            if (mSearchTerm == null && newFilter == null) {
                Log.d("KieuThang", "newFilter == null");
                return;
            }

            // Updates current filter to new filter
            mSearchTerm = newFilter;
            // necessary content Uri from mSearchTerm.
            mSearchQueryChanged = true;
            getLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, FragmentContact.this);
            return;
        }

        if (Utils.hasICS()) {
            // When the user collapses the SearchView the current search string is
            // cleared and the loader restarted.
            if (!TextUtils.isEmpty(mSearchTerm)) {
                onSelectionCleared();
            }
            mSearchTerm = null;
            getLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, FragmentContact.this);
            return;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("KieuThang", "onItemLongClick");
        final Cursor cursor = mAdapter.getCursor();

        // Moves to the Cursor row corresponding to the ListView item that was clicked
        cursor.moveToPosition(position);

        // Creates a contact lookup Uri from contact ID and lookup_key
        return false;
    }
}
