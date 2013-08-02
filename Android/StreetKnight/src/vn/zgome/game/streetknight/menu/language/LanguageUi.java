package vn.zgome.game.streetknight.menu.language;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class LanguageUi extends IEntityMenu implements IVisible{

	public LanguageUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		vie = new VietNam(game, screen);
		eng = new EngLish(game, screen);
	}
	Table container;
	VietNam vie;
	EngLish eng;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		container = game.stageHelper.createTable(0, 0, game.X(838), game.Y(490));
		container.addActor(game.stageHelper.createImage(Asset.bgLanguageRegion, 0, 0,(int)game.X(839),(int)game.Y(490)));
		container.addActor(game.stageHelper.createImage(Asset.languageRegion, game.X(144), game.Y(170)));							
		vie.start();
		container.addActor(vie.but);
		eng.start();
		container.addActor(eng.but);
		game.stage.addActor(container);		
		show();
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
