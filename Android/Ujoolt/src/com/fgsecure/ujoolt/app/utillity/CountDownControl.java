package com.fgsecure.ujoolt.app.utillity;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

public class CountDownControl {
	CountDownTimer countDown;
	public boolean isCount;
	TextView textView;
	long timeToLive;
	long time;

	public CountDownControl(TextView t, long lifeTime, long curTime, long startTime) {
		// TODO Auto-generated constructor stub

		textView = t;
		long timeLived = curTime - startTime;
		timeToLive = lifeTime - timeLived;

		Log.e("timeLived", " " + timeLived);
		Log.e("time To Live", " " + timeToLive);

		if (timeToLive < 0) {
			textView.setText("00:00:00");
		}

		countDown = new CountDownTimer(timeToLive * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				if (isCount) {
					long timeLeft = millisUntilFinished / 1000;
					int hour = (int) timeLeft / 3600;
					int minute = (int) (timeLeft - hour * 3600) / 60;
					int second = (int) (timeLeft - hour * 3600 - minute * 60);
					String time;
					if (hour < 10) {
						time = " 0" + hour;
					} else {
						time = " " + hour;
					}

					if (minute < 10) {
						time += ":0" + minute;
					} else {
						time += ":" + minute;
					}

					if (timeLeft % 60 < 10) {
						time += ":0" + second;
					} else
						time += ":" + second;
					if (textView != null) {
						textView.setText(time);
					}
				}
			}

			@Override
			public void onFinish() {

			}
		};

	}

	public void start() {
		isCount = true;
		if (countDown != null) {
			countDown.start();
		}
	}

	public void stop() {
		isCount = false;
		if (countDown != null) {
			countDown.cancel();
		}
	}
}
