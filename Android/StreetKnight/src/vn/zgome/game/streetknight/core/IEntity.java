package vn.zgome.game.streetknight.core;

public abstract class IEntity implements IRun
{
	public GameOS game;
	public IScreen screen;	
	public IEntity(GameOS game, IScreen screen)
	{
		this.game = game;
		this.screen = screen;
	}
}
