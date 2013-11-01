package vn.com.shoppie.adapter;

import vn.com.shoppie.R;
import vn.com.shoppie.util.CoverLoader;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.OnItemClick;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CatelogyAdapter extends MPagerAdapterBase{
	
	private String url[] = {
		"http://s2.haivl.com/data/photos2/20131101/0c417aa1769641e4840f52b9c0fa31cc/medium-8f2db76cd755492ba0eb72de22f9df5c-400.jpg",
		"http://img.youtube.com/vi/P6CwUHzB5Jk/0.jpg",
		"http://s6.haivl.com/data/photos2/20131101/4b79b3c7f5e74103ad3452debb337224/medium-090e0226f02541b8bd77a4933f3ec863-400.jpg",
		"http://pagead2.googlesyndication.com/simgad/2294249788465311495",
		"http://s6.haivl.com/data/photos2/20131031/615e32fe62aa4140930d12f3d8167dd4/medium-c764679780ee49dd8188f583f884566c-650.jpg"
	};
	
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
			TextView title = (TextView) v.findViewById(R.id.catelogy);
			TextView subTitle = (TextView) v.findViewById(R.id.subcatelogy);
			ImageButton icon = (ImageButton) v.findViewById(R.id.icon);
			if(position == 0){
				title.setText("Coffee");
				subTitle.setText("HÀNG QUÁN");
				icon.setImageResource(R.drawable.icon_cafe);
			}
			else if(position == 1){
				title.setText("Nhà hàng");
				subTitle.setText("QUÁN ĂN, NHẬU NHẸT");
				icon.setImageResource(R.drawable.icon_dish);
			}
			else if(position == 2){
				title.setText("Thời trang");
				subTitle.setText("QUẦN ÁO, MŨ NÓN, KÍNH");
				icon.setImageResource(R.drawable.icon_fashion);
			}
			else if(position == 3){
				title.setText("Giải trí");
				subTitle.setText("ĂN CHƠI NHẢY MÚA");
				icon.setImageResource(R.drawable.icon_relax);
			}
			else if(position == 4){
				title.setText("Mua sắm");
				subTitle.setText("SIÊU THỊ, CỬA HÀNG");
				icon.setImageResource(R.drawable.icon_shoping);
			}
			
			ImageView image = (ImageView) v.findViewById(R.id.image);
			CoverLoader.getInstance(context).DisplayImage(url[position % url.length], image);

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
