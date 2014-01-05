package vn.com.shoppie.adapter;

import java.text.DecimalFormat;
import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.util.CoverLoader;
import vn.com.shoppie.util.ImageUtil;
import vn.com.shoppie.util.Utils;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.android.gms.maps.model.LatLng;

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
			holder.title = (MyTextView) convertView.findViewById(R.id.title);
			holder.subTitle1 = (MyTextView) convertView.findViewById(R.id.subtitle1);
			holder.subTitle2 = (MyTextView) convertView.findViewById(R.id.subtitle2);
			holder.star = (MyTextView) convertView.findViewById(R.id.star);
			
			holder.subTitle1.setLight();
			holder.subTitle2.setLight();
			holder.star.setLight();
			
			convertView.setTag(holder);
		}
		else{
			holder = (ItemHolder) convertView.getTag();
		}
		
		if(position % 2 == 1)
			holder.backgroundView.setBackgroundColor(0xfff8f8f8);
		else
			holder.backgroundView.setBackgroundColor(0xffffffff);
		
		if(position == 0) {
			holder.backgroundView.setBackgroundResource(R.drawable.round_corner_shape_04);
			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.store_count);
			holder.star.setBackgroundDrawable(new BitmapDrawable(ImageUtil.getInstance(context).getShapeBitmap(bmp, false, true, false, false)));
		}
		
		holder.title.setText(getItem(position).getStoreName());
		try {
			if(location != null) {
				double length = Utils.calculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()), 
							new LatLng(Double.parseDouble(getItem(position).getLatitude()), Double.parseDouble(getItem(position).getLongtitude())));
				holder.subTitle1.setText("Khoảng cách: " + fmt(length) + " km");
			}
			else {
				holder.subTitle1.setText("Khoảng cách: ");
			}
		} catch (Exception e) {
			holder.subTitle1.setText("Khoảng cách: ");
		}
		
		
		holder.subTitle2.setText(getItem(position).getStoreAddress());
		holder.star.setText("+" + getItem(position).getPieQty());
		CoverLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + getItem(position).getMerchLogo(), holder.image
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
		public MyTextView title;
		public MyTextView subTitle1;
		public MyTextView subTitle2;
		public MyTextView star;
	}
	
}
