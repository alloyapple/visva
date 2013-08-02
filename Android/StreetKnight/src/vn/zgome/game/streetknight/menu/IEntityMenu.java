package vn.zgome.game.streetknight.menu;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IEntity;
import vn.zgome.game.streetknight.core.IScreen;

public abstract class IEntityMenu extends IEntity{
	public MenuScreen _screen;
	public IEntityMenu (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		this._screen = (MenuScreen)screen;
	}
	
}
