package vn.zgome.game.streetknight.android;

import com.facebook.FacebookRequestError;
import com.facebook.model.GraphUser;

import android.app.Application;

public class MainApplication extends Application{
	
	// Tag used when logging all messages with the same tag (e.g. for demoing purposes)
	static final String TAG = "Street Kungfu";
	
	// Player's current score
	private int score = -1;
	
	/* Facebook application attributes */

	// Logged in status of the user
	private boolean loggedIn = false;
	private static final String LOGGED_IN_KEY = "logged_in";
	
	// Current logged in FB user and key for saving/restoring during the Activity lifecycle
	private GraphUser currentFBUser;
	private static final String CURRENT_FB_USER_KEY = "current_fb_user";
		
	// FacebookRequestError to show when the GameFragment closes
	private FacebookRequestError gameFragmentFBRequestError = null;

	/* Friend Smash application attribute getters & setters */
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	
	/* Facebook attribute getters & setters */
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
		if (!loggedIn) {
			// If the user is logged out, reset the score and nullify all the logged-in user's values
			setScore(-1);
			setCurrentFBUser(null);
		}
	}

	public GraphUser getCurrentFBUser() {
		return currentFBUser;
	}

	public void setCurrentFBUser(GraphUser currentFBUser) {
		this.currentFBUser = currentFBUser;
	}

	public FacebookRequestError getGameFragmentFBRequestError() {
		return gameFragmentFBRequestError;
	}

	public void setGameFragmentFBRequestError(FacebookRequestError gameFragmentFBRequestError) {
		this.gameFragmentFBRequestError = gameFragmentFBRequestError;
	}

	public static String getLoggedInKey() {
		return LOGGED_IN_KEY;
	}
	
	public static String getCurrentFbUserKey() {
		return CURRENT_FB_USER_KEY;
	}
}
