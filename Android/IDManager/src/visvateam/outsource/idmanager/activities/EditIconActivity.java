package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.contants.Contants;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class EditIconActivity extends Activity {
	ImageView imageView;
	private Uri fileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_edit_icon);
		imageView=(ImageView) findViewById(R.id.id_img_icon_edit);
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, EditIconActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onLibrary(View v) {
		startGalleryIntent();
	}

	public void onInternet(View v) {
		GetInternetImageActivity.startActivity(this);
	}

	private void startGalleryIntent() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, Contants.SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {

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
}
