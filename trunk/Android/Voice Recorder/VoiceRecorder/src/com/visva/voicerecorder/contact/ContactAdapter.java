package com.visva.voicerecorder.contact;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;

/**
 * An adapter used for the list view
 */
public class ContactAdapter extends ArrayAdapter<ContactItem> {
    private static final String TAG = MyCallRecorderConstant.TAG;
    private LayoutInflater mLayoutInflater;
    private ArrayList<ContactItem> mContactList;

    public ContactAdapter(Context context, int textViewResourceId, ArrayList<ContactItem> contactLists) {
        super(context, textViewResourceId, contactLists);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContactList = contactLists;
    }

    public Uri getContactPhotoFromNumber(ContentResolver resolver, String phoneNo) {
        Uri result = null;
        if (phoneNo == "" || phoneNo == "null") {
            phoneNo = "111111111";
        }
        String[] projection = { ContactsContract.Contacts._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.PHOTO_URI };
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor cursor = resolver.query(lookupUri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            if (cursor.getString(3) != null)
                result = Uri.parse(cursor.getString(3));
        }
        cursor.close();
        return result;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.recorder_item, null);
        }
        ContactItem contactItem = mContactList.get(position);
        if(contactItem == null){
            Log.e(TAG, "contactItem is null");
            return null;
        }
        TextView phoneTextView = (TextView) convertView.findViewById(R.id.phoneTextView);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTimeTextView);
        ImageView profileImage = (ImageView) convertView.findViewById(R.id.imageProfile);
        ImageView callStateImage = (ImageView) convertView.findViewById(R.id.callIndicator);
        phoneTextView.setText(contactItem.number);
        Uri photoUri = this.getContactPhotoFromNumber(getContext().getContentResolver(), contactItem.number);
        if (photoUri != null) {
            profileImage.setImageURI(photoUri);
            profileImage.getLayoutParams().width = 68;
            profileImage.getLayoutParams().height = 68;
        } else {
            profileImage.setImageResource(R.drawable.default_contact_icon);
        }
        return convertView;
    }

    public void removeAt(int position) {
    }
}
