package visvateam.outsource.idmanager.contants;

import android.os.Environment;

/**
 * This class initialize contant values for project
 * 
 * @author KieuThang
 * 
 */
public class Contants {
	public static final String KEY_TO_BROWSER = "browserActivity";
	public static final int INFO_TO = 0;
	public static final int PASTE_TO = 1;

	public static final int DIALOG_ADD_NEW_FOLDER = 0;
	public static final int DIALOG_DELETE_FOLDER = 1;
	public static final int DIALOG_EDIT_FOLDER = 2;
	public static final int DIALOG_DELETE_ID = 3;
	public static final int DIALOG_EDIT_ID = 4;
	public static final int DIALOG_CREATE_ID = 5;
	public static final int DIALOG_EXIT = 6;
	public static final int DIALOG_WRONG_PASS = 7;
	public static final int DIALOG_EMPTY_PASS = 8;
	public static final int DIALOG_NO_NET_WORK = 9;
	public static final int DIALOG_CHOICE_CLOUD_TYPE = 10;
	public static final int DIALOG_MOVE_ID_TO_FOLDER = 11;
	public static final int IS_SEARCH_MODE = 12;
	
	public static final int NUMBER_FOLDER_DEFALT = 3;
	public static final int TYPE_FOLDER_NORMAL = 1;
	public static final int TYPE_FOLDER_NON_NORMAL = 0;
	public static final int TEXT_ID = 0;
	public static final int TEXT_NUMBER_CHARACTER_ID = 1000;

	public static final int CAPTURE_IMAGE = 1;
	public static final int SELECT_PHOTO = 2;
	public static final int INTENT_IMG_MEMO = 3;

	public static final String IS_INTENT_CREATE_NEW_ID = "IS_INTENT_CREATE_NEW_ID";
	public static final String NAME_HISTORY_FOLDER = "history";
	public static final String NAME_SEARCH_FOLDER = "search";
	public static final String NAME_FAVOURITE_FOLDER = "favourite";
	public static final String FIlE_PATH_IMG_MEMO = "FIlE_PATH_IMG_MEMO";
	public static final String CURRENT_FOLDER_ID = "CURRENT_FOLDER_ID";
	public static final String CURRENT_PASSWORD_ID = "CURRENT_PASSWORD_ID";

	public static final int MAX_ITEM_PASS_ID = 12;
	public static final String DATA_IDMANAGER_NAME = "idmanager.db";
	public static final int DATA_VERSION = 1;
	public static final String KEY_DATA_PW = "key_data_id_manager";
	public static final int KEY_OFF = 0;
	public static final int MIN_KEY_TO_REMOVE_DATA = 1;
	public static final String PATH_ID_FILES = Environment.getExternalStorageDirectory().getPath()
			+ "/IDManager/Files/";
	public static final String FOLDER_ON_DROPBOX = "/Documents/";
	public static final String IS_CHANGE_PASSWORD = "isChangePW";
	public static int MASTER_PASSWORD_ID = 101;
	public static final String IS_SYNC_TO_CLOUD = "IS_SYNC_TO_CLOUD";
	public static final String FILE_CSV_NAME = "idmanager.csv";
	public static final String IS_EXPORT_FILE = "IS_EXPORT_FILE";
	

}
