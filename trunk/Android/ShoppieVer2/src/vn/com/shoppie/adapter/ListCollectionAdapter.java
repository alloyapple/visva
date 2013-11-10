package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchCampaignItem;
import vn.com.shoppie.util.CoverLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListCollectionAdapter extends BaseAdapter{
	private ArrayList<MerchCampaignItem> data;
	
	private Context context;
	private View cacheView[];
	
	public ListCollectionAdapter(Context context , ArrayList<MerchCampaignItem> data){
		this.context = context;
		
		this.data = data;
		cacheView = new View[getCount()];
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
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.subTitle = (TextView) convertView.findViewById(R.id.subtitle);
			holder.star = convertView.findViewById(R.id.star);
			holder.like = (TextView) convertView.findViewById(R.id.like);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ItemHolder) convertView.getTag();
		}
		
		if(position % 2 == 0)
			holder.backgroundView.setBackgroundColor(0xfff8f8f8);
		else
			holder.backgroundView.setBackgroundColor(0xffffffff);
		holder.title.setText(getItem(position).getCampaignName());
		holder.subTitle.setText(getItem(position).getCampaignDesc());
		holder.like.setText("" + getItem(position).getLikedNumber());
		CoverLoader.getInstance(context).DisplayImage(CatelogyAdapter.URL_HEADER + getItem(position).getCampaignImage(), holder.image
				, (int) context.getResources().getDimension(R.dimen.collection_item_item_width)
				, (int) context.getResources().getDimension(R.dimen.collection_item_item_height));
		
		return convertView;
	}
	
	class TitleHolder {
		
	}
	
	class ItemHolder {
		public View backgroundView;
		public View image;
		public TextView title;
		public TextView subTitle;
		public View star;
		public TextView like;
	}
}
