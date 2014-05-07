package vsvteam.outsource.android.soundeffect.layout;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class RecordManager {
	private ArrayList<HashMap<String, String>> recordsList = new ArrayList<HashMap<String, String>>();
 
	public RecordManager(Context context) {
		Log.e("getPacakename", "pacejka2"+context.getApplicationContext().getPackageName());
		File dataDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/"
				+ context.getApplicationContext().getPackageName() + "/records/");
		Log.e("dataDir ", "dataDir "+dataDir.getAbsolutePath());
		if (dataDir != null && dataDir.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : dataDir.listFiles(new FileExtensionFilter())) {

				HashMap<String, String> song = new HashMap<String, String>();
				// get author,size,path,title of song here
				song.put("recordTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("recordPath", file.getPath());
				// Adding each song to SongList
				recordsList.add(song);
			}
		}
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}

	public ArrayList<HashMap<String, String>> getPlayList() {
		// TODO Auto-generated method stub
		return recordsList;
	}
}
