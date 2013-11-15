package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.util.CoverLoader;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;

public class CatelogyIconAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<MerchantCategoryItem> data;
	public CatelogyIconAdapter(Context context , ArrayList<MerchantCategoryItem> data) {
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
		View v = new View(context);
		MarginLayoutParams params = new MarginLayoutParams((int) context.getResources().getDimension(R.dimen.actionbar_height), 
				(int) context.getResources().getDimension(R.dimen.actionbar_height));
		v.setLayoutParams(params);
		CoverLoader.getInstance(context).DisplayImage(CatelogyAdapter.URL_HEADER + data.get(position).getIcon(), v);
		return v;
	}

}
