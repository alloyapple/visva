package vn.zgome.game.streetknight.game.main;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.action.FrameAnimation;

public class Blood extends ILive{

	FrameAnimation ani;
	
	public Blood (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		ani = new FrameAnimation();
	}
	
	public boolean isBlood()
	{
		return true;
	}
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		ani.setInfo(0, 16, 50);
	}
	
	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		if(type>0)
		{
			game.batcher.draw(Asset.blood.frames[ani.current].region, x, y, Asset.blood.frames[frame].wi, Asset.blood.frames[frame].hi);
		}
	}
	public void set(int type, int x, int y)
	{		
		this.type = 1;
		this.x = x;
		this.y = y;
		if(this.type == 1)
		{
			ani.setInfo(0, 7, 50);
		}
		else if(this.type == 2)
		{
			ani.setInfo(8, 15,50);
		}
	}
	int type;
	int x,y;
	int frame;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		if(type == 1)
		{			
			ani.update(delayTime);			
			if(ani.current == 7)
			{
				type = 0;
				isDie = true;
			}
		}
		else if(type == 2)
		{			
			ani.update(delayTime);			
			if(ani.current == 15)
			{
				type = 0;
				isDie = true;
			}
		}
	}

}
