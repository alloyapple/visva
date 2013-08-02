package vn.zgome.game.streetknight.menu.shop.bonus;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class BonusUi extends IEntityMenu implements IVisible{

	TimeButton timeButton;
	AddMoneyButton addMoneyButton;
   AddBloodButton addBloodButton;
	QuickReloadButton quickReloadButton;
	Plus15HP plus15hp;
   
	public void refresh()
	{	
		timeButton.refresh();
		addBloodButton.refresh();
		addMoneyButton.refresh();
		quickReloadButton.refresh();
		plus15hp.refresh();
	}
	
	public BonusUi (GameOS game, IScreen screen) 
	{
		super(game, screen);
		// TODO Auto-generated constructor stub	
		timeButton = new TimeButton(game, screen);
	   addMoneyButton = new AddMoneyButton(game, screen);
		addBloodButton = new AddBloodButton(game, screen);
		quickReloadButton = new QuickReloadButton(game, screen);
		plus15hp = new Plus15HP(game, screen);
	}
	
	public Table table;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub		
		table = game.stageHelper.createTable(game.X(86), game.Y(490-402), game.X(522-86), game.Y(402-160));

		timeButton.start();		
		table.addActor(timeButton.equip);
		table.addActor(timeButton.select);
		table.addActor(timeButton.normal);
		table.addActor(timeButton.but);
		
		addMoneyButton.start();		
		table.addActor(addMoneyButton.equip);
		table.addActor(addMoneyButton.select);
		table.addActor(addMoneyButton.normal);
		table.addActor(addMoneyButton.but);
		
		addBloodButton.start();		
		table.addActor(addBloodButton.equip);
		table.addActor(addBloodButton.select);
		table.addActor(addBloodButton.normal);
		table.addActor(addBloodButton.but);
		
		quickReloadButton.start();		
		table.addActor(quickReloadButton.equip);
		table.addActor(quickReloadButton.select);
		table.addActor(quickReloadButton.normal);
		table.addActor(quickReloadButton.but);
		
		plus15hp.start();		
		table.addActor(plus15hp.equip);
		table.addActor(plus15hp.select);
		table.addActor(plus15hp.normal);
		table.addActor(plus15hp.but);
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

	@Override
	public void hide () {
		// TODO Auto-generated method stub
		table.setVisible(false);
	}

	@Override
	public void show () {
		// TODO Auto-generated method stub
		table.setVisible(true);
	}

}
