package vn.com.shoppie.view;

import vn.com.shoppie.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LayoutTop {
	Activity mActivity;
	public View mViewParent;
	public ImageButton mBtnLeft;
	public ImageButton mBtnRight;
	public TextView mTvTitle;

	public LayoutTop(Activity activity,View parent) {
		this.mActivity = activity;
		this.mViewParent=parent;
		
		mBtnLeft = (ImageButton) mViewParent.findViewById(R.id.layout_top_btn_left);
		mBtnRight = (ImageButton) mViewParent.findViewById(R.id.layout_top_btn_right);
		mTvTitle = (TextView) mViewParent.findViewById(R.id.layout_top_tv_toptitle);
	}

	public void setText(String text) {
		mTvTitle.setText(text);
	}

	public void setImageLeft(int drawableId) {
		mBtnLeft.setImageResource(drawableId);
	}

	public void setImageLeft(Drawable drawable) {
		mBtnLeft.setImageDrawable(drawable);
	}

	public void setImageLeft(Bitmap bmp) {
		mBtnLeft.setImageBitmap(bmp);
	}

	public void setImageRight(int drawableId) {
		mBtnRight.setImageResource(drawableId);
	}

	public void setImageRight(Drawable drawable) {
		mBtnRight.setImageDrawable(drawable);
	}

	public void setImageRight(Bitmap bmp) {
		mBtnRight.setImageBitmap(bmp);
	}
}
