package com.fgsecure.ujoolt.app.camera.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;
import com.fgsecure.ujoolt.app.utillity.Language;

public class MediaViewGallery extends Activity implements OnClickListener {

	// ==========================Control define=======================
	private Button btnRetake;
	private Button btnUse;
	private VideoView videoView;
	private Button btnPlay;
	private ImageView imageViewThumnail;
	// private ImageView imageViewGallery;
	private FrameLayout frameLayoutThumnail;
	// ==========================Variable define =====================
	private static String selectedImagePath;
	private static long timeOfVideo;
	// private boolean isPhoto;
	private Bitmap avatar;
	private boolean isGetFile = false;
	private static final int SELECT_PICTURE = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_camera_view_gallery);

		// init Button
		btnRetake = (Button) findViewById(R.id.btn_retake_video1);
		btnRetake.setOnClickListener(this);
		btnUse = (Button) findViewById(R.id.btn_use_video1);
		btnUse.setOnClickListener(this);
		btnPlay = (Button) findViewById(R.id.btn_play_video1);
		btnPlay.setOnClickListener(this);
		imageViewThumnail = (ImageView) findViewById(R.id.image_thumnail1);
		// imageViewGallery = (ImageView) findViewById(R.id.image_view1);
		videoView = (VideoView) findViewById(R.id.video_view1);
		frameLayoutThumnail = (FrameLayout) findViewById(R.id.frame_thumnail1);

		if (!isGetFile)
			openGallery();
	}

	private void openGallery() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_PICK);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				Log.e("file path", "" + selectedImagePath);
				avatar = ThumbnailUtils.createVideoThumbnail(selectedImagePath,
						MediaStore.Images.Thumbnails.MINI_KIND);
				imageViewThumnail.setBackgroundDrawable(new BitmapDrawable(avatar));
				controlVideoRecording();
			}
		} else {
			Intent i = new Intent(MediaViewGallery.this, DgCamActivity.class);
			i.putExtra("isPhoto", false);
			startActivity(i);
			finish();
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	// add controls to a MediaPlayer like play, pause.
	private void controlVideoRecording() {
		isGetFile = true;
		MediaController mc = new MediaController(MediaViewGallery.this);
		videoView.setMediaController(mc);
		mc.setAnchorView(videoView);
		mc.setMediaPlayer(videoView);
		// Set the path of Video or URI
		videoView.setVideoURI(Uri.parse(selectedImagePath));
		videoView.setPressed(true);
		// Set the focus
		videoView.requestFocus();
		videoView.setVisibility(View.VISIBLE);
		imageViewThumnail.setVisibility(View.GONE);
		btnPlay.setVisibility(View.GONE);

		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				long mediatime = videoView.getDuration() / 1000;
				Log.i("Luong: mediatime", mediatime + "");
				timeOfVideo = mediatime;
				Log.d("time of video", "" + timeOfVideo);
				// int hour = (int) mediatime / 3600;
				// int minute = (int) (mediatime - hour * 3600) / 60;
				// int second = (int) (mediatime - hour * 3600 - minute * 60);
				// Toast.makeText(getBaseContext(),
				// hour + ": " + minute + ": " + second,
				// Toast.LENGTH_SHORT).show();
				isGetFile = true;
				if (timeOfVideo > 15) {
					Log.e("time eeee", "" + timeOfVideo);
					showDialogVideoOutOfTime();
				}
			}
		});
		showDialogCheckTimeVideo();

	}

	private void showDialogCheckTimeVideo() {
		imageViewThumnail.setVisibility(View.VISIBLE);
		btnPlay.setVisibility(View.VISIBLE);
	}

	private void showDialogVideoOutOfTime() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Language.alert);
		builder.setMessage(Language.notifyVideoMoreThan15s);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = null;
				i = new Intent(MediaViewGallery.this, DgCamActivity.class);
				i.putExtra("isPhoto", false);
				startActivity(i);
				isGetFile = false;
				selectedImagePath = null;
				dialog.dismiss();
				finish();
			}
		});
		builder.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (v == btnRetake) {
			Intent i = null;
			i = new Intent(MediaViewGallery.this, DgCamActivity.class);
			i.putExtra("isPhoto", false);
			startActivity(i);
			isGetFile = false;
			selectedImagePath = null;
			finish();
		} else if (v == btnUse) {
			MainScreenActivity.bitmapJoltAvatar = avatar;
			MainScreenActivity.btnUploadPhoto.setBackgroundDrawable(new BitmapDrawable(avatar));

			JoltHolder.pathVideoToUpLoad = selectedImagePath;
			JoltHolder.isPhoto = false;
			finish();
		} else if (v == btnPlay) {
			frameLayoutThumnail.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			videoView.start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("file path", "" + selectedImagePath);
		if (selectedImagePath != null) {
			avatar = ThumbnailUtils.createVideoThumbnail(selectedImagePath,
					MediaStore.Images.Thumbnails.MINI_KIND);
			imageViewThumnail.setBackgroundDrawable(new BitmapDrawable(avatar));
			controlVideoRecording();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
		default:
			return super.onKeyDown(keyCode, event);
		}
	}
}