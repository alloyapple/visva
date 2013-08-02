package vn.zgome.game.streetknight.menu.shop.bonus;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class TimeButton extends IEntityMenu{

	public TimeButton (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	public void refresh()
	{
		normal.setVisible(false);
		select.setVisible(false);
		equip.setVisible(false);		
		boolean isSelect = false;
		if(_screen.shopUi.shopData.tab == 3 && _screen.shopUi.shopData.item == 1)
			isSelect = true;		
		if(_screen.shopUi.shopData.tab3Item[0].isBuy)
		{
			if(_screen.shopUi.shopData.tab3Item[0].isEquipe)
			{				
				if(isSelect)
				{					
					equip.setVisible(true);
				}
				else
				{					
					equip.setVisible(true);
				}
			}
			else
			{	
				if(isSelect)
				{					
					select.setVisible(true);
				}
				else
				{					
					normal.setVisible(true);
				}
			}
		}
		else
		{		
			if(isSelect)
			{				
				select.setVisible(true);
			}
			else
			{				
				normal.setVisible(true);
			}
		}		
	}
	ImageButton but;
	Image normal;
	Image select;
	Image equip;
	@Override
	public void start () 
	{		
		// TODO Auto-generated method stub					
		but = game.stageHelper.createButtonLogic(game.X(98-82), game.Y(139), game.X(100), game.Y(80), new ChangeListener() {		
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				_screen.shopUi.shopData.tab = 3;
				_screen.shopUi.shopData.item = 1;
				_screen.shopUi.refresh();				
			}
		});		
		normal = game.stageHelper.createImage(Asset.addTimeItemRegion, game.X(98-82), game.Y(139));
		select = game.stageHelper.createImage(Asset.addTimeItemSLRegion, game.X(98-82), game.Y(139));
	   equip = game.stageHelper.createImage(Asset.addTimeItemEQRegion, game.X(98-82-12), game.Y(139));
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
