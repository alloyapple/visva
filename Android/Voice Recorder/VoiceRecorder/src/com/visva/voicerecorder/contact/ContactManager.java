package com.visva.voicerecorder.contact;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactManager {
    private static ContactManager mInstance;
    private Context mContext;

    /**
     * RequestWrapper constructor
     * 
     * @param context
     */
    public ContactManager(Context context) {
        super();
        mContext = context;
    }

    /**
     * get instance of RequestWrapper singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized ContactManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new ContactManager(context);
        return mInstance;
    }

    public ArrayList<ContactItem> getAllContact() {
        ArrayList<ContactItem> list = new ArrayList<ContactItem>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        Cursor people = mContext.getContentResolver().query(uri, projection, null, null, null);
        if(people == null)
            return null;
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();
        do {
            String name = people.getString(indexName);
            String number = people.getString(indexNumber);
            list.add(new ContactItem(name, number));

        } while (people.moveToNext());
        
        if(people != null){
        	people.close();
        	people = null;
        }
        return list;
    }
}
