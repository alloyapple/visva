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
import com.sharebravo.bravo.model.response.ObGetUserBlocking.User;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterPostList.IClickUserAvatar;

@SuppressLint("ViewHolder")
public class AdapterUserList extends BaseAdapter {
    private ArrayList<User>  mObUserList;
    private ImageLoader      mImageLoader = null;

    private Context          mContext;
    private LayoutInflater   mLayoutInflater;

    private IClickUserAvatar iClickUserAvatar;

    public AdapterUserList(Context context) {
        this.mContext = context;
        mImageLoader = new ImageLoader(mContext);
        mObUserList = new ArrayList<User>();
    }

    @Override
    public int getCount() {
        return mObUserList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mLayoutInflater.inflate(R.layout.row_user_list, null);

        holder = new ViewHolder();
        holder._userAvatar = (ImageView) convertView.findViewById(R.id.image_user_search);
        holder._userName = (TextView) convertView.findViewById(R.id.name_user_search);
        if (mObUserList.size() > 0 && position < mObUserList.size()) {
            final User userObj = mObUserList.get(position);
            if (StringUtility.isEmpty(userObj.Full_Name)) {
                holder._userName.setText("Unknown");
            } else
                holder._userName.setText(userObj.Full_Name);
            String profile_img_url = userObj.Profile_Img_URL;

            AIOLog.d("obGetBravo.Profile_Img_URL: " + userObj.Profile_Img_URL);
            if (StringUtility.isEmpty(profile_img_url)) {
                holder._userAvatar.setImageResource(R.drawable.user_picture_default);
            } else {
                mImageLoader.DisplayImage(profile_img_url, R.drawable.user_picture_default, holder._userAvatar, true);
            }
            holder._userAvatar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    iClickUserAvatar.onClickUserAvatar(userObj.User_ID);
                }
            });
        }
        return convertView;
    }

    public void setListener(IClickUserAvatar iClickUserAvatar) {
        this.iClickUserAvatar = iClickUserAvatar;
    }

    public void updateUserList(ArrayList<User> mObUserList) {
        if (mObUserList == null)
            this.mObUserList.clear();
        else
            this.mObUserList = mObUserList;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView _userAvatar;
        TextView  _userName;
    }

    public void updatePullDownLoadMorePostList(ArrayList<User> obUserList, boolean isPulDownToRefresh) {
        if (isPulDownToRefresh)
            mObUserList.addAll(0, obUserList);
        else
            mObUserList.addAll(obUserList);
        notifyDataSetChanged();
    }

    public void removeAllList() {
        mObUserList.clear();
        notifyDataSetChanged();
    }
}
