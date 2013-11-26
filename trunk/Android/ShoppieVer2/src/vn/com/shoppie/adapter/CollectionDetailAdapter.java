package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.util.ImageUtil;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.OnItemClick;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class CollectionDetailAdapter extends MPagerAdapterBase {

	private ArrayList<MerchProductItem> data;
	public int id = 0;

	private Context context;
	private View cacheView[];
	private boolean isNeedUpdateImage[];
	private MPager mPager;

	public CollectionDetailAdapter(Context context, MPager mPager,
			ArrayList<MerchProductItem> data) {
		this.context = context;
		this.mPager = mPager;
		this.data = data;
		initCache();
	}

	private void initCache() {
		cacheView = new View[getCount()];
		isNeedUpdateImage = new boolean[getCount()];
		for (int i = 0; i < isNeedUpdateImage.length; i++)
			isNeedUpdateImage[i] = true;
	}

	@Override
	public View getView(final int position) {
		try {
			View v;
			if (cacheView[position] != null) {
				v = cacheView[position];
			} else {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (position == getCount() - 2) {
					v = inflater.inflate(R.layout.collectiondetail3, null,
							false);
					
					Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_lucky_pie);
					Bitmap bg = ImageUtil.getInstance(context).getShapeBitmap(bitmap, true, true, true, true);
					v.setBackgroundDrawable(new BitmapDrawable(bg));
					bitmap.recycle();
					bitmap = null;
				}
				else if (position == getCount() - 1)
					v = inflater.inflate(R.layout.collectiondetail5, null,
							false);
				else {
					v = inflater.inflate(R.layout.collectiondetail_1, null,
							false);
					View text = v.findViewById(R.id.text);
					text.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// v.setVisibility(View.GONE);
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
					
					Button btFacebook = (Button) v.findViewById(R.id.bt_fb);
					btFacebook.setTag(getItem(position));
					Button btMail = (Button) v.findViewById(R.id.bt_mail);
					btMail.setTag(getItem(position));
					Button btSms = (Button) v.findViewById(R.id.bt_sms);
					btSms.setTag(getItem(position));
					
					btFacebook.setOnClickListener(new OnClickListener() {
											
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MerchProductItem item = (MerchProductItem) v.getTag();
							publishFeedDialog(item);
						}
					});
					btSms.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MerchProductItem item = (MerchProductItem) v.getTag();
							initShareSMS(item);
						}
					});
					btMail.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MerchProductItem item = (MerchProductItem) v.getTag();
							initShareItent(item);
						}
					});
				}
				cacheView[position] = v;
				TextView count = (TextView) v.findViewById(R.id.count);
				count.setText("" + id);

			}

			if (position == getCount() - 2) {
				v.setSoundEffectsEnabled(false);
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (onItemClick != null) {
							onItemClick.onClick(position);
						}

						if (!pied) {
							SoundPool sp = new SoundPool(5,
									AudioManager.STREAM_MUSIC, 0);

							int iTmp = sp.load(context, R.raw.pied, 1); // in
																		// 2nd
																		// param
																		// u
																		// have
																		// to
																		// pass
																		// your
																		// desire
																		// ringtone
							sp.play(iTmp, 1, 1, 0, 0, 1);
							MediaPlayer mPlayer = MediaPlayer.create(context,
									R.raw.pied); // in 2nd param u have to pass
													// your desire ringtone
							if (mPlayer != null)
								mPlayer.start();

							View piedView = v.findViewById(R.id.pied_view);
							piedView.setVisibility(View.VISIBLE);
							ScaleAnimation anim = new ScaleAnimation(0, 1f, 0,
									1f, piedView.getWidth() / 2, piedView
											.getHeight() / 2);
							anim.setDuration(1000);
							anim.setInterpolator(new OvershootInterpolator());
							anim.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {
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
			} else if (position <= getCount() - 3) {
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						View text = v.findViewById(R.id.text);
						if (text.getAnimation() == null) {
							mPager.setLockSlide(true);
							text.setVisibility(View.VISIBLE);
							AnimatorSet set = new AnimatorSet();
							set.playTogether(ObjectAnimator.ofFloat(text,
									"alpha", 0, 0.8f), ObjectAnimator.ofFloat(
									text, "translationY", getViewHeight(), 0));
							set.setDuration(350);
							set.setInterpolator(new AccelerateInterpolator());
							set.start();
						}
					}
				});

			}

			if (isNeedUpdateImage[position]) {
				View image = v.findViewById(R.id.image);
				if (image != null) {
					ImageLoader.getInstance(context).DisplayImage(
							CatelogyAdapter.URL_HEADER
									+ data.get(position).getProductImage(),
							image  , true
							, true , false , false , true, false);
					isNeedUpdateImage[position] = false;
				}
			}
			return v;
		} catch (OutOfMemoryError e) {
			freeImage(position);
			// return null;
			return getView(position);
		} catch (InflateException e) {
			// TODO: handle exception
			freeImage(position);
			return getView(position);
		}
	}

	public void freeAll() {
		for (int i = 0; i < cacheView.length; i++) {
			View v = cacheView[i];
			if(v != null) {
				View image = v.findViewById(R.id.image);
				if (image != null) {
					BitmapDrawable d = (BitmapDrawable) image.getBackground();
					image.setBackgroundDrawable(null);
					if(d != null) {
						Bitmap b = d.getBitmap();
						if(b != ImageLoader.defaultBitmap) {
							b.recycle();
							b = null;
						}
					}
				}
			}
			if(!isNeedUpdateImage[i])
				isNeedUpdateImage[i] = true;
		}
		System.gc();
	}
	
	public void freeImage(int currPos) {
		int pre = 0;
		int next = 0;
		if (currPos == 0) {
			pre = getCount() - 1;
			next = currPos + 1;
		} else if (currPos == getCount() - 1) {
			pre = currPos - 1;
			next = 0;
		}
		else {
			next = currPos + 1;
			pre = currPos - 1;
		}
		
		for (int i = 0; i < cacheView.length; i++) {
			if (i != currPos && i != pre && i != next) {
				View v = cacheView[i];
				if(v != null) {
					View image = v.findViewById(R.id.image);
					if (image != null) {
						BitmapDrawable d = (BitmapDrawable) image.getBackground();
						image.setBackgroundDrawable(null);
						if(d != null) {
							Bitmap b = d.getBitmap();
							if(b != ImageLoader.defaultBitmap) {
								b.recycle();
								b = null;
							}
						}
					}
				}
				if(!isNeedUpdateImage[i])
					isNeedUpdateImage[i] = true;
			}
		}
		System.gc();
	}
	
	private void freeImageForException(int pos) {
		int pre = 0;
		int next = 0;
		if (pos == 0) {
			pre = getCount() - 1;
			next = pos + 1;
		} else if (pos == getCount() - 1) {
			pre = pos - 1;
			next = 0;
		}

		for (int i = 0; i < cacheView.length; i++) {
			View v = cacheView[i];
			if (i != pos && i != pre && i != next) {
				if(v != null) {
					View image = v.findViewById(R.id.image);
					if (image != null) {
						BitmapDrawable d = (BitmapDrawable) image.getBackground();
						image.setBackgroundDrawable(null);
						if(d != null) {
							Bitmap b = d.getBitmap();
							b.recycle();
							b = null;
							System.gc();
						}
					}
				}
				isNeedUpdateImage[i] = true;
			}
		}
	}

	@Override
	public View getNextView(int currPosition) {
		if (currPosition + 1 == getCount())
			return getView(0);
		else
			return getView(currPosition + 1);
	}

	@Override
	public View getBackView(int currPosition) {
		if (currPosition - 1 == -1)
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
		return (int) context.getResources().getDimension(
				R.dimen.collectiondetail_item_width);
	}

	@Override
	public int getViewHeight() {
		return (int) context.getResources().getDimension(
				R.dimen.collectiondetail_item_height);
	}

	@Override
	public int getNextItemId(int currPosition) {
		// TODO Auto-generated method stub
		if (currPosition + 1 == getCount())
			return 0;
		else
			return currPosition + 1;
	}

	@Override
	public int getBackItemId(int currPosition) {
		// TODO Auto-generated method stub
		if (currPosition - 1 == -1)
			return getCount() - 1;
		else
			return currPosition - 1;
	}

	@Override
	public int getTitlePadding() {
		// TODO Auto-generated method stub
		return (int) context.getResources().getDimension(
				R.dimen.page_item_title_padding);
	}

	public void setOnItemClick(OnItemClick onItemClick) {
		this.onItemClick = onItemClick;
	}

	private OnItemClick onItemClick;

	@Override
	public boolean isCircle() {
		// TODO Auto-generated method stub
		return false;
	}

	private void closeDesc(final View v) {
		if (v.getAnimation() != null)
			return;
		TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
				getViewHeight());
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
		for (int i = 0; i < cacheView.length; i++) {
			if (cacheView[i] != null) {
				cacheView = null;
			}
		}
	}

	@Override
	public boolean canbeNext(int curId) {
		// TODO Auto-generated method stub
//		if (curId == getCount() - 2 && !pied)
//			return false;
		return true;
	}

	public void pied() {
		View v = cacheView[getCount() - 2].findViewById(R.id.pied_layout);
		v.setVisibility(View.VISIBLE);
	}

	public boolean pied = false;

	/**
	 * share via facebook
	 * 
	 * @param image_url
	 */
	private void publishFeedDialog(MerchProductItem item) {
		Bundle params = new Bundle();
		params.putString("name", "Shoppie");
		params.putString("caption", "");
		params.putString("description",
				context.getString(R.string.introduct_invitation));
//		params.putString("link", "" + image_url);
		params.putString("picture",
				"http://farm6.staticflickr.com/5480/10948560363_bf15322277_m.jpg");
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(context,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							Toast.makeText(context, "Share successfully ",
									Toast.LENGTH_SHORT).show();
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(context, "Publish cancelled",
									Toast.LENGTH_SHORT).show();
						} else {
							// Generic, ex: network error
							Toast.makeText(context, "Error posting story",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).build();
		feedDialog.show();
	}

	/**
	 * share via email
	 */
	private void initShareItent(MerchProductItem item) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, "Shoppie Invitation");
		share.putExtra(Intent.EXTRA_TEXT,
				"" + context.getString(R.string.introduct_invitation));
		// share.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		// if (pFilePath != null)
		// share.putExtra(Intent.EXTRA_STREAM,
		// Uri.fromFile(new File(pFilePath)));
		share.setType("vnd.android.cursor.dir/email");
		context.startActivity(Intent.createChooser(share, "Select"));
	}

	/**
	 * share vis sms
	 */
	private void initShareSMS(MerchProductItem item) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body",
				context.getString(R.string.introduct_invitation));
		sendIntent.setType("vnd.android-dir/mms-sms");
		context.startActivity(sendIntent);
	}
}
