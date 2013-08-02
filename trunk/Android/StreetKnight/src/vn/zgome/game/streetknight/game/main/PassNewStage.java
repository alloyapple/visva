package vn.zgome.game.streetknight.game.main;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.action.FrameAnimation;

public class PassNewStage extends ILive{

	FrameAnimation ani;
	
	public PassNewStage (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		ani = new FrameAnimation();
	}
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		ani.setInfo(game.dataSave.lang==0?0:8, game.dataSave.lang==0?7:15, 300);
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
			if(!isDie)
				game.batcher.draw(Asset.pass.frames[ani.current].region, x, y, Asset.pass.frames[frame].wi*0.5f, Asset.pass.frames[frame].hi*0.5f);
			//game.freeFontTypeTool.normalFont.draw(game.batcher, "+x", x, y);
		}
	}
	public void set(int type, int x, int y)
	{		
		this.type = type;
		this.x = x;
		this.y = y;	
	}

	int type=1;
	int x,y;
	int frame;
	
	public void restart()
	{
		ani.setInfo(game.dataSave.lang==0?0:8, game.dataSave.lang==0?7:15, 30);
		isDie = false;
		state = 1;
	}
	
	public void reset()
	{
		isDie = true;
	}
	
	int state = 1;
	int tickDie;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		if(type == 1)
		{			
			if(!isDie){
				if(state == 1){
					ani.update(delayTime);	
					if(ani.current == 7||ani.current==15)
					{			
						state = 2;
						tickDie = 0;
					}
				}
				else if(state == 2)
				{						
					tickDie+=delayTime;
					if(tickDie>=2000)
					{
						isDie = true;
					}
				}
			}
		}
	}
}
