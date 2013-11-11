package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.util.CoverLoader;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.OnItemClick;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class CollectionDetailAdapter extends MPagerAdapterBase{

	private ArrayList<MerchProductItem> data;
	
	private Context context;
	private View cacheView[];
	private MPager mPager;
	public CollectionDetailAdapter(Context context , MPager mPager , ArrayList<MerchProductItem> data){
		this.context = context;
		this.mPager = mPager;
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
			if(position == getCount() - 1)
				v = inflater.inflate(R.layout.collectiondetail3, null, false);
			else {
				v = inflater.inflate(R.layout.collectiondetail_1, null, false);
				View text = v.findViewById(R.id.text);
				text.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						v.setVisibility(View.GONE);
						closeDesc(v);
						mPager.setLockSlide(false);
					}
				});
				text.setVisibility(View.GONE);
				
				TextView name = (TextView) v.findViewById(R.id.name);
				TextView name1 = (TextView) v.findViewById(R.id.name1);
				TextView price = (TextView) v.findViewById(R.id.price);
				TextView price1 = (TextView) v.findViewById(R.id.price1);
				TextView color = (TextView) v.findViewById(R.id.color);
				TextView like = (TextView) v.findViewById(R.id.like);
				TextView like1 = (TextView) v.findViewById(R.id.like1);
				
				name.setText(getItem(position).getProductName());
				name1.setText(getItem(position).getProductName());
				price.setText("Giá: " + getItem(position).getPrice());
				price1.setText("Giá: " + getItem(position).getPrice());
				color.setText("" + getItem(position).getShortDesc());
				like.setText("" + getItem(position).getLikedNumber());
				like1.setText("" + getItem(position).getLikedNumber());
				
				View image = v.findViewById(R.id.image);
				CoverLoader.getInstance(context).DisplayImage(CatelogyAdapter.URL_HEADER + data.get(position).getProductImage(), image);
			}
			cacheView[position] = v;
		}
		
		if(position == getCount() - 1)
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onItemClick != null){
						onItemClick.onClick(position);
					}
				}
			});
		else {
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					View text = v.findViewById(R.id.text);
					if(text.getAnimation() == null){
						mPager.setLockSlide(true);
						text.setVisibility(View.VISIBLE);
						AnimatorSet set = new AnimatorSet();
						set.playTogether(
								ObjectAnimator.ofFloat(text, "alpha", 0 , 0.8f),
								ObjectAnimator.ofFloat(text, "translationY", getViewHeight(), 0)
								);
						set.setDuration(350);
						set.setInterpolator(new AccelerateInterpolator());
						set.start();
					}
				}
			});
			
			TextView desc = (TextView) v.findViewById(R.id.desc1);
			desc.setText(getItem(position).getLongDesc());
			desc.setTag(v.findViewById(R.id.text));
			desc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					View text = (View) v.getTag();
					closeDesc(text);
					mPager.setLockSlide(false);
				}
			});
		}
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
		return data.size() + 1;
	}

	@Override
	public MerchProductItem getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
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
	
	private void closeDesc(final View v) {
		if(v.getAnimation() != null)
			return;
		TranslateAnimation anim = new TranslateAnimation(0, 0, 0, getViewHeight());
		anim.setDuration(350);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
			}
		});
		
		v.startAnimation(anim);
		
	}

	@Override
	public int getTitleHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
}
