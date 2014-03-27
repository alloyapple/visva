package com.visva.android.flashlight.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareUtilities {

	private Context context;
	private Activity activity;

	public static final String TITLE = " Flash Light + LED(Free) - Android Apps on Google Play";
	public static final String URL_ANDROID_MARKET = "https://play.google.com/store/apps/details?id=com.visva.android.flashlight";
	public static final String URL_FACEBOOK = "http://m.facebook.com/sharer.php?u=https%3A%2F%2Fmarket.android.com%2Fdetails%3Fid%3Dcom.devuni.flashlight&t=Brightest.+Fastest.+Simplest.+Flashlight+for+your+device.&_rdr";
	public static final String URL_TWITTER = "http://bit.ly/hgU3me";
	public static final String SUBJECT = "Check this amazing app";
	public static final String BODY = "Flashlight is the brightest, fastest, and simples flashlight app for Android. Check it out..."
			+ "\n\n" + URL_ANDROID_MARKET;

	public ShareUtilities(Activity activity) {
		this.activity = activity;
		this.context = activity.getBaseContext();
	}

	public String createUrlShare(String title, String summary, String url) {
		String str = "http://www.facebook.com/sharer.php?s=100&p[title]=" + title + "&p[summary]=" + summary
				+ "&p[url]=" + url;
		return str;
	}

	public String createUrlShareFaceBook(String url, String title) {
		String str = "http://m.facebook.com/sharer.php?u=" + url + "&t=" + title + "&_rdr";
		return str;
	}

	public String createUrlShateTwitter(String url, String title) {
		String str = "http://twitter.com/intent/tweet?source=sharethiscom&text=" + title + "&url=" + url;
		return str;
	}

	public String createUrlShareGooglePlus() {
		String str = "https://accounts.google.com/ServiceLogin?service=oz&passive=1209600&continue=https://plusone.google.com/_/%2B1/confirm?hl%3Den%26url%3Dhttps://market.android.com/details?id%253Dcom.devuni.flashlight&followup=https://plusone.google.com/_/%2B1/confirm?hl%3Den%26url%3Dhttps://market.android.com/details?id%253Dcom.devuni.flashlight&hl=en";
		return str;
	}

	public void shareFacebook() {
		Intent facebook = new Intent(Intent.ACTION_VIEW);
		facebook.setData(Uri.parse(createUrlShareFaceBook(URL_ANDROID_MARKET, TITLE)));
		activity.startActivity(facebook);
	}

	public void shareTwitter() {
		Intent twitter = new Intent(Intent.ACTION_VIEW);
		twitter.setData(Uri.parse(createUrlShateTwitter(URL_TWITTER, TITLE)));
		activity.startActivity(twitter);
	}

	public void shareGooglePlus() {
		Intent googlePlus = new Intent(Intent.ACTION_VIEW);
		googlePlus.setData(Uri.parse(createUrlShareGooglePlus()));
		activity.startActivity(googlePlus);
	}

	public void sendMail(String[] email, String[] cc, String subject, String body) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, email);
		intent.putExtra(Intent.EXTRA_CC, cc);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);
		activity.startActivity(Intent.createChooser(intent, "send"));
	}
}
