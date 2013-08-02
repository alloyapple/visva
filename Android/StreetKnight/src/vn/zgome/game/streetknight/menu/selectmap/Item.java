package vn.zgome.game.streetknight.menu.selectmap;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class Item extends IEntityMenu{

	public static final int ITEM_HANOI = 1;
	public static final int ITEM_HALONG = 2;
	public int id;
	public Item (GameOS game, IScreen screen, int id) {
		super(game, screen);
		this.id = id;
		// TODO Auto-generated constructor stub
	}

	ImageButton but;
	
	void checkNVC(){
	if(game.dataSave.isEquip[3])
	{
		game.gameScreen.nvc.delta = 1;
	}
	else if(game.dataSave.isEquip[4])
	{
		game.gameScreen.nvc.delta = 2;
	}
	else if(game.dataSave.isEquip[2])
		game.gameScreen.nvc.delta = 0;
	else 
		game.gameScreen.nvc.delta = -1;
	}
	@Override
	public void start () {
		// TODO Auto-generated method stub		
		if(id == ITEM_HANOI){
			but = game.stageHelper.createImageButton2( game.dataSave.stateMap[1]==1?Asset.itemHanoiRegion:Asset.itemHanoiRegion, game.dataSave.stateMap[1]==1?Asset.itemHanoiRegion:Asset.itemHanoiRegion,game.X(110), game.Y(74), game.X(306), game.Y(490-167));						
			//but.addActor(game.stageHelper.createImageButton2(Asset.stringHanoiRegion, Asset.stringHanoiRegion,game.X(110), game.Y(37), game.X(0), game.Y(74)));
			//if(game.dataSave.stateMap[5]!=1){
				but.addActor(addString("HÀ NỘI", 0, game.Y(74), game.X(110),0,1f,game.freeFontTypeTool.variBigFont));
			//}
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {					
					// TODO Auto-generated method stub					
					game.menuScreen.shopUi.shopData.getDataSave();
					checkNVC();
					_screen.nameMapUi.set(1);
					game.gameScreen.stage = 1;
					_screen.nameMapUi.show();
					if(game.gameScreen.stage == 1)
						Asset.loadHanoi();
					Asset.loadNVC(game);
				}
			});
		}		
		// Sài gòn
		else if(id == 3){
			but = game.stageHelper.createImageButton2( game.dataSave.stateMap[3]==1?Asset.itemSaigonRegion:Asset.itemSaigonLockRegion,  game.dataSave.stateMap[3]==1?Asset.itemSaigonRegion:Asset.itemSaigonLockRegion,game.X(110), game.Y(74), game.X(588), game.Y(490-167));			
			but.addActor(addString("SÀI GÒN", 0, game.Y(74), game.X(110),0,1f,game.freeFontTypeTool.variBigFont));
			if(game.dataSave.stateMap[3]!=1){
			but.addActor(addString("Score unlock:", 0, game.Y(45), game.X(110),0,1f,game.freeFontTypeTool.variSmallFont));
			but.addActor(addString(""+game.dataSave.scoreUnlock[1], 0, game.Y(0), game.X(110),0,1f,game.freeFontTypeTool.variBiggerFont));
			}
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub					
					if(Asset.isTestVersion||(!Asset.isTestVersion&&game.dataSave.stateMap[3]==1)){
						game.menuScreen.shopUi.shopData.getDataSave();
						checkNVC();
					_screen.nameMapUi.set(3);
					game.gameScreen.stage = 3;
					_screen.nameMapUi.show();
					if(game.gameScreen.stage == 3)
						Asset.loadSaigon();
					Asset.loadNVC(game);
					}
				}
			});
		}
		else if(id == ITEM_HALONG){
			but = game.stageHelper.createImageButton2( game.dataSave.stateMap[2]==1?Asset.itemHalongRegion:Asset.itemHalongLockRegion,  game.dataSave.stateMap[2]==1?Asset.itemHalongRegion:Asset.itemHalongLockRegion,game.X(110), game.Y(74), game.X(447), game.Y(490-167));			
			but.addActor(addString("HẠ LONG", 0, game.Y(74), game.X(110),0,1f,game.freeFontTypeTool.variBigFont));

			if(game.dataSave.stateMap[2]!=1){
				but.addActor(addString("Score unlock:", 0, game.Y(45), game.X(110),0,1f,game.freeFontTypeTool.variSmallFont));
				but.addActor(addString(""+game.dataSave.scoreUnlock[0], 0, game.Y(0), game.X(110),0,1f,game.freeFontTypeTool.variBiggerFont));
			}
			//but.addActor(game.stageHelper.createImageButton2(Asset.stringHalongRegion, Asset.stringHalongRegion, game.X(110), game.Y(37), game.X(0), game.Y(74)));
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub					
					if(Asset.isTestVersion||(!Asset.isTestVersion&&game.dataSave.stateMap[2]==1)){
						game.menuScreen.shopUi.shopData.getDataSave();
						checkNVC();
					_screen.nameMapUi.set(2);
					game.gameScreen.stage = 2;
					_screen.nameMapUi.show();
					if(game.gameScreen.stage == 2)
						Asset.loadHalong();
					Asset.loadNVC(game);
					}					
				}
			});
		}
		else if(id == 4){
			but = game.stageHelper.createImageButton2( game.dataSave.stateMap[4]==1?Asset.itemJapanRegion:Asset.itemJapanLockRegion,  game.dataSave.stateMap[4]==1?Asset.itemJapanRegion:Asset.itemJapanLockRegion,game.X(110), game.Y(74), game.X(164), game.Y(490-313));			
			but.addActor(addString("JAPAN", 0, game.Y(74), game.X(110),0,1f,game.freeFontTypeTool.variBigFont));			
			if(game.dataSave.stateMap[4]!=1){
				but.addActor(addString("Score unlock:", 0, game.Y(45), game.X(110),0,1f,game.freeFontTypeTool.variSmallFont));
				but.addActor(addString(""+game.dataSave.scoreUnlock[2], 0, game.Y(0), game.X(110),0,1f,game.freeFontTypeTool.variBiggerFont));
			}
			//but.addActor(game.stageHelper.createImageButton2(Asset.stringJapanRegion, Asset.stringJapanRegion, game.X(110), game.Y(37), game.X(0), game.Y(74)));
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub					
					if(Asset.isTestVersion||(!Asset.isTestVersion&&game.dataSave.stateMap[4]==1)){
						game.menuScreen.shopUi.shopData.getDataSave();
						checkNVC();
					_screen.nameMapUi.set(4);
					game.gameScreen.stage = 4;
					_screen.nameMapUi.show();
					if(game.gameScreen.stage == 4)
						Asset.loadJapan();
					Asset.loadNVC(game);
					}
				}
			});
		}
		else if(id == 5){
			but = game.stageHelper.createImageButton2( game.dataSave.stateMap[5]==1?Asset.itemRomaRegion:Asset.itemRomaLockRegion,  game.dataSave.stateMap[5]==1?Asset.itemRomaRegion:Asset.itemRomaLockRegion,game.X(110), game.Y(74), game.X(308), game.Y(490-313));			
			//but.addActor(game.stageHelper.createImageButton2(Asset.stringRomaRegion, Asset.stringRomaRegion,  game.X(110), game.Y(37), game.X(0), game.Y(74)));
			but.addActor(addString("ROME", 0, game.Y(74), game.X(110),0,1f,game.freeFontTypeTool.variBigFont));

			if(game.dataSave.stateMap[5]!=1){
				but.addActor(addString("Score unlock:", 0, game.Y(45), game.X(110),0,1f,game.freeFontTypeTool.variSmallFont));
				but.addActor(addString(""+game.dataSave.scoreUnlock[3], 0, game.Y(0), game.X(110),0,1f,game.freeFontTypeTool.variBiggerFont));
			}
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub					
					if(Asset.isTestVersion||(!Asset.isTestVersion&&game.dataSave.stateMap[5]==1)){
						game.menuScreen.shopUi.shopData.getDataSave();
						checkNVC();
					_screen.nameMapUi.set(5);
					game.gameScreen.stage = 5;
					_screen.nameMapUi.show();
					if(game.gameScreen.stage == 5)
						Asset.loadRome();
					Asset.loadNVC(game);
					}
				}
			});
		}
		else if(id == 6){
			but = game.stageHelper.createImageButton2( game.dataSave.stateMap[6]==1?Asset.itemAicapRegion:Asset.itemAicapLockRegion,  game.dataSave.stateMap[6]==1?Asset.itemAicapRegion:Asset.itemAicapLockRegion,game.X(110), game.Y(74), game.X(444), game.Y(490-313));			
			but.addActor(addString("EGYPT", 0, game.Y(74), game.X(110),0,1f,game.freeFontTypeTool.variBigFont));
			if(game.dataSave.stateMap[6]!=1){
				but.addActor(addString("Score unlock:", 0, game.Y(45), game.X(110),0,1f,game.freeFontTypeTool.variSmallFont));
				but.addActor(addString(""+game.dataSave.scoreUnlock[4], 0, game.Y(0), game.X(110),0,1f,game.freeFontTypeTool.variBiggerFont));
			}
			//but.addActor(game.stageHelper.createImageButton2(Asset.stringAicapRegion, Asset.stringAicapRegion, game.X(110), game.Y(37), game.X(0), game.Y(74)));
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub		
					if(Asset.isTestVersion||(!Asset.isTestVersion&&game.dataSave.stateMap[6]==1)){
						game.menuScreen.shopUi.shopData.getDataSave();
						checkNVC();
					_screen.nameMapUi.set(6);
					game.gameScreen.stage = 6;
					_screen.nameMapUi.show();
					if(game.gameScreen.stage == 6)
						Asset.loadAicap();
					Asset.loadNVC(game);
					}
				}
			});
		}
		else if(id == 7){
			but = game.stageHelper.createImageButton2( game.dataSave.stateMap[7]==1?Asset.itemDaoRegion:Asset.itemDaoLockRegion,  game.dataSave.stateMap[7]==1?Asset.itemDaoRegion:Asset.itemDaoLockRegion,game.X(110),game.Y(74), game.X(589), game.Y(490-313));			
			but.addActor(addString("HẢI ĐẢO", 0, game.Y(74), game.X(110),0,1f,game.freeFontTypeTool.variBigFont));
			if(game.dataSave.stateMap[7]!=1){
				but.addActor(addString("Score unlock:", 0, game.Y(45), game.X(110),0,1f,game.freeFontTypeTool.variSmallFont));
				but.addActor(addString(""+game.dataSave.scoreUnlock[5], 0, game.Y(0), game.X(110),0,1f,game.freeFontTypeTool.variBiggerFont));
			}
			//but.addActor(game.stageHelper.createImageButton2(Asset.stringDaoRegion, Asset.stringDaoRegion, game.X(110), game.Y(37), game.X(0), game.Y(74)));
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub		
					if(Asset.isTestVersion||(!Asset.isTestVersion&&game.dataSave.stateMap[7]==1)){
						game.menuScreen.shopUi.shopData.getDataSave();
						checkNVC();
					_screen.nameMapUi.set(7);
					game.gameScreen.stage = 7;
					_screen.nameMapUi.show();
					if(game.gameScreen.stage == 7)
						Asset.loadDao();
					Asset.loadNVC(game);
					}
				}
			});
		}
		else if(id == 8){
			but = game.stageHelper.createImageButton2(Asset.tutRegion,  Asset.blackRegion,game.X(110),game.Y(74), game.X(170), game.Y(490-167));			
			//but.addActor(game.stageHelper.createLabel("TUT", Color.WHITE, game.X(20), game.Y(10)));
			but.addListener(new ChangeListener() {				
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub										
					//if(Asset.isTestVersion||(!Asset.isTestVersion&&game.dataSave.stateMap[8]==1)){
					_screen.nameMapUi.set(8);
					_screen.nameMapUi.show();
					Asset.loadTut();
					//}
				}
			});
		}
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		
	}
	
	public Label addString(String name, int xBegin, int yBegin, int wi, int hi, float scale, BitmapFont font)
	{
		LabelStyle style = new LabelStyle(font, Color.WHITE);
		Label title = new Label(name, style);
		title.setFontScale(scale);
		xBegin = xBegin+(int)((wi - title.getTextBounds().width)/2);
		title.setPosition(xBegin, yBegin);
		return title;
	}
}
