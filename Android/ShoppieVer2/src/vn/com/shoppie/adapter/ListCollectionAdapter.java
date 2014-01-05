package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchCampaignItem;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListCollectionAdapter extends BaseAdapter{
	private ArrayList<MerchCampaignItem> data;
	private ShoppieDBProvider mShoppieDBProvider;
	private Context context;
	
	public ListCollectionAdapter(Context context , ArrayList<MerchCampaignItem> data){
		this.context = context;
		
		this.data = data;
		mShoppieDBProvider = new ShoppieDBProvider(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public MerchCampaignItem getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemHolder holder;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.colection_item, null, false);
			
			holder = new ItemHolder();
			holder.backgroundView = convertView.findViewById(R.id.background_view);
			holder.image = convertView.findViewById(R.id.image);
			holder.title = (MyTextView) convertView.findViewById(R.id.title);
			holder.subTitle = (MyTextView) convertView.findViewById(R.id.subtitle);
			holder.star = convertView.findViewById(R.id.star);
			holder.like = (MyTextView) convertView.findViewById(R.id.like);
			holder.viewed = convertView.findViewById(R.id.ic_viewed);
			
			holder.title.setNormal();
			holder.subTitle.setLight();
			holder.like.setNormal();
			
			convertView.setTag(holder);
		}
		else{
			holder = (ItemHolder) convertView.getTag();
		}
		
		if(mShoppieDBProvider.checkViewed(getItem(position).getMerchId(), getItem(position).getCampaignId())) {
			holder.viewed.setVisibility(View.VISIBLE);
		}
		else {
			holder.viewed.setVisibility(View.GONE);
		}
		
		if(position % 2 == 0)
			holder.backgroundView.setBackgroundColor(0xfff8f8f8);
		else
			holder.backgroundView.setBackgroundColor(0xffffffff);
		
		holder.title.setText(getItem(position).getCampaignName());
		holder.subTitle.setText(getItem(position).getCampaignDesc());
		holder.like.setText("" + getItem(position).getLikedNumber());
		holder.star.setVisibility(getItem(position).getLuckyPie() > 0 ? 
				View.VISIBLE : View.INVISIBLE);
		ImageLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + getItem(position).getCampaignImage(), holder.image);
		
		return convertView;
	}
	
	class TitleHolder {
		
	}
	
	public void clear() {
		ImageLoader.getInstance(context).clearCache();
	}
	
	class ItemHolder {
		public View backgroundView;
		public View image;
		public MyTextView title;
		public MyTextView subTitle;
		public View star;
		public View viewed;
		public MyTextView like;
	}
}
