package vn.zgome.game.streetknight.game;

import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IEntity;
import vn.zgome.game.streetknight.core.IScreen;

public abstract class IEntityGame extends IEntity{
	public GameScreen _screen;
	public IEntityGame (GameOS game, IScreen screen) {
		super(game, screen);
		// TODO Auto-generated constructor stub
		this._screen = (GameScreen)screen;
	}
}
