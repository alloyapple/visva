package com.visva.android.app.funface.view.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.visva.android.app.funface.R;

import com.visva.android.app.funface.photointent.AlbumStorageDirFactory;
import com.visva.android.app.funface.photointent.BaseAlbumDirFactory;
import com.visva.android.app.funface.photointent.FroyoAlbumDirFactory;

public class FaceDetectionExample extends Activity {
	private static final int TAKE_PICTURE_CODE = 100;
	private static final int REQUEST_CODE_GALLERY = 101;
	private static final int MAX_FACES = 10;

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	private Bitmap cameraBitmap = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((Button) findViewById(R.id.take_picture)).setOnClickListener(btnClick);

		((Button) findViewById(R.id.take_picture_from_gallery))
				.setOnClickListener(btnClick);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null || resultCode == RESULT_CANCELED)
		    return;
		if (TAKE_PICTURE_CODE == requestCode) {
			processCameraImage(data);
		} else if (REQUEST_CODE_GALLERY == requestCode) {

			Uri uri = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(uri, filePathColumn,
					null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String imagePath = cursor.getString(columnIndex);
			cursor.close();

			File file = new File(imagePath);
			if (file.exists()) {
				cameraBitmap = BitmapFactory.decodeFile(imagePath);
				setContentView(R.layout.detectlayout);

				((Button) findViewById(R.id.detect_face))
						.setOnClickListener(btnClick);

				ImageView imageView = (ImageView) findViewById(R.id.image_view);

				imageView.setImageBitmap(cameraBitmap);
			}
		}
	}

	private void openCamera() {
		Intent takePictureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File f = null;

		try {
			f = setUpPhotoFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		} catch (IOException e) {
			e.printStackTrace();
			f = null;
			mCurrentPhotoPath = null;
		}
		startActivityForResult(takePictureIntent, TAKE_PICTURE_CODE);
	}

	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();

		return f;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	private void processCameraImage(Intent intent) {
		setContentView(R.layout.detectlayout);

		((Button) findViewById(R.id.detect_face)).setOnClickListener(btnClick);

		ImageView imageView = (ImageView) findViewById(R.id.image_view);

		// cameraBitmap = (Bitmap) intent.getExtras().get("data");

		imageView.setImageBitmap(cameraBitmap);
		handleBigCameraPhoto(imageView);
	}

	private void handleBigCameraPhoto(ImageView imageView) {
		if (mCurrentPhotoPath != null) {
			setPic(imageView);
			galleryAddPic();
			mCurrentPhotoPath = null;
		}
	}

	/**
	 * rotate image to have the exact orientation
	 * 
	 * @param fileUri
	 * @return
	 */
	private int checkOrientation(Uri fileUri) {
		int rotate = 0;
		String imagePath = fileUri.getPath();
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(imagePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Since API Level 5
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

	private Bitmap decodeBitmap(Bitmap bitmap, int orientation) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(orientation);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				"android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private void setPic(ImageView imageView) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
		int orientation = checkOrientation(uri);
		cameraBitmap = decodeBitmap(bitmap, orientation);
		/* Associate the Bitmap to the ImageView */
		imageView.setImageBitmap(cameraBitmap);
	}

	private void detectFaces() {
		if (null != cameraBitmap) {
			int width = cameraBitmap.getWidth();
			int height = cameraBitmap.getHeight();

			FaceDetector detector = new FaceDetector(width, height,
					FaceDetectionExample.MAX_FACES);
			Face[] faces = new Face[FaceDetectionExample.MAX_FACES];

			Bitmap bitmap565 = Bitmap.createBitmap(width, height,
					Config.RGB_565);
			Paint ditherPaint = new Paint();
			Paint drawPaint = new Paint();

			ditherPaint.setDither(true);
			drawPaint.setColor(Color.RED);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeWidth(2);

			Canvas canvas = new Canvas();
			canvas.setBitmap(bitmap565);
			canvas.drawBitmap(cameraBitmap, 0, 0, ditherPaint);

			int facesFound = detector.findFaces(bitmap565, faces);
			PointF midPoint = new PointF();
			float eyeDistance = 0.0f;
			float confidence = 0.0f;

			Log.i("FaceDetector", "Number of faces found: " + facesFound);

			if (facesFound > 0) {
				for (int index = 0; index < facesFound; ++index) {
					faces[index].getMidPoint(midPoint);
					eyeDistance = faces[index].eyesDistance();
					confidence = faces[index].confidence();
					float facex = faces[index].pose(Face.EULER_X);
					float facey = faces[index].pose(Face.EULER_Y);

					
					Log.i("FaceDetector", "Confidence: " + confidence
							+ ", Eye distance: " + eyeDistance
							+ ", Mid Point: (" + midPoint.x + ", " + midPoint.y
							+ ")");

					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
					Log.d("FaceDetector", "bitmap:" + bitmap);
					Rect src = new Rect(0, 0, bitmap.getWidth(),
							bitmap.getHeight());
					Rect dst = new Rect(
							(int) (midPoint.x - eyeDistance * 2.0f),
							(int) (midPoint.y - eyeDistance * 2.0f),
							(int) (midPoint.x + eyeDistance * 2.0f),
							(int) (midPoint.y + eyeDistance * 2.0f));
					canvas.drawBitmap(bitmap, src, dst, drawPaint);
				}
			}
			
			//
			saveImageToSdCard(bitmap565);
		}
	}

	private void saveImageToSdCard(Bitmap bitmap) {
//		String filepath = Environment.getExternalStorageDirectory()
//				+ "/facedetect" + System.currentTimeMillis() + ".png";
//
//		try {
//			FileOutputStream fos = new FileOutputStream(filepath);
//
//			bitmap.compress(CompressFormat.PNG, 100, fos);
//
//			fos.flush();
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		ImageView imageView = (ImageView) findViewById(R.id.image_view);

		imageView.setImageBitmap(bitmap);
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.take_picture:
				openCamera();
				break;
			case R.id.detect_face:
				detectFaces();
				break;
			case R.id.take_picture_from_gallery:
				takePictureFromGallery();
				break;
			}
		}
	};

	private void takePictureFromGallery() {
		// when user click gallery to get image
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
	}
}
