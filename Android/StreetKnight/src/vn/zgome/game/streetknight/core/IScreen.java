package vn.zgome.game.streetknight.core;

public abstract class IScreen implements IRun
{
	
	public GameOS game;
	public boolean isUpdate;
	public boolean isDraw;
	
	public void set(boolean isUpdate, boolean isDraw)
	{
		this.isDraw = isDraw;
		this.isUpdate = isUpdate;
	}
	
	public IScreen(GameOS game)
	{
		this.game = game;
	}
	
	public void update1(int delayTime)
	{
		if(isUpdate)
		{
			update(delayTime);
		}
	}
	
	public void draw1()
	{
		if(isDraw)
		{			
			draw();
		}
	}
}
