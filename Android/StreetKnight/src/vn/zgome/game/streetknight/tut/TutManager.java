package vn.zgome.game.streetknight.tut;


import java.util.Vector;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IEntity;
import vn.zgome.game.streetknight.core.IScreen;

public class TutManager extends IEntity
{
	public TutManager (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	Vector<TutItem> items = new Vector<TutItem>();

	@Override
	public void start () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		for (TutItem item : items) {
			if(item.flip)
			{
			   //game.batcher.draw(item.region.region, item.x, item.y, 0, 0, item.region.wi, item.region.hi, 1, 1, item.rotate);
				game.batcher.draw(item.region.region.getTexture(),  item.x,item.y,item.region.wi*item.scale, item.region.hi*item.scale,item.region.region.getRegionX() , item.region.region.getRegionY(), item.region.region.getRegionWidth(), item.region.region.getRegionHeight(), true, false);
				//game.batcher.draw(item.region.region, item.x, item.y, item.region.wi, item.region.hi);
			}
			else{
				if(item.rotate!=0)
				   game.batcher.draw(item.region.region, item.x, item.y, 0, 0, item.region.wi*item.scale, item.region.hi*item.scale, 1, 1, item.rotate);
				else
					game.batcher.draw(item.region.region, item.x, item.y, item.region.wi*item.scale, item.region.hi*item.scale);
			}
		}
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub		
	}	
	
}
