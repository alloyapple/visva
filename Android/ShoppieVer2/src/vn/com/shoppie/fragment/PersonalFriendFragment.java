package vn.com.shoppie.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.HomeActivity;
import vn.com.shoppie.adapter.ListFBFriendAdapter;
import vn.com.shoppie.adapter.ListFBFriendAdapter.InviteFriendJoinSPInterface;
import vn.com.shoppie.constant.ShopieSharePref;
import vn.com.shoppie.database.sobject.MerchantStoreList;
import vn.com.shoppie.database.sobject.UserInfo;
import vn.com.shoppie.database.sobject.UserInfoList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.network.ParserUtility;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

public class PersonalFriendFragment extends FragmentBasic implements
		InviteFriendJoinSPInterface {
	// =============================Constant Define=====================
	private static final String FB_USER_ID = "id";
	private static final String FB_USER_NAME = "name";
	private static final String FB_USER_PICTURE = "picture";
	// ============================Control Define =====================
	private ListView mFriendListView;
	private RelativeLayout mLinearProgressBar;
	// ============================Class Define =======================
	private ListFBFriendAdapter mListFBFriendAdapter;
	private ShopieSharePref mShopieSharePref;
	// ============================Variable Define =====================
	private ArrayList<FBUser> mListFriend = new ArrayList<FBUser>();
	private onViewFriendDetail mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.page_personal_friend,
				null);
		mFriendListView = (ListView) root
				.findViewById(R.id.list_personal_friend);
		mListFBFriendAdapter = new ListFBFriendAdapter(getActivity(),
				mListFriend);
		mListFBFriendAdapter.setListener(this);
		mFriendListView.setAdapter(mListFBFriendAdapter);
		mFriendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.e("adkjhdf", "asdfjf " + arg2);
				mListener.onClickViewFriendDetail(mListFriend.get(arg2));
			}
		});
		
		mShopieSharePref = new ShopieSharePref(getActivity());
		mLinearProgressBar = (RelativeLayout) root
				.findViewById(R.id.layout_progressBar);
		mLinearProgressBar.setVisibility(View.VISIBLE);
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// lifecycleHelper = new UiLifecycleHelper(getActivity(),
		// new Session.StatusCallback() {
		// @Override
		// public void call(Session session, SessionState state,
		// Exception exception) {
		// onSessionStateChanged(session, state, exception);
		// }
		//
		// private void onSessionStateChanged(Session session,
		// SessionState state, Exception exception) {
		// // TODO Auto-generated method stub
		// }
		// });
		// lifecycleHelper.onCreate(savedInstanceState);
		// ensureOpenSession();
	}

	@Override
	public void onResume() {
		super.onResume();
		// getFriends();
	}

	// private boolean ensureOpenSession() {
	// if (Session.getActiveSession() == null
	// || !Session.getActiveSession().isOpened()) {
	// Session.openActiveSession(getActivity(), true,
	// new Session.StatusCallback() {
	// @Override
	// public void call(Session session, SessionState state,
	// Exception exception) {
	// onSessionStateChanged(session, state, exception);
	// }
	// });
	// return false;
	// }
	// return true;
	// }

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
													Log.e("url ", "datata "
															+ url);
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
							updateListFriendFromSP(""+mShopieSharePref.getCustId());
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

		// TODO Auto-generated method stub
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
							UserInfoList userInfoList = gson.fromJson(
									jsonObject.toString(), UserInfoList.class);
							Log.e("merchantproductlist", "merchantproductlist "
									+ userInfoList.getResult().size());
							for (int i = 0; i < userInfoList.getResult().size(); i++) {
								Log.e("merchantproductlist",
										"merchantproductlist "
												+ userInfoList.getResult().get(i)
														.getCustName());
							}
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

	// private void onSessionStateChanged(Session session, SessionState state,
	// Exception exception) {
	// if (pickFriendsWhenSessionOpened && state.isOpened()) {
	// pickFriendsWhenSessionOpened = false;
	// Log.e("gdfsdaf", "adfdf");
	// getUserData(session);
	// }
	// }

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

	public interface onViewFriendDetail {
		public void onClickViewFriendDetail(FBUser friend);
		
		public void inviteFriendJoinSP(FBUser friend);
	}

	public void setListener(onViewFriendDetail monVieFriendDetail) {
		this.mListener = monVieFriendDetail;
	}
}
