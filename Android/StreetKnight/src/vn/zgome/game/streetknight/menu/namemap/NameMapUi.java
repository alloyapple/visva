package vn.zgome.game.streetknight.menu.namemap;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public class NameMapUi extends IEntityMenu implements IVisible{

	public int id;
	
	public NameMapUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	public Label addString(String name, int xBegin, int yBegin, int wi, int hi, float scale, BitmapFont font)
	{
		LabelStyle style = new LabelStyle(font, Color.WHITE);
		Label title = new Label(name, style);
		title.setFontScale(scale);
		xBegin = xBegin+(int)((wi - title.getTextBounds().width)/2);
		yBegin = yBegin+(int)((hi - title.getTextBounds().height)/2);
		title.setPosition(xBegin, yBegin);
		return title;
	}
	public void refresh()
	{
		int xBegin=0, yBegin=0, wi=game.X(839), hi=game.Y(490);
		xBegin = xBegin+(int)((wi - name.getTextBounds().width)/2);
		yBegin = yBegin+(int)((hi - name.getTextBounds().height)/2);
		name.setPosition(xBegin, yBegin);
	}
	Label name;
	Table container;	
	Image hanoi, halong, saigon,japan, roma,aicap,hoangsa;
	Label tutorial;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		container = game.stageHelper.createTable(0, 0, game.X(838), game.Y(490));
		container.addActor(game.stageHelper.createImage(Asset.blackRegion, 0, 0, game.X(839), game.Y(490)));		
		hanoi = game.stageHelper.createImage(Asset.stringHanoiRegion, game.X(318), game.Y(207), game.X(202), game.Y(75));
		halong = game.stageHelper.createImage(Asset.stringHalongRegion, game.X(318), game.Y(207), game.X(202), game.Y(75));
		saigon = game.stageHelper.createImage(Asset.stringSaigonRegion, game.X(318), game.Y(207), game.X(202), game.Y(75));
		japan = game.stageHelper.createImage(Asset.stringJapanRegion, game.X(318), game.Y(207), game.X(202), game.Y(75));
		roma = game.stageHelper.createImage(Asset.stringRomaRegion, game.X(318), game.Y(207), game.X(202), game.Y(75));
		aicap = game.stageHelper.createImage(Asset.stringAicapRegion, game.X(318), game.Y(207), game.X(202), game.Y(75));
		hoangsa = game.stageHelper.createImage(Asset.stringDaoRegion, game.X(318), game.Y(207), game.X(202), game.Y(75));

		tutorial = game.stageHelper.createLabel("TUTORIAL", Color.WHITE, game.X(350), game.Y(250));
		
//		container.addActor(hanoi);
//		container.addActor(halong);
//		container.addActor(saigon);
//		container.addActor(japan);
//		container.addActor(roma);
//		container.addActor(aicap);
//		container.addActor(hoangsa);
//		container.addActor(tutorial);
		name = addString("", 0, 0, 0, 0, 1f, game.freeFontTypeTool.variBiggerFont);
		container.addActor(name);
		game.stage.addActor(container);				
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
	int tick;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		if(container.isVisible())
		{
			if(Asset.manager.update()){
				if(id==8)
				{
					Asset.loadedTut(game, game.screenInfo);
					game.menuScreen.stop();					
					game.tutScreen.start();
				}
				else{
				if(game.gameScreen.stage == 1)
					Asset.loadedHanoi(game,game.screenInfo);
				else if(game.gameScreen.stage == 2)
					Asset.loadedHalong(game, game.screenInfo);
				else if(game.gameScreen.stage == 3)
					Asset.loadedSaigon(game, game.screenInfo);
				else if(game.gameScreen.stage == 4)
					Asset.loadedJapan(game, game.screenInfo);
				else if(game.gameScreen.stage == 5)
					Asset.loadedRome(game, game.screenInfo);
				else if(game.gameScreen.stage == 6)
					Asset.loadedAicap(game, game.screenInfo);
				else if(game.gameScreen.stage == 7)
					Asset.loadedDao(game, game.screenInfo);
				game.menuScreen.stop();
				game.gameScreen.start();
				}
			}
		}
	}

	@Override
	public void hide () {
		// TODO Auto-generated method stub
		container.setVisible(false);
	}

	public void set(int id)
	{
		this.id = id;
		halong.setVisible(false);
		hanoi.setVisible(false);
		saigon.setVisible(false);
		japan.setVisible(false);
		roma.setVisible(false);
		aicap.setVisible(false);
		hoangsa.setVisible(false);
		tutorial.setVisible(false);
		if(id == 1)
		{
			name.setText("HÀ NỘI");
			hanoi.setVisible(true);
		}
		else if(id == 2)
		{			
			name.setText("HẠ LONG");
			halong.setVisible(true);
		}
		else if(id==3)
		{
			name.setText("SÀI GÒN");
			saigon.setVisible(true);
		}
		else if(id == 4)
		{
			name.setText("JAPAN");
			japan.setVisible(true);
		}
		else if(id == 5)
		{
			name.setText("ROME");
			roma.setVisible(true);
		}
		else if(id == 6)
		{
			name.setText("EGYPT");
			aicap.setVisible(true);
		}
		else if(id == 7)
		{
			name.setText("HẢI ĐẢO");
			hoangsa.setVisible(true);
		}
		else if(id == 8)
		{
			name.setText("TUTORIAL");
			tutorial.setVisible(true);
		}
		refresh();
	}
	
	@Override
	public void show () {
		// TODO Auto-generated method stub
		container.setVisible(true);
	}

}
