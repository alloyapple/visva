package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.sobject.HistoryTransactionList;
import vn.com.shoppie.fragment.FeedbackFragment;
import vn.com.shoppie.fragment.FragmentPersonalInfo;
import vn.com.shoppie.fragment.FragmentSupport;
import vn.com.shoppie.fragment.HistoryTradeFragment;
import vn.com.shoppie.fragment.MainPersonalInfoFragment;
import vn.com.shoppie.fragment.MainPersonalInfoFragment.MainPersonalInfoListener;
import vn.com.shoppie.fragment.PersonalFriendFragment;
import vn.com.shoppie.fragment.PersonalFriendFragment.IOnViewFriendDetail;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.network.ParserUtility;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.object.FacebookUser;
import vn.com.shoppie.object.ShoppieUserInfo;
import vn.com.shoppie.util.FacebookUtil;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

public class PersonalInfoActivity extends FragmentActivity implements
		MainPersonalInfoListener, IOnViewFriendDetail {
	// =============================Constant Define=====================
	private static final String FB_USER_ID = "id";
	private static final String FB_USER_NAME = "name";
	private static final String FB_USER_PICTURE = "picture";
	private static final String MAIN_PERSONAL_INFO_FRAGMENT = "main_info";
	private static final String PERSONAL_FRIEND_FRAGMENT = "friend";
	private static final String HELP_FRAGMENT = "favourite";
	private static final String HISTORY_TRADE_FRAGMET = "history_trade";
	private static final String FRAGMENT_PERSONAL_INFO = "personal_info";
	private static final String FEEDBACK_FRAGMENT = "feedback";

	private static final int MAIN_PERSONAL_INFO = 1001;
	private static final int PERSONAL_FRIEND = 1002;
	private static final int HISTORY_TRADE = 1003;
	private static final int HELP = 1004;
	private static final int PERSONAL_INFO = 1005;
	private static final int FEEDBACK = 1006;
	// ===========================Control Define==================
	private FeedbackFragment mFeedbackFragment;
	private MainPersonalInfoFragment mMainPersonalInfoFragment;
	private PersonalFriendFragment mPersonalFriendFragment;
	private FragmentPersonalInfo mFragmentPersonalInfo;
	private HistoryTradeFragment mHistoryTradeFragment;
	private FragmentSupport mHelpFragment;
	private FragmentManager mFmManager;
	private FragmentTransaction mTransaction;
	private MyTextView mTxtTitle;
	// =========================Class Define ====================
	private UiLifecycleHelper lifecycleHelper;
	private ShoppieSharePref mShopieSharePref;
	// =========================Variable Define==================
	private ArrayList<String> backstack = new ArrayList<String>();
	private ArrayList<FBUser> mListFriend = new ArrayList<FBUser>();
	private int custId;
	private int isShowFavourite;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			Log.e("Session change", session.isOpened() + "-" + state.toString());
			// onSe(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_personal_info);

		isShowFavourite = getIntent().getExtras().getInt(
				GlobalValue.IS_SHOW_FAVOURITE);
		mShopieSharePref = new ShoppieSharePref(this);
		// Facebook
		lifecycleHelper = new UiLifecycleHelper(this, callback);
		lifecycleHelper.onCreate(savedInstanceState);
		initialize();

		if (mShopieSharePref.getLoginType()) {
			// Facebook
			Session session = Session.getActiveSession();
			Log.e("adfksdfjh " + (session != null && session.isOpened()),
					"asdfdfjh " + mShopieSharePref.getLoginType());
			if (session != null && session.isOpened())
				updateUserInfo();
			// lifecycleHelper = new
			// UiLifecycleHelper(PersonalInfoActivity.this,
			// new Session.StatusCallback() {
			// @Override
			// public void call(Session session, SessionState state,
			// Exception exception) {
			// // onSessionStateChange(session, state, exception);
			//
			// }
			//
			// });
			// lifecycleHelper.onCreate(savedInstanceState);
			// ensureOpenSession();
		} else {
			ShoppieUserInfo userInfo = new ShoppieUserInfo(
					mShopieSharePref.getCustName(),
					mShopieSharePref.getPhone(), ""
							+ mShopieSharePref.getCustId(),
					mShopieSharePref.getEmail(),
					mShopieSharePref.getCustAddress(),
					mShopieSharePref.getGender(),
					mShopieSharePref.getImageAvatar(),
					mShopieSharePref.getImageCover());
			mMainPersonalInfoFragment.updateShoppieUser(userInfo);
			custId = mShopieSharePref.getCustId();
			requestToUpdateUserPie("" + custId);
		}
	}

	private void requestToUpdateUserPie(String custId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.updateHistoryTransaction(custId);
		AsyncHttpPost postUpdateLuckyPie = new AsyncHttpPost(
				PersonalInfoActivity.this, new AsyncHttpResponseProcess(
						PersonalInfoActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							HistoryTransactionList historyTransactionList = gson
									.fromJson(jsonObject.toString(),
											HistoryTransactionList.class);
							mMainPersonalInfoFragment
									.updatePie(historyTransactionList);
							mHistoryTradeFragment
									.updatePie(historyTransactionList);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
						finish();
					}
				}, nameValuePairs, true);
		postUpdateLuckyPie.execute(WebServiceConfig.URL_HISTORY_TRANSACTION);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void updateUserInfo() {
		// TODO Auto-generated method stub

		Session activeSession = Session.getActiveSession();
		Log.e("adfdfh", "asdfdfk " + activeSession.getState().isOpened());
		if (activeSession.getState().isOpened()) {
			Request infoRequest = Request.newMeRequest(activeSession,
					new GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							// TODO Auto-generated method stub
							GraphObject graphObject = response.getGraphObject();
							if (graphObject != null) {
								JSONObject jsonObject = graphObject
										.getInnerJSONObject();
								Gson gson = new Gson();
								FacebookUser facebookUser = gson.fromJson(
										jsonObject.toString(),
										FacebookUser.class);
								mMainPersonalInfoFragment
										.updateUserInfo(facebookUser);
								custId = mShopieSharePref.getCustId();
								requestToUpdateUserPie("" + custId);
								getFriends();
							}
						}
					});
			Bundle params = new Bundle();
			params.putString("fields", "id, name, picture");
			infoRequest.setParameters(params);
			infoRequest.executeAsync();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		lifecycleHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		lifecycleHelper.onSaveInstanceState(bundle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		lifecycleHelper.onResume();
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());
		if (enableButtons && mShopieSharePref.getLoginToShowFriendSuccess()) {
			mShopieSharePref.setLoginToShowFriendSuccess(false);
			showFragment(PERSONAL_FRIEND);
			mPersonalFriendFragment.getFriends();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lifecycleHelper.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		lifecycleHelper.onPause();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		mFmManager = getSupportFragmentManager();
		mMainPersonalInfoFragment = (MainPersonalInfoFragment) mFmManager
				.findFragmentById(R.id.layout_personal_main_info);
		mPersonalFriendFragment = (PersonalFriendFragment) mFmManager
				.findFragmentById(R.id.layout_personal_friend);
		mHelpFragment = (FragmentSupport) mFmManager
				.findFragmentById(R.id.layout_personal_favourite);
		mFragmentPersonalInfo = (FragmentPersonalInfo) mFmManager
				.findFragmentById(R.id.layout_personal_info_fragment);
		mHistoryTradeFragment = (HistoryTradeFragment) mFmManager
				.findFragmentById(R.id.layout_personal_history_trade);
		mFeedbackFragment = (FeedbackFragment) mFmManager
				.findFragmentById(R.id.layout_feedback_fragment);

		mTxtTitle = (MyTextView) findViewById(R.id.txt_title_fragment);
		mTxtTitle.setLight();

		mMainPersonalInfoFragment.setListener(this);
		mPersonalFriendFragment.setListener(this);

		mMainPersonalInfoFragment.setShowFavouriteProduct(isShowFavourite);
		if (isShowFavourite < 2) {
			showFragment(MAIN_PERSONAL_INFO);
			mTransaction = hideFragment();
			mTransaction.show(mMainPersonalInfoFragment);
			addToSBackStack(MAIN_PERSONAL_INFO_FRAGMENT);
		} else {
			showFragment(PERSONAL_FRIEND);
			mTransaction = hideFragment();
			mTransaction.show(mPersonalFriendFragment);
			addToSBackStack(PERSONAL_FRIEND_FRAGMENT);
		}
		mTransaction.commit();
	}

	private void showFragment(int fragment) {
		// TODO Auto-generated method stub
		switch (fragment) {

		case MAIN_PERSONAL_INFO:
			mTransaction = hideFragment();
			mTransaction.show(mMainPersonalInfoFragment);
			addToSBackStack(MAIN_PERSONAL_INFO_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personal_info));
			break;

		case PERSONAL_FRIEND:
			mTransaction = hideFragment();
			mTransaction.show(mPersonalFriendFragment);
			addToSBackStack(PERSONAL_FRIEND_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personl_friend));
			break;

		case HISTORY_TRADE:
			mTransaction = hideFragment();
			mTransaction.show(mHistoryTradeFragment);
			addToSBackStack(HISTORY_TRADE_FRAGMET);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.history_trade));
			break;

		case HELP:
			Log.e("asdfdfjh", "adfkdh ");
			mTransaction = hideFragment();
			mTransaction.show(mHelpFragment);
			addToSBackStack(HELP_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personal_help));
			break;

		case PERSONAL_INFO:
			mTransaction = hideFragment();
			mTransaction.show(mFragmentPersonalInfo);
			addToSBackStack(FRAGMENT_PERSONAL_INFO);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.main_personal_info));
			break;
		case FEEDBACK:
			mTransaction = hideFragment();
			mTransaction.show(mFeedbackFragment);
			mFeedbackFragment.updateUI();
			addToSBackStack(FEEDBACK_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.feedback));
			break;
		default:
			break;
		}
	}

	public FragmentTransaction hideFragment() {
		mTransaction = mFmManager.beginTransaction();
		mTransaction.hide(mMainPersonalInfoFragment);
		mTransaction.hide(mPersonalFriendFragment);
		mTransaction.hide(mHelpFragment);
		mTransaction.hide(mFragmentPersonalInfo);
		mTransaction.hide(mHistoryTradeFragment);
		mTransaction.hide(mFeedbackFragment);
		// if (!backstack.isEmpty())
		// showToast(backstack.get(0));
		return mTransaction;
	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(PersonalInfoActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public void onClickBackPersonal(View v) {
		/*
		 * if (backstack.size() == 0) { if(mTopPanel.isOpen()){
		 * mTopPanel.setOpen(false, true); return; } super.onBackPressed();
		 * return; } if (backstack.size() == 1) { if
		 * (!backstack.get(0).equals(VIEW_HOME)) { showToast(backstack.get(0));
		 * mTransaction = hideFragment(); mTransaction.show(mFmHome);
		 * backstack.clear(); addToSBackStack(VIEW_HOME); mFmHome.refreshUI();
		 * mTransaction.commitAllowingStateLoss(); } else {
		 * if(mTopPanel.isOpen()){ mTopPanel.setOpen(false, true); return; }
		 * super.onBackPressed(); backstack.clear(); } return; }
		 */
		try {
			backstack.remove(backstack.size() - 1);
			if (backstack.size() == 0) {
				super.onBackPressed();
				return;
			}
		} catch (IndexOutOfBoundsException e) {
			super.onBackPressed();
			return;
		}
		String currentView = backstack.get(backstack.size() - 1);
		mTransaction = hideFragment();
		if (currentView.equals(MAIN_PERSONAL_INFO_FRAGMENT)) {
			mTransaction.show(mMainPersonalInfoFragment);
			mTxtTitle.setText(getString(R.string.personal_info));
			// mMainPersonalInfoFragment.refreshUI();
		} else if (currentView.equals(PERSONAL_FRIEND_FRAGMENT)) {
			mTransaction.show(mPersonalFriendFragment);
			mTxtTitle.setText(getString(R.string.personl_friend));
			// mPersonalFriendFragment.
		} else if (currentView.equals(HELP_FRAGMENT)) {
			mTransaction.show(mHelpFragment);
			mTxtTitle.setText(getString(R.string.personal_favourite));
			// mPersonalFriendFragment.
		} else if (currentView.equals(HISTORY_TRADE_FRAGMET)) {
			mTransaction.show(mHistoryTradeFragment);
			mTxtTitle.setText(getString(R.string.history_trade));
			// mPersonalFriendFragment.
		} else if (currentView.equals(FRAGMENT_PERSONAL_INFO)) {
			mTransaction.show(mFragmentPersonalInfo);
			mTxtTitle.setText(getString(R.string.main_personal_info));
		} else if (currentView.equals(FEEDBACK_FRAGMENT)) {
			mTransaction.show(mFeedbackFragment);
			mTxtTitle.setText(getString(R.string.feedback));
		}
		mTransaction.commitAllowingStateLoss();
	}

	public void addToSBackStack(String tag) {
		int index = backstack.lastIndexOf(tag);
		if (index == -1) {
			backstack.add(tag);
			return;
		}
		try {
			if (!backstack.get(index - 1).equals(
					backstack.get(backstack.size() - 1))) {
				backstack.add(tag);
				return;
			}
		} catch (IndexOutOfBoundsException e) {

		}
		try {
			ArrayList<String> subStack = new ArrayList<String>(backstack);
			for (int i = 0; i < subStack.size(); i++) {
				if (i > index) {
					backstack.remove(index);
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@Override
	public void onBackPressed() {

		/*
		 * if (backstack.size() == 0) { if(mTopPanel.isOpen()){
		 * mTopPanel.setOpen(false, true); return; } super.onBackPressed();
		 * return; } if (backstack.size() == 1) { if
		 * (!backstack.get(0).equals(VIEW_HOME)) { showToast(backstack.get(0));
		 * mTransaction = hideFragment(); mTransaction.show(mFmHome);
		 * backstack.clear(); addToSBackStack(VIEW_HOME); mFmHome.refreshUI();
		 * mTransaction.commitAllowingStateLoss(); } else {
		 * if(mTopPanel.isOpen()){ mTopPanel.setOpen(false, true); return; }
		 * super.onBackPressed(); backstack.clear(); } return; }
		 */
		try {
			backstack.remove(backstack.size() - 1);
			if (backstack.size() == 0) {
				super.onBackPressed();
				return;
			}
		} catch (IndexOutOfBoundsException e) {
			super.onBackPressed();
			return;
		}
		String currentView = backstack.get(backstack.size() - 1);
		mTransaction = hideFragment();
		if (currentView.equals(MAIN_PERSONAL_INFO_FRAGMENT)) {
			mTransaction.show(mMainPersonalInfoFragment);
			mTxtTitle.setText(getString(R.string.personal_info));
			// mMainPersonalInfoFragment.refreshUI();
		} else if (currentView.equals(PERSONAL_FRIEND_FRAGMENT)) {
			mTransaction.show(mPersonalFriendFragment);
			mTxtTitle.setText(getString(R.string.personl_friend));
			// mPersonalFriendFragment.
		} else if (currentView.equals(HELP_FRAGMENT)) {
			mTransaction.show(mHelpFragment);
			mTxtTitle.setText(getString(R.string.personal_favourite));
			// mPersonalFriendFragment.
		} else if (currentView.equals(HISTORY_TRADE_FRAGMET)) {
			mTransaction.show(mHistoryTradeFragment);
			mTxtTitle.setText(getString(R.string.history_trade));
			// mPersonalFriendFragment.
		} else if (currentView.equals(FRAGMENT_PERSONAL_INFO)) {
			mTransaction.show(mFragmentPersonalInfo);
			mTxtTitle.setText(getString(R.string.main_personal_info));
		} else if (currentView.equals(FEEDBACK_FRAGMENT)) {
			mTransaction.show(mFeedbackFragment);
			mTxtTitle.setText(getString(R.string.feedback));
		}
		mTransaction.commitAllowingStateLoss();
	}

	@Override
	public void onClickPersonalInfo() {
		// TODO Auto-generated method stub
		showFragment(PERSONAL_INFO);
	}

	@Override
	public void onClickHelp() {
		// TODO Auto-generated method stub
		showFragment(HELP);
	}

	@Override
	public void onClickHistoryTrade() {
		// TODO Auto-generated method stub
		showFragment(HISTORY_TRADE);
	}

	@Override
	public void onClickFriend() {
		// TODO Auto-generated method stub
		showFragment(PERSONAL_FRIEND);
		mPersonalFriendFragment.getFriends();
	}

	@Override
	public void onClickFeedback() {
		// TODO Auto-generated method stub
		showFragment(FEEDBACK);
	}

	@Override
	public void onClickViewFriendDetail(FBUser friend) {
		// TODO Auto-generated method stub
		// showFragment(FRIEND_DETAIL);
		// mFriendDetailFragment.updateUser(friend);
	}

	@Override
	public void inviteFriendJoinSP(FBUser friend) {
		// TODO Auto-generated method stub
		FacebookUtil.getInstance(PersonalInfoActivity.this).publishFeedDialog(
				"" + mShopieSharePref.getCustId(), friend);
	}

	private void getFriends() {
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

									FBUser fbUser = new FBUser(
											PersonalInfoActivity.this,
											userName, url, isJoinSP, numberPie,
											userId);
									mListFriend.add(fbUser);
								}
							/* update list facebook friend */
							mMainPersonalInfoFragment
									.updateNumberFriend(mListFriend.size());
						}
					});
			Bundle params = new Bundle();
			params.putString("fields", "id, name, picture");
			friendRequest.setParameters(params);
			friendRequest.executeAsync();
		}
	}
}
