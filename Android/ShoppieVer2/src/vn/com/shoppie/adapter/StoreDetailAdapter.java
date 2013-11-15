package vn.com.shoppie.adapter;

import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.view.MPagerAdapterBase;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class StoreDetailAdapter extends MPagerAdapterBase {
	private Context context;
	private Vector<MerchantStoreItem> data;
	private View cacheView[];
	
	public StoreDetailAdapter(Context context , Vector<MerchantStoreItem> data) {
		this.context = context;
		this.data = data;
	}
	
	private void initCache(){
		cacheView = new View[getCount()];
	}
	
	@Override
	public View getView(int position) {
		// TODO Auto-generated method stub
		View v;
		if(cacheView[position] != null){
			v = cacheView[position];
		}
		else{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService
					(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.store_lookbook_item, null, false);
				
				TextView name = (TextView) v.findViewById(R.id.name);
				TextView desc = (TextView) v.findViewById(R.id.desc);
				TextView count = (TextView) v.findViewById(R.id.count);
				View Image = v.findViewById(R.id.image);
			}
			cacheView[position] = v;
			
		return v;
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
	public View getNextView(int currPosition) {
		// TODO Auto-generated method stub
		if(currPosition + 1 == getCount())
			return getView(0);
		else
			return getView(currPosition + 1);
	}

	@Override
	public View getBackView(int currPosition) {
		// TODO Auto-generated method stub
		if(currPosition - 1 == -1)
			return getView(getCount() - 1);
		else
			return getView(currPosition - 1);
	}

	@Override
	public int getViewWidth() {
		// TODO Auto-generated method stub
		return (int) context.getResources().getDimension(R.dimen.storedetail_item_width);
	}

	@Override
	public int getViewHeight() {
		// TODO Auto-generated method stub
		return (int) context.getResources().getDimension(R.dimen.storedetail_item_height);
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
		return 0;
	}

	@Override
	public int getTitleHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCircle() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canbeNext(int curId) {
		// TODO Auto-generated method stub
		return true;
	}

}
