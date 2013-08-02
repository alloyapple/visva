package vn.zgome.game.streetknight.menu.shop.weapon;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class WeaponUi extends IEntityMenu implements IVisible{

	NormalButton normalButton;
	SuperManButton superManButton;
	SamuraiButton samuraiButton;
	SaButton saButton;
	AiButton aiButton;
	
	public void refresh()
	{	
		normalButton.refresh();
		samuraiButton.refresh();
		superManButton.refresh();		
		saButton.refresh();
		aiButton.refresh();
	}
	
	public WeaponUi (GameOS game, IScreen screen) 
	{
		super(game, screen);
		// TODO Auto-generated constructor stub	
		normalButton = new NormalButton(game, screen);
		superManButton = new SuperManButton(game, screen);
		samuraiButton = new SamuraiButton(game, screen);
		saButton = new SaButton(game, screen);
		aiButton = new AiButton(game, screen);
	}
	
	public Table table;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub		
		table = game.stageHelper.createTable(game.X(86), game.Y(490-402), game.X(522-86), game.Y(402-160));
		normalButton.start();		
		table.addActor(normalButton.equip);
		table.addActor(normalButton.select);
		table.addActor(normalButton.normal);
		table.addActor(normalButton.but);
		
		samuraiButton.start();		
		table.addActor(samuraiButton.equip);
		table.addActor(samuraiButton.select);
		table.addActor(samuraiButton.normal);
		table.addActor(samuraiButton.but);
		
		superManButton.start();		
		table.addActor(superManButton.equip);
		table.addActor(superManButton.select);
		table.addActor(superManButton.normal);
		table.addActor(superManButton.but);
		
		saButton.start();		
		table.addActor(saButton.equip);
		table.addActor(saButton.select);
		table.addActor(saButton.normal);
		table.addActor(saButton.but);
		
		aiButton.start();		
		table.addActor(aiButton.equip);
		table.addActor(aiButton.select);
		table.addActor(aiButton.normal);
		table.addActor(aiButton.but);
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
