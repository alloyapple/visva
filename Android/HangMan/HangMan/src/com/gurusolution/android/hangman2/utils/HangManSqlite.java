package com.gurusolution.android.hangman2.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HangManSqlite extends SQLiteOpenHelper{
	private static SQLiteDatabase hmDatabase;
	static final String CATEGORY_TABLE_NAME = "sqlite_sequence";

	private String DB_PATH = "/data/data/com.gurusolution.android.hangman2/databases/";
	 
	private static String DB_NAME = "word_list.sqlite";
	private final Context myContext;
	public static final String _ID = "id";
	public static final String KEY_SEQ = "seq";
	public static final String KEY_NAME = "name";    
	private static final int DATABASE_VERSION = 10;
	   /**
	*
     */
    public HangManSqlite(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
        	this.getReadableDatabase();
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
 
    }
    /**
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    	}catch(SQLiteException e){
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * */
    private void copyDataBase() throws IOException{
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	String outFileName = DB_PATH + DB_NAME;
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    public void openDataBase() throws SQLException{
    	 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        hmDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }
	  @Override
		public synchronized void close() {
	 
	    	    if(hmDatabase != null)
	    	    	hmDatabase.close();
	    	    super.close();
		}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	//Design by iRocker
	public String GetRandomWord(String categoryName){
		String word = "";
		Cursor cursor = null;
		cursor =  hmDatabase.query(CATEGORY_TABLE_NAME, new String[] {KEY_NAME,CategoryColumns.SEQ}, null, null, null, null, "seq");
		Random rand = new Random();
		if(cursor.moveToFirst()){
			for(int i = 0; i < cursor.getCount(); i ++){
				cursor.moveToPosition(i);
				if(cursor.getString(cursor.getColumnIndex(WordColumns.NAME)).equalsIgnoreCase(categoryName)){
					Log.e("Column",categoryName.toString());
					break;
				}
			}
			int seq = cursor.getInt(cursor.getColumnIndex(CategoryColumns.SEQ));
			Log.e("seg", String.valueOf(seq));
			cursor = hmDatabase.query(categoryName, new String[] {KEY_NAME},WordColumns.ID + "=" + rand.nextInt(seq) , null, null, null, "id");
			if(cursor.moveToFirst()){
				word = cursor.getString(cursor.getColumnIndex(WordColumns.NAME));
			}
		}
		return word;
	}
}
