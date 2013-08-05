package vn.zgome.game.streetknight.core;

import vn.zgome.game.streetknight.core.graphics.FrameRegionInfo;
import vn.zgome.game.streetknight.core.graphics.ScreenInfo;
import vn.zgome.game.streetknight.core.graphics.TextureRegionInfo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Asset 
{	
	
	public static boolean isTestVersion = false;
	
	public static AssetManager manager;
	
	public static FrameRegionInfo thuoc;
	public static FrameRegionInfo bom;
	public static FrameRegionInfo cai_kim;
	public static FrameRegionInfo coi_gia;
	public static FrameRegionInfo chai_bia; 
	public static FrameRegionInfo nvc_normal;
	public static FrameRegionInfo nvc_dao;
	public static FrameRegionInfo nvc_dau;
	public static FrameRegionInfo nvc_sa;
	public static FrameRegionInfo nvc_ai;
	public static FrameRegionInfo hit;
	public static FrameRegionInfo cho_ha_noi;
	public static FrameRegionInfo dau_gau_ha_noi1;	
	public static FrameRegionInfo dau_gau_ha_noi2;	
	public static FrameRegionInfo comment;	
	public static FrameRegionInfo blood;
	public static FrameRegionInfo blood3;	
	public static FrameRegionInfo bua;
	public static FrameRegionInfo mo_neo;
	public static FrameRegionInfo oc_sen; 
	public static FrameRegionInfo dau_gau_ha_long1;	
	public static FrameRegionInfo dau_gau_ha_long2;	
	public static FrameRegionInfo pass;
	
	public static FrameRegionInfo guoc;
	public static FrameRegionInfo may_say_toc;
	public static FrameRegionInfo meo; 
	public static FrameRegionInfo dau_gau_sai_gon_1;	
	public static FrameRegionInfo dau_gau_sai_gon_2;	
	
	public static FrameRegionInfo dao;
	public static FrameRegionInfo phi_tieu;
	public static FrameRegionInfo ran; 
	public static FrameRegionInfo dau_gau_nhat_1;	
	public static FrameRegionInfo dau_gau_nhat_2;
	
	public static FrameRegionInfo riu;
	public static FrameRegionInfo luoi_3;
	public static FrameRegionInfo ngua_dien; 
	public static FrameRegionInfo dau_gau_roma_1;	
	public static FrameRegionInfo dau_gau_roma_2;
	
	public static FrameRegionInfo ten_lua;
	public static FrameRegionInfo dao_dua;
	public static FrameRegionInfo cho_dao; 
	public static FrameRegionInfo dau_gau_dao_1;	
	public static FrameRegionInfo dau_gau_dao_2;
	
	public static FrameRegionInfo dao_gam;
	public static FrameRegionInfo luoi_hai;
	public static FrameRegionInfo bo_cap; 
	public static FrameRegionInfo dau_gau_ai_cap_1;	
	public static FrameRegionInfo dau_gau_ai_cap_2;
	
	public static TextureAtlas menuAtlas;
	public static TextureRegionInfo nen_chung;
	public static TextureRegionInfo aboutTextRegion;
	public static TextureRegionInfo fb_shared;
	public static TextureRegionInfo bgMenuRegion;
	public static TextureRegionInfo transButRegion;
	public static TextureRegionInfo aboutButRegion;
	public static TextureRegionInfo bgShopRegion;
	public static TextureRegionInfo bgHiscoreRegion;
	public static TextureRegionInfo hiScore;
	public static TextureRegionInfo kill;
	public static TextureRegionInfo move;
	public static TextureRegionInfo bgSettingRegion;
	public static TextureRegionInfo vibrate;
	public static TextureRegionInfo music;
	public static TextureRegionInfo bgAboutRegion;
	public static TextureRegionInfo languageRegion;
	public static TextureRegionInfo bgLanguageRegion;
	public static TextureRegionInfo blackRegion;
	
	public static TextureRegionInfo itemHanoiRegion;
	public static TextureRegionInfo stringHanoiRegion;
	
	public static TextureRegionInfo tutRegion;
	public static TextureRegionInfo itemHalongRegion;
	public static TextureRegionInfo stringHalongRegion;
	public static TextureRegionInfo itemHalongLockRegion;
	
	public static TextureRegionInfo itemSaigonRegion;
	public static TextureRegionInfo stringSaigonRegion;
	public static TextureRegionInfo itemSaigonLockRegion;
	
	public static TextureRegionInfo itemJapanRegion;
	public static TextureRegionInfo stringJapanRegion;
	public static TextureRegionInfo itemJapanLockRegion;
	
	public static TextureRegionInfo itemRomaRegion;
	public static TextureRegionInfo stringRomaRegion;
	public static TextureRegionInfo itemRomaLockRegion;
	
	public static TextureRegionInfo itemDaoRegion;
	public static TextureRegionInfo stringDaoRegion;
	public static TextureRegionInfo itemDaoLockRegion;
	
	public static TextureRegionInfo itemAicapRegion;
	public static TextureRegionInfo stringAicapRegion;
	public static TextureRegionInfo itemAicapLockRegion;
	
	public static TextureRegionInfo onRegion;
	public static TextureRegionInfo offRegion;
	public static TextureRegionInfo menuButRegion;
	public static TextureRegionInfo pauseDialogRegion;
	public static TextureRegionInfo loseDialogRegion;
	public static TextureRegionInfo menuRegion;
	public static TextureRegionInfo againRegion;

	public static FrameRegionInfo youare;
	public static TextureRegionInfo novice;
	public static TextureRegionInfo expert;
	public static TextureRegionInfo unstoppable;
	public static TextureRegionInfo master;
	public static TextureRegionInfo skillful;
	
	public static TextureAtlas scoreAtlas;
	public static TextureRegionInfo[] HP = new TextureRegionInfo[6];
	public static TextureRegionInfo score;
	public static TextureRegionInfo money;
	
	public static TextureAtlas stageHaNoiAtlas;
	public static TextureAtlas stageHalongAtlas;
	public static TextureAtlas stageSaigonAtlas;
	public static TextureAtlas stageJapanAtlas;
	public static TextureAtlas stageRomaAtlas;
	public static TextureAtlas stageAicapAtlas;
	public static TextureAtlas stageDaoAtlas;
	public static TextureRegionInfo bgGameHaNoi;
	public static TextureRegionInfo bgGameHaLong;
	public static TextureRegionInfo bgGameSaigon;
	public static TextureRegionInfo bgGameJapan;
	public static TextureRegionInfo bgGameRoma;
	public static TextureRegionInfo bgGameDao;
	public static TextureRegionInfo bgGameAiCap;
	
	public static TextureAtlas itemAtlas;
	
	public static TextureRegionInfo buy;
	public static TextureRegionInfo equip;
	public static TextureRegionInfo unequip;
	
	public static TextureRegionInfo normalRegion;
	public static TextureRegionInfo normalSLRegion;
	public static TextureRegionInfo normalEQRegion;
	public static TextureRegionInfo samuraiRegion;
	public static TextureRegionInfo samuraiSLRegion;
	public static TextureRegionInfo samuraiEQRegion;
	public static TextureRegionInfo superRegion;
	public static TextureRegionInfo superSLRegion;
	public static TextureRegionInfo superEQRegion;
	public static TextureRegionInfo saRegion;
	public static TextureRegionInfo saSLRegion;
	public static TextureRegionInfo saEQRegion;
	public static TextureRegionInfo aiRegion;
	public static TextureRegionInfo aiSLRegion;
	public static TextureRegionInfo aiEQRegion;
	public static TextureRegionInfo bonus15Region;
	public static TextureRegionInfo bonus15SLRegion;
	public static TextureRegionInfo bonus15EQRegion;
	
	public static TextureRegionInfo bomRegion;
	public static TextureRegionInfo bomSLRegion;
	public static TextureRegionInfo bomEQRegion;
	public static TextureRegionInfo snowRegion;
	public static TextureRegionInfo snowSLRegion;
	public static TextureRegionInfo snowEQRegion;
	public static TextureRegionInfo s10Region;
	public static TextureRegionInfo s10SLRegion;
	public static TextureRegionInfo s10EQRegion;
	
	public static TextureRegionInfo addTimeItemRegion;
	public static TextureRegionInfo addTimeItemSLRegion;
	public static TextureRegionInfo addTimeItemEQRegion;
	public static TextureRegionInfo addMoneyRegion;
	public static TextureRegionInfo addMoneySLRegion;
	public static TextureRegionInfo addMoneyEQRegion;
	public static TextureRegionInfo addHPRegion;
	public static TextureRegionInfo addHPSLRegion;
	public static TextureRegionInfo addHPEQRegion;
	public static TextureRegionInfo quickReloadRegion;
	public static TextureRegionInfo quickReloadSLRegion;
	public static TextureRegionInfo quickReloadEQRegion;
	
	public static TextureAtlas itemInfoAtlas;
	public static TextureRegionInfo[][] details;
	
	public static TextureAtlas moneyAtlas;
	public static TextureRegionInfo moneyButton[];
	public static TextureRegionInfo screenPay;
	public static TextureRegionInfo buy8Dialog;
	
	public static TextureAtlas tutAtlas;
	public static TextureRegionInfo next, prev;
	public static TextureRegionInfo hocchiendau[];
	public static TextureRegionInfo leftHere, rightHere;
	public static TextureRegionInfo tancong6huong[], chamvaomanhinh[], midAttack, hiAttack, hiAttack2, lowAttack,lowAttack2, hoccombo[], midAttack2, cham2diem[], tieudiet2[], kethopcombo[], combobanthich[], kickkinang[], chamnvc[], chamday, chamicon[], kichhoatskill[], ngonrui[];
	
	public static Music begin, nen;
	public static Sound chuong, vu, glass_break, kim_loai,achoo;
	
	public static void loaded(ScreenInfo screenInfo)
	{
		begin = manager.get("data/music/begin.ogg", Music.class);
		begin.setLooping(false);
		
		nen = manager.get("data/music/nen.ogg", Music.class);
		nen.setLooping(true);
		
		chuong =  manager.get("data/music/chuong.ogg", Sound.class);
		vu =  manager.get("data/music/vu.ogg", Sound.class);
		glass_break = manager.get("data/music/glass_break.ogg", Sound.class);
		kim_loai = manager.get("data/music/kim_loai.ogg", Sound.class);
		achoo = manager.get("data/music/achoo.mp3", Sound.class);
		
		pass = new FrameRegionInfo(manager.get("data/game/pass/pass.atlas", TextureAtlas.class), 16,screenInfo);	
		comment = new FrameRegionInfo(manager.get("data/game/comment/comment.atlas", TextureAtlas.class), 24,screenInfo);	
		blood = new FrameRegionInfo(manager.get("data/game/blood/blood.atlas", TextureAtlas.class), 16,screenInfo);	
		blood3 = new FrameRegionInfo(manager.get("data/game/blood3/blood3.atlas", TextureAtlas.class), 4,screenInfo);	
		hit = new FrameRegionInfo(manager.get("data/game/hit/hit.atlas", TextureAtlas.class), 8,screenInfo);	
		youare = new FrameRegionInfo(manager.get("data/game/rank/rank.atlas", TextureAtlas.class), 8,screenInfo);	
	
		thuoc = new FrameRegionInfo(manager.get("data/game/thuoc/thuoc.atlas", TextureAtlas.class), 8,screenInfo);	
		bom = new FrameRegionInfo(manager.get("data/game/bom/bom.atlas", TextureAtlas.class), 9,screenInfo);	
		
		menuAtlas = manager.get("data/menu/menu.atlas", TextureAtlas.class);
		
		nen_chung = new TextureRegionInfo(menuAtlas.findRegion("nen_chung"), screenInfo);
		novice = new TextureRegionInfo(menuAtlas.findRegion("32"), screenInfo);
		expert = new TextureRegionInfo(menuAtlas.findRegion("34"), screenInfo);
		unstoppable = new TextureRegionInfo(menuAtlas.findRegion("35"), screenInfo);
		master = new TextureRegionInfo(menuAtlas.findRegion("36"), screenInfo);
		skillful = new TextureRegionInfo(menuAtlas.findRegion("37"), screenInfo);

		
		aboutTextRegion = new TextureRegionInfo(menuAtlas.findRegion("about"), screenInfo);
		fb_shared = new TextureRegionInfo(menuAtlas.findRegion("fb_but"), screenInfo);
		bgMenuRegion = new TextureRegionInfo(menuAtlas.findRegion("2"), screenInfo);
		transButRegion = new TextureRegionInfo(menuAtlas.findRegion("trans"), screenInfo);
		aboutButRegion = new TextureRegionInfo(menuAtlas.findRegion("43"), screenInfo);
		bgShopRegion = new TextureRegionInfo(menuAtlas.findRegion("shop"), screenInfo);
		bgHiscoreRegion = new TextureRegionInfo(menuAtlas.findRegion("31"), screenInfo);
		hiScore = new TextureRegionInfo(menuAtlas.findRegion("Highest_score"), screenInfo);
		kill = new TextureRegionInfo(menuAtlas.findRegion("kil"), screenInfo);
		move = new TextureRegionInfo(menuAtlas.findRegion("move"), screenInfo);
		bgSettingRegion = new TextureRegionInfo(menuAtlas.findRegion("31"), screenInfo);
		vibrate = new TextureRegionInfo(menuAtlas.findRegion("vibration"), screenInfo);
		music = new TextureRegionInfo(menuAtlas.findRegion("music"), screenInfo);
		bgAboutRegion = new TextureRegionInfo(menuAtlas.findRegion("4"), screenInfo);
		languageRegion = new TextureRegionInfo(menuAtlas.findRegion("113"), screenInfo);
		bgLanguageRegion = new TextureRegionInfo(menuAtlas.findRegion("white"), screenInfo);
		
		itemHanoiRegion = new TextureRegionInfo(menuAtlas.findRegion("12"), screenInfo);
		stringHanoiRegion = new TextureRegionInfo(menuAtlas.findRegion("hANOI"), screenInfo);
		
		tutRegion = new TextureRegionInfo(menuAtlas.findRegion("tut"), screenInfo);
		itemHalongRegion = new TextureRegionInfo(menuAtlas.findRegion("13"), screenInfo);
		stringHalongRegion = new TextureRegionInfo(menuAtlas.findRegion("HALONG"), screenInfo);
		itemHalongLockRegion = new TextureRegionInfo(menuAtlas.findRegion("17"), screenInfo);
		
		itemSaigonRegion = new TextureRegionInfo(menuAtlas.findRegion("14"), screenInfo);
		stringSaigonRegion = new TextureRegionInfo(menuAtlas.findRegion("SAIGON"), screenInfo);
		itemSaigonLockRegion = new TextureRegionInfo(menuAtlas.findRegion("18"), screenInfo);
		
		itemJapanRegion = new TextureRegionInfo(menuAtlas.findRegion("15"), screenInfo);
		stringJapanRegion = new TextureRegionInfo(menuAtlas.findRegion("JAPAN"), screenInfo);
		itemJapanLockRegion = new TextureRegionInfo(menuAtlas.findRegion("19"), screenInfo);
		
		itemRomaRegion = new TextureRegionInfo(menuAtlas.findRegion("16"), screenInfo);
		stringRomaRegion = new TextureRegionInfo(menuAtlas.findRegion("ROME"), screenInfo);
		itemRomaLockRegion = new TextureRegionInfo(menuAtlas.findRegion("20"), screenInfo);		
		
		itemDaoRegion = new TextureRegionInfo(menuAtlas.findRegion("22"), screenInfo);
		stringDaoRegion = new TextureRegionInfo(menuAtlas.findRegion("HOANGSA"), screenInfo);
		itemDaoLockRegion = new TextureRegionInfo(menuAtlas.findRegion("1"), screenInfo);
		
		itemAicapRegion = new TextureRegionInfo(menuAtlas.findRegion("44"), screenInfo);
		stringAicapRegion = new TextureRegionInfo(menuAtlas.findRegion("EGYPT"), screenInfo);
		itemAicapLockRegion = new TextureRegionInfo(menuAtlas.findRegion("33"), screenInfo);
		
		onRegion = new TextureRegionInfo(menuAtlas.findRegion("39"), screenInfo);
		offRegion = new TextureRegionInfo(menuAtlas.findRegion("40"), screenInfo);
		blackRegion = new TextureRegionInfo(menuAtlas.findRegion("black"), screenInfo);
		menuButRegion = new TextureRegionInfo(menuAtlas.findRegion("26"), screenInfo);
		pauseDialogRegion = new TextureRegionInfo(menuAtlas.findRegion("25"), screenInfo);
		loseDialogRegion = new TextureRegionInfo(menuAtlas.findRegion("5"), screenInfo);
		menuRegion = new TextureRegionInfo(menuAtlas.findRegion("back_menu"), screenInfo);
		againRegion = new TextureRegionInfo(menuAtlas.findRegion("again"), screenInfo);
		
		scoreAtlas = manager.get("data/game/score/score.atlas", TextureAtlas.class);
		HP[0] = new TextureRegionInfo(scoreAtlas.findRegion("6"), screenInfo);
		HP[1] = new TextureRegionInfo(scoreAtlas.findRegion("5"), screenInfo);
		HP[2] = new TextureRegionInfo(scoreAtlas.findRegion("4"), screenInfo);
		HP[3] = new TextureRegionInfo(scoreAtlas.findRegion("3"), screenInfo);
		HP[4] = new TextureRegionInfo(scoreAtlas.findRegion("2"), screenInfo);
		HP[5] = new TextureRegionInfo(scoreAtlas.findRegion("1"), screenInfo);
		score = new TextureRegionInfo(scoreAtlas.findRegion("11"), screenInfo);
		money = new TextureRegionInfo(scoreAtlas.findRegion("15"), screenInfo);
				
		itemAtlas = manager.get("data/menu/item.atlas", TextureAtlas.class);
		
		buy = new TextureRegionInfo(itemAtlas.findRegion("4"), screenInfo);
		equip = new TextureRegionInfo(itemAtlas.findRegion("5"), screenInfo);
		unequip = new TextureRegionInfo(itemAtlas.findRegion("6"), screenInfo);		
		
		normalRegion = new TextureRegionInfo(itemAtlas.findRegion("1"), screenInfo);
		normalSLRegion = new TextureRegionInfo(itemAtlas.findRegion("1.1"), screenInfo);
		normalEQRegion = new TextureRegionInfo(itemAtlas.findRegion("18"), screenInfo);
		superRegion = new TextureRegionInfo(itemAtlas.findRegion("3"), screenInfo);
		superSLRegion = new TextureRegionInfo(itemAtlas.findRegion("3.1"), screenInfo);
		superEQRegion = new TextureRegionInfo(itemAtlas.findRegion("25"), screenInfo);
		samuraiRegion = new TextureRegionInfo(itemAtlas.findRegion("2"), screenInfo);
		samuraiSLRegion = new TextureRegionInfo(itemAtlas.findRegion("2.1"), screenInfo);
		samuraiEQRegion = new TextureRegionInfo(itemAtlas.findRegion("24"), screenInfo);
		
		snowRegion = new TextureRegionInfo(itemAtlas.findRegion("7"), screenInfo);
		snowSLRegion = new TextureRegionInfo(itemAtlas.findRegion("7.1"), screenInfo);
		snowEQRegion = new TextureRegionInfo(itemAtlas.findRegion("26"), screenInfo);
		bomRegion = new TextureRegionInfo(itemAtlas.findRegion("9"), screenInfo);
		bomSLRegion = new TextureRegionInfo(itemAtlas.findRegion("9.1"), screenInfo);
		bomEQRegion = new TextureRegionInfo(itemAtlas.findRegion("27"), screenInfo);
		s10Region = new TextureRegionInfo(itemAtlas.findRegion("11"), screenInfo);
		s10SLRegion = new TextureRegionInfo(itemAtlas.findRegion("11.1"), screenInfo);
		s10EQRegion = new TextureRegionInfo(itemAtlas.findRegion("19"), screenInfo);
		
		addTimeItemRegion = new TextureRegionInfo(itemAtlas.findRegion("13"), screenInfo);
		addTimeItemSLRegion = new TextureRegionInfo(itemAtlas.findRegion("13.1"), screenInfo);
		addTimeItemEQRegion = new TextureRegionInfo(itemAtlas.findRegion("20"), screenInfo);
		addMoneyRegion = new TextureRegionInfo(itemAtlas.findRegion("15"), screenInfo);
		addMoneySLRegion = new TextureRegionInfo(itemAtlas.findRegion("15.1"), screenInfo);
		addMoneyEQRegion = new TextureRegionInfo(itemAtlas.findRegion("21"), screenInfo);
		addHPRegion = new TextureRegionInfo(itemAtlas.findRegion("16"), screenInfo);
		addHPSLRegion = new TextureRegionInfo(itemAtlas.findRegion("16.1"), screenInfo);
		addHPEQRegion = new TextureRegionInfo(itemAtlas.findRegion("22"), screenInfo);
		quickReloadRegion = new TextureRegionInfo(itemAtlas.findRegion("17"), screenInfo);
		quickReloadSLRegion = new TextureRegionInfo(itemAtlas.findRegion("17.1"), screenInfo);
		quickReloadEQRegion = new TextureRegionInfo(itemAtlas.findRegion("23"), screenInfo);

		saRegion = new TextureRegionInfo(itemAtlas.findRegion("100.1"), screenInfo);
		saSLRegion = new TextureRegionInfo(itemAtlas.findRegion("100"), screenInfo);
		saEQRegion = new TextureRegionInfo(itemAtlas.findRegion("100.3"), screenInfo);

		aiRegion = new TextureRegionInfo(itemAtlas.findRegion("200.1"), screenInfo);
		aiSLRegion = new TextureRegionInfo(itemAtlas.findRegion("200"), screenInfo);
		aiEQRegion = new TextureRegionInfo(itemAtlas.findRegion("200.3"), screenInfo);

		bonus15Region = new TextureRegionInfo(itemAtlas.findRegion("bonus1"), screenInfo);
		bonus15SLRegion = new TextureRegionInfo(itemAtlas.findRegion("bonus2"), screenInfo);
		bonus15EQRegion = new TextureRegionInfo(itemAtlas.findRegion("bonus3"), screenInfo);		
		
		itemInfoAtlas = manager.get("data/menu/item_info.atlas", TextureAtlas.class);
		
		
		details = new TextureRegionInfo[2][15];
		details[0][0] = new TextureRegionInfo(itemInfoAtlas.findRegion("Weapon"), screenInfo);
		details[0][1] = new TextureRegionInfo(itemInfoAtlas.findRegion("BruceLee"), screenInfo);
		details[0][2] = new TextureRegionInfo(itemInfoAtlas.findRegion("samurai"), screenInfo);
		details[0][3] = new TextureRegionInfo(itemInfoAtlas.findRegion("gladiator"), screenInfo);
		details[0][4] = new TextureRegionInfo(itemInfoAtlas.findRegion("skill"), screenInfo);
		details[0][5] = new TextureRegionInfo(itemInfoAtlas.findRegion("slowtime"), screenInfo);
		details[0][6] = new TextureRegionInfo(itemInfoAtlas.findRegion("No_bomb"), screenInfo);
		details[0][7] = new TextureRegionInfo(itemInfoAtlas.findRegion("battu_10giay"), screenInfo);
		details[0][8] = new TextureRegionInfo(itemInfoAtlas.findRegion("Bonus"), screenInfo);
		details[0][9] = new TextureRegionInfo(itemInfoAtlas.findRegion("tang_them_thoi_gian_su_dung_ky_nang"), screenInfo);
		details[0][10] = new TextureRegionInfo(itemInfoAtlas.findRegion("Cong_them_tien"), screenInfo);
		details[0][11] = new TextureRegionInfo(itemInfoAtlas.findRegion("Cong_1_mau_moi_40s"), screenInfo);
		details[0][12] = new TextureRegionInfo(itemInfoAtlas.findRegion("Cong_them_15_mau"), screenInfo);
		details[0][13] = new TextureRegionInfo(itemInfoAtlas.findRegion("aicap"), screenInfo);
		details[0][14] = new TextureRegionInfo(itemInfoAtlas.findRegion("HaiQuan"), screenInfo);
		
//		details[1][0] = new TextureRegionInfo(itemInfoAtlas.findRegion("weapon_viet"), screenInfo);
//		details[1][1] = new TextureRegionInfo(itemInfoAtlas.findRegion("bruce"), screenInfo);
//		details[1][2] = new TextureRegionInfo(itemInfoAtlas.findRegion("samurai"), screenInfo);
//		details[1][3] = new TextureRegionInfo(itemInfoAtlas.findRegion("gla"), screenInfo);
//		details[1][4] = new TextureRegionInfo(itemInfoAtlas.findRegion("Skill_viet"), screenInfo);
//		details[1][5] = new TextureRegionInfo(itemInfoAtlas.findRegion("slow"), screenInfo);
//		details[1][6] = new TextureRegionInfo(itemInfoAtlas.findRegion("bomb"), screenInfo);
//		details[1][7] = new TextureRegionInfo(itemInfoAtlas.findRegion("battu"), screenInfo);
//		details[1][8] = new TextureRegionInfo(itemInfoAtlas.findRegion("Bonus_viet"), screenInfo);
//		details[1][9] = new TextureRegionInfo(itemInfoAtlas.findRegion("timeadd"), screenInfo);
//		details[1][10] = new TextureRegionInfo(itemInfoAtlas.findRegion("moneyadd"), screenInfo);
//		details[1][11] = new TextureRegionInfo(itemInfoAtlas.findRegion("130"), screenInfo);
//		details[1][12] = new TextureRegionInfo(itemInfoAtlas.findRegion("cooldown"), screenInfo);
//		details[1][13] = new TextureRegionInfo(itemInfoAtlas.findRegion("Text_Aicap"), screenInfo);
//		details[1][14] = new TextureRegionInfo(itemInfoAtlas.findRegion("Text_HaiQuan"), screenInfo);

		
		moneyAtlas = manager.get("data/menu/money.atlas", TextureAtlas.class);
		moneyButton = new TextureRegionInfo[2];
		moneyButton[0] = new TextureRegionInfo(menuAtlas.findRegion("Nap_tien_TiengVIET"), screenInfo);
		moneyButton[1] = new TextureRegionInfo(menuAtlas.findRegion("Nap_tien_tiengANH"), screenInfo);
		screenPay = new TextureRegionInfo(moneyAtlas.findRegion("1"), screenInfo);
		buy8Dialog = new TextureRegionInfo(moneyAtlas.findRegion("tieptucchoi"), screenInfo);
		
		hocchiendau = new TextureRegionInfo[2];
		tancong6huong = new TextureRegionInfo[2];
		chamvaomanhinh = new TextureRegionInfo[2];
		hoccombo = new TextureRegionInfo[2];
		cham2diem = new TextureRegionInfo[2];
		tieudiet2 = new TextureRegionInfo[2];
		kethopcombo = new TextureRegionInfo[2];
		combobanthich = new TextureRegionInfo[2];
		kickkinang = new TextureRegionInfo[2];
		chamnvc = new TextureRegionInfo[2];
		chamicon = new TextureRegionInfo[2];
	   kichhoatskill = new TextureRegionInfo[2];
	   ngonrui = new TextureRegionInfo[2];
	   
		tutAtlas = manager.get("data/game/tut/tut.atlas", TextureAtlas.class);
		next = new TextureRegionInfo(tutAtlas.findRegion("2"), screenInfo);
		prev = new TextureRegionInfo(tutAtlas.findRegion("1"), screenInfo);
		
		hocchiendau[0] = new TextureRegionInfo(tutAtlas.findRegion("CHIENDAU"), screenInfo);
		hocchiendau[1] = new TextureRegionInfo(tutAtlas.findRegion("4"), screenInfo);
		leftHere = new TextureRegionInfo(tutAtlas.findRegion("5"), screenInfo);
		rightHere = new TextureRegionInfo(tutAtlas.findRegion("6"), screenInfo);		
		tancong6huong[0] = new TextureRegionInfo(tutAtlas.findRegion("6HUONG"), screenInfo);
		tancong6huong[1] = new TextureRegionInfo(tutAtlas.findRegion("3"), screenInfo);
		chamvaomanhinh[0] = new TextureRegionInfo(tutAtlas.findRegion("CHAM"), screenInfo);
		chamvaomanhinh[1] = new TextureRegionInfo(tutAtlas.findRegion("7"), screenInfo);
		midAttack = new TextureRegionInfo(tutAtlas.findRegion("8"), screenInfo);
		midAttack2 = new TextureRegionInfo(tutAtlas.findRegion("13"), screenInfo);

		hiAttack = new TextureRegionInfo(tutAtlas.findRegion("9"), screenInfo);
		hiAttack2 = new TextureRegionInfo(tutAtlas.findRegion("14"), screenInfo);
		lowAttack = new TextureRegionInfo(tutAtlas.findRegion("10"), screenInfo);
		lowAttack2 = new TextureRegionInfo(tutAtlas.findRegion("12"), screenInfo);
		hoccombo[0] = new TextureRegionInfo(tutAtlas.findRegion("HOCCOMBO"), screenInfo);
		hoccombo[1] = new TextureRegionInfo(tutAtlas.findRegion("11"), screenInfo);	
		cham2diem[0] = new TextureRegionInfo(tutAtlas.findRegion("CUNGMOTLUC"), screenInfo);
		cham2diem[1] = new TextureRegionInfo(tutAtlas.findRegion("15"), screenInfo);
		tieudiet2[0] = new TextureRegionInfo(tutAtlas.findRegion("2KEDICH"), screenInfo);
		tieudiet2[1] = new TextureRegionInfo(tutAtlas.findRegion("16"), screenInfo);
		kethopcombo[0] = new TextureRegionInfo(tutAtlas.findRegion("KETHOP"), screenInfo);	
		kethopcombo[1] = new TextureRegionInfo(tutAtlas.findRegion("18"), screenInfo);	 
		combobanthich[0] = new TextureRegionInfo(tutAtlas.findRegion("BANTHICH"), screenInfo);
		combobanthich[1] = new TextureRegionInfo(tutAtlas.findRegion("17"), screenInfo);
		kickkinang[0] = new TextureRegionInfo(tutAtlas.findRegion("KYNANG"), screenInfo);
		kickkinang[1] = new TextureRegionInfo(tutAtlas.findRegion("19"), screenInfo);
		chamnvc[0] = new TextureRegionInfo(tutAtlas.findRegion("NHANVAT"), screenInfo);	
		chamnvc[1] = new TextureRegionInfo(tutAtlas.findRegion("21"), screenInfo);
		chamday = new TextureRegionInfo(tutAtlas.findRegion("24"), screenInfo);	
		chamicon[0] = new TextureRegionInfo(tutAtlas.findRegion("ICON"), screenInfo);
		chamicon[1] = new TextureRegionInfo(tutAtlas.findRegion("22"), screenInfo);
		kichhoatskill[0] = new TextureRegionInfo(tutAtlas.findRegion("KICHHOAT"), screenInfo);
		kichhoatskill[1] = new TextureRegionInfo(tutAtlas.findRegion("23"), screenInfo);
		ngonrui[0] = new TextureRegionInfo(tutAtlas.findRegion("NGON"), screenInfo);
		ngonrui[1] = new TextureRegionInfo(tutAtlas.findRegion("25"), screenInfo);
		
	}	
	
	public static void load()
	{
		Texture.setEnforcePotImages(false);
		
		manager.load("data/game/rank/rank.atlas", TextureAtlas.class);
		manager.load("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class);
		manager.load("data/menu/menu.atlas", TextureAtlas.class);
		manager.load("data/game/thuoc/thuoc.atlas", TextureAtlas.class);
		manager.load("data/game/bom/bom.atlas", TextureAtlas.class);
		manager.load("data/game/comment/comment.atlas", TextureAtlas.class);
		manager.load("data/menu/item.atlas", TextureAtlas.class);
		manager.load("data/menu/item_info.atlas", TextureAtlas.class);
		manager.load("data/game/blood/blood.atlas", TextureAtlas.class);
		manager.load("data/game/blood3/blood3.atlas", TextureAtlas.class);
		manager.load("data/game/score/score.atlas", TextureAtlas.class);
		manager.load("data/menu/money.atlas", TextureAtlas.class);
		manager.load("data/game/hit/hit.atlas", TextureAtlas.class);
		manager.load("data/game/pass/pass.atlas", TextureAtlas.class);

		manager.load("data/game/tut/tut.atlas", TextureAtlas.class);	

		manager.load("data/music/begin.ogg", Music.class);
		manager.load("data/music/nen.ogg", Music.class);
		
		manager.load("data/music/vu.ogg", Sound.class);
		manager.load("data/music/chuong.ogg", Sound.class);
		manager.load("data/music/glass_break.ogg", Sound.class);		
		manager.load("data/music/kim_loai.ogg", Sound.class);		
		manager.load("data/music/achoo.mp3", Sound.class);	
	}

	public static void loadedTut(GameOS game, ScreenInfo screenInfo)
	{		
		nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);		
		stageHaNoiAtlas = manager.get("data/game/stage/hanoi.atlas", TextureAtlas.class);
		bgGameHaNoi = new TextureRegionInfo(stageHaNoiAtlas.findRegion("1"), screenInfo);
		nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		cai_kim = new FrameRegionInfo(manager.get("data/game/cai_kim/cai_kim.atlas", TextureAtlas.class), 13,screenInfo);
		coi_gia = new FrameRegionInfo(manager.get("data/game/coi_gia/coi_gia.atlas", TextureAtlas.class), 13,screenInfo);
   	chai_bia = new FrameRegionInfo(manager.get("data/game/chai_bia/chai_bia.atlas", TextureAtlas.class), 14,screenInfo);
   	cho_ha_noi = new FrameRegionInfo(manager.get("data/game/cho_ha_noi/cho_ha_noi.atlas", TextureAtlas.class), 8,screenInfo);
		dau_gau_ha_noi1 = new FrameRegionInfo(manager.get("data/game/dau_gau_ha_noi1/dau_gau_ha_noi1.atlas", TextureAtlas.class), 8,screenInfo);	

		dau_gau_ha_noi2 = new FrameRegionInfo(manager.get("data/game/dau_gau_ha_noi2/dau_gau_ha_noi2.atlas", TextureAtlas.class), 8,screenInfo);		
	}
	
	public static void loadTut()
	{
		manager.load("data/game/cai_kim/cai_kim.atlas", TextureAtlas.class);
		manager.load("data/game/coi_gia/coi_gia.atlas", TextureAtlas.class);
		manager.load("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class);
		manager.load("data/game/chai_bia/chai_bia.atlas", TextureAtlas.class);
		manager.load("data/game/cho_ha_noi/cho_ha_noi.atlas", TextureAtlas.class);	
		manager.load("data/game/dau_gau_ha_noi1/dau_gau_ha_noi1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_ha_noi2/dau_gau_ha_noi2.atlas", TextureAtlas.class);						
		manager.load("data/game/stage/hanoi.atlas", TextureAtlas.class);
	}
	
	public static void unloadTut()
	{
		manager.unload("data/game/cai_kim/cai_kim.atlas");
		manager.unload("data/game/coi_gia/coi_gia.atlas");
		manager.unload("data/game/chai_bia/chai_bia.atlas");
		manager.unload("data/game/cho_ha_noi/cho_ha_noi.atlas");	
		manager.unload("data/game/dau_gau_ha_noi1/dau_gau_ha_noi1.atlas");
		manager.unload("data/game/dau_gau_ha_noi2/dau_gau_ha_noi2.atlas");		
		manager.unload("data/game/stage/hanoi.atlas");	
		
	}
	
	public static void loadHanoi()
	{
		manager.load("data/game/cai_kim/cai_kim.atlas", TextureAtlas.class);
		manager.load("data/game/coi_gia/coi_gia.atlas", TextureAtlas.class);
		manager.load("data/game/chai_bia/chai_bia.atlas", TextureAtlas.class);
		manager.load("data/game/cho_ha_noi/cho_ha_noi.atlas", TextureAtlas.class);	
		manager.load("data/game/dau_gau_ha_noi1/dau_gau_ha_noi1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_ha_noi2/dau_gau_ha_noi2.atlas", TextureAtlas.class);						
		manager.load("data/game/stage/hanoi.atlas", TextureAtlas.class);
	}
	
	public static void unloadHanoi()
	{
		manager.unload("data/game/cai_kim/cai_kim.atlas");
		manager.unload("data/game/coi_gia/coi_gia.atlas");
		manager.unload("data/game/chai_bia/chai_bia.atlas");
		manager.unload("data/game/cho_ha_noi/cho_ha_noi.atlas");	
		manager.unload("data/game/dau_gau_ha_noi1/dau_gau_ha_noi1.atlas");
		manager.unload("data/game/dau_gau_ha_noi2/dau_gau_ha_noi2.atlas");		
		manager.unload("data/game/stage/hanoi.atlas");		
		
	}
	
	public static void loadNVC(GameOS game)
	{	
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			manager.load("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class);
		if(id == 2)
			manager.load("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				manager.load("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class);
			if(game.gameScreen.nvc.delta == 1)
				manager.load("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class);
			if(game.gameScreen.nvc.delta == 2)
				manager.load("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class);
		}
	}
	
	public static void loadedHanoi(GameOS game, ScreenInfo screenInfo)
	{
		stageHaNoiAtlas = manager.get("data/game/stage/hanoi.atlas", TextureAtlas.class);
		bgGameHaNoi = new TextureRegionInfo(stageHaNoiAtlas.findRegion("1"), screenInfo);

		//if(manager.update()){
		cai_kim = new FrameRegionInfo(manager.get("data/game/cai_kim/cai_kim.atlas", TextureAtlas.class), 13,screenInfo);
		coi_gia = new FrameRegionInfo(manager.get("data/game/coi_gia/coi_gia.atlas", TextureAtlas.class), 13,screenInfo);
   	chai_bia = new FrameRegionInfo(manager.get("data/game/chai_bia/chai_bia.atlas", TextureAtlas.class), 14,screenInfo);
		
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		if(id == 2)
			nvc_dao = new FrameRegionInfo(manager.get("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class), 28,screenInfo);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				nvc_dau = new FrameRegionInfo(manager.get("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 1)
				nvc_ai = new FrameRegionInfo(manager.get("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 2)
				nvc_sa = new FrameRegionInfo(manager.get("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class), 14,screenInfo);
		}
		cho_ha_noi = new FrameRegionInfo(manager.get("data/game/cho_ha_noi/cho_ha_noi.atlas", TextureAtlas.class), 7,screenInfo);
		dau_gau_ha_noi1 = new FrameRegionInfo(manager.get("data/game/dau_gau_ha_noi1/dau_gau_ha_noi1.atlas", TextureAtlas.class), 9,screenInfo);	
		dau_gau_ha_noi2 = new FrameRegionInfo(manager.get("data/game/dau_gau_ha_noi2/dau_gau_ha_noi2.atlas", TextureAtlas.class), 8,screenInfo);	
		//}
	}
	
	public static void loadHalong()
	{
		manager.load("data/game/dau_gau_ha_long/dau_gau_ha_long1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_2_ha_long/dau_gau_ha_long2.atlas", TextureAtlas.class);
		manager.load("data/game/bua/bua.atlas", TextureAtlas.class);
		manager.load("data/game/mo_neo/mo_neo.atlas", TextureAtlas.class);
		manager.load("data/game/oc_sen/oc_sen.atlas", TextureAtlas.class);	
		manager.load("data/game/stage/halong.atlas", TextureAtlas.class);	
	}
	
	public static void unloadHalong()
	{
		manager.unload("data/game/dau_gau_ha_long/dau_gau_ha_long1.atlas");
		manager.unload("data/game/dau_gau_2_ha_long/dau_gau_ha_long2.atlas");
		manager.unload("data/game/bua/bua.atlas");
		manager.unload("data/game/mo_neo/mo_neo.atlas");
		manager.unload("data/game/oc_sen/oc_sen.atlas");	
		manager.unload("data/game/stage/halong.atlas");	
	}
	
	public static void loadedHalong(GameOS game, ScreenInfo screenInfo)
	{	
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		if(id == 2)
			nvc_dao = new FrameRegionInfo(manager.get("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class), 28,screenInfo);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				nvc_dau = new FrameRegionInfo(manager.get("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 1)
				nvc_ai = new FrameRegionInfo(manager.get("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 2)
				nvc_sa = new FrameRegionInfo(manager.get("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class), 14,screenInfo);
		}
		stageHalongAtlas = manager.get("data/game/stage/halong.atlas", TextureAtlas.class);
		bgGameHaLong = new TextureRegionInfo(stageHalongAtlas.findRegion("8"), screenInfo);
		dau_gau_ha_long1 = new FrameRegionInfo(manager.get("data/game/dau_gau_ha_long/dau_gau_ha_long1.atlas", TextureAtlas.class), 8,screenInfo);	
		dau_gau_ha_long2 = new FrameRegionInfo(manager.get("data/game/dau_gau_2_ha_long/dau_gau_ha_long2.atlas", TextureAtlas.class), 8,screenInfo);	
		bua = new FrameRegionInfo(manager.get("data/game/bua/bua.atlas", TextureAtlas.class), 13,screenInfo);
		mo_neo = new FrameRegionInfo(manager.get("data/game/mo_neo/mo_neo.atlas", TextureAtlas.class), 13,screenInfo);
		oc_sen = new FrameRegionInfo(manager.get("data/game/oc_sen/oc_sen.atlas", TextureAtlas.class), 6,screenInfo);
		//}
	}
	
	public static void loadSaigon()
	{
		manager.load("data/game/guoc/guoc.atlas", TextureAtlas.class);
		manager.load("data/game/may_say_toc/may_say_toc.atlas", TextureAtlas.class);
		manager.load("data/game/meo/meo.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_sai_gon_1/dau_gau_sai_gon_1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_sai_gon_2/dau_gau_sai_gon_2.atlas", TextureAtlas.class);
		manager.load("data/game/stage/saigon.atlas", TextureAtlas.class);	
	}
	
	public static void unloadSaigon()
	{
		manager.unload("data/game/guoc/guoc.atlas");
		manager.unload("data/game/may_say_toc/may_say_toc.atlas");
		manager.unload("data/game/meo/meo.atlas");
		manager.unload("data/game/dau_gau_sai_gon_1/dau_gau_sai_gon_1.atlas");
		manager.unload("data/game/dau_gau_sai_gon_2/dau_gau_sai_gon_2.atlas");
		manager.unload("data/game/stage/saigon.atlas");	
	}
	
	public static void loadedSaigon(GameOS game, ScreenInfo screenInfo)
	{	
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		if(id == 2)
			nvc_dao = new FrameRegionInfo(manager.get("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class), 28,screenInfo);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				nvc_dau = new FrameRegionInfo(manager.get("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 1)
				nvc_ai = new FrameRegionInfo(manager.get("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 2)
				nvc_sa = new FrameRegionInfo(manager.get("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class), 14,screenInfo);
		}
		stageSaigonAtlas = manager.get("data/game/stage/saigon.atlas", TextureAtlas.class);
		bgGameSaigon = new TextureRegionInfo(stageSaigonAtlas.findRegion("9"), screenInfo);
		dau_gau_sai_gon_1 = new FrameRegionInfo(manager.get("data/game/dau_gau_sai_gon_1/dau_gau_sai_gon_1.atlas", TextureAtlas.class), 8,screenInfo);	
		dau_gau_sai_gon_2 = new FrameRegionInfo(manager.get("data/game/dau_gau_sai_gon_2/dau_gau_sai_gon_2.atlas", TextureAtlas.class), 9,screenInfo);	
		guoc = new FrameRegionInfo(manager.get("data/game/guoc/guoc.atlas", TextureAtlas.class), 13,screenInfo);
		may_say_toc = new FrameRegionInfo(manager.get("data/game/may_say_toc/may_say_toc.atlas", TextureAtlas.class), 13,screenInfo);
		meo = new FrameRegionInfo(manager.get("data/game/meo/meo.atlas", TextureAtlas.class), 7,screenInfo);
		//}
	}
	
	public static void loadJapan()
	{
		manager.load("data/game/dao/dao.atlas", TextureAtlas.class);
		manager.load("data/game/phi_tieu/phi_tieu.atlas", TextureAtlas.class);
		manager.load("data/game/ran/ran.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_nhat_1/dau_gau_nhat_1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_nhat_2/dau_gau_nhat_2.atlas", TextureAtlas.class);
		manager.load("data/game/stage/japan.atlas", TextureAtlas.class);	
	}
	
	public static void unloadJapan()
	{
		manager.unload("data/game/dao/dao.atlas");
		manager.unload("data/game/phi_tieu/phi_tieu.atlas");
		manager.unload("data/game/ran/ran.atlas");
		manager.unload("data/game/dau_gau_nhat_1/dau_gau_nhat_1.atlas");
		manager.unload("data/game/dau_gau_nhat_2/dau_gau_nhat_2.atlas");
		manager.unload("data/game/stage/japan.atlas");	
	}
	
	public static void loadedJapan(GameOS game, ScreenInfo screenInfo)
	{	
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		if(id == 2)
			nvc_dao = new FrameRegionInfo(manager.get("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class), 28,screenInfo);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				nvc_dau = new FrameRegionInfo(manager.get("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 1)
				nvc_ai = new FrameRegionInfo(manager.get("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 2)
				nvc_sa = new FrameRegionInfo(manager.get("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class), 14,screenInfo);
		}
		stageJapanAtlas = manager.get("data/game/stage/japan.atlas", TextureAtlas.class);
		bgGameJapan = new TextureRegionInfo(stageJapanAtlas.findRegion("10"), screenInfo);
		
		dau_gau_nhat_1 = new FrameRegionInfo(manager.get("data/game/dau_gau_nhat_1/dau_gau_nhat_1.atlas", TextureAtlas.class), 8,screenInfo);	
		dau_gau_nhat_2 = new FrameRegionInfo(manager.get("data/game/dau_gau_nhat_2/dau_gau_nhat_2.atlas", TextureAtlas.class), 8,screenInfo);	
		dao = new FrameRegionInfo(manager.get("data/game/dao/dao.atlas", TextureAtlas.class), 13,screenInfo);
		phi_tieu = new FrameRegionInfo(manager.get("data/game/phi_tieu/phi_tieu.atlas", TextureAtlas.class), 13,screenInfo);
		ran = new FrameRegionInfo(manager.get("data/game/ran/ran.atlas", TextureAtlas.class), 10,screenInfo);
		//}		
	}
	
	public static void loadRome()
	{
		manager.load("data/game/riu/riu.atlas", TextureAtlas.class);
		manager.load("data/game/luoi_3/luoi_3.atlas", TextureAtlas.class);
		manager.load("data/game/ngua_dien/ngua_dien.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_roma_1/dau_gau_roma_1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_roma_2/dau_gau_roma_2.atlas", TextureAtlas.class);
		manager.load("data/game/stage/roma.atlas", TextureAtlas.class);	
	}
	
	public static void unloadRome()
	{
		manager.unload("data/game/riu/riu.atlas");
		manager.unload("data/game/luoi_3/luoi_3.atlas");
		manager.unload("data/game/ngua_dien/ngua_dien.atlas");
		manager.unload("data/game/dau_gau_roma_1/dau_gau_roma_1.atlas");
		manager.unload("data/game/dau_gau_roma_2/dau_gau_roma_2.atlas");
		manager.unload("data/game/stage/roma.atlas");	
	}
	
	public static void loadedRome(GameOS game, ScreenInfo screenInfo)
	{	
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		if(id == 2)
			nvc_dao = new FrameRegionInfo(manager.get("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class), 28,screenInfo);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				nvc_dau = new FrameRegionInfo(manager.get("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 1)
				nvc_ai = new FrameRegionInfo(manager.get("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 2)
				nvc_sa = new FrameRegionInfo(manager.get("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class), 14,screenInfo);
		}
		stageRomaAtlas = manager.get("data/game/stage/roma.atlas", TextureAtlas.class);
		bgGameRoma = new TextureRegionInfo(stageRomaAtlas.findRegion("11"), screenInfo);
		dau_gau_roma_1 = new FrameRegionInfo(manager.get("data/game/dau_gau_roma_1/dau_gau_roma_1.atlas", TextureAtlas.class), 8,screenInfo);	
		dau_gau_roma_2 = new FrameRegionInfo(manager.get("data/game/dau_gau_roma_2/dau_gau_roma_2.atlas", TextureAtlas.class), 8,screenInfo);	
		riu = new FrameRegionInfo(manager.get("data/game/riu/riu.atlas", TextureAtlas.class), 13,screenInfo);
		luoi_3 = new FrameRegionInfo(manager.get("data/game/luoi_3/luoi_3.atlas", TextureAtlas.class), 13,screenInfo);
		ngua_dien = new FrameRegionInfo(manager.get("data/game/ngua_dien/ngua_dien.atlas", TextureAtlas.class), 10,screenInfo);
		//}
	}
	
	public static void loadAicap()
	{
		manager.load("data/game/dao_gam/dao_gam.atlas", TextureAtlas.class);
		manager.load("data/game/luoi_hai/luoi_hai.atlas", TextureAtlas.class);
		manager.load("data/game/bo_cap/bo_cap.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_ai_cap_1/dau_gau_ai_cap_1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_ai_cap_2/dau_gau_ai_cap_2.atlas", TextureAtlas.class);
		manager.load("data/game/stage/aicap.atlas", TextureAtlas.class);	
	}
	
	public static void unloadAicap()
	{
		manager.unload("data/game/dao_gam/dao_gam.atlas");
		manager.unload("data/game/luoi_hai/luoi_hai.atlas");
		manager.unload("data/game/bo_cap/bo_cap.atlas");
		manager.unload("data/game/dau_gau_ai_cap_1/dau_gau_ai_cap_1.atlas");
		manager.unload("data/game/dau_gau_ai_cap_2/dau_gau_ai_cap_2.atlas");
		manager.unload("data/game/stage/aicap.atlas");	
	}
	
	public static void loadedAicap(GameOS game, ScreenInfo screenInfo)
	{	
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		if(id == 2)
			nvc_dao = new FrameRegionInfo(manager.get("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class), 28,screenInfo);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				nvc_dau = new FrameRegionInfo(manager.get("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 1)
				nvc_ai = new FrameRegionInfo(manager.get("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 2)
				nvc_sa = new FrameRegionInfo(manager.get("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class), 14,screenInfo);
		}
		stageAicapAtlas = manager.get("data/game/stage/aicap.atlas", TextureAtlas.class);
		bgGameAiCap = new TextureRegionInfo(stageAicapAtlas.findRegion("45"), screenInfo);
		dau_gau_ai_cap_1 = new FrameRegionInfo(manager.get("data/game/dau_gau_ai_cap_1/dau_gau_ai_cap_1.atlas", TextureAtlas.class), 8,screenInfo);	
		dau_gau_ai_cap_2 = new FrameRegionInfo(manager.get("data/game/dau_gau_ai_cap_2/dau_gau_ai_cap_2.atlas", TextureAtlas.class), 8,screenInfo);	
		dao_gam = new FrameRegionInfo(manager.get("data/game/dao_gam/dao_gam.atlas", TextureAtlas.class), 14,screenInfo);
		luoi_hai = new FrameRegionInfo(manager.get("data/game/luoi_hai/luoi_hai.atlas", TextureAtlas.class), 14,screenInfo);
		bo_cap = new FrameRegionInfo(manager.get("data/game/bo_cap/bo_cap.atlas", TextureAtlas.class), 9,screenInfo);
		//}
	}
	
	public static void loadDao()
	{
		manager.load("data/game/dao_dua/dao_dua.atlas", TextureAtlas.class);
		manager.load("data/game/ten_lua/ten_lua.atlas", TextureAtlas.class);
		manager.load("data/game/cho_dao/cho_dao.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_dao_1/dau_gau_dao_1.atlas", TextureAtlas.class);
		manager.load("data/game/dau_gau_dao_2/dau_gau_dao_2.atlas", TextureAtlas.class);
		manager.load("data/game/stage/dao.atlas", TextureAtlas.class);	
	}
	
	public static void unloadDao()
	{
		manager.unload("data/game/dao_dua/dao_dua.atlas");
		manager.unload("data/game/ten_lua/ten_lua.atlas");
		manager.unload("data/game/cho_dao/cho_dao.atlas");
		manager.unload("data/game/dau_gau_dao_1/dau_gau_dao_1.atlas");
		manager.unload("data/game/dau_gau_dao_2/dau_gau_dao_2.atlas");
		manager.unload("data/game/stage/dao.atlas");	
	}
	
	public static void loadedDao(GameOS game, ScreenInfo screenInfo)
	{	
		int id = 1;
		for(int i=0;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab1Item[i].isEquipe)
			{
				id = i+1;
			}
		}
		if(game.gameScreen.nvc.delta>0)
		{
			id = 3;
		}
		if(id == 1)
			nvc_normal = new FrameRegionInfo(manager.get("data/game/nvc_normal/nvc_normal.atlas", TextureAtlas.class), 16,screenInfo);
		if(id == 2)
			nvc_dao = new FrameRegionInfo(manager.get("data/game/nvc_dao/vo_si_dao.atlas", TextureAtlas.class), 28,screenInfo);
		if(id == 3){
			if(game.gameScreen.nvc.delta == 0)
				nvc_dau = new FrameRegionInfo(manager.get("data/game/nvc_dau/vo_si_giac_dau.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 1)
				nvc_ai = new FrameRegionInfo(manager.get("data/game/nvc_ai/nvc_ai.atlas", TextureAtlas.class), 28,screenInfo);
			if(game.gameScreen.nvc.delta == 2)
				nvc_sa = new FrameRegionInfo(manager.get("data/game/nvc_sa/nvc_sa.atlas", TextureAtlas.class), 14,screenInfo);
		}
		stageDaoAtlas = manager.get("data/game/stage/dao.atlas", TextureAtlas.class);
		bgGameDao = new TextureRegionInfo(stageDaoAtlas.findRegion("12"), screenInfo);
		dau_gau_dao_1 = new FrameRegionInfo(manager.get("data/game/dau_gau_dao_1/dau_gau_dao_1.atlas", TextureAtlas.class), 8,screenInfo);	
		dau_gau_dao_2 = new FrameRegionInfo(manager.get("data/game/dau_gau_dao_2/dau_gau_dao_2.atlas", TextureAtlas.class), 8,screenInfo);	
		dao_dua = new FrameRegionInfo(manager.get("data/game/dao_dua/dao_dua.atlas", TextureAtlas.class), 14,screenInfo);
		ten_lua = new FrameRegionInfo(manager.get("data/game/ten_lua/ten_lua.atlas", TextureAtlas.class), 8,screenInfo);
		cho_dao = new FrameRegionInfo(manager.get("data/game/cho_dao/cho_dao.atlas", TextureAtlas.class), 6,screenInfo);
		//}
	}
}
