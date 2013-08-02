package vn.zgome.game.streetknight.game.main;

import java.util.Random;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.action.FrameAnimation;

public class Comment extends ILive{	
	
	FrameAnimation ani;
	float alpha;
	int score;	
	
	public Comment (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		ani = new FrameAnimation();
	}
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		ani.setInfo(0, 23, 300);
	}
	
	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}
	float alphaText=1f;
	int tickAlphaText;
	int xText, yText;
	@Override
	public void draw () {
		// TODO Auto-generated method stub
		if(type>0)
		{
			game.batcher.draw(Asset.comment.frames[ani.current].region, x, y, Asset.comment.frames[frame].wi, Asset.comment.frames[frame].hi);
			if(score > 0 ){
				game.freeFontTypeTool.normalFont.setColor(1f, 1f, 1f, alphaText);
				game.freeFontTypeTool.normalFont.draw(game.batcher, "+"+score, xText, yText);
				game.freeFontTypeTool.normalFont.setColor(1f, 1f, 1f, 1f);
			}
		}
	}
	
	Random rand = new Random();
	
	public void set(int x, int y, int score)
	{		
		this.score = score;
		if(_screen.stage == 4)
		{
			if(_screen.nvc.id == 2)
			{
				this.score+=5;
			}
		}
		if(_screen.stage == 5)
		{
			if(_screen.nvc.id == 3 && _screen.nvc.delta == 0)
			{
				this.score+=5;
			}
		}
		if(_screen.stage == 6)
		{
			if(_screen.nvc.id == 3 && _screen.nvc.delta == 1)
			{
				this.score+=5;
			}
		}
		if(_screen.stage == 7)
		{
			if(_screen.nvc.id == 3 && _screen.nvc.delta == 2)
			{
				this.score+=20;
			}
		}
		this.type = rand.nextInt(4)+1;
		this.x = x;
		this.y = y;
		xText = x;		
		yText = y+game.Y(100);
		if(type == 1)
		{
			ani.setInfo(0, 5, 200);
		}
		else if(type == 2)
		{
			ani.setInfo(6, 11, 200);
		}
		else if(type == 3)
		{
			ani.setInfo(12, 17, 200);
		}
		else if(type == 4)
		{
			ani.setInfo(18, 23, 200);
		}
		alpha = 1f;
	}
	
	public void set(float x, float y, int score)
	{		
		set((int) x, (int)y ,score);
	}
	
	int type;
	int x,y;
	int frame;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		alpha -= 0.1f;
		if(tickAlphaText>=40)
		{			
			tickAlphaText = 0;
			alphaText -= 0.05f;
		}
		else
		{
			tickAlphaText += delayTime;			
		}
		
		if(type == 1)
		{			
			ani.update(delayTime);			
			y+=game.X(2);
			yText += game.X(2);
			if(ani.current == 5)
			{
				type = 0;
				isDie = true;
			}
		}
		else if(type == 2)
		{			
			ani.update(delayTime);			
			y+=game.X(2);
			yText += game.X(2);
			if(ani.current == 11)
			{
				type = 0;
				isDie = true;
			}
		}
		else if(type == 3)
		{			
			ani.update(delayTime);			
			y+=game.X(2);
			yText += game.X(2);
			if(ani.current == 17)
			{
				type = 0;
				isDie = true;
			}
		}
		else if(type == 4)
		{			
			ani.update(delayTime);			
			y+=game.X(2);
			yText += game.X(2);
			if(ani.current == 23)
			{
				type = 0;
				isDie = true;
			}
		}
	}

}
