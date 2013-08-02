package vn.zgome.game.streetknight.game.main;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.action.FrameAnimation;
import vn.zgome.game.streetknight.core.graphics.TextureRegionInfo;

public class Rank extends ILive{

	FrameAnimation ani;
	TextureRegionInfo danhhieu;
	public Rank (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		ani = new FrameAnimation();
	}
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		ani.setInfo(0, 7, 300);
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
			{				
				game.batcher.draw(Asset.youare.frames[ani.current].region, x, y+danhhieu.hi, Asset.hit.frames[frame].wi*1.5f, Asset.hit.frames[frame].hi);
				game.batcher.draw(danhhieu.region, x+Asset.hit.frames[frame].wi*1.5f/2-danhhieu.wi/2, 0, danhhieu.wi, danhhieu.hi);
			}
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
	
	public void restart(TextureRegionInfo danhhieu)
	{
		this.danhhieu = danhhieu;
		ani.setInfo(0, 7, 30);
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
					if(ani.current == 7)
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
