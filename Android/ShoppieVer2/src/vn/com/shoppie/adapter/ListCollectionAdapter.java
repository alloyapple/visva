package vn.com.shoppie.adapter;

import vn.com.shoppie.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListCollectionAdapter extends BaseAdapter{
	
	private Context context;
	private View cacheView[];
	
	public ListCollectionAdapter(Context context){
		this.context = context;
		
		cacheView = new View[getCount()];
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
			
			convertView.setTag(holder);
		}
		else{
			holder = (ItemHolder) convertView.getTag();
		}
		
		if(position % 2 == 0)
			holder.backgroundView.setBackgroundColor(0xfff8f8f8);
		else
			holder.backgroundView.setBackgroundColor(0xffffffff);
			
		return convertView;
	}
	
	class TitleHolder {
		
	}
	
	class ItemHolder {
		public View star;
		public View backgroundView;
	}
}
