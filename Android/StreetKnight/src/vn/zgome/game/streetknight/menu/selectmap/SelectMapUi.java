package vn.zgome.game.streetknight.menu.selectmap;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;
import vn.zgome.game.streetknight.menu.selectmap.BackBut;

import com.badlogic.gdx.scenes.scene2d.ui.Table;


public class SelectMapUi extends IEntityMenu implements IVisible{

	BackBut back;	
	Item tut, hanoi,halong,saigon, japan,roma,aicap,hoangsa;
	
	public SelectMapUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		back = new BackBut(game, screen);
		tut = new Item(game, screen,8);
		hanoi = new Item(game, screen,1);
		halong = new Item(game, screen,2);
		saigon = new Item(game, screen,3);
		japan = new Item(game, screen,4);
		roma = new Item(game, screen,5);
		aicap = new Item(game, screen,6);
		hoangsa = new Item(game, screen,7);
	}
	
	Table container;	
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		container = game.stageHelper.createTable(0, 0, game.X(838), game.Y(490));
		container.addActor(game.stageHelper.createImage(Asset.nen_chung, game.X(0), game.Y(0)));	
		container.addActor(game.stageHelper.createImage(Asset.bgAboutRegion, game.X(101), game.Y(22)));		
		back.start();
		hanoi.start();
		container.addActor(hanoi.but);
		halong.start();
		container.addActor(halong.but);
		saigon.start();
		container.addActor(saigon.but);
		japan.start();
		container.addActor(japan.but);
		roma.start();
		container.addActor(roma.but);
		aicap.start();
		container.addActor(aicap.but);
		hoangsa.start();
		container.addActor(hoangsa.but);
		container.addActor(back.but);
		game.stage.addActor(container);	
		tut.start();
		container.addActor(tut.but);
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
		container.setVisible(false);
	}

	@Override
	public void show () {
		// TODO Auto-generated method stub
		container.setVisible(true);
	}

}
