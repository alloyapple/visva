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

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.widgets.Dialog;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.utils.ContactsQuery;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;
import com.visva.voicerecorder.view.common.FragmentBasic;
import com.visva.voicerecorder.view.widget.CircleImageView;
import com.visva.voicerecorder.view.widget.quickaction.ActionItem;
import com.visva.voicerecorder.view.widget.quickaction.QuickAction;

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
        AdapterView.OnItemLongClickListener {
    // ======================Constant Define=====================

    // Bundle key for saving previously selected search result item
    private static final String           STATE_PREVIOUSLY_SELECTED_KEY =
                                                                                "com.example.android.contactslist.ui.SELECTED_ITEM";
    // ======================Control Define =====================
    private ListView                      mListContact;
    private TextView                      mTextNoContact;
    private QuickAction                   mQuickAction;
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

    private boolean                       mIsOnItemLongClick;

    private int                           mSelectedPosition;

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
            mPreviouslySelectedSearchItem = savedInstanceState.getInt(STATE_PREVIOUSLY_SELECTED_KEY, 0);
        }

        iHomeActionListener = (ActivityHome) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the list fragment layout
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        mListContact = (ListView) view.findViewById(R.id.list_contacts);
        mTextNoContact = (TextView) view.findViewById(R.id.text_no_contact);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListContact.setAdapter(mAdapter);
        mListContact.setOnItemClickListener(this);
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

        initQuickAction();
    }

    private void initQuickAction() {
        String editTitle = getActivity().getString(R.string.edit);
        String deleteTitle = getActivity().getString(R.string.delete);
        String favouriteTitle = getActivity().getString(R.string.favourite);
        String shareTitle = getActivity().getString(R.string.share);

        ActionItem editAction = new ActionItem();
        editAction.setTitle(editTitle);
        editAction.setIcon(getResources().getDrawable(R.drawable.ic_action_edit));

        ActionItem deleteAction = new ActionItem();
        deleteAction.setTitle(deleteTitle);
        deleteAction.setIcon(getResources().getDrawable(R.drawable.delete_button));

        ActionItem favouriteAction = new ActionItem();
        favouriteAction.setTitle(favouriteTitle);
        favouriteAction.setIcon(getResources().getDrawable(R.drawable.ic_star_outline_grey600_24dp));

        ActionItem shareAction = new ActionItem();
        shareAction.setTitle(shareTitle);
        shareAction.setIcon(getResources().getDrawable(R.drawable.ic_share_variant_grey600_48dp));

        mQuickAction = new QuickAction(getActivity());

        mQuickAction.addActionItem(editAction);
        mQuickAction.addActionItem(deleteAction);
        mQuickAction.addActionItem(favouriteAction);
        mQuickAction.addActionItem(shareAction);

        // setup the action item click listener
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                switch (pos) {
                case 0:
                    editThisContact(mSelectedPosition);
                    break;
                case 1:
                    deleteThisContact(mSelectedPosition);
                    break;
                case 2:
                    updateThisContactFavourite(mSelectedPosition);
                    break;
                case 3:
                    shareThisContactAction(mSelectedPosition);
                    break;
                default:
                    break;
                }
            }
        });
        mQuickAction.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                mIsOnItemLongClick = false;
            }
        });
    }

    private void shareThisContactAction(int selectedPosition) {
        Cursor cursor = mAdapter.getCursor();

        // Moves to the Cursor row corresponding to the ListView item that was clicked
        cursor.moveToPosition(selectedPosition);

        String contactId = cursor.getString(ContactsQuery.ID);
        if (StringUtility.isEmpty(contactId))
            return;

        ArrayList<String> phones = Utils.getContactUriTypeFromContactId(getActivity().getContentResolver(), contactId);
        String phone = "";
        if (phones == null || phones.size() == 0) {
            phone = "";
        } else
            phone = phones.get(0);
        String displayName = cursor.getString(ContactsQuery.DISPLAY_NAME);
        Resources res = getActivity().getResources();
        StringBuilder builder = new StringBuilder();
        if (!phone.equals(displayName))
            builder.append(res.getString(R.string.name)).append(displayName + "\n");

        if (!StringUtility.isEmpty(phone)) {
            builder.append(res.getString(R.string.phone_no)).append(phone);
        }
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, res.getString(R.string.share_contact));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, builder.toString());
        startActivity(Intent.createChooser(sharingIntent, displayName));
    }

    // This method will check this contact is favourite contact or not first,
    // the checking action includes checking in the contact app. After that, it will add or 
    // remove (if the contact is already existed in favourites) in the contact and my call recorder database application
    private void updateThisContactFavourite(int selectedPosition) {
        final Cursor cursor = mAdapter.getCursor();
        Resources res = getActivity().getResources();
        if (cursor == null)
            return;
        // Moves to the Cursor row corresponding to the ListView item that was clicked
        cursor.moveToPosition(selectedPosition);
        if (mSQLiteHelper == null) {
            mSQLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(getActivity());
        }

        String contactId = cursor.getString(ContactsQuery.ID);
        if (StringUtility.isEmpty(contactId))
            return;

        ArrayList<String> phones = Utils.getContactUriTypeFromContactId(getActivity().getContentResolver(), contactId);
        String phoneNo = "";
        if (phones == null || phones.size() == 0) {
            phoneNo = "";
        } else
            phoneNo = phones.get(0);
        String phoneName = cursor.getString(ContactsQuery.DISPLAY_NAME);
        FavouriteItem favouriteItem = new FavouriteItem(phoneNo, phoneName, 1, contactId);
        if (Utils.isCheckFavouriteContact(getActivity(), contactId)) {
            String removedFavouriteContact = res.getString(R.string.removed_from_favourite, phoneName);
            mSQLiteHelper.deleteFavouriteItem(favouriteItem);
            Toast.makeText(getActivity(), removedFavouriteContact, Toast.LENGTH_SHORT).show();
        }
        else {
            String addFavouriteContact = res.getString(R.string.added_to_favourite, phoneName);
            mSQLiteHelper.addNewFavoriteItem(favouriteItem);
            Toast.makeText(getActivity(), addFavouriteContact, Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void deleteThisContact(int selectedPosition) {
        String deleteTitle = getActivity().getString(R.string.delete);
        String contentMsg = getActivity().getString(R.string.are_you_sure_to_delete_contact);
        String cancel = getActivity().getString(R.string.cancel);
        final Cursor cursor = mAdapter.getCursor();
        if (cursor == null)
            return;
        // Moves to the Cursor row corresponding to the ListView item that was clicked
        cursor.moveToPosition(selectedPosition);

        // Creates a contact lookup Uri from contact ID and lookup_key
        final Uri uri = Contacts.getLookupUri(cursor.getLong(ContactsQuery.ID), cursor.getString(ContactsQuery.LOOKUP_KEY));

        Dialog dialog = new Dialog(getActivity(), deleteTitle, contentMsg);
        dialog.addCancelButton(cancel, new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.deleteContact(getActivity(), uri);
            }
        });

        dialog.getButtonAccept();

        dialog.show();
    }

    private void editThisContact(int selectedPosition) {
        final Cursor cursor = mAdapter.getCursor();
        if (cursor == null)
            return;
        cursor.moveToPosition(selectedPosition);

        final Uri uri = Contacts.getLookupUri(cursor.getLong(ContactsQuery.ID), cursor.getString(ContactsQuery.LOOKUP_KEY));

        // Standard system edit contact intent
        Intent intent = new Intent(Intent.ACTION_EDIT, uri);

        intent.putExtra("finishActivityOnSaveCompleted", true);

        // Start the edit activity
        startActivity(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Gets the Cursor object currently bound to the ListView
        if (mIsOnItemLongClick) {
            mIsOnItemLongClick = false;
            return;
        }
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
        if (!TextUtils.isEmpty(mSearchTerm)) {
            // Saves the current search string
            outState.putString(SearchManager.QUERY, mSearchTerm);

            // Saves the currently selected contact
            outState.putInt(STATE_PREVIOUSLY_SELECTED_KEY, mListContact.getCheckedItemPosition());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
                contentUri = Uri.withAppendedPath(ContactsQuery.FILTER_URI, Uri.encode(mSearchTerm));
            }

            // Returns a new CursorLoader for querying the Contacts table. No arguments are used
            // for the selection clause. The search string is either encoded onto the content URI,
            // or no contacts search string is used. The other search criteria are constants. See
            // the ContactsQuery interface.
            return new CursorLoader(getActivity(), contentUri, ContactsQuery.PROJECTION, ContactsQuery.SELECTION, null, ContactsQuery.SORT_ORDER);
        }
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

        public void onClickCallContact(int position) {
            Cursor cursor = mAdapter.getCursor();

            // Moves to the Cursor row corresponding to the ListView item that was clicked
            cursor.moveToPosition(position);

            String contactId = cursor.getString(ContactsQuery.ID);
            if (StringUtility.isEmpty(contactId))
                return;

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
            holder.textPhoneName = (TextView) itemLayout.findViewById(R.id.text_phone_name);
            holder.textPhoneName.setSelected(true);
            holder.textMatchOtherField = (TextView) itemLayout.findViewById(R.id.text_match_other_field);
            holder.icon = (CircleImageView) itemLayout.findViewById(android.R.id.icon);
            holder.divider = (View) itemLayout.findViewById(R.id.divider);
            holder.layoutCall = (LayoutRipple) itemLayout.findViewById(R.id.layout_call);
            holder.icStar = (ImageView) itemLayout.findViewById(R.id.ic_star);
            holder.dividerCall = (View) itemLayout.findViewById(R.id.divider_call);
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
            final String contactId = cursor.getString(ContactsQuery.ID);
            final String displayName = cursor.getString(ContactsQuery.DISPLAY_NAME);

            final int startIndex = indexOfSearchQuery(displayName);
            final int position = cursor.getPosition();
            final int sessionPosition = mAlphabetIndexer.getSectionForPosition(cursor.getPosition());
            int positionOfSession = mAlphabetIndexer.getPositionForSection(sessionPosition);
            if (positionOfSession == cursor.getPosition()) {
                holder.textAlphabet.setVisibility(View.VISIBLE);
                holder.divider.setVisibility(View.VISIBLE);
                final String alphabet = context.getString(R.string.alphabet);
                char alphabetCharacter = alphabet.charAt(sessionPosition);
                holder.textAlphabet.setText(alphabetCharacter + "");

            } else {
                holder.divider.setVisibility(View.GONE);
                holder.textAlphabet.setVisibility(View.GONE);
            }
            if (startIndex == -1) {
                holder.textPhoneName.setText(displayName);

                if (TextUtils.isEmpty(mSearchTerm)) {
                    holder.textMatchOtherField.setVisibility(View.GONE);
                } else {
                    holder.textMatchOtherField.setVisibility(View.VISIBLE);
                }
            } else {
                final SpannableString highlightedName = new SpannableString(displayName);
                highlightedName.setSpan(highlightTextSpan, startIndex, startIndex + mSearchTerm.length(), 0);

                holder.textPhoneName.setText(highlightedName);

                holder.textMatchOtherField.setVisibility(View.GONE);
            }

            final Uri contactUri = Contacts.getLookupUri(cursor.getLong(ContactsQuery.ID), cursor.getString(ContactsQuery.LOOKUP_KEY));
            if (photoUri != null) {
                holder.icon.setImageURI(Uri.parse(photoUri));
            } else {
                holder.icon.setImageResource(R.drawable.ic_contact_picture_holo_light);
            }
            holder.icon.assignContactUri(contactUri);

            ArrayList<String> phones = Utils.getContactUriTypeFromContactId(getActivity().getContentResolver(), contactId);
            String phoneNo = "";
            if (phones == null || phones.size() == 0) {
                phoneNo = "";
            } else
                phoneNo = phones.get(0);
            if ("".equals(phoneNo)) {
                holder.layoutCall.setVisibility(View.GONE);
                holder.dividerCall.setVisibility(View.GONE);
            } else {
                holder.layoutCall.setVisibility(View.VISIBLE);
                holder.dividerCall.setVisibility(View.VISIBLE);
            }
            holder.layoutCall.setFocusable(false);
            holder.layoutCall.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickCallContact(position);
                }
            });

            boolean isFavourite = Utils.isCheckFavouriteContact(getActivity(), contactId);
            if (isFavourite)
                holder.icStar.setVisibility(View.VISIBLE);
            else
                holder.icStar.setVisibility(View.GONE);
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
            TextView        textPhoneName;
            TextView        textMatchOtherField;
            CircleImageView icon;
            View            divider;
            ImageView       icStar;
            View            dividerCall;
            LayoutRipple    layoutCall;
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

    public void onQueryTextChange(CharSequence s) {
        String searchText = s == null ? "" : s.toString();
        mSearchTerm = searchText;
        // In version 3.0 and later, sets up and configures the ActionBar SearchView
        if (Utils.hasHoneycomb()) {
            String newFilter = !TextUtils.isEmpty(mSearchTerm) ? mSearchTerm : null;

            // Don't do anything if the filter is empty
            if (mSearchTerm == null && newFilter == null) {
                AIOLog.e(MyCallRecorderConstant.TAG, "newFilter == null");
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
        mIsOnItemLongClick = true;
        mSelectedPosition = position;

        CircleImageView image = (CircleImageView) view.findViewById(android.R.id.icon);
        if (mQuickAction == null)
            return false;
        mQuickAction.show(view, image);
        return false;
    }
}
