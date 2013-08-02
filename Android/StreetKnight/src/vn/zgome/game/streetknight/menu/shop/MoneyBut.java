package vn.zgome.game.streetknight.menu.shop;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class MoneyBut extends IEntityMenu
{

	public MoneyBut (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	
	ImageButton butVi, butEn;
	@Override
	public void start () {
		// TODO Auto-generated method stub	
		butVi = game.stageHelper.createImageButton(Asset.moneyButton[0], Asset.moneyButton[0], game.X(385), game.Y(35));
		butVi.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				_screen.shopUi.moneyUi.show();
			}
		});
		butEn = game.stageHelper.createImageButton(Asset.moneyButton[1], Asset.moneyButton[1], game.X(385), game.Y(35));
		butEn.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				_screen.shopUi.moneyUi.show();
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
	
	public void refresh()
	{
		butEn.setVisible(false);
		butVi.setVisible(false);
		if(game.dataSave.lang==0)
		{
			butVi.setVisible(true);
		}
		else
		{
			butEn.setVisible(true);
		}
	}
}
