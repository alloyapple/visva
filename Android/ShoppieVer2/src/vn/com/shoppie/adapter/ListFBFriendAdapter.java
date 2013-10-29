package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.object.FBUser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListFBFriendAdapter extends BaseAdapter{

	private ArrayList<FBUser> mListFriend;
	private Context context;
	
	public ListFBFriendAdapter(Context context,ArrayList<FBUser> mListFriend){
		this.context = context;
		this.mListFriend = mListFriend;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListFriend.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
