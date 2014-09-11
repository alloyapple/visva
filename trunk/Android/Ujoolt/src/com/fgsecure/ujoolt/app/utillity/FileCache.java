package com.fgsecure.ujoolt.app.utillity;

import java.io.File;
import android.content.Context;
import android.util.Log;

public class FileCache {
	public static final String ROOT_DIR = "/sdcard";
	private File cacheDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "Ujoolt");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public void createCacheDir(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "Ujoolt");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		String temp = url.substring(0, 7);
		if (temp.equalsIgnoreCase(ROOT_DIR)) {
			File f1 = new File(url);
			return f1;
		}
		String format = url.substring(url.lastIndexOf('.'), url.length());
		File[] files = cacheDir.listFiles();
		String fileName = "" + (files.length + 1) + format;
		File f = new File(cacheDir, fileName);
		return f;
	}

	public String getName(String url) {
		String temp = url.substring(0, 7);
		Log.e("Temp", "" + temp);
		if (temp.equalsIgnoreCase(ROOT_DIR)) {
			return url;
		}
		String format = url.substring(url.lastIndexOf('.'), url.length());
		File[] files = cacheDir.listFiles();
		String fileName = "" + (files.length + 1) + format;
		return fileName;
	}

	public String getFileName(String url) {
		return url.substring(url.lastIndexOf('/'), url.length());
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}