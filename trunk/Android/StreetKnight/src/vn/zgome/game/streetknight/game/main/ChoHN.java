package vn.zgome.game.streetknight.game.main;

import java.util.Random;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.action.FrameAnimation;
import vn.zgome.game.streetknight.game.main.Blood;
import vn.zgome.game.streetknight.game.main.Comment;
import vn.zgome.game.streetknight.game.main.ILive;


public class ChoHN extends ILive{
	
	FrameAnimation ani;
	int x;int y;
	int state;
	boolean isLeft;	
	boolean isFlip;
	
	boolean isDownSpeed;
	int tickDownSpeed;		
	int frame;
	
	int frameStartNor=0, frameEndNor=1, frameStartAttack=2, frameEndAttack=3, frameStartKick=4, frameEndKick=5;
	int timeFrameNor=ILive.TIME_STEP_AND_FPS_ANIMAL, timeFrameAttack=800, timeFrameKick=200, timeWaitKick=1000;
	int xStartLeft, xEndLeft;
	int xStartRight, xEndRight;
	int yStartConner, yEndConner;
	int xStartAttackLeft, xEndAttackLeft;
	int xStartAttackRight, xEndAttackRight;
	int yStartAttackConner, yEndAttackConner;
	int timeDownSpeed = 10000;
	int deltaX, deltaY;
	int bloodX, bloodY;
	int deltaBloodX, deltaBloodY;
	int timeStep=ILive.TIME_STEP_AND_FPS_ANIMAL;
	int score = 30;
	int money = 3;
	
	int hp;
	int timeStepVang = 20;
	int totalVang;
	int vang, vangPlus;
	
	int TIME_STEP = ILive.TIME_STEP_AND_FPS_ANIMAL;
	int stateDS = 1;
	int deltaDS;
	int mulStep = 5;
	int mulFPS = 5;
	
	public void updateDS(int delayTime)
	{
		if(stateDS==1)
		{
			if(_screen.itemUi.idItem1==0&&_screen.itemUi.state == 1&&(state == 1||state == 2))
			{				
					if(!isLeft)
					{
						if(x>=xStartRight &&x<=xEndRight+deltaDS)
						{						
							stateDS = 2;	
							timeStep *= mulStep;
							ani.TIME_PER_FRAME *= mulFPS;
						}
					}
					else if(isLeft)
					{
						if(x<=xEndLeft&&x>=xStartLeft-deltaDS)
						{							
							stateDS = 2;	
							timeStep *= mulStep;
							ani.TIME_PER_FRAME *= mulFPS;
						}
					}
			}
		}
		else if(stateDS==2)
		{			
			if((_screen.itemUi.idItem1==0&&_screen.itemUi.state!=1)||state == 3)
			{								
				if(state == 1){
				stateDS = 1;		
				timeStep = TIME_STEP;				
				ani.TIME_PER_FRAME = timeFrameNor;
				}
				else if(state == 2)
				{					
					stateDS = 1;
					timeStep = TIME_STEP;
					ani.TIME_PER_FRAME = timeFrameAttack;
				}
				else if(state == 3)
				{					
					stateDS = 1;
					timeStep = TIME_STEP;
					ani.TIME_PER_FRAME = timeFrameKick;
				}
			}
		}
		
	}
	
	
	public void explode()
	{	
		if(state == 1||state==2)
		{
			state = 3;
			ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
			isDrawEnd = false;
			tickDraw = 0;
			deltaVang = 0;
		}
	}
	
	public void downSpeed()
	{
		isDownSpeed = true;
		tickDownSpeed = 0;		
	}
	
	public void updateDownSpeed(int delayTime)
	{
		if(isDownSpeed){
		if(tickDownSpeed >= timeDownSpeed)
		{			
			isDownSpeed = false;
			tickDownSpeed = 0;
		}
		else
		{
			tickDownSpeed += delayTime;
		}
		}
		
	}
	
	public ChoHN (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		ani = new FrameAnimation();		
	}

	public void set(int type)
	{
		
		if(type==5)
		{
			isLeft = true;
			isFlip = false;
		}
		else
		{
			isLeft = false;
			isFlip = true;
		}
	}
	
	@Override
	public void start () {
		// TODO Auto-generated method stub		
		ani.setInfo(frameStartNor, frameEndNor, timeFrameNor);
		if(!isLeft){
		x = game.X(839);
		}
		else
		{
			x = game.X(0) - game.X(250);
		}
		y = game.Y(65);
		state = 1;
		xStartLeft=_screen.nvc.x + _screen.nvc.wiLeftAT + game.X(12) - game.X(120);
		xEndLeft =_screen.nvc.x+_screen.nvc.wi;
		xStartRight=_screen.nvc.x;
		xEndRight= _screen.nvc.x + _screen.nvc.wi - _screen.nvc.wiRightAT - game.X(12);
		
		if(isLeft)
			deltaBloodX = game.X(80-100);
		else
		{
			deltaBloodX = game.X(80-100);
		}		
		deltaBloodY = game.Y(-180);
		
		deltaX = game.X(20);
		xStartAttackLeft = (int)(_screen.nvc.x+_screen.nvc.wi*_screen.nvc.legL+game.X(-120));
		xStartAttackRight =  (int)(_screen.nvc.x+_screen.nvc.wi*_screen.nvc.legR);
		
		bloodX = _screen.nvc.x + _screen.nvc.wi/2-Asset.blood.frames[0].wi/2;
		bloodY = _screen.nvc.y;
		
		totalVang = game.X(70);
		vang = game.X(35);
		vangPlus = game.X(20);
		deltaDS = game.X(50);
		
		deltaX *= _screen.enemyManager.scale;
		deltaY *= _screen.enemyManager.scale;
	}
	
	public void setFPS(int normal, int attack)
	{		
		this.timeFrameNor = normal;
		this.timeFrameAttack = attack;
	}
	public void setDelta(int deltaX, int deltaY)
	{		
		this.deltaX = (int)(deltaX*((float)timeStep/100));
		this.deltaY = (int)(deltaY*((float)timeStep/100));
	}
	
	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw () {
		// TODO Auto-generated method stub
		if(isFlip)
			drawFlip();
		else
			game.batcher.draw(Asset.cho_ha_noi.frames[ani.current].region, x, y, Asset.cho_ha_noi.frames[ani.current].wi, Asset.cho_ha_noi.frames[ani.current].hi);
	}
	public void drawFlip()
	{
		game.batcher.draw(Asset.cho_ha_noi.frames[ani.current].region.getTexture(),  x,y,Asset.cho_ha_noi.frames[ani.current].wi, Asset.cho_ha_noi.frames[ani.current].hi,Asset.cho_ha_noi.frames[ani.current].region.getRegionX() , Asset.cho_ha_noi.frames[ani.current].region.getRegionY(), Asset.cho_ha_noi.frames[ani.current].region.getRegionWidth(), Asset.cho_ha_noi.frames[ani.current].region.getRegionHeight(), true, false);
	}
	
	int tick;
	boolean isDrawEnd;
	int tickDraw;
	int deltaVang;
	int tickVang;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		updateDS(delayTime);
		if(state !=3 &&(((( (((_screen.nvc.frame==14 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(_screen.nvc.frame==6 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
			||
			(_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(_screen.nvc.frame==13  && _screen.nvc.isFlip == true&& _screen.nvc.isUsed)
			)&&!isLeft)
			||
			(((_screen.nvc.frame==4 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(_screen.nvc.frame==6 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(_screen.nvc.frame==13  && _screen.nvc.isFlip == false&& _screen.nvc.isUsed))
				&&isLeft) ))&&_screen.nvc.id == 1)
				||
				(( (((_screen.nvc.frame==13 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
					||
					(_screen.nvc.frame==6 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
					||
					(_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
					||
					(_screen.nvc.frame==4  && _screen.nvc.isFlip == false&& _screen.nvc.isUsed)
					)&&!isLeft)
					||
					(((_screen.nvc.frame==4 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
						||
						(_screen.nvc.frame==6 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
						||
						(_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
						||
						(_screen.nvc.frame==13  && _screen.nvc.isFlip == true&& _screen.nvc.isUsed))
						&&isLeft)
						)&&(_screen.nvc.id == 2||_screen.nvc.id == 3))))
		{	
			if(isLeft){
				if(x>=xStartLeft && x<= xEndLeft){				
				state = 3;
				ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
				isDrawEnd = false;
				tickDraw = 0;
				deltaVang = 0;
				
				_screen.score.addScore(score,money);
				
				commentXY();
				
				Blood blood = new Blood(game, screen);
				blood.set(new Random().nextInt(2), x+deltaBloodX, y+deltaBloodY);
				_screen.enemyManager.add(blood);
				
				Asset.chuong.play(game.dataSave.sound?1f:0f);
				
				}
			}
			else{
				if(x>=xStartRight && x<= xEndRight){				
				state = 3;
				ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
				isDrawEnd = false;
				tickDraw = 0;
				deltaVang = 0;
				
				_screen.score.addScore(score,money);
				
				commentXY();
				
				Blood blood = new Blood(game, screen);
				blood.set(new Random().nextInt(2), x+deltaBloodX, y+deltaBloodY);
				_screen.enemyManager.add(blood);
				
				Asset.chuong.play(game.dataSave.sound?1f:0f);				
				}
			}			
		}
		if(state == 1){
			ani.update(delayTime);
		if(tick>=timeStep)
		{
			tick = 0;
			if(isLeft)
			{
				x += deltaX; 
				if(x>=xStartAttackLeft)
				{
					state = 2;
					ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);
				}
			}
			else{
				x -= deltaX; 
				if(x<=xStartAttackRight)
				{
					state = 2;
					ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);
				}
			}
		}
		else
		{
			tick += delayTime;
		}
		}
		else if(state == 2)
		{						
			ani.update(delayTime);
			if(ani.current == frameEndAttack)
			{				
				if(frame!=frameEndAttack){
					_screen.score.subHP();
					frame = frameEndAttack;
					Asset.chuong.play(game.dataSave.sound?1f:0f);
				}
				Blood blood = new Blood(game, screen);
				blood.set(new Random().nextInt(2), bloodX, bloodY);
				_screen.enemyManager.add(blood);
			}
			else
			{
				frame = ani.current;
			}
		}
		else if(state == 3)
		{			
			if(deltaVang<=totalVang)
			{
				if(tickVang>=timeStepVang)
				{					
					tickVang = 0;
					if(isLeft)
					{
						x-=vang;
					}
					else
						x+=vang;
					deltaVang += vangPlus;
				}
				else
				{
					tickVang += delayTime;
				}
				return;
			}			
			if(ani.current==frameEndKick)
			{
				if(!isDrawEnd)
				{
					isDrawEnd = true;				
				}
				else
				{
					if(tickDraw>=timeWaitKick){
						//start();
						isDie = true;
					}
					else
					{	
						tickDraw+=delayTime;
					}
				}
			}
			else
			{
				ani.update(delayTime);
			}
		}
	}
	
	public void commentXY()
	{		
		Comment comment = new Comment(game, screen);
		comment.set(x+Asset.cho_ha_noi.frames[0].wi/2 - Asset.comment.frames[0].wi/2, y+Asset.cho_ha_noi.frames[0].hi/2-Asset.comment.frames[0].hi/2,score);
		_screen.enemyManager.add(comment);
	}	
}
