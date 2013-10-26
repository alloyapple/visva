package vn.com.shoppie.adapter.holder;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.database.smng.MerchantMng;
import vn.com.shoppie.database.sobject.Merchant;
import vn.com.shoppie.database.sobject.Notification;
import vn.com.shoppie.util.BitmapWorkerTask;
import vn.com.shoppie.util.BitmapWorkerTask.OnLoadBitmapHandle;
import vn.com.shoppie.util.SUtilBitmap;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemNotification extends BitmapWorkerTask.BitmapHolder {
	Context context;
	public TextView mTvTitle;
	public ImageView mIvBrand;
	public TextView mTvTime;
	MerchantMng merchMng;
	ArrayList<Merchant> arrMerch;

	public ItemNotification(Context context, View parent) {
		this.context = context;
		mTvTitle = (TextView) parent.findViewById(R.id.item_notification_tv_title);
		mIvBrand = (ImageView) parent.findViewById(R.id.item_notification_iv);
		mTvTime=(TextView)parent.findViewById(R.id.tv_time);
		
		merchMng = new MerchantMng(context);
		
	}
	
	public void setData(final Notification data) {
		mTvTitle.setText(data.content);
		mTvTime.setText(data.regdate);
		arrMerch = merchMng.select(MerchantMng.merchId,data.merchId);
		mIvBrand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent iBrand = new Intent(context, ActivityBrand.class);
				// iBrand.putExtra(ActivityBrand.MERCH_ID, data.merchId);
				// context.startActivity(iBrand);
			}
		});
		BitmapWorkerTask.loadBitmap(context, new OnLoadBitmapHandle() {

			@Override
			public Bitmap loadBitmapInBackground(Object... strings) {
				Notification data = (Notification) strings[0];
				if (arrMerch == null || arrMerch.isEmpty())
					return null;
				return data.getBitmap(context, arrMerch.get(0).merchImage);
			}
		}, this, data);
		
	}

	@Override
	public ImageView getImageView() {
		return mIvBrand;
	}

	@SuppressWarnings("static-access")
	@Override
	public Bitmap getBitmapHolder() {
		try {
			return SUtilBitmap.getInstance(context).decodeSampledBitmapFromResource(context.getResources(), R.drawable.ico_unknown, 90, 90);
		} catch (OutOfMemoryError e) {
			return null;
		}
	}
}
