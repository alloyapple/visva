package vn.zgome.game.streetknight.game.pause;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.game.IEntityGame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class PauseUi extends IEntityGame implements IVisible{

	public PauseUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	public Table table;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		table = game.stageHelper.createTable(game.X(0), game.Y(0), game.X(839), game.Y(490));
		table.addActor(game.stageHelper.createImage(Asset.pauseDialogRegion, game.X(280), game.Y(95)));		
		table.addActor(game.stageHelper.createButtonLogic( game.X(280+67), game.Y(95+300-190), game.X(197-67), game.Y(190-153), new ChangeListener() {		
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub								
				_screen.countDownUi.count();
				hide();				
//				_screen.stop();
//				game.menuScreen.start();
//				game.menuScreen.languageUi.hide();
//				game.menuScreen.selectMapUi.show();
				
//				_screen.loseUi.show();
	
//				_screen.buy8Ui.show();
			}
		}));
		table.addActor(game.stageHelper.createButtonLogic( game.X(280+25), game.Y(95+300-257), game.X(244-25), game.Y(257-222), new ChangeListener() {		
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub								
				_screen.stop();
				game.menuScreen.start();
				game.menuScreen.languageUi.hide();			
				hide();
			}
		}));
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
		table.setVisible(false);
	}

	@Override
	public void show () {
		// TODO Auto-generated method stub
		table.setVisible(true);
	}
}
