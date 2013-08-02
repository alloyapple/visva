package vn.zgome.game.streetknight.game;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;

public class Background extends IEntityGame{

	public Background (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
	
		if(_screen.stage == 1)
		{
			game.batcher.draw(Asset.bgGameHaNoi.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
		}
		else if(_screen.stage == 2)
		{
			game.batcher.draw(Asset.bgGameHaLong.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
		}
		else if(_screen.stage == 3)
		{
			game.batcher.draw(Asset.bgGameSaigon.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
		}
		else if(_screen.stage == 4)
		{
			game.batcher.draw(Asset.bgGameJapan.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
		}
		else if(_screen.stage == 5)
		{
			game.batcher.draw(Asset.bgGameRoma.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
		}
		else if(_screen.stage == 6)
		{
			game.batcher.draw(Asset.bgGameAiCap.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
		}
		else if(_screen.stage == 7)
		{
			game.batcher.draw(Asset.bgGameDao.region, 0, 0, game.screenInfo.SCREEN_WI, game.screenInfo.SCREEN_HI);
		}
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		
	}

}
