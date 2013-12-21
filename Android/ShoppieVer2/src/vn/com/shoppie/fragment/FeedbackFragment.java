package vn.com.shoppie.fragment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.PersonalInfoActivity;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.MyCircleImageView;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FeedbackFragment extends FragmentBasic {
	private ShoppieSharePref mSharePref;
	private View root;
	private MyCircleImageView mImgAvatar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSharePref = new ShoppieSharePref(getActivity());
		View root = (ViewGroup) inflater.inflate(R.layout.feedback_act,
				null);

		RelativeLayout containerLayout = (RelativeLayout) root.findViewById(R.id.container);
		View content = (View) root.findViewById(R.id.content);

		View cover = new View(getActivity());
		cover.setBackgroundResource(R.drawable.bg_center);
		LayoutParams params = new LayoutParams((int)(getDimention(R.dimen.collectiondetail_item_width) * 1.06f), 
				(int)((getDimention(R.dimen.collectiondetail_item_height) * 1.05f)));
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		containerLayout.addView(cover, 0 , params);
		mImgAvatar = (MyCircleImageView) root.findViewById(R.id.img_avatar);
		final TextView name= (TextView)root.findViewById(R.id.name);
		name.setText(mSharePref.getCustName());
		root.findViewById(R.id.btn_accept).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText edt = (EditText) FeedbackFragment.this.root.findViewById(R.id.feed_content);
				String content = edt.getText().toString().trim();
				if(content.equals(""))
					return;
				uploadFeedback("" + mSharePref.getCustId(), content);
			}
		});

		root.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((PersonalInfoActivity)getActivity()).onClickBackPersonal(null);
			}
		});
		
		
		
		this.root = root;
		return root;
	}

	private Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth,
			int reqHeight, int orientation) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Matrix mtx = new Matrix();
		mtx.postRotate(orientation);
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = MainPersonalInfoFragment.calculateInSampleSize(options, reqWidth,
				reqHeight);
		int width = options.outWidth;
		int height = options.outHeight;
		Log.d("width " + height, "width " + width);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Log.d("orientation ", "orientation " + orientation);
		// return Bitmap.createBitmap(BitmapFactory.decodeFile(filePath,
		// options), 0, 0, reqHeight,
		// reqWidth, mtx, true);

		return decodeBitmap(BitmapFactory.decodeFile(filePath, options),
				orientation);

	}
	
	private Bitmap decodeBitmap(Bitmap bitmap, int orientation) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(orientation);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
	}
	
	private int checkOrientation(Uri fileUri) {
		int rotate = 0;
		String imagePath = fileUri.getPath();
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(imagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Since API Level 5
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = 90;
			break;
		}
		return rotate;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		int gender = mSharePref.getGender();
		if (gender == 1)
			mImgAvatar.setImageResource(R.drawable.ic_male);
		else
			mImgAvatar.setImageResource(R.drawable.ic_female);
	}

	private void uploadFeedback(String custId, String message) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.sendFeedback(
				custId, message);
		AsyncHttpPost postFeedback = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(
						getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("post success ", "post success");
						((PersonalInfoActivity)getActivity()).onClickBackPersonal(null);
					}

					@Override
					public void processIfResponseFail() {
						// finish();
					}
				}, nameValuePairs, true);
		postFeedback.execute(WebServiceConfig.URL_FEEDBACK);

	}

	public void updateUI() {
		if (mSharePref.getLoginType()) {
			ImageLoader.getInstance(getActivity()).DisplayImage(mSharePref.getImageAvatar(), mImgAvatar);
		}
		else {
			File file = new File(mSharePref.getImageAvatar());
			String imageName = file.getName();
			if (file.exists()) {
				Uri fileUri = Uri.fromFile(file);
				int orientation = checkOrientation(fileUri);
			
			Bitmap bmp = decodeSampledBitmapFromFile(mSharePref.getImageAvatar(), 100,
					100, orientation);
			mImgAvatar.setImageBitmap(bmp);
			}
		}
	}
	
	private int getDimention(int id) {
		return (int) getResources().getDimension(id);
	}
}
