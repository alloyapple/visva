package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FacebookLikedPageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StoryItemUnit> mListLikedPages;


    public FacebookLikedPageAdapter(Context context, ArrayList<StoryItemUnit> listLikedPages) {
        this.mContext = context;
        this.mListLikedPages = listLikedPages;
    }

    @Override
    public int getCount() {
        return mListLikedPages.size();
    }

    @Override
    public StoryItemUnit getItem(int index) {
        return mListLikedPages.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup arg2) {
        int numberFan = 0;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.liked_pages_item, null);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.fb_likedpage_name);
        TextView txtCategory = (TextView) convertView.findViewById(R.id.txt_page_category);
        txtName.setText(mListLikedPages.get(index).getTitle());
        try{
            numberFan = Integer.valueOf(mListLikedPages.get(index).getMessage()); 
        }catch(Exception e){
            
        }
        if (numberFan > 0) {
            txtCategory.setText(mListLikedPages.get(index).getMessage() + " fans");
        }
        else {
            txtCategory.setText(mListLikedPages.get(index).getMessage());
        }

        return convertView;
    }

    public void removeItem(int index) {
        mListLikedPages.remove(index);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<StoryItemUnit> listLikedPage) {
        this.mListLikedPages = listLikedPage;
        notifyDataSetChanged();
    }

}
