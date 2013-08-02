package vn.zgome.game.streetknight.tut;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class TutScreen extends IScreen{

	TutManager tutManager;

	public TutScreen (GameOS game) {
		super(game);
		// TODO Auto-generated constructor stub
		tutManager = new TutManager(game, this);
	}
	
	int current = 1;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		set(true, true);
		current = 1;
		game.stage.clear();
		ImageButton next,prev;
		next = game.stageHelper.createImageButton(Asset.next, Asset.next, game.X(614), game.Y(490-88));
		prev = game.stageHelper.createImageButton(Asset.prev, Asset.prev, game.X(65), game.Y(490-88));
		next.addListener(new ChangeListener() {
		
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				current++;
				if(current>16)
				{
					game.tutScreen.stop();
					game.menuScreen.start();
					game.menuScreen.languageUi.hide();
					game.menuScreen.selectMapUi.show();
				}
				else
				{
					refreshView(current);
				}
			}
		});
		prev.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				current--;
				if(current<0)
				{	
					game.tutScreen.stop();
					game.menuScreen.start();
					game.menuScreen.languageUi.hide();
				}
				else
				{								
					refreshView(current);
				}
			}
		});
		game.stage.addActor(prev);
		game.stage.addActor(next);
		refreshView(current);
		Gdx.input.setInputProcessor(game.stage);
	}

	public void refreshView(int current)
	{		
		switch (current) {
		case 1:		
			stage1();
			break;
		case 2:	
			stage2();
			break;
		case 3:	
			stage3();
			break;
		case 4:	
			stage4();
			break;
		case 5:	
			stage5();
			break;
		case 6:	
			stage6();
			break;
		case 7:	
			stage7();
			break;
		case 8:	
			stage8();
			break;
		case 9:	
			stage9();
			break;
		case 10:
			stage10();
			break;
		case 11:	
			stage11();
			break;
		case 12:	
			stage12();
			break;
		case 13:	
			stage13();
			break;
		case 14:	
			stage14();
			break;
		case 15:	
			stage15();
			break;
		case 16:
			stage16();
			break;
		default:
			break;
		}
	}
	
	public void stage1()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.hocchiendau[game.dataSave.lang], game.X(220), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[0], game.X(300), game.Y(490-422),0,false,1f));
	}
	
	public void stage2()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.tancong6huong[game.dataSave.lang], game.X(151), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[0], game.X(300), game.Y(490-422),0,false,1f));
		tutManager.items.add(new TutItem(Asset.dau_gau_ha_noi1.frames[0], game.X(30), game.Y(490-430),0,true,1f));
		tutManager.items.add(new TutItem(Asset.cho_ha_noi.frames[0], game.X(640), game.Y(490-428),0,true,1f));
		tutManager.items.add(new TutItem(Asset.leftHere, game.X(656), game.Y(490-130),30,false,1f));
		tutManager.items.add(new TutItem(Asset.chai_bia.frames[1], game.X(648), game.Y(490-324),0,false,1f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[1], game.X(94), game.Y(490-122),0,false,0.5f));
		tutManager.items.add(new TutItem(Asset.rightHere, game.X(221), game.Y(490-330),0,false,1f));
		tutManager.items.add(new TutItem(Asset.rightHere, game.X(221), game.Y(490-431),0,false,1f));
		tutManager.items.add(new TutItem(Asset.leftHere, game.X(538), game.Y(490-328),0,false,1f));
		tutManager.items.add(new TutItem(Asset.leftHere, game.X(538), game.Y(490-431),0,false,1f));
		tutManager.items.add(new TutItem(Asset.rightHere, game.X(105), game.Y(490-100),-30,false,1f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[1], game.X(674), game.Y(490-122),0,true,0.5f));
	}
	
	public void stage3()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.chamvaomanhinh[game.dataSave.lang], game.X(197), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[1], game.X(359-80), game.Y(490-437),0,false,1f));
		tutManager.items.add(new TutItem(Asset.chai_bia.frames[9], game.X(455), game.Y(490-392),0,false,1f));
		tutManager.items.add(new TutItem(Asset.midAttack, game.X(641), game.Y(490-353),0,false,1f));
	}
	
	public void stage4()
	{
		tutManager.items.removeAllElements();
		//tutManager.items.add(new TutItem(Asset.chamvaomanhinh, game.X(197), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[3], game.X(359-80), game.Y(490-437),0,false,1f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(400), game.Y(490-370),0,false,0.5f));
		tutManager.items.add(new TutItem(Asset.hiAttack, game.X(642), game.Y(490-201),0,false,1f));
	}
	
	public void stage5()
	{
		tutManager.items.removeAllElements();
		//tutManager.items.add(new TutItem(Asset.chamvaomanhinh, game.X(197), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[4], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.cho_ha_noi.frames[4], game.X(450), game.Y(490-431),0,true,1f));
		tutManager.items.add(new TutItem(Asset.lowAttack, game.X(642), game.Y(0),0,false,1f));
	}
	
	public void stage6()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.hoccombo[game.dataSave.lang], game.X(261), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[7], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.dau_gau_ha_noi1.frames[4], game.X(200), game.Y(490-429),0,true,1f));
		tutManager.items.add(new TutItem(Asset.chai_bia.frames[9], game.X(420), game.Y(490-400),0,false,1f));
		tutManager.items.add(new TutItem(Asset.midAttack, game.X(641), game.Y(490-353),0,false,1f));
		tutManager.items.add(new TutItem(Asset.midAttack2, game.X(94), game.Y(490-353),0,false,1f));
	}
	
	public void stage7()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.cham2diem[game.dataSave.lang], game.X(190), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[8], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.cho_ha_noi.frames[4], game.X(500), game.Y(490-434),0,true,1f));
		tutManager.items.add(new TutItem(Asset.cho_ha_noi.frames[4], game.X(240), game.Y(490-434),0,false,1f));
		tutManager.items.add(new TutItem(Asset.lowAttack, game.X(641), game.Y(0),0,false,1f));
		tutManager.items.add(new TutItem(Asset.lowAttack2, game.X(94), game.Y(0),0,false,1f));
	}
	
	public void stage8()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.tieudiet2[game.dataSave.lang], game.X(190), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[5], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(400), game.Y(490-370),0,false,0.5f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(230), game.Y(490-370),0,true,0.5f));
		tutManager.items.add(new TutItem(Asset.hiAttack, game.X(641), game.Y(490-201),0,false,1f));
		tutManager.items.add(new TutItem(Asset.hiAttack2, game.X(94), game.Y(490-201),0,false,1f));
	}
	
	public void stage9()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.tieudiet2[game.dataSave.lang], game.X(190), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[10], game.X(359-80), game.Y(490-437),0,false,1f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(230), game.Y(490-370),0,true,0.5f));
		//tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(400), game.Y(490-370),0,false,0.5f));
		//tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(230), game.Y(490-370),0,true,0.5f));
		//tutManager.items.add(new TutItem(Asset.hiAttack, game.X(641), game.Y(490-201),0,false,1f));
		tutManager.items.add(new TutItem(Asset.chai_bia.frames[9], game.X(455), game.Y(490-392),0,false,1f));
		tutManager.items.add(new TutItem(Asset.hiAttack2, game.X(94), game.Y(490-201),0,false,1f));
		tutManager.items.add(new TutItem(Asset.midAttack, game.X(641), game.Y(490-353),0,false,1f));
		
	}
	
	public void stage10()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.kethopcombo[game.dataSave.lang], game.X(190), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[6], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(230), game.Y(490-370),0,true,0.5f));
		tutManager.items.add(new TutItem(Asset.hiAttack2, game.X(94), game.Y(490-201),0,false,1f));
		tutManager.items.add(new TutItem(Asset.cho_ha_noi.frames[4], game.X(450), game.Y(490-431),0,true,1f));
		tutManager.items.add(new TutItem(Asset.lowAttack, game.X(642), game.Y(0),0,false,1f));
	}
	
	public void stage11()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.combobanthich[game.dataSave.lang], game.X(190), game.Y(490-172),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[13], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.dau_gau_ha_noi1.frames[4], game.X(200), game.Y(490-429),0,true,1f));		
		tutManager.items.add(new TutItem(Asset.midAttack2, game.X(94), game.Y(490-353),0,false,1f));
		tutManager.items.add(new TutItem(Asset.cho_ha_noi.frames[4], game.X(450), game.Y(490-431),0,true,1f));
		tutManager.items.add(new TutItem(Asset.lowAttack, game.X(642), game.Y(0),0,false,1f));
	}
	
	public void stage12()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.kickkinang[game.dataSave.lang], game.X(190), game.Y(-20),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[0
		                                                         ], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.bomRegion, game.X(387), game.Y(490-194),0,false,1f));
	}
	
	public void stage13()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.chamnvc[game.dataSave.lang], game.X(190), game.Y(-20),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[0], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.bomRegion, game.X(387), game.Y(490-194),0,false,1f));
		tutManager.items.add(new TutItem(Asset.chamday, game.X(370), game.Y(490-406),0,false,1f));
		
	}
	
	public void stage14()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.chamicon[game.dataSave.lang], game.X(190), game.Y(-20),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[0], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.bomRegion, game.X(387), game.Y(490-194),0,false,1f));
		tutManager.items.add(new TutItem(Asset.chamday, game.X(370), game.Y(490-248),0,false,1f));
		
	}
	
	public void stage15()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.kichhoatskill[game.dataSave.lang], game.X(190), game.Y(490-185),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[0], game.X(359-80), game.Y(490-437),0,true,1f));
		tutManager.items.add(new TutItem(Asset.dau_gau_ha_noi1.frames[4], game.X(200-50), game.Y(490-429),0,true,1f));		
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(94), game.Y(490-122-70),0,false,0.5f));
		tutManager.items.add(new TutItem(Asset.coi_gia.frames[9], game.X(674), game.Y(490-122-70),0,true,0.5f));
		tutManager.items.add(new TutItem(Asset.cho_ha_noi.frames[4], game.X(450+50), game.Y(490-431),0,true,1f));
		tutManager.items.add(new TutItem(Asset.chai_bia.frames[9], game.X(455+50), game.Y(490-392),0,false,1f));
	}
	
	public void stage16()
	{
		tutManager.items.removeAllElements();
		tutManager.items.add(new TutItem(Asset.ngonrui[game.dataSave.lang], game.X(120), game.Y(490-185),0,false,1f));
		tutManager.items.add(new TutItem(Asset.nvc_normal.frames[0], game.X(359-80), game.Y(490-437),0,true,1f));
	}
	
	@Override
	public void stop () {
		// TODO Auto-generated method stub
		set(false, false);
		Asset.unloadTut();
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
			game.batcher.begin();
			//if(_screen.stage == 1)		
			{			
				game.batcher.draw(Asset.bgGameHaNoi.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
			}
			game.batcher.end();
	}
	
	public void draw2()
	{
		game.batcher.begin();
		tutManager.draw();		
		game.batcher.end();
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		
	}

}
