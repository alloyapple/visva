package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.util.CoverLoader;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.OnItemClick;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class CollectionDetailAdapter extends MPagerAdapterBase{

	private ArrayList<MerchProductItem> data;
	public int id = 0;
	
	private Context context;
	private View cacheView[];
	private boolean isNeedUpdateImage[];
	private MPager mPager;
	public CollectionDetailAdapter(Context context , MPager mPager , ArrayList<MerchProductItem> data){
		this.context = context;
		this.mPager = mPager;
		this.data = data;
		initCache();
	}
	
	private void initCache(){
		cacheView = new View[getCount()];
		isNeedUpdateImage = new boolean[getCount()];
		for(int i = 0 ; i < isNeedUpdateImage.length ; i++)
			isNeedUpdateImage[i] = true;
	}
	
	@Override
	public View getView(final int position) {
		try {
			View v;
			if(cacheView[position] != null){
				v = cacheView[position];
			}
			else{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService
						(Context.LAYOUT_INFLATER_SERVICE);
				if(position == getCount() - 2)
					v = inflater.inflate(R.layout.collectiondetail3, null, false);
				else if(position == getCount() - 1)
					v = inflater.inflate(R.layout.collectiondetail5, null, false);
				else {
					v = inflater.inflate(R.layout.collectiondetail_1, null, false);
					View text = v.findViewById(R.id.text);
					text.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
//							v.setVisibility(View.GONE);
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
				cacheView[position] = v;
				TextView count = (TextView) v.findViewById(R.id.count);
				count.setText("" + id);
				
			}
			
			if(position == getCount() - 2) {
				v.setSoundEffectsEnabled(false);
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(onItemClick != null){
							onItemClick.onClick(position);
						}
						
						if(!pied) {
							SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		
							int iTmp = sp.load(context, R.raw.pied, 1); // in 2nd param u have to pass your desire ringtone
							sp.play(iTmp, 1, 1, 0, 0, 1);
							MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.pied); // in 2nd param u have to pass your desire ringtone
							mPlayer.start();
							
							View piedView = v.findViewById(R.id.pied_view);
							piedView.setVisibility(View.VISIBLE);
							ScaleAnimation anim = new ScaleAnimation(0, 1f, 0, 1f, piedView.getWidth() / 2, piedView.getHeight() / 2);
							anim.setDuration(1000);
							anim.setInterpolator(new OvershootInterpolator());
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
									pied = true;
									pied();
								}
							});
							
							piedView.startAnimation(anim);
						}
					}
				});
			}
			else if(position <= getCount() - 3){
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
				
				
			}
			
			if(isNeedUpdateImage[position]) {
				View image = v.findViewById(R.id.image);
				if(image != null) {
					CoverLoader.getInstance(context).DisplayImage(CatelogyAdapter.URL_HEADER + data.get(position).getProductImage(), image);
					isNeedUpdateImage[position] = false;
				}
			}
			return v;
		} catch (OutOfMemoryError e) {
			freeImageForException(position);
//			return null;
			return new View(context);
		} catch (InflateException e) {
			// TODO: handle exception
			freeImageForException(position);
			return getView(position);
		}
		
		
	}
	
	private void freeImageForException(int pos) {
		int pre = 0;
		int next = 0;
		if(pos == 0) {
			pre = getCount() - 1;
			next = pos + 1;
		}
		else if(pos == getCount() - 1) {
			pre = pos - 1;
			next = 0;
		}
		
		for(int i = 0 ; i < cacheView.length ; i++) {
			View v = cacheView[i];
			if(i != pos && i != pre && i != next) {
				View image = v.findViewById(R.id.image);
				if(image != null) {
					BitmapDrawable d = (BitmapDrawable) image.getBackground();
					image.setBackgroundDrawable(null);
					Bitmap b = d.getBitmap();
					b.recycle();
					b = null;
					System.gc();
					isNeedUpdateImage[i] = true;
				}
			}
		}
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
		return data.size() + 2;
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
	
	public void recycle() {
		for(int i = 0 ; i < cacheView.length ; i++) {
			if(cacheView[i] != null) {
				cacheView = null;
			}
		}
	}

	@Override
	public boolean canbeNext(int curId) {
		// TODO Auto-generated method stub
		if(curId == getCount() - 2 && !pied)
			return false;
		return true;
	}
	
	public void pied() {
		View v = cacheView[getCount() - 2].findViewById(R.id.pied_layout);
		v.setVisibility(View.VISIBLE);
	}
	
	public boolean pied = false;
}
