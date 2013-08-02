package vn.zgome.game.streetknight.core.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class FrameRegionInfo 
{
	public TextureRegionInfo[] frames;
	
	public FrameRegionInfo(TextureAtlas atlas, int num, ScreenInfo screenInfo)
	{
		frames = new TextureRegionInfo[num];

		for(int i=0;i<num;i++)
		{		
			try{
			frames[i] = new TextureRegionInfo(atlas.findRegion(""+(i+1)), screenInfo);			
			}
			catch(Exception ex)
			{
				frames[i] = null;
			}
		}
	}	
}
