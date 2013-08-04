package vn.zgome.game.streetknight.core;

import vn.zgome.game.streetknight.core.graphics.FreeFontTypeTool;
import vn.zgome.game.streetknight.core.graphics.ScreenInfo;
import vn.zgome.game.streetknight.core.graphics.StageHelper;
import vn.zgome.game.streetknight.game.GameScreen;
import vn.zgome.game.streetknight.loading.LoadingScreen;
import vn.zgome.game.streetknight.menu.MenuScreen;
import vn.zgome.game.streetknight.splash.SplashScreen;
import vn.zgome.game.streetknight.tut.TutScreen;
import android.content.Context;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameOS extends Game{

	public ProcessLib processLib;
	public IAPListener iapListener;
	public FacebookListener facebookListener;
	// Draw Tool
	public OrthographicCamera guiCam;
	public SpriteBatch batcher;

	// Skin Tool
	public Skin skin;
	public Stage stage;
	public AndroidFunction android;
	
	// Logic screen
	public ScreenInfo screenInfo;
	public FreeFontTypeTool freeFontTypeTool;
	public StageHelper stageHelper;
	
	public TestScreen testScreen;
	public SplashScreen splashScreen;
	public LoadingScreen loadingScreen;
	public MenuScreen menuScreen;
	public GameScreen gameScreen;
	public TutScreen tutScreen;
	
	public DataSave dataSave;
	
	// Time loop
	public long lastTime;
	public long currentTime;
	public int delayTime;
	private Context context;
	
	public boolean isFistTime;
	
	public GameOS(Context context)
	{		
		this.context = context;
		processLib = new ProcessLib(this);
		
		screenInfo = new ScreenInfo(this);
		freeFontTypeTool = new FreeFontTypeTool(this);
		stageHelper = new StageHelper(this);
		
		testScreen = new TestScreen(this);
		splashScreen = new SplashScreen(this);
		loadingScreen = new LoadingScreen(this);
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		tutScreen = new TutScreen(this);
		dataSave = new DataSave(this,context);
	}
	
	public void create()
	{				
		DrawToolCreate();
		SkinToolCreate();
		AssetsLoadResource();
		StartFirstScreen();
		isFistTime = true;
	}

	public void resume()
	{			
		super.resume();
		isFistTime = true;
	}
	
	public void pause()
	{	
		if(gameScreen.isUpdate)
		{
			if(gameScreen.isPause == false)
			{
				gameScreen.countDownUi.isPlay = false;
				gameScreen.pauseUi.show();
				gameScreen.isPause = true;
			}
		}
		super.pause();
	}
	
	public void render()
	{	
		currentTime = System.currentTimeMillis();
		if(isFistTime)
		{
			lastTime = currentTime;
			isFistTime = false;
		}
		delayTime = (int)(currentTime-lastTime);		
		lastTime = currentTime;
	
		super.render();
		AllScreenUpdate(delayTime);
		AllScreenDraw();
	}
	
	public void dispose()
	{
		batcher.dispose();
		freeFontTypeTool.normalFont.dispose();
		freeFontTypeTool.variFont.dispose();
		freeFontTypeTool.variSmallFont.dispose();
		freeFontTypeTool.variBigFont.dispose();
		freeFontTypeTool.variBiggerFont.dispose();
		Asset.manager.dispose();
		Asset.manager = null;
		super.dispose();
	}	
	
	public void DrawToolCreate ()
	{
		guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		guiCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		batcher = new SpriteBatch();
	}
	
	private void SkinToolCreate () {
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
	}
	
	public void AssetsLoadResource()
	{
		screenInfo.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void StartFirstScreen()
	{
		freeFontTypeTool.loadFont();
		dataSave.getAll();
		//testScreen.start();
		splashScreen.start();
		//loadingScreen.start();
		//menuScreen.start();
		//tutScreen.start();
	}
	public void AllScreenUpdate (int delayTime) 
	{
		testScreen.update1(delayTime);
		splashScreen.update1(delayTime);
		loadingScreen.update1(delayTime);
		menuScreen.update1(delayTime);		
		gameScreen.update1(delayTime);
		tutScreen.update1(delayTime);
	}
	
	public void AllScreenDraw () 
	{
		GLCommon gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);		
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		testScreen.draw1();
		splashScreen.draw1();
		loadingScreen.draw1();
		menuScreen.draw1();
		gameScreen.draw1();
		tutScreen.draw1();
		if(tutScreen.isUpdate){
			DrawStageUi();
			tutScreen.draw2();
		}
		if(!tutScreen.isUpdate)
			DrawStageUi();		
	}
	
	public void DrawStageUi()
	{		
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();		
	}
	
	
	//=======================================
	public int X(int x)
	{
		return (int)screenInfo.getX(x);
	}
	public int Y(int y)
	{
		return (int)screenInfo.getY(y);
	}
	public float X(float x)
	{
		return (int)screenInfo.getX(x);
	}
	public float Y(float y)
	{
		return (int)screenInfo.getY(y);
	}
	
	public void vibrate(int time)
	{		
		if(android!=null)
			android.vibrate(time);
	}
}
