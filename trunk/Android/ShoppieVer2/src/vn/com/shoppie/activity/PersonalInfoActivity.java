package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.ListFBFriendAdapter.InviteFriendJoinSPInterface;
import vn.com.shoppie.constant.ShopieSharePref;
import vn.com.shoppie.database.sobject.HistoryTransactionList;
import vn.com.shoppie.fragment.FragmentPersonalInfo;
import vn.com.shoppie.fragment.FriendDetailFragment;
import vn.com.shoppie.fragment.HelpFragment;
import vn.com.shoppie.fragment.HistoryTradeFragment;
import vn.com.shoppie.fragment.MainPersonalInfoFragment;
import vn.com.shoppie.fragment.MainPersonalInfoFragment.MainPersonalInfoListener;
import vn.com.shoppie.fragment.PersonalFriendFragment;
import vn.com.shoppie.fragment.PersonalFriendFragment.onViewFriendDetail;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.object.FacebookUser;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

public class PersonalInfoActivity extends FragmentActivity implements
		MainPersonalInfoListener, onViewFriendDetail
		 {
	// ==========================Constant Define=================
	private static final String MAIN_PERSONAL_INFO_FRAGMENT = "main_info";
	private static final String PERSONAL_FRIEND_FRAGMENT = "friend";
	private static final String HELP_FRAGMENT = "favourite";
	private static final String HISTORY_TRADE_FRAGMET = "history_trade";
	private static final String FRAGMENT_PERSONAL_INFO = "personal_info";
	private static final String FRIEND_DETAIL_FRAGMENT = "friend_detail";
	private static final int MAIN_PERSONAL_INFO = 1001;
	private static final int PERSONAL_FRIEND = 1002;
	private static final int HISTORY_TRADE = 1003;
	private static final int HELP = 1004;
	private static final int PERSONAL_INFO = 1005;
	private static final int FRIEND_DETAIL = 1006;
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";

	// ===========================Control Define==================
	private MainPersonalInfoFragment mMainPersonalInfoFragment;
	private PersonalFriendFragment mPersonalFriendFragment;
	private FragmentPersonalInfo mFragmentPersonalInfo;
	private HistoryTradeFragment mHistoryTradeFragment;
	private HelpFragment mHelpFragment;
	private FriendDetailFragment mFriendDetailFragment;
	private FragmentManager mFmManager;
	private FragmentTransaction mTransaction;
	private TextView mTxtTitle;
	// =========================Class Define ====================
	private UiLifecycleHelper lifecycleHelper;
	private ShopieSharePref mShopieSharePref;
	// =========================Variable Define==================
	private ArrayList<String> backstack = new ArrayList<String>();
	private int custId;
	private boolean pendingPublishReauthorization = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Facebook
		lifecycleHelper = new UiLifecycleHelper(PersonalInfoActivity.this,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						// onSessionStateChange(session, state, exception);
						updateUserInfo();
					}

				});
		lifecycleHelper.onCreate(savedInstanceState);
		// ensureOpenSession();

		mShopieSharePref = new ShopieSharePref(this);
		setContentView(R.layout.page_personal_info);

		initialize();

	}

	// private boolean ensureOpenSession() {
	// Log.e("addfjh", "adkjdfh "
	// + (Session.getActiveSession() == null || !Session
	// .getActiveSession().isOpened()));
	// if (Session.getActiveSession() == null
	// || !Session.getActiveSession().isOpened()) {
	// Session.openActiveSession(PersonalInfoActivity.this, true,
	// new Session.StatusCallback() {
	// @Override
	// public void call(Session session, SessionState state,
	// Exception exception) {
	// Log.e("run ládhf", "sđfjhh ");
	// onSessionStateChange(session, state, exception);
	// }
	// });
	// return false;
	// }
	// return true;
	// }

	private void requestToUpdateUserPie(String custId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
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

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUserInfo();
	}

	private void updateUserInfo() {
		// TODO Auto-generated method stub
		Session activeSession = Session.getActiveSession();
		Log.e("ddfjh", "asddfdfkjfd " + activeSession.getState().isOpened());
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
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// private void onSessionStateChange(final Session session,
	// SessionState state, Exception exception) {
	// if (session != null && session.isOpened()) {
	// Request request = Request.newMeRequest(session,
	// new Request.GraphUserCallback() {
	// @Override
	// public void onCompleted(GraphUser user,
	// Response response) {
	// if (session == Session.getActiveSession()) {
	// if (user != null) {
	//
	// }
	// }
	// }
	// });
	// request.executeAsync();
	// }
	//
	// }

	private void initialize() {
		// TODO Auto-generated method stub
		mFmManager = getSupportFragmentManager();
		mMainPersonalInfoFragment = (MainPersonalInfoFragment) mFmManager
				.findFragmentById(R.id.layout_personal_main_info);
		mPersonalFriendFragment = (PersonalFriendFragment) mFmManager
				.findFragmentById(R.id.layout_personal_friend);
		mHelpFragment = (HelpFragment) mFmManager
				.findFragmentById(R.id.layout_personal_favourite);
		mFragmentPersonalInfo = (FragmentPersonalInfo) mFmManager
				.findFragmentById(R.id.layout_personal_info_fragment);
		mHistoryTradeFragment = (HistoryTradeFragment) mFmManager
				.findFragmentById(R.id.layout_personal_history_trade);
		mFriendDetailFragment = (FriendDetailFragment) mFmManager
				.findFragmentById(R.id.layout_personal_friend_detail);

		mTxtTitle = (TextView) findViewById(R.id.txt_title_fragment);
		mMainPersonalInfoFragment.setListener(this);
		mPersonalFriendFragment.setListener(this);

		showFragment(MAIN_PERSONAL_INFO);
		mTransaction = hideFragment();
		mTransaction.show(mMainPersonalInfoFragment);
		addToSBackStack(MAIN_PERSONAL_INFO_FRAGMENT);
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
		case FRIEND_DETAIL:
			mTransaction = hideFragment();
			mTransaction.show(mFriendDetailFragment);
			addToSBackStack(FRIEND_DETAIL_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personl_friend));
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
		mTransaction.hide(mFriendDetailFragment);
		if (!backstack.isEmpty())
			showToast(backstack.get(0));
		return mTransaction;
	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(PersonalInfoActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public void onClickBackPersonal(View v) {
		finish();
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
		} else if (currentView.equals(FRIEND_DETAIL_FRAGMENT)) {
			mTransaction.show(mFriendDetailFragment);
			mTxtTitle.setText(getString(R.string.personl_friend));
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
		
	}

	@Override
	public void onClickViewFriendDetail(FBUser friend) {
		// TODO Auto-generated method stub
		showFragment(FRIEND_DETAIL);
		mFriendDetailFragment.updateUser(friend);
	}




	private void inviteFBFriendJoinSP(FBUser friend) {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
//			List<String> permissions = session.getPermissions();
//			if (!isSubsetOf(PERMISSIONS, permissions)) {
//				pendingPublishReauthorization = true;
//				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
//						this, PERMISSIONS);
//				session.requestNewPublishPermissions(newPermissionsRequest);
//				return;
//			}

			Bundle postParams = new Bundle();
			postParams.putString("name", "Shoppie");
			postParams.putString("caption", "");
			postParams.putString("description", getString(R.string.invitation_content,mShopieSharePref.getCustId()));
			postParams
					.putString(
							"link",
							"http://www.shoppie.com.vn/");
			postParams
					.putString("picture",
							"http://farm4.staticflickr.com/3736/10907501955_f8724f3633_m.jpg");
			//http://radio.vnmedia.vn/Uploads/Images/xcan-02-on%20rainy%20days.jpg
			//	http://farm4.staticflickr.com/3736/10907501955_f8724f3633_m.jpg
			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					// JSONObject graphResponse = response.getGraphObject()
					// .getInnerJSONObject();
					// String postId = null;
					// try {
					// postId = graphResponse.getString("id");
					// } catch (JSONException e) {
					// Log.i("dfjdhf", "JSON error " + e.getMessage());
					// }
					// FacebookRequestError error = response.getError();
					// if (error != null) {
					// Toast.makeText(context, error.getErrorMessage(),
					// Toast.LENGTH_SHORT).show();
					// } else {
					// Toast.makeText(context, postId, Toast.LENGTH_LONG)
					// .show();
					// }
					Log.e("adkdfj", "ienjdh " + response.toString());
				}
			};

			Request request = new Request(session, friend.getUserId()+"/feed", postParams,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}

	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void inviteFriendJoinSP(FBUser friend) {
		// TODO Auto-generated method stub
		inviteFBFriendJoinSP(friend);
	}
}
