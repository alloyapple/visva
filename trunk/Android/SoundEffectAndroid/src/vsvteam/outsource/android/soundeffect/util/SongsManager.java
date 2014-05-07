package vsvteam.outsource.android.soundeffect.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Environment;

public class SongsManager {
	// SDCard Path
	final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
	final String EXTENAL_PATH = MEDIA_PATH;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> artistList = new ArrayList<HashMap<String, String>>();

	// Constructor
	public SongsManager() {

	}

	public ArrayList<HashMap<String, String>> getArtistList(String artistName) {
		songList = getPlayList();
		for (int i = 0; i < songList.size(); i++) {
			String[] splitString = songList.get(i).get("songTitle").split("_");
			File file = new File(songList.get(i).get("songPath"));
			if ((splitString.length > 1 && artistName.equals(splitString[1]))
					|| (splitString.length <= 1 && artistName.equals("Unknown"))) {
				HashMap<String, String> song = new HashMap<String, String>();
				// get author,size,path,title of song here
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());
				song.put("albumn", file.getParent().toString());
				// Adding each song to SongList
				artistList.add(song);
			}
		}
		return artistList;
	}

	public ArrayList<HashMap<String, String>> getAlbumnList(String albumnPath) {
		File home = new File(albumnPath);
		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {

				HashMap<String, String> song = new HashMap<String, String>();
				// get author,size,path,title of song here
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());
				song.put("albumn", file.getParent().toString());

				// Adding each song to SongList
				songsList.add(song);
			}
		}
		return songsList;
	}

	/**
	 * Function to read all mp3 files from sdcard and store the details in
	 * ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList() {
		File extenalFile = new File(EXTENAL_PATH);
		if (extenalFile.isDirectory()) {
			String externalFiles[] = extenalFile.list();
			for (int i = 0; i < externalFiles.length; i++) {
				File externalHome = new File(EXTENAL_PATH + externalFiles[i]);
				if (externalHome != null) {
					getMusicFromExternalSdCard(externalHome);
				}
			}
		}
		// return songs list array
		return songsList;
	}

	/**
	 * get file.mp3 from sdcard
	 * 
	 * @param home
	 */
	private void getMusicFromExternalSdCard(File home) {
		if (home.isDirectory() && home.canRead()) {

			String files[] = home.list();
			if (!files.equals(""))
				for (int i = 0; i < files.length; i++) {
					File file = new File(home.getPath() + "/" + files[i]);
					getMusicFromExternalSdCard(file);
				}

			if (home.listFiles(new FileExtensionFilter()).length > 0) {
				for (File file : home.listFiles(new FileExtensionFilter())) {

					HashMap<String, String> song = new HashMap<String, String>();
					// get author,size,path,title of song here
					song.put("songTitle", file.getName()
							.substring(0, (file.getName().length() - 4)));
					song.put("songPath", file.getPath());
					song.put("albumn", file.getParent().toString());

					// Adding each song to SongList
					songsList.add(song);

				}
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
}
