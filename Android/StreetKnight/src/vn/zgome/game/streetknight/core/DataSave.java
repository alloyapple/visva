package vn.zgome.game.streetknight.core;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.Test;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
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
	private String KEY_AES = "STPGalaxyNetwork";


	public DataSave(GameOS game, Context context) {
		this.game = game;

//		initDB(context);
	}

//	private void initDB(Context context) {
//		// TODO Auto-generated method stub
//		boolean isBuy[] = new boolean[13];
//		int numberID = mKungfuDBHandler.getItemCount();
//		if (numberID <= 0) {
//			KungfuDBItem kungfuDBItem = new KungfuDBItem(
//					Contants.KUNGFU_ITEM_ID, 0, 0, 0, 0, 0, 0, 0, isBuy);
//			mKungfuDBHandler.addNewItem(kungfuDBItem);
//		}
//	}

	protected AndroidFunction getPrefs() {
		return game.android;
	}

	public void getAll() {
//		KungfuDBItem kungfuDBItem = mKungfuDBHandler
//				.getItem(Contants.KUNGFU_ITEM_ID);
//		scoreMax = kungfuDBItem.getScoreMax();
//		kill = kungfuDBItem.getKill();
//		move = kungfuDBItem.getMove();
//		money = kungfuDBItem.getMoney();
//		stageHiscore = kungfuDBItem.getStageHiscore();
//		nextMap = kungfuDBItem.getNextMap();
//		coutHPBonus = kungfuDBItem.getCoutHPBonus();
//		isBuy = kungfuDBItem.getIsBuy();
		String trueStr = "true";
		String trueSave = encryptByAES(trueStr);
		if (getPrefs() != null) {
			String soundSave = getPrefs().getString("sound", trueSave);
			String soundStr = decryptByAES(soundSave);
			Log.e("soundStr", "SoundStr "+soundStr);
			try {
				sound = Boolean.valueOf(soundStr);
			} catch (Exception e) {
				sound = true;
			}
			Log.e("sound", "soundStr "+soundStr +"   sound "+sound);
			// sound = getPrefs().getBoolean("sound", true);
			String vibrateSave = getPrefs().getString("vibrate", trueSave);
			String vibrateStr = decryptByAES(vibrateSave);
			try {
				vibrate = Boolean.valueOf(vibrateStr);
			} catch (Exception e) {
				vibrate = true;
			}

			Log.e("sound save " + soundSave + "  sound " + sound,
					"vibrateSave " + vibrateSave + "  vibrate " + vibrate);
			// vibrate = getPrefs().getBoolean("vibrate", true);

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

			String isFirstPlaySave = getPrefs().getString("firstPlay", trueSave);
			String isFirstPlayStr = decryptByAES(isFirstPlaySave);
			try {
				isFirstPlay = Boolean.valueOf(isFirstPlayStr);
			} catch (Exception e) {
				isFirstPlay = true;
			}
			// isFirstPlay = getPrefs().getBoolean("firstPlay", true);

			boolean equip;
			for (int i = 0; i < 13; i++) {
				// buy = false;
				equip = false;
				if (i == 0) {
					// buy = true;
					equip = false;
				}
				// isBuy[i] = getPrefs().getBoolean("buy" + (i), buy);
				String equipStr = String.valueOf(equip);
				String equipSave = encryptByAES(equipStr);
				String isEquipSave = getPrefs().getString("equip" + (i), equipSave);
				String isEquipStr = decryptByAES(isEquipSave);
				try {
					isEquip[i] = Boolean.valueOf(isEquipStr);
				} catch (Exception e) {
					isEquip[i] = equip;
				}
				// isEquip[i] = getPrefs().getBoolean("equip" + (i), equip);
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
//		KungfuDBItem kungfuDBItem = new KungfuDBItem(Contants.KUNGFU_ITEM_ID,
//				scoreMax, kill, move, money, nextMap, stageHiscore,
//				coutHPBonus, isBuy);
//		mKungfuDBHandler.updateItem(kungfuDBItem);
		if (getPrefs() != null) {
			String soundStr = String.valueOf(sound);
			String soundSave = encryptByAES(soundStr);
			getPrefs().putString("sound", soundSave);
			// getPrefs().putBoolean("sound", sound);
			String vibrateStr = String.valueOf(vibrate);
			String vibrateSave = encryptByAES(vibrateStr);
			getPrefs().putString("vibrate", vibrateSave);
			// getPrefs().putBoolean("vibrate", vibrate);
			String scoreStr = String.valueOf(scoreMax);
			String scoreSave = encryptByAES(scoreStr);
			getPrefs().putString("score", scoreSave);
			// getPrefs().putInteger("score", scoreMax);
			String killStr = String.valueOf(kill);
			String killSave = encryptByAES(killStr);
			getPrefs().putString("kill", killSave);
			// getPrefs().putInteger("kill", kill);
			//
			String moveStr = String.valueOf(move);
			String moveSave = encryptByAES(moveStr);
			getPrefs().putString("move", moveSave);
			// getPrefs().putInteger("move", move);
			String moneyStr = String.valueOf(money);
			String moneySave = encryptByAES(moneyStr);
			getPrefs().putString("money", moneySave);
			// getPrefs().putInteger("money", money);
			for (int i = 0; i < 8; i++) {
				getPrefs().putInteger("map" + (i + 1), stateMap[i]);
			}
			String nextMapStr = String.valueOf(nextMap);
			String nextMapSave = encryptByAES(nextMapStr);
			getPrefs().putString("nextMap", nextMapSave);
			// getPrefs().putInteger("nextmap", nextMap);
			String coutHPBonusStr = String.valueOf(coutHPBonus);
			String coutHPBonusSave = encryptByAES(coutHPBonusStr);
			getPrefs().putString("hpBonus", coutHPBonusSave);
			// getPrefs().putInteger("hpBonus", coutHPBonus);
			String isFirstPlayStr = String.valueOf(isFirstPlay);
			String isFirstPlaySave = encryptByAES(isFirstPlayStr);
			getPrefs().putString("firstPlay", isFirstPlaySave);
			// getPrefs().putBoolean("firstPlay", isFirstPlay);

			for (int i = 0; i < 13; i++) {
				String buyStr = String.valueOf(isBuy[i]);
				String buySave = encryptByAES(buyStr);
				getPrefs().putString("buy" + i, buySave);
				// getPrefs().putBoolean("buy" + (i), isBuy[i]);
				String equipStr = String.valueOf(isEquip[i]);
				String isEquipSave = encryptByAES(equipStr);
				getPrefs().putString("equip" + i, isEquipSave);
				// getPrefs().putBoolean("equip" + (i), isEquip[i]);
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

	// MESSAGE ENCRYPTION BY AES ALGORITHM
	private String encryptByAES(String message) {
		String messageEncrypted = "";
		try {
			byte[] raw = KEY_AES.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(message.getBytes());
			byte[] encodeMessageEncrypted = Base64.encode(encrypted, 1);
			try {
				messageEncrypted = new String(encodeMessageEncrypted, "UTF-8");
				Log.d("", "encryptByAES - message: " + message);
				Log.d("", "encryptByAES - messageEncrypted: "
						+ messageEncrypted);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// keyAES = new String(skeySpec.getEncoded());
			return messageEncrypted;
			// return new String(skeySpec.getEncoded());
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BadPaddingException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidKeyException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	// MESSAGE DECRYPTION BY AES ALGORITHM
	private String decryptByAES(String encrypted) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(KEY_AES.getBytes(),
					"AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE,
					new SecretKeySpec(skeySpec.getEncoded(), "AES"));
			// getting error here
			byte[] decodeBase64 = Base64.decode(encrypted.getBytes(), 1);

			byte[] original = cipher.doFinal(decodeBase64);
			String originalMessage = new String(original);
			Log.d("", "decryptByAES - original message: " + originalMessage);
			return originalMessage;
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BadPaddingException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidKeyException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;

		/*
		 * Log.d(TAG, "decryptByAES - encrypted message: " + encrypted); try {
		 * SecretKeySpec skeySpec = new SecretKeySpec(
		 * Define.KEY_AES.getBytes(), "AES"); Cipher cipher =
		 * Cipher.getInstance("AES"); cipher.init(Cipher.DECRYPT_MODE, new
		 * SecretKeySpec(skeySpec.getEncoded(), "AES")); // getting error here
		 * byte[] decodeBase64 = Base64.decode(encrypted.getBytes(), 1);
		 * 
		 * byte[] original = cipher.doFinal(decodeBase64); String
		 * originalMessage = new String(original); Log.d("",
		 * "decryptByAES - original message: " + originalMessage); return
		 * originalMessage; } catch (IllegalBlockSizeException ex) {
		 * Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex); }
		 * catch (BadPaddingException ex) {
		 * Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex); }
		 * catch (InvalidKeyException ex) {
		 * Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex); }
		 * catch (NoSuchAlgorithmException ex) {
		 * Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex); }
		 * catch (NoSuchPaddingException ex) {
		 * Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex); }
		 * return null;
		 */

	}
}
