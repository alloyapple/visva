package visvateam.outsource.idmanager.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import visvateam.outsource.idmanager.contants.Contants;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

public class ImageMemoActivity extends Activity {

	private ImageView imageView;
	private Uri fileUri;
	private CheckBox mCheckBoxChoiceImgMemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_memo);

		imageView = (ImageView) findViewById(R.id.img_memo);
		mCheckBoxChoiceImgMemo = (CheckBox) findViewById(R.id.check_box_choice_img);
		mCheckBoxChoiceImgMemo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Log.e("file uri", "fie uri " + fileUri);

				if (fileUri != null && mCheckBoxChoiceImgMemo.isChecked()) {
					 Intent resultIntent =new Intent();
					 String filePathImgMemo = fileUri.getPath();
					 Log.e("file a", "file a"+filePathImgMemo);
					 resultIntent.putExtra(Contants.FIlE_PATH_IMG_MEMO,
					 filePathImgMemo);
					 setResult(Activity.RESULT_OK, resultIntent);
					 finish();
				} else {
					showToast("No image is choosed");
					mCheckBoxChoiceImgMemo.setChecked(false);
				}
			}
		});
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, ImageMemoActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onCamera(View v) {
		startCameraIntent();
	}

	public void onLibrary(View v) {
		startGalleryIntent();
	}

	public void onTrash(View v) {
		imageView.setImageBitmap(null);
		fileUri = null;
	}

	private void startCameraIntent() {
		String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getPath();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		fileUri = Uri.fromFile(new java.io.File(mediaStorageDir + java.io.File.separator + "IMG_"
				+ "test" + ".jpg"));

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(cameraIntent, Contants.CAPTURE_IMAGE);
	}

	private void startGalleryIntent() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, Contants.SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		switch (requestCode) {
		case Contants.CAPTURE_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				// Bitmap photo = (Bitmap) data.getExtras().get("data");
				// imageView.setImageBitmap(photo);
				imageView.setImageURI(fileUri);
			}
			break;
		case Contants.SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Log.e("data", "dataat " + data);
				fileUri = data.getData();
				// InputStream imageStream =
				// getContentResolver().openInputStream(selectedImage);
				// Bitmap yourSelectedImage =
				// BitmapFactory.decodeStream(imageStream);
				imageView.setImageBitmap(null);
				imageView.setImageURI(fileUri);
			}
			break;
		default:
			return;
		}
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
