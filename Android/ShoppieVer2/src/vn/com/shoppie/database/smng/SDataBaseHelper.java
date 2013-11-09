package vn.com.shoppie.database.smng;

import vn.com.shoppie.database.adbSqlHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SDataBaseHelper extends adbSqlHelper {

	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/vn.com.shoppie/databases/";

	private static String DB_NAME = "shoppie.sqlite";

	public SDataBaseHelper(Context context) {
		super(context, DB_PATH, DB_NAME);
	}

	@Override
	public void createAdb(SQLiteDatabase db) {
		db.execSQL(getSql(UserMng.TABLE,UserMng.COLUMNS));
		db.execSQL(getSql(LinkMng.TABLE,LinkMng.COLUMNS));
		db.execSQL(getSql(MerchantMng.TABLE,MerchantMng.COLUMNS));
		db.execSQL(getSql(LikeMng.TABLE,LikeMng.COLUMNS));
		db.execSQL(getSql(GcmNotifyMng.TABLE,GcmNotifyMng.COLUMNS));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + UserMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + LinkMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + MerchantMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + LikeMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + GcmNotifyMng.TABLE+" ;");
		createAdb(db);
	}

}
