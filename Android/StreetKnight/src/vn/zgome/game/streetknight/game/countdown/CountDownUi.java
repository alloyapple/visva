package vn.zgome.game.streetknight.game.countdown;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.game.IEntityGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class CountDownUi extends IEntityGame implements IVisible{

	public CountDownUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	public boolean isPlay;
	public Table table;
	Label label;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		table = game.stageHelper.createTable(game.X(0), game.Y(0), game.X(838), game.Y(490));		
		label = game.stageHelper.createLabel("1", Color.RED, game.X(100), game.Y(100));
		int xCenter = (int)((game.X(838) - label.getTextBounds().width)/2);
		int yCenter = (int)((game.Y(490) - label.getTextBounds().height)/2);
		label.setPosition(xCenter, yCenter);
		table.addActor(label);
		hide();
	}

	public void count()
	{
		label.setText("3");
		tick =0;
		count = 3;
		isPlay = true;
		show();
	}
	
	@Override
	public void stop () {
		// TODO Auto-generated method stub		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub		
	}
   int tick, count;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub		
		if(isPlay){
		if(tick>=1000)
		{			
			tick = 0;
			count --;
			if(count == -1)
			{
				_screen.isPause = false;				
				hide();
				return;
			}
			label.setText(""+count);
			
		}
		else
		{
			tick += delayTime;
		}
		}
	}

	@Override
	public void hide () {
		// TODO Auto-generated method stub
		table.setVisible(false);
	}

	@Override
	public void show () {
		// TODO Auto-generated method stub
		table.setVisible(true);
	}

}
