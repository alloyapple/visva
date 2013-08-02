package vn.zgome.game.streetknight.game.lose;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.game.IEntityGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class Buy8Ui extends IEntityGame implements IVisible{

	public Buy8Ui (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	public Table table;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		table = game.stageHelper.createTable(game.X(0), game.Y(0), game.X(839), game.Y(490));
		table.addActor(game.stageHelper.createImage(Asset.buy8Dialog, game.X(200), game.Y(150)));
		table.addActor(addString("Bạn có muốn mua 05 máu", game.X(200), game.Y(320), game.X(450), game.Y(0), 1f, game.freeFontTypeTool.variBigFont));
		table.addActor(addString("và tiếp tục chơi?", game.X(200), game.Y(285), game.X(450), game.Y(0), 1f, game.freeFontTypeTool.variBigFont));
		table.addActor(addString("Có", game.X(210), game.Y(170), game.X(122), game.Y(0), 1f, game.freeFontTypeTool.variBigFont));
		table.addActor(addString("Không", game.X(510), game.Y(170), game.X(122), game.Y(0), 1f, game.freeFontTypeTool.variBigFont));

		table.addActor(game.stageHelper.createButtonLogic(game.X(215), game.Y(490-330), game.X(122), game.Y(64), new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				hide();
				if(game.iapListener!=null)
					game.iapListener.callSmsAuto();
			}
		}));

		table.addActor(game.stageHelper.createButtonLogic(game.X(507), game.Y(490-330), game.X(122), game.Y(64), new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				hide();
				game.gameScreen.loseUi.show();
				Asset.nen.stop();
				Asset.begin.stop();
			}
		}));
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
	}
	
	public Label addString(String name, int xBegin, int yBegin, int wi, int hi, float scale, BitmapFont font)
	{
		LabelStyle style = new LabelStyle(font, Color.WHITE);
		Label title = new Label(name, style);
		title.setFontScale(scale);
		xBegin = xBegin+(int)((wi - title.getTextBounds().width)/2);
		title.setPosition(xBegin, yBegin);
		return title;
	}
}
