package com.visva.android.ailatrieuphu_visva.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SdCardPath")
@SuppressWarnings("deprecation")
public class DBConnector extends SQLiteOpenHelper {
	public static final String KEY_ID = "_id";
	private static final String DATABASE_NAME = "Question.mp3";
	private static final String DATABASE_PATH = "/data/data/com.visva.android.ailatrieuphu_visva/databases/";
	private static final int DATABASE_VERSION = 1;

	public static final String QUESTION = "Question";
	public static final String CASE_A = "CaseA";
	public static final String CASE_B = "CaseB";
	public static final String CASE_C = "CaseC";
	public static final String CASE_D = "CaseD";
	public static final String TRUE_CASE = "TrueCase";
	public static final String PASS = "Pass";

	private SQLiteDatabase myDB;
	private final Context ctx;

	public DBConnector(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean isOpen() {
		return myDB.isOpen();
	}

	public synchronized void close() {
		if (myDB != null && myDB.isOpen())
			myDB.close();
		super.close();
	}

	public boolean checkAndCopyDatabase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			return true;
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
				openDataBase();
				createIndexInFirstPlay();
				close();
			} catch (IOException e) {
				Toast.makeText(ctx, "Hãy Reboot lại máy...",
						Toast.LENGTH_LONG).show();
			}
			return false;
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			Log.v("DATABASE", "OPEN CHECK");
		} catch (SQLiteException e) {
		}
		if (checkDB != null) {
			checkDB.close();
			Log.v("DATABASE", "CLOSE CHECK");
		}

		return checkDB != null ? true : false;
	}

	private int copyDataBase() throws IOException {
		InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		int total = 0;

		while ((length = myInput.read(buffer)) > 0) {
			total++;
			myOutput.write(buffer, 0, length);

		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
		return total;
	}

	public void openDataBase() throws SQLException {
		if (myDB == null || (myDB != null && !myDB.isOpen())) {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			myDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		}
	}

	public Cursor getQuestion(String tableName, int id) {
		Cursor cursor = null;
		try {
			cursor = myDB.query(tableName, new String[] { KEY_ID, QUESTION,
					CASE_A, CASE_B, CASE_C, CASE_D, TRUE_CASE }, KEY_ID + " = "
					+ id, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public Cursor getIndex(String tableName, int level) {
		Cursor cursor = null;
		try {
			cursor = myDB.query(tableName, new String[] { KEY_ID, PASS, },
					KEY_ID + " = " + level, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public void setIndex(int level, int index) {
		ContentValues inititalValues = new ContentValues();
		inititalValues.put(PASS, index);
		myDB.update("Manager", inititalValues, KEY_ID + "=" + level, null);  
	}

	public void createIndexInFirstPlay() {
		Random r = new Random();
		for (int i = 0; i < 15; i++) {
			setIndex(i + 1, r.nextInt(150));
		}
	}

	public Question getData(int level) {
		Cursor sugCursor = null;
		int id = getId(level);
		try {
			sugCursor = this
					.getQuestion("Question" + String.valueOf(level), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		((Activity) ctx).startManagingCursor(sugCursor);
		Question question = new Question();
		question.setLevel(level);
		if (!sugCursor.isClosed() && sugCursor.moveToFirst()) {
			do {
				//Cursor cur = sugCursor;
				question.set_id(sugCursor.getInt(0));
				question.set_question_content(sugCursor.getString(1));
				question.set_answer_a(sugCursor.getString(2));
				question.set_answer_b(sugCursor.getString(3));
				question.set_answer_c(sugCursor.getString(4));
				question.set_answer_d(sugCursor.getString(5));
				question.set_correct_answer(sugCursor.getInt(6));
				question.swap_correct_answer();
			} while (sugCursor.moveToNext());
		}
		if (sugCursor != null && !sugCursor.isClosed()) {
			((Activity) ctx).stopManagingCursor(sugCursor);
			sugCursor.close();
		}
		this.setIndex(level, id + 1);
		return question;
	}

	public int getId(int level) {
		int index = 1;
		Cursor sugCursor = getIndex("Manager", level);
		((Activity) ctx).startManagingCursor(sugCursor);
		if (!sugCursor.isClosed() && sugCursor.moveToFirst()) {
			do {
				//Cursor cur = sugCursor;
				index = sugCursor.getInt(1);
			} while (sugCursor.moveToNext());
		}
		if (sugCursor != null && !sugCursor.isClosed()) {
			((Activity) ctx).stopManagingCursor(sugCursor);
			sugCursor.close();
		}
		return index;
	}
}
