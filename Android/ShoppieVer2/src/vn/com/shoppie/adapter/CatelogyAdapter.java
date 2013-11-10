package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.util.CoverLoader;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.OnItemClick;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CatelogyAdapter extends MPagerAdapterBase{
	
	public static final String URL_HEADER = "http://shoppie.com.vn:8080/";
	
	private ArrayList<MerchantCategoryItem> data;
	
	private String url[] = {
		"http://s2.haivl.com/data/photos2/20131101/0c417aa1769641e4840f52b9c0fa31cc/medium-8f2db76cd755492ba0eb72de22f9df5c-400.jpg",
		"http://img.youtube.com/vi/P6CwUHzB5Jk/0.jpg",
		"http://s6.haivl.com/data/photos2/20131101/4b79b3c7f5e74103ad3452debb337224/medium-090e0226f02541b8bd77a4933f3ec863-400.jpg",
		"http://pagead2.googlesyndication.com/simgad/2294249788465311495",
		"http://s6.haivl.com/data/photos2/20131031/615e32fe62aa4140930d12f3d8167dd4/medium-c764679780ee49dd8188f583f884566c-650.jpg"
	};
	
	private Context context;
	private View cacheView[];
	public CatelogyAdapter(Context context , ArrayList<MerchantCategoryItem> data){
		this.context = context;
		this.data = data;
		initCache();
	}
	
	private void initCache(){
		cacheView = new View[getCount()];
	}
	
	@Override
	public View getView(final int position) {
		View v;
		if(cacheView[position] != null){
			v = cacheView[position];
		}
		else{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService
					(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.catelogy_item, null, false);
			TextView title = (TextView) v.findViewById(R.id.catelogy);
			TextView subTitle = (TextView) v.findViewById(R.id.subcatelogy);
			View icon = v.findViewById(R.id.icon);
			title.setText(data.get(position).getMerchCatName());
			subTitle.setText(data.get(position).getMerchCatDesc());
			CoverLoader.getInstance(context).DisplayImage(URL_HEADER + data.get(position).getIcon(), icon);
			Log.d("Icon", data.get(position).getIcon());
			Log.d("Image", data.get(position).getImage());
			
			ImageView image = (ImageView) v.findViewById(R.id.image);
			CoverLoader.getInstance(context).DisplayImage(URL_HEADER + data.get(position).getImage(), image);

			cacheView[position] = v;
		}
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onItemClick != null){
					onItemClick.onClick(position);
				}
			}
		});
		return v;
	}
	
	@Override
	public View getNextView(int currPosition) {
		if(currPosition + 1 == getCount())
			return getView(0);
		else
			return getView(currPosition + 1);
	}
	
	@Override
	public View getBackView(int currPosition) {
		if(currPosition - 1 == -1)
			return getView(getCount() - 1);
		else
			return getView(currPosition - 1);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public MerchantCategoryItem getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public int getViewWidth() {
		return (int) context.getResources().getDimension(R.dimen.page_item_width);
	}

	@Override
	public int getViewHeight() {
		return (int) context.getResources().getDimension(R.dimen.page_item_height);
	}

	@Override
	public int getNextItemId(int currPosition) {
		// TODO Auto-generated method stub
		if(currPosition + 1 == getCount())
			return 0;
		else
			return currPosition + 1;
	}

	@Override
	public int getBackItemId(int currPosition) {
		// TODO Auto-generated method stub
		if(currPosition - 1 == -1)
			return getCount() - 1;
		else
			return currPosition - 1;
	}

	@Override
	public int getTitlePadding() {
		// TODO Auto-generated method stub
		return (int) context.getResources().getDimension(R.dimen.page_item_title_padding);
	}
	
	public void setOnItemClick(OnItemClick onItemClick){
		this.onItemClick = onItemClick;
	}
	
	private OnItemClick onItemClick;
	@Override
	public boolean isCircle() {
		// TODO Auto-generated method stub
		return true;
	}
}
