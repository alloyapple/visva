package vn.zgome.game.streetknight.game;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.game.countdown.CountDownUi;
import vn.zgome.game.streetknight.game.lose.Buy8Ui;
import vn.zgome.game.streetknight.game.lose.LoseUi;
import vn.zgome.game.streetknight.game.main.EnemyManager;
import vn.zgome.game.streetknight.game.main.Hit;
import vn.zgome.game.streetknight.game.main.NVC;
import vn.zgome.game.streetknight.game.main.Rank;
import vn.zgome.game.streetknight.game.main.Score;
import vn.zgome.game.streetknight.game.pause.PauseUi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class GameScreen extends IScreen implements InputProcessor{

	Background background;
	public NVC nvc;
	public Score score;
	public MenuBut menuBut;
	public PauseUi pauseUi;
	public CountDownUi countDownUi;
	public Buy8Ui buy8Ui;
	public LoseUi loseUi;
	public ItemUi itemUi;
	public Hit hit;
	public Rank rank;
	public boolean isPause;
	public int stage=1;
	int yUp, yDown;
	
	public EnemyManager enemyManager;
	
	public GameScreen (GameOS game) {
		super(game);
		// TODO Auto-generated constructor stub
		background = new Background(game, this);
		nvc = new NVC(game, this);
		
		enemyManager = new EnemyManager(game, this);
		score = new Score(game, this);
		menuBut = new MenuBut(game, this);
		pauseUi = new PauseUi(game, this);
		countDownUi = new CountDownUi(game, this);
		loseUi = new LoseUi(game, this);
		buy8Ui = new Buy8Ui(game, this);
		itemUi = new ItemUi(game, this);
		hit = new Hit(game, this);
		rank = new Rank(game, this);
	}
	
	public void startNewGame()
	{
		game.menuScreen.shopUi.shopData.getDataSave();
		enemyManager.reset();
		nvc.reset();
		score.reset();
		hit.reset();	
		rank.reset();
		hit.set(1, game.X(600), game.Y(-40));
		rank.set(1, game.X(0), game.Y(-70));
		isPause = false;
		itemUi.reset();
		lastTouch = 0;
		thisTouch = 0;
		if(game.dataSave.sound){
		Asset.begin.stop();
		Asset.begin.play();
		isWaitBg = true;
		}
		else
		{
			isWaitBg = false;
		}
	}
	boolean isWaitBg;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		set(true, true);
		score.start();
		enemyManager.start();
		game.stage.clear();		
		menuBut.start();
		itemUi.start();
		game.stage.addActor(menuBut.menu);
		pauseUi.start();
		game.stage.addActor(pauseUi.table);
		countDownUi.start();
		game.stage.addActor(countDownUi.table);
		
		loseUi.start();
		game.stage.addActor(loseUi.table);
		buy8Ui.start();
		game.stage.addActor(buy8Ui.table);
		yUp = game.Y(260);
		yDown = game.Y(155);				
		
		startNewGame();
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		if(game.gameScreen.stage == 1)
			Asset.unloadHanoi();
		else if(game.gameScreen.stage == 2)
			Asset.unloadHalong();
		else if(game.gameScreen.stage == 3)
			Asset.unloadSaigon();
		else if(game.gameScreen.stage == 4)
			Asset.unloadJapan();
		else if(game.gameScreen.stage == 5)
			Asset.unloadRome();
		else if(game.gameScreen.stage == 6)
			Asset.unloadAicap();
		else if(game.gameScreen.stage == 7)
			Asset.unloadDao();
		Asset.begin.stop();
		Asset.nen.stop();
		game.dataSave.money = score.money;
		game.dataSave.saveScore(stage,score.score, score.kill, score.move);				
		game.dataSave.saveAll();
		set(false, false);		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		game.batcher.begin();
		background.draw();
		nvc.draw();
		enemyManager.draw();
		score.draw();
		hit.draw();
		rank.draw();
		game.batcher.end();
	}
	@Override
	public void update(int delayTime)
	{
		if(isWaitBg)
		{	
			if(Asset.begin.isPlaying())
			{
				
			}
			else
			{
				isWaitBg = false;
				Asset.nen.play();
			}
		}
		else
		{
		}
		if(isPause == false)
		{
			update2(delayTime);			
		}
		else
		{
			countDownUi.update(delayTime);
		}
	}
	int lastTouch;
	int thisTouch;
	public void update2 (int delayTime) {
		// TODO Auto-generated method stub		
		if(score.hp<=0)
		{	
			//Log.show(score.score+"");
			game.dataSave.money = score.money;
			game.dataSave.saveScore(stage,score.score, score.kill, score.move);				
			game.dataSave.saveAll();
			buy8Ui.show();
			//loseUi.show();			
			isPause = true;
			return;
		}
		itemUi.update(delayTime);
		int[] huong = new int[2];
		int count=0;		
		for (int i = 0; i < 2; i++) {
			if (Gdx.input.isTouched(i) == false) continue;

			boolean isLeft = false;
			boolean isRight = false;
			boolean isDown = false;
			boolean isNormal = false;
			float x = Gdx.input.getX(i);
			float y = Gdx.graphics.getHeight() - Gdx.input.getY(i);
			if(x>=Gdx.graphics.getWidth()/2)
			{
				isRight = true;
				if(y>=yUp)
				{
//					isUp = true;
				}
				else if(y<=yDown)
				{
					isDown = true;
				}
				else
				{
					isNormal = true;
				}				
			}
			else
			{
				isLeft = true;
				if(y>=yUp)
				{
//					isUp = true;
				}
				else if(y<=yDown)
				{
					isDown = true;
				}
				else
				{
					isNormal = true;
				}	
			}
			if(isLeft || isRight)
			{
				if(isLeft)
				{
					if(isDown)
					{
						huong[count] = 6;
					}
					else if(isNormal)
					{						
						huong[count] = 5;
					}
					else
					{						
						huong[count] = 4;
					}
				}
				else
				{			
					if(isDown)
					{
						huong[count] = 3;
					}
					else if(isNormal)
					{						
						huong[count] = 2;
					}
					else
					{						
						huong[count] = 1;
					}
				}
				if(count == 0)
					count++;
			}			
		}	
		boolean isHasAction = false;
		thisTouch = 0;
		for(int i = 0;i<2;i++)
		{
			if(huong[i]!=0)
			{
				isHasAction = true;
				thisTouch++;
			}
		}
				
		if(isHasAction)
		{
			if(lastTouch == 2)
			{
				if(thisTouch == 2)
				{
					processTouch(huong);
				}
				if(thisTouch == 1)
				{					
					if(nvc.frame!=0)
					{
						
					   nvc.tickQ = 0;
					   nvc.isQLeft = false;
					   nvc.isWaitQ = true;
					   nvc.countQ = 0;
					}
					nvc.frame = 0;
					nvc.isFlip = false;
				}			
			}
			else
			{
				processTouch(huong);
				lastTouch = thisTouch;
			}
		}
		else
		{
			if(nvc.frame!=0)
			{
				
			   nvc.tickQ = 0;
			   nvc.isQLeft = false;
			   nvc.isWaitQ = true;
			   nvc.countQ = 0;
			}
			nvc.frame = 0;
			nvc.isFlip = false;
			lastTouch = 0;
		}
		nvc.update(delayTime);
		if(!Asset.begin.isPlaying())
			enemyManager.update(delayTime);
		score.update(delayTime);
		hit.update(delayTime);
		rank.update(delayTime);
	}

	public void processTouch(int[] huong)
	{
		if(nvc.id == 1){
		switch (huong[0]) {
		case 1:		
			if (huong[1] != 0) {
				switch (huong[1]) {
				case 1:
					nvc.frame = 11;
					nvc.isFlip = false;
					break;
				case 2:
					nvc.frame = 11;
					nvc.isFlip = false;
					break;
				case 3:
					nvc.frame = 11;
					nvc.isFlip = false;
					break;
				case 4:
					nvc.frame = 5;
					nvc.isFlip = false;
					break;
				case 5:
					nvc.frame = 10;
					nvc.isFlip = true;
					break;
				case 6:
					nvc.frame = 6;
					nvc.isFlip = false;
					break;
				}
			}
			else
			{	
				nvc.frame=11;
				nvc.isFlip = false;
			}
			break;
		case 2:			
			if (huong[1] != 0) {
				switch (huong[1]) {
				case 1:
					nvc.frame = 1;
					nvc.isFlip = false;
					break;
				case 2:
					nvc.frame = 1;
					nvc.isFlip = false;
					break;
				case 3:
					nvc.frame = 1;
					nvc.isFlip = false;
					break;
				case 4:
					nvc.frame = 10;
					nvc.isFlip = false;
					break;
				case 5:
					nvc.frame = 7;
					nvc.isFlip = false;
					break;
				case 6:
					nvc.frame = 13;
					nvc.isFlip = false;
					break;
				}
			}
			else
			{	
				nvc.frame=1;
				nvc.isFlip = false;
			}
			break;
			
		case 3:			
			if (huong[1] != 0) {
				switch (huong[1]) {
				case 1:
					nvc.frame = 14;
					nvc.isFlip = false;
					break;
				case 2:
					nvc.frame = 14;
					nvc.isFlip = false;
					break;
				case 3:
					nvc.frame = 14;
					nvc.isFlip = false;
					break;
				case 4:
					nvc.frame = 6;
					nvc.isFlip = true;
					break;
				case 5:
					nvc.frame = 13;
					nvc.isFlip = true;
					break;
				case 6:
					nvc.frame = 8;
					nvc.isFlip = false;
					break;
				}
			}
			else
			{	
				nvc.frame=14;
				nvc.isFlip = false;
			}
			break;
		case 4:				
			if (huong[1] != 0) {
				switch (huong[1]) {
				case 1:
					nvc.frame = 5;
					nvc.isFlip = false;
					break;
				case 2:
					nvc.frame = 10;
					nvc.isFlip = false;
					break;
				case 3:
					nvc.frame = 6;
					nvc.isFlip = true;
					break;
				case 4:
					nvc.frame = 11;
					nvc.isFlip = true;
					break;
				case 5:
					nvc.frame = 11;
					nvc.isFlip = true;
					break;
				case 6:
					nvc.frame = 11;
					nvc.isFlip = true;
					break;
				}
			}
			else
			{	
				nvc.frame=11;
				nvc.isFlip = true;
			}
			break;
		case 5:				
			if (huong[1] != 0) {
				switch (huong[1]) {
				case 1:
					nvc.frame = 10;
					nvc.isFlip = true;
					break;
				case 2:
					nvc.frame = 7;
					nvc.isFlip = false;
					break;
				case 3:
					nvc.frame = 13;
					nvc.isFlip = true;
					break;
				case 4:
					nvc.frame = 12;
					nvc.isFlip = true;
					break;
				case 5:
					nvc.frame = 12;
					nvc.isFlip = true;
					break;
				case 6:
					nvc.frame = 12;
					nvc.isFlip = true;
					break;
				}
			}
			else
			{	
				nvc.frame=12;
				nvc.isFlip = true;
			}
			break;
		case 6:				
			if (huong[1] != 0) {
				switch (huong[1]) {
				case 1:
					nvc.frame = 6;
					nvc.isFlip = false;
					break;
				case 2:
					nvc.frame = 13;
					nvc.isFlip = false;
					break;
				case 3:
					nvc.frame = 8;
					nvc.isFlip = false;
					break;
				case 4:
					nvc.frame = 4;
					nvc.isFlip = false;
					break;
				case 5:
					nvc.frame = 4;
					nvc.isFlip = false;
					break;
				case 6:
					nvc.frame = 4;
					nvc.isFlip = false;
					break;
				}
			}
			else
			{	
				nvc.frame=4;
				nvc.isFlip = false;
			}
			break;
		}
		}
		//==========================================================================
		else 
			if(nvc.id == 2){
				switch (huong[0]) {
				case 1:		
					if (huong[1] != 0) {
						switch (huong[1]) {
						case 1:
							nvc.frame = 3;
							nvc.isFlip = false;
							break;
						case 2:
							nvc.frame = 3;
							nvc.isFlip = false;
							break;
						case 3:
							nvc.frame = 3;
							nvc.isFlip = false;
							break;
						case 4:
							nvc.frame = 5;
							nvc.isFlip = false;
							break;
						case 5:
							nvc.frame = 10;
							nvc.isFlip = true;
							break;
						case 6:
							nvc.frame = 6;
							nvc.isFlip = true;
							break;
						}
					}
					else
					{	
						nvc.frame=3;
						nvc.isFlip = false;
					}
					break;
				case 2:			
					if (huong[1] != 0) {
						switch (huong[1]) {
						case 1:
							nvc.frame = 1;
							nvc.isFlip = false;
							break;
						case 2:
							nvc.frame = 1;
							nvc.isFlip = false;
							break;
						case 3:
							nvc.frame = 1;
							nvc.isFlip = false;
							break;
						case 4:
							nvc.frame = 10;
							nvc.isFlip = false;
							break;
						case 5:
							nvc.frame = 7;
							nvc.isFlip = false;
							break;
						case 6:
							nvc.frame = 13;
							nvc.isFlip = true;
							break;
						}
					}
					else
					{	
						nvc.frame=1;
						nvc.isFlip = false;
					}
					break;
					
				case 3:			
					if (huong[1] != 0) {
						switch (huong[1]) {
						case 1:
							nvc.frame = 4;
							nvc.isFlip = false;
							break;
						case 2:
							nvc.frame = 4;
							nvc.isFlip = false;
							break;
						case 3:
							nvc.frame = 4;
							nvc.isFlip = false;
							break;
						case 4:
							nvc.frame = 6;
							nvc.isFlip = false;
							break;
						case 5:
							nvc.frame = 13;
							nvc.isFlip = false;
							break;
						case 6:
							nvc.frame = 8;
							nvc.isFlip = false;
							break;
						}
					}
					else
					{	
						nvc.frame=4;
						nvc.isFlip = false;
					}
					break;
				case 4:				
					if (huong[1] != 0) {
						switch (huong[1]) {
						case 1:
							nvc.frame = 5;
							nvc.isFlip = false;
							break;
						case 2:
							nvc.frame = 10;
							nvc.isFlip = false;
							break;
						case 3:
							nvc.frame = 6;
							nvc.isFlip = false;
							break;
						case 4:
							nvc.frame = 3;
							nvc.isFlip = true;
							break;
						case 5:
							nvc.frame = 3;
							nvc.isFlip = true;
							break;
						case 6:
							nvc.frame = 3;
							nvc.isFlip = true;
							break;
						}
					}
					else
					{	
						nvc.frame=3;
						nvc.isFlip = true;
					}
					break;
				case 5:				
					if (huong[1] != 0) {
						switch (huong[1]) {
						case 1:
							nvc.frame = 10;
							nvc.isFlip = true;
							break;
						case 2:
							nvc.frame = 7;
							nvc.isFlip = false;
							break;
						case 3:
							nvc.frame = 13;
							nvc.isFlip = false;
							break;
						case 4:
							nvc.frame = 1;
							nvc.isFlip = true;
							break;
						case 5:
							nvc.frame = 1;
							nvc.isFlip = true;
							break;
						case 6:
							nvc.frame = 1;
							nvc.isFlip = true;
							break;
						}
					}
					else
					{	
						nvc.frame=1;
						nvc.isFlip = true;
					}
					break;
				case 6:				
					if (huong[1] != 0) {
						switch (huong[1]) {
						case 1:
							nvc.frame = 6;
							nvc.isFlip = true;
							break;
						case 2:
							nvc.frame = 13;
							nvc.isFlip = true;
							break;
						case 3:
							nvc.frame = 8;
							nvc.isFlip = false;
							break;
						case 4:
							nvc.frame = 4;
							nvc.isFlip = true;
							break;
						case 5:
							nvc.frame = 4;
							nvc.isFlip = true;
							break;
						case 6:
							nvc.frame = 4;
							nvc.isFlip = true;
							break;
						}
					}
					else
					{	
						nvc.frame=4;
						nvc.isFlip = true;
					}
					break;
				}
				}
		//===============================================
			else 
				if(nvc.id == 3){
					switch (huong[0]) {
					case 1:		
						if (huong[1] != 0) {
							switch (huong[1]) {
							case 1:
								nvc.frame = 3;
								nvc.isFlip = false;
								break;
							case 2:
								nvc.frame = 3;
								nvc.isFlip = false;
								break;
							case 3:
								nvc.frame = 3;
								nvc.isFlip = false;
								break;
							case 4:
								nvc.frame = 5;
								nvc.isFlip = false;
								break;
							case 5:
								nvc.frame = 10;
								nvc.isFlip = true;
								break;
							case 6:
								nvc.frame = 6;
								nvc.isFlip = true;
								break;
							}
						}
						else
						{	
							nvc.frame=3;
							nvc.isFlip = false;
						}
						break;
					case 2:			
						if (huong[1] != 0) {
							switch (huong[1]) {
							case 1:
								nvc.frame = 1;
								nvc.isFlip = false;
								break;
							case 2:
								nvc.frame = 1;
								nvc.isFlip = false;
								break;
							case 3:
								nvc.frame = 1;
								nvc.isFlip = false;
								break;
							case 4:
								nvc.frame = 10;
								nvc.isFlip = false;
								break;
							case 5:
								nvc.frame = 7;
								nvc.isFlip = false;
								break;
							case 6:
								nvc.frame = 13;
								nvc.isFlip = true;
								break;
							}
						}
						else
						{	
							nvc.frame=1;
							nvc.isFlip = false;
						}
						break;
						
					case 3:			
						if (huong[1] != 0) {
							switch (huong[1]) {
							case 1:
								nvc.frame = 4;
								nvc.isFlip = false;
								break;
							case 2:
								nvc.frame = 4;
								nvc.isFlip = false;
								break;
							case 3:
								nvc.frame = 4;
								nvc.isFlip = false;
								break;
							case 4:
								nvc.frame = 6;
								nvc.isFlip = false;
								break;
							case 5:
								nvc.frame = 13;
								nvc.isFlip = false;
								break;
							case 6:
								nvc.frame = 8;
								nvc.isFlip = false;
								break;
							}
						}
						else
						{	
							nvc.frame=4;
							nvc.isFlip = false;
						}
						break;
					case 4:				
						if (huong[1] != 0) {
							switch (huong[1]) {
							case 1:
								nvc.frame = 5;
								nvc.isFlip = false;
								break;
							case 2:
								nvc.frame = 10;
								nvc.isFlip = false;
								break;
							case 3:
								nvc.frame = 6;
								nvc.isFlip = false;
								break;
							case 4:
								nvc.frame = 3;
								nvc.isFlip = true;
								break;
							case 5:
								nvc.frame = 3;
								nvc.isFlip = true;
								break;
							case 6:
								nvc.frame = 3;
								nvc.isFlip = true;
								break;
							}
						}
						else
						{	
							nvc.frame=3;
							nvc.isFlip = true;
						}
						break;
					case 5:				
						if (huong[1] != 0) {
							switch (huong[1]) {
							case 1:
								nvc.frame = 10;
								nvc.isFlip = true;
								break;
							case 2:
								nvc.frame = 7;
								nvc.isFlip = false;
								break;
							case 3:
								nvc.frame = 13;
								nvc.isFlip = false;
								break;
							case 4:
								nvc.frame = 1;
								nvc.isFlip = true;
								break;
							case 5:
								nvc.frame = 1;
								nvc.isFlip = true;
								break;
							case 6:
								nvc.frame = 1;
								nvc.isFlip = true;
								break;
							}
						}
						else
						{	
							nvc.frame=1;
							nvc.isFlip = true;
						}
						break;
					case 6:				
						if (huong[1] != 0) {
							switch (huong[1]) {
							case 1:
								nvc.frame = 6;
								nvc.isFlip = true;
								break;
							case 2:
								nvc.frame = 13;
								nvc.isFlip = true;
								break;
							case 3:
								nvc.frame = 8;
								nvc.isFlip = false;
								break;
							case 4:
								nvc.frame = 4;
								nvc.isFlip = true;
								break;
							case 5:
								nvc.frame = 4;
								nvc.isFlip = true;
								break;
							case 6:
								nvc.frame = 4;
								nvc.isFlip = true;
								break;
							}
						}
						else
						{	
							nvc.frame=4;
							nvc.isFlip = true;
						}
						break;
					}
					}
		//===============================================
	}
	
	@Override
	public boolean keyDown (int keycode) {
		// TODO Auto-generated method stub
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
