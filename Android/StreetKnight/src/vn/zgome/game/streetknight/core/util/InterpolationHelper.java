package vn.zgome.game.streetknight.core.util;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class InterpolationHelper 
{
	public static Vector2 temp = new Vector2();
	
	public static Vector2 getCurrentPosition (Vector2 start, Vector2 end, float percent, String name) 
	{
		temp.set(end);
		temp.sub(start);
		temp.mul(getInterpolation(name).apply( percent));
		temp.add(start);
		return temp;
	}
	
	public static Interpolation getInterpolation (String name) {
		try {
			return (Interpolation)Interpolation.class.getField(name).get(null);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
