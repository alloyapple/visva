package vn.zgome.game.streetknight.menu.shop;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;
import vn.zgome.game.streetknight.menu.shop.bonus.BonusUi;
import vn.zgome.game.streetknight.menu.shop.money.MoneyUi;
import vn.zgome.game.streetknight.menu.shop.skill.SkillUi;
import vn.zgome.game.streetknight.menu.shop.weapon.WeaponUi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class ShopUi extends IEntityMenu implements IVisible{

	public ShopData shopData;	
	BackBut back;	
	WeaponBut weaponBut;
	SkillBut skillBut;
	BonusBut bonusBut;
	WeaponUi weaponUi;
	SkillUi skillUi;
	BonusUi bonusUi;
	AboutItemUi aboutItemUi;
	MoneyBut moneyBut;
	MoneyUi moneyUi;
	
	public ShopUi (GameOS game, IScreen screen) 
	{
		super(game, screen);
		// TODO Auto-generated constructor stub
		back = new BackBut(game, screen);
		shopData = new ShopData(game);
		weaponUi = new WeaponUi(game, screen);
		skillUi = new SkillUi(game, screen);
		bonusUi = new BonusUi(game, screen);
		weaponBut = new WeaponBut(game, screen);
		skillBut = new SkillBut(game, screen);
		bonusBut = new BonusBut(game, screen);
		aboutItemUi = new AboutItemUi(game, screen);
		moneyBut = new MoneyBut(game, screen);
		moneyUi = new MoneyUi(game, screen);
	}

	Table container;
	Label money;
	@Override
	public void start () 
	{
		// TODO Auto-generated method stub
		container = game.stageHelper.createTable(0, 0, game.X(838), game.Y(490));
		container.addActor(game.stageHelper.createImage(Asset.nen_chung, game.X(0), game.Y(0)));	
		container.addActor(game.stageHelper.createImage(Asset.bgShopRegion, game.X(0), game.Y(0)));		
		back.start();
		container.addActor(back.but);
		weaponBut.start();
		container.addActor(weaponBut.but);
		skillBut.start();
		container.addActor(skillBut.but);
		bonusBut.start();
		container.addActor(bonusBut.but);
		weaponUi.start();
		container.addActor(weaponUi.table);
		skillUi.start();
		container.addActor(skillUi.table);
		bonusUi.start();
		container.addActor(bonusUi.table);
		aboutItemUi.start();
		container.addActor(aboutItemUi.table);
		moneyBut.start();
		container.addActor(moneyBut.butVi);
		container.addActor(moneyBut.butEn);
		money = game.stageHelper.createLabel("", Color.YELLOW, game.X(610), game.Y(490-82+10));
		container.addActor(money);
		moneyUi.start();
		container.addActor(moneyUi.table);
		game.stage.addActor(container);				
		hide();
		refresh();
	}

	public void refresh()
	{		
		weaponUi.hide();
		skillUi.hide();
		bonusUi.hide();
		if(shopData.tab == 1)
		{
			weaponUi.show();
			weaponUi.refresh();
		}
		else if(shopData.tab == 2)
		{
			skillUi.show();
			skillUi.refresh();
		}
		else if(shopData.tab == 3)
		{
			bonusUi.show();
			bonusUi.refresh();
		}
		aboutItemUi.refesh();
		money.setText(game.dataSave.money+"");
		int xMoney = game.X(580)+(int)((game.X(744-630) - money.getTextBounds().width)/2);
		money.setX(xMoney);
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
		container.setVisible(false);
	}

	@Override
	public void show () {
		// TODO Auto-generated method stub
		container.setVisible(true);
		shopData.getDataSave();
		refresh();
		moneyBut.refresh();
	}

}
