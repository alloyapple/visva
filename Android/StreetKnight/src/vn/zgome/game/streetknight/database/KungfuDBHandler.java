package vn.zgome.game.streetknight.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class KungfuDBHandler extends SQLiteOpenHelper {

	// table database
	private static final String DATABASE_NAME = "kungfu.db";
	private static final String DB_TABLE = "kungfu_table";
	private static final int DATA_VERSION = 1;

	// columns name
	private static final String KEY_DB_ID = "id";
	private static final String KEY_SCORE_MAX = "score_max";
	private static final String KEY_KILL = "kill";
	private static final String KEY_MOVE = "move";
	private static final String KEY_MONEY = "money";
	private static final String KEY_NEXT_MAP = "nextMap";
	private static final String KEY_STAGE_HIGH_SCORE = "stageHiscore";
	private static final String KEY_COUNT_HP_BONUS = "coutHPBonus";
	private static final String KEY_BOOL_IS_BUY1 = "isBuy1";
	private static final String KEY_BOOL_IS_BUY2 = "isBuy2";
	private static final String KEY_BOOL_IS_BUY3 = "isBuy3";
	private static final String KEY_BOOL_IS_BUY4 = "isBuy4";
	private static final String KEY_BOOL_IS_BUY5 = "isBuy5";
	private static final String KEY_BOOL_IS_BUY6 = "isBuy6";
	private static final String KEY_BOOL_IS_BUY7 = "isBuy7";
	private static final String KEY_BOOL_IS_BUY8 = "isBuy8";
	private static final String KEY_BOOL_IS_BUY9 = "isBuy9";
	private static final String KEY_BOOL_IS_BUY10 = "isBuy10";
	private static final String KEY_BOOL_IS_BUY11 = "isBuy11";
	private static final String KEY_BOOL_IS_BUY12 = "isBuy12";
	private static final String KEY_BOOL_IS_BUY13 = "isBuy13";
	private static final String KEY_KUNGFU_PW = "kf_knight@@_2013";

	public KungfuDBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATA_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_USER_TABLE = "CREATE TABLE " + DB_TABLE + "(" + KEY_DB_ID
				+ " INTEGER PRIMARY KEY," + KEY_SCORE_MAX + " INTEGER,"
				+ KEY_KILL + " INTEGER," + KEY_MOVE + " INTEGER," + KEY_MONEY
				+ " INTEGER," + KEY_NEXT_MAP + " INTEGER,"
				+ KEY_STAGE_HIGH_SCORE + " INTEGER," + KEY_COUNT_HP_BONUS
				+ " INTEGER," + KEY_BOOL_IS_BUY1 + " BOOLEAN,"
				+ KEY_BOOL_IS_BUY2 + " BOOLEAN," + KEY_BOOL_IS_BUY3
				+ " BOOLEAN," + KEY_BOOL_IS_BUY4 + " BOOLEAN,"
				+ KEY_BOOL_IS_BUY5 + " BOOLEAN," + KEY_BOOL_IS_BUY6
				+ " BOOLEAN," + KEY_BOOL_IS_BUY7 + " BOOLEAN,"
				+ KEY_BOOL_IS_BUY8 + " BOOLEAN," + KEY_BOOL_IS_BUY9
				+ " BOOLEAN," + KEY_BOOL_IS_BUY10 + " BOOLEAN,"
				+ KEY_BOOL_IS_BUY11 + " BOOLEAN," + KEY_BOOL_IS_BUY12
				+ " BOOLEAN," + KEY_BOOL_IS_BUY13 + " BOOLEAN" + ")";
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new user
	public void addNewItem(KungfuDBItem kungfuDBItem) {
		SQLiteDatabase db = this.getWritableDatabase(KEY_KUNGFU_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_DB_ID, kungfuDBItem.getItemId());
		values.put(KEY_SCORE_MAX, kungfuDBItem.getScoreMax());
		values.put(KEY_KILL, kungfuDBItem.getKill());
		values.put(KEY_MOVE, kungfuDBItem.getMove());
		values.put(KEY_MONEY, kungfuDBItem.getMoney());
		values.put(KEY_NEXT_MAP, kungfuDBItem.getNextMap());
		values.put(KEY_STAGE_HIGH_SCORE, kungfuDBItem.getStageHiscore());
		values.put(KEY_COUNT_HP_BONUS, kungfuDBItem.getCoutHPBonus());
		values.put(KEY_BOOL_IS_BUY1, kungfuDBItem.getIsBuy()[0]);
		values.put(KEY_BOOL_IS_BUY2, kungfuDBItem.getIsBuy()[1]);
		values.put(KEY_BOOL_IS_BUY3, kungfuDBItem.getIsBuy()[2]);
		values.put(KEY_BOOL_IS_BUY4, kungfuDBItem.getIsBuy()[3]);
		values.put(KEY_BOOL_IS_BUY5, kungfuDBItem.getIsBuy()[4]);
		values.put(KEY_BOOL_IS_BUY6, kungfuDBItem.getIsBuy()[5]);
		values.put(KEY_BOOL_IS_BUY7, kungfuDBItem.getIsBuy()[6]);
		values.put(KEY_BOOL_IS_BUY8, kungfuDBItem.getIsBuy()[7]);
		values.put(KEY_BOOL_IS_BUY9, kungfuDBItem.getIsBuy()[8]);
		values.put(KEY_BOOL_IS_BUY10, kungfuDBItem.getIsBuy()[9]);
		values.put(KEY_BOOL_IS_BUY11, kungfuDBItem.getIsBuy()[10]);
		values.put(KEY_BOOL_IS_BUY12, kungfuDBItem.getIsBuy()[11]);
		values.put(KEY_BOOL_IS_BUY13, kungfuDBItem.getIsBuy()[12]);

		// Inserting Row
		db.insert(DB_TABLE, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single user
	public KungfuDBItem getItem(int itemId) {
		SQLiteDatabase db = this.getReadableDatabase(KEY_KUNGFU_PW);

		Cursor cursor = db.query(DB_TABLE, new String[] { KEY_DB_ID,
				KEY_SCORE_MAX, KEY_KILL, KEY_MOVE, KEY_MONEY, KEY_NEXT_MAP,
				KEY_STAGE_HIGH_SCORE, KEY_COUNT_HP_BONUS, KEY_BOOL_IS_BUY1,
				KEY_BOOL_IS_BUY2, KEY_BOOL_IS_BUY3, KEY_BOOL_IS_BUY4,
				KEY_BOOL_IS_BUY5, KEY_BOOL_IS_BUY6, KEY_BOOL_IS_BUY7,
				KEY_BOOL_IS_BUY8, KEY_BOOL_IS_BUY9, KEY_BOOL_IS_BUY10,
				KEY_BOOL_IS_BUY11, KEY_BOOL_IS_BUY12, KEY_BOOL_IS_BUY13 },
				KEY_DB_ID + "=?", new String[] { String.valueOf(itemId) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		boolean isBuy[] = new boolean[13];
		isBuy[0] = Boolean.parseBoolean(cursor.getString(8));
		isBuy[1] = Boolean.parseBoolean(cursor.getString(9));
		isBuy[2] = Boolean.parseBoolean(cursor.getString(10));
		isBuy[3] = Boolean.parseBoolean(cursor.getString(11));
		isBuy[4] = Boolean.parseBoolean(cursor.getString(12));
		isBuy[5] = Boolean.parseBoolean(cursor.getString(13));
		isBuy[6] = Boolean.parseBoolean(cursor.getString(14));
		isBuy[7] = Boolean.parseBoolean(cursor.getString(15));
		isBuy[8] = Boolean.parseBoolean(cursor.getString(16));
		isBuy[9] = Boolean.parseBoolean(cursor.getString(17));
		isBuy[10] = Boolean.parseBoolean(cursor.getString(18));
		isBuy[11] = Boolean.parseBoolean(cursor.getString(19));
		isBuy[12] = Boolean.parseBoolean(cursor.getString(20));

		KungfuDBItem user = new KungfuDBItem(itemId, Integer.parseInt(cursor
				.getString(1)), Integer.parseInt(cursor.getString(2)),
				Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor
						.getString(4)), Integer.parseInt(cursor.getString(5)),
				Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor
						.getString(7)), isBuy);
		cursor.close();
		db.close();
		// return folder
		return user;
	}

	// Updating single folder
	public int updateItem(KungfuDBItem kungfuDBItem) {
		SQLiteDatabase db = this.getWritableDatabase(KEY_KUNGFU_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_SCORE_MAX, kungfuDBItem.getScoreMax());
		values.put(KEY_KILL, kungfuDBItem.getKill());
		values.put(KEY_MOVE, kungfuDBItem.getMove());
		values.put(KEY_MONEY, kungfuDBItem.getMoney());
		values.put(KEY_NEXT_MAP, kungfuDBItem.getNextMap());
		values.put(KEY_STAGE_HIGH_SCORE, kungfuDBItem.getStageHiscore());
		values.put(KEY_COUNT_HP_BONUS, kungfuDBItem.getCoutHPBonus());
		values.put(KEY_BOOL_IS_BUY1, kungfuDBItem.getIsBuy()[0]);
		values.put(KEY_BOOL_IS_BUY2, kungfuDBItem.getIsBuy()[1]);
		values.put(KEY_BOOL_IS_BUY3, kungfuDBItem.getIsBuy()[2]);
		values.put(KEY_BOOL_IS_BUY4, kungfuDBItem.getIsBuy()[3]);
		values.put(KEY_BOOL_IS_BUY5, kungfuDBItem.getIsBuy()[4]);
		values.put(KEY_BOOL_IS_BUY6, kungfuDBItem.getIsBuy()[5]);
		values.put(KEY_BOOL_IS_BUY7, kungfuDBItem.getIsBuy()[6]);
		values.put(KEY_BOOL_IS_BUY8, kungfuDBItem.getIsBuy()[7]);
		values.put(KEY_BOOL_IS_BUY9, kungfuDBItem.getIsBuy()[8]);
		values.put(KEY_BOOL_IS_BUY10, kungfuDBItem.getIsBuy()[9]);
		values.put(KEY_BOOL_IS_BUY11, kungfuDBItem.getIsBuy()[10]);
		values.put(KEY_BOOL_IS_BUY12, kungfuDBItem.getIsBuy()[11]);
		values.put(KEY_BOOL_IS_BUY13, kungfuDBItem.getIsBuy()[12]);
		// updating row
		db.update(DB_TABLE, values, KEY_DB_ID + " = ?",
				new String[] { String.valueOf(kungfuDBItem.getItemId()) });
		db.close();
		return 1;
	}

	// Getting users Count
	public int getItemCount() {
		int count = 0;
		String countQuery = "SELECT  * FROM " + DB_TABLE;
		SQLiteDatabase db = this.getReadableDatabase(KEY_KUNGFU_PW);
		Cursor cursor = db.rawQuery(countQuery, null);
		// return count
		count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}
}
