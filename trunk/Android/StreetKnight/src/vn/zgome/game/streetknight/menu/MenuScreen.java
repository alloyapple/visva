package vn.zgome.game.streetknight.menu;

import vn.zgome.game.streetknight.core.AndroidFunction;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.menu.hiscore.HiScoreUi;
import vn.zgome.game.streetknight.menu.info.InfoUi;
import vn.zgome.game.streetknight.menu.language.LanguageUi;
import vn.zgome.game.streetknight.menu.namemap.NameMapUi;
import vn.zgome.game.streetknight.menu.selectmap.SelectMapUi;
import vn.zgome.game.streetknight.menu.setting.SettingUi;
import vn.zgome.game.streetknight.menu.shop.ShopUi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;


public class MenuScreen extends IScreen implements InputProcessor{

	public OtherUi otherUi;
	public StartBut startBut;
	public ShopBut shopBut;
	public HiScoreBut hiScoreBut;
	public OptionBut optionBut;
	public AboutBut aboutBut;
	public ShopUi shopUi;
	public HiScoreUi hiScoreUi;
	public SettingUi settingUi;
	public InfoUi infoUi;
	public LanguageUi languageUi;
	public SelectMapUi selectMapUi;
	public NameMapUi nameMapUi;
	
	public MenuScreen (GameOS game) {
		super(game);
		// TODO Auto-generated constructor stub
		otherUi = new OtherUi(game, this);
		startBut = new StartBut(game, this);
		shopBut = new ShopBut(game, this);
		hiScoreBut = new HiScoreBut(game, this);
		optionBut = new OptionBut(game, this);
		aboutBut = new AboutBut(game, this);
		shopUi = new ShopUi(game, this);
		hiScoreUi = new HiScoreUi(game, this);
		settingUi = new SettingUi(game, this);
		infoUi = new InfoUi(game, this);
		languageUi = new LanguageUi(game, this);
		selectMapUi = new SelectMapUi(game, this);
		nameMapUi = new NameMapUi(game, this);
	}

	@Override
	public void start () {
		// TODO Auto-generated method stub		
		set(true, true);
		game.stage.clear();
		otherUi.start();
		startBut.start();
		shopBut.start();
		hiScoreBut.start();
		optionBut.start();
		aboutBut.start();
		shopUi.start();
		hiScoreUi.start();
		settingUi.start();
		infoUi.start();
		languageUi.start();
		selectMapUi.start();
		nameMapUi.start();
		//Gdx.input.setInputProcessor(game.stage);
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();		
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(game.stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
		
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		set(false, false);
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub		
		nameMapUi.update(delayTime);
	}

	@Override
	public boolean keyDown (int keycode) {
		// TODO Auto-generated method stub
	   if(keycode == Keys.BACK){
	   	if(game.android!=null){
	   		game.android.showDialog("Bạn có muốn thoát game?",new AndroidFunction.Action() {
				
					public void action () {
						// TODO Auto-generated method stub						
						Gdx.app.exit();
					}
				});
	   	}
      }	        
      return false;	  
	}

	@Override
	public boolean keyUp (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
