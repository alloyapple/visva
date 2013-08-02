package vn.zgome.game.streetknight.menu.hiscore;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.FacebookListener;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.game.main.Score;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class HiScoreUi extends IEntityMenu implements IVisible{

	BackBut back;	
	public HiScoreUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		back = new BackBut(game, screen);
	}

	Table container;
	Label score;
	public String achivementHighscore(final int score) {
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
	Image novice, skillful, expert, unstop, master;
	@Override
	public void start () {
		// TODO Auto-generated method stub
		novice = game.stageHelper.createImage(Asset.novice, game.X(370), game.Y(490-360));
		skillful = game.stageHelper.createImage(Asset.skillful, game.X(370), game.Y(490-360));
		expert = game.stageHelper.createImage(Asset.expert, game.X(370), game.Y(490-360));
		unstop = game.stageHelper.createImage(Asset.unstoppable, game.X(350), game.Y(490-360));
		master = game.stageHelper.createImage(Asset.master, game.X(370), game.Y(490-360));
		
		container = game.stageHelper.createTable(0, 0, game.X(838), game.Y(490));
		container.addActor(game.stageHelper.createImage(Asset.nen_chung, 0, 0));	
		container.addActor(game.stageHelper.createImage(Asset.bgSettingRegion, game.X(279), game.Y(24)));		
		container.addActor(game.stageHelper.createImage(Asset.hiScore, game.X(319), game.Y(490-190)));	
		container.addActor(game.stageHelper.createImage(Asset.kill, game.X(300), game.Y(490-270)));		
		container.addActor(game.stageHelper.createImage(Asset.move, game.X(310), game.Y(490-320)));	
		back.start();
		Label hiScore = game.stageHelper.createLabel(game.dataSave.scoreMax+"", Color.WHITE, game.X(0), game.Y(490-230));
		int xScore = (int)((game.X(838) - hiScore.getTextBounds().width)/2);
		hiScore.setX(xScore);
		Label move =  game.stageHelper.createLabel(game.dataSave.kill+"", Color.WHITE, game.X(411), game.Y(490-270));;
		Label kill =  game.stageHelper.createLabel(game.dataSave.move+"", Color.WHITE, game.X(411), game.Y(490-318));;
		container.addActor(hiScore);
		container.addActor(kill);
		container.addActor(move);
		container.addActor(novice);
		container.addActor(skillful);
		container.addActor(expert);
		container.addActor(unstop);
		container.addActor(master);
		container.addActor(back.but);
		
		ImageButton fb;
		fb = game.stageHelper.createImageButton2(Asset.fb_shared, Asset.fb_shared, game.X(70), game.Y(70), game.X(839-70-60), game.Y(20));
		container.addActor(fb);
		fb.addListener(new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				final String link = FacebookListener.LINK;
				final String name = "Street Knight"; //ten cua feed
				final String caption = "Level " + FacebookListener.NAME_STAGE[game.dataSave.stageHiscore - 1];
				final int score = game.dataSave.scoreMax;
				final String descrip = "Trở thành " + Score.achivementHighscore(score) +" với điểm số " +score;
				final String picture = FacebookListener.PICTURE_URL[game.dataSave.stageHiscore - 1];
				
				game.facebookListener.onFeedRequest(link, name, caption, descrip, picture);
			}
		});
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
		novice.setVisible(false);
		skillful.setVisible(false);
		expert.setVisible(false);
		unstop.setVisible(false);
		master.setVisible(false);
		
		container.setVisible(true);
		
		String archi = achivementHighscore(game.dataSave.scoreMax);
		if(archi.equals("Novice"))
		{
			novice.setVisible(true);
		}
		else if(archi.equals("Skillfull"))
		{
			skillful.setVisible(true);
		}
		else if(archi.equals("Expert"))
		{
			expert.setVisible(true);
		}
		else if(archi.equals("Unstoppedable"))
		{
			unstop.setVisible(true);
		}
		else if(archi.equals("Master"))
		{
			master.setVisible(true);
		}
	}
	
}
