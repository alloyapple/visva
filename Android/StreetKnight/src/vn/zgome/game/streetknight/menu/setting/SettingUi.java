package vn.zgome.game.streetknight.menu.setting;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingUi extends IEntityMenu implements IVisible{

	BackBut back;	
	
	public SettingUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		back = new BackBut(game, screen);
	}

	Table container;
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		container = game.stageHelper.createTable(0, 0, game.X(838), game.Y(490));
		container.addActor(game.stageHelper.createImage(Asset.nen_chung, 0, 0));	
		container.addActor(game.stageHelper.createImage(Asset.bgSettingRegion, game.X(279), game.Y(24)));		
		container.addActor(game.stageHelper.createImage(Asset.vibrate, game.X(326), game.Y(490-189)));		
		container.addActor(game.stageHelper.createImage(Asset.music, game.X(345), game.Y(490-298)));		
		CheckBox sound = game.stageHelper.createCheckBox(Asset.onRegion, Asset.offRegion);
		sound.setPosition(game.X(366), game.Y(490-246));
		sound.setChecked(game.dataSave.vibrate);
		sound.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.dataSave.vibrate = !game.dataSave.vibrate;
				game.dataSave.saveAll();
			}
		});
		CheckBox viber = game.stageHelper.createCheckBox(Asset.onRegion, Asset.offRegion);
		viber.setPosition(game.X(366), game.Y(490-352));
		viber.setChecked(game.dataSave.sound);
		viber.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.dataSave.sound = !game.dataSave.sound;
				game.dataSave.saveAll();
			}
		});
		back.start();
		container.addActor(viber);
		container.addActor(sound);
		container.addActor(back.but);
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
