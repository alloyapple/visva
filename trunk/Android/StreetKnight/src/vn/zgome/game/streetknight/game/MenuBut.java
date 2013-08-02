package vn.zgome.game.streetknight.game;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class MenuBut extends IEntityGame
{

	public MenuBut (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	ImageButton menu;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		menu = game.stageHelper.createImageButton(Asset.menuButRegion, Asset.menuButRegion, game.X(710), game.Y(450));
		menu.addListener(new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				_screen.countDownUi.isPlay = false;
				_screen.pauseUi.show();
				_screen.isPause = true;
			}
		});
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
