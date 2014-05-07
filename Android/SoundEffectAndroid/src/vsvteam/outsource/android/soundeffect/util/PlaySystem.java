package vsvteam.outsource.android.soundeffect.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class PlaySystem {

	private boolean isPlaying;
	private String fileLocation;

	public PlaySystem(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public void stopPlay() {
		isPlaying = false;
	}

	@SuppressWarnings("deprecation")
	public void startPlay(int playFrequency) {
		isPlaying = true;
		int frequency = 11025;
		int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

		File file = new File(fileLocation);

		try {
			InputStream is = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			DataInputStream dis = new DataInputStream(bis);

			int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration,
					audioEncoding);

			// initialize an audiotrack with a different frequency then
			// originally used for the recording
			// this wil automatically manipulate the 'pitch' of the sound
			AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, playFrequency,
					channelConfiguration, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);

			// start playback
			audioTrack.play();

			short[] buffer = new short[bufferSize];

			while (isPlaying) {
				for (int i = 0; i < bufferSize; i++) {
					// write the 'short' buffer blocks to the audiotrack
					try {
						short s = dis.readShort();
						buffer[i] = s;
					} catch (EOFException eofe) {
						isPlaying = false;
					}
				}
				audioTrack.write(buffer, 0, bufferSize);
			}

			audioTrack.stop();
			dis.close();

		} catch (Throwable t) {
			Log.e("AudioPlay", "Playing Failed");
		}
	}
}