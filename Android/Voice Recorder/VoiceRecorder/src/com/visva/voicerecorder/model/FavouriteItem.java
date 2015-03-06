package com.visva.voicerecorder.model;

/**
 * A class represent for a recorded session
 */
public class FavouriteItem {
    public String phoneNo;
    public String phoneName;
    public int    isFavourite;
    public String contactId;

    public FavouriteItem(String phoneNo, String phoneName, int isFavourite, String contactId) {
        this.phoneNo = phoneNo;
        this.phoneName = phoneName;
        this.isFavourite = isFavourite;
        this.contactId = contactId;
    }

    public FavouriteItem() {
    }
}