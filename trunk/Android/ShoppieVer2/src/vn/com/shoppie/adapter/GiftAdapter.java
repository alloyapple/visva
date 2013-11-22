package vn.com.shoppie.adapter;

import java.util.List;
import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.GiftItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GiftAdapter extends BaseAdapter{
	private Vector<GroupGift> groupList;
	private Context context;
	
	public GiftAdapter(Context context , List<GiftItem> data) {
		this.context = context;
		createGroups(data);
	}
	
	private void createGroups(List<GiftItem> data) {
		int count = data.size() % 4 == 0 ? data.size() / 4 : data.size() / 4 + 1;
		groupList = new Vector<GiftAdapter.GroupGift>();
		for(int i = 0 ; i < count ; i++) {
			GroupGift group = new GroupGift();
			for(int j = 0 ; j < 4 && i * 4 + j < data.size(); j++) {
				group.add(data.get(i * 4 + j));
			}
			groupList.add(group);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupList.size();
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
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gift_item, null);
			
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.row0 = (LinearLayout) convertView.findViewById(R.id.row0);
			holder.row1 = (LinearLayout) convertView.findViewById(R.id.row1);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText("Danh sách quà đã đủ điểm đổi");
		addGiftByGroup(getItem(position), holder.row0, holder.row1);
		
		return convertView;
	}
	
	private void addGiftByGroup(GroupGift group, LinearLayout row0 , LinearLayout row1) {
		row0.removeAllViews();
		row1.removeAllViews();
		if(group.getCount() == 0) {
			row0.setVisibility(View.GONE);
			row1.setVisibility(View.GONE);
		}
		else if(group.getCount() <= 2) {
			row0.setVisibility(View.VISIBLE);
			row1.setVisibility(View.GONE);
		}
		else {
			row0.setVisibility(View.VISIBLE);
			row1.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < group.getCount(); i++) {
			View v = createViewByGift(group.getItem(i));
			if(i == 0 || i == 1) {
				row0.addView(v , getDimention(R.dimen.gift_item_row_height) , -1);
			}
			else if(i == 2 || i == 3) {
				row1.addView(v , getDimention(R.dimen.gift_item_row_height) , -1);
			}
		}
		
	}
	
	private int getDimention(int id) {
		return (int) context.getResources().getDimension(id);
	}
	
	private View createViewByGift(GiftItem item) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.gift_small_item, null);
		
		TextView tvName = (TextView) v.findViewById(R.id.name);
		ImageView image = (ImageView) v.findViewById(R.id.image);
		
		tvName.setText("Quà quà quà");
		
		return v;
	}
	
	class ViewHolder {
		public TextView title;
		public LinearLayout row0;
		public LinearLayout row1;
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
}
