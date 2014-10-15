package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObGetComment;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;

public class AdapterComments extends BaseAdapter {
    private ArrayList<ObGetComment> mObGetComments = new ArrayList<ObGetComment>();
    private ImageLoader             mImageLoader   = null;
    private Context                 mContext;
    private LayoutInflater          mLayoutInflater;
    private IClickUserAvatar        iClickUserAvatar;

    public AdapterComments(Context context, ObGetComments obGetComments) {
        this.mContext = context;

        if (obGetComments != null)
            mObGetComments = obGetComments.data;
        AIOLog.d("obGetComments:" + obGetComments);
        mImageLoader = new ImageLoader(mContext);

    }

    @Override
    public int getCount() {
        return mObGetComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mObGetComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.row_comment_content_undo_2, null);

        holder = new ViewHolder();
        holder._recentPostImage = (ImageView) convertView.findViewById(R.id.img_post_recent);
        holder._recentPostTime = (TextView) convertView.findViewById(R.id.text_recent_post_time);
        holder._recentPostSpotName = (TextView) convertView.findViewById(R.id.text_recent_post_spot_name);
        holder._userAvatar = (ImageView) convertView.findViewById(R.id.img_recent_post_user_avatar);
        holder._userName = (TextView) convertView.findViewById(R.id.text_recent_post_user_name);
        holder._totalComment = (TextView) convertView.findViewById(R.id.text_total_spot_comment);
        AIOLog.d("mObGetComments.size():" + mObGetComments.size() + ", position:" + position);
        if (mObGetComments.size() > 0 && position < mObGetComments.size()) {
            
        }
        return convertView;
    }

    class ViewHolder {
        ImageView _userAvatar;
        TextView  _userName;
        ImageView _recentPostImage;
        TextView  _recentPostTime;
        TextView  _recentPostSpotName;
        TextView  _totalComment;
    }

    public void updateRecentPostList(ArrayList<ObGetComment> obGetComments) {
        AIOLog.d("mObGetComments.size():" + obGetComments.size());
        mObGetComments = obGetComments;
        notifyDataSetChanged();
    }

    public void updatePullDownLoadMorePostList(ArrayList<ObGetComment> obGetComments, boolean isPulDownToRefresh) {
        if (isPulDownToRefresh)
            mObGetComments.addAll(0, obGetComments);
        else
            mObGetComments.addAll(obGetComments);
    }

    public interface IClickUserAvatar {
        public void onClickUserAvatar(String userId);
    }

    public void setListener(IClickUserAvatar iClickUserAvatar) {
        this.iClickUserAvatar = iClickUserAvatar;
    }

    public void remove(int position) {
        if (position < mObGetComments.size())
            mObGetComments.remove(position);
        notifyDataSetChanged();
    }
}
