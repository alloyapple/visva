package vn.zgome.game.streetknight.menu;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class AboutBut extends IEntityMenu{
	public AboutBut (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void start () {
		// TODO Auto-generated method stub
		game.stage.addActor(game.stageHelper.createImage(Asset.aboutButRegion, game.X(8), game.Y(490-95)));
		game.stage.addActor(game.stageHelper.createButtonLogic(game.X(8), game.Y(490-95), game.X(120), game.Y(100), new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub				
				_screen.infoUi.show();
			}
		}));
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
}
