package vn.zgome.game.streetknight.core.graphics;

import vn.zgome.game.streetknight.core.GameOS;

public class ScreenInfo {
	
	public int STANDAR_WI = 838;
	public int STANDAR_HI = 490;
	
	public int SCREEN_WI;
	public int SCREEN_HI;
	
	public float scaleX = 1f;
	public float scaleY = 1f;
	
	public void set(int wi, int hi)
	{
		this.SCREEN_HI = hi;
		this.SCREEN_WI = wi;
		
		scaleX = (float)((float)SCREEN_WI/(float)STANDAR_WI);
		scaleY = (float)((float)SCREEN_HI/(float)STANDAR_HI);
	}
	
	GameOS game;
	
	public ScreenInfo(GameOS game)
	{
		this.game = game;
	}
	
	public float getX(float x)
	{		
		return scaleX*x;
	}
	
	public float getY(float y)
	{
		return scaleY*y;
	}
	
	public float portX(int x)
	{
		return x/scaleX;
	}
	
	public float portY(int y)
	{
		return y/scaleY;
	}
}
