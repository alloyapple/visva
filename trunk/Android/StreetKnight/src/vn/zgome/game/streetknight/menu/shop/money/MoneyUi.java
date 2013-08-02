package vn.zgome.game.streetknight.menu.shop.money;

import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IScreen;
import vn.zgome.game.streetknight.core.IVisible;
import vn.zgome.game.streetknight.core.Log;
import vn.zgome.game.streetknight.menu.IEntityMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class MoneyUi extends IEntityMenu implements IVisible{

	public MoneyUi (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
	}

	public Table table;
	
	public Label addString(String name, int xBegin, int yBegin, int wi, int hi, float scale, BitmapFont font)
	{
		LabelStyle style = new LabelStyle(font, Color.WHITE);
		Label title = new Label(name, style);
		title.setFontScale(scale);
		xBegin = xBegin+(int)((wi - title.getTextBounds().width)/2);
		title.setPosition(xBegin, yBegin);
		return title;
	}
	
	@Override
	public void start () {
		// TODO Auto-generated method stub
		table = game.stageHelper.createTable(game.X(0), game.Y(0), game.X(838), game.Y(490));
		
		table.addActor(game.stageHelper.createImage(Asset.nen_chung, 0,0));			
		table.addActor(game.stageHelper.createImage(Asset.screenPay, game.X(0), game.Y(0)));
		table.addActor(addString("15.000 VNĐ/1 tin nhắn", game.X(65), game.Y(490-125+35), game.X(306), game.Y(95), 1f, game.freeFontTypeTool.variFont));
		table.addActor(addString("1000VNĐ = 1000$ \n    Trong game", game.X(65), game.Y(490-125-80), game.X(306), game.Y(95), 1f, game.freeFontTypeTool.variFont));
		table.addActor(addString("SMS", game.X(385), game.Y(490-125+35), game.X(189), game.Y(95), 1f, game.freeFontTypeTool.variFont));
		table.addActor(addString("THẺ CÀO", game.X(385), game.Y(490-125-70), game.X(189), game.Y(95), 1f, game.freeFontTypeTool.variFont));
		table.addActor(addString("TÍN DỤNG", game.X(385), game.Y(490-125-70-100), game.X(189), game.Y(95), 1f, game.freeFontTypeTool.variFont));
		table.addActor(addString("PAYPAL", game.X(385), game.Y(490-125-70-100*2), game.X(189), game.Y(95), 1f, game.freeFontTypeTool.variFont));
		table.addActor(addString("BACK", game.X(624), game.Y(490-125-70-100), game.X(132), game.Y(95), 1f, game.freeFontTypeTool.variFont));
		table.addActor(game.stageHelper.createButtonLogic(game.X(624), game.Y(490-326), game.X(132), game.Y(95), new ChangeListener() {			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				hide();
			}
		}));
		table.addActor(game.stageHelper.createButtonLogic(game.X(385), game.Y(490-125), game.X(189), game.Y(95), new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.iapListener.callSmsPurchase();
				Log.show("SMS");
				hide();
			}
		}));
		table.addActor(game.stageHelper.createButtonLogic(game.X(385), game.Y(490-226), game.X(189), game.Y(95), new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.iapListener.callCardPurchase();
				Log.show("Thẻ cào");
				hide();
			}
		}));
		table.addActor(game.stageHelper.createButtonLogic(game.X(385), game.Y(490-327), game.X(189), game.Y(95), new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.iapListener.callBankPurchase();
				Log.show("Tin dung");
				hide();
			}
		}));
		table.addActor(game.stageHelper.createButtonLogic(game.X(385), game.Y(490-428), game.X(189), game.Y(95), new ChangeListener() {
			
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				game.iapListener.callPaypalPurchase();
				Log.show("PayPal");
				hide();
			}
		}));
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
		table.setVisible(false);
	}

	@Override
	public void show () {
		// TODO Auto-generated method stub
		table.setVisible(true);
	}

}
