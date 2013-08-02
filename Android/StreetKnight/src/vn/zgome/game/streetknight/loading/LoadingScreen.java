package vn.zgome.game.streetknight.loading;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.graphics.TextureRegionInfo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * @author Mats Svensson
 */
public class LoadingScreen extends IScreen{
	
    public LoadingScreen(GameOS game) {   	 
        super(game);
    }
 
    Label percentPro;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		set(true, true);
		
		game.stage.clear();
      
      Asset.manager = new AssetManager();
      Asset.manager.load("data/loading.atlas", TextureAtlas.class);
      // Wait until they are finished loading
      Asset.manager.finishLoading();
      
      TextureAtlas atlas = Asset.manager.get("data/loading.atlas", TextureAtlas.class);
      game.stage.addActor(game.stageHelper.createImage(new TextureRegionInfo(atlas.findRegion("loading"),game.screenInfo), 0, 0));
      percentPro = game.stageHelper.createLabel("", Color.BLACK,game.X(732), game.Y(490-215));
      game.stage.addActor(percentPro);    
      // Initialize the stage where we will place everything
      Asset.load();
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
      // Dispose the loading assets as we no longer need them
		set(false, false);
      Asset.manager.unload("data/loading.atlas");
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
      if (Asset.manager.update()) {
      	Asset.loaded(game.screenInfo);
      	game.loadingScreen.stop();
      	game.menuScreen.start();
      	//game.tutScreen.start();
      }
      percentPro.setText((int)(Asset.manager.getProgress()*100)+"%");
      // Interpolate the percentage to make it more smooth 
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub		
	}
}
