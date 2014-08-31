package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterRecentPostDetail extends BaseAdapter {
    private Context            mContext;
    private ArrayList<String>  commentsData = new ArrayList<String>();
    private DetailPostListener listener;
    private ObGetBravo         bravoObj     = null;
    private ImageLoader        mImageLoader = null;

    public AdapterRecentPostDetail(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    public void setListener(DetailPostListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return commentsData.size() + 6;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setBravoOb(ObGetBravo obj) {
        this.bravoObj = obj;
    }

    NetworkImageView imagePost;
    TextView         contentPost;
    ImageView        userAvatar;
    TextView         txtUserName;
    Button           btnCallSpot;
    Button           btnViewMap;
    Button           btnFollow;
    ImageView        followIcon;
    boolean          isFollowing;
    Button           btnSave;
    Button           btnShare;
    EditText         textboxComment;
    Button           btnSubmitComment;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (position == 0) // post content
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_post_detail_header, null, false);
            String imgSpotUrl = bravoObj.Last_Pic;
            imagePost = (NetworkImageView) convertView.findViewById(R.id.image_post_detail);
            // imagePost.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sample1));
            imagePost.setImageUrl(imgSpotUrl, mImageLoader);
            contentPost = (TextView) convertView.findViewById(R.id.content_post_detail);
            contentPost.setText(bravoObj.Spot_Name);
            userAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            
            txtUserName = (TextView) convertView.findViewById(R.id.txt_user_name);
            txtUserName.setText(bravoObj.Full_Name);
            btnCallSpot = (Button) convertView.findViewById(R.id.btn_call_spot);
            btnCallSpot.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToCallSpot();
                }
            });
            btnViewMap = (Button) convertView.findViewById(R.id.btn_view_map);
            btnViewMap.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToMapView();
                }
            });
            followIcon = (ImageView) convertView.findViewById(R.id.icon_follow);
            btnFollow = (Button) convertView.findViewById(R.id.btn_follow);
            btnFollow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    isFollowing = !isFollowing;
                    followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
                }
            });

        }
        else if (position == getCount() - 2) // post content
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_input_comment, null, false);
            textboxComment = (EditText) convertView.findViewById(R.id.textbox_comment);
            btnSubmitComment = (Button) convertView.findViewById(R.id.btn_submit_comment);
            btnSubmitComment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToSubmitComment();
                }
            });
        }
        else if (position == getCount() - 1) // post content
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_post_detail_footer, null, false);

        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_comment_content, null, false);
        }

        return convertView;
    }

    public void updateCommentList() {
        notifyDataSetChanged();
    }

    class ViewHolderComment {
    }

    class ViewHolderHeader {
    }

}
