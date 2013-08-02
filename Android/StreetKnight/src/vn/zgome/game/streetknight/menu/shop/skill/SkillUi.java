package vn.zgome.game.streetknight.menu.shop.skill;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class SkillUi extends IEntityMenu implements IVisible{

	SnowButton snowButton;
	BomButton bomButton;
   s10Button s10Button;
	
	public void refresh()
	{	
		snowButton.refresh();
		bomButton.refresh();
		s10Button.refresh();
	}
	
	public SkillUi (GameOS game, IScreen screen) 
	{
		super(game, screen);
		// TODO Auto-generated constructor stub	
		snowButton = new SnowButton(game, screen);
	   bomButton = new BomButton(game, screen);
		s10Button = new s10Button(game, screen);
	}
	
	public Table table;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub		
		table = game.stageHelper.createTable(game.X(86), game.Y(490-402), game.X(522-86), game.Y(402-160));
		snowButton.start();		
		table.addActor(snowButton.equip);
		table.addActor(snowButton.select);
		table.addActor(snowButton.normal);
		table.addActor(snowButton.but);
		
		bomButton.start();		
		table.addActor(bomButton.equip);
		table.addActor(bomButton.select);
		table.addActor(bomButton.normal);
		table.addActor(bomButton.but);
		
		s10Button.start();		
		table.addActor(s10Button.equip);
		table.addActor(s10Button.select);
		table.addActor(s10Button.normal);
		table.addActor(s10Button.but);
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
