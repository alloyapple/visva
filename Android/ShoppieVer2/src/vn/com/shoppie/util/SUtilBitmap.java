package vn.com.shoppie.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import vn.com.shoppie.activity.SettingPreference;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class SUtilBitmap {
	private static SUtilBitmap _instance = new SUtilBitmap();
	private static Context context;

	public static int reqWidthExtraH = 640;
	public static int reqHeightExtraH = 640;

	public static int reqWidthH = 256;
	public static int reqHeightH = 256;

	public static int reqWidthM = 128;
	public static int reqHeightM = 128;

	public static int reqWidthL = 90;
	public static int reqHeightL = 90;

	public static int reqWidth = reqWidthH;
	public static int reqHeight = reqHeightH;

	public static SUtilBitmap getInstance(Context context) {
		SUtilBitmap.context = context;
		return _instance;
	}

	public void deleteCache() {
		long thresold = 15 * 24 * 60 * 60 * 1000;
		long currentTime = System.currentTimeMillis();
		long lastTime = SettingPreference.getTimeLastDel(context);
		if (currentTime - lastTime >= thresold) {
			SettingPreference.setTimeLastDel(context, currentTime);
			File folderApp = getFolderApp();
			if (folderApp.isDirectory() && folderApp.exists()) {
				deleteSubFile(folderApp, currentTime, thresold);
			}
		}
	}

	public void deleteSubFile(File f, long crtTime, long thresold) {
		if (f.isFile()) {
			if (crtTime - f.lastModified() >= thresold) {
				f.delete();
				return;
			}
		} else {
			File[] subFiles = f.listFiles();
			for (File file : subFiles) {
				deleteSubFile(file, crtTime, thresold);
			}
		}
	}

	// get Link Image
	// get Image from CACHE
	// if not have
	// get Image from sdCard
	// if have: return
	// if not have: download Image from network
	// if download success
	// save Image to sdCard
	// get Image from sdCard
	// if download not success
	// return null

	public static HashMap<String, Bitmap> CACHE_BITMAP = new HashMap<String, Bitmap>();
	public static HashMap<String, Bitmap> CACHE_HIGHT_BITMAP = new HashMap<String, Bitmap>();
	private int sizePoolCache = 40;

	static Bitmap result = null;

	private synchronized void recycleCache() {
		synchronized (this) {
			if (CACHE_BITMAP.size() > sizePoolCache) {
				int i = 0;
				ArrayList<String> lstKey = new ArrayList<String>();
				for (Entry<String, Bitmap> entry : CACHE_BITMAP.entrySet()) {
					// Bitmap bmp = entry.getValue();
					// bmp.recycle();
					// bmp=null;
					i++;
					lstKey.add(entry.getKey());
					if (i >= sizePoolCache * 2 / 3)
						break;

				}
				for (String key : lstKey) {
					// Bitmap bmp = CACHE_BITMAP.get(key);
					// if (bmp != null) {
					// bmp.recycle();
					// bmp = null;
					// }
					CACHE_BITMAP.remove(key);
				}
				Log.e("RECYCLE BITMAP CACHE", i + " items");
				// CACHE_BITMAP.clear();
				System.gc();
			}
			if (CACHE_HIGHT_BITMAP.size() > sizePoolCache) {
				int i = 0;
				ArrayList<String> lstKey = new ArrayList<String>();
				for (Entry<String, Bitmap> entry : CACHE_HIGHT_BITMAP.entrySet()) {
					// Bitmap bmp = entry.getValue();
					// bmp.recycle();
					// bmp=null;
					i++;
					lstKey.add(entry.getKey());
					if (i >= sizePoolCache * 2 / 3)
						break;

				}
				for (String key : lstKey) {
					// Bitmap bmp = CACHE_HIGHT_BITMAP.get(key);
					// if (bmp != null) {
					// bmp.recycle();
					// bmp = null;
					// }
					CACHE_HIGHT_BITMAP.remove(key);
				}
				Log.e("RECYCLE BITMAP CACHE", i + " items");
				// CACHE_BITMAP.clear();
				System.gc();
			}
		}
	}

	// PROCESS BITMAP - LINK
	public Bitmap getBitmap(Context context, String link) {
		Bitmap result = null;

		recycleCache();
		if (CACHE_BITMAP.containsKey(link)) {
			result = CACHE_BITMAP.get(link);
			if (result == null)
				CACHE_BITMAP.remove(link);
			else if (!result.isRecycled())
				return result;
		}
		int currentMem = SettingPreference.getMem(context);
		switch (currentMem) {
		case 2:
			reqWidth = reqWidthH;
			reqHeight = reqHeightH;
			break;
		case 1:
			reqWidth = reqWidthM;
			reqHeight = reqHeightM;
			break;
		case 0:
			reqWidth = reqWidthL;
			reqHeight = reqHeightL;
			break;
		default:
			break;
		}
		Log.d("mem load", currentMem + "");
		try {
			result = getBitmapNotCache(context, link, reqWidth, reqHeight);
		} catch (OutOfMemoryError e) {
			processMemError();
		}
		if (result != null) {
			CACHE_BITMAP.put(link, result);
		}

		return result;
	}

	public Bitmap getBitmap(Context context, String link, int width, int height) {
		Bitmap result = null;

		recycleCache();
		if (CACHE_HIGHT_BITMAP.containsKey(link)) {
			result = CACHE_HIGHT_BITMAP.get(link);
			if (result == null)
				CACHE_HIGHT_BITMAP.remove(link);
			else if (!result.isRecycled())
				return result;
		}
		try {
			result = getBitmapNotCache(context, link, width, height);
		} catch (OutOfMemoryError e) {
			processMemError();
		}
		if (result != null) {
			CACHE_HIGHT_BITMAP.put(link, result);
		}

		return result;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private String linkToMemory3(String link) {
		InputStream in;
		try {
			Log.i("link image", link);
			in = new java.net.URL(link).openStream(); // -->> IOException
														// ':8080' -> ipv6
			Bitmap bitmap = null;
			// bitmap=BitmapFactory.decodeStream(in);
			File file = inputstreamToFile(link, in);
			bitmap = decodeSampledBitmapFromFile(file, reqWidth, reqHeight);
			CACHE_BITMAP.put(link, bitmap);
			return file.getAbsolutePath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			linkToMemory3(link);
			return null;
		} catch (OutOfMemoryError e) {
			processMemError();
			return null;
		}

	}

	private String linkToMemory2(String link) {
		InputStream in = null;
		try {
			String path = link.replaceAll("http://web.shoppie.com.vn:8080", "");
			URL url = new URL("http", "web.shoppie.com.vn", 8080, path);
			Log.e("3", url.toString());
			in = url.openStream();

			Bitmap bitmap = null;
			// bitmap=BitmapFactory.decodeStream(in);
			File file = inputstreamToFile(link, in);
			bitmap = decodeSampledBitmapFromFile(file, reqWidth, reqHeight);
			CACHE_BITMAP.put(link, bitmap);
			in.close();

			return file.getAbsolutePath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			processMemError();
			return null;
		} finally {
			if (in != null) {
				in = null;
			}
		}

	}

	@SuppressWarnings("unused")
	@Deprecated
	private String linkToMemory(String link) {
		Log.e("DOWNLOAD IMAGE", "Link: " + link);
		String filePath = "";
		File file = new File(linkToPath(link));
		// Log.e("full link", linkToPath(link));
		FileOutputStream fileOutput = null;
		InputStream in = null;
		try {
			URL url = new URL(link);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			if (!file.isFile()) {
				file.createNewFile();
			}
			in = new java.net.URL(link).openStream();

			Bitmap bitmap = null;
			// bitmap=BitmapFactory.decodeStream(in);
			file = inputstreamToFile(link, in);
			bitmap = decodeSampledBitmapFromFile(file, reqWidth, reqHeight);
			// bitmap = decodeSampledBitmapFromResourceMemOpt(in, 400, 400);
			CACHE_BITMAP.put(link, bitmap);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
			bitmap = BitmapWorkerTask.decodeSampledBitmapFromFile(context.getResources(), file.getAbsolutePath(), 100, 100);
			// bitmap.recycle();
			byte[] bitmapdata = bos.toByteArray();

			// write the bytes in file
			new FileOutputStream(file).write(bitmapdata);

			if (bitmap != null)
				return file.getAbsolutePath();

			fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();
			int totalSize = urlConnection.getContentLength();
			int downloadedSize = 0;
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				// Log.i("Progress:", "downloadedSize:" + downloadedSize
				// + "totalSize:" + totalSize);
			}

			if (downloadedSize == totalSize)
				filePath = file.getPath();
			if (bitmap != null)
				bitmap.recycle();
			in.close();
			fileOutput.close();
		} catch (MalformedURLException e) {
			filePath = null;
			file.delete();
		} catch (IOException e) {
			filePath = null;
			file.delete();
		} catch (OutOfMemoryError e) {
			processMemError();
		} finally {
			if (fileOutput != null)
				fileOutput = null;
			if (in != null)
				in = null;
		}
		return filePath;
	}

	ActivityManager activity_manager;

	private Bitmap decodeSampledBitmapFromFile(File file, int reqWidth, int reqHeight) throws OutOfMemoryError {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		} catch (OutOfMemoryError e) {
			throw e;
		}
		return bmp;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) throws OutOfMemoryError {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		// if(SettingPreference.getHighMem(context))
		// options.inSampleSize = calculateInSampleSize2(options);
		// else
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			throw e;
		}
		return bmp;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static int calculateInSampleSize2(BitmapFactory.Options options) {
		final int REQUIRED_SIZE = 512;
		// Raw height and width of image
		int height_tmp = options.outHeight;
		int width_tmp = options.outWidth;
		int scale = 1;

		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		return scale;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 2;

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

	@SuppressWarnings("unused")
	@Deprecated
	private Bitmap decodeSampledBitmapFromResourceMemOpt(InputStream inputStream, int reqWidth, int reqHeight) {
		activity_manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		byte[] byteArr = new byte[0];
		byte[] buffer = new byte[1024];
		int len;
		int count = 0;

		try {
			while ((len = inputStream.read(buffer)) > -1) {
				if (len != 0) {
					if (count + len > byteArr.length) {
						byte[] newbuf = new byte[(count + len) * 2];
						System.arraycopy(byteArr, 0, newbuf, 0, count);
						byteArr = newbuf;
					}

					System.arraycopy(buffer, 0, byteArr, count, len);
					count += len;
				}
			}

			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(byteArr, 0, count, options);

			options.inSampleSize = BitmapWorkerTask.calculateInSampleSize(options, reqWidth, reqHeight);
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			// int[] pids = { android.os.Process.myPid() };
			// android.os.Debug.MemoryInfo myxMemInfo =
			// activity_manager.getProcessMemoryInfo(pids)[0];
			// Log.e(TAG, "dalvikPss (decoding) = " + myMemInfo.dalvikPss);

			return BitmapFactory.decodeByteArray(byteArr, 0, count, options);

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public String bitmapToFile(Bitmap bitmap, String link) throws IOException {
		String nameBitmap = encodeLink(link);
		String writeTo = new File(Environment.getExternalStorageDirectory() + File.separator + nameBitmap).toString();
		FileOutputStream output;
		output = new FileOutputStream(writeTo);
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
		bitmap.recycle(); // try test
		output.close();
		return nameBitmap;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private Bitmap getBitmapNotCache(Context context, String link) {
		String path = linkToPath(link);
		try {
			return pathLocalToBitmap(path);
		} catch (FileNotFoundException e) {
			String pathImage = linkToMemory2(link);
			if (pathImage != null && !pathImage.equals("")) {
				return null;
			} else {
				return null;
			}
		} catch (IOException e) {
			String pathImage = linkToMemory2(link);
			if (pathImage != null && !pathImage.equals("")) {
				return null;
			} else {
				return null;
			}
		}

	}

	private Bitmap getBitmapNotCache(Context context, String link, int width, int height) throws OutOfMemoryError {
		String path = linkToPath(link);
		try {
			return fileToBitmap(new File(path), width, height);
		} catch (FileNotFoundException e) {
			String pathImage = linkToMemory2(link);
			if (pathImage != null && !pathImage.equals("")) {
				return null;
			} else {
				return null;
			}
		} catch (IOException e) {
			String pathImage = linkToMemory2(link);
			if (pathImage != null && !pathImage.equals("")) {
				return null;
			} else {
				return null;
			}
		} catch (OutOfMemoryError e) {
			throw e;
		}

	}

	private Bitmap fileToBitmap(File f) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 2; // try to decrease decoded image
		// options.inPurgeable = true; // if necessary purge pixels into
		// // disk
		// options.inScaled = true; // scale down image to actual device
		// // density
		Bitmap result = null;
		// result= BitmapFactory.decodeStream(fis, null, options);
		// result = decodeSampledBitmapFromResourceMemOpt(fis, 400, 400);
		try {
			result = decodeSampledBitmapFromFile(f, reqWidth, reqHeight);
		} catch (OutOfMemoryError e) {
			processMemError();
		}
		fis.close();
		return result;
	}

	private void processMemError() {
		int currentMem = SettingPreference.getMem(context);
		if (currentMem == 0)
			return;
		if (currentMem > 0)
			currentMem--;
		Log.e("set mem", currentMem + "");
		SettingPreference.setMem(context, currentMem);
	}

	@SuppressWarnings("resource")
	private Bitmap fileToBitmap(File f, int width, int height) throws IOException, OutOfMemoryError {
		FileInputStream fis = new FileInputStream(f);
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 2; // try to decrease decoded image
		// options.inPurgeable = true; // if necessary purge pixels into
		// // disk
		// options.inScaled = true; // scale down image to actual device
		// // density
		Bitmap result = null;
		// result= BitmapFactory.decodeStream(fis, null, options);
		// result = decodeSampledBitmapFromResourceMemOpt(fis, 400, 400);
		try {
			result = decodeSampledBitmapFromFile(f, width, height);
		} catch (OutOfMemoryError e) {
			throw e;
		}
		fis.close();
		return result;
	}

	private Bitmap pathLocalToBitmap(String path) throws IOException {
		File f = new File(path);
		return fileToBitmap(f);
	}

	@SuppressWarnings("unused")
	@Deprecated
	private Bitmap pathLocalToBitmap(String path, int width, int height) throws IOException {
		return fileToBitmap(new File(path), width, height);
	}

	private File getFolderApp() {
		File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
		File folderApp = new File(SDCardRoot.getAbsolutePath(), "/shoppie/cache/img");
		if (!folderApp.exists()) {
			if (!folderApp.isDirectory()) {
				boolean success = folderApp.mkdirs();
				if (!success) {
					Log.e("create folder app", "Failed");
					folderApp = context.getDir("img", Context.MODE_PRIVATE);
					if (!folderApp.exists()) {

						success = folderApp.mkdirs();
						if (!success) {
							Log.e("create folder private-app", "Failed");
						}
					}
				}
			}
		}
		return folderApp;
	}

	/**
	 * convert url link to path of file on SDcard include /mnt/sdcard/
	 * */
	private String linkToPath(String link) {
		// Log.e("Folder App", folderApp.getAbsolutePath());
		String filename = getFolderApp().getAbsolutePath() + File.separator + encodeLink(link);
		// File file = new File(SDCardRoot, filename);
		// return file.getAbsolutePath();
		return filename;
	}

	private static File inputstreamToFile(String link, InputStream inputStream) {
		String path = getInstance(context).linkToPath(link);
		OutputStream outputStream = null;
		File file = null;
		file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
				Log.e("Create temp file", file.getAbsolutePath());
			} catch (IOException e) {

				Log.e("ERROR: inputStreamToFile", "can not create new file");
				e.printStackTrace();
				return null;
			}
		}
		try {
			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(file);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	// PROCESS BASE64
	/**
	 * compress link url to path file
	 * */
	private String encodeLink(String link) {
		// http://web.shoppie.com.vn:8080/Upload/MERCHANTS/8/CAMPAIGNS/cp.jpg
		link = link.replaceAll("http://web.shoppie.com.vn:8080/", "%22");
		link = link.replaceAll("http://ws.shoppie.com.vn/", "%23");
		link = link.replaceAll("/", "%21");
		link = link.replaceAll(" ", "%20");
		link = link.replaceAll(":", "%19");
		return link;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private String decodeLinkBase64(String base64) {
		return Base64.decode(base64, Base64.URL_SAFE).toString();
	}

	public static Bitmap createReflectedImage(Bitmap bmp) {
		if (bmp == null)
			return null;
		// The gap we want between the reflection and the original image
		final int reflectionGap = 0;
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// Create a Bitmap with the flip matrix applied to it.
		int reflection_part = 4;
		// We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(bmp, 0, height - height / reflection_part, width, height / reflection_part, matrix, false);
		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / reflection_part), Config.ARGB_8888);
		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(bmp, 0, 0, null);
		// Draw in the gap
		Paint deafaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		// Create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, height, 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;

	}
}
