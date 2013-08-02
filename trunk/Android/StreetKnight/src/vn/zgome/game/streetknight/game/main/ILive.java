package vn.zgome.game.streetknight.game.main;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.game.IEntityGame;

public abstract class ILive extends IEntityGame{
	
	public static int TIME_STEP_FLYING = 80;	
	public static int TIME_STEP_AND_FPS_ANIMAL = 80;
	public boolean isDie;
	
	public ILive (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	public void explode(){}
	
	public boolean isBlood()
	{
		return false;
	}
	
	public void setDelta(int deltaX, int deltaY)
	{
	}
	public void set(int deltaX, int deltaY)
	{
	}
	
	public void vibration(boolean isShort)
	{
		if(game.dataSave.vibrate)
		{
			if(game.android!=null)
			{
				game.android.vibrate(isShort?500:1000);
			}
		}
	}
}
