package vn.zgome.game.streetknight.menu.shop;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class BonusBut extends IEntityMenu
{

	public BonusBut (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	ImageButton but;
	@Override
	public void start () {
		// TODO Auto-generated method stub	
		but = game.stageHelper.createButtonLogic(game.X(368), game.Y(490-132), game.X(490-368), game.Y(100), new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub				
				_screen.shopUi.shopData.tab = 3;
				_screen.shopUi.shopData.item = 0;
				_screen.shopUi.refresh();
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
