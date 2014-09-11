package com.fgsecure.ujoolt.app.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.Language;

public class ImageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private int[] ids_en = { R.drawable.tut_en_1, R.drawable.tut_en_2, R.drawable.tut_en_3,
			R.drawable.tut_en_4, R.drawable.tut_en_5, R.drawable.tut_en_6 };
	private int[] ids_fr = { R.drawable.tut_fr_1, R.drawable.tut_fr_2, R.drawable.tut_fr_3,
			R.drawable.tut_fr_4, R.drawable.tut_fr_5, R.drawable.tut_fr_6 };
	// public Context mContext;
	public ArrayList<Bitmap> arrBitmaps = new ArrayList<Bitmap>();
	public MainScreenActivity mainScreenActivity;

	public ImageAdapter(MainScreenActivity mainScreenActivity) {
		this.mainScreenActivity = mainScreenActivity;
		mInflater = (LayoutInflater) mainScreenActivity.getBaseContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		setBitmap();
		// mContext = context;
		// setBitmap();
	}

	public void setBitmap() {
		if (arrBitmaps != null) {
			arrBitmaps.clear();
			System.gc();
			if (mainScreenActivity.language == Language.ENGLISH) {
				for (int i = 0; i < 6F; i++) {
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					BitmapFactory.decodeResource(
							mainScreenActivity.getBaseContext().getResources(), ids_fr[i], o);

					// Find the correct scale value. It should be the power of
					// 2.
					final int REQUIRED_SIZE = 260;
					int width_tmp = o.outWidth, height_tmp = o.outHeight;
					int scale = 1;
					while (true) {
						if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
							break;
						width_tmp /= 2;
						height_tmp /= 2;
						scale *= 2;
					}
					// long avai = Utils.getAvailableStorage();
					// Log.e("AvaiStorage", "" + avai);
					// if(avai < Utils.MEM_ALLOW){
					// Log.e("Out", "Memory");
					// clearCache();
					// lazyAdapter.notifyDataSetChanged();
					// }
					// decode with inSampleSize
					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = scale;
					arrBitmaps.add(BitmapFactory.decodeResource(mainScreenActivity.getBaseContext()
							.getResources(), ids_fr[i], o2));
				}
			} else {
				for (int i = 0; i < 6; i++) {
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					BitmapFactory.decodeResource(
							mainScreenActivity.getBaseContext().getResources(), ids_en[i], o);

					// Find the correct scale value. It should be the power of
					// 2.
					final int REQUIRED_SIZE = 260;
					int width_tmp = o.outWidth, height_tmp = o.outHeight;
					int scale = 1;
					while (true) {
						if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
							break;
						width_tmp /= 2;
						height_tmp /= 2;
						scale *= 2;
					}
					// long avai = Utils.getAvailableStorage();
					// Log.e("AvaiStorage", "" + avai);
					// if(avai < Utils.MEM_ALLOW){
					// Log.e("Out", "Memory");
					// clearCache();
					// lazyAdapter.notifyDataSetChanged();
					// }
					// decode with inSampleSize
					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = scale;
					arrBitmaps.add(BitmapFactory.decodeResource(mainScreenActivity.getBaseContext()
							.getResources(), ids_en[i], o2));
				}
			}
		}
	}

	@Override
	public int getCount() {
		return ids_en.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.image_item, null);

		}
		if (convertView != null) {
			((ImageView) convertView.findViewById(R.id.image_view)).setImageBitmap(arrBitmaps
					.get(position));
			((ImageView) convertView.findViewById(R.id.image_view_close))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mainScreenActivity.hideTutorial();
						}
					});
		}
		return convertView;
	}

}
