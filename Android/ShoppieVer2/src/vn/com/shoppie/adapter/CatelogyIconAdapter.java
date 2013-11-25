package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.util.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CatelogyIconAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MerchantCategoryItem> data;

	public CatelogyIconAdapter(Context context,
			ArrayList<MerchantCategoryItem> data) {
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
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
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.category_header_item, null, false);
			ImageView imgIcon = (ImageView) v.findViewById(R.id.img_category);
			ImageLoader.getInstance(context).DisplayImage(
					CatelogyAdapter.URL_HEADER + data.get(position).getIcon(),
					imgIcon);
		}
		return v;
	}

}
