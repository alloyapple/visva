package com.fgsecure.ujoolt.app.camera.galaxys;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

public class VideoViewActivityS extends Activity implements OnClickListener,
		OnPreparedListener {

	// ==========================Control define=======================
	private Button btnRetake;
	private Button btnUse;
	private VideoView videoView;
	private Button btnPlay;

	// ==========================Variable define ======================
	private String fileName;
	private int timeOfVideo;
	private boolean isPhoto;
	private boolean isCameraBack;
	private ImageView imageViewThumnail;
	private ImageView imageViewGallery;
	private FrameLayout frameLayoutThumnail;
	private Uri uriImage;
	private Bitmap avatar;

	// private static final int SELECT_PICTURE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_media_view_s);
		// init Button
		btnRetake = (Button) findViewById(R.id.btn_retake_video);
		btnRetake.setOnClickListener(this);
		btnUse = (Button) findViewById(R.id.btn_use_video);
		btnUse.setOnClickListener(this);
		btnPlay = (Button) findViewById(R.id.btn_play_video);
		btnPlay.setOnClickListener(this);
		imageViewThumnail = (ImageView) findViewById(R.id.image_thumnail);
		imageViewGallery = (ImageView) findViewById(R.id.image_view);
		videoView = (VideoView) findViewById(R.id.video_view);
		frameLayoutThumnail = (FrameLayout) findViewById(R.id.frame_thumnail);
		// get variable from intent
		isPhoto = getIntent().getExtras().getBoolean("isPhoto");
		fileName = getIntent().getExtras().getString("fileName");
		isCameraBack = getIntent().getExtras().getBoolean("isCameraBack");

		if (isPhoto) {
			frameLayoutThumnail.setVisibility(View.GONE);
			videoView.setVisibility(View.GONE);
			imageViewGallery.setVisibility(View.VISIBLE);
			uriImage = Uri.parse(fileName);
			imageViewGallery.setImageURI(uriImage);
		} else {
			avatar = ThumbnailUtils.createVideoThumbnail(fileName,
					MediaStore.Images.Thumbnails.MINI_KIND);
			imageViewThumnail.setBackgroundDrawable(new BitmapDrawable(avatar));
			controlVideoRecording();
		}
	}

	// add controls to a MediaPlayer like play, pause.
	private void controlVideoRecording() {
		MediaController mc = new MediaController(VideoViewActivityS.this);
		videoView.setMediaController(mc);
		mc.setAnchorView(videoView);
		mc.setMediaPlayer(videoView);
		// Set the path of Video or URI
		videoView.setVideoURI(Uri.parse(fileName));
		// String myPath = getRealPathFromURI(photoUri);
		videoView.setPressed(true);
		// Set the focus
		videoView.requestFocus();

		MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(fileName));
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		videoView.setOnPreparedListener(this);
		onPrepared(mediaPlayer);
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		if (cursor == null)
			return null;

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnRetake) {
			Intent i = null;
			i = new Intent(VideoViewActivityS.this, DgCamActivityS.class);
			i.putExtra("isPhoto", isPhoto);
			i.putExtra("isCameraBack", isCameraBack);
			startActivity(i);
			finish();
		} else if (v == btnUse) {

			MainScreenActivity.bitmapJoltAvatar = avatar;
			MainScreenActivity.btnUploadPhoto
					.setBackgroundDrawable(new BitmapDrawable(avatar));

			JoltHolder.pathVideoToUpLoad = fileName;
			JoltHolder.isPhoto = false;

			finish();
		} else if (v == btnPlay) {
			frameLayoutThumnail.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			videoView.start();
			Log.d("time", "" + timeOfVideo);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		long mediatime = videoView.getDuration() / 1000;// this returned
		// the
		// duration instead of//
		Log.i(": mediatime", mediatime + "");
		int hour = (int) mediatime / 3600;
		int minute = (int) (mediatime - hour * 3600) / 60;
		int second = (int) (mediatime - hour * 3600 - minute * 60);
		timeOfVideo = second + 60 * minute + hour * 3600;
		// Toast.makeText(getBaseContext(), hour + ": " + minute + ": " +
		// second,
		// Toast.LENGTH_SHORT).show();
		// if (mediatime > 15) {
		// Toast.makeText(getBaseContext(), "out of time2 ",
		// Toast.LENGTH_SHORT).show();
		// }
	}
}
