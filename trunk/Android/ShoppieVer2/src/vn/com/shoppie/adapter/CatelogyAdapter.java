package vn.com.shoppie.adapter;

import vn.com.shoppie.R;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.OnItemClick;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CatelogyAdapter extends MPagerAdapterBase{
	private String url = "http://www.skillsofmen.com/wp-content/uploads/2013/02/AtTheBar.jpg";
	
	private Context context;
	private View cacheView[];
	public CatelogyAdapter(Context context){
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
			v = inflater.inflate(R.layout.catelogy_item, null, false);
			TextView tv = (TextView) v.findViewById(R.id.catelogy);
			tv.setText("" + position);
			cacheView[position] = v;
			
			ImageButton icon = (ImageButton) v.findViewById(R.id.icon);
			if(position == 0)
				icon.setImageResource(R.drawable.icon_cafe);
			else if(position == 1)
				icon.setImageResource(R.drawable.icon_dish);
			else if(position == 2)
				icon.setImageResource(R.drawable.icon_fashion);
			else if(position == 3)
				icon.setImageResource(R.drawable.icon_relax);
			else if(position == 4)
				icon.setImageResource(R.drawable.icon_shoping);
			
			View image = v.findViewById(R.id.image);
			
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
		return 50;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
}
