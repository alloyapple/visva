package com.japanappstudio.IDxPassword.idletime;

import android.util.Log;

public class Waiter extends Thread {
	private static final String TAG = Waiter.class.getName();
	private long lastUsed;
	private long period;
	private boolean stop;
	private ControlApplication app;
	private long idle = 0;
	private boolean check = true;

	public Waiter(long period, ControlApplication app) {
		this.period = period;
		stop = false;
		this.app = app;
	}

	public void run() {
		idle = 0;
		lastUsed = System.currentTimeMillis();
		this.touch();
		do {
			if (check)
				idle = System.currentTimeMillis() - lastUsed;
			Log.d(TAG, "Application is idle for " + idle + " ms");
			try {
				Thread.sleep(1000); // check every 5 seconds
			} catch (InterruptedException e) {
				Log.d(TAG, "Waiter interrupted!");
			}
			if (check && idle > period) {
				check = false;
				app.startMasterPass();
				idle=0;
				// do something here - e.g. call popup or so
			}
		} while (!stop);
		Log.d(TAG, "Finishing Waiter thread");
	}

	public synchronized void touch() {
		lastUsed = System.currentTimeMillis();
	}

	public synchronized void forceInterrupt() {
		this.interrupt();
	}

	// soft stopping of thread
	public synchronized void stop(boolean b) {
		stop = true;
	}

	public synchronized void setPeriod(long period) {
		this.period = period;
	}
	public synchronized void setLastUsed() {
		this.lastUsed = System.currentTimeMillis();
	}
	public synchronized void setIdle(long idle) {
		this.idle = idle;
	}

	public synchronized void setCheck(boolean b) {
		check = b;
	}
}
