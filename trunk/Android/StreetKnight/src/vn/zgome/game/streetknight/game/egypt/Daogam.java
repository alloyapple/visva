package vn.zgome.game.streetknight.game.egypt;

import java.util.Random;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.action.FrameAnimation;
import vn.zgome.game.streetknight.game.main.Blood;
import vn.zgome.game.streetknight.game.main.Comment;
import vn.zgome.game.streetknight.game.main.ILive;
import vn.zgome.game.streetknight.game.main.NVC;

public class Daogam extends ILive
{
	int frame = 1;
	int x,y;
	int state;
	FrameAnimation ani = new FrameAnimation();
	int type;	
	boolean isDownSpeed;
	int tickDownSpeed;	
	
	int frameStartNor=0, frameEndNor=7, frameStartAttack=8, frameEndAttack=13, frameStartKick=8, frameEndKick=13;
	int timeFrameNor=50, timeFrameAttack=100, timeFrameKick=100, timeWaitKick;
	int xStartLeft, xEndLeft;
	int xStartRight, xEndRight;
	int yStartConner, yEndConner;
	int xStartAttackLeft, xEndAttackLeft;
	int xStartAttackRight, xEndAttackRight;
	int yStartAttackConner, yEndAttackConner;
	int deltaX, deltaY;
	int bloodX, bloodY;
	int timeStep=ILive.TIME_STEP_FLYING;
	int score = 60;
	int money = 10;
	int hp;
	
	int TIME_STEP = 40;
	int stateDS = 1;
	int deltaDS;
	int mulStep = 5;
	int mulFPS = 5;
	public void updateDS(int delayTime)
	{
		if(stateDS==1)
		{
			if(_screen.itemUi.idItem1==0&&_screen.itemUi.state == 1&&state == 1)
			{				
				if(type == 1 || type == 4){
					if(y>=yStartConner&&y<=yEndConner+deltaDS)
					{
						stateDS = 2;	
						timeStep *= mulStep;
						ani.TIME_PER_FRAME *= mulFPS;
					}
					}
					else if(type == 2)
					{
						if(x>=xStartRight &&x<=xEndRight+deltaDS)
						{						
							stateDS = 2;	
							timeStep *= mulStep;
							ani.TIME_PER_FRAME *= mulFPS;
						}
					}
					else if(type == 5)
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
			if((_screen.itemUi.idItem1==0&&_screen.itemUi.state!=1)||state == 2)
			{								
				if(state == 1){
				stateDS = 1;		
				timeStep = TIME_STEP;				
				ani.TIME_PER_FRAME = timeFrameNor;
				}
				else
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
		if(state == 1)
		{
			state = 2;
			ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
			brokenXY();
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
		if(tickDownSpeed >= 3000)
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
	
	public void set(int type)
	{
		this.type = type;
		if(type == 4)
		{			
		}
		else if(type==2)
		{			
		}
		else if(type==5)
		{			
		}
		else if(type==1)
		{			
		}		
	}
	
	public Daogam (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start () {
		state = 1;
		float xCPy;
		if(type == 1){
		x = (int)game.X(839-200*0.6f/2);
		y = (int)game.Y(490-200*0.6f/2);
		xCPy = ((float)((x+game.X(200*0.6f/2))-(_screen.nvc.x+_screen.nvc.xHead))/(float)((y+game.Y(200*0.6f/2))-(_screen.nvc.y+_screen.nvc.yHead)));
		deltaX=game.X(9);
		deltaY=(int)game.Y(9/xCPy);
		}
		else if(type==4)
		{
			x = (int)game.X(0-200*0.6f/2);
			y = (int)game.Y(490-200*0.6f/2);
			xCPy = ((float)(-(x+game.X(200*0.6f/2))+(_screen.nvc.x+_screen.nvc.xHead))/(float)((y+game.Y(200*0.6f/2))-(_screen.nvc.y+_screen.nvc.yHead)));
			deltaX=game.X(9);
			deltaY=(int)game.Y(9/xCPy);
		}
		else if(type==2)
		{
			x = game.X(839);
			y = game.Y(120);
			deltaX=game.X(9);
		}
		else if(type == 5){
			x = game.X(0-80);
			y = game.Y(120);
			deltaX=game.X(9);
		}
		ani.setInfo(frameStartNor, frameEndNor, timeFrameNor);
		
		yStartConner=_screen.nvc.y;
		yEndConner= (int)(_screen.nvc.y+_screen.nvc.hiConner);
		xStartRight =_screen.nvc.x;
		xEndRight =_screen.nvc.x+_screen.nvc.wi;
		xStartLeft = (int)(_screen.nvc.x-game.X(200*0.6f));
		xEndLeft = _screen.nvc.x+_screen.nvc.wi;

		yStartAttackConner = _screen.nvc.y;
		yEndAttackConner = (int)(_screen.nvc.y+_screen.nvc.yHead);
		
		bloodX = _screen.nvc.x + _screen.nvc.wi/2-Asset.blood.frames[0].wi/2;
		bloodY = _screen.nvc.y;
		
		xStartAttackRight = _screen.nvc.x + _screen.nvc.wi/2;
		//xEndAttackRight = _screen.nvc.x+_screen.nvc.wi/2-game.X(50);
		xStartAttackLeft = (int)(_screen.nvc.x + _screen.nvc.wi/2 + game.X((100-200)*0.6f));
		//xEndAttackLeft=_screen.nvc.x + _screen.nvc.wi;
		
		deltaDS = game.X(50);
	}

	public void setFPS(int normal, int attack)
	{		
		this.timeFrameNor = normal;
		this.timeFrameAttack = attack;
	}
	
	public void setDelta(int deltaX, int deltaY)
	{
		if(type==1){
		this.deltaX = (int)(deltaX*NVC.cos);
		this.deltaY = (int)(deltaY*NVC.sin);
		}
		else if(type==4)
		{
			this.deltaX = (int)(deltaX*NVC.cos1);
			this.deltaY = (int)(deltaY*NVC.sin1);
		}
		else
		{
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}
	}
	
	
	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		game.batcher.draw(Asset.dao_gam.frames[frame].region, x, y, Asset.dao_gam.frames[frame].wi*0.6f, Asset.dao_gam.frames[frame].hi*0.6f);
		if(state == 2)
		{
			game.batcher.draw(Asset.blood3.frames[bloodAnimation.current].region, xBlood3, yBlood3, Asset.blood3.frames[bloodAnimation.current].wi, Asset.blood3.frames[bloodAnimation.current].hi);
		}
	}
	int tick;
	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		updateDS(delayTime);
		if((
			((((state == 1&&_screen.nvc.frame == 11 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 5 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 6 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
			)&&type == 1)
			||
			(((state == 1&&_screen.nvc.frame == 11 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 5 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 6 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			)&&type == 4)
			||
			(((state == 1&&_screen.nvc.frame == 1 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 7 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 13 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			)&&type == 2)
			||
			(((state == 1&&_screen.nvc.frame == 12 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 7 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
			||
			(state == 1&&_screen.nvc.frame == 13 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
			)&&type == 5))&&(_screen.nvc.id==1))
			||
			((
				(((state == 1&&_screen.nvc.frame == 3 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 5 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 6 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
				)&&type == 1)
				||
				(((state == 1&&_screen.nvc.frame == 3 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 5 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 6 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				)&&type == 4)
				||
				(((state == 1&&_screen.nvc.frame == 1 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 7 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 13 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
				)&&type == 2)
				||
				(((state == 1&&_screen.nvc.frame == 1 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 10 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 7 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame == 13 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				)&&type == 5))&&(_screen.nvc.id==2||_screen.nvc.id == 3))
			)
		{
			if(type == 1 || type == 4){
				if((y>=yStartConner&&y<=yEndConner)||(type==4&&x>_screen.nvc.x + _screen.nvc.wi/2 - _screen.nvc.deltaConner +  Asset.dao_gam.frames[0].wi*0.6f/2)||(type==1&&x<=_screen.nvc.x + _screen.nvc.wi/2 +_screen.nvc.deltaConner -  Asset.dao_gam.frames[0].wi*0.6f/2))
			{				
				state = 2;
				ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
				
				_screen.score.addScore(score,money);				

				commentXY();

				Asset.kim_loai.play(game.dataSave.sound?1f:0f);
				brokenXY();
			}
			}
			else if(type == 2)
			{
				if(x>=xStartRight &&x<=xEndRight)
				{
					state = 2;
					ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
					
					_screen.score.addScore(score,money);
					
					commentXY();
					
					Asset.kim_loai.play(game.dataSave.sound?1f:0f);
					brokenXY();
				}
			}
			else if(type == 5)
			{
				if(x<=xEndLeft&&x>=xStartLeft)
				{
					state = 2;
					
					ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
					
					_screen.score.addScore(score,money);
					
					commentXY();
					
					Asset.kim_loai.play(game.dataSave.sound?1f:0f);
					brokenXY();
					
				}
			}
		}
		if(state == 1){
		ani.update(delayTime);
		frame = ani.current;
		if(tick>=timeStep){
		if(type == 1){
		x-=deltaX;
		y-=deltaY;
		}
		else if(type==4)
		{
			x+=deltaX;
			y-=deltaY;
		}
		else if(type==2)
		{
			x-=deltaX;
			y-=0;
		}
		else if(type == 5)
		{
			x+=deltaX;
			y-=0;
		}
		tick = 0;
		if(type == 1|| type == 4){
		if((y>=yStartAttackConner&&y<=yEndAttackConner)||((type==4&&(x>=_screen.nvc.x+_screen.nvc.wi/2-Asset.dao_gam.frames[0].wi*0.6f/2)||(type==1&&(x<=_screen.nvc.x+_screen.nvc.wi/2-Asset.dao_gam.frames[0].wi*0.6f/2)))))
		{	
			state = 2;
			ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);			
						
				_screen.score.subHP();
			
			Blood blood = new Blood(game, screen);
			blood.set(new Random().nextInt(2), bloodX, bloodY);
			_screen.enemyManager.add(blood);
			Asset.chuong.play(game.dataSave.sound?1f:0f);
			brokenXY();
		}
		}
		else if(type == 2)
		{
			if(x<=xStartAttackRight)
			{	
			state = 2;
			ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);				
				_screen.score.subHP();
			
			Blood blood = new Blood(game, screen);
			blood.set(new Random().nextInt(2), bloodX, bloodY);
			_screen.enemyManager.add(blood);
			Asset.chuong.play(game.dataSave.sound?1f:0f);
			brokenXY();
			}
		}
		else if(type == 5)
		{		
			if(x>=xStartAttackLeft)
			{	
				state = 2;
				ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);				
				_screen.score.subHP();
			
			Blood blood = new Blood(game, screen);
			blood.set(new Random().nextInt(2), bloodX, bloodY);
				_screen.enemyManager.add(blood);
				Asset.chuong.play(game.dataSave.sound?1f:0f);
				brokenXY();
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
			bloodAnimation.update(delayTime);
			frame = ani.current;
			if(ani.current == frameEndAttack)
			{		
				isDie = true;
			}
		}		
	}	
	
	int xBlood3;
	int yBlood3;
	FrameAnimation bloodAnimation = new FrameAnimation();
	
	public void brokenXY()
	{
		x = (int)(x+Asset.dao_gam.frames[0].wi*0.6f/2);
		y = (int)(y+Asset.dao_gam.frames[0].hi*0.6f/2);
		
		x = (int)(x-Asset.dao_gam.frames[9].wi*0.6f/2);
		y = (int)(y-Asset.dao_gam.frames[9].hi*0.6f/2);
		
		xBlood3 = (int)(x + Asset.dao_gam.frames[9].wi*0.6f/2);
		yBlood3 = (int)(y + Asset.dao_gam.frames[9].wi*0.6f/2);
		xBlood3 = xBlood3 - Asset.blood3.frames[0].wi/2;
		yBlood3 = yBlood3 - Asset.blood3.frames[1].hi/2;
		bloodAnimation.setInfo(0, 3, 100);
		
	}	
	
	public void commentXY()
	{		
		Comment comment = new Comment(game, screen);
		comment.set(x+Asset.dao_gam.frames[0].wi*0.6f/2 - Asset.comment.frames[0].wi/2, y+Asset.dao_gam.frames[0].hi*0.6f/2-Asset.comment.frames[0].hi/2,score);
		_screen.enemyManager.add(comment);
	}
}
