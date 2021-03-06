package vn.com.shoppie.adapter;

import java.util.ArrayList;
import vn.com.shoppie.R;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.util.ImageFriendLoader;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListFBFriendAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<FBUser> mListFriend;
	private InviteFriendJoinSPInterface mListener;
	private ImageFriendLoader mImageLoader;

	public ListFBFriendAdapter(Context context, ArrayList<FBUser> mListFriend) {
		this.mContext = context;
		this.mListFriend = mListFriend;
		this.mImageLoader = new ImageFriendLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListFriend.size();
	}

	@Override
	public FBUser getItem(int position) {
		// TODO Auto-generated method stub
		return this.mListFriend.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = (RelativeLayout) RelativeLayout.inflate(mContext,
					R.layout.friend_item, null);
		}
		ImageView imgPhoto = (ImageView) convertView
				.findViewById(R.id.fb_friend_avatar);
		TextView txtName = (TextView) convertView
				.findViewById(R.id.fb_friend_name);
		TextView txtNumberPie = (TextView) convertView
				.findViewById(R.id.fb_friend_number_pie);
		Button btnInvite = (Button) convertView
				.findViewById(R.id.fb_friend_btn);

		txtName.setSelected(true);
		txtName.setText(mListFriend.get(position).getUserName());

		btnInvite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FBUser friend = mListFriend.get(position);
				mListener.inviteFriendJoinSp(friend);
			}
		});

		if (mListFriend.get(position).isJoinSP()) {
			txtNumberPie.setVisibility(View.VISIBLE);
			btnInvite.setVisibility(View.GONE);
			txtNumberPie.setText(mListFriend.get(position).getNumberPie()
					+ " pie");
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));

		} else {
			btnInvite.setVisibility(View.VISIBLE);
			txtNumberPie.setText("");
			convertView.setBackgroundResource(R.drawable.bg_friend_fb_only);
		}
		btnInvite.setFocusable(false);
		imgPhoto.setFocusable(false);

		mImageLoader.DisplayImage(
				mListFriend.get(position).getUserAvatarLink(), imgPhoto);
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickFriendDetail(mListFriend.get(position));
			}
		});
		return convertView;
	}

	public void removeItem(int position) {
		mListFriend.remove(position);
		notifyDataSetChanged();
	}

	public void updateFolderList(ArrayList<FBUser> mListFriend) {
		this.mListFriend = mListFriend;
		for(int i = 0;i < this.mListFriend.size();i++){
			for(int j = i+1 ; j < this.mListFriend.size();j++){
				if(this.mListFriend.get(i).getNumberPie() < this.mListFriend.get(j).getNumberPie())
					swap(this.mListFriend,i,j);
			}
		}
		notifyDataSetChanged();
	}

	private void swap(ArrayList<FBUser> mListFriend, int i, int j) {
		// TODO Auto-generated method stub
		FBUser user1 = mListFriend.get(i);
		FBUser user2 = mListFriend.get(j);
		mListFriend.remove(j);
		mListFriend.add(j, user1);
		mListFriend.remove(i);
		mListFriend.add(i, user2);
	}

	public void addNewPhoto(FBUser friend) {
		// TODO Auto-generated method stub
		mListFriend.add(friend);
		notifyDataSetChanged();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public interface InviteFriendJoinSPInterface {
		public void inviteFriendJoinSp(FBUser friend);
		public void onClickFriendDetail(FBUser friend);
	}

	public void setListener(
			InviteFriendJoinSPInterface mInviteFriendJoinSPInterface) {
		// TODO Auto-generated method stub
		this.mListener = mInviteFriendJoinSPInterface;
	}
}
