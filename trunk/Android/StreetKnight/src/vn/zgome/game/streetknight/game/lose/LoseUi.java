package vn.zgome.game.streetknight.game.lose;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.FacebookListener;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.game.IEntityGame;
import vn.zgome.game.streetknight.game.main.Score;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class LoseUi extends IEntityGame implements IVisible{

	public LoseUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	public Table table; 
	Label money, move, kill, score;
	@Override
	public void start () {
		// TODO Auto-generated method stub		
		table = game.stageHelper.createTable(game.X(0), game.Y(0), game.X(839), game.Y(490));	
		table.addActor(game.stageHelper.createImage(Asset.nen_chung, 0, 0));			
		table.addActor(game.stageHelper.createImage(Asset.loseDialogRegion, game.X(125), game.Y(103)));
		table.addActor(game.stageHelper.createImage(Asset.menuRegion, game.X(23), game.Y(490-467)));
		table.addActor(game.stageHelper.createImage(Asset.againRegion, game.X(656), game.Y(490-467)));
		//Log.show(_screen.score.money+"");
		money = game.stageHelper.createLabel(_screen.score.money+"",Color.WHITE, game.X(520), game.Y(490-135));
		move = game.stageHelper.createLabel(_screen.score.move+"",Color.WHITE, game.X(520), game.Y(490-187));
		kill = game.stageHelper.createLabel(_screen.score.kill+"",Color.WHITE, game.X(520), game.Y(490-232));
		score = game.stageHelper.createLabel(_screen.score.score+"",Color.WHITE, game.X(478), game.Y(490-317));
		table.addActor(money);
		table.addActor(move);
		table.addActor(kill);
		table.addActor(score);

		table.addActor(game.stageHelper.createButtonLogic(game.X(664), game.Y(490-461), game.X(813-664), game.Y(461-392), new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				_screen.startNewGame();
				hide();
			}
		}));
		table.addActor(game.stageHelper.createButtonLogic(game.X(31), game.Y(490-461), game.X(813-664), game.Y(461-392), new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				_screen.stop();
				game.menuScreen.start();
				game.menuScreen.languageUi.hide();
//				game.menuScreen.selectMapUi.show();
				hide();
			}
		}));
		ImageButton fb;
		fb = game.stageHelper.createImageButton2(Asset.fb_shared, Asset.fb_shared, game.X(70), game.Y(70), game.X(550), game.Y(35));		
		fb.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				final String link = FacebookListener.LINK;
				final String name = "Street Knight"; //ten cua feed
				final String caption = "Level " + FacebookListener.NAME_STAGE[_screen.stage - 1];
				final int score = _screen.score.score;
				final String descrip = "Trở thành " + Score.achivementHighscore(score) +" với điểm số " +score;
				final String picture = FacebookListener.PICTURE_URL[_screen.stage - 1];
				
				game.facebookListener.onFeedRequest(link, name, caption, descrip, picture);
			}
		});
		table.addActor(fb);
		hide();
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		
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
		
		money.setText(_screen.score.money+"");
		move.setText(_screen.score.move+"");
		kill.setText(_screen.score.kill+"");
		score.setText(_screen.score.score+"");
	}

}
