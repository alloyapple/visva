package com.samsung.android.alwayssocial.object.twitter;

import java.util.ArrayList;
import java.util.List;
import com.samsung.android.alwayssocial.object.ResponseBase;

import twitter4j.SavedSearch;

public class TwitterSavedSearchesData extends ResponseBase {
    private List<SavedSearch> mListSavedSearches;
    
    public TwitterSavedSearchesData(){
        mListSavedSearches = new ArrayList<SavedSearch>();
    }
    
    public TwitterSavedSearchesData(List<SavedSearch> listSavedSearches){
        this.mListSavedSearches = listSavedSearches;
    }

    public List<SavedSearch> getListSavedSearches() {
        return mListSavedSearches;
    }

    public void setListSavedSearches(List<SavedSearch> mListSavedSearches) {
        this.mListSavedSearches = mListSavedSearches;
    }

}
