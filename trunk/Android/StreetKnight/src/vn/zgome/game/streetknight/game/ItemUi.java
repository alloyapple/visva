package vn.zgome.game.streetknight.game;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class ItemUi extends IEntityGame
{	
	public int idItem1;
	public int idItem2;
	public int state = 0;
	int timeRunning;
	int timeReload;
	int tick;
	
	public void reset()
	{
		state = 0;
		timeRunning = 0;
		timeReload = 0;
		tickPlusHP = 0;
		tick = 0;
		if(idItem2 == 4)
		{
			if(game.dataSave.coutHPBonus <=0)
			{
				item2.setVisible(false);
				countHP.setVisible(false);
			}
			else
			{
				item2.setVisible(true);
				countHP.setVisible(true);
			}
		}
	}
	
	public ItemUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	public void load()
	{
		//game.menuScreen.shopUi.shopData.tab1Item[0].isEquipe 
	}

	Table table;
	CheckBox item1;
	Image item2;
	Label time;
	public Label countHP;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		table = game.stageHelper.createTable(game.X(0), game.Y(0), game.X(839), game.Y(490));
		countHP = game.stageHelper.createLabel(game.dataSave.coutHPBonus+"", Color.WHITE, game.X(100), game.Y(490-120));
		idItem1 = -1;
		idItem2 = -1;
		countHP.setVisible(false);
		for(int i =0 ;i<3;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab2Item[i].isEquipe)
			{				
				idItem1 = i;
			}			
		}
		
		for(int i =0 ;i<5;i++)
		{
			if(game.menuScreen.shopUi.shopData.tab3Item[i].isEquipe)
			{				
				idItem2 = i;
			}			
		}
		if(idItem1>=0){
			if(idItem1==0){
				item1 = game.stageHelper.createCheckBox(Asset.snowSLRegion, Asset.snowRegion);
			}
			else if(idItem1==1){
				item1 = game.stageHelper.createCheckBox(Asset.bomSLRegion, Asset.bomRegion);
			}
			else if(idItem1==2){
				item1 = game.stageHelper.createCheckBox(Asset.s10SLRegion, Asset.s10Region);
			}			
			item1.setPosition(game.X(400), game.Y(300));
			time = game.stageHelper.createLabel("", Color.WHITE, game.X(420), game.Y(320));
			item1.setChecked(false);
			item1.addListener(new ChangeListener() {			
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					// TODO Auto-generated method stub
					if(item1.isChecked())
					{		
						if(idItem1==1){
							for(int i=0;i<100;i++)
							{
								if(_screen.enemyManager.enemys[i]!=null){
									if(_screen.enemyManager.enemys[i].isDie == false)
									{					
										_screen.enemyManager.enemys[i].explode();
									}
								}
							}
							state = 1;
							tick = 0;
							timeRunning = 0;
							item1.setDisabled(true);			   	
							time.setText(""+0);
						}		
						else if(idItem1==2)
						{
							_screen.nvc.battu(idItem2==0?15000:10000);
							state = 1;
							tick = 0;
							timeRunning = idItem2==0?15:10;
							item1.setDisabled(true);			   	
							time.setText(""+(idItem2==0?15:10));
						}
						else if(idItem1==0)
						{
							state = 1;
							tick = 0;
							timeRunning = idItem2==0?15:10;
							item1.setDisabled(true);			   	
							time.setText(""+(idItem2==0?15:10));
						}
					}
				}
			});
		}
		//item1 = game.stageHelper.createImageButton(Asset.snowRegion, Asset.snowSLRegion, game.X(100), game.Y(100));
		if(idItem2>=0){
			if(idItem2 == 0)
				item2 = game.stageHelper.createImage(Asset.addTimeItemRegion, game.X(10), game.Y(370));
			else if(idItem2 == 1)
			{
				item2 = game.stageHelper.createImage(Asset.addMoneyRegion, game.X(10), game.Y(370));
			}
			else if(idItem2 == 2)
			{
				item2 = game.stageHelper.createImage(Asset.addHPRegion, game.X(10), game.Y(370));
			}
			else if(idItem2 == 3)
			{
				item2 = game.stageHelper.createImage(Asset.quickReloadRegion, game.X(10), game.Y(370));
			}
			else if(idItem2 == 4)
			{
				item2 = game.stageHelper.createImage(Asset.bonus15Region, game.X(10), game.Y(370));
				countHP.setVisible(true);
			}
		}
		if(idItem1>=0)
			table.addActor(item1);
		if(idItem2>=0)
			table.addActor(item2);
		if(idItem1>=0)
			table.addActor(time);
		//table.addActor(countHP);
		game.stage.addActor(table);
		
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		
	}
	int tickPlusHP;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		
		if(idItem2 == 2)
		{			
			if(tickPlusHP>=40000)
			{
				tickPlusHP = 0;
				_screen.score.hp++;
			}
			else
			{
				tickPlusHP+=delayTime;
			}
		}
		
		if(state == 1)
		{
			if(tick>=1000)
			{				
				tick = 0;
				timeRunning --;
				if(timeRunning <=0)
				{
					timeRunning = 0;
					state = 2;
					if(idItem2==3)
					{
						timeReload = 30;
						time.setText("30");
					}
					else{
					timeReload = 60;
					time.setText("60");					
					}
				}
				time.setText(""+timeRunning);
			}
			else
			{
				tick+=delayTime;
			}
		}
		else if(state == 2)
		{
			if(tick>=1000)
			{				
				tick = 0;
				timeReload --;
				if(timeReload <=0)
				{
					timeReload = 0;
					state = 0;				
					time.setText("");
					item1.setDisabled(false);
					item1.setChecked(false);
					return;
				}
				time.setText(""+timeReload);
			}
			else
			{
				tick+=delayTime;
			}
		}
	}
}
