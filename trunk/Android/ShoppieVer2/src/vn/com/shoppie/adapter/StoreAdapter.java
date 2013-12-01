package vn.com.shoppie.adapter;

import java.text.DecimalFormat;
import java.util.Vector;

import com.google.android.gms.maps.model.LatLng;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.util.CoverLoader;
import vn.com.shoppie.util.Utils;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoreAdapter extends BaseAdapter {

	private Context context;
	private Vector<MerchantStoreItem> data;
	private Location location;
	
	public StoreAdapter(Context context , Vector<MerchantStoreItem> data ,Location location) {
		this.context = context;
		this.data = data;
		this.location = location;
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
		if(location != null) {
			double length = Utils.calculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()), 
						new LatLng(Double.parseDouble(getItem(position).getLatitude()), Double.parseDouble(getItem(position).getLongtitude())));
			holder.subTitle1.setText("Khoảng cách: " + fmt(length) + " km");
		}
		else {
			holder.subTitle1.setText("Khoảng cách: ");
		}
		
		holder.subTitle2.setText(getItem(position).getStoreAddress());
		holder.star.setText("+" + getItem(position).getPieQty());
		CoverLoader.getInstance(context).DisplayImage(CatelogyAdapter.URL_HEADER + getItem(position).getMerchLogo(), holder.image
				, (int) context.getResources().getDimension(R.dimen.collection_item_item_width)
				, (int) context.getResources().getDimension(R.dimen.collection_item_item_height));
		
		return convertView;
	}
	
	public static String fmt(double d) {
		return new DecimalFormat("#.##").format(d);
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
