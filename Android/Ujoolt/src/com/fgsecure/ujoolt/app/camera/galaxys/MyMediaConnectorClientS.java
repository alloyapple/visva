package com.fgsecure.ujoolt.app.camera.galaxys;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MyMediaConnectorClientS implements MediaScannerConnectionClient {

	String _fisier;
	MediaScannerConnection MEDIA_SCANNER_CONNECTION;

	public MyMediaConnectorClientS(String nume) {
		_fisier = nume;
	}

	public void setScanner(MediaScannerConnection msc) {
		MEDIA_SCANNER_CONNECTION = msc;
	}

	@Override
	public void onMediaScannerConnected() {
		MEDIA_SCANNER_CONNECTION.scanFile(_fisier, null);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		if (path.equals(_fisier))
			MEDIA_SCANNER_CONNECTION.disconnect();
	}

}