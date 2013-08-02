package vn.zgome.game.streetknight.game.main;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.action.FrameAnimation;
import vn.zgome.game.streetknight.game.main.Comment;
import vn.zgome.game.streetknight.game.main.ILive;


public class Thuoc extends ILive
{
	int frame = 1;
	int x,y;
	int state;
	FrameAnimation ani = new FrameAnimation();
	int type;	
	boolean isDownSpeed;
	int tickDownSpeed;	
	
	int frameStartNor=0, frameEndNor=7, frameStartAttack=0, frameEndAttack=0, frameStartKick=0, frameEndKick=0;
	int timeFrameNor=ILive.TIME_STEP_FLYING, timeFrameAttack=100, timeFrameKick=100, timeWaitKick;
	int xStartLeft, xEndLeft;
	int xStartRight, xEndRight;
	int yStartConner, yEndConner;
	int xStartAttackLeft, xEndAttackLeft;
	int xStartAttackRight, xEndAttackRight;
	int yStartAttackConner, yEndAttackConner;
	int deltaX, deltaY;
	int bloodX, bloodY;
	int timeStep=ILive.TIME_STEP_FLYING;
	int score = 0;
	int money = 0;
	int hp;
	
	int TIME_STEP = ILive.TIME_STEP_FLYING;
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
					else if(type == 2||type == 3)
					{
						if(x>=xStartRight &&x<=xEndRight+deltaDS)
						{						
							stateDS = 2;	
							timeStep *= mulStep;
							ani.TIME_PER_FRAME *= mulFPS;
						}
					}
					else if(type == 5||type==6)
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
	
	public Thuoc (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}
	float xCPy;
	@Override
	public void start () {
		// TODO Auto-generated method stub		
		state = 1;
		if(type == 1){
		x = game.X(839-60);
		y = game.Y(490-50);
		xCPy = ((float)((x+game.X(80/2))-(_screen.nvc.x+_screen.nvc.xHead))/(float)((y+game.Y(80/2))-(_screen.nvc.y+_screen.nvc.yHead)));
		deltaX=game.X(9);
		deltaY=(int)game.Y(9/xCPy);
		}
		else if(type==4)
		{
			x = game.X(0-60);
			y = game.Y(490-50);
			xCPy = ((float)(-(x+game.X(80/2))+(_screen.nvc.x+_screen.nvc.xHead))/(float)((y+game.Y(80/2))-(_screen.nvc.y+_screen.nvc.yHead)));
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
		else if(type==3)
		{
			x = game.X(839);
			y = game.Y(50);
			deltaX=game.X(9);
		}
		else if(type == 6){
			x = game.X(0-80);
			y = game.Y(50);
			deltaX=game.X(9);
		}
		ani.setInfo(frameStartNor, frameEndNor, timeFrameNor);
		
		yStartConner=_screen.nvc.y;
		yEndConner= (int)(_screen.nvc.y+_screen.nvc.hiConner);
		xStartRight =_screen.nvc.x;
		xEndRight =_screen.nvc.x+_screen.nvc.wi;
		xStartLeft = _screen.nvc.x;
		xEndLeft = _screen.nvc.x+_screen.nvc.wi;

		yStartAttackConner = _screen.nvc.y;
		yEndAttackConner = (int)(_screen.nvc.y+_screen.nvc.yHead);
		
		bloodX = _screen.nvc.x + game.X(80);
		bloodY = _screen.nvc.y;
		
		xStartAttackRight = _screen.nvc.x + _screen.nvc.wi/2;
		//xEndAttackRight = _screen.nvc.x+_screen.nvc.wi/2-game.X(50);
		xStartAttackLeft = _screen.nvc.x + _screen.nvc.wi/2 + game.X(40-80);
		//xEndAttackLeft=_screen.nvc.x + _screen.nvc.wi;
		
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
		game.batcher.draw(Asset.thuoc.frames[frame].region, x, y, Asset.thuoc.frames[frame].wi, Asset.thuoc.frames[frame].hi);
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
			)&&type == 5)
			||
			(((state == 1&&_screen.nvc.frame==14 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame==6 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame==13  && _screen.nvc.isFlip == true&& _screen.nvc.isUsed)
				)&&type==3)
			||
			(((state == 1&&_screen.nvc.frame==4 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame==6 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
				||
				(state == 1&&_screen.nvc.frame==13  && _screen.nvc.isFlip == false&& _screen.nvc.isUsed))
				&&type==6)
			)
			&&(_screen.nvc.id==1))
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
				)&&type == 5)
				||
				(((state == 1&&_screen.nvc.frame==13 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
					||
					(state == 1&&_screen.nvc.frame==6 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
					||
					(state == 1&&_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
					||
					(state == 1&&_screen.nvc.frame==4  && _screen.nvc.isFlip == false&& _screen.nvc.isUsed)
					)&&type==3)
				||
				(((state == 1&&_screen.nvc.frame==4 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
					||
					(state == 1&&_screen.nvc.frame==6 && _screen.nvc.isFlip == true && _screen.nvc.isUsed)
					||
					(state == 1&&_screen.nvc.frame==8 && _screen.nvc.isFlip == false && _screen.nvc.isUsed)
					||
					(state == 1&&_screen.nvc.frame==13  && _screen.nvc.isFlip == true&& _screen.nvc.isUsed))
					&&type==6)
				)
				&&(_screen.nvc.id==2||_screen.nvc.id == 3))
			)
		{
			if(type == 1 || type == 4){
				if((y>=yStartConner&&y<=yEndConner)||(type==4&&x>_screen.nvc.x + _screen.nvc.wi/2 - _screen.nvc.deltaConner -  Asset.thuoc.frames[0].wi/(2))||(type==1&&x<=_screen.nvc.x + _screen.nvc.wi/2 +_screen.nvc.deltaConner))
			{				
				state = 2;
				ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
				
				_screen.score.addScore(score,money);				
				_screen.score.hp++;
				commentXY();
				Asset.chuong.play(game.dataSave.sound?1f:0f);
			}
			}
			else if(type == 2||type == 3)
			{
				if(x>=xStartRight &&x<=xEndRight)
				{
					state = 2;
					ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);					
					_screen.score.addScore(score,money);					
					_screen.score.hp++;
					commentXY();
					Asset.chuong.play(game.dataSave.sound?1f:0f);
				}
			}
			else if(type == 5 || type == 6)
			{
				if(x<=xEndLeft&&x>=xStartLeft)
				{
					state = 2;
					
					ani.setInfo(frameStartKick, frameEndKick, timeFrameKick);
					
					_screen.score.addScore(score,money);
					
					_screen.score.hp++;
					commentXY();
					Asset.chuong.play(game.dataSave.sound?1f:0f);
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
		else if(type==2||type==3)
		{
			x-=deltaX;
			y-=0;
		}
		else if(type == 5||type == 6)
		{
			x+=deltaX;
			y-=0;
		}
		tick = 0;
		if(type == 1|| type == 4){
			if((y>=yStartAttackConner&&y<=yEndAttackConner)||((type==4&&(x<=xEndLeft&&x>=_screen.nvc.x+_screen.nvc.wi/2-Asset.thuoc.frames[0].wi/2)||(type==1&&(x>=xStartRight&&x<=_screen.nvc.x+_screen.nvc.wi/2)))))
		{	
			state = 2;
			ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);			
						
			//_screen.score.subHP();
			
			Asset.chuong.play(game.dataSave.sound?1f:0f);
		}
		}
		else if(type == 2||type==3)
		{
			if(x<=xStartAttackRight)
			{	
			state = 2;
			ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);				
			//_screen.score.subHP();
			
			Asset.chuong.play(game.dataSave.sound?1f:0f);
			}
		}
		else if(type == 5||type==6)
		{		
			if(x>=xStartAttackLeft)
			{	
				state = 2;
				ani.setInfo(frameStartAttack, frameEndAttack, timeFrameAttack);				
				//_screen.score.subHP();				
				Asset.chuong.play(game.dataSave.sound?1f:0f);
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
			frame = ani.current;
			if(ani.current == frameEndAttack)
			{		
				isDie = true;
			}
		}		
	}	
	public void commentXY()
	{		
		Comment comment = new Comment(game, screen);
		comment.set(x+Asset.thuoc.frames[0].wi/2 - Asset.comment.frames[0].wi/2, y+Asset.thuoc.frames[0].hi/2-Asset.comment.frames[0].hi/2,score);
		_screen.enemyManager.add(comment);
	}	
}

