
package vn.com.shoppie.adapter;

import java.util.ArrayList;

import com.google.analytics.tracking.android.Log;

import vn.com.shoppie.R;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FavouriteAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<FavouriteDataObject> mFavouriteDataObjects = new ArrayList<FavouriteDataObject>();
	private ImageLoader mImageLoader;
	public FavouriteAdapter(Context context, ArrayList<FavouriteDataObject> mFavouriteDataObjects){
		this.context = context;
		this.mFavouriteDataObjects = mFavouriteDataObjects;
		mImageLoader = new ImageLoader(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFavouriteDataObjects.size();
	}

	@Override
	public FavouriteDataObject getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.mFavouriteDataObjects.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		if(convertView == null){
			convertView = (LinearLayout)LinearLayout.inflate(context,R.layout.friend_favourite_item, null);
		}
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_friend_favourite_item);
		android.util.Log.e("adfdfjh","adsfdf "+mFavouriteDataObjects.get(position).getImage_url());
		mImageLoader.DisplayImage(WebServiceConfig.HEAD_IMAGE+mFavouriteDataObjects.get(position).getImage_url(),iv);
		iv.setContentDescription(this.mFavouriteDataObjects.get(position).getType());
		return convertView;
	}
}
