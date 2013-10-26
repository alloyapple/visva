package vn.com.shoppie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class adbSqlHelper extends SQLiteOpenHelper {
	public static final String TAG = "abstract class: aSqlHelper";

	public String DB_NAME = "";
	public static int DB_VERSION = 2;

	private SQLiteDatabase myDataBase;

	public Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public adbSqlHelper(Context context, String dbPath, String dbName) {
		super(context, dbPath + dbName, null, DB_VERSION);
		this.myContext = context;
		this.DB_NAME = dbPath + dbName;

		try {
			if (!DB_NAME.equals(""))
				myDataBase = SQLiteDatabase.openDatabase(DB_NAME, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch (SQLiteException e) {
			
		}finally{
			if(myDataBase!=null){
				myDataBase.close();
			}
		}
	}

	public adbSqlHelper(Context context, String dbPath, String dbName, int version) {
		super(context, dbPath + dbName, null, version);
		this.myContext = context;
		adbSqlHelper.DB_VERSION = version;
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createAdb(db);
	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.
	public abstract void createAdb(SQLiteDatabase db);

	protected String getSql(String TABLE, String... COLUMNS) {
		String str = "CREATE TABLE '%s' ( '_id' INTEGER PRIMARY KEY AUTOINCREMENT,  ";
		for (int i = 1; i < COLUMNS.length; ++i) {
			str += "'" + COLUMNS[i] + "' TEXT";
			if (i < COLUMNS.length - 1) {
				str += ",";
			}
		}
		str += ");";
		String tblName = TABLE;
		String sql = String.format(str, tblName);
		return sql;
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}
}
