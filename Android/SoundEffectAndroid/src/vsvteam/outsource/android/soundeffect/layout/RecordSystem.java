package vsvteam.outsource.android.soundeffect.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vsvteam.outsource.android.soundeffect.util.SoundEffectSharePreference;

/**
 * 
 * @author vsvteam
 */
@SuppressLint("SimpleDateFormat")
public class RecordSystem {

	private boolean isRecording;
	private String fileLocation;
	private String fileNamePath;
	private File dataDir;
	public RecordSystem(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public RecordSystem(Context context) {
		dataDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/"
				+ context.getApplicationContext().getPackageName() + "/records/");
		createFilePath(dataDir);
		SoundEffectSharePreference.getInstance(context);
	}

	public void stopRecord() {
		isRecording = false;
	}

	@SuppressWarnings("deprecation")
	public void startRecord() {
		isRecording = true;
		int frequency = 11025;
		int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

		fileNamePath = (new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp3")
				.toString();
		this.fileLocation = dataDir + "/" + fileNamePath;
		File file = new File(fileLocation);

		// create the new file on SD
		try {
			file.createNewFile();
		} catch (IOException e) {
			return;
			//throw new IllegalStateException("Failed to create " + file.toString());
		}

		try {
			// create a DataOuputStream to write the audio data into the saved
			// file
			OutputStream os = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			DataOutputStream dos = new DataOutputStream(bos);

			int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration,
					audioEncoding);
//			int bufferSize = soundEffectSharePreference.getBufferSizeValue();
			// create a new AudioRecord object to record the audio
			// uses the permission 'android.permission.RECORD_AUDIO' in the
			// manifest
			AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
					channelConfiguration, audioEncoding, bufferSize);

			short[] buffer = new short[bufferSize];
			audioRecord.startRecording();

			while (isRecording) {
				// grab the buffered input (mic) and write it to a file on the
				// SD
				int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
				for (int i = 0; i < bufferReadResult; i++) {
					dos.writeShort(buffer[i]);
				}
			}

			audioRecord.stop();
			dos.flush();
			dos.close();

		} catch (Throwable t) {
			Log.e("AudioRecord", "Recording Failed");
		}
	}

	/**
	 * create file path hold incoming phone call
	 * 
	 * @param file
	 * @return
	 */
	private boolean createFilePath(File file) {
		if (file.mkdirs()) {
			return true;
		} else
			return false;
	}
}