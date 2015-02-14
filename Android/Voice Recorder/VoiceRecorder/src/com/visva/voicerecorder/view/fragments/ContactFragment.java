package com.visva.voicerecorder.view.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visva.voicerecorder.contact.ContactAdapter;
import com.visva.voicerecorder.contact.ContactItem;
import com.visva.voicerecorder.contact.ContactListView;
import com.visva.voicerecorder.contact.ContactManager;

/**
 * A fragment that display all the recording sessions
 */

public class ContactFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        ViewGroup vg = (ViewGroup) getView();
        vg.removeAllViews();
        vg.addView(this.getListView());
    }

    private View getListView() {
        ArrayList<ContactItem> contactList = ContactManager.getInstance(getActivity()).getAllContact();
        ContactListView listView = new ContactListView(getActivity());
        ContactAdapter adapter = new ContactAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, contactList);
        listView.setAdapter(adapter);
        return listView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return this.getListView();
    }
}