package vn.com.shoppie.adapter;

import java.util.ArrayList;

import com.nineoldandroids.animation.ObjectAnimator;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.view.OnItemClick;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CatelogyAdapter extends MPagerAdapterBase{
	
//	public static final String URL_HEADER = "http://shoppie.com.vn:8080/";
	
	private ArrayList<MerchantCategoryItem> data;
	
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
	
	public View getView(final int position , boolean showBottom) {
		View v;
		if(cacheView[position] != null){
			v = cacheView[position];
		}
		else{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService
					(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.catelogy_item, null, false);
			
			MyTextView title    = (MyTextView) v.findViewById(R.id.catelogy);
			MyTextView subTitle = (MyTextView) v.findViewById(R.id.subcatelogy);
			MyTextView tvCount = (MyTextView) v.findViewById(R.id.count);
			View 	  icon     = v.findViewById(R.id.icon);
			View image    = v.findViewById(R.id.image);
			
			title.setNormal();
			subTitle.setLight();
			tvCount.setLight();
			
			title.setText(data.get(position).getMerchCatName());
			subTitle.setText(data.get(position).getMerchCatDesc());
			tvCount.setText("" + data.get(position).getCampaignNumber());
			
			if(showBottom)
				ImageLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + data.get(position).getIcon(), icon , true
					, false , false , false , false , false);
			else
				ImageLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + data.get(position).getIcon(), icon , true
						, false , true , false , false , false);
				
//			ImageLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + data.get(position).getImage(), image , false, false ,false ,false,false);

			ImageLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + data.get(position).getImage(), image , false
					, false , true , true , false , false);
			
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
	public View getView(final int position) {
		return getView(position, true);
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
	
	public void hideBottom() {
		for(int i = 0 ; i < getCount() ; i++) {
			View v = getView(i , false);
			v.findViewById(R.id.image).setVisibility(View.INVISIBLE);
			if(Build.VERSION.SDK_INT >= 11)
				v.findViewById(R.id.view1).setBackgroundResource(R.drawable.round_corner_shape_01);
			else
				v.findViewById(R.id.view1).setBackgroundResource(R.drawable.reound_corner_shape_01_23);
			ImageLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + data.get(i).getIcon(), v.findViewById(R.id.icon) , true
					, false , true , false , false);
		}
	}
	
	public void showBottom() {
		for(int i = 0 ; i < getCount() ; i++) {
			View v = getView(i);
			View bottom = v.findViewById(R.id.image);
			v.findViewById(R.id.view1).setBackgroundResource(R.drawable.round_topright_corner);
			ImageLoader.getInstance(context).DisplayImage(WebServiceConfig.HEAD_IMAGE + data.get(i).getIcon(), v.findViewById(R.id.icon) , true
					, false , false , false , false , false);
			bottom.setVisibility(View.VISIBLE);
			if(Build.VERSION.SDK_INT >= 11)
				ObjectAnimator.ofFloat(bottom, "alpha", 0 , 1f).setDuration(500).start();
		}
	}

	@Override
	public int getTitleHeight() {
		// TODO Auto-generated method stub
		return (int) context.getResources().getDimension(R.dimen.page_item_icon_height);
	}

	@Override
	public boolean canbeNext(int curId) {
		// TODO Auto-generated method stub
		return true;
	}
}
