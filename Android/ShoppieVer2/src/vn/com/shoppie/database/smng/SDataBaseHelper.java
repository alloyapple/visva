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
		db.execSQL(getSql(CampaignMng.TABLE,CampaignMng.COLUMNS));
		db.execSQL(getSql(CampaignImageMng.TABLE,CampaignImageMng.COLUMNS));
		db.execSQL(getSql(GiftMng.TABLE,GiftMng.COLUMNS));
		db.execSQL(getSql(ImageMng.TABLE,ImageMng.COLUMNS));
		db.execSQL(getSql(ProductMng.TABLE,ProductMng.COLUMNS));
		db.execSQL(getSql(ShopMng.TABLE,ShopMng.COLUMNS));
		db.execSQL(getSql(StoreMng.TABLE,StoreMng.COLUMNS));
		db.execSQL(getSql(UserMng.TABLE,UserMng.COLUMNS));
		db.execSQL(getSql(LinkMng.TABLE,LinkMng.COLUMNS));
		db.execSQL(getSql(MerchantMng.TABLE,MerchantMng.COLUMNS));
		db.execSQL(getSql(MerchantCategoryMng.TABLE,MerchantCategoryMng.COLUMNS));
		db.execSQL(getSql(CustBalanceMng.TABLE,CustBalanceMng.COLUMNS));
		db.execSQL(getSql(PromotionMng.TABLE,PromotionMng.COLUMNS));
		db.execSQL(getSql(NotificationMng.TABLE,NotificationMng.COLUMNS));
		db.execSQL(getSql(MerchImageMng.TABLE,MerchImageMng.COLUMNS));
		db.execSQL(getSql(HistoryTxnMng.TABLE,HistoryTxnMng.COLUMNS));
		db.execSQL(getSql(GiftRedeemMng.TABLE,GiftRedeemMng.COLUMNS));
		db.execSQL(getSql(LikeMng.TABLE,LikeMng.COLUMNS));
		db.execSQL(getSql(GcmNotifyMng.TABLE,GcmNotifyMng.COLUMNS));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CampaignMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + CampaignImageMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + GiftMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + ImageMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + ProductMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + ShopMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + StoreMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + UserMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + LinkMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + MerchantMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + MerchantCategoryMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + CustBalanceMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + PromotionMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + NotificationMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + MerchImageMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + HistoryTxnMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + GiftRedeemMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + LikeMng.TABLE+" ;");
		db.execSQL("DROP TABLE IF EXISTS " + GcmNotifyMng.TABLE+" ;");
		createAdb(db);
	}

}
