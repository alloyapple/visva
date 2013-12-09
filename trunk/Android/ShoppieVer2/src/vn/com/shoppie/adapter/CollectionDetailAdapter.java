package vn.com.shoppie.adapter;

import java.util.ArrayList;
import java.util.Vector;
import vn.com.shoppie.R;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.util.FacebookUtil;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.util.ImageUtil;
import vn.com.shoppie.util.Utils;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPagerAdapterBase;
import vn.com.shoppie.view.MScrollView;
import vn.com.shoppie.view.OnItemClick;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class CollectionDetailAdapter extends MPagerAdapterBase {

	private ArrayList<MerchProductItem> data;
	public int id = 0;

	private Activity context;
	private View cacheView[];
	private boolean isNeedUpdateImage[];
	private MPager mPager;
	private boolean hasPie = false;
	private int pie = 0;
	private ShoppieSharePref mShoppieSharePref;

	public CollectionDetailAdapter(Activity context, MPager mPager,
			ArrayList<MerchProductItem> data, boolean hasPie, int pie) {
		this.context = context;
		this.mPager = mPager;
		this.data = data;
		this.hasPie = hasPie;
		this.pie = pie;
		this.mShoppieSharePref = new ShoppieSharePref(this.context);
		initCache();
	}

	private void initCache() {
		cacheView = new View[getCount()];
		isNeedUpdateImage = new boolean[getCount()];
		for (int i = 0; i < isNeedUpdateImage.length; i++)
			isNeedUpdateImage[i] = true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position) {
		try {
			View v;
			if (cacheView[position] != null) {
				v = cacheView[position];
			} else {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (position >= data.size() && position == getCount() - 2) {
					v = inflater.inflate(R.layout.collectiondetail3, null,
							false);

					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.bg_lucky_pie);
					Bitmap bg = ImageUtil.getInstance(context).getShapeBitmap(
							bitmap, true, true, true, true);
					v.setBackgroundDrawable(new BitmapDrawable(bg));
					// bitmap.recycle();
					// bitmap = null;
				} else if (position == getCount() - 1) {
					v = inflater.inflate(R.layout.collectiondetail5, null,
							false);
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// v.setVisibility(View.GONE);
							if (itemClickListener != null)
								itemClickListener.onItemClick(null, null,
										position, position);
						}
					});

				} else {
					if (pullView.size() > 0) {
						v = pullView.get(0);
						pullView.remove(0);
					} else
						v = inflater.inflate(R.layout.collectiondetail_1, null,
								false);
					View text = v.findViewById(R.id.text);
					text.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// v.setVisibility(View.GONE);
							closeDesc(v);
							mPager.setLockSlide(false);
						}
					});
					text.setVisibility(View.GONE);

					TextView name = (TextView) v.findViewById(R.id.name);
					TextView name1 = (TextView) v.findViewById(R.id.name1);
					TextView price = (TextView) v.findViewById(R.id.price);
					TextView priceGoc = (TextView) v
							.findViewById(R.id.price_goc);
					TextView price1 = (TextView) v.findViewById(R.id.price1);
					TextView priceGoc1 = (TextView) v
							.findViewById(R.id.price1_goc);
					TextView color = (TextView) v.findViewById(R.id.color);
					TextView like = (TextView) v.findViewById(R.id.like);

					priceGoc.setPaintFlags(priceGoc.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);
					priceGoc1.setPaintFlags(priceGoc1.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);

					name.setText(getItem(position).getProductName());
					name1.setText(getItem(position).getProductName());
					price.setText(""
							+ Utils.formatMoney(getItem(position).getPrice())
							+ " VNĐ");
					price1.setText(""
							+ Utils.formatMoney(getItem(position).getPrice())
							+ " VNĐ");
					color.setText("" + getItem(position).getShortDesc());
					like.setText("" + getItem(position).getLikedNumber());

					priceGoc.setText(getItem(position).getOldPrice() > 0 ? "Gốc: "
							+ Utils.formatMoney(getItem(position).getOldPrice())
							+ " VNĐ"
							: "");
					priceGoc1
							.setText(getItem(position).getOldPrice() > 0 ? "Gốc: "
									+ Utils.formatMoney(getItem(position)
											.getOldPrice()) + " VNĐ"
									: "");
					TextView count = (TextView) v.findViewById(R.id.count);
					count.setText(getItem(position).getPieQty() > 0 ? ""
							+ getItem(position).getPieQty() : "");
					//
					TextView count1 = (TextView) v.findViewById(R.id.count1);
					count1.setText(getItem(position).getPieQty() > 0 ? ""
							+ getItem(position).getPieQty() : "");

					if (getItem(position).getPieQty() > 999) {
						MarginLayoutParams params = (MarginLayoutParams) count
								.getLayoutParams();
						params.width *= 1.25f;
						params.height *= 1.25f;
						count.setLayoutParams(params);

						params = (MarginLayoutParams) count1.getLayoutParams();
						params.width *= 1.25f;
						params.height *= 1.25f;
						count1.setLayoutParams(params);

						// params = (MarginLayoutParams)
						// v.findViewById(R.id.muatang).getLayoutParams();
						// params.width *= 1.25f;
						// params.height *= 1.25f;
						// v.findViewById(R.id.muatang).setLayoutParams(params);
						//
						// params = (MarginLayoutParams)
						// v.findViewById(R.id.muatang1).getLayoutParams();
						// params.width *= 1.25f;
						// params.height *= 1.25f;
						// v.findViewById(R.id.muatang1).setLayoutParams(params);
					}

					Button likeBt = (Button) v.findViewById(R.id.like_click);
					likeBt.setTag(getItem(position));
					int resId = 0;
					if (getItem(position).getLiked() == 0) {
						resId = R.drawable.ic_like;
					} else {
						resId = R.drawable.ic_liked;
					}
					like.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);

					OnClickListener onClickListener = new OnClickListener() {

						@Override
						public void onClick(View v) {
							MerchProductItem item = (MerchProductItem) v
									.getTag();
							if (item.getLiked() == 0) {
								item.setLiked(1);
								item.setLikedNumber(item.getLikedNumber() + 1);
							} else {
								item.setLiked(0);
								item.setLikedNumber(item.getLikedNumber() - 1);
							}
							updateLiked(item.getProductId());
							if (onLikeListenner != null) {
								onLikeListenner.onLike(item.getLiked() == 1,
										item.getProductId());
							}
						}
					};

					likeBt.setOnClickListener(onClickListener);

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
							MerchProductItem item = (MerchProductItem) v
									.getTag();
							publishFeedDialog(item);
						}
					});
					btSms.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							MerchProductItem item = (MerchProductItem) v
									.getTag();
							initShareSMS(item);
						}
					});
					btMail.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							MerchProductItem item = (MerchProductItem) v
									.getTag();
							initShareItent(item);
						}
					});
				}
				cacheView[position] = v;
			}

			if (position == getCount() - 2 && position >= data.size()) {
				v.setSoundEffectsEnabled(false);
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
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

							final View piedView = v
									.findViewById(R.id.pied_view);
							((TextView) v.findViewById(R.id.pie_text))
									.setText("+" + pie);

							piedView.setVisibility(View.VISIBLE);
							ScaleAnimation anim = new ScaleAnimation(0, 1f, 0,
									1f, piedView.getWidth() / 2, piedView
											.getHeight() / 2);
							anim.setDuration(1000);
							anim.setInterpolator(new DecelerateInterpolator());

							final ScaleAnimation anim2 = new ScaleAnimation(1f,
									1f, 1f, 1f, piedView.getWidth() / 2,
									piedView.getHeight() / 2);
							anim2.setDuration(1000);

							final ScaleAnimation anim1 = new ScaleAnimation(1f,
									0f, 1f, 0f, piedView.getWidth() / 2,
									piedView.getHeight() / 2);
							anim1.setInterpolator(new AccelerateInterpolator());
							anim1.setDuration(500);

							anim.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									piedView.startAnimation(anim2);
								}
							});
							anim2.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									piedView.startAnimation(anim1);
								}
							});
							anim1.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {

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
			} else if (position <= data.size() - 1) {
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						View text = v.findViewById(R.id.text);
						if (text.getAnimation() == null) {
							mPager.setLockSlide(true);
							text.setVisibility(View.VISIBLE);
							// if(Build.VERSION.SDK_INT >= 11) {
							System.out.println(">>>>>>>>>>>>>>>> open Desc");
							AnimatorSet set = new AnimatorSet();
							set.playTogether(ObjectAnimator.ofFloat(text,
									"alpha", 0, 0.8f), ObjectAnimator.ofFloat(
									text, "translationY", getViewHeight(), 0));
							set.setDuration(350);
							set.setInterpolator(new AccelerateInterpolator());
							set.start();
							// }
							// else {
							// AnimationSet set = new AnimationSet(true);
							// set.addAnimation(new TranslateAnimation(0, 0,
							// getViewHeight(), 0));
							// set.addAnimation(new AlphaAnimation(0, 0.8f));
							// set.setFillAfter(true);
							// set.setDuration(350);
							// text.startAnimation(set);
							// }
						}
					}
				});

			}

			if (isNeedUpdateImage[position]) {
				View image = v.findViewById(R.id.image);
				if (image != null) {
					ImageLoader.getInstance(context).DisplayImage(
							WebServiceConfig.HEAD_IMAGE
									+ data.get(position).getProductImage(),
							image, true, true, false, false, true, false);
					isNeedUpdateImage[position] = false;
				}
			}
			return v;
		} catch (OutOfMemoryError e) {
			freeImage(position);
			// return null;
			return getView(position);
		} catch (InflateException e) {
			freeImage(position);
			return getView(position);
		}
	}

	@SuppressWarnings("deprecation")
	public void freeAll() {
		for (int i = 0; i < cacheView.length; i++) {
			View v = cacheView[i];
			if (v != null) {
				View image = v.findViewById(R.id.image);
				if (image != null) {
					BitmapDrawable d = (BitmapDrawable) image.getBackground();
					image.setBackgroundDrawable(null);
					if (d != null) {
						Bitmap b = d.getBitmap();
						if (b != ImageLoader.defaultBitmap) {
							b.recycle();
							b = null;
						}
					}
				}
			}
			if (!isNeedUpdateImage[i])
				isNeedUpdateImage[i] = true;
		}
		ImageLoader.getInstance(context).clearCache();
	}

	public void freeImage(int currPos) {
		int pre = 0;
		int pre1 = 0;
		int pre2 = 0;
		int next = 0;
		int next1 = 0;
		int next2 = 0;
		if (currPos == 0) {
			pre = getCount() - 1;
			next = currPos + 1;
		} else if (currPos == getCount() - 1) {
			pre = currPos - 1;
			next = 0;
		} else {
			next = currPos + 1;
			pre = currPos - 1;
		}

		pre1 = getBackItemId(pre);
		next1 = getNextItemId(next);
		pre2 = getBackItemId(pre1);
		next2 = getNextItemId(next1);

		for (int i = 0; i < cacheView.length; i++) {
			if (i != currPos && i != pre && i != next && i < getCount() - 2
					&& i != next1 && i != pre1 && i != next2 && i != pre2) {
				View v = cacheView[i];
				if (v != null) {
					View image = v.findViewById(R.id.image);
					if (image != null) {
						BitmapDrawable d = (BitmapDrawable) image
								.getBackground();
						image.setBackgroundDrawable(null);
						if (d != null) {
							Bitmap b = d.getBitmap();
							if (b != ImageLoader.defaultBitmap) {
								b.recycle();
								b = null;
							}
						}
					}
				}
				if (!isNeedUpdateImage[i])
					isNeedUpdateImage[i] = true;
			}
		}
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
				if (v != null) {
					View image = v.findViewById(R.id.image);
					if (image != null) {
						BitmapDrawable d = (BitmapDrawable) image
								.getBackground();
						image.setBackgroundDrawable(null);
						if (d != null) {
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
		return hasPie ? data.size() + 2 : data.size() + 1;
	}

	@Override
	public MerchProductItem getItem(int position) {
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
		if (currPosition + 1 == getCount())
			return 0;
		else
			return currPosition + 1;
	}

	@Override
	public int getBackItemId(int currPosition) {
		if (currPosition - 1 == -1)
			return getCount() - 1;
		else
			return currPosition - 1;
	}

	@Override
	public int getTitlePadding() {
		return (int) context.getResources().getDimension(
				R.dimen.page_item_title_padding);
	}

	public void setOnItemClick(OnItemClick onItemClick) {
		this.onItemClick = onItemClick;
	}

	private OnItemClick onItemClick;

	@Override
	public boolean isCircle() {
		return false;
	}

	private void closeDesc(final View v) {
		Log.d("ONClick", ">>>>>>>>>>>>>>>>>>>>> ");
		// if (v.getAnimation() != null)
		// return;
		// if (Build.VERSION.SDK_INT >= 11) {
		// TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
		// getViewHeight());
		// anim.setDuration(350);
		// anim.setAnimationListener(new AnimationListener() {
		//
		// @Override
		// public void onAnimationStart(Animation animation) {
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		//
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// v.setVisibility(View.GONE);
		// }
		// });
		//
		// v.startAnimation(anim);
		// }
		// else {
		TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
				getViewHeight());
		anim.setDuration(0);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
			}
		});

		v.startAnimation(anim);
		// }
	}

	@Override
	public int getTitleHeight() {
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
		// if (curId == getCount() - 2 && !pied)
		// return false;
		return true;
	}

	public void pied() {
		if (mShoppieSharePref.getLoginType())
			FacebookUtil.getInstance(context).publishLuckyPieInBackground(
					mShoppieSharePref.getCustName());
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
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Bundle params = new Bundle();
			params.putString("name", "Shoppie");
			params.putString("caption", "");
			params.putString(
					"description",
					context.getString(R.string.introduction_invitation,
							item.getProductName(), item.getShortDesc()));
			params.putString("link",
					"" + WebServiceConfig.HEAD_IMAGE + item.getThumbNail());
			params.putString("picture",
					"" + WebServiceConfig.HEAD_IMAGE + item.getThumbNail());
			// http://farm6.staticflickr.com/5480/10948560363_bf15322277_m.jpg
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
		} else {
			ensureOpenSession();
		}
	}

	/**
	 * share via email
	 */
	private void initShareItent(MerchProductItem item) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, "Shoppie Invitation");
		share.putExtra(
				Intent.EXTRA_TEXT,
				""
						+ context.getString(R.string.introduction_invitation,
								item.getProductName(), item.getShortDesc()));
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
		sendIntent.putExtra(
				"sms_body",
				context.getString(R.string.introduction_invitation,
						item.getProductName(), item.getShortDesc()));
		sendIntent.setType("vnd.android-dir/mms-sms");
		context.startActivity(sendIntent);
	}

	private boolean ensureOpenSession() {
		android.util.Log.e("adfdsfh", "afiun df ");
		Session.openActiveSession((Activity) context, true,
				new Session.StatusCallback() {
					@Override
					public void call(final Session session, SessionState state,
							Exception exception) {
						// TODO Auto-generated method stub
						Request request = Request.newMeRequest(session,
								new Request.GraphUserCallback() {
									@Override
									public void onCompleted(GraphUser user,
											Response response) {
										if (session == Session
												.getActiveSession()) {
											if (user != null) {
												String name = user.getName();
												android.util.Log.e(
														"name " + user.getId(),
														"adfname "
																+ user.getName());
											}
										}
									}
								});
						request.executeAsync();
					}
				});
		return true;
	}

	private void onSessionStateChanged(final Session session,
			SessionState state, Exception exception) {
		android.util.Log.e("adfdf",
				"adfd " + (session != null && session.isOpened()));
		if (session != null && session.isOpened()) {
			if (state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				Session.getActiveSession();
			} else {
				Request request = Request.newMeRequest(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (session == Session.getActiveSession()) {
									if (user != null) {
										String name = user.getName();
										android.util.Log.e(
												"name " + user.getId(),
												"adfname " + user.getName());
									}
								}
							}
						});
				request.executeAsync();
			}
		} else {
			Session.getActiveSession();
		}
	}

	public void refresh(int current) {
		for (int i = 0; i < isNeedUpdateImage.length; i++)
			isNeedUpdateImage[i] = true;
		getView(current);
		getView(getNextItemId(current));
		getView(getBackItemId(current));
	}

	private void updateLiked(int productionId) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getProductId() == productionId) {
				View v = getView(i);
				TextView like = (TextView) v.findViewById(R.id.like);
				int resId = 0;
				if (data.get(i).getLiked() == 0) {
					resId = R.drawable.ic_like;
				} else {
					resId = R.drawable.ic_liked;
				}
				like.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
				like.setText("" + data.get(i).getLikedNumber());
			}
		}
	}

	public void startLoading() {
		View v = getView(getCount() - 1);
		View loading = v.findViewById(R.id.pie_view);
		RotateAnimation anim = new RotateAnimation(0, 1800,
				loading.getWidth() / 2, loading.getHeight() / 2);
		anim.setDuration(4000);
		anim.setInterpolator(new Interpolator() {

			@Override
			public float getInterpolation(float input) {
				// TODO Auto-generated method stub
				return input;
			}
		});
		loading.startAnimation(anim);
	}

	public void setOnLikeListenner(OnLikeListenner onLikeListenner) {
		this.onLikeListenner = onLikeListenner;
	}

	private OnLikeListenner onLikeListenner;

	public interface OnLikeListenner {
		public void onLike(boolean liked, int productionId);
	}

	OnItemClickListener itemClickListener;

	public void setItemClickListener(OnItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	private Vector<View> pullView = new Vector<View>();
}
