package vn.zgome.game.streetknight.core;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import vn.zgome.game.streetknight.database.KungfuDBHandler;
import vn.zgome.game.streetknight.database.KungfuDBItem;
import vn.zgome.game.streetknight.util.Contants;

public class DataSave {
	GameOS game;
	public boolean sound;
	public boolean vibrate;
	public int scoreMax;
	public int kill;
	public int move;
	public int money;
	public int nextMap;
	public int stageHiscore;
	public int[] stateMap = new int[8];
	public int[] scoreUnlock = { 4000, 7000, 9000, 12000, 17000, 20000 };
	public int lang = 0;
	public int coutHPBonus;
	public boolean isFirstPlay;

	public boolean[] isBuy = new boolean[13];
	public boolean[] isEquip = new boolean[13];

	private KungfuDBHandler mKungfuDBHandler;

	public DataSave(GameOS game, Context context) {
		this.game = game;
		SQLiteDatabase.loadLibs(context);
		mKungfuDBHandler = new KungfuDBHandler(context);
		
		initDB(context);
	}

	private void initDB(Context context) {
		// TODO Auto-generated method stub
		boolean isBuy[] = new boolean[13];
		int numberID = mKungfuDBHandler.getItemCount();
		if (numberID <= 0) {
			KungfuDBItem kungfuDBItem = new KungfuDBItem(
					Contants.KUNGFU_ITEM_ID, 0, 0, 0, 0, 0, 0, 0, isBuy);
			mKungfuDBHandler.addNewItem(kungfuDBItem);
		}
	}

	protected AndroidFunction getPrefs() {
		return game.android;
	}

	public void getAll() {
		KungfuDBItem kungfuDBItem = mKungfuDBHandler
				.getItem(Contants.KUNGFU_ITEM_ID);
		scoreMax = kungfuDBItem.getScoreMax();
		kill = kungfuDBItem.getKill();
		move = kungfuDBItem.getMove();
		money = kungfuDBItem.getMoney();
		stageHiscore = kungfuDBItem.getStageHiscore();
		nextMap = kungfuDBItem.getNextMap();
		coutHPBonus = kungfuDBItem.getCoutHPBonus();
		isBuy = kungfuDBItem.getIsBuy();
		if (getPrefs() != null) {
			sound = getPrefs().getBoolean("sound", true);
			vibrate = getPrefs().getBoolean("vibrate", true);
			// scoreMax = getPrefs().getInteger("score", 0);
			// kill = getPrefs().getInteger("kill", 0);
			// move = getPrefs().getInteger("move", 0);
			// money = getPrefs().getInteger("money",
			// Asset.isTestVersion?999999:0);
			// stageHiscore = getPrefs().getInteger("stage", 1);

			for (int i = 0; i < 8; i++) {
				int df = 0;
				if (i == 1) {
					df = 1;
				}
				stateMap[i] = getPrefs().getInteger("map" + (i + 1), df);
			}
			// nextMap = getPrefs().getInteger("nextmap", 2);
			// coutHPBonus = getPrefs().getInteger("hpBonus", 0);
			isFirstPlay = getPrefs().getBoolean("firstPlay", true);

			boolean equip;
			for (int i = 0; i < 13; i++) {
				// buy = false;
				equip = false;
				if (i == 0) {
					// buy = true;
					equip = true;
				}
				// isBuy[i] = getPrefs().getBoolean("buy" + (i), buy);
				isEquip[i] = getPrefs().getBoolean("equip" + (i), equip);
			}
			if (coutHPBonus > 0) {
				isBuy[12] = true;
			} else {
				isBuy[12] = false;
				isEquip[12] = false;
			}
		} else {
			sound = true;
			vibrate = true;
			scoreMax = 0;
			kill = 0;
			move = 0;
			money = 100000;

			for (int i = 0; i < 8; i++) {
				int df = 0;
				if (i == 1) {
					df = 1;
				}
				stateMap[i] = df;
			}
			nextMap = 2;
			coutHPBonus = 0;
			isFirstPlay = true;

			boolean buy, equip;
			for (int i = 0; i < 13; i++) {
				buy = false;
				equip = false;
				if (i == 0) {
					buy = true;
					equip = true;
				}
				isBuy[i] = buy;
				isEquip[i] = equip;
			}
		}
	}

	public void saveAll() {
		KungfuDBItem kungfuDBItem = new KungfuDBItem(Contants.KUNGFU_ITEM_ID,
				scoreMax, kill, move, money, nextMap, stageHiscore,
				coutHPBonus, isBuy);
		mKungfuDBHandler.updateItem(kungfuDBItem);
		if (getPrefs() != null) {
			getPrefs().putBoolean("sound", sound);
			getPrefs().putBoolean("vibrate", vibrate);
			// getPrefs().putInteger("score", scoreMax);
			// getPrefs().putInteger("kill", kill);
			//
			// getPrefs().putInteger("move", move);
			// getPrefs().putInteger("money", money);
			for (int i = 0; i < 8; i++) {
				getPrefs().putInteger("map" + (i + 1), stateMap[i]);
			}
			// getPrefs().putInteger("nextmap", nextMap);
			// getPrefs().putInteger("hpBonus", coutHPBonus);
			getPrefs().putBoolean("firstPlay", isFirstPlay);

			for (int i = 0; i < 13; i++) {
				// getPrefs().putBoolean("buy" + (i), isBuy[i]);
				getPrefs().putBoolean("equip" + (i), isEquip[i]);
			}
		}
	}

	public void saveScore(int stage, int score, int kill, int move) {
		if (score >= scoreMax) {
			this.stageHiscore = stage;
			this.scoreMax = score;
			this.kill = kill;
			this.move = move;
			saveAll();
		}
		if (nextMap <= 8) {
			if (stage + 1 == nextMap) {
				if (score >= scoreUnlock[nextMap - 2]) {
					stateMap[nextMap] = 1;
					nextMap++;
					saveAll();
				}
			}
		}
	}
}
