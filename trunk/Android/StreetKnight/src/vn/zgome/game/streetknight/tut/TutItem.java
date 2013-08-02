package vn.zgome.game.streetknight.tut;

import vn.zgome.game.streetknight.core.graphics.TextureRegionInfo;

public class TutItem 
{
	TextureRegionInfo region;
	int x, y;
	int rotate;
	boolean flip;
	float scale;	
	
	public TutItem(TextureRegionInfo region, int x, int y, int rotate, boolean isFlip, float scale)
	{
		this.region = region;
		this.x = x;
		this.y = y;		
		this.rotate = rotate;
		this.flip = isFlip;
		this.scale = scale;
	}
}
