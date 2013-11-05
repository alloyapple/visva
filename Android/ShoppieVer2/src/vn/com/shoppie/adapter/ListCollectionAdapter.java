package vn.com.shoppie.adapter;

import vn.com.shoppie.R;
import vn.com.shoppie.util.CoverLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListCollectionAdapter extends BaseAdapter{
	
	private String url[] = {
		"http://enbac10.vcmedia.vn/zoom/400_300/i:up_new/2013/10/25/item/767183/20131025082631/Lau-Rieu-Cua-Bap-Bo-Suon-Sun-Goi-Ga-Bap-Cai-4N.jpg",
		"http://enbac10.vcmedia.vn/zoom/400_300/i:up_new/2013/08/17/item/513545/20130817092201/Cuc-ngon-voi-Lau-Hai-San-cao-cap.jpg",
		"http://enbac10.vcmedia.vn/zoom/400_300/i:up_new/2013/10/21/item/797241/20131021085709/Quan-150.jpg",
		"http://enbac10.vcmedia.vn/zoom/400_300/i:up_new/2013/10/21/item/797241/20131021085113/Anh-Thuy-Lau-mam-Lau-ca-keo.jpg"
	};
	
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
			holder.image = convertView.findViewById(R.id.image);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ItemHolder) convertView.getTag();
		}
		
		if(position % 2 == 0)
			holder.backgroundView.setBackgroundColor(0xfff8f8f8);
		else
			holder.backgroundView.setBackgroundColor(0xffffffff);
		
		CoverLoader.getInstance(context).DisplayImage(url[position % url.length], holder.image);
		return convertView;
	}
	
	class TitleHolder {
		
	}
	
	class ItemHolder {
		public View star;
		public View backgroundView;
		public View image;
	}
}
