package vn.com.shoppie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import vn.com.shoppie.R;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.OnItemClick;

public class CollectionDetailAdapter extends MPagerAdapterBase{

	private Context context;
	private View cacheView[];
	public CollectionDetailAdapter(Context context){
		this.context = context;
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
			if(position == getCount() - 1)
				v = inflater.inflate(R.layout.collectiondetail3, null, false);
			else if(position % 2 == 0)
				v = inflater.inflate(R.layout.collectiondetail_1, null, false);
			else
				v = inflater.inflate(R.layout.collectiondetail2, null, false);
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
		return 5;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getViewWidth() {
		return (int) context.getResources().getDimension(R.dimen.collectiondetail_item_width);
	}

	@Override
	public int getViewHeight() {
		return (int) context.getResources().getDimension(R.dimen.collectiondetail_item_height);
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
		return false;
	}
}
