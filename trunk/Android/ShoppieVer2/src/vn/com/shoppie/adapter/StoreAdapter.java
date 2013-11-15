package vn.com.shoppie.adapter;

import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.ListCollectionAdapter.ItemHolder;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.util.CoverLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoreAdapter extends BaseAdapter {

	private Context context;
	private Vector<MerchantStoreItem> data;
	
	public StoreAdapter(Context context , Vector<MerchantStoreItem> data) {
		this.context = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public MerchantStoreItem getItem(int position) {
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
		// TODO Auto-generated method stub
		ItemHolder holder;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.store_search_item, null, false);
			
			holder = new ItemHolder();
			holder.backgroundView = convertView.findViewById(R.id.background_view);
			holder.image = convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.subTitle1 = (TextView) convertView.findViewById(R.id.subtitle1);
			holder.subTitle2 = (TextView) convertView.findViewById(R.id.subtitle2);
			holder.star = (TextView) convertView.findViewById(R.id.star);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ItemHolder) convertView.getTag();
		}
		
		if(position % 2 == 0)
			holder.backgroundView.setBackgroundColor(0xfff8f8f8);
		else
			holder.backgroundView.setBackgroundColor(0xffffffff);
		holder.title.setText(getItem(position).getStoreName());
		holder.subTitle1.setText(getItem(position).getLongtitude());
		holder.subTitle2.setText(getItem(position).getStoreAddress());
		holder.star.setText("+" + getItem(position).getPieQty());
		CoverLoader.getInstance(context).DisplayImage(CatelogyAdapter.URL_HEADER + getItem(position).getMerchLogo(), holder.image
				, (int) context.getResources().getDimension(R.dimen.collection_item_item_width)
				, (int) context.getResources().getDimension(R.dimen.collection_item_item_height));
		
		return convertView;
	}
	
	class ItemHolder {
		public View backgroundView;
		public View image;
		public TextView title;
		public TextView subTitle1;
		public TextView subTitle2;
		public TextView star;
	}
	
}
