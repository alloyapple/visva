package vn.zgome.game.streetknight.menu.info;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class InfoUi extends IEntityMenu implements IVisible{
	
	BackBut back;		
	public InfoUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		back = new BackBut(game, screen);
	}

	Table container;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		container = game.stageHelper.createTable(0, 0, game.X(838), game.Y(490));
		container.addActor(game.stageHelper.createImage(Asset.nen_chung, game.X(0), game.Y(0)));	
		container.addActor(game.stageHelper.createImage(Asset.bgAboutRegion, game.X(101), game.Y(22)));		
		back.start();
		container.addActor(game.stageHelper.createImage(Asset.aboutTextRegion,game.X(0) , game.Y(390-390)));
		container.addActor(back.but);
		game.stage.addActor(container);		
		hide();
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
	}
}
