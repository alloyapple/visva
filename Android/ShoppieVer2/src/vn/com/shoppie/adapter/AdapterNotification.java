package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.holder.ItemNotification;
import vn.com.shoppie.database.sobject.Notification;
import vn.com.shoppie.database.sobject.ShoppieObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterNotification extends BaseAdapter{
	Context context;
	ArrayList<ShoppieObject> data;
	
	public AdapterNotification(Context context,ArrayList<ShoppieObject> data) {
		this.context=context;
		this.data=data;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;
		ItemNotification holder;
		if(v==null){
			v=LayoutInflater.from(context).inflate(R.layout.item_notification, null);
			holder=new ItemNotification(context,v);
			v.setTag(holder);
		}
		holder=(ItemNotification)v.getTag();
		holder.setData((Notification)data.get(position));
		return v;
	}

}