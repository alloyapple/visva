package vn.com.shoppie.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.network.ParserUtility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.UiLifecycleHelper;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.model.GraphUser;

public class PersonalFriendFragment extends FragmentBasic {
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;
	private static final String FB_USER_ID = "id";
	private static final String FB_USER_NAME = "name";
	private static final String FB_USER_PICTURE = "picture";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.page_personal_friend,
				null);
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lifecycleHelper = new UiLifecycleHelper(getActivity(),
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChanged(session, state, exception);
					}

					private void onSessionStateChanged(Session session,
							SessionState state, Exception exception) {
						// TODO Auto-generated method stub

					}
				});
		lifecycleHelper.onCreate(savedInstanceState);

		ensureOpenSession();
	}

	@Override
	public void onResume() {
		super.onResume();
		getFriends();
	}

	private boolean ensureOpenSession() {
		if (Session.getActiveSession() == null
				|| !Session.getActiveSession().isOpened()) {
			Session.openActiveSession(getActivity(), true,
					new Session.StatusCallback() {
						@Override
						public void call(Session session, SessionState state,
								Exception exception) {
							onSessionStateChanged(session, state, exception);
						}
					});
			return false;
		}
		return true;
	}

	private void getFriends() {
		Log.e("run hadfh", "adsfd ");
		Session activeSession = Session.getActiveSession();
		if (activeSession.getState().isOpened()) {
			Request friendRequest = Request.newMyFriendsRequest(activeSession,
					new GraphUserListCallback() {
						@Override
						public void onCompleted(List<GraphUser> users,
								Response response) {
							// Log.e("user size", "user size "+users.size());
							String userId;
							String userName;
							String userPicture;
							for (int i = 0; i < users.size(); i++) {
								JSONObject jsonObject = users.get(i)
										.getInnerJSONObject();
								if (jsonObject != null
										&& jsonObject.length() > 0) {
									// Log.e("jsonObject", "jsonObject "
									// + jsonObject.toString());
									userId = ParserUtility.getStringValue(
											jsonObject, FB_USER_ID);
									userName = ParserUtility.getStringValue(
											jsonObject, FB_USER_NAME);
									userPicture = ParserUtility.getStringValue(
											jsonObject, FB_USER_PICTURE);
									JSONObject jsonPicture;
									try {
										jsonPicture = new JSONObject(
												userPicture);
										// Log.e("use name "+userName,
										// "picture "+jsonPicture.toString());
										if (jsonPicture != null
												&& jsonPicture.length() > 0) {
											String data = ParserUtility
													.getStringValue(
															jsonPicture, "data");
											JSONObject jsonPictureData = new JSONObject(
													data);
											if (jsonPictureData != null
													&& jsonPictureData.length() > 0) {
												String url = ParserUtility
														.getStringValue(
																jsonPictureData,
																"url");
												Log.e("url ", "datata " + url);
											}
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}
							// Log.e("INFO", "INFO " + response.toString());
						}
					});
			Bundle params = new Bundle();
			params.putString("fields", "id, name, picture");
			friendRequest.setParameters(params);
			friendRequest.executeAsync();
		}
	}

	private void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		if (pickFriendsWhenSessionOpened && state.isOpened()) {
			pickFriendsWhenSessionOpened = false;
			Log.e("gdfsdaf", "adfdf");
			getUserData(session);
		}
	}

	private void getUserData(final Session session) {
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						if (user != null
								&& session == Session.getActiveSession()) {
							// pictureView.setProfileId(user.getId());
							// userName.setText(user.getName());
							getFriends();

						}
						if (response.getError() != null) {

						}
					}
				});
		request.executeAsync();
	}
}
