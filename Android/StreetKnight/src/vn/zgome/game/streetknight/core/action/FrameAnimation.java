package vn.zgome.game.streetknight.core.action;

public class FrameAnimation 
{
	int max;
	int min;
	public int current;
	public int TIME_PER_FRAME = 1000;
	public int timeTick;
	
	public void setInfo(int min, int max, int timePerFrame)	
	{		
		this.max = max;
		this.min = min;
		this.TIME_PER_FRAME = timePerFrame;
		timeTick = 0;
		this.current = min;
	}
	
	public void update(int delayTime)
	{		
		if(timeTick>=TIME_PER_FRAME)
		{
			timeTick = 0;
			current++;
			if(current>max)
			{
				current = min;
			}
		}
		else
		{
			timeTick+=delayTime;
		}
	}
}
