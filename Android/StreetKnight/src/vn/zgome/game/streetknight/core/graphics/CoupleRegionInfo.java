package vn.zgome.game.streetknight.core.graphics;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class CoupleRegionInfo 
{
	public TextureRegionInfo on;
	public TextureRegionInfo off;
	
	public CoupleRegionInfo(TextureAtlas atlas, String name, ScreenInfo screenInfo)
	{		
		on = new TextureRegionInfo(atlas.findRegion(name+"1"), screenInfo);
		off = new TextureRegionInfo(atlas.findRegion(name+"2"), screenInfo);
	}
}
