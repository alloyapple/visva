package vn.com.shoppie.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.GiftItem;
import vn.com.shoppie.util.ImageLoader;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Log;

public class GiftAdapter extends BaseAdapter{
	public static final int TYPE_AVAI = 0;
	public static final int TYPE_INVAI = 1;
	private List<GiftItem> data;
	private Vector<GroupGift> groupList;
	private Activity context;
	private ImageLoader mImageLoader;
	private int type = TYPE_AVAI;
	
	public GiftAdapter(Activity context , List<GiftItem> data , int type) {
		this.context = context;
		mImageLoader = new ImageLoader(context);
		mImageLoader.setRequiredSize(256);
		createGroups(data);
		this.type = type;
		this.data = data;
	}
	
	private void createGroups(List<GiftItem> data) {
		int count = data.size() % 4 == 0 ? data.size() / 4 : data.size() / 4 + 1;
		groupList = new Vector<GiftAdapter.GroupGift>();
		for(int i = 0 ; i < count ; i++) {
			GroupGift group = new GroupGift();
			for(int j = 0 ; j < 4 && i * 4 + j < data.size(); j++) {
				group.add(data.get(i * 4 + j));
				Log.d("Link Image : " + (CatelogyAdapter.URL_HEADER + data.get(i * 4 + j).getGiftImage()));
			}
			groupList.add(group);
		}
		cacheView = new View[getCount()];
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public GroupGift getItem(int arg0) {
		// TODO Auto-generated method stub
		return groupList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
//		if(convertView == null) {
//			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(R.layout.gift_item, null);
//			
//			holder = new ViewHolder();
//			holder.title = (TextView) convertView.findViewById(R.id.title);
//			holder.row0 = (LinearLayout) convertView.findViewById(R.id.row0);
//			holder.row1 = (LinearLayout) convertView.findViewById(R.id.row1);
//			convertView.setTag(holder);
//		}
//		else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		holder.title.setText("Danh sách quà đã đủ điểm đổi");
//		addGiftByGroup(getItem(position), holder.row0, holder.row1);
		
		if(cacheView[position] == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			cacheView[position] = inflater.inflate(R.layout.gift_item, null);
			
			holder = new ViewHolder();
			holder.title = (TextView) cacheView[position].findViewById(R.id.title);
			
			if(type == TYPE_AVAI)
				holder.title.setText("Danh sách quà đã đủ điểm đổi");
			else
				holder.title.setText("Danh sách quà chưa đủ điểm đổi");
			addGiftByGroup((LinearLayout) cacheView[position]);
		}
		
		return cacheView[position];
	}
	
	private void addGiftByGroup(LinearLayout groupView) {
		LinearLayout layout = new LinearLayout(context);
		for (int i = 0; i < data.size(); i++) {
			if(i % 2 == 0) {
				layout = new LinearLayout(context);
				groupView.addView(layout, -1, getDimention(R.dimen.gift_item_row_height));
			}
			View v = createViewByGift(data.get(i));
			layout.addView(v, getDimention(R.dimen.gift_item_row_height), getDimention(R.dimen.gift_item_row_height));
		}
		
//		row0.removeAllViews();
//		row1.removeAllViews();
//		if(group.getCount() == 0) {
//			row0.setVisibility(View.GONE);
//			row1.setVisibility(View.GONE);
//		}
//		else if(group.getCount() <= 2) {
//			row0.setVisibility(View.VISIBLE);
//			row1.setVisibility(View.GONE);
//		}
//		else {
//			row0.setVisibility(View.VISIBLE);
//			row1.setVisibility(View.VISIBLE);
//		}
//		for (int i = 0; i < group.getCount(); i++) {
//			View v = createViewByGift(group.getItem(i));
//			if(v.getParent() != null) {
//				((LinearLayout) v.getParent()).removeView(v);
//			}
//			if(i == 0 || i == 1) {
//				row0.addView(v , getDimention(R.dimen.gift_item_row_height) , -1);
//			}
//			else if(i == 2 || i == 3) {
//				row1.addView(v , getDimention(R.dimen.gift_item_row_height) , -1);
//			}
//		}
//		
	}
	
	private int getDimention(int id) {
		return (int) context.getResources().getDimension(id);
	}
	
	private View createViewByGift(final GiftItem item) {
		if(manageViewByItem.get(item) != null)
			return manageViewByItem.get(item);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.gift_small_item, null);
		
		TextView tvName = (TextView) v.findViewById(R.id.name);
		ImageView image = (ImageView) v.findViewById(R.id.image);
		
//		String temp[] = item.getPieQty().split(",");
//		if(temp != null) {
//			tvName.setText(temp[0]);
//		}
//		else {
//			tvName.setText(item.getPieQty());
//		}
		
		tvName.setText(item.getPieStr());
		
		mImageLoader.DisplayImage(CatelogyAdapter.URL_HEADER + item.getGiftImage() , image
				, false ,false, false , false ,false , false);
		manageViewByItem.put(item, v);
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onClickItem != null)
					onClickItem.onClickItem(item);
			}
		});
		
		return v;
	}
	
	public void setOnClickItem(OnClickItem onClickItem) {
		this.onClickItem = onClickItem;
	}
	
	private OnClickItem onClickItem;
	
	public interface OnClickItem {
		public void onClickItem(GiftItem item);
	}
	
	class ViewHolder {
		public TextView title;
	}
	
	class GroupGift {
		Vector<GiftItem> list = new Vector<GiftItem>();
		
		public void add(GiftItem item) {
			list.add(item);
		}
		
		public int getCount() {
			return list.size();
		}
		
		public GiftItem getItem(int pos) {
			return list.get(pos);
		}
	}
	
	private View[] cacheView;
	private HashMap<GiftItem, View> manageViewByItem = new HashMap<GiftItem, View>();
}
