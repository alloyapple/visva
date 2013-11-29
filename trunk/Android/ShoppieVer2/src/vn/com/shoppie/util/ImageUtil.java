package vn.com.shoppie.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import vn.com.shoppie.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;

public class ImageUtil {
	private int radius = 40;
	
	private static ImageUtil instance;
	
	private Context context;
	
	public static ImageUtil getInstance(Context context) {
		if(instance == null) {
			instance = new ImageUtil(context, (int) context.getResources().getDimension(R.dimen.conner_circle_radius));
		}
		return instance;
	}
	
	private ImageUtil(Context context , int radius) {
		this.context = context;
		this.radius = radius;
	}
	
	public Bitmap getShapeBitmap(Bitmap bitmap, boolean topleft , boolean topright
			,boolean bottomleft , boolean bottomright) {
		return getShapeBitmap(bitmap, topleft, topright, bottomleft, bottomright, radius);
	}
	
	public Bitmap getShapeBitmap(Bitmap bitmap, boolean topleft , boolean topright
			,boolean bottomleft , boolean bottomright , int width , int height) {
		return getShapeBitmap(bitmap, topleft, topright, bottomleft, bottomright, radius , width , height);
	}
	
	public Bitmap getShapeBitmap(Bitmap bitmap, boolean topleft , boolean topright
			,boolean bottomleft , boolean bottomright , int radius) {
		return getShapeBitmap(bitmap, topleft, topright, bottomleft, bottomright, radius , bitmap.getWidth() , bitmap.getHeight());
	}
	
	public Bitmap getShapeBitmap(Bitmap bitmap, boolean topleft , boolean topright
			,boolean bottomleft , boolean bottomright , int radius , int width , int height) {
		if(!topleft && !topright && !bottomleft && !bottomright)
			return bitmap;
		if(width != bitmap.getWidth())
			radius /= (float)width / (float)bitmap.getWidth();
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    
	    Path path = new Path();
	    if(topleft) {
	    	path.moveTo(0, radius);
	    	path.lineTo(radius, radius);
	    	path.lineTo(radius, 0);
	    	canvas.drawCircle(radius, radius, radius, paint);
	    }
	    else {
	    	path.moveTo(0, 0);
	    }
	    if(topright) {
	    	path.lineTo(canvas.getWidth() - radius, 0);
	    	path.lineTo(canvas.getWidth() - radius, radius);
	    	path.lineTo(canvas.getWidth() , radius);
	    	canvas.drawCircle(canvas.getWidth() - radius, radius, radius, paint);
	    }
	    else {
	    	path.lineTo(canvas.getWidth(), 0);
	    }
	    if(bottomright) {
	    	path.lineTo(canvas.getWidth() , canvas.getHeight() - radius);
	    	path.lineTo(canvas.getWidth() - radius , canvas.getHeight() - radius);
	    	path.lineTo(canvas.getWidth() - radius , canvas.getHeight());
	    	canvas.drawCircle(canvas.getWidth() - radius, canvas.getHeight() - radius, radius, paint);
	    }
	    else {
	    	path.lineTo(canvas.getWidth() , canvas.getHeight());
	    }
	    if(bottomleft) {
	    	path.lineTo(radius , canvas.getHeight());
	    	path.lineTo(radius , canvas.getHeight() - radius);
	    	path.lineTo(0 , canvas.getHeight() - radius);
	    	canvas.drawCircle(radius, canvas.getHeight() - radius, radius, paint);
	    }
	    else {
	    	path.lineTo(0 , canvas.getHeight());
	    }
	    path.close();
	    canvas.drawPath(path, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    canvas = null;
//	    bitmap.recycle();bitmap = null;
	    return output;
	}
	
	public static Bitmap convertToMutable(Bitmap imgIn) {
	    try {
	        //this is the file going to use temporally to save the bytes. 
	        // This file will not be a image, it will store the raw image data.
	        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

	        //Open an RandomAccessFile
	        //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
	        //into AndroidManifest.xml file
	        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

	        // get the width and height of the source bitmap.
	        int width = imgIn.getWidth();
	        int height = imgIn.getHeight();
	        Config type = imgIn.getConfig();

	        //Copy the byte to the file
	        //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
	        FileChannel channel = randomAccessFile.getChannel();
	        MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
	        imgIn.copyPixelsToBuffer(map);
	        //recycle the source bitmap, this will be no longer used.
	        imgIn.recycle();
	        System.gc();// try to force the bytes from the imgIn to be released

	        //Create a new bitmap to load the bitmap again. Probably the memory will be available. 
	        imgIn = Bitmap.createBitmap(width, height, type);
	        map.position(0);
	        //load it back from temporary 
	        imgIn.copyPixelsFromBuffer(map);
	        //close the temporary file and channel , then delete that also
	        channel.close();
	        randomAccessFile.close();

	        // delete the temp file
	        file.delete();

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 

	    return imgIn;
	}
}
