package vn.zgome.game.streetknight.core;

public abstract class AndroidFunction 
{	
	public abstract void showToast(String info);
	public abstract void showDialog(String title, AndroidFunction.Action action);
	public interface Action
	{
		public void action();
	}
	public abstract void vibrate(int delay);
	
	public abstract void putString(String key, String put);
	public abstract void putInteger(String key, int put);
	public abstract void putBoolean(String key, boolean put);
	
	public abstract String getString(String key, String df);
	public abstract int getInteger(String key, int df);
	public abstract boolean getBoolean(String key, boolean df);
}
