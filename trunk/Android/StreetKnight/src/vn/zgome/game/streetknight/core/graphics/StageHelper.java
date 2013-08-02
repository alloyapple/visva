package vn.zgome.game.streetknight.core.graphics;
import vn.zgome.game.streetknight.core.Asset;
import vn.zgome.game.streetknight.core.GameOS;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class StageHelper 
{
	GameOS game;
	
	public StageHelper(GameOS game)
	{
		this.game = game;
	}
	
	public ImageButton createButtonLogic(int x,int y, int wi, int hi, ChangeListener lis)
	{
		ImageButton but = null;
		ImageButtonStyle style;
		style = new ImageButtonStyle(game.skin.get("trans", ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(Asset.transButRegion.region);
		style.imageUp.setMinWidth(wi);
		style.imageUp.setMinHeight(hi);
		style.imageDown = new TextureRegionDrawable(Asset.transButRegion.region);
		style.imageDown.setMinWidth(wi);
		style.imageDown.setMinHeight(hi);
		but = new ImageButton(style);
		but.setPosition(x, y);
		if(lis!=null)
		{
			but.addListener(lis);
		}
		return but;
	}
	
	public ImageButton createImageButton(TextureRegionInfo up, TextureRegionInfo down, int x, int y)
	{
		ImageButton but = null;
		ImageButtonStyle style;
		style = new ImageButtonStyle(game.skin.get("trans", ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(up.region);
		style.imageUp.setMinWidth(up.wi);
		style.imageUp.setMinHeight(up.hi);
		style.imageDown = new TextureRegionDrawable(down.region);
		style.imageDown.setMinWidth(down.wi);
		style.imageDown.setMinHeight(down.hi);
		but = new ImageButton(style);
		but.setPosition(x, y);
		return but;
	}
	
	public ImageButton createImageButton2(TextureRegionInfo up, TextureRegionInfo down, int wi, int hi, int x, int y)
	{
		ImageButton but = null;
		ImageButtonStyle style;
		style = new ImageButtonStyle(game.skin.get("trans", ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(up.region);
		style.imageUp.setMinWidth(wi);
		style.imageUp.setMinHeight(hi);
		style.imageDown = new TextureRegionDrawable(down.region);
		style.imageDown.setMinWidth(wi);
		style.imageDown.setMinHeight(hi);
		but = new ImageButton(style);
		but.setPosition(x, y);
		return but;
	}
	
	public ImageButton createImageButton2(TextureRegion up, TextureRegion down, TextureRegion disable)
	{
		ImageButton but = null;
		ImageButtonStyle style;
		style = new ImageButtonStyle(game.skin.get("trans", ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(up);
		style.imageUp.setMinWidth(game.screenInfo.getX(up.getRegionWidth()));
		style.imageUp.setMinHeight(game.screenInfo.getY(up.getRegionHeight()));
		style.imageDown = new TextureRegionDrawable(down);
		style.imageDown.setMinWidth(game.screenInfo.getX(down.getRegionWidth()));
		style.imageDown.setMinHeight(game.screenInfo.getY(down.getRegionHeight()));
		style.imageDisabled = new TextureRegionDrawable(disable);
		style.imageDisabled.setMinWidth(game.screenInfo.getX(disable.getRegionWidth()));
		style.imageDisabled.setMinHeight(game.screenInfo.getY(disable.getRegionHeight()));
		but = new ImageButton(style);
		return but;
	}
	
	public CheckBox createCheckBox(TextureRegionInfo on, TextureRegionInfo off)
	{
		CheckBox i=null;
		CheckBoxStyle style = new CheckBoxStyle();
		style.checkboxOn = new TextureRegionDrawable(on.region);
		style.checkboxOn.setMinWidth(on.wi);
		style.checkboxOn.setMinHeight(on.hi);
		style.checkboxOff = new TextureRegionDrawable(off.region);
		style.checkboxOff.setMinWidth(on.wi);
		style.checkboxOff.setMinHeight(off.hi);
		style.font = game.freeFontTypeTool.normalFont;
		i = new CheckBox("", style);
		return i;
	}
	
	public Image createImage(TextureRegionInfo img, int x, int y)
	{
		Image i = null;
		i = new Image(img.region);
		i.setSize(img.wi, img.hi);
		i.setPosition(x, y);
		return i;
	}
	
	public Image createImage(TextureRegionInfo img, int x, int y,int wi,int hi)
	{
		Image i = null;
		i = new Image(img.region);
		i.setSize(wi, hi);
		i.setPosition(x, y);
		return i;
	}
	
	public Table createTable(int x, int y, int wi, int hi)
	{
		Table table;
		table = new Table(game.skin);
		table.setSize(wi, hi);
		table.setPosition(x, y);
		return table;
	}
	
	public TextureRegionDrawable createDrawable(TextureRegion img)
	{
		TextureRegionDrawable i = new TextureRegionDrawable(img);
		i.setMinWidth(game.screenInfo.getX(img.getRegionWidth()));
		i.setMinHeight(game.screenInfo.getY(img.getRegionHeight()));
		return i;
	}
	public Label createLabel(String s, boolean isInDialog)
	{
		LabelStyle style = new LabelStyle(game.freeFontTypeTool.normalFont, Color.WHITE);
		Label text;
		if(isInDialog)
			text = new Label(s + "\n \n \n \n", style);
		else
		{
			text = new Label(s, style);
		}
		return text;
	}
	public Label createLabel(String s, Color color, int x, int y)
	{
		LabelStyle style = new LabelStyle(game.freeFontTypeTool.normalFont, color);
		Label text = new Label(s, style);
		text.setPosition(x, y);
		return text;
	}
}
