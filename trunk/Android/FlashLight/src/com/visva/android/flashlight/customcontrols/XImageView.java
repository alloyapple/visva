package com.visva.android.flashlight.customcontrols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.visva.android.flashlight.R;

public class XImageView extends ImageView {

	private Bitmap bitmap;
	private Bitmap bitmapResize;
	private Paint paint;

	private void init(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.w_bulb_gradient);
		bitmapResize = resizeBitmap(bitmap, display.getWidth(), display.getHeight());
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
	}

	public XImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public XImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public XImageView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bitmapResize != null) {
			canvas.drawBitmap(bitmapResize, 0, 0, paint);
		}
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleHeight = (float) newHeight / height;
		float scaleWidth = (float) newWidth / width;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmapResize = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return bitmapResize;
	}

}
