package vn.zgome.game.streetknight.menu.setting;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class BackBut extends IEntityMenu{

	public BackBut (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	ImageButton but;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		but = game.stageHelper.createButtonLogic(game.X(337), game.Y(490-467), game.X(129), game.Y(65), new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub				
				_screen.settingUi.hide();
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
