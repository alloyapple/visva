package vn.zgome.game.streetknight.menu.language;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class EngLish extends IEntityMenu{

	public EngLish (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	ImageButton but;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		but = game.stageHelper.createButtonLogic(game.X(522), game.Y(170), game.X(157), game.Y(150), new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.dataSave.lang = 1;				
				_screen.languageUi.hide();
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
