package vn.com.shoppie.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.com.shoppie.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

public class ImageLoader {

	public static Bitmap defaultBitmap;

	private int REQUIRED_SIZE = 512;

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<View, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<View, String>());
	ExecutorService executorService;
	// Handler to display images in UI thread
	Handler handler = new Handler();

	private static ImageLoader instance;
	private Context context;

	public static ImageLoader getInstance(Context context) {
		if (instance == null)
			instance = new ImageLoader(context);
		return instance;
	}

	public ImageLoader(Context context) {
		this.context = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	final int stub_id = R.drawable.bg_avatar;

	@SuppressWarnings("deprecation")
	private void setImageBitmap(View v, Bitmap bitmap) {
		if (v instanceof ImageView) {
			((ImageView) v).setImageBitmap(decodeBitmap(bitmap));
		} else {
			v.setBackgroundDrawable(new BitmapDrawable(bitmap));
		}
		Log.d("Bitmap Size", "Bitmap Size w " + bitmap.getWidth() + " h "
				+ bitmap.getHeight());
	}

	private void setImageResource(View v, int resId) {
		if (v instanceof ImageView) {
			((ImageView) v).setImageResource(resId);
		} else {
			v.setBackgroundResource(resId);
		}
	}

	public void DisplayImage(String url, View imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			setImageBitmap(imageView, bitmap);
		// imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			// imageView.setImageResource(stub_id);
			if (defaultBitmap == null) {
				defaultBitmap = BitmapFactory.decodeResource(
						context.getResources(), stub_id);
			}
			setImageBitmap(imageView, defaultBitmap);
			// setImageResource(imageView, stub_id);
		}
	}

	public Bitmap getBitmapOfUrl(String url, View view) {
		imageViews.put(view, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			return decodeBitmap(bitmap);
		return null;
	}

	public void DisplayImage(String url, View imageView, boolean topleft,
			boolean topright, boolean bottomleft, boolean bottomright) {
		DisplayImage(url, imageView, topleft, topright, bottomleft,
				bottomright, true);
	}

	public void DisplayImage(String url, View imageView, boolean topleft,
			boolean topright, boolean bottomleft, boolean bottomright,
			boolean keepSize) {
		DisplayImage(url, imageView, topleft, topright, bottomleft,
				bottomright, keepSize, true);
	}

	public void DisplayImage(String url, View imageView, boolean topleft,
			boolean topright, boolean bottomleft, boolean bottomright,
			boolean keepSize, boolean isCache) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			if (keepSize) {
				Bitmap bmp = ImageUtil.getInstance(context).getShapeBitmap(
						bitmap, topleft, topright, bottomleft, bottomright);
				setImageBitmap(imageView, bmp);
			} else {
				Log.d("KeepSize", "NonKeepSize1");
				MarginLayoutParams params = (MarginLayoutParams) imageView
						.getLayoutParams();
				Bitmap bmp = ImageUtil.getInstance(context).getShapeBitmap(
						bitmap, topleft, topright, bottomleft, bottomright,
						params.width, params.height);
				setImageBitmap(imageView, bmp);
			}
		} else {
			queuePhoto(url, imageView, topleft, topright, bottomleft,
					bottomright, keepSize, isCache);
			// setImageResource(imageView, stub_id);
			if (defaultBitmap == null) {
				defaultBitmap = BitmapFactory.decodeResource(
						context.getResources(), stub_id);
			}
			setImageBitmap(
					imageView,
					ImageUtil.getInstance(context).getShapeBitmap(
							defaultBitmap, topleft, topright, bottomleft,
							bottomright));
		}
	}

	private void queuePhoto(String url, View imageView) {
		queuePhoto(url, imageView, false, false, false, false);
	}

	private void queuePhoto(String url, View imageView, boolean topleft,
			boolean topright, boolean bottomleft, boolean bottomright) {
		queuePhoto(url, imageView, topleft, topright, bottomleft, bottomright,
				true);
	}

	private void queuePhoto(String url, View imageView, boolean topleft,
			boolean topright, boolean bottomleft, boolean bottomright,
			boolean keepSize) {
		queuePhoto(url, imageView, topleft, topright, bottomleft, bottomright,
				keepSize, true);
	}

	private void queuePhoto(String url, View imageView, boolean topleft,
			boolean topright, boolean bottomleft, boolean bottomright,
			boolean keepSize, boolean isCache) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, topleft, topright,
				bottomleft, bottomright, keepSize, isCache);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// Download Images from the Internet
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// Decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			// Recommended Size 512

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();

			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public View imageView;
		public boolean isCache = true;
		public boolean roundCornerTopLeft = false;
		public boolean roundCornerTopRight = false;
		public boolean roundCornerBottomLeft = false;
		public boolean roundCornerBottomRight = false;
		public boolean keepSize = true;

		public PhotoToLoad(String u, View i) {
			url = u;
			imageView = i;
		}

		public PhotoToLoad(String u, View i, boolean roundCornerTopLeft,
				boolean roundCornerTopRight, boolean roundCornerBottomLeft,
				boolean roundCornerBottomRight) {
			url = u;
			imageView = i;
			this.roundCornerTopLeft = roundCornerTopLeft;
			this.roundCornerTopRight = roundCornerTopRight;
			this.roundCornerBottomLeft = roundCornerBottomLeft;
			this.roundCornerBottomRight = roundCornerBottomRight;
		}

		public PhotoToLoad(String u, View i, boolean roundCornerTopLeft,
				boolean roundCornerTopRight, boolean roundCornerBottomLeft,
				boolean roundCornerBottomRight, boolean keepSize) {
			url = u;
			imageView = i;
			this.roundCornerTopLeft = roundCornerTopLeft;
			this.roundCornerTopRight = roundCornerTopRight;
			this.roundCornerBottomLeft = roundCornerBottomLeft;
			this.roundCornerBottomRight = roundCornerBottomRight;
			this.keepSize = keepSize;
		}

		public PhotoToLoad(String u, View i, boolean roundCornerTopLeft,
				boolean roundCornerTopRight, boolean roundCornerBottomLeft,
				boolean roundCornerBottomRight, boolean keepSize,
				boolean isCache) {
			url = u;
			imageView = i;
			this.roundCornerTopLeft = roundCornerTopLeft;
			this.roundCornerTopRight = roundCornerTopRight;
			this.roundCornerBottomLeft = roundCornerBottomLeft;
			this.roundCornerBottomRight = roundCornerBottomRight;
			this.keepSize = keepSize;
			this.isCache = isCache;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				if (photoToLoad.isCache)
					memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;

				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				if (photoToLoad.keepSize) {
					Bitmap bmp = ImageUtil.getInstance(context).getShapeBitmap(
							bitmap, photoToLoad.roundCornerTopLeft,
							photoToLoad.roundCornerTopRight,
							photoToLoad.roundCornerBottomLeft,
							photoToLoad.roundCornerBottomRight);
					setImageBitmap(photoToLoad.imageView, bmp);
				} else {
					Log.d("KeepSize", "NonKeepSize "
							+ photoToLoad.roundCornerTopLeft
							+ photoToLoad.roundCornerTopRight
							+ photoToLoad.roundCornerBottomLeft
							+ photoToLoad.roundCornerBottomRight);
					MarginLayoutParams params = (MarginLayoutParams) photoToLoad.imageView
							.getLayoutParams();
					Bitmap bmp = ImageUtil.getInstance(context).getShapeBitmap(
							bitmap, photoToLoad.roundCornerTopLeft,
							photoToLoad.roundCornerTopRight,
							photoToLoad.roundCornerBottomLeft,
							photoToLoad.roundCornerBottomRight, params.width,
							params.height);
					setImageBitmap(photoToLoad.imageView, bmp);
				}

			} else {
				// setImageResource(photoToLoad.imageView, stub_id);
				if (defaultBitmap == null) {
					defaultBitmap = BitmapFactory.decodeResource(
							context.getResources(), stub_id);
				}
				setImageBitmap(
						photoToLoad.imageView,
						ImageUtil.getInstance(context).getShapeBitmap(
								defaultBitmap, photoToLoad.roundCornerTopLeft,
								photoToLoad.roundCornerTopRight,
								photoToLoad.roundCornerBottomLeft,
								photoToLoad.roundCornerBottomRight));
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public void setRequiredSize(int REQUIRED_SIZE) {
		this.REQUIRED_SIZE = REQUIRED_SIZE;
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	private Bitmap decodeBitmap(Bitmap bitmap) {
		int width = 100;
		int height = 100;
		return Bitmap.createBitmap(bitmap, 0, 0, width, height);
	}
}