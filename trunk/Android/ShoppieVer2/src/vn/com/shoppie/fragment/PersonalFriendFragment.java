package vn.com.shoppie.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.ListFBFriendAdapter;
import vn.com.shoppie.adapter.ListFBFriendAdapter.InviteFriendJoinSPInterface;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.database.sobject.ShoppieFriendList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.network.ParserUtility;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

@SuppressLint("DefaultLocale")
public class PersonalFriendFragment extends FragmentBasic implements
		InviteFriendJoinSPInterface {
	// =============================Constant Define=====================
	private static final String FB_USER_ID = "id";
	private static final String FB_USER_NAME = "name";
	private static final String FB_USER_PICTURE = "picture";
	// ============================Control Define =====================
	private ListView mFriendListView;
	private RelativeLayout mLinearProgressBar;
	private EditText mEditTextSearch;
	// ============================Class Define =======================
	private ListFBFriendAdapter mListFBFriendAdapter;
	private ShoppieSharePref mShopieSharePref;
	// ============================Variable Define =====================
	private ArrayList<FBUser> mListFriend = new ArrayList<FBUser>();
	private Map<String, FBUser> manageByName = new HashMap<String, FBUser>();
	private Vector<String> nameList = new Vector<String>();
	private IOnViewFriendDetail mListener;
	// FaceBook
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChanged(session, state, exception);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.page_personal_friend,
				null);
		mEditTextSearch = (EditText) root.findViewById(R.id.edt_search);
		mFriendListView = (ListView) root
				.findViewById(R.id.list_personal_friend);
		mListFBFriendAdapter = new ListFBFriendAdapter(getActivity(),
				mListFriend);
		mListFBFriendAdapter.setListener(this);
		mFriendListView.setAdapter(mListFBFriendAdapter);

		mEditTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				filterSearch(mEditTextSearch.getText().toString());
			}
		});

		mShopieSharePref = new ShoppieSharePref(getActivity());
		mLinearProgressBar = (RelativeLayout) root
				.findViewById(R.id.layout_progressBar);
		mLinearProgressBar.setVisibility(View.VISIBLE);
		return root;
	}

	private void filterSearch(String content) {
		// TODO Auto-generated method stub
		Vector<FBUser> data = new Vector<FBUser>();
		ArrayList<FBUser> _fbUserList = new ArrayList<FBUser>();
		for (FBUser fbUser : mListFriend) {
			if (fbUser.getUserName().toLowerCase()
					.contains(content.toLowerCase().toString()))
				_fbUserList.add(fbUser);
		}
		if ("".equals(content.toString()))
			mListFBFriendAdapter.updateFolderList(mListFriend);
		else
			mListFBFriendAdapter.updateFolderList(_fbUserList);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Facebook
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
//		uiHelper = new UiLifecycleHelper(getActivity(),
//				new Session.StatusCallback() {
//					@Override
//					public void call(Session session, SessionState state,
//							Exception exception) {
//						onSessionStateChanged(session, state, exception);
//					}
//
//					private void onSessionStateChanged(Session session,
//							SessionState state, Exception exception) {
//						// TODO Auto-generated method stub
//					}
//				});
//		ensureOpenSession();
	}

	@Override
	public void onResume() {
		super.onResume();
		// getFriends();
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

	public void getFriends() {
		Session activeSession = Session.getActiveSession();
		if (activeSession.getState().isOpened()) {
			Request friendRequest = Request.newMyFriendsRequest(activeSession,
					new GraphUserListCallback() {
						@Override
						public void onCompleted(List<GraphUser> users,
								Response response) {
							// Log.e("user size", "user size "+users.size());
							String userId = "";
							String userName = "";
							String userPicture = "";
							String url = "";
							int numberPie = 0;
							boolean isJoinSP = false;
							if (users != null)
								for (int i = 0; i < users.size(); i++) {
									JSONObject jsonObject = users.get(i)
											.getInnerJSONObject();
									if (jsonObject != null
											&& jsonObject.length() > 0) {
										userId = ParserUtility.getStringValue(
												jsonObject, FB_USER_ID);
										userName = ParserUtility
												.getStringValue(jsonObject,
														FB_USER_NAME);
										userPicture = ParserUtility
												.getStringValue(jsonObject,
														FB_USER_PICTURE);
										JSONObject jsonPicture;
										try {
											jsonPicture = new JSONObject(
													userPicture);
											if (jsonPicture != null
													&& jsonPicture.length() > 0) {
												String data = ParserUtility
														.getStringValue(
																jsonPicture,
																"data");
												JSONObject jsonPictureData = new JSONObject(
														data);
												if (jsonPictureData != null
														&& jsonPictureData
																.length() > 0) {
													url = ParserUtility
															.getStringValue(
																	jsonPictureData,
																	"url");
												}
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}

									FBUser fbUser = new FBUser(getActivity(),
											userName, url, isJoinSP, numberPie,
											userId);
									mListFriend.add(fbUser);
								}
							/* update list facebook friend */
							mListFBFriendAdapter.updateFolderList(mListFriend);
							if (mLinearProgressBar.getVisibility() == View.VISIBLE)
								mLinearProgressBar.setVisibility(View.GONE);
							updateListFriendFromSP(""
									+ mShopieSharePref.getCustId());
						}
					});
			Bundle params = new Bundle();
			params.putString("fields", "id, name, picture");
			friendRequest.setParameters(params);
			friendRequest.executeAsync();
		}
	}

	private void updateListFriendFromSP(String custId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.updateFriends(custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							ShoppieFriendList userInfoList = gson.fromJson(
									jsonObject.toString(),
									ShoppieFriendList.class);
							if (userInfoList.getResult().size() > 0)
								updateListFriendOnShoppie(userInfoList);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_UPDATE_FRIENDS);
	}

	private void updateListFriendOnShoppie(ShoppieFriendList userInfoList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < userInfoList.getResult().size(); i++) {
			for (int j = 0; j < mListFriend.size(); j++) {
				if (mListFriend
						.get(j)
						.getUserId()
						.equals(userInfoList.getResult().get(i).getFacebookid())) {
					mListFriend.get(j).setJoinSP(true);
					mListFriend.get(j).setNumberPie(
							userInfoList.getResult().get(i).getCurrentBal());

					/* update list facebook friend */
					mListFBFriendAdapter.updateFolderList(mListFriend);

				}
			}
		}
	}

	private void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		if (state != null && state.isOpened()) {
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

	@Override
	public void inviteFriendJoinSp(FBUser friend) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "invite " + friend.getUserName(),
				Toast.LENGTH_SHORT).show();
		mListener.inviteFriendJoinSP(friend);

	}

	public interface IOnViewFriendDetail {
		public void onClickViewFriendDetail(FBUser friend);

		public void inviteFriendJoinSP(FBUser friend);
	}

	public void setListener(IOnViewFriendDetail monVieFriendDetail) {
		this.mListener = monVieFriendDetail;
	}
}
