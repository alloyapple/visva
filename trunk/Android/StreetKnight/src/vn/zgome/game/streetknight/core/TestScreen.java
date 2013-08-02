package vn.zgome.game.streetknight.core;

import vn.zgome.game.streetknight.core.action.FrameAnimation;

public class TestScreen extends IScreen
{

	public TestScreen (GameOS game) {
		super(game);
		// TODO Auto-generated constructor stub
		test.setInfo(0, 6, 50);
	}

	FrameAnimation test = new FrameAnimation();
	
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		test.update(delayTime);
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		game.batcher.begin();
		game.batcher.draw(Asset.cho_ha_noi.frames[test.current].region,0,0,Asset.cho_ha_noi.frames[test.current].wi,Asset.cho_ha_noi.frames[test.current].hi);
		game.batcher.end();
	}

	@Override
	public void start () {
		// TODO Auto-generated method stub
		set(true, true);
		game.stage.clear();
		game.stage.addActor(game.stageHelper.createLabel("Ghép test đồ họa", false));
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}
}
