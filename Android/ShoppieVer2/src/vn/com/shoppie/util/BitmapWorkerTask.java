package vn.com.shoppie.util;

import java.lang.ref.WeakReference;
import java.util.TreeMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * This class used for load bitmap into ListView, GridView, on an AsyncTask
 * 
 * @author brother
 * @since 13/4/2013
 * @version 1.0
 * @see {@link http
 *      ://developer.android.com/training/displaying-bitmaps/load-bitmap.html}
 * */
public class BitmapWorkerTask extends AsyncTask<Object, Integer, Bitmap> {

	private static boolean FLAG_DEBUG = false;

	private static BitmapWorkerCache mCache;
	private static boolean enableCache = false;

	private OnLoadBitmapHandle _loadBitmapListener;
	private final WeakReference<BitmapWorkerTask.BitmapHolder> imageViewReference;
	private String pathImage = "";
	private Context context;

	public BitmapWorkerTask(Context context, BitmapWorkerTask.BitmapHolder holder) {
		this.context = context;
		this.imageViewReference = new WeakReference<BitmapWorkerTask.BitmapHolder>(holder);
		if (enableCache && mCache == null) {
			mCache = new BitmapWorkerCache(context);
		}
	}

	boolean smoothChange = false;

	public BitmapWorkerTask(Context context, BitmapWorkerTask.BitmapHolder holder, boolean smoothChange) {
		this.context = context;
		this.imageViewReference = new WeakReference<BitmapWorkerTask.BitmapHolder>(holder);
		if (enableCache && mCache == null) {
			mCache = new BitmapWorkerCache(context);
		}
		this.smoothChange = smoothChange;
	}

	@Override
	protected Bitmap doInBackground(Object... arg0) {
		Bitmap result;
		if (_loadBitmapListener != null) {
			result = _loadBitmapListener.loadBitmapInBackground(arg0);
		} else {
			pathImage = arg0[0].toString();
			result = decodeSampledBitmapFromFile(context.getResources(), pathImage, 100, 100);
		}

		// add to Cache
		if (enableCache && mCache != null) {
			mCache.addBitmapToMemoryCache(pathImage, result);
		}
		return result;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null && bitmap != null) {
			try {
				final ImageView imageView = imageViewReference.get().getImageView();
				final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageViewReference.get());
				if (this == bitmapWorkerTask && imageView != null) {
					if (smoothChange) {
						setImageBitmap(context, imageView, bitmap);

					} else {
						imageView.setImageBitmap(bitmap);
					}

				}
			} catch (NullPointerException e) {
			}
		}
	}

	// PUBLIC METHOD
	public static void setDebug(boolean flag) {
		FLAG_DEBUG = flag;
		if (mCache != null)
			mCache.FLAG_DEBUG = flag;
	}

	public static void setCache(Context context, boolean enable) {
		BitmapWorkerTask.enableCache = enable;

		if (enable) {
			mCache = new BitmapWorkerCache(context);

		}
	}

	public static void loadBitmap(Context context, BitmapWorkerTask.BitmapHolder holder, Object... pathImage) {
		loadBitmap(context, null, holder, pathImage);
	}

	public static void loadBitmap(Context context, OnLoadBitmapHandle listener, BitmapWorkerTask.BitmapHolder holder, Object... pathImage) {
		if (enableCache && mCache != null) {
			final Bitmap bitmap = mCache.getBitmapFromMemCache(pathImage[0].toString());
			if (bitmap != null) {
				if (FLAG_DEBUG)
					Log.i("getBitmap", "from Cache");
				holder.getImageView().setImageBitmap(bitmap);
			} else {
				holder.getImageView().setImageBitmap(null);
				loadImplicitBitmap(context, listener, false, holder, pathImage);
			}
		} else {
			loadImplicitBitmap(context, listener, false, holder, pathImage);
		}
	}

	public static void loadBitmap(Context context, OnLoadBitmapHandle listener, boolean smoothChange, BitmapWorkerTask.BitmapHolder holder, Object... pathImage) {
		if (enableCache && mCache != null) {
			final Bitmap bitmap = mCache.getBitmapFromMemCache(pathImage[0].toString());
			if (bitmap != null) {
				if (FLAG_DEBUG)
					Log.i("getBitmap", "from Cache");
				setImageBitmap(context, holder.getImageView(), bitmap);
				// holder.getImageView().setImageBitmap(bitmap);
			} else {
				setImageBitmap(context, holder.getImageView(), null);
				// holder.getImageView().setImageBitmap(null);
				loadImplicitBitmap(context, listener, smoothChange, holder, pathImage);
			}
		} else {
			loadImplicitBitmap(context, listener, smoothChange, holder, pathImage);
		}
	}

	public static void loadBitmap(Context context, OnLoadBitmapHandle listener, boolean smoothChange, BitmapWorkerTask.BitmapHolder holder, int resourceDefault, String... pathImage) {
		if (enableCache && mCache != null) {
			final Bitmap bitmap = mCache.getBitmapFromMemCache(pathImage[0].toString());
			if (bitmap != null) {
				if (FLAG_DEBUG)
					Log.i("getBitmap", "from Cache");
				setImageBitmap(context, holder.getImageView(), bitmap);
				// holder.getImageView().setImageBitmap(bitmap);
			} else {
				setImageBitmap(context, holder.getImageView(), resourceDefault);
				// holder.getImageView().setImageBitmap(null);
				loadImplicitBitmap(context, listener, smoothChange, holder, pathImage);
			}
		} else {
			loadImplicitBitmap(context, listener, smoothChange, holder, pathImage);
		}
	}

	public static void setImageBitmap(Context context, ImageView imv, Bitmap bmp) {
		imageViewAnimatedChange(context, imv, bmp);
		// imv.setImageBitmap(bmp);
	}

	public static void setImageBitmap(Context context, ImageView imv, int bmp) {
		imageViewAnimatedChange(context, imv, bmp);
		// imv.setImageBitmap(bmp);
	}

	public static void setImageDrawable(ImageView imv, Drawable draw) {

		imv.setImageDrawable(draw);
	}

	public static void imageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
		final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
		final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
		anim_out.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.setImageBitmap(new_image);
				anim_in.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
					}
				});
				v.startAnimation(anim_in);
			}
		});
		Object tag = v.getTag();
		if (tag != null) {
			v.setImageBitmap(new_image);
			return;
		}
		v.setTag(1);
		v.startAnimation(anim_out);
	}

	public static void imageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
		final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
		final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
		anim_out.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.setImageResource(new_image);
				anim_in.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
					}
				});
				v.startAnimation(anim_in);
			}
		});
		Object tag = v.getTag();
		if (tag != null) {
			v.setImageResource(new_image);
			return;
		}
		v.setTag(1);
		v.startAnimation(anim_out);
	}

	public static void loadImplicitBitmap(Context context, BitmapWorkerTask.BitmapHolder holder, Object... pathImage) {
		loadImplicitBitmap(context, null, holder, pathImage);
	}

	public static void loadImplicitBitmap(Context context, OnLoadBitmapHandle listener, boolean smoothChange, BitmapWorkerTask.BitmapHolder holder, Object... pathImage) {
		if (cancelPotentialWork(pathImage[0], holder)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(context, holder, smoothChange);
			if (listener != null) {
				task.setOnLoadBitmapHandle(listener);
			}
			AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), holder.getBitmapHolder(), task);
			setImageDrawable(holder.getImageView(), asyncDrawable);
			// holder.getImageView().setImageDrawable(asyncDrawable);
			try {
				task.execute(pathImage);
			} catch (RejectedExecutionException e) {
				task.cancel(true);
			}
		}
	}

	// PRIVATE METHOD
	private static boolean cancelPotentialWork(Object pathImage, BitmapWorkerTask.BitmapHolder holder) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(holder);

		if (bitmapWorkerTask != null) {
			final String bitmapData = bitmapWorkerTask.pathImage;
			if (!bitmapData.equals(pathImage)) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(BitmapWorkerTask.BitmapHolder holder) {
		if (holder != null) {
			final Drawable drawable = holder.getImageView().getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	// PUBLIC UTILITY METHOD
	public static Bitmap decodeSampledBitmapFromFile(Resources res, String pathImage, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathImage, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathImage, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	// INNER CLASS
	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	@SuppressLint("NewApi")
	public static class BitmapWorkerCache {
		private boolean FLAG_DEBUG = false;

		private LruCache<String, Bitmap> mMemoryCache;
		private TreeMap<String, Bitmap> mMemoryCacheOld;
		boolean newApi = false;
		Context context;

		public BitmapWorkerCache(Context context) {
			if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 12)
				newApi = true;
			else
				newApi = false;

			// Get max available VM memory, exceeding this amount will throw an
			// OutOfMemory exception. Stored in kilobytes as LruCache takes an
			// int in its constructor.
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory / 2;
			mMemoryCacheOld = new TreeMap<String, Bitmap>();
			mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					// The cache size will be measured in kilobytes rather than
					// number of items.
					// by: Google
					// return bitmap.getByteCount() / 1024;
					// by: StackOverFlow
					if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 12)
						return bitmap.getRowBytes() * bitmap.getHeight();
					// return bitmap.getByteCount();
					else
						return (bitmap.getRowBytes() * bitmap.getHeight());

				}
			};

		}

		public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
			if (newApi) {
				if (getBitmapFromMemCache(key) == null && bitmap != null) {
					if (FLAG_DEBUG)
						System.out.println("putImage");
					mMemoryCache.put(key, bitmap);
				}
			} else {
				if (mMemoryCacheOld.size() > 150) {
					if (FLAG_DEBUG)
						Log.i("cache Bitmap Old", "recycled");
					mMemoryCacheOld.clear();
				}
				if (!mMemoryCacheOld.containsKey(key) && bitmap != null)
					mMemoryCacheOld.put(key, bitmap);
			}

		}

		public Bitmap getBitmapFromMemCache(String key) {
			if (newApi)
				return mMemoryCache.get(key);
			else
				return mMemoryCacheOld.get(key);
		}
	}

	public void setOnLoadBitmapHandle(OnLoadBitmapHandle listener) {
		this._loadBitmapListener = listener;
	}

	public interface OnLoadBitmapHandle {
		/**
		 * this method is called in AsynTask.doInBackground(String...). if
		 * result is too BIG, you must resize it before return;
		 * 
		 * @see: decodeSampledBitmapFromFile().
		 * @see: calculateInSampleSize().
		 * @return: bitmap to show.
		 * @param string
		 *            input of BitmapWorkerTask.execute(string);
		 * */
		public Bitmap loadBitmapInBackground(Object... strings);
	}

	public static abstract class BitmapHolder {
		public abstract ImageView getImageView();

		public abstract Bitmap getBitmapHolder();
	}

	public static class BitmapHolderDefault extends BitmapHolder {
		public ImageView imageView;
		public Bitmap bitmap;

		@Override
		public ImageView getImageView() {
			return imageView;
		}

		@Override
		public Bitmap getBitmapHolder() {
			return bitmap;
		}
	}
}
