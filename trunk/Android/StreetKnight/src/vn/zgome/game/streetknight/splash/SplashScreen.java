package vn.zgome.game.streetknight.splash;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.graphics.TextureRegionInfo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class SplashScreen extends IScreen{

	public SplashScreen (GameOS game) {
		super(game);
		// TODO Auto-generated constructor stub		
	}
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		set(true, true);
		game.stage.clear();
		if(Asset.manager == null)
			Asset.manager = new AssetManager();
      Asset.manager.load("data/splash.atlas", TextureAtlas.class);
      // Wait until they are finished loading
      Asset.manager.finishLoading();
      tick = 0; 
      TextureAtlas atlas = Asset.manager.get("data/splash.atlas", TextureAtlas.class);
      game.stage.addActor(game.stageHelper.createImage(new TextureRegionInfo(atlas.findRegion("logoIRIS"),game.screenInfo), 0, 0,game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI));
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		set(false, false);
		Asset.manager.unload("data/splash.atlas");
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub		
	}
	int tick;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		if(tick >= 2000)
		{
			stop();
			game.loadingScreen.start();
		}
		else
		{
			tick+=delayTime;
		}
	}
}
