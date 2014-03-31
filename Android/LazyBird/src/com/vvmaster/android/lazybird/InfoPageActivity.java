package com.vvmaster.android.lazybird;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class InfoPageActivity extends Activity {
	public static final int FINISH = 0;
	public static final int NO_FINISH = 1;
	private int mMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Run as full-screen
		if (savedInstanceState != null) {
			mMode = savedInstanceState.getInt("modeIntent",
					StoryActionActivity.MODE_INFO);

		} else {
			Intent intent = getIntent();
			mMode = intent.getIntExtra("modeIntent",
					StoryActionActivity.MODE_INFO);

		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.info_page);
		final Button btn_review = (Button) findViewById(R.id.id_btn_review);
		btn_review.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String site_review = getResources().getString(
						R.string.site_review);
				Intent myWebLink = new Intent(
						android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri.parse(site_review));
				startActivity(myWebLink);
			}
		});
		final Button btn_tell_a_friend = (Button) findViewById(R.id.id_btn_tell_a_friend);
		btn_tell_a_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String share_message = getResources().getString(
						R.string.share_message);
				String share_title = getResources().getString(
						R.string.share_title);
				shareContent(share_title, share_message);
			}
		});
		final Button btn_feedback = (Button) findViewById(R.id.id_btn_feedback);
		btn_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String feedBackMessage = getResources().getString(
						R.string.feedback_content);
				String feedBackEmail = getResources().getString(
						R.string.feedback_email);
				String feedBackTitle = getResources().getString(
						R.string.feedback_title);
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { feedBackEmail });// alex.do@setacinq.vn//son.dt1.fet.hut@gmail.com
				email.putExtra(Intent.EXTRA_SUBJECT, feedBackTitle);
				email.putExtra(Intent.EXTRA_TEXT, feedBackMessage);

				// need this to prompts email client only
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email,
						"Choose an Email client :"));
			}
		});
		final Button btn_more_stories = (Button) findViewById(R.id.id_btn_more_ifp_stories);
		btn_more_stories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MoreStoriesActivity.startIt(InfoPageActivity.this, MoreStoriesActivity.MODE_INFO);
				finish();
			}
		});
		final ImageButton btn_back = (ImageButton) findViewById(R.id.btn_ifp_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mMode != StoryActionActivity.MODE_MENU) {
					Intent intentResult = new Intent();
					setResult(NO_FINISH, intentResult);
					finish();
				} else {
					Intent intent = new Intent(InfoPageActivity.this,
							MenuActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		final ImageButton btn_house = (ImageButton) findViewById(R.id.btn_ifp_house);
		btn_house.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mMode != StoryActionActivity.MODE_MENU) {
					Intent intentResult = new Intent();
					setResult(FINISH, intentResult);
					finish();
					Intent intent = new Intent(InfoPageActivity.this,
							MenuActivity.class);
					InfoPageActivity.this.startActivity(intent);
				} else {
					Intent intent = new Intent(InfoPageActivity.this,
							MenuActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		ImageView img_the_end = (ImageView) findViewById(R.id.id_text_the_end);
		if (mMode != StoryActionActivity.MODE_END)
			img_the_end.setVisibility(View.GONE);
	}

	public static void startIt(Activity caller, int mode) {
		Intent intent = new Intent(caller, InfoPageActivity.class);
		caller.startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mMode != StoryActionActivity.MODE_MENU) {
				Intent intentResult = new Intent();
				setResult(NO_FINISH, intentResult);
				finish();
			} else {
				Intent intent = new Intent(InfoPageActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/*
	 * share content tell a friend
	 */
	private void shareContent(String subject, String content) {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		// set the type
		shareIntent.setType("text/plain");
		// add a subject
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		// build the body of the message to be shared
		String shareMessage = content;
		// add the messagelăm
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
		// start the chooser for sharing
		startActivity(Intent.createChooser(shareIntent, "Share"));
	}
}
