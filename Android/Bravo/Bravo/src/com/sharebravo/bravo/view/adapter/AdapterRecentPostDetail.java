package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import com.sharebravo.bravo.R;

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

    ImageView imagePost;
    TextView  contentPost;
    Button    btnCallSpot;
    Button    btnViewMap;
    Button    btnFollow;
    ImageView followIcon;
    boolean   isFollowing;
    Button    btnSave;
    Button    btnShare;
    EditText  textboxComment;
    Button    btnSubmitComment;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (position == 0) // post content
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_post_detail_header, null, false);
            imagePost = (ImageView) convertView.findViewById(R.id.image_post_detail);
            imagePost.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sample1));
            contentPost = (TextView) convertView.findViewById(R.id.content_post_detail);
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

    class ViewHolderComment {
    }

}
