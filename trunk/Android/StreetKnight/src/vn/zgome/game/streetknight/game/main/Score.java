package vn.zgome.game.streetknight.game.main;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.game.IEntityGame;

public class Score extends IEntityGame{
	
	public int score = 0;
	public int money = 0;
	public int hp = 4;
	public int move;
	public int kill;
	public int killRank;
	
	public void reset()
	{
		score = 0;
		money = game.dataSave.money;
		if(game.dataSave.isFirstPlay){
			hp = 15;
			game.dataSave.isFirstPlay = false;
			game.dataSave.saveAll();
		}
		else
		{
			hp = 5;
		}
		move = 0;
		kill = 0;
		killRank = 0;
		isCheckNextMap = false;
	}
	
	public Score (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start () {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw () {
		// TODO Auto-generated method stub
		int hp = this.hp;
		if(hp<0)
			hp = 0;
		if(hp>5)
			hp=5;
		game.batcher.draw(Asset.HP[hp].region, game.X(20), game.Y(488)-Asset.HP[hp].hi, Asset.HP[hp].wi, Asset.HP[hp].hi);
		game.batcher.draw(Asset.score.region, game.X(20+180+20), game.Y(490)-Asset.score.hi, Asset.score.wi, Asset.score.hi);		
		game.batcher.draw(Asset.money.region, game.X(20+180+20+300+20), game.Y(490)-Asset.money.hi, Asset.money.wi, Asset.money.hi);
		int total;
		if(_screen.itemUi.idItem2 == 4)
		{
			total = this.hp+game.dataSave.coutHPBonus;
		}
		else
		{			
			total = this.hp;
		}
		game.freeFontTypeTool.normalFont.draw(game.batcher, ""+(total>5?total:""), game.X(20+150), game.Y(490-10));
		game.freeFontTypeTool.normalFont.draw(game.batcher, ""+score, game.X(20+180+20+10)+Asset.score.wi, game.Y(490-10));
		game.freeFontTypeTool.normalFont.draw(game.batcher, ""+money, game.X(20+180+20+300+20)+Asset.money.wi, game.Y(490-10));
		
	}

	@Override
	public void update (int delayTime) {
		// TODO Auto-generated method stub
		
	}

	public void addScore(int x, int y)
	{		
		score+=x;
		if(_screen.itemUi.idItem2 == 1){
			money+=y+2;
		}
		else
		{
			money+=y;
		}
		if(_screen.stage == 4)
		{
			if(_screen.nvc.id == 2)
			{
				score+=5;
			}
		}
		if(_screen.stage == 5)
		{
			if(_screen.nvc.id == 3 && _screen.nvc.delta == 0)
			{
				score+=5;
			}
		}
		if(_screen.stage == 6)
		{
			if(_screen.nvc.id == 3 && _screen.nvc.delta == 1)
			{
				score+=5;
			}
		}
		//if(_screen.stage == 7)
		//{
			if(_screen.nvc.id == 3 && _screen.nvc.delta == 2)
			{
				score+=10;
			}
		//}
		kill ++;
		killRank++;
		
		if(!isCheckNextMap){
		if(game.dataSave.nextMap<=8){			
		if (_screen.stage + 1 == game.dataSave.nextMap) {				
			if (score >= game.dataSave.scoreUnlock[game.dataSave.nextMap - 2]) {
			//if(score>200){
				isCheckNextMap = true;
			   game.dataSave.stateMap[game.dataSave.nextMap] = 1;
				game.dataSave.nextMap++;
				game.dataSave.saveAll();
				PassNewStage passNewStage = new PassNewStage(game, screen);
				passNewStage.set(1, game.X(838/2-550/4+5), game.Y(255));
				passNewStage.restart();
				_screen.enemyManager.add(passNewStage);
			}
			}
		}
		}
		
	}
	
	boolean isCheckNextMap;
	public void subHP()
	{
		if(_screen.nvc.isBattu == false){
			if(_screen.itemUi.idItem2 == 4)
			{
				if(game.dataSave.coutHPBonus>0)
				{
					game.dataSave.coutHPBonus--;
					if(game.dataSave.vibrate)
						game.vibrate(500);
					_screen.itemUi.countHP.setText(game.dataSave.coutHPBonus+"");					
					if(game.dataSave.coutHPBonus<=0)
					{
						game.dataSave.isBuy[12] = false;
						game.dataSave.isEquip[12] = false;
					}
					game.dataSave.saveAll();
				}
				else
				{									
					hp --;
					_screen.hit.restart();
					_screen.nvc.nhapnhay();			
					if(game.dataSave.vibrate)
						game.vibrate(500);
//					if (killRank < 20) {
//					} else if (killRank < 30) {
//					} else if (killRank < 50) {
//						killRank = 30;
//					} else if (killRank < 70) {
//						killRank=50;
//					} else if (killRank < 90) {
//						killRank = 70;
//					} else {
//						killRank = 90;
//					}
					killRank = 0;
				}
			}
			else{
				hp --;
				_screen.hit.restart();
				_screen.nvc.nhapnhay();			
				if(game.dataSave.vibrate)
					game.vibrate(500);
//				if (killRank < 20) {
//				} else if (killRank < 30) {
//					killRank=20;
//				} else if (killRank < 50) {
//					killRank = 30;
//				} else if (killRank < 70) {
//					killRank=50;
//				} else if (killRank < 90) {
//					killRank = 70;
//				} else {
//					killRank = 90;
//				}
				killRank = 0;
			}
		}
	}
	
	public void saveData()
	{		
	}
	
	public void saveHiScore()
	{		
	}
	
	public static String achivementHighscore(final int score) {
		if (score < 4000) {
			return "Novice";
		} else if (score < 7000) {
			return "Skillfull";
		} else if (score < 15000) {
			return "Expert";
		} else if (score < 25000) {
			return "Unstoppedable";
		} else {
			return "Master";
		}
	}
	
	public int achivementPlaying(final int count) {
		if (count < 20) {
			return 0;
		} else if (count < 30) {
			return 1;
		} else if (count < 50) {
			return 2;
		} else if (count < 70) {
			return 3;
		} else if (count < 90) {
			return 4;
		} else {
			return 5;
		}
	}
}
