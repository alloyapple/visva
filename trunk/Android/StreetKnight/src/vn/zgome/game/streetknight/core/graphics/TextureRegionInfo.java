package vn.zgome.game.streetknight.core.graphics;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionInfo 
{
	public TextureRegion region;
	public int wi, hi;
	
	public TextureRegionInfo(TextureRegion region, ScreenInfo screenInfo)
	{		
		this.region = region;
		wi = (int)screenInfo.getX(region.getRegionWidth());
		hi = (int)screenInfo.getY(region.getRegionHeight());
	}	
}
