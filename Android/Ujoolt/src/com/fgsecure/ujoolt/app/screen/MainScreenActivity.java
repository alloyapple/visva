package com.fgsecure.ujoolt.app.screen;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fgsecure.ujoolt.app.CommonUtilities;
import com.fgsecure.ujoolt.app.MessageBox;
import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.adapter.Rejolter;
import com.fgsecure.ujoolt.app.adapter.RejolterItem;
import com.fgsecure.ujoolt.app.camera.galaxys.MyMediaConnectorClientS;
import com.fgsecure.ujoolt.app.camera.other.DgCamActivity;
import com.fgsecure.ujoolt.app.camera.util.VideoSaveSdCard;
import com.fgsecure.ujoolt.app.define.Filter;
import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.define.ModeScreen;
import com.fgsecure.ujoolt.app.define.RequestCode;
import com.fgsecure.ujoolt.app.define.ResultCode;
import com.fgsecure.ujoolt.app.define.Search;
import com.fgsecure.ujoolt.app.define.Step;
import com.fgsecure.ujoolt.app.facebook.AsyncFacebookRunner;
import com.fgsecure.ujoolt.app.facebook.Facebook;
import com.fgsecure.ujoolt.app.facebook.FacebookConnector;
import com.fgsecure.ujoolt.app.facebook.FacebookConstant;
import com.fgsecure.ujoolt.app.facebook.SessionEvents;
import com.fgsecure.ujoolt.app.facebook.SessionEvents.AuthListener;
import com.fgsecure.ujoolt.app.facebook.SessionEvents.LogoutListener;
import com.fgsecure.ujoolt.app.facebook.SessionStore;
import com.fgsecure.ujoolt.app.facebook.UtilityFacebook;
import com.fgsecure.ujoolt.app.info.UjooltSharedPreferences;
import com.fgsecure.ujoolt.app.info.WebServiceConfig;
import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.json.UserSync;
import com.fgsecure.ujoolt.app.network.AsyncHttpGet;
import com.fgsecure.ujoolt.app.network.AsyncHttpResponseProcess;
import com.fgsecure.ujoolt.app.screen.MyOverlay.OnMapMoveListener;
import com.fgsecure.ujoolt.app.twitter.TwitterConnector;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;
import com.fgsecure.ujoolt.app.utillity.DeviceConfig;
import com.fgsecure.ujoolt.app.utillity.GoogleLocation;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;
import com.fgsecure.ujoolt.app.utillity.Language;
import com.fgsecure.ujoolt.app.utillity.NetworkUtility;
import com.fgsecure.ujoolt.app.utillity.ShakeListener;
import com.fgsecure.ujoolt.app.utillity.StringUtility;
import com.fgsecure.ujoolt.app.utillity.Utility;
import com.fgsecure.ujoolt.app.view.CircleOverlay;
import com.fgsecure.ujoolt.app.view.IconView;
import com.fgsecure.ujoolt.app.view.InformationView;
import com.fgsecure.ujoolt.app.view.JoltView;
import com.fgsecure.ujoolt.app.view.LoginView;
import com.fgsecure.ujoolt.app.view.MyCustomMapView;
import com.fgsecure.ujoolt.app.view.SearchItemView;
import com.fgsecure.ujoolt.app.view.SearchView;
import com.fgsecure.ujoolt.app.view.SettingView;
import com.fgsecure.ujoolt.app.widget.DraggableView;
import com.fgsecure.ujoolt.app.widget.ImageAdapter;
import com.fgsecure.ujoolt.app.widget.imagezoom.ImageViewTouch;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

public class MainScreenActivity extends MapActivity implements View.OnFocusChangeListener,
		OnTouchListener, View.OnClickListener, OnSeekBarChangeListener, LocationListener,
		SensorEventListener, FacebookConstant {

	private final int RESULT_CLOSE_ALL = 2;
	private final int ID_NOTIFICATION = 1;
	/*
	 * the app has three modes defined in Utility class.
	 */
	public ModeScreen mode;
	private ShakeListener mShaker;
	public boolean isShowNetworkError;
	public boolean isPublicPostJolt = true;
	public static boolean isSendRegistrationId;

	// private RelativeLayout headerLayout;
	public UjooltSharedPreferences ujooltSharedPreferences;

	public FacebookConnector facebookConnector;
	public TwitterConnector twitterConnector;
	// public TwitterSession twitterSession;

	private String TAG = getClass().getSimpleName();
	private Typeface typeface;

	public ArrayList<String> arrFriendsFacebook;
	public ArrayList<String> arrFavouriteId;

	public CircleOverlay circleOverlay;

	public static String myPath;

	private ProgressDialog progressDialog;
	public ProgressBar progressBar;

	public MyCustomMapView mapView;
	public MapController mapController;

	// Array contains all the item displaying on the map
	public ArrayList<ItemizedOverlays> arrItem;

	// Objects relative GPS
	private MyLocationOverlay myLocationOverlay;
	private LocationManager locationManager;

	public int numberJoltOnScreen;
	public int numberThreadNomal;
	public int numberThreadInstagram;
	public int numberThreadFacebook;
	public int numberThreadFacebookMe;
	public int numberThreadFacebookMyEvent;

	private boolean isPush;
	private boolean isUpdateJolters;
	private boolean isGetImage, isAbout, isReport, isPause, isTakePhotoOn;

	public boolean language;
	public boolean isTapOnItem;
	public boolean isShareFB, isShareTW, isStartFacebookConnect, isStartTwitterConnect;
	public boolean isJolting, isBubbleDetail, isShowJoltSource, isSetting, isShowAvatar, isRejolt;
	public boolean isLoading, isStarted, isSearch, isStartSearch, isUpdate, isImage;
	public boolean isFirstSearch, isStartJolt, isQuitRegister;
	public boolean isBackRegister, isRegister;

	public ItemizedOverlays curItemizedOverlays;
	public JoltHolder joltHolder;

	private MyOverlay mMapOverlay;
	private Handler mHandler = new Handler();

	private ImageViewTouch imageViewAvatar;
	public RelativeLayout layoutNumberJolt;

	// public Bitmap bm_thumbnail;
	public static Bitmap bitmapJoltAvatar;
	public static Bitmap bitmapNoAvatar;

	// image of instagram url
	// public static Bitmap bm_instagram_avatar;
	public static Uri photoUri;
	// private Uri uriUpload;
	public int curLati, curLongi, mLatitudeE6, mLongitudeE6;
	public int curTapItem;
	public Jolt jolter;
	public Handler handler;

	// public boolean isBackFromNotify;

	// tutorial
	private RelativeLayout layoutTutorial;
	private ImageView imgStep1, imgStep2, imgStep3, imgStep4, imgStep5, imgStep6;
	public boolean isTutorialMode;
	public Step step;
	// tutorial

	// view login
	private Button btnCloseLogin, btnLoginFacebook, btnLoginTwitter, btnLoginUjoolt,
			btnRegisterUjoolt, btnShareFacebook, btnShareTwitter;
	private TextView lblLoginTitle, lblLoginUser, lblLoginPassword, lblLoginUjooltAccount;
	private EditText txtLoginUser, txtLoginPassword;
	public ImageView imgToungeLogin;
	public String myUserName, myLoginUserId;
	public LoginType myLoginType;
	// public String myAddress;
	public String myUserNameUjoolt, myUserIdUjoolt;
	public String myUserNameFacebook, myUserIdFacebook;
	public String myUserNameTwitter, myUserIdTwitter;
	public boolean isUjooltConnected, isFacebookConnected, isTwitterConnected;
	// view login

	// view post jolt
	private Button btnChangeMedia, btnJolt, btnCloseJoltScreen;
	private LinearLayout layoutListView;
	private ImageView imgPublic, imgPrivate;
	private TextView lblCounterCharacter;
	public static Button btnUploadPhoto;
	public EditText txtText;
	public TextView lblMyNickname;
	public ImageView imgToungeJolt;
	// view post jolt

	// view information full
	private TextView lblWhoRejolt;
	private ImageView imgFavourite, imgMagicEye;
	private Button btnNextJoltDetail, btnPreJoltDetail;
	private LinearLayout layoutInfomationFull, layoutDetailFull;
	public TextView lblDragAndReleaseToRejolt, lblLifeJolt, lblNumberRejolt, lblNick, lblIndexJolt,
			lblText, lblJoltAge;
	public ImageView imgClockAndSkull, imgPlay, imgAvatar, imgArrowLeft, imgArrowRight;
	public Button btnRejolt;
	public LinearLayout layoutShowAvatar, linearLayoutJoltDetail;
	// view information full

	// view jolt information lite
	private RelativeLayout layoutDetailLite;
	private TextView textViewJoltAgeDetailLite, textView1stHead, textView2ndHead, textView3rdHead,
			textView4thHead;
	// view jolt information lite

	// view search
	private Button btnSearch, btnDeleteText, btnSearchByLocation, btnSearchByJolt;
	private LinearLayout searchLayout;
	public Button btnGps, btnSetting, btnNumberJolt;
	public EditText txtSearchBar;
	public Search searchType;
	public ImageView imgBackGroundSearchBar;
	// view search

	// view setting
	private LinearLayout layoutLogout, linearLayoutLanguage, linearLayoutReportProblem,
			linearLayoutAbout, linearLayoutTurotial;
	private ImageView imgViewCloseSetting;
	public ToggleButton toggleButtonPush, toggleButtonShareFacebook, toggleButtonShareTwitter;
	private TextView lblSetting, lblPush, lblShareFacebook, lblShareTwitter, lblAbout, lblTutorial,
			lblLanguage, lblReportProblem, lblLogout;
	public TextView lblUserNameFacebookSettingView, lblUserNameTwitterSettingView;
	// view setting

	// view filter
	private LinearLayout layoutInactiveFilter;
	private LinearLayout layoutActiveFilter, layoutFilterMine, layoutFilterRecent,
			layoutFilterSocial, layoutFilterFacebook, layoutFilterInstagram, layoutFilterFavourite,
			layoutFilterTop;
	private ImageView imgCloseFilter;
	private ImageView imgFilterMine, imgFilterRecent, imgFilterSocial, imgFilterFacebook,
			imgFilterInstagram, imgFilterFavourite, imgFilterTop;
	private TextView lblFilterActive, lblFilterInactive, lblFilterMine, lblFilterRecent,
			lblFilterFavourite;
	private boolean isFilterRecent, isFilterSocial, isFilterFacebook, isFilterInstagram,
			isFilterFavourite, isFilterTop;
	public boolean isFilterMine;
	// view filter

	// custom views
	public LoginView viewLogin;
	public JoltView viewJolt;
	public SettingView viewSetting;
	public InformationView viewInformation;
	public SearchView viewSearch;
	public SearchItemView viewSearchItem;
	public ImageAdapter imageAdapter;
	// custom views

	// sensor
	private SensorManager sensorManager = null;
	private int orientation, degrees = -1;
	// sensor

	private Runnable updateJoltTask = new Runnable() {

		@Override
		public void run() {
			try {
				if (isUpdateJolters) {
					isUpdateJolters = false;

					Log.i("isUpdateJolters", "= " + isUpdateJolters);
					Log.e("start to load jolts", "ok");

					curLati = mLatitudeE6;
					curLongi = mLongitudeE6;

					// getLocationName(((double) mLatitudeE6) / 1E6, ((double)
					// mLongitudeE6) / 1E6);

					posisionLast_UpdateJoltInstagram = new GeoPoint(mLatitudeE6, mLongitudeE6);
					posisionLast_UpdateJoltUjolt = new GeoPoint(mLatitudeE6, mLongitudeE6);
					posisionLast_UpdateJoltFacebook = new GeoPoint(mLatitudeE6, mLongitudeE6);

					// joltHolder.setCoordinates(mLatitudeE6, mLongitudeE6);

					if (checkLogin()) {
						joltHolder.getFavouriteFromUser(new GeoPoint(curLati, curLongi),
								ConfigUtility.getCurTimeStamp(), true, 6000, "",
								JoltHolder.GET_DEFAULT, false);

					} else {
						joltHolder.getAllJoltsFromLocation(curLati, curLongi,
								ConfigUtility.getCurTimeStamp(), true, 6000, "",
								JoltHolder.GET_DEFAULT, false);
					}

					if (isFilterInstagram) {
						joltHolder.getAllJoltFromInstagram(curLati, curLongi,
								JoltHolder.GET_DEFAULT, 5000);
					}

					if (myLoginType == null) {
						myLoginType = LoginType.NONE;
					}

					if (isFilterFacebook && myLoginType == LoginType.FACEBOOK) {
						joltHolder.getAllJoltFromFacebook(new GeoPoint(curLati, curLongi));
						joltHolder.getAllJoltOfMeFromFacebook();
						joltHolder.getAllJoltOfMyEventFromFacebook();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * Runnable this is running when app start not full
			 */
			// Log.v("APP Running", "NOT FULL");
			if (joltHolder.arrAvailableJolt.size() == 0)
				handler.postDelayed(updateJoltTask, 250);
			else {
				/*
				 * Stop this when connected to server UJoolt
				 */
				handler.removeCallbacksAndMessages(null);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		isShowNetworkError = false;
		ujooltSharedPreferences = new UjooltSharedPreferences(this);

		language = ujooltSharedPreferences.getLanguage();
		Language.setLanguage(language);
		super.onCreate(savedInstanceState);
		if (!NetworkUtility.getInstance(this).isNetworkAvailable() && !isShowNetworkError) {
			showDialogNetworkError();
		}

		ConfigUtility.getConfig(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.page_main_screen);

		bitmapNoAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
		arrFavouriteId = new ArrayList<String>();
		arrFriendsFacebook = new ArrayList<String>();

		DeviceConfig.getDeviceInfo(this);
		// Getting the sensor service.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// Register this class as a listener for the accelerometer sensor
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		// get resolution of device

		typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Bold.ttf");
		bitmapJoltAvatar = null;
		isStarted = false;

		// myAddress = "";// ujooltSharedPreferences.getUserAddress();

		if (!ujooltSharedPreferences.getFirstInstallStatus()) {
			isTutorialMode = true;
			ujooltSharedPreferences.putFirstIstallStatus();
		}
		hideTutorial();

		// hideTutorial();

		// checkLoginStatus();

		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			public void onShake() {
				if (!isShowJoltSource && !isSetting && (mode != ModeScreen.SEARCH) && !isShowAvatar
						&& toggleButtonPush.isChecked()) {
					if (viewLogin.isLogin) {
						if (!viewLogin.isLoginning) {
							viewSearch.disappear(SearchView.REGISTER);
							imgToungeLogin.setOnTouchListener(null);
							viewLogin.setVisibility(View.VISIBLE);
							viewLogin.isStarted = true;
							viewLogin.isMoveUp = true;
							viewLogin.isMoveDown = false;
							viewLogin.isAppear = false;
							viewLogin.isDisapear = false;
							viewLogin.setOnTouchListener(MainScreenActivity.this);
							viewInformation.disappear(InformationView.REGISTER);
							viewLogin.isLoginning = true;
						}
					} else {
						if (!isJolting) {
							viewSearch.disappear(SearchView.JOLT);
							imgToungeJolt.setOnTouchListener(null);
							viewJolt.setVisibility(View.VISIBLE);
							viewJolt.isStarted = true;
							viewJolt.isMoveUp = true;
							viewJolt.isMoveDown = false;
							viewJolt.setOnTouchListener(MainScreenActivity.this);
							viewInformation.disappear(InformationView.JOLT);
							isJolting = true;
						}
					}
				}
			}
		});

		initIconJolt();
		isStart = true;
		isUpdateJolters = true;
		isSendRegistrationId = false;
		numberThreadNomal = 0;
		numberThreadFacebook = 0;
		numberThreadFacebookMe = 0;
		numberThreadFacebookMyEvent = 0;
		numberThreadInstagram = 0;
		numberDisConnectInstagram = 0;
		isPressHeaderBubbleDetail = false;

		isFilterMine = true;
		isFilterFacebook = true;
		isFilterInstagram = true;

		isFilterRecent = false;
		isFilterSocial = false;
		isFilterFavourite = false;
		isFilterTop = false;

		// Set up location manager for determining present location of phone
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		Log.e(TAG, "widthe " + width + ":height " + height);
	}

	@Override
	protected void onStop() {
		Log.e("stop", "ok");
		super.onStop();

		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
	}

	@Override
	protected void onPause() {
		Log.e("pause", "ok");
		isPause = true;
		mShaker.pause();
		super.onPause();
		handlerUpdateAllWithCycleTime.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	public static boolean isStart;

	@Override
	protected void onResume() {
		super.onResume();

		numberThreadFacebook = 0;
		numberThreadFacebookMe = 0;
		numberThreadFacebookMyEvent = 0;

		if (photoUri != null) {
			scanSdCardPhoto(photoUri);
		}

		if (mShaker != null) {
			mShaker.resume();
		}

		if (!isGetImage && !isAbout && !isReport && !isPause) {
			init();

			if (facebookConnector == null) {
				facebookConnector = new FacebookConnector(FACEBOOK_APPID, this, FACEBOOK_PERMISSION);
			}

			if (twitterConnector == null) {
				twitterConnector = new TwitterConnector(this);
			}

			checkLoginStatus();

			locationManager.removeUpdates(this);
			myLocationOverlay.enableMyLocation();

			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
					MainScreenActivity.this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1,
					MainScreenActivity.this);

			if (mLatitudeE6 == 0 && mLongitudeE6 == 0) {
				Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (loc == null) {
					loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				mLatitudeE6 = (int) (loc.getLatitude() * 1E6);
				mLongitudeE6 = (int) (loc.getLongitude() * 1E6);
			}

			handler.removeCallbacksAndMessages(null);
			handler.postAtTime(updateJoltTask, SystemClock.uptimeMillis() + 200);

		} else {
			if (isPause) {
				getLocation();
				isPause = false;
			}
			if (isGetImage)
				isGetImage = false;
			if (isAbout)
				isAbout = false;
			if (isReport)
				isReport = false;
		}

		if (!isSendRegistrationId && !CommonUtilities.isRegistrationNull()) {
			postRegistrationId();
			isSendRegistrationId = true;
		}

		/*
		 * Start runnable update jolt with cycle time
		 */

		if (isStart) {
			time = 300000;
		} else {
			time = 400;
		}

		handlerUpdateAllWithCycleTime.removeCallbacksAndMessages(null);

		Log.i("**************************", "***********************");
		Log.e(": START AUTO UPDATE JOLT", "WITH CYCLE TIME");
		Log.i("**************************", "***********************");

		handlerUpdateAllWithCycleTime.postDelayed(runnable_Update_AllJolt, time);

		isStart = false;

		onClickGPS();
	}

	public int time = 0;

	public void checkLoginStatus() {
		if (ujooltSharedPreferences.getLoginStatus()) {
			viewLogin.isLogin = false;
			viewLogin.isLogined = true;
			viewLogin.isLoginning = false;
			viewLogin.setVisibility(View.INVISIBLE);
			viewLogin.disappear();
			imgToungeLogin.setOnTouchListener(null);
			viewJolt.setVisibility(View.VISIBLE);

			setMainAccount(ujooltSharedPreferences.getMainUserName(),
					ujooltSharedPreferences.getMainUserId(),
					ujooltSharedPreferences.getMainUserType());

			UserSync.IdUjoolt = ujooltSharedPreferences.getUserUjoolt().getUserId();

			myUserNameFacebook = ujooltSharedPreferences.getUserNameFacebook();
			myUserIdFacebook = ujooltSharedPreferences.getUserIdFacebook();
			UserSync.IdFacebook = myUserIdFacebook;
			UtilityFacebook.mFacebook.setAccessToken(ujooltSharedPreferences
					.getAccessTokenFacebook());

			myUserNameTwitter = ujooltSharedPreferences.getUserNameTwitter();
			myUserIdTwitter = ujooltSharedPreferences.getUserIdTwitter();
			UserSync.IdTwitter = myUserIdTwitter;

			if (myLoginType == LoginType.FACEBOOK) {
				isFacebookConnected = true;
				isTwitterConnected = false;
				isStartFacebookConnect = true;
				// UtilityFacebook.mFacebook.setAccessToken(ujooltSharedPreferences.getAccessTokenFacebook());
				facebookConnector.getFacebookFriend();

			} else if (myLoginType == LoginType.TWITTER) {
				// twitterConnector.mTwitterAuthen.mAccessToken =
				// twitterSession.getAccessToken();
				twitterConnector.mTwitterAuthen.configureToken();
			}

			lblUserNameFacebookSettingView.setText(myUserNameFacebook);
			lblUserNameTwitterSettingView.setText(myUserNameTwitter);

			setIconSocialNetwork();
		}
	}

	private void init() {
		joltHolder = new JoltHolder(this);
		progressBar = (ProgressBar) findViewById(R.id.progressbar_id);
		arrItem = new ArrayList<ItemizedOverlays>();
		handler = new Handler();

		initTutorial();
		initSearchView();
		initLoginView();
		initInformationFull();
		initInformationLite();
		initFilterView();
		initSettingView();
		initViewPostJolt();
		initCustomView();

		setLanguage(language);

		isQuitRegister = false;
		isStartJolt = false;
		isBackRegister = false;

		isLoading = false;
		isStartFacebookConnect = false;
		isStartTwitterConnect = false;
		isFacebookConnected = false;
		isTwitterConnected = false;
		OnMapMoveListener mapListener = new OnMapMoveListener() {
			public void mapMovingFinishedEvent() {
			}
		};
		mMapOverlay = new MyOverlay(mapListener, this);
		mMapOverlay.setOnTapListener(new MapViewTapListener());

		mapView = (MyCustomMapView) findViewById(R.id.mapview);
		mapView.setTraffic(true);
		mapView.setMainActivity(this);
		mapView.getOverlays().add(mMapOverlay);
		mapView.setOnChangeListener(new MapViewChangeListener());

		mapController = mapView.getController();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabledGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean enableWF = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!enabledGPS && !enableWF) {
			showDialogRequestGPS();
		} else {
			startGPS();
		}

		showNotification();

		/*
		 * Info heap size Luong -add
		 */
		// actMgr = (ActivityManager)
		// getSystemService(Context.ACTIVITY_SERVICE);
		// minfo = new ActivityManager.MemoryInfo();
		// actMgr.getMemoryInfo(minfo);

		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			public void onShake() {
				if (!isShowJoltSource && !isSetting && (mode != ModeScreen.SEARCH) && !isShowAvatar) {
					if (viewLogin.isLogin) {
						if (!viewLogin.isLoginning) {
							viewSearch.disappear(SearchView.REGISTER);
							imgToungeLogin.setOnTouchListener(null);
							viewLogin.setVisibility(View.VISIBLE);
							viewLogin.isStarted = true;
							viewLogin.isMoveUp = true;
							viewLogin.isMoveDown = false;
							viewLogin.isAppear = false;
							viewLogin.isDisapear = false;
							viewLogin.setOnTouchListener(MainScreenActivity.this);
							viewInformation.disappear(InformationView.REGISTER);
							viewLogin.isLoginning = true;
						}
					} else {
						if (!isJolting) {
							viewSearch.disappear(SearchView.JOLT);
							imgToungeJolt.setOnTouchListener(null);
							viewJolt.setVisibility(View.VISIBLE);
							viewJolt.isStarted = true;
							viewJolt.isMoveUp = true;
							viewJolt.isMoveDown = false;
							viewJolt.setOnTouchListener(MainScreenActivity.this);
							viewInformation.disappear(InformationView.JOLT);
							isJolting = true;
						}
					}
				}
			}
		});

		// reset Icon and Item after 10 minutes
		// UpdateItem();

		// facebook
		if (facebookConnector == null) {
			facebookConnector = new FacebookConnector(FACEBOOK_APPID, this, FACEBOOK_PERMISSION);
		}

		UtilityFacebook.mFacebook = new Facebook(FACEBOOK_APPID, this);
		UtilityFacebook.mAsyncRunner = new AsyncFacebookRunner(UtilityFacebook.mFacebook);
		SessionStore.restore(UtilityFacebook.mFacebook, getApplicationContext());
		SessionListener mSessionListener = new SessionListener();
		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
	}

	public void getLocation() {
		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		boolean enabledGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean enableWF = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!enabledGPS && !enableWF) {
			showDialogRequestGPS();
		} else {
			startGPS();
		}
	}

	private void initViewPostJolt() {
		// layoutPostJolt = (RelativeLayout) findViewById(R.id.layoutPostJolt);
		// layoutPostJolt.setOnClickListener(this);
		imgPublic = (ImageView) findViewById(R.id.imgPublic);
		imgPublic.setOnClickListener(this);

		imgPrivate = (ImageView) findViewById(R.id.imgPrivate);
		imgPrivate.setOnClickListener(this);

		btnJolt = (Button) findViewById(R.id.button_jolt);
		btnJolt.setOnClickListener(this);

		btnCloseJoltScreen = (Button) findViewById(R.id.btnCloseJoltScreen);
		btnCloseJoltScreen.setOnClickListener(this);

		btnUploadPhoto = (Button) findViewById(R.id.button_capture);
		btnUploadPhoto.setOnClickListener(this);

		btnChangeMedia = (Button) findViewById(R.id.edit_photo_upload);
		btnChangeMedia.setOnClickListener(this);

		btnShareFacebook = (Button) findViewById(R.id.btnShareFacebookLoginView);
		btnShareFacebook.setOnClickListener(this);

		btnShareTwitter = (Button) findViewById(R.id.btnShareTwitterLoginView);
		btnShareTwitter.setOnClickListener(this);

		btnRejolt = (Button) findViewById(R.id.button_rejoolt);
		btnRejolt.setOnClickListener(this);

		lblMyNickname = (TextView) findViewById(R.id.text_nickname);
		lblCounterCharacter = (TextView) findViewById(R.id.text_view_counter);

		txtText = (EditText) findViewById(R.id.edittext_text);

		txtText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				lblCounterCharacter.setText("" + (142 - txtText.length()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initSearchView() {

		imgBackGroundSearchBar = (ImageView) findViewById(R.id.image_bg_search);
		imgBackGroundSearchBar.setOnClickListener(this);

		if (btnGps == null) {
			btnGps = (Button) findViewById(R.id.button_gps);
		}
		btnGps.setOnClickListener(this);

		if (btnSearch == null) {
			btnSearch = (Button) findViewById(R.id.button_search);
		}
		btnSearch.setOnClickListener(this);

		if (btnDeleteText == null) {
			btnDeleteText = (Button) findViewById(R.id.button_delete_text);
		}
		// button search by location and by jolt
		if (btnSearchByLocation == null) {
			btnSearchByLocation = (Button) findViewById(R.id.search_by_location);
		}
		btnSearchByLocation.setOnClickListener(this);

		if (btnSearchByJolt == null) {
			btnSearchByJolt = (Button) findViewById(R.id.search_by_jolt);
		}
		btnSearchByJolt.setOnClickListener(this);
		if (btnNumberJolt == null) {
			btnNumberJolt = (Button) findViewById(R.id.btn_number_jolt);
		}

		btnNumberJolt.setTextColor(Color.WHITE);
		btnNumberJolt.setText("0 Jolt visible");

		if (searchLayout == null) {
			searchLayout = (LinearLayout) findViewById(R.id.searchLocation_JoltView);
		}
		searchLayout.setVisibility(View.GONE);

		if (layoutNumberJolt == null) {
			layoutNumberJolt = (RelativeLayout) findViewById(R.id.relative_number_jolt);
		}

		btnDeleteText.setVisibility(View.INVISIBLE);

		searchType = Search.LOCATION;

		txtSearchBar = (EditText) findViewById(R.id.edittext_search);
		txtSearchBar.setOnClickListener(this);
		txtSearchBar.setInputType(0);
		txtSearchBar.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// Log.e("Search Keyboard", "" + keyCode);
				// Log.e("Search Keyboard", "" + KeyEvent.KEYCODE_DEL);
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (isSearch) {
						endSearch();
					}
					break;

				case KeyEvent.KEYCODE_ENTER:
					mode = ModeScreen.SEARCH;
					endSearch();
					String key = txtSearchBar.getText().toString();
					searchJolt(key);
					break;
				}
				return false;
			}
		});
	}

	private void initLoginView() {
		setVariableOfLoginViewToDefault();

		imgToungeJolt = (ImageView) findViewById(R.id.imgToungeJoltScreen);
		imgToungeJolt.setOnTouchListener(this);

		lblLoginTitle = (TextView) findViewById(R.id.lblLoginTitle);

		btnLoginFacebook = (Button) findViewById(R.id.btnLoginFacebook);
		btnLoginFacebook.setOnClickListener(this);

		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
		btnLoginTwitter.setOnClickListener(this);

		lblLoginUjooltAccount = (TextView) findViewById(R.id.lblLoginUjooltAccount);

		lblLoginUser = (TextView) findViewById(R.id.lblLoginUser);
		lblLoginPassword = (TextView) findViewById(R.id.lblLoginPassword);
		txtLoginUser = (EditText) findViewById(R.id.txtLoginUser);
		txtLoginPassword = (EditText) findViewById(R.id.txtLoginPassword);

		btnLoginUjoolt = (Button) findViewById(R.id.btnLoginUjoolt);
		btnLoginUjoolt.setOnClickListener(this);
		btnLoginUjoolt.setTypeface(typeface);
		btnLoginUjoolt.setTextSize(19);

		btnRegisterUjoolt = (Button) findViewById(R.id.btnRegisterUjoolt);
		btnRegisterUjoolt.setOnClickListener(this);
		btnRegisterUjoolt.setTypeface(typeface);
		btnRegisterUjoolt.setTextSize(19);

		btnCloseLogin = (Button) findViewById(R.id.btnCloseLogin);
		btnCloseLogin.setOnClickListener(this);
	}

	private void setVariableOfLoginViewToDefault() {
		myUserName = "";
		myUserNameUjoolt = "";
		myUserNameFacebook = "";
		myUserNameTwitter = "";

		myLoginUserId = "";
		myUserIdUjoolt = "";
		myUserIdFacebook = "";
		myUserIdTwitter = "";

		myLoginType = LoginType.NONE;
		// myAddress = "";

		isUjooltConnected = false;
		isFacebookConnected = false;
		isTwitterConnected = false;
	}

	private void initInformationFull() {
		imgToungeLogin = (ImageView) findViewById(R.id.tounge_register);
		imgToungeLogin.setOnTouchListener(this);

		imgAvatar = (ImageView) findViewById(R.id.image_view_detail);
		imgAvatar.setOnClickListener(this);

		imageViewAvatar = (ImageViewTouch) findViewById(R.id.image_view_big_avatar);
		imageViewAvatar.setVisibility(View.GONE);
		imageViewAvatar.setMainScreenActivity(this);

		imgArrowLeft = (ImageView) findViewById(R.id.arr_left);
		imgArrowRight = (ImageView) findViewById(R.id.arr_right);

		imgClockAndSkull = (ImageView) findViewById(R.id.img_skull);
		imgClockAndSkull.setOnClickListener(this);

		imgFavourite = (ImageView) findViewById(R.id.imgFavourite);
		imgFavourite.setOnClickListener(this);

		layoutListView = (LinearLayout) findViewById(R.id.layoutListView);
		layoutListView.setVisibility(View.INVISIBLE);

		imgPlay = (ImageView) findViewById(R.id.btn_play);
		imgPlay.setOnClickListener(this);

		lblNumberRejolt = (TextView) findViewById(R.id.text_view_rejolt);

		lblNick = (TextView) findViewById(R.id.text_view_nick);
		lblIndexJolt = (TextView) findViewById(R.id.jolt_index_group);
		lblText = (TextView) findViewById(R.id.text_view_text);
		lblText.setSelected(true);
		lblJoltAge = (TextView) findViewById(R.id.txt_jolt_age);
		lblJoltAge.setOnClickListener(this);

		lblWhoRejolt = (TextView) findViewById(R.id.whoRejolt);
		lblWhoRejolt.setTextColor(Color.rgb(189, 208, 242));
		lblWhoRejolt.setTypeface(null, Typeface.BOLD);

		btnPreJoltDetail = (Button) findViewById(R.id.button_prejolt);
		btnPreJoltDetail.setOnClickListener(this);

		btnNextJoltDetail = (Button) findViewById(R.id.button_nextjolt);
		btnNextJoltDetail.setOnClickListener(this);

		lblDragAndReleaseToRejolt = (TextView) findViewById(R.id.text_rejolt);
		lblLifeJolt = (TextView) findViewById(R.id.text_view_life_jolt);

		layoutViewListRejolt = (TableLayout) findViewById(R.id.viewRejolt);
		layoutParentTableListView = (LinearLayout) findViewById(R.id.parentTableListView);
		arrRejolter = new ArrayList<Rejolter>();

		if (linearLayoutJoltDetail == null) {
			linearLayoutJoltDetail = (LinearLayout) findViewById(R.id.layoutJoltDetail);
		}
		imgMagicEye = (ImageView) findViewById(R.id.img_magic_eye);
		imgMagicEye.setOnClickListener(this);

		layoutDetailFull = (LinearLayout) findViewById(R.id.layoutDetailFull);
		layoutDetailFull.setOnTouchListener(this);

		layoutDetailLite = (RelativeLayout) findViewById(R.id.layoutDetailLite);
		layoutDetailLite.setOnClickListener(this);

		layoutInfomationFull = (LinearLayout) findViewById(R.id.layoutDetailFull);
		layoutInfomationFull.setOnTouchListener(this);

		layoutShowAvatar = (LinearLayout) findViewById(R.id.layout_show_avatar);
		layoutShowAvatar.setOnTouchListener(this);
		layoutShowAvatar.setVisibility(View.INVISIBLE);
	}

	private void initInformationLite() {
		textViewJoltAgeDetailLite = (TextView) findViewById(R.id.text_view_jolt_age_header);
		textView1stHead = (TextView) findViewById(R.id.text_view_number_1st_header);
		textView2ndHead = (TextView) findViewById(R.id.text_view_number_2nd_header);
		textView3rdHead = (TextView) findViewById(R.id.text_view_number_3rd_header);
		textView4thHead = (TextView) findViewById(R.id.text_view_number_4th_header);
	}

	private void searchJolt(String key) {

		if (key != null && !key.equalsIgnoreCase("")) {

			switch (searchType) {
			case LOCATION:
				Log.i("Search", "follow Lieu");
				GoogleLocation LatLng_From_Addess = new GoogleLocation(key);

				GeoPoint gPoint = new GeoPoint(LatLng_From_Addess.getLat(),
						LatLng_From_Addess.getLng());

				this.mapController.animateTo(gPoint);

				this.mapController.zoomToSpan(LatLng_From_Addess.getDistance_lat(),
						LatLng_From_Addess.getDistance_lng());

				btnNumberJolt.setTextColor(Color.WHITE);
				btnNumberJolt.setText("0 Jolt visible");
				// --------------UPDATE JOLT--------------------------------

				float distanceUpdateInstagramJolt = getDistanceBetween(
						posisionLast_UpdateJoltInstagram, gPoint);
				if (distanceUpdateInstagramJolt > 50000) {

					joltHolder.arrAvailableJoltInstagram.clear();
					joltHolder.arrAvailableJolt.clear();
					joltHolder.arrAvailableJoltFacebook.clear();
				}

				posisionLast_UpdateJoltUjolt = gPoint;

				if (isFilterInstagram) {
					posisionLast_UpdateJoltInstagram = gPoint;

					JoltHolder.isUpdateInstagram = true;
					joltHolder.getAllJoltFromInstagram(gPoint, JoltHolder.GET_DEFAULT, 5000, true,
							false);
				}

				joltHolder.getAllJoltsFromLocation(gPoint, ConfigUtility.getCurTimeStamp(), false,
						6000, "", JoltHolder.GET_DEFAULT, false, false);

				if (isFilterFacebook && myLoginType == LoginType.FACEBOOK) {
					posisionLast_UpdateJoltFacebook = gPoint;

					joltHolder.getAllJoltFromFacebook(new GeoPoint(mLatitudeE6, mLongitudeE6));
					joltHolder.getAllJoltOfMeFromFacebook();
					joltHolder.getAllJoltOfMyEventFromFacebook();
					Log.e(TAG, "login chay vao getAllJolt FB");
				}
				break;

			case JOLT:
				joltHolder.getSearchResult(curLati, curLongi, key);
				Log.i("Search", "----follow Jolt");
				break;
			}
		}
	}

	public void reGroupAllJolt() {
		ArrayList<Jolt> arrAllJolt = new ArrayList<Jolt>();

		arrAllJolt.addAll(joltHolder.arrAvailableJolt);
		arrAllJolt.addAll(joltHolder.arrAvailableJoltInstagram);
		arrAllJolt.addAll(joltHolder.arrAvailableJoltFacebook);

		mapView.regroupJolts(arrAllJolt);
	}

	private void initFilterView() {
		layoutInactiveFilter = (LinearLayout) findViewById(R.id.layout_inactive_filter);
		layoutActiveFilter = (LinearLayout) findViewById(R.id.layout_active_filter);
		layoutFilterMine = (LinearLayout) findViewById(R.id.layout_my_jolt);
		layoutFilterRecent = (LinearLayout) findViewById(R.id.layout_recent_jolt);
		layoutFilterSocial = (LinearLayout) findViewById(R.id.layout_social_jolt);
		layoutFilterFacebook = (LinearLayout) findViewById(R.id.layout_facebook_jolt);
		layoutFilterInstagram = (LinearLayout) findViewById(R.id.layout_instagram_jolt);
		layoutFilterFavourite = (LinearLayout) findViewById(R.id.layout_favourite_jolt);
		layoutFilterTop = (LinearLayout) findViewById(R.id.layout_top_jolt);

		imgCloseFilter = (ImageView) findViewById(R.id.img_filter_close);

		lblFilterActive = (TextView) findViewById(R.id.lblFilterActive);
		lblFilterInactive = (TextView) findViewById(R.id.lblFilterInactive);
		lblFilterMine = (TextView) findViewById(R.id.txt_filter_my_jolt);
		lblFilterRecent = (TextView) findViewById(R.id.txt_filter_recent_jolt);
		lblFilterFavourite = (TextView) findViewById(R.id.txt_filter_favourite_jolt);

		imgFilterMine = (ImageView) findViewById(R.id.img_filter_my_jolt);
		imgFilterRecent = (ImageView) findViewById(R.id.img_filter_recent_jolt);
		imgFilterSocial = (ImageView) findViewById(R.id.img_filter_social_jolt);
		imgFilterFacebook = (ImageView) findViewById(R.id.img_filter_facebook_jolt);
		imgFilterInstagram = (ImageView) findViewById(R.id.img_filter_instagram_jolt);
		imgFilterFavourite = (ImageView) findViewById(R.id.img_filter_favourite_jolt);
		imgFilterTop = (ImageView) findViewById(R.id.img_filter_top_jolt);

		// Set default background for icon filter
		imgFilterMine.setImageResource(R.drawable.ic_filter_active_jolt_mine);
		imgFilterFacebook.setImageResource(R.drawable.ic_filter_active_jolt_facebook);
		imgFilterInstagram.setImageResource(R.drawable.ic_filter_active_jolt_instagram);

		imgFilterRecent.setImageResource(R.drawable.ic_filter_inactive_jolt_recently);
		imgFilterSocial.setImageResource(R.drawable.ic_filter_inactive_jolt_social);
		imgFilterFavourite.setImageResource(R.drawable.ic_filter_inactive_jolt_favourite);
		imgFilterTop.setImageResource(R.drawable.ic_filter_inactive_jolt_top);

		LinearLayout.LayoutParams lpInactive = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lpActive = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		switch (ConfigUtility.screenSize) {
		case W240:
			break;
		case W320:
			lpInactive.setMargins(7, 410, 0, 0);
			lpActive.setMargins(7, 60, 0, 0);
			break;
		case W480:
			lpInactive.setMargins(20, ConfigUtility.scrHeight - 145, 0, 0);
			lpActive.setMargins(20, ConfigUtility.scrHeight - 670, 0, 0);
			break;
		case W540:
			lpInactive.setMargins(20, ConfigUtility.scrHeight - 145, 0, 0);
			lpActive.setMargins(20, ConfigUtility.scrHeight - 670, 0, 0);
			break;
		case W600:
			lpInactive.setMargins(20, ConfigUtility.scrHeight - 85, 0, 0);
			lpActive.setMargins(20, ConfigUtility.scrHeight - 430, 0, 0);
			break;
		case W720:
			lpInactive.setMargins(20, ConfigUtility.scrHeight - 180, 0, 0);
			lpActive.setMargins(20, ConfigUtility.scrHeight - 880, 0, 0);
			break;
		}

		layoutInactiveFilter.setLayoutParams(lpInactive);
		layoutActiveFilter.setLayoutParams(lpActive);
		layoutActiveFilter.setVisibility(View.GONE);

		layoutInactiveFilter.setOnClickListener(this);
		layoutFilterMine.setOnClickListener(this);
		layoutFilterRecent.setOnClickListener(this);
		layoutFilterSocial.setOnClickListener(this);
		layoutFilterFacebook.setOnClickListener(this);
		layoutFilterInstagram.setOnClickListener(this);
		layoutFilterFavourite.setOnClickListener(this);
		layoutFilterTop.setOnClickListener(this);
		imgCloseFilter.setOnClickListener(this);
	}

	public void initSettingView() {
		isPush = ujooltSharedPreferences.getPushStatus();

		btnSetting = (Button) findViewById(R.id.button_setting);
		btnSetting.setOnClickListener(this);

		toggleButtonPush = (ToggleButton) findViewById(R.id.toggle_button_push);
		toggleButtonPush.setOnClickListener(this);
		toggleButtonPush.setChecked(isPush);

		toggleButtonShareFacebook = (ToggleButton) findViewById(R.id.toggle_button_share_facebook);
		toggleButtonShareFacebook.setOnClickListener(this);
		toggleButtonShareFacebook.setChecked(false);
		toggleButtonShareFacebook.setEnabled(true);

		toggleButtonShareTwitter = (ToggleButton) findViewById(R.id.toggle_button_share_twitter);
		toggleButtonShareTwitter.setOnClickListener(this);
		toggleButtonShareTwitter.setChecked(false);
		toggleButtonShareTwitter.setEnabled(true);

		lblUserNameFacebookSettingView = (TextView) findViewById(R.id.lblUserNameFacebook);
		lblUserNameFacebookSettingView.setText(myUserNameFacebook);
		lblUserNameTwitterSettingView = (TextView) findViewById(R.id.lblUserNameTwitter);
		lblUserNameTwitterSettingView.setText(myUserNameTwitter);

		lblSetting = (TextView) findViewById(R.id.lblSetting);
		lblPush = (TextView) findViewById(R.id.text_view_push);
		lblShareFacebook = (TextView) findViewById(R.id.text_view_share_facebook);
		lblShareTwitter = (TextView) findViewById(R.id.text_view_share_twitter);
		lblTutorial = (TextView) findViewById(R.id.text_view_turorial);
		lblLanguage = (TextView) findViewById(R.id.text_view_language);
		lblLogout = (TextView) findViewById(R.id.text_view_logout);
		lblReportProblem = (TextView) findViewById(R.id.text_view_report_a_problem);
		lblAbout = (TextView) findViewById(R.id.text_view_about);

		linearLayoutTurotial = (LinearLayout) findViewById(R.id.linear_layout_about_tutorial);
		linearLayoutTurotial.setOnClickListener(this);

		layoutLogout = (LinearLayout) findViewById(R.id.linear_layout_logout);
		layoutLogout.setOnClickListener(this);

		linearLayoutReportProblem = (LinearLayout) findViewById(R.id.linear_layout_report_a_problem);
		linearLayoutReportProblem.setOnClickListener(this);

		linearLayoutAbout = (LinearLayout) findViewById(R.id.linear_layout_about);
		linearLayoutAbout.setOnClickListener(this);

		linearLayoutLanguage = (LinearLayout) findViewById(R.id.linear_layout_language);
		linearLayoutLanguage.setOnClickListener(this);

		imgViewCloseSetting = (ImageView) findViewById(R.id.image_view_close_setting);
		imgViewCloseSetting.setOnClickListener(this);
	}

	private void initTutorial() {
		layoutTutorial = (RelativeLayout) findViewById(R.id.layoutTutorial);
		if (isTutorialMode) {
			imgStep1 = (ImageView) findViewById(R.id.imgStep1);
			imgStep2 = (ImageView) findViewById(R.id.imgStep2);
			imgStep3 = (ImageView) findViewById(R.id.imgStep3);
			imgStep4 = (ImageView) findViewById(R.id.imgStep4);
			imgStep5 = (ImageView) findViewById(R.id.imgStep5);
			imgStep6 = (ImageView) findViewById(R.id.imgStep6);

			imgStep2.setOnClickListener(this);
			imgStep3.setOnClickListener(this);
			imgStep4.setOnClickListener(this);

			showStep(Step.ONE);
			layoutTutorial.setVisibility(View.VISIBLE);
		} else {
			layoutTutorial.setVisibility(View.GONE);
		}
	}

	private void initCustomView() {
		if (viewSearch == null) {
			viewSearch = (SearchView) findViewById(R.id.search_view);
		}
		if (viewSearch != null) {
			viewSearch.removeThread();
			viewSearch.setMainActivity(this);
			viewSearch.setType(DraggableView.SEARCH);
			viewSearch.isStarted = false;
		}
		if (viewSearchItem == null) {
			viewSearchItem = (SearchItemView) findViewById(R.id.search_item_view);
		}
		if (viewSearchItem != null) {
			viewSearchItem.removeThread();
			viewSearchItem.setMainActivity(this);
			viewSearchItem.setType(DraggableView.SEARCH_ITEM);
			viewSearchItem.isStarted = false;
		}

		if (viewJolt == null) {
			viewJolt = (JoltView) findViewById(R.id.jolt_view);
		}
		if (viewJolt != null) {
			viewJolt.removeThread();
			viewJolt.setMainActivity(this);
			viewJolt.setVisibility(View.INVISIBLE);
			viewJolt.setType(DraggableView.JOLT);
			viewJolt.setOnTouchListener(null);
			viewJolt.isStarted = false;
		}

		if (viewLogin == null) {
			viewLogin = (LoginView) findViewById(R.id.login_view);
		}
		if (viewLogin != null) {
			viewLogin.removeThread();
			viewLogin.setMainActivity(this);
			viewLogin.setVisibility(View.VISIBLE);
			viewLogin.setOnTouchListener(null);
			viewLogin.isStarted = false;
			viewLogin.isLogin = true;
			viewLogin.isLogined = false;
			viewLogin.isLoginning = false;
		}

		if (viewSetting == null) {
			viewSetting = (SettingView) findViewById(R.id.setting_view);
		}
		if (viewSetting != null) {
			viewSetting.removeThread();
			viewSetting.setMainActivity(this);
			viewSetting.setVisibility(View.VISIBLE);
			viewSetting.setType(DraggableView.SETTING);
		}

		if (viewInformation == null) {
			viewInformation = (InformationView) findViewById(R.id.information_view);
		}
		if (viewInformation != null) {
			viewInformation.removeThread();
			viewInformation.setMainActivity(this);
			viewInformation.setType(DraggableView.INFORMATION);
			viewInformation.isStarted = false;
		}

		DraggableView.setWH(ConfigUtility.scrWidth, ConfigUtility.scrHeight * 2);

		switch (ConfigUtility.screenSize) {
		case W240:
			break;

		case W320:
			if (viewJolt != null) {
				viewJolt.setMaxTopBottom(85, -375);
				viewJolt.setHiddenPoint(-580);
				viewJolt.setDisappearPoint(480);
			}

			if (viewInformation != null) {
				viewInformation.setMaxTopBottom(290, 100);
				viewInformation.setHiddenPoint(820);
			}

			if (viewLogin != null) {
				viewLogin.setMaxTopBottom(85, -375);
				viewLogin.setHiddenPoint(-580);
				viewLogin.setDisappearPoint(530);
			}

			if (viewSetting != null) {
				viewSetting.setMaxTopBottom(405, -10);
			}

			if (viewSearch != null) {
				if (ConfigUtility.scrWidth >= 480)
					viewSearch.setMaxTopBottom(60, -30);
				else
					viewSearch.setMaxTopBottom(60, -20);
			}

			if (viewSearchItem != null) {
				viewSearchItem.setMaxLeftRight(ConfigUtility.scrWidth / 2 - 120,
						ConfigUtility.scrWidth / 2 - 75);
			}
			break;
		case W480:
			if (viewJolt != null) {
				viewJolt.setMaxTopBottom(120, -630);
				viewJolt.setHiddenPoint(-850);
				viewJolt.setDisappearPoint(750);
			}

			if (viewLogin != null) {
				viewLogin.setMaxTopBottom(120, -630);
				viewLogin.setHiddenPoint(-850);
				viewLogin.setDisappearPoint(850);
				viewLogin.scroll = viewLogin.bottom_max;
			}

			if (viewSetting != null) {
				viewSetting.setMaxTopBottom(530, -20);
			}

			if (viewInformation != null) {
				viewInformation.setMaxTopBottom(460, 100);
				viewInformation.setHiddenPoint(980);
			}

			if (viewSearch != null) {
				viewSearch.setMaxTopBottom(60, -30);
			}

			if (viewSearchItem != null) {
				viewSearchItem.setMaxLeftRight(ConfigUtility.scrWidth / 2 - 175,
						ConfigUtility.scrWidth / 2 - 115);
			}

			break;
		case W540:
			break;
		case W600:
			if (viewJolt != null) {
				viewJolt.setMaxTopBottom(80, -870);
				viewJolt.setHiddenPoint(-1050);
				viewJolt.setDisappearPoint(1050);
			}

			if (viewLogin != null) {
				viewLogin.setMaxTopBottom(80, -890);
				viewLogin.setHiddenPoint(-1050);
				viewLogin.setDisappearPoint(1050);
				viewLogin.scroll = viewLogin.bottom_max;
			}

			if (viewSetting != null) {
				viewSetting.setMaxTopBottom(530, -20);
			}

			if (viewInformation != null) {
				viewInformation.setMaxTopBottom(280, 110);
				viewInformation.setHiddenPoint(880);
			}

			if (viewSearch != null) {
				if (ConfigUtility.scrWidth >= 480)
					viewSearch.setMaxTopBottom(60, -30);
				else
					viewSearch.setMaxTopBottom(60, -20);
			}
			if (viewSearchItem != null) {
				viewSearchItem.setMaxLeftRight(ConfigUtility.scrWidth / 2 - 120,
						ConfigUtility.scrWidth / 2 - 80);
			}
			break;
		case W720:
			if (viewJolt != null) {
				viewJolt.setMaxTopBottom(160, -1070);
				viewJolt.setHiddenPoint(-1050);
				viewJolt.setDisappearPoint(1500);
			}

			if (viewLogin != null) {
				viewLogin.setMaxTopBottom(170, -1070);
				viewLogin.setHiddenPoint(-1050);
				viewLogin.setDisappearPoint(1500);
				viewLogin.scroll = viewLogin.bottom_max;
			}

			if (viewSetting != null) {
				viewSetting.setMaxTopBottom(700, -30);
			}

			if (viewInformation != null) {
				viewInformation.setMaxTopBottom(610, 150);
				viewInformation.setHiddenPoint(1100);
			}

			if (viewSearch != null) {
				viewSearch.setMaxTopBottom(80, -30);
			}

			if (viewSearchItem != null) {
				viewSearchItem.setMaxLeftRight(ConfigUtility.scrWidth / 2 - 120,
						ConfigUtility.scrWidth / 2 - 80);
			}
			break;
		}

		if (viewLogin != null) {
			if (viewLogin.isLogin) {
				viewLogin.setVisibility(View.VISIBLE);
			} else {
				viewLogin.setVisibility(View.INVISIBLE);
			}
		}

		if (viewSetting != null) {
			viewSetting.setVisibility(View.INVISIBLE);
		}

		if (viewInformation != null) {
			viewInformation.setVisibility(View.INVISIBLE);
		}

		if (viewSearch != null) {
			viewSearch.setVisibility(View.VISIBLE);
		}
	}

	public void setImageSourceDistance(int distance) {
		int d = 2 * distance;
		int t1 = d / 100000;
		int t2 = (d - t1 * 100000) / 10000;
		int t3 = (d - t1 * 100000 - t2 * 10000) / 1000;
		int t4 = (d - t1 * 100000 - t2 * 10000 - t3 * 1000) / 100;
		textView1stHead.setText("" + t1);
		textView2ndHead.setText("" + t2);
		textView3rdHead.setText("" + t3);
		textView4thHead.setText("." + t4);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		Log.e("Keycode", "" + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			closeProgressDialog();
			if (isShowAvatar) {
				// hide Full Avatar when press back
				Log.e("Lemon-Hide Full Avatar", "Hide Full Avatar when press back");
				isBubbleDetail = true;
				isShowAvatar = false;
				layoutShowAvatar.setVisibility(View.GONE);

				return true;
			} else {
				if (viewLogin.isLogin) {
					if (isTakePhotoOn) {
						onClickCancelTakePhoto();

					} else if (viewLogin.scroll == viewLogin.top_max) {
						if (!isRegister) {
							onClickCloseLogin();
							// } else {
							// onClickButtonCancelRegister();
						}

					} else if (isSetting && viewSetting.scroll == viewSetting.bottom_max) {
						onClickCloseSetting();

					} else if (isBubbleDetail) {
						ItemizedOverlays item = getCurItem();
						if (item != null) {
							item.balloonView.resetIcon();
						}
						if (!isShowJoltSource) {
							viewInformation.disappear(InformationView.QUIT);
						}
					} else if (!isJolting && !isSetting) {
						showDialogExit();
					}
				} else {
					if (isTakePhotoOn) {
						onClickCancelTakePhoto();

					} else if (viewJolt.scroll == viewJolt.top_max) {
						onClickButtonDownJolt();

					} else if (isSetting && viewSetting.scroll == viewSetting.bottom_max) {
						onClickCloseSetting();

					} else if (isBubbleDetail) {
						ItemizedOverlays item = getCurItem();
						if (item != null) {
							item.balloonView.resetIcon();
						}
						if (!isShowJoltSource) {
							viewInformation.disappear(InformationView.QUIT);
						}
					} else if (!isJolting && !isSetting) {
						showDialogExit();
					}
				}
			}

			break;
		case KeyEvent.KEYCODE_SEARCH:

			if (!isShowAvatar) {
				if (viewLogin.isLogin) {
					if (isTakePhotoOn) {
					} else if (viewLogin.scroll == viewLogin.top_max) {
					} else if (isSetting && viewSetting.scroll == viewSetting.bottom_max) {
					} else if (isBubbleDetail) {
					} else if (!isJolting && !isSetting) {
						onClickSearchBar();
					}
				} else {
					if (isTakePhotoOn) {
					} else if (viewJolt.scroll == viewJolt.top_max) {
					} else if (isSetting && viewSetting.scroll == viewSetting.bottom_max) {
					} else if (isBubbleDetail) {
					} else if (!isJolting && !isSetting) {
						onClickSearchBar();
					}
				}
			}

			break;
		case KeyEvent.KEYCODE_MENU:
			if (!isShowAvatar) {
				if (viewLogin.isLogin) {
					if (isTakePhotoOn) {
					} else if (viewLogin.scroll == viewLogin.top_max) {
					} else if (isSetting && viewSetting.scroll == viewSetting.bottom_max) {
					} else if (isBubbleDetail) {
					} else if (!isJolting && !isSetting) {
						onClickSetting();
					}
				} else {
					if (isTakePhotoOn) {
					} else if (viewJolt.scroll == viewJolt.top_max) {
					} else if (isSetting && viewSetting.scroll == viewSetting.bottom_max) {
					} else if (isBubbleDetail) {
					} else if (!isJolting && !isSetting) {
						onClickSetting();
					}
				}
			}
			break;
		}
		return true;
		// return super.onKeyDown(keyCode, event);
	}

	public String encode(String value) throws UnsupportedEncodingException {
		if (value != null) {
			return StringUtility.encode(value);
		} else {
			return "";
		}
	}

	public void addItemOverlay(Jolt jolt) {
		Log.e(TAG, "here");
		ItemizedOverlays itemizedOverlays = getItemizedOverlayContainJolt(jolt);
		if (itemizedOverlays != null) {
			itemizedOverlays.addJolt(jolt);
			// itemizedOverlays.chaneNormalIcon();
			// itemizedOverlays.balloonView.setIcon();
			itemizedOverlays.balloonView.setAnimationWhenDisplay();
		} else {
			int size = arrItem.size();
			ItemizedOverlays itemizedoverlay = new ItemizedOverlays(this, mapView, jolt, size,
					Utility.CREATE);
			arrItem.add(itemizedoverlay);
		}
	}

	public ItemizedOverlays checkExistLocationNormal(Jolt jolt) {
		int size = arrItem.size();
		for (int i = 0; i < size; i++) {
			// Object object = mapOverlays.get(i);

			// mapOverlays.get(i);
			// if (object instanceof ItemizedOverlays) {
			ItemizedOverlays itemizedOverlays = (ItemizedOverlays) arrItem.get(i);
			// OverlayItem overlayItem = itemizedOverlays.getItem(0);

			// GeoPoint geoPoint = itemizedOverlays.geoPoint;

			// if (geoPoint.getLatitudeE6() == jolt.getLatitude()
			// && geoPoint.getLongitudeE6() == jolt.getLongitude()) {
			// return itemizedOverlays;
			// }
			Jolt jolt2 = itemizedOverlays.getCurrentJolt();
			if (jolt.groupID == jolt2.groupID) {
				return itemizedOverlays;
			}
			// }
		}
		return null;
	}

	// public void reloadMapOverlays() {
	// if (joltHolder != null && joltHolder.arrJolt != null) {
	// addItemOverlays(joltHolder.arrJolt);
	// }
	// }

	public void reset() {
		isSetting = false;
		isBubbleDetail = false;
		isQuitRegister = false;
		isStartJolt = false;
		isBackRegister = false;

		if (viewLogin != null) {
			viewLogin.reset();
			viewLogin.isLogin = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			Log.e("Ddd", "DOWN");
			Log.e("Ddd", "DOWN");
			Log.e("Ddd", "DOWN");
			Log.e("Ddd", "DOWN");

			if (isBubbleDetail) {
				viewInformation.isDown = true;
				viewInformation.isDrag = false;
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (!isLoading) {
			int action = event.getAction();
			float xTouch = event.getX();
			float yTouch = event.getY();

			if (action == MotionEvent.ACTION_DOWN) {

				if (isBubbleDetail) {
					viewInformation.isDown = true;
					viewInformation.isDrag = false;
				}
			}

			if (action == MotionEvent.ACTION_MOVE) {

				if (isBubbleDetail) {
					viewInformation.isMove = true;
				}
			}

			if (action == MotionEvent.ACTION_UP) {

				if (isBubbleDetail) {
					if (viewInformation.isMove) {
						viewInformation.isDrag = true;
					} else {
						viewInformation.isDrag = false;
					}

					viewInformation.isMove = false;
					viewInformation.isDown = false;
				}

				if (v == layoutInfomationFull) {
					viewInformation.isMove = true;
					viewInformation.setOnTouchListener(this);
				}
			}

			if (v == imgToungeJolt) {

				if (!isJolting) {
					viewSearch.disappear(SearchView.JOLT);
					imgToungeJolt.setOnTouchListener(null);
					viewJolt.setVisibility(View.VISIBLE);
					viewJolt.isStarted = true;
					viewJolt.isMoveUp = true;
					viewJolt.isMoveDown = false;
					viewJolt.setOnTouchListener(this);
					viewInformation.disappear(InformationView.JOLT);
					viewJolt.move(action, (int) event.getX(), (int) yTouch);
					isJolting = true;

					if (isTutorialMode && step == Step.ONE) {
						showStep(Step.TWO);
					}

					return false;
				}
			}

			if (v == imgToungeLogin) {
				if (!viewLogin.isLoginning) {
					if (step == Step.ONE && isTutorialMode) {
						showStep(Step.ONE_HALF);
					}
					viewSearch.disappear(SearchView.REGISTER);
					imgToungeLogin.setOnTouchListener(null);
					viewLogin.setVisibility(View.VISIBLE);
					viewLogin.isStarted = true;
					viewLogin.isMoveUp = true;
					viewLogin.isMoveDown = false;
					viewLogin.isAppear = false;
					viewLogin.isDisapear = false;
					viewLogin.setOnTouchListener(this);
					viewInformation.disappear(InformationView.REGISTER);
					viewLogin.move(action, (int) event.getX(), (int) yTouch);
					viewLogin.isLoginning = true;
					return false;
				}
			}

			if (v == layoutShowAvatar) {

				// isShowAvatar = false;
				// isBubbleDetail = true;
				// layoutShowAvatar.setVisibility(View.INVISIBLE);
				return true;
			}

			if (v == layoutInfomationFull) {
				ItemizedOverlays itemizedOverlays = getCurItem();
				Jolt jolt = itemizedOverlays.getCurrentJolt();

				if (viewInformation.isEnableDrag && !jolt.isMyJolt()) {

					viewInformation.isMove = true;
					viewInformation.move(action, (int) event.getX(), (int) yTouch);

					layoutListView.setVisibility(View.GONE);
					linearLayoutJoltDetail.setVisibility(View.VISIBLE);

					if (!isRejolt) {
						// view_information.setVisibility(View.VISIBLE);

						viewInformation.setOnTouchListener(this);
						viewInformation.move(action, (int) event.getX(), (int) yTouch);
						isRejolt = true;

						return false;
					}
				}
			}

			if (isJolting && !isStartJolt) {
				viewJolt.move(action, (int) xTouch, (int) yTouch);
			}
			if (viewLogin.isLoginning && !isQuitRegister) {
				// if(v == view_register)
				viewLogin.move(action, (int) event.getX(), (int) event.getY());
			}
			if (isBubbleDetail && !isShowAvatar && isRejolt) {
				viewInformation.move(action, (int) xTouch, (int) yTouch);
			}
		}

		return true;
	}

	public void setOnTouchforDetail() {
		viewInformation.setOnTouchListener(this);
	}

	public void removeOnTouchforDetail() {
		viewInformation.removeListener();
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
	}

	@Override
	public void onClick(View v) {

		if (!isLoading) {
			if (v == btnLoginFacebook) {
				onClickLoginFacebook();

			} else if (v == btnLoginTwitter) {
				onClickButtonLoginTwitter();

			} else if (v == btnShareFacebook) {
				onClickButtonShareFB();

			} else if (v == btnShareTwitter) {
				onClickButtonShareTW();

			} else if (v == toggleButtonShareTwitter) {
				onClickToggleShareTW();

			} else if (v == toggleButtonShareFacebook) {
				onClickToggleShareFB();

			} else if (v == imgMagicEye) {
				onClickPropagation();

			} else if (v == layoutDetailLite) {
				onClickLayoutHeaderDetail();

			} else if (v == txtSearchBar) {
				onClickSearchBar();

			} else if (v == btnDeleteText) {
				txtSearchBar.setText("");

			} else if (v == imgBackGroundSearchBar) {
				onClickSearchBar();

			} else if (v == layoutLogout) {
				onClickLayoutLogout();

			} else if (v == btnSetting) {
				onClickSetting();

			} else if (v == btnJolt) {
				onClickJolt();

			} else if (v == btnGps) {
				onClickGPS();

			} else if (v == btnLoginUjoolt) {
				onClickLoginUjoolt();

			} else if (v == btnRegisterUjoolt) {
				onClickRegister();

			} else if (v == btnCloseJoltScreen) {
				Log.e("abc", "dfdsfsdf");
				onClickButtonDownJolt();

			} else if (v == imgPlay) {
				onClickImageDetail();

			} else if (v == btnRejolt) {
				onClickButtonRejolt();

			} else if (v == lblJoltAge) {
				onClickTextViewJoltAge();

			} else if (v == imgFavourite) {
				onClickFavourite();

			} else if (v == imgClockAndSkull) {
				onClickClockAndSkull();

			} else if (v == imgPublic) {
				onClickPublic();
			}

			else if (v == imgPrivate) {
				onClickFriend();

			} else if (v == btnCloseLogin) {
				onClickCloseLogin();

			} else if (v == imgAvatar) {
				onClickImageDetail();

				// } else if (v == btnCloseVideo) {
				// onClickCloseVideo();

			} else if (v == btnUploadPhoto) {
				onClickUploadPhoto();

			} else if (v == btnChangeMedia) {
				onClickUploadPhoto();

			} else if (v == imgViewCloseSetting) {
				onClickCloseSetting();

			} else if (v == linearLayoutTurotial) {
				onClickTutorial();

			} else if (v == linearLayoutAbout) {
				onClickAbout();

			} else if (v == linearLayoutReportProblem) {
				onClickLinearLayoutReportProblem();

			} else if (v == linearLayoutLanguage) {
				onClickLinearLayoutLanguage();

			} else if (v == toggleButtonPush) {
				onClickToggleButtonPush();

			} else if (v == btnSearch) {
				onClickSearchIcon();

			} else if (v == btnSearchByLocation) {
				onClickSearchByLocation();

			} else if (v == btnSearchByJolt) {
				onClickSearchByJolt();
			}

			else if (v == btnPreJoltDetail) {
				onClickButtonPrejoltDetail();

			} else if (v == btnNextJoltDetail) {
				onClickButtonNextJoltDetail();

			} else if (v == layoutInactiveFilter) {
				onClickButtonLayoutInactiveFilter();

			} else if (v == layoutFilterMine) {
				onClickButtonLayoutFilterMine();

			} else if (v == layoutFilterRecent) {
				onClickButtonLayoutFilterRecent();

			} else if (v == layoutFilterSocial) {
				onClickButtonLayoutFilterSocial();

			} else if (v == layoutFilterFacebook) {
				onClickButtonLayoutFilterFacebook();

			} else if (v == layoutFilterInstagram) {
				onClickButtonLayoutFilterInstagram();

			} else if (v == layoutFilterFavourite) {
				onClickButtonLayoutFilterFavourite();

			} else if (v == layoutFilterTop) {
				onClickButtonLayoutFilterTop();

			} else if (v == imgCloseFilter) {
				onClickImgCloseFilter();

			} else if (v == imgClockAndSkull) {
				onClickClockAndSkull();

				// } else if (v == headerLayout) {
				// showNickRejolt();
				// onClickImgMagicEye();

				// } else if (v == layoutPostJolt) {
				// showNextStep();

			} else if (v == imgStep2 || v == imgStep3 || v == imgStep4) {
				showNextStep();
			}
		}
	}

	private void showNextStep() {
		if (isTutorialMode) {
			switch (step) {
			case TWO:
				showStep(Step.THREE);
				break;
			case THREE:
				showStep(Step.FOUR);
				hideKeyBoard(txtText);
				break;
			case FOUR:
				showStep(Step.FIVE);
				hideKeyBoard(txtText);
				break;
			default:
				showStep(Step.QUIT);
				break;
			}
		}
	}

	private void onClickClockAndSkull() {
		if (lblJoltAge.getVisibility() == View.INVISIBLE) {
			if (linearLayoutJoltDetail.getVisibility() == View.VISIBLE) {
				linearLayoutJoltDetail.setVisibility(View.INVISIBLE);
				layoutListView.setVisibility(View.VISIBLE);
				showNickRejolt();
			} else {
				hideListRejolt();
				linearLayoutJoltDetail.setVisibility(View.VISIBLE);
			}
		}
	}

	public TableLayout layoutViewListRejolt;
	private LinearLayout layoutParentTableListView;
	public ArrayList<Rejolter> arrRejolter;

	// public ScrollView parentTableListView;

	public void setAdapterListView(ArrayList<ArrayList<String>> arr) {
		// view_information.countDownControl.stop();

		int size = arr.size();

		Log.e(TAG, "luong Size = " + size);

		ArrayList<String> nick = new ArrayList<String>();
		ArrayList<String> rejolt_fbid = new ArrayList<String>();
		ArrayList<Boolean> arrTypeOfJolts = new ArrayList<Boolean>();

		for (int i = 0; i < size; i++) {
			nick.add(arr.get(i).get(0));
			rejolt_fbid.add(arr.get(i).get(1));

			Log.e(TAG, "luong nick" + nick.get(i));
		}

		for (int i = 0; i < size; i++) {
			if (rejolt_fbid.get(i).equals("")) {
				arrTypeOfJolts.add(false);
			} else {
				arrTypeOfJolts.add(true);
			}

			Log.e(TAG, "is jolt Ujolt " + arrTypeOfJolts.get(i));
		}

		fillCountryTable(nick, arrTypeOfJolts);
	}

	private void fillCountryTable(ArrayList<String> nick, ArrayList<Boolean> type) {

		TableRow row;
		ImageView i0, i1;
		TextView t0, t1;

		// Converting to dip unit
		int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 1,
				getResources().getDisplayMetrics());

		layoutViewListRejolt.removeAllViewsInLayout();
		// parentTableListView.removeView(viewRejolt);

		if (nick.size() > 0)
			for (int i = 0; i < nick.size(); i++) {

				row = new TableRow(this);

				t0 = new TextView(this);
				t0.setTextColor(getResources().getColor(R.color.blue));
				t1 = new TextView(this);
				t1.setTextColor(getResources().getColor(R.color.orange));

				i0 = new ImageView(this);
				i0.setImageResource(R.drawable.ic_item_facebook);
				i1 = new ImageView(this);
				i1.setImageResource(R.drawable.ic_item_ujoolt);

				t0.setText(nick.get(i));
				t1.setText(nick.get(i));

				// t0.setTypeface(null, 1);
				// t1.setTypeface(null, 1);

				t0.setTextSize(15);
				t1.setTextSize(15);

				t0.setWidth(80 * dip);
				t1.setWidth(80 * dip);

				// t0.setPadding(5 * dip, 0, 0, 0);
				row.addView(i0);
				row.addView(t0);

				row.addView(i1);
				row.addView(t1);

				Log.e(TAG, "row =" + i);

				layoutViewListRejolt.addView(row, new TableLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			}

		Log.e("aaaaa", "num = " + layoutParentTableListView.getChildCount());
		Log.e("aaaaa", "num row = " + layoutViewListRejolt.getChildCount());

		// parentTableListView.addView(viewRejolt);
		curItemizedOverlays.balloonView.zoomIcon();
	}

	private ArrayList<RejolterItem> getArrRejolterItems(ArrayList<Rejolter> arrRejolter) {
		ArrayList<RejolterItem> result = new ArrayList<RejolterItem>();
		if (arrRejolter.size() % 2 == 1) {
			arrRejolter.add(new Rejolter("", true));
		}
		for (int i = 0; i < arrRejolter.size() / 2; i++) {
			result.add(new RejolterItem(arrRejolter.get(2 * i), arrRejolter.get(2 * i + 1)));
		}
		return result;
	}

	private void setValue(Rejolter rejolter, ImageView img, TextView lbl, TableRow row, int dip) {
		if (rejolter.getNick().equalsIgnoreCase("")) {
			img.setVisibility(View.INVISIBLE);
			lbl.setVisibility(View.INVISIBLE);

		} else if (rejolter.isUjoolt()) {
			lbl.setTextColor(getResources().getColor(R.color.white));
			lbl.setText(rejolter.getNick());
			lbl.setTypeface(null, 1);
			lbl.setTextSize(15);
			lbl.setWidth(75 * dip);

			img.setImageResource(R.drawable.ic_item_ujoolt);

			// row.addView(img);
			// row.addView(lbl);

		} else {
			lbl.setTextColor(getResources().getColor(R.color.blue));
			lbl.setText(rejolter.getNick());
			lbl.setTypeface(null, 1);
			lbl.setTextSize(15);
			lbl.setWidth(75 * dip);

			img.setImageResource(R.drawable.ic_item_facebook);

			// row.addView(img);
			// row.addView(lbl);
		}
	}

	public void fillRejoltTable() {

		layoutViewListRejolt.removeAllViewsInLayout();
		ArrayList<RejolterItem> arrRejolterItem = getArrRejolterItems(arrRejolter);
		// Converting to dip unit
		int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 1,
				getResources().getDisplayMetrics());

		for (int i = 0; i < arrRejolterItem.size(); i++) {
			TableRow row = new TableRow(this);
			ImageView imgL = new ImageView(this);
			ImageView imgR = new ImageView(this);
			TextView lblL = new TextView(this);
			TextView lblR = new TextView(this);
			setValue(arrRejolterItem.get(i).getRejolterL(), imgL, lblL, row, dip);
			setValue(arrRejolterItem.get(i).getRejolterR(), imgR, lblR, row, dip);

			row.addView(imgL);
			row.addView(lblL);
			row.addView(imgR);
			row.addView(lblR);

			layoutViewListRejolt.addView(row, new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		curItemizedOverlays.balloonView.zoomIcon();
	}

	// public void setAdapterListView(ArrayList<ArrayList<String>> arr) {
	// // view_information.countDownControl.stop();
	//
	// int size = arr.size();
	//
	// // layoutInfomationFrame.removeView(scrollDetail);
	//
	// Log.e(TAG, "luong Size = " + size);
	//
	// String[] nick = new String[size];
	// String[] rejolt_fbid = new String[size];
	// Boolean[] arrTypeOfJolts = new Boolean[size];
	//
	// for (int i = 0; i < arr.size(); i++) {
	// nick[i] = arr.get(i).get(0);
	// rejolt_fbid[i] = arr.get(i).get(1);
	//
	// Log.e(TAG, "luong nick" + nick[i]);
	// }
	//
	// for (int i = 0; i < size; i++) {
	// if (rejolt_fbid[i].equals("")) {
	// arrTypeOfJolts[i] = false;
	// } else {
	// arrTypeOfJolts[i] = true;
	// }
	//
	// Log.e(TAG, "is jolt Ujolt " + arrTypeOfJolts[i]);
	// }
	//
	// RejoltListItem list[];
	//
	// int length;
	//
	// if (nick.length % 2 == 0)
	// length = nick.length / 2;
	// else
	// length = nick.length / 2 + 1;
	//
	// list = new RejoltListItem[length];
	//
	// Log.e("tag", "tag " + length);
	//
	// for (int i = 0; i < list.length; i++) {
	// if (arrTypeOfJolts[i] == true) {
	// if (i == nick.length / 2 && (nick.length % 2 != 0))
	// list[i] = new RejoltListItem(R.drawable.ic_item_ujoolt, nick[2 * i],
	// R.drawable.ic_ujoolt_fake_jolter, "");
	// else
	// list[i] = new RejoltListItem(R.drawable.ic_item_ujoolt, nick[2 * i],
	// R.drawable.ic_item_ujoolt, nick[2 * i + 1]);
	// } else {
	// if (i == nick.length / 2 && (nick.length % 2 != 0))
	// list[i] = new RejoltListItem(R.drawable.ic_item_facebook, nick[2 * i],
	// R.drawable.ic_ujoolt_fake_jolter, "");
	// else
	// list[i] = new RejoltListItem(R.drawable.ic_item_facebook, nick[2 * i],
	// R.drawable.ic_item_ujoolt, nick[2 * i + 1]);
	// }
	// }
	//
	// RejoltListAdapter adapter = new RejoltListAdapter(this,
	// R.layout.list_row, list);
	// // adapter.notifyDataSetChanged();
	// layoutListView.removeView(myListView);
	// myListView.setAdapter(adapter);
	// layoutListView.addView(myListView);
	// // layoutInfomationFrame.addView(scrollDetail);
	// }

	public void onClickRegister() {
		isRegister = true;
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	public void onClickTutorial() {
		Intent intent = new Intent(this, TutorialActivity.class);
		startActivityForResult(intent, RequestCode.SHOW_TUTORIAL);
	}

	public void onClickSearchBar() {
		if (!isSearch) {
			startSearch();
		}
		Animation searchViewDown = AnimationUtils.loadAnimation(MainScreenActivity.this,
				R.anim.move_search_view_down);
		searchViewDown.setDuration(900);
		searchLayout.setAnimation(searchViewDown);

		showKeyBoard(txtSearchBar);
		layoutNumberJolt.setVisibility(View.GONE);
		searchLayout.setVisibility(View.VISIBLE);
		Log.d("visible", "" + searchLayout.getVisibility());
		btnSearchByJolt.setVisibility(View.VISIBLE);

		showSearchByLocation_JoltView();
	}

	public void onClickLoginFacebook() {
		loginFacebook();
	}

	// Click search location
	public void onClickSearchByLocation() {
		btnSearchByLocation.setBackgroundResource(R.drawable.ic_lieu_on);
		btnSearchByJolt.setBackgroundResource(R.drawable.ic_jolt_off);
		searchType = Search.LOCATION;
	}

	// Click search jolt
	public void onClickSearchByJolt() {
		btnSearchByLocation.setBackgroundResource(R.drawable.ic_lieu_off);
		btnSearchByJolt.setBackgroundResource(R.drawable.ic_jolt_on);
		searchType = Search.JOLT;
	}

	private void showSearchByLocation_JoltView() {
		searchLayout.setVisibility(0);
	}

	public void setPostPublic(boolean isPublic) {
		if (isPublic) {
			isPublicPostJolt = true;
			imgPublic.setImageResource(R.drawable.btn_public_active);
			if (language == Language.FRANCE) {
				imgPrivate.setImageResource(R.drawable.btn_amis_inactive);
			} else {
				imgPrivate.setImageResource(R.drawable.btn_friend_inactive);
			}
		} else {
			isPublicPostJolt = false;
			imgPublic.setImageResource(R.drawable.btn_public_inactive);
			if (language == Language.FRANCE) {
				imgPrivate.setImageResource(R.drawable.btn_amis_active);
			} else {
				imgPrivate.setImageResource(R.drawable.btn_friend_active);
			}
		}
	}

	public void onClickPublic() {
		setPostPublic(true);
	}

	public void onClickFriend() {
		setPostPublic(false);
		if (UtilityFacebook.mFacebook.getAccessToken() == null
				|| UtilityFacebook.mFacebook.getAccessToken().equalsIgnoreCase("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(Language.confirmLoginFacebook).setCancelable(false)
					.setNegativeButton(Language.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							loginFacebook();
						}
					}).setPositiveButton(Language.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							setPostPublic(true);
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			setMainAccount(myUserNameFacebook, myUserIdFacebook, LoginType.FACEBOOK);
		}
	}

	// private boolean delayAgainFilter = false;
	public Handler handgroupFilter = new Handler();
	public Runnable runGroupFilter = new Runnable() {

		@Override
		public void run() {
			if (delayAgain) {
				delayAgain = false;
				handgroup.removeCallbacksAndMessages(null);
				handgroup.postDelayed(this, 400);
				return;
			} else {
				groupArrayJoltFilter();
			}
		}
	};

	//
	// private boolean checkActive(Jolt jolt) {
	// jolt.isActive = false;
	//
	// boolean isMyJolt = jolt.getLoginUserid().equalsIgnoreCase(myLoginUserid);
	//
	// if (!isMyJolt && !jolt.isFacebook() && !jolt.isInstagram() &&
	// !jolt.isTop()
	// && !jolt.isLike()) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	//
	// if (isFilterMine) {
	// if (isMyJolt) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	// }
	//
	// if (isFilterSocial) {
	// boolean isJoltSocial = joltHolder.isSocialJolt(jolt);
	// if (isJoltSocial == true) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	// }
	//
	// if (isFilterRecent) {
	// boolean isJoltRecent = isRecent(jolt.getDate());
	// if (isJoltRecent == true) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	// }
	//
	// if (isFilterFavourite) {
	// boolean isJoltFavourite = jolt.isLike();
	// if (isJoltFavourite == true) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	// }
	//
	// if (isFilterFacebook) {
	// if (jolt.isFacebook()) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	// }
	//
	// if (isFilterInstagram) {
	// if (jolt.isInstagram()) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	// }
	//
	// if (isFilterTop) {
	// if (jolt.isTop()) {
	// jolt.isActive = true;
	// return jolt.isActive;
	// }
	// }
	//
	// return jolt.isActive;
	// }

	public void showStep(Step step) {
		this.step = step;
		switch (step) {
		case ONE:
			imgStep1.setVisibility(View.VISIBLE);
			imgStep2.setVisibility(View.INVISIBLE);
			imgStep3.setVisibility(View.INVISIBLE);
			imgStep4.setVisibility(View.INVISIBLE);
			imgStep5.setVisibility(View.INVISIBLE);
			imgStep6.setVisibility(View.INVISIBLE);
			break;
		case ONE_HALF:
			imgStep1.setVisibility(View.INVISIBLE);
			imgStep2.setVisibility(View.INVISIBLE);
			imgStep3.setVisibility(View.INVISIBLE);
			imgStep4.setVisibility(View.INVISIBLE);
			imgStep5.setVisibility(View.INVISIBLE);
			imgStep6.setVisibility(View.INVISIBLE);
			break;
		case TWO:
			imgStep1.setVisibility(View.INVISIBLE);
			imgStep2.setVisibility(View.VISIBLE);
			imgStep3.setVisibility(View.INVISIBLE);
			imgStep4.setVisibility(View.INVISIBLE);
			imgStep5.setVisibility(View.INVISIBLE);
			imgStep6.setVisibility(View.INVISIBLE);
			break;

		case THREE:
			imgStep1.setVisibility(View.INVISIBLE);
			imgStep2.setVisibility(View.INVISIBLE);
			imgStep3.setVisibility(View.VISIBLE);
			imgStep4.setVisibility(View.INVISIBLE);
			imgStep5.setVisibility(View.INVISIBLE);
			imgStep6.setVisibility(View.INVISIBLE);
			break;

		case FOUR:
			imgStep1.setVisibility(View.INVISIBLE);
			imgStep2.setVisibility(View.INVISIBLE);
			imgStep3.setVisibility(View.INVISIBLE);
			imgStep4.setVisibility(View.VISIBLE);
			imgStep5.setVisibility(View.INVISIBLE);
			imgStep6.setVisibility(View.INVISIBLE);
			break;

		case FIVE:
			imgStep1.setVisibility(View.INVISIBLE);
			imgStep2.setVisibility(View.INVISIBLE);
			imgStep3.setVisibility(View.INVISIBLE);
			imgStep4.setVisibility(View.INVISIBLE);
			imgStep5.setVisibility(View.VISIBLE);
			imgStep6.setVisibility(View.INVISIBLE);
			break;
		case FIVE_HALF:
			imgStep1.setVisibility(View.INVISIBLE);
			imgStep2.setVisibility(View.INVISIBLE);
			imgStep3.setVisibility(View.INVISIBLE);
			imgStep4.setVisibility(View.INVISIBLE);
			imgStep5.setVisibility(View.INVISIBLE);
			imgStep6.setVisibility(View.INVISIBLE);
			break;

		case SIX:
			imgStep1.setVisibility(View.INVISIBLE);
			imgStep2.setVisibility(View.INVISIBLE);
			imgStep3.setVisibility(View.INVISIBLE);
			imgStep4.setVisibility(View.INVISIBLE);
			imgStep5.setVisibility(View.INVISIBLE);
			imgStep6.setVisibility(View.VISIBLE);
			break;

		case QUIT:
			imgStep1.setVisibility(View.GONE);
			imgStep2.setVisibility(View.GONE);
			imgStep3.setVisibility(View.GONE);
			imgStep4.setVisibility(View.GONE);
			imgStep5.setVisibility(View.GONE);
			imgStep6.setVisibility(View.GONE);
			// layoutPostJolt.setOnClickListener(null);
			break;
		}
	}

	private boolean checkActive(Jolt jolt) {
		if ((jolt.isUjooltJolt() && !isFilterMine) || (jolt.isFacebook() && !isFilterFacebook)
				|| (jolt.isInstagram() && !isFilterInstagram)) {
			return false;

		} else {
			if (isFilterRecent && !isRecent(jolt.getDate())) {
				return false;
			} else if (isFilterSocial && !jolt.isSocialJolt()) {
				return false;
			} else if (isFilterTop && !jolt.isTop()) {
				return false;
			} else if (isFilterFavourite && !jolt.isLike()) {
				return false;
			} else {
				return true;
			}
		}
	}

	public void groupArrayJoltFilter() {

		ArrayList<Jolt> allJolt = new ArrayList<Jolt>();
		ArrayList<Jolt> joltFilter = new ArrayList<Jolt>();

		if (isFilterInstagram) {
			for (int i = joltHolder.arrAvailableJoltInstagram.size() - 1; i >= 0; i--) {
				Jolt joltInstagram = joltHolder.arrAvailableJoltInstagram.get(i);
				if (joltHolder.isExistInArray(joltInstagram, joltHolder.arrAvailableJolt)) {
					joltHolder.arrAvailableJoltInstagram.remove(i);
				}
			}
		}

		if (isFilterFacebook) {
			for (int i = joltHolder.arrAvailableJoltFacebook.size() - 1; i >= 0; i--) {
				Jolt joltFacebook = joltHolder.arrAvailableJoltFacebook.get(i);
				if (joltHolder.isExistInArray(joltFacebook, joltHolder.arrAvailableJolt)) {
					joltHolder.arrAvailableJoltFacebook.remove(i);
				}
			}
		}

		allJolt.addAll(joltHolder.arrAvailableJolt);
		allJolt.addAll(joltHolder.arrAvailableJoltInstagram);
		allJolt.addAll(joltHolder.arrAvailableJoltFacebook);

		for (Jolt jolt : allJolt) {
			boolean isActive = checkActive(jolt);
			boolean isMyJolt = jolt.isMyJolt();

			if (isActive) {
				if (jolt.isPublicFacebook() || jolt.isFriendFacebook(arrFriendsFacebook)
						|| jolt.isFacebook() || jolt.isInstagram() || isMyJolt) {

					/*
					 * Neu dang nhap roi thi add het nhung jolt dc favourit len
					 * ban do Neu chua dap nhap (or out roi) thi loc bo jolt da
					 * qua thoi gian
					 */
					if (checkLogin()) {
						joltFilter.add(jolt);
					} else {
						backDefaultArrayJolt();
						if (checkLifeTime(jolt)) {
							joltFilter.add(jolt);
						}
					}
				}
			}
		}

		Log.e(TAG, "arrAvailableJolt size:)" + joltHolder.arrAvailableJolt.size());
		Log.e(TAG, "array Instagram size:)" + joltHolder.arrAvailableJoltInstagram.size());
		Log.e(TAG, "array Facebook size:)" + joltHolder.arrAvailableJoltFacebook.size());

		if (curItemizedOverlays != null) {
			joltHolder.flagCurrentIndexJolt = curItemizedOverlays.index;
		}

		displayJolts(joltFilter, JoltHolder.GET_DEFAULT);

		countJoltNumberInsideScreen();

		/*
		 * hien thi lai jolt dang select khi bi update dot ngot
		 */
		if (isShowingdetail) {
			// isShowingdetail = false;
			Log.e("isShowingdetail in filter", isShowingdetail + "");

			for (Jolt jolt : joltFilter) {
				String only = jolt.getId() + jolt.getFacebookId() + jolt.getInstagramId();
				if (only.equalsIgnoreCase(FlagJoltID)) {

					showJoltDetail(jolt);

					// Set information for jolt current
					curItemizedOverlays.index = joltHolder.flagCurrentIndexJolt;
					curItemizedOverlays.setInformation();
				}
			}
		}
	}

	private boolean checkLifeTime(Jolt jolt) {
		long lt = (long) (jolt.getLifeTime() * 3600);
		long timeAlive = ConfigUtility.getCurTimeStamp() - jolt.getDate();

		if (timeAlive > lt)
			return false;
		else
			return true;
	}

	public boolean isShowingdetail = false;
	public String FlagJoltID = "";

	public static boolean isRecent(long time) {
		long recentTime = (System.currentTimeMillis() - 1000 * time);
		// Log.i("recent Time", " = " + recentTime / 3600000 + "h");

		if (recentTime <= 900000) {
			return true;
		} else {
			return false;
		}
	}

	public void setIconFilter(Filter filter, boolean isActive) {
		switch (filter) {
		case MINE:
			if (isActive) {
				imgFilterMine.setImageResource(R.drawable.ic_filter_active_jolt_mine);
			} else {
				imgFilterMine.setImageResource(R.drawable.ic_filter_inactive_jolt_mine);
			}
			break;

		case RENCENT:
			if (isActive) {
				imgFilterRecent.setImageResource(R.drawable.ic_filter_active_jolt_recently);
			} else {
				imgFilterRecent.setImageResource(R.drawable.ic_filter_inactive_jolt_recently);
			}
			break;

		case SOCIAL:
			if (isActive) {
				imgFilterSocial.setImageResource(R.drawable.ic_filter_active_jolt_social);
			} else {
				imgFilterSocial.setImageResource(R.drawable.ic_filter_inactive_jolt_social);
			}
			break;

		case FACEBOOK:
			if (isActive) {
				imgFilterFacebook.setImageResource(R.drawable.ic_filter_active_jolt_facebook);
			} else {
				imgFilterFacebook.setImageResource(R.drawable.ic_filter_inactive_jolt_facebook);
			}
			break;

		case INSTAGRAM:
			if (isActive) {
				imgFilterInstagram.setImageResource(R.drawable.ic_filter_active_jolt_instagram);
			} else {
				imgFilterInstagram.setImageResource(R.drawable.ic_filter_inactive_jolt_instagram);
			}
			break;

		case FAVOURITE:
			if (isActive) {
				imgFilterFavourite.setImageResource(R.drawable.ic_filter_active_jolt_favourite);
			} else {
				imgFilterFavourite.setImageResource(R.drawable.ic_filter_inactive_jolt_favourite);
			}
			break;

		case TOP:
			if (isActive) {
				imgFilterTop.setImageResource(R.drawable.ic_filter_active_jolt_top);
			} else {
				imgFilterTop.setImageResource(R.drawable.ic_filter_inactive_jolt_top);
			}
			break;
		}

		/*
		 * Blinking for icon option filter
		 */
		if (!isFilterRecent && !isFilterSocial && !isFilterFavourite && !isFilterTop) {
			handler_blinkIcon.removeCallbacksAndMessages(null);
		} else {
			isBlink = true;
			handler_blinkIcon.postDelayed(runnable_blinkIcon, 1000);
		}
	}

	private boolean isBlink = true;
	private Handler handler_blinkIcon = new Handler();
	private Runnable runnable_blinkIcon = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			blinkingIconOption(isBlink);
			isBlink = !isBlink;
			handler_blinkIcon.removeCallbacksAndMessages(null);
			handler_blinkIcon.postDelayed(runnable_blinkIcon, 1000);
		}
	};

	private void blinkingIconOption(boolean isBlink) {

		if (isFilterRecent) {
			if (isBlink) {
				imgFilterRecent.setImageResource(R.drawable.ic_filter_active_jolt_recently);
			} else {
				imgFilterRecent.setImageResource(R.drawable.ic_filter_inactive_jolt_recently);
			}
		}

		if (isFilterSocial) {
			if (isBlink) {
				imgFilterSocial.setImageResource(R.drawable.ic_filter_active_jolt_social);
			} else {
				imgFilterSocial.setImageResource(R.drawable.ic_filter_inactive_jolt_social);
			}
		}

		if (isFilterFavourite) {
			if (isBlink) {
				imgFilterFavourite.setImageResource(R.drawable.ic_filter_active_jolt_favourite);
			} else {
				imgFilterFavourite.setImageResource(R.drawable.ic_filter_inactive_jolt_favourite);
			}
		}

		if (isFilterTop) {
			if (isBlink) {
				imgFilterTop.setImageResource(R.drawable.ic_filter_active_jolt_top);
			} else {
				imgFilterTop.setImageResource(R.drawable.ic_filter_inactive_jolt_top);
			}
		}
	}

	public void onClickButtonLoginTwitter() {
		loginTwitter();
	}

	public void onClickLinearLayoutReportProblem() {
		isReport = true;
		sendMail();
	}

	public void onClickAbout() {
		isAbout = true;
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://mobile.ujoolt.com/cgu/"));
		startActivity(browserIntent);
	}

	public void onClickButtonPrejoltDetail() {
		hideListRejolt();
		linearLayoutJoltDetail.setVisibility(View.VISIBLE);
		ItemizedOverlays itemizedOverlays = getCurItem();
		if (itemizedOverlays != null) {
			itemizedOverlays.preJolt();
		}
	}

	public void onClickButtonNextJoltDetail() {
		hideListRejolt();
		linearLayoutJoltDetail.setVisibility(View.VISIBLE);
		ItemizedOverlays itemizedOverlays = getCurItem();
		if (itemizedOverlays != null) {
			itemizedOverlays.nextJolt();
		}
	}

	public void hideListRejolt() {
		layoutListView.setVisibility(View.INVISIBLE);
		layoutViewListRejolt.removeAllViewsInLayout();
	}

	public void onClickButtonLayoutInactiveFilter() {
		layoutActiveFilter.setVisibility(View.VISIBLE);
		layoutInactiveFilter.setVisibility(View.GONE);
	}

	public void onClickButtonLayoutFilterMine() {
		isFilterMine = !isFilterMine;
		setIconFilter(Filter.MINE, isFilterMine);

		// delayAgainFilter = true;
		handgroupFilter.post(runGroupFilter);

		if (isFilterMine) {
			isUpdatejoltNormal = true;

			posisionLast_UpdateJoltUjolt = mapView.getMapCenter();
			handler_UpdatejoltNormal.post(runnable_UpdatejoltUjolt);
		}
	}

	public void onClickButtonLayoutFilterRecent() {
		isFilterRecent = !isFilterRecent;
		isFilterSocial = false;
		isFilterFavourite = false;
		isFilterTop = false;
		setIconFilter(Filter.RENCENT, isFilterRecent);
		setIconFilter(Filter.SOCIAL, isFilterSocial);
		setIconFilter(Filter.FAVOURITE, isFilterFavourite);
		setIconFilter(Filter.TOP, isFilterTop);

		// delayAgainFilter = true;
		handgroupFilter.post(runGroupFilter);
	}

	public void onClickButtonLayoutFilterSocial() {
		isFilterSocial = !isFilterSocial;
		isFilterRecent = false;
		isFilterFavourite = false;
		isFilterTop = false;
		setIconFilter(Filter.RENCENT, isFilterRecent);
		setIconFilter(Filter.SOCIAL, isFilterSocial);
		setIconFilter(Filter.FAVOURITE, isFilterFavourite);
		setIconFilter(Filter.TOP, isFilterTop);

		// delayAgainFilter = true;
		handgroupFilter.post(runGroupFilter);
	}

	public void onClickButtonLayoutFilterFacebook() {
		isFilterFacebook = !isFilterFacebook;
		setIconFilter(Filter.FACEBOOK, isFilterFacebook);

		// delayAgainFilter = true;
		handgroupFilter.post(runGroupFilter);

		if (isFilterFacebook) {
			joltHolder.isUpdateJoltFacebook = true;

			if (joltHolder.arrAvailableJoltFacebook.size() > 0) {
				joltHolder.timeFacebook = 800;
			} else {
				joltHolder.timeFacebook = 200;
			}

			posisionLast_UpdateJoltFacebook = mapView.getMapCenter();
			joltHolder.handle_updateJoltFacebook.post(joltHolder.runnable_updateJoltFacebook);
		}
	}

	public void onClickButtonLayoutFilterInstagram() {
		isFilterInstagram = !isFilterInstagram;
		setIconFilter(Filter.INSTAGRAM, isFilterInstagram);

		Log.e("Luong", "Insgramclick");
		Log.e("Luong", "Insgramclick");
		Log.e("Luong", "Insgramclick");
		Log.e("Luong", "Insgramclick");
		Log.e("Luong", "Insgramclick");

		// delayAgainFilter = true;
		handgroupFilter.post(runGroupFilter);

		if (isFilterInstagram) {

			isUpdatejoltInstagram = true;
			if (joltHolder.arrAvailableJoltInstagram.size() > 0) {
				timeInstagram = 800;
			} else {
				timeInstagram = 200;
			}

			posisionLast_UpdateJoltInstagram = mapView.getMapCenter();
			handler_UpdatejoltInstagram.post(runnable_UpdatejoltInstagram);
		}
	}

	public void onClickButtonLayoutFilterFavourite() {
		isFilterFavourite = !isFilterFavourite;
		isFilterRecent = false;
		isFilterSocial = false;
		isFilterTop = false;
		setIconFilter(Filter.RENCENT, isFilterRecent);
		setIconFilter(Filter.SOCIAL, isFilterSocial);
		setIconFilter(Filter.FAVOURITE, isFilterFavourite);
		setIconFilter(Filter.TOP, isFilterTop);

		// delayAgainFilter = true;
		handgroupFilter.post(runGroupFilter);
	}

	public void onClickButtonLayoutFilterTop() {
		isFilterTop = !isFilterTop;
		isFilterRecent = false;
		isFilterSocial = false;
		isFilterFavourite = false;
		setIconFilter(Filter.RENCENT, isFilterRecent);
		setIconFilter(Filter.SOCIAL, isFilterSocial);
		setIconFilter(Filter.FAVOURITE, isFilterFavourite);
		setIconFilter(Filter.TOP, isFilterTop);

		// delayAgainFilter = true;
		handgroupFilter.post(runGroupFilter);
	}

	/*
	 * close layout filter
	 */
	public void onClickImgCloseFilter() {
		layoutActiveFilter.setVisibility(View.GONE);
		layoutInactiveFilter.setVisibility(View.VISIBLE);
	}

	public void onClickSearchIcon() {
		if (mode == ModeScreen.SEARCH) {
			endSearch();
			String key = txtSearchBar.getText().toString();
			searchJolt(key);
		}
	}

	public void onClickToggleButtonPush() {
		isPush = !isPush;
		ujooltSharedPreferences.putPushStatus(isPush);
		postRegistrationId();
	}

	public void onClickLinearLayoutLanguage() {
		language = !language;
		setLanguage(language);
	}

	public void onClickCancelTakePhoto() {
		if (isTakePhotoOn) {
			turnOffTakePhoto();
		}
	}

	public void onClickImageDetail() {
		ItemizedOverlays itemizedOverlays = getCurItem();
		Jolt jolt = itemizedOverlays.getCurrentJolt();
		if (jolt.getPhotoBitmap() != null) {
			if (!jolt.isVideoJolt()) {
				isShowAvatar = true;
				isBubbleDetail = false;
				// videoView.setVisibility(View.GONE);
				layoutShowAvatar.setVisibility(View.VISIBLE);
				imageViewAvatar.setVisibility(View.VISIBLE);
				// im_detail_enlager.setImageBitmap(jolt.getPhoto_bitmap());
				// imageViewAvatar.setImageBitmap(jolt.getPhoto_bitmap());
				imageViewAvatar.setImageBitmapReset(jolt.getPhotoBitmap(), 0, true);
			} else {

				// if (VideoView == null)
				// VideoView = new Intent(getBaseContext(),
				// MediaPlayerDemo_Video.class);
				//
				// // videoURLjolt = jolt.getVideoURL();
				//
				// VideoView.putExtra("videoURL", jolt.getVideoURL());
				// startActivity(VideoView);

				Log.e(TAG, "phone model: " + ConfigUtility.PHONE_MODEL);
				if (ConfigUtility.PHONE_MODEL.contains("GT-I9000")) {
					Intent intent = new Intent(getBaseContext(), VideoSaveSdCard.class);
					intent.putExtra("videoURL", jolt.getVideoURL());
					startActivity(intent);

				} else {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(jolt.getVideoURL()), "video/*");
					startActivity(intent);
				}

				// VideoViewer videoPlayer = new VideoViewer(this);
				// MediaController mediaController = new MediaController(this);
				// mediaController.setAnchorView(videoPlayer);
				// videoPlayer.setMediaController(mediaController);
				// videoPlayer.setVideoURI(Uri.parse(object.getVideoURL()));
				// LinearLayout container =
				// (LinearLayout)ObjectInfo.this.findViewById(R.id.VideoContainer);
				// container.setVisibility(VISIBLE);
				// container.addView(videoPlayer, new
				// LayoutParams(LayoutParams.FILL_PARENT,
				// LayoutParams.FILL_PARENT));
				// videoPlayer.requestFocus();
				// videoPlayer.start();

				// imageViewAvatar.setVisibility(View.GONE);
				// layoutShowAvatar.setVisibility(View.VISIBLE);
				// btnCloseVideo.setVisibility(View.VISIBLE);

				// videoView.setVisibility(View.VISIBLE);
				// runVideo(jolt.getVideoURL());
				// im_detail_enlager.setImageBitmap(jolt.getPhoto_bitmap());
				// imageViewAvatar.setImageBitmap(jolt.getPhoto_bitmap());
				// imageViewAvatar //
				// .setImageBitmapReset(jolt.getPhoto_bitmap(), 0, true);
			}
		}
	}

	private void onClickGPS() {
		joltHolder.getNearestJolts(mLatitudeE6, mLongitudeE6);
	}

	public void groupArrayJolt(byte type) {
		ArrayList<Jolt> arrAvailable = new ArrayList<Jolt>();

		if (isFilterInstagram) {
			for (int i = joltHolder.arrAvailableJoltInstagram.size() - 1; i >= 0; i--) {
				Jolt joltInstagram = joltHolder.arrAvailableJoltInstagram.get(i);
				if (joltHolder.isExistInArray(joltInstagram, joltHolder.arrAvailableJolt)) {
					joltHolder.arrAvailableJoltInstagram.remove(i);
				}
			}
		}

		if (isFilterFacebook) {
			for (int i = joltHolder.arrAvailableJoltFacebook.size() - 1; i >= 0; i--) {
				Jolt joltFacebook = joltHolder.arrAvailableJoltFacebook.get(i);
				if (joltHolder.isExistInArray(joltFacebook, joltHolder.arrAvailableJolt)) {
					joltHolder.arrAvailableJoltFacebook.remove(i);
				}
			}
		}

		arrAvailable.addAll(joltHolder.arrAvailableJoltInstagram);

		arrAvailable.addAll(joltHolder.arrAvailableJoltFacebook);

		arrAvailable.addAll(joltHolder.arrAvailableJolt);

		Log.e(TAG, "array jolt size:)" + joltHolder.arrAvailableJolt.size());
		Log.e(TAG, "array jolt in size:)" + joltHolder.arrAvailableJoltInstagram.size());
		Log.e(TAG, "array jolt fb size:)" + joltHolder.arrAvailableJoltFacebook.size());

		if (type == JoltHolder.GET_AFTER_POST) {
			displayJolts(arrAvailable, type);
		}

		Log.e(TAG, "Size Total Jolt: " + arrAvailable.size());
	}

	public void onClickButtonRejolt() {
		if (
		// view_information.isEnableDrag &&
		!curItemizedOverlays.getCurrentJolt().isMyJolt()) {
			viewInformation.isPressRejoltButton = true;
			viewInformation.isPressGoDown = true;
			viewInformation.isPressGoUp = false;
			viewInformation.isStarted = true;

		} else if (viewInformation.isEnableDelte) {
			showDialogDeleteJoltConfirm();
		}
	}

	public void onClickTextViewJoltAge() {
		// luong

		// if (linearLayoutJoltDetail.getVisibility() == View.VISIBLE) {
		// linearLayoutJoltDetail.setVisibility(View.INVISIBLE);
		// // myListView.setVisibility(View.VISIBLE);
		// // whoRejolt.setVisibility(View.VISIBLE);
		//
		// } else {
		// linearLayoutJoltDetail.setVisibility(View.VISIBLE);
		// // myListView.setVisibility(View.INVISIBLE);
		// // whoRejolt.setVisibility(View.INVISIBLE);
		// }
	}

	public boolean checkLogin() {
		if (myLoginUserId.equals("") || myLoginUserId == null) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Set onclick for Favourite
	 */

	public void onClickFavourite() {
		if (checkLogin()) {
			joltHolder.flagCurrentIndexJolt = curItemizedOverlays.index;

			ItemizedOverlays itemizedOverlays = getCurItem();
			final Jolt jolt = itemizedOverlays.getCurrentJolt();

			if (checkTimeForJoltFavourite(jolt) == true) {

				if (jolt.isLike()) {
					new AlertDialog.Builder(this).setMessage(Language.confirmUnfavourite)
							.setTitle("Alert").setCancelable(false)
							.setNegativeButton(Language.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									processForFavourite(jolt);
								}
							})
							.setPositiveButton(Language.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									hideJolt = false;
									dialog.dismiss();
								}
							}).show();
				} else {
					processForFavourite(jolt);
				}

			} else {
				hideJolt = true;
				if (jolt.isLike()) {
					new AlertDialog.Builder(this).setMessage(Language.confirmDeleteFavourite)
							.setTitle("Alert").setCancelable(false)
							.setNegativeButton(Language.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									processForFavourite(jolt);
								}
							})
							.setPositiveButton(Language.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									hideJolt = false;
									dialog.dismiss();
								}
							}).show();
				} else {
					processForFavourite(jolt);
				}
			}
		} else {
			Toast.makeText(getApplication(), Language.notifyLogin, Toast.LENGTH_LONG).show();
		}
	}

	private boolean hideJolt = false;
	public boolean postFavourite = false;

	private void processForFavourite(Jolt jolt) {
		String joltID = jolt.getId();

		if (joltID.equalsIgnoreCase("")) {
			postFavourite = true;
			joltHolder.postRejolt(jolt);
		} else {
			postFavourite(jolt);
		}
	}

	public void postFavourite(Jolt jolt) {
		String joltID = jolt.getId();
		String loginUserid = myLoginUserId;

		boolean isLike = !jolt.isLike();
		jolt.setLike(isLike);

		setIconFavourite(isLike);

		if (isLike) {
			joltHolder.addToArrFavouriteId(joltID);

			groupArrayJoltFilter();

			// Show jolt current
			joltHolder.flagJolt = curItemizedOverlays.currentPositionJolt;
			showJoltDetail(jolt);

			// Set information for jolt current
			curItemizedOverlays.index = joltHolder.flagCurrentIndexJolt;
			curItemizedOverlays.setInformation();
			joltHolder.postFavourite(joltID, loginUserid, isLike);

		} else {
			if (hideJolt) {
				if (arrItem.size() > 0) {
					curItemizedOverlays.nextJolt();
				} else {
					curItemizedOverlays.preJolt();
				}
				removeJolt(jolt);
			}
			groupArrayJoltFilter();

			joltHolder.postFavourite(joltID, loginUserid, isLike);
		}

		hideJolt = false;

	}

	public void setIconFavourite(boolean isFavourite) {
		if (isFavourite) {
			imgFavourite.setImageResource(R.drawable.ic_favourite_active);
		} else {
			imgFavourite.setImageResource(R.drawable.ic_favourite_inactive);
		}
	}

	public void removeJolt(Jolt jolt) {

		if (jolt.isFacebook()) {
			for (int i = joltHolder.arrAvailableJoltFacebook.size() - 1; i >= 0; i--) {
				if (joltHolder.arrAvailableJoltFacebook.get(i).equals(jolt))
					joltHolder.arrAvailableJoltFacebook.remove(i);
			}

		} else if (jolt.isInstagram()) {
			for (int i = joltHolder.arrAvailableJoltInstagram.size() - 1; i >= 0; i--) {
				if (joltHolder.arrAvailableJoltInstagram.get(i).equals(jolt))
					joltHolder.arrAvailableJoltInstagram.remove(i);
			}

		} else {
			for (int i = joltHolder.arrAvailableJolt.size() - 1; i >= 0; i--) {
				if (joltHolder.arrAvailableJolt.get(i).equals(jolt))
					joltHolder.arrAvailableJolt.remove(i);
			}
		}
	}

	private boolean checkTimeForJoltFavourite(Jolt jolt) {
		// long currentTime = System.currentTimeMillis() / 1000;
		// long startTime = jolt.getDate();
		// long lifeJolt = currentTime - startTime;
		//
		// if (lifeJolt > 14400) {
		// return false;
		// } else
		// return true;

		long lt = (long) (jolt.getLifeTime() * 3600);
		long timeAlive = ConfigUtility.getCurTimeStamp() - jolt.getDate();

		if (timeAlive > lt) {
			return false;
		} else
			return true;
	}

	private void showNickRejolt() {
		joltHolder.getRejoltFromId();
	}

	// public void showNickRejolt() {
	// ItemizedOverlays itemizedOverlays = getCurItem();
	// Jolt jolt = itemizedOverlays.getCurrentJolt();
	//
	// if (jolt.getNumberRejolt() > 0) {
	// joltHolder.getRejoltFromId();
	// } else {
	// textViewDistance.setVisibility(View.VISIBLE);
	// onClickImgMagicEye();
	// }
	// }

	public void onClickSetting() {
		viewSearch.disappear(SearchView.SETTING);
		if (viewLogin.isLogin) {
			viewLogin.disappear();
			viewLogin.setVisibility(View.INVISIBLE);
		} else {
			viewJolt.disappear();
			viewJolt.setVisibility(View.INVISIBLE);
		}
		layoutInactiveFilter.setVisibility(View.INVISIBLE);
		layoutActiveFilter.setVisibility(View.INVISIBLE);
	}

	public void onClickLayoutLogout() {
		if (!viewLogin.isLogin) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(Language.confirmLogout).setCancelable(false)
					.setNegativeButton(Language.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							logout();
							SessionStore.clear(getApplicationContext());

							hideAllFilter();
							// view_setting.isMoveDown = true;
							viewSetting.disappear(SettingView.REGISTER);
							// disappear(OptionsView.REGISTER);
						}
					}).setPositiveButton(Language.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public void onClickButtonShareFB() {
		if (UtilityFacebook.mFacebook.isSessionValid()) {
			if (!isShareFB) {
				isShareFB = true;
				btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook);
				toggleButtonShareFacebook.setChecked(true);
			} else {
				isShareFB = false;
				btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook_off);
				toggleButtonShareFacebook.setChecked(false);
			}
		} else {
			loginFacebook();
		}
	}

	public void onClickButtonShareTW() {
		Log.e(TAG, "onClickButtonShareTW");
		if (twitterConnector.mTwitterAuthen.hasAccessToken()) {
			if (!isShareTW) {
				isShareTW = true;
				btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter);
				toggleButtonShareTwitter.setChecked(true);
			} else {
				isShareTW = false;
				btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter_off);
				toggleButtonShareTwitter.setChecked(false);
			}
		} else {
			loginTwitter();
		}
	}

	public void onClickToggleShareFB() {
		if (UtilityFacebook.mFacebook.isSessionValid()) {
			if (toggleButtonShareFacebook.isChecked()) {
				isShareFB = true;
				btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook);
			} else {
				isShareFB = false;
				btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook_off);
			}
		} else {
			loginFacebook();
		}
	}

	public void onClickToggleShareTW() {
		Log.e(TAG, "touch tw");
		if (twitterConnector.mTwitterAuthen.hasAccessToken()) {
			if (toggleButtonShareTwitter.isChecked()) {
				isShareTW = true;
				btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter);
			} else {
				isShareTW = false;
				btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter);
			}
		} else {
			loginTwitter();
		}
	}

	private void onClickLoginUjoolt() {
		if (isEmptyLoginView()) {
			showDialogAlert(Language.signUp, Language.notifyNotEnoughInput, Language.tryAgain);
		} else {
			if (!isQuitRegister) {
				String user = txtLoginUser.getText().toString();
				String pass = txtLoginPassword.getText().toString();
				pass = Utility.encodeMD5(pass);
				joltHolder.loginUjoolt(user, pass, "UJ");
			}
		}
	}

	private boolean isEmptyLoginView() {
		if (Utility.checkEmptyEditText(txtLoginUser)
				|| Utility.checkEmptyEditText(txtLoginPassword)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Chose camera
	 */
	// public void onClickButtonTakePicture() {
	// // Intent i = new Intent(MainScreenActivity.this, DgCamActivityS.class);
	// // startActivity(i);
	//
	// Intent i;
	// if (ConfigUtility.PHONE_MODEL.equalsIgnoreCase("gt-i9000")) {
	// // if (DeviceConfig.device_name
	// // .equalsIgnoreCase(DeviceConfig.SAMSUNG)) {
	// i = new Intent(MainScreenActivity.this, DgCamActivityS.class);
	// // i = new Intent(MainScreenActivity.this, DgCamActivity.class);
	// Log.e("", "model samsung");
	// } else {
	// i = new Intent(MainScreenActivity.this, DgCamActivity.class);
	// Log.e("", "model other");
	// }
	// startActivity(i);
	// Log.d("Click camera", "ok");

	// isGetImage = true;
	// Intent intent = new Intent();
	// // intent.setType("image/*");
	// intent.setType("video/*");
	// //
	//
	// intent.setAction(Intent.ACTION_GET_CONTENT);
	// // intent.setAction(Intent.ACTION_PICK);
	// // startActivityForResult(intent, 2);
	//
	// startActivityForResult(Intent.createChooser(intent,
	// "Select Picture"),
	// GALLERY);
	// }

	public boolean isPressHeaderBubbleDetail;;

	private void onClickPropagation() {
		if (!isShowJoltSource) {
			isShowJoltSource = true;
			activePropogation();

			if (viewLogin.isLogin) {
				viewLogin.disappear();
			} else {
				viewJolt.disappear();
			}

			textViewJoltAgeDetailLite.setVisibility(View.VISIBLE);
			Jolt jolt = curItemizedOverlays.CurrentJolt;

			long curTime = ConfigUtility.getCurTimeStamp();
			long startTime = jolt.getDate();
			long lifetime = ((long) jolt.getLifeTime()) * 3600;

			if (viewInformation.countDownControl != null)
				viewInformation.countDownControl.stop();

			viewInformation.setCountDownTimer(textViewJoltAgeDetailLite, lifetime, startTime,
					curTime, true);

			disUpdateAndShowCycleForJolt();
		}
	}

	private void disUpdateAndShowCycleForJolt() {
		isPressHeaderBubbleDetail = true;
		Jolt jolt = curItemizedOverlays.CurrentJolt;
		double d = jolt.getRadius();

		GeoPoint g1 = new GeoPoint(jolt.getLatitudeE6(), jolt.getLongitudeE6());

		ArrayList<Jolt> show = new ArrayList<Jolt>();
		show.add(jolt);
		if (circleOverlay == null) {
			circleOverlay = new CircleOverlay(this, jolt.getLatitudeE6(), jolt.getLongitudeE6(),
					jolt.getRadius());
		}
		mapView.getOverlays().add(circleOverlay);

		if (ConfigUtility.scrWidth <= 320) {
			mapController.zoomToSpan((int) (d * 17), (int) (d * 17));
		} else if (ConfigUtility.scrWidth <= 480) {
			mapController.zoomToSpan((int) (d * 12), (int) (d * 12));
		} else {
			mapController.zoomToSpan((int) (d * 12), (int) (d * 12));
		}

		mapController.animateTo(g1);
		displayJolts(show, JoltHolder.GET_DEFAULT);
	}

	public void onClickLayoutHeaderDetail() {
		isPressHeaderBubbleDetail = false;

		if (viewInformation.countDownControl != null)
			viewInformation.countDownControl.stop();

		if (isShowJoltSource) {
			mapView.getOverlays().remove(circleOverlay);
			circleOverlay = null;
			isShowJoltSource = false;
			for (ItemizedOverlays itemizedOverlays : arrItem) {
				if (itemizedOverlays.ballooOverlayCircle != null) {
					itemizedOverlays.ballooOverlayCircle.setVisibility(View.GONE);
					itemizedOverlays.ballooOverlayCircle = null;
					itemizedOverlays.setInformation();
				}
			}
			// displayJolts(joltHolder.arrJolt);
			if (viewLogin.isLogin) {
				viewLogin.appear();
			} else {
				viewJolt.appear();
			}

			// groupArrayJolt(JoltHolder.GET_DEFAULT);
			groupArrayJoltFilter();

		}

		// // Log.d("Thang", "headerDetail");
		// if (isShowJoltSource) {
		// // Log.d("Thang", "isShowJoltSource " + isShowJoltSource);
		//
		// for (ItemizedOverlays itemizedOverlays : arrItem) {
		// if (itemizedOverlays.ballooOverlayCircle != null) {
		// // Log.d("Thang", "isShowJoltSource22 " + isShowJoltSource);
		// itemizedOverlays.ballooOverlayCircle
		// .setVisibility(View.GONE);
		//
		// itemizedOverlays.setInformation();
		// itemizedOverlays.ballooOverlayCircle = null;
		// }
		// }
		//
		// view_header.disappear();
		// if (view_login.isLogin) {
		// // Log.d("Thang", "isLogin");
		// view_login.appear();
		// } else {
		// // Log.d("Thang", "he he ");
		// view_jolt.appear();
		// }
		// isShowJoltSource = false;
		// } else {
		//
		// for (ItemizedOverlays itemizedOverlays : arrItem) {
		// if (itemizedOverlays.ballooOverlayCircle != null) {
		// // Log.d("Thang", "isShow2 " + isShowJoltSource);
		// itemizedOverlays.ballooOverlayCircle
		// .setVisibility(View.GONE);
		//
		// itemizedOverlays.setInformation();
		// itemizedOverlays.ballooOverlayCircle = null;
		// }
		// }
		// }

	}

	public void resetJoltCreate() {
		myPath = "";
		photoUri = null;
		UtilityFacebook.srcBitmap = null;
		txtText.setText("");
		bitmapJoltAvatar = null;
		// bm_instagram_avatar = null;

		btnUploadPhoto.setBackgroundResource(R.drawable.ic_camera_off);
		btnChangeMedia.setVisibility(View.GONE);
		JoltHolder.pathVideoToUpLoad = "";
		// GlobalValue.bmpBeforeRejolt = null;
		/*
		 * Auto hide keyboard when post jolt
		 */
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		System.gc();
	}

	public void resestForButtonUp() {
		JoltHolder.pathVideoToUpLoad = "";
		JoltHolder.isPhoto = true;
		// btnUploadPhoto.setBackgroundResource(R.drawable.ic_camera_off);
		// button_edit_photo.setVisibility(View.VISIBLE);
		// btnUploadPhoto.setBackgroundResource(R.drawable.bg_confirm_delete);

		bitmapJoltAvatar = null;

	}

	public void onClickJolt() {
		if (isJolting && !isTakePhotoOn) {
			if (isTutorialMode && step == Step.FIVE) {
				showStep(Step.FIVE_HALF);
			}
			mode = ModeScreen.JOLTS;
			String status = txtText.getText().toString();
			if (status != null && !status.equalsIgnoreCase("")) {
				mapController.animateTo(new GeoPoint(mLatitudeE6, mLongitudeE6));

				if (jolter == null) {
					jolter = new Jolt(joltHolder);
				}
				if (jolter != null) {
					jolter.setPhotoBitmap(null);
				}

				jolter.setLatitudeE6(mLatitudeE6);
				jolter.setLongitudeE6(mLongitudeE6);
				jolter.setNick(myUserName);
				jolter.setText(txtText.getText().toString());
				jolter.setPhotoBitmap(bitmapJoltAvatar);
				Log.e("cane:post jolt", "bm_jolt_avart  " + bitmapJoltAvatar);
				jolter.setDeviceID(DeviceConfig.device_id);
				jolter.setLoginType(myLoginType);
				jolter.setRadius(500);
				jolter.setLifeTime(4.0);
				jolter.setDate(ConfigUtility.getCurTimeStamp());

				Log.e("Postjolt", "reJolt zz " + ConfigUtility.getCurTimeStamp());

				jolter.setInstagramId("");
				// if (myAddress != null && !myAddress.equalsIgnoreCase("")) {
				// jolter.setAddress(myAddress);
				// } else {
				// jolter.setAddress("");
				// }
				jolter.setLoginUserid(myLoginUserId);

				if (bitmapJoltAvatar != null) {
					jolter.setPhotoBitmap(bitmapJoltAvatar);
				}

				joltHolder.postJolt(jolter, isPublicPostJolt);

				mapController.setZoom(16);

				isFilterMine = true;
				setIconFilter(Filter.MINE, isFilterMine);

				viewJolt.setOnTouchListener(null);
				imgToungeJolt.setOnTouchListener(this);
				viewJolt.isStarted = false;

				// Lemon comment 23/07/2012 move to onPostExecute in JoltHolder
				// view_search.appear(SearchView.JOLT);
				isJolting = false;

				// postToFacebook(uriUpload, status, mLati, mLongi);
				postToFacebook(photoUri, status, mLatitudeE6, mLongitudeE6);
				postToTwitter(status, myPath, mLatitudeE6, mLongitudeE6);

				// groupArrayJolt(joltHolder.GET_DEFAULT);
			} else {
				showDialogEmptyText();
			}
		}
		if (isPush) {
			postRegistrationId();
		}
	}

	public void onClickUploadPhoto() {

		Log.e("Click", "capture");
		Intent i;
		if (ConfigUtility.PHONE_MODEL.equalsIgnoreCase("gt-i9000")) {
			// i = new Intent(MainScreenActivity.this, DgCamActivityS.class);
			i = new Intent(MainScreenActivity.this, DgCamActivity.class);
			Log.e("", "model i9000");
		} else {
			i = new Intent(MainScreenActivity.this, DgCamActivity.class);
			Log.e("", "model other");
		}

		startActivity(i);
	}

	// public void onClickButtonGalery() {
	// isGetImage = true;
	// Intent intent = new Intent();
	// // intent.setType("image/*");
	// intent.setType("video/*");
	// //
	//
	// intent.setAction(Intent.ACTION_GET_CONTENT);
	// // intent.setAction(Intent.ACTION_PICK);
	// // startActivityForResult(intent, 2);
	//
	// startActivityForResult(Intent.createChooser(intent, "Select Picture"),
	// GALLERY);
	//
	// /*
	// * dialog
	// */
	// // final CharSequence[] items = {"Foo", "Bar", "Baz"};
	// //
	// // AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// // builder.setTitle("Make your selection");
	// // builder.setItems(items, new DialogInterface.OnClickListener() {
	// // public void onClick(DialogInterface dialog, int item) {
	// // // Do something with the selection
	// // }
	// // });
	// // AlertDialog alert = builder.create();
	// // alert.show();
	//
	// }

	public void backToDefaultMode() {
		txtSearchBar.setText("");
		txtSearchBar.setHint(Language.locationOrJolt);
		if (mode == ModeScreen.SEARCH) {
			endSearch();
		}
		mode = ModeScreen.JOLTS;

		clearMapOverlays();
		// joltHolder.setCoordinates(mLatitudeE6, mLongitudeE6);
		// joltHolder.getAvailableJolt(joltHolder.arrJolt);

		// groupArrayJolt();

		// displayJolts(joltHolder.arrAvailableJolt, Utility.CREATE);
		mapView.viewFullPoint(joltHolder.arrAvailableJolt, mLatitudeE6, mLongitudeE6,
				ModeScreen.JOLTS);
		// groupArrayJolt(JoltHolder.GET_DEFAULT);
		groupArrayJoltFilter();
	}

	public void setNullInformationPostJolt() {
		lblText.setText("");
		txtText.setText("");
	}

	public void inactiveFilter() {
		layoutActiveFilter.setVisibility(View.INVISIBLE);
		layoutInactiveFilter.setVisibility(View.VISIBLE);
	}

	public void activeFilter() {
		layoutActiveFilter.setVisibility(View.VISIBLE);
		layoutInactiveFilter.setVisibility(View.INVISIBLE);
	}

	public void hideAllFilter() {
		layoutActiveFilter.setVisibility(View.INVISIBLE);
		layoutInactiveFilter.setVisibility(View.INVISIBLE);
	}

	public void activePropogation() {
		layoutDetailFull.setVisibility(View.INVISIBLE);
		layoutDetailLite.setVisibility(View.VISIBLE);
	}

	public void inactivePropogation() {
		layoutDetailFull.setVisibility(View.VISIBLE);
		layoutDetailLite.setVisibility(View.INVISIBLE);
	}

	public void onClickCloseLogin() {
		inactiveFilter();
		viewLogin.isMoveDown = true;
		viewLogin.isMoveUp = false;
		hideKeyBoardLoginView();
		if (mode == ModeScreen.SEARCH) {
			backToDefaultMode();
		}
	}

	public void onClickButtonDownJolt() {
		turnOffTakePhoto();
		txtLoginUser.setSelected(false);
		hideKeyBoardLoginView();

		viewJolt.isMoveDown = true;
		viewJolt.isMoveUp = false;
		if (mode == ModeScreen.SEARCH) {
			backToDefaultMode();
		}
	}

	public void onClickCloseSetting() {
		viewSetting.setTypeAction(SettingView.QUIT);
		viewSetting.isMoveUp = true;
		viewSetting.isMoveDown = false;
		if (viewLogin.isLogin) {
			viewLogin.setVisibility(View.VISIBLE);
		} else {
			viewJolt.setVisibility(View.VISIBLE);
		}
		onClickImgCloseFilter();
		// executeShowOrHideInstagramJolt();
	}

	public boolean showDialogInstagram = false;
	public ProgressDialog dialogInstagram;

	public void executeShowOrHideInstagramJolt() {
		joltHolder.getAllJoltFromInstagram(mapView.getMapCenter(), JoltHolder.GET_DEFAULT, 5000,
				false, false);

		showDialogInstagram = true;
		dialogInstagram = ProgressDialog.show(this, "Ujoolt", Language.pleaseWait, true, false);

		groupArrayJoltFilter();
	}

	public void hideTutorial() {
		if (imageAdapter != null && imageAdapter.arrBitmaps != null) {
			imageAdapter.arrBitmaps.clear();
		}
		System.gc();
	}

	public void sendMail() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@ujoolt.com" });
		intent.putExtra(Intent.EXTRA_SUBJECT, Language.subjectMailReportProblem);
		intent.putExtra(Intent.EXTRA_TEXT, Language.bodyMailReportProblem);

		final PackageManager pm = getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
		ResolveInfo best = null;
		for (final ResolveInfo info : matches)
			if (info.activityInfo.packageName.endsWith(".gm")
					|| info.activityInfo.name.toLowerCase().contains("gmail")) {
				best = info;
			}
		if (best != null) {
			intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
		}
		startActivity(intent);
	}

	public void resetIdForItem() {
		int size = arrItem.size();
		for (int i = 0; i < size; i++) {
			ItemizedOverlays itemizedOverlays = (ItemizedOverlays) arrItem.get(i);
			itemizedOverlays.positionOfItemizedOverlay = i;
		}
		curTapItem = 0;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == Facebook.DEFAULT_AUTH_ACTIVITY_CODE) {
				Log.e("come back", "ok");
				UtilityFacebook.mFacebook.authorizeCallback(MainScreenActivity.this, requestCode,
						resultCode, data);

			} else if (requestCode == 100) {
				data.getDataString();

			}

		} else if (resultCode == ResultCode.BACK) {

		} else if (resultCode == ResultCode.START_TUTORIAL) {
			Log.e(TAG, "finish: next");
			onClickCloseSetting();
			isTutorialMode = true;
			initTutorial();
		}
	}

	// upload to facebook photo
	// private void getImageInfoSamsung() {
	// String[] projection = {
	// MediaStore.Images.Thumbnails._ID, // The columns
	// // we want
	// MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.KIND,
	// MediaStore.Images.Thumbnails.DATA };
	// String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select
	// // only
	// // mini's
	// MediaStore.Images.Thumbnails.MINI_KIND;
	//
	// String sort = MediaStore.Images.Thumbnails._ID + " DESC";
	//
	// // At the moment, this is a bit of a hack, as I'm returning ALL
	// // images, and just taking the latest one. There is a better way
	// // to narrow this down I think with a WHERE clause which is
	// // currently the selection variable
	// Cursor myCursor =
	// this.managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
	// projection, selection, null, sort);
	//
	// long imageId = 0l;
	// long thumbnailImageId = 0l;
	// // String thumbnailPath = "";
	//
	// try {
	// myCursor.moveToFirst();
	// imageId = myCursor.getLong(myCursor
	// .getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
	// thumbnailImageId = myCursor.getLong(myCursor
	// .getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
	// // thumbnailPath =
	// myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
	// } finally {
	// myCursor.close();
	// }
	//
	// // Create new Cursor to obtain the file Path for the large image
	//
	// String[] largeFileProjection = { MediaStore.Images.ImageColumns._ID,
	// MediaStore.Images.ImageColumns.DATA };
	//
	// String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
	// myCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// largeFileProjection,
	// null, null, largeFileSort);
	// String largeImagePath = "";
	//
	// try {
	// myCursor.moveToFirst();
	//
	// // This will actually give yo uthe file path location of the
	// // image.
	// largeImagePath = myCursor.getString(myCursor
	// .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
	// } finally {
	// myCursor.close();
	// }
	// // These are the two URI's you'll be interested in. They give
	// // you a handle to the actual images
	// photoUri =
	// Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// String.valueOf(imageId));
	// // Uri uriThumbnailImage =
	// Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
	// String.valueOf(thumbnailImageId));
	//
	// // I've left out the remaining code, as all I do is assign the
	// // URI's to my own objects anyways...
	// if (largeImagePath != null) {
	// myPath = largeImagePath;
	// } // else {
	// // myPath = getRealPathFromURI(photoUri);
	// // }
	// if (myPath == null) {
	// myPath = photoUri.getPath();
	// Log.e("PhotoURI", "" + photoUri);
	// }
	//
	// // InputStream in;
	// try {
	// UtilityFacebook.scaleImage(getBaseContext(), photoUri);
	// bitmapJoltAvatar = UtilityFacebook.srcBitmap;
	// // in = new FileInputStream(myPath);
	// // // decode image size (decode metadata only, not the whole
	// // // image)
	// // BitmapFactory.Options options = new BitmapFactory.Options();
	// // options.inJustDecodeBounds = true;
	// // BitmapFactory.decodeStream(in, null, options);
	// // in = null;
	// // // decode full image pre-resizeda
	// // in = new FileInputStream(myPath);
	// // // options = new BitmapFactory.Options();
	// // // calc rought re-size (this is no exact resize)
	// // final int REQUIRED_SIZE = 480;
	// // int width_tmp = options.outWidth, height_tmp = options.outHeight;
	// // int scale = 1;
	// // while (true) {
	// // if (width_tmp / 2 < REQUIRED_SIZE
	// // || height_tmp / 2 < REQUIRED_SIZE)
	// // break;
	// // width_tmp /= 2;
	// // height_tmp /= 2;
	// // scale *= 2;
	// // }
	// // BitmapFactory.Options o2 = new BitmapFactory.Options();
	// // o2.inSampleSize = scale;
	// // // options.inPurgeable = true;
	// // // options.inPreferredConfig = Bitmap.Config.RGB_565;
	// // // decode full image
	// //
	// // bm_jolt_avatar = BitmapFactory.decodeStream(new FileInputStream(
	// // myPath), null, o2);
	// if (bitmapJoltAvatar != null) {
	// btnUploadPhoto.setBackgroundDrawable(new
	// BitmapDrawable(bitmapJoltAvatar));
	// btnChangeMedia.setVisibility(View.VISIBLE);
	//
	// UtilityFacebook.data = null;
	// UtilityFacebook.bMapArray = null;
	// }
	//
	// } catch (Exception e) {
	// Log.e("Image", e.getMessage(), e);
	// }
	// }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// if (seekBar == this.seekBar) {
		// int value = (progress * 29 + 100) / 100;
		// joltHolder.setMaxDistance(value);
		// textViewDistanceSeekBar.setText("" + value + " km");
		// }
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onLocationChanged(Location location) {
		// Log.e("Change location", "ok");

		Log.e("Change location", "ok");
		Log.e("Change location", "ok");
		Log.e("Change location", "ok");

		mLatitudeE6 = (int) (location.getLatitude() * 1E6);
		mLongitudeE6 = (int) (location.getLongitude() * 1E6);

		postRegistrationId();
		// double longi = location.getLongitude();

		if (!isStarted) {
			GeoPoint geoPoint = new GeoPoint(mLatitudeE6, mLongitudeE6);
			if (geoPoint != null) {
				// if (!isBackFromNotify)
				mapController.animateTo(geoPoint);
				mapController.setZoom(16);
				if (mLatitudeE6 != geoPoint.getLatitudeE6()
						&& mLongitudeE6 != geoPoint.getLongitudeE6()) {
					clearMapOverlays();
					mLatitudeE6 = geoPoint.getLatitudeE6();
					mLongitudeE6 = geoPoint.getLongitudeE6();

					if (!isUpdateJolters) {
						isUpdateJolters = true;
					}
				}
				isStarted = true;
			}
		}
	}

	public void postRegistrationId() {
		if (CommonUtilities.REGISTRATION_ID != null
				&& !CommonUtilities.REGISTRATION_ID.equalsIgnoreCase("")) {

			String registrationId = CommonUtilities.REGISTRATION_ID;
			double lati = (double) (mLatitudeE6 / 1E6);
			double longi = (double) (mLongitudeE6 / 1E6);
			boolean isActive = isPush;
			String stringLanguage;
			if (language == Language.FRANCE) {
				stringLanguage = "fr";
			} else {
				stringLanguage = "en";
			}

			String registerId1 = registrationId.substring(0, 81);
			String registerId2 = registrationId.substring(81);

			String httpGet = null;
			try {
				httpGet = WebServiceConfig.URL_POST_REGISTRATION + "?udid="
						+ encode(DeviceConfig.getDeviceId(getBaseContext())) + "&register_id1="
						+ encode(registerId1) + "&register_id2=" + encode(registerId2)
						+ "&isactivate=" + encode("" + isActive) + "&lat=" + encode("" + lati)
						+ "&long=" + encode("" + longi) + "&lang=" + encode(stringLanguage);

				Log.e(TAG, "Post regiter ID, send: " + httpGet);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			AsyncHttpGet post = new AsyncHttpGet(this, new AsyncHttpResponseProcess(this) {

				@Override
				public void processIfResponseSuccess(String response) {
					Log.e(TAG, "Response register : " + response);
				}

			});
			post.execute(httpGet);
		}
	}

	@Override
	public void onProviderEnabled(String provider) {

		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 1000, 1,
		// MainScreenActivity.this);
		locationManager.removeUpdates(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				MainScreenActivity.this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1,
				MainScreenActivity.this);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// Called when user disables the location provider. If
		// requestLocationUpdates is called on an already disabled
		// provider, this method is called immediately.

		locationManager.removeUpdates(this);
	}//

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * Open progress dialog
	 * 
	 * @param text
	 */
	public void showProgressDialog(String text) {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "Ujoolt", text, true);
			progressDialog.setCancelable(false);
		}
	}

	/**
	 * Open progress dialog
	 */
	public void showProgressDialog() {
		showProgressDialog(Language.pleaseWait);
	}

	/**
	 * Close progress dialog
	 */
	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	public void showDialogDeleteJoltConfirm() {
		// final Dialog dialog = new Dialog(this,
		// android.R.style.Theme_Translucent_NoTitleBar);
		// // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(R.layout.layout_confirm_delete);
		// dialog.setCancelable(true);
		// TextView textViewTitleConfirmDelete = (TextView) dialog
		// .findViewById(R.id.text_view_title_confirm_delete);
		// textViewTitleConfirmDelete.setText(Language.deleteStringConfirm);
		// Button buttonCancel = (Button)
		// dialog.findViewById(R.id.button_cancel_delete);
		// buttonCancel.setText(Language.cancel);
		//
		// Button buttonConfirm = (Button)
		// dialog.findViewById(R.id.button_confirm_delete);
		// buttonConfirm.setText(Language.confirm);
		// buttonCancel.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // alert.cancel();
		// if (dialog != null) {
		// dialog.cancel();
		// }
		//
		// }
		// });
		// buttonConfirm.setOnClickListener(new View.OnClickListener() {
		// //
		// @Override
		// public void onClick(View v) {
		// // alert.cancel();
		// if (dialog != null) {
		// dialog.cancel();
		// }
		// if (curItemizedOverlays != null) {
		// curItemizedOverlays.delete();
		// } else {
		// Log.e("curItem", "null");
		// }
		//
		// // displayJolts(joltHolder.arrAvailableJolt,
		// // JoltHolder.GET_DEFAULT);
		// groupArrayJolt(JoltHolder.GET_DEFAULT);
		// }
		// });
		// dialog.show();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Language.confirmDelete).setCancelable(false)
				.setPositiveButton(Language.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}

				}).setNegativeButton(Language.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						if (dialog != null) {
							dialog.cancel();
						}
						if (curItemizedOverlays != null) {
							curItemizedOverlays.delete();
						} else {
							Log.e("curItem", "null");
						}
						// groupArrayJolt(JoltHolder.GET_DEFAULT);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showDialogExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Language.confirmExit).setCancelable(false)
				.setPositiveButton(Language.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).setNegativeButton(Language.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						quitActivity();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showDialogRequestGPS() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Language.notifyTurnOnGPS);
		builder.setCancelable(true);
		builder.setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});
		builder.setPositiveButton(Language.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.show();
	}

	public void showDialogNetworkError() {
		isShowNetworkError = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Language.notifyNoNetwork);
		builder.setMessage(Language.confirmExit);
		builder.setCancelable(true);
		builder.setPositiveButton(Language.no, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setNegativeButton(Language.yes, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				quitActivity();
			}
		});

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	public void showDialogAlert(String tittle, String message, String buttonLabel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
		builder.setTitle(tittle);
		builder.setMessage(message);
		builder.setPositiveButton(buttonLabel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	public void showDialogAuthen(String s) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(s);
		builder.setCancelable(true);
		builder.setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!isSetting) {
					isQuitRegister = true;
				}
				isLoading = false;
				if ((isFacebookConnected && !isTwitterConnected)
						|| (!isFacebookConnected && isTwitterConnected)) {
					lblMyNickname.setText(myUserName);
				}
				resetIconItems();
				// setToggleShare();
				setIconSocialNetwork();

			}
		});
		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.show();
	}

	public void showDialogAuthenError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Language.notifyAuthenError);
		builder.setCancelable(true);
		builder.setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isLoading = false;
			}
		});
		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.show();
	}

	public void showDialogJolt(String title, String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setNegativeButton(Language.close, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Jolt jolt = jolter;
				jolt.setLatitudeE6(mLatitudeE6);
				jolt.setLongitudeE6(mLongitudeE6);

				joltHolder.addJolt(joltHolder.arrJolt, jolt);
				joltHolder.addJolt(joltHolder.arrJoltInstagram, jolt);

				Log.e("Jolt jolt", "" + jolt.getLatitudeE6() + "   " + jolt.getLongitudeE6());
				mapController.animateTo(new GeoPoint(jolt.getLatitudeE6(), jolt.getLongitudeE6()));
				addItemOverlay(jolt);
				if (viewLogin.isLogin) {
					viewJolt.setVisibility(View.VISIBLE);
					viewJolt.isStarted = true;
					viewJolt.isMoveUp = false;
					viewJolt.isMoveDown = false;
					viewJolt.setOnTouchListener(null);
					imgToungeJolt.setOnTouchListener(MainScreenActivity.this);
				}

				viewInformation.setVisibility(View.INVISIBLE);
				// layoutSearch.setVisibility(View.VISIBLE);
				isJolting = false;
			}
		});
		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.show();
	}

	public void showDialogRejolt(String title, String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setNegativeButton(Language.close, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Jolt jolt = jolter;

				joltHolder.addJolt(joltHolder.arrJolt, jolt);
				joltHolder.addJolt(joltHolder.arrJoltInstagram, jolt);

				Log.e("Jolt jolt", "" + jolt.getLatitudeE6() + "   " + jolt.getLongitudeE6());
				addItemOverlay(jolt);
				mapController.animateTo(new GeoPoint(jolt.getLatitudeE6(), jolt.getLongitudeE6()));
				if (viewLogin.isLogin) {
					viewJolt.setVisibility(View.VISIBLE);
					viewJolt.isStarted = true;
					viewJolt.isMoveUp = false;
					viewJolt.isMoveDown = false;
					viewJolt.setOnTouchListener(null);
				}

				viewInformation.setVisibility(View.INVISIBLE);
				// layoutSearch.setVisibility(View.VISIBLE);
				isJolting = false;
			}
		});
		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.show();
	}

	public void showDialogEmptyText() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(Language.warning);
		builder.setMessage(Language.notifyEmptyText);
		builder.setNegativeButton(Language.close, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				showKeyBoard(txtText);
			}
		});
		AlertDialog alert = builder.create();
		alert.setCancelable(true);
		alert.show();
	}

	public void turnOffTakePhoto() {
		isTakePhotoOn = false;
	}

	public void turnOnTakePhoto() {
		isTakePhotoOn = true;
	}

	public boolean compareJolt(Jolt j1, Jolt j2) {
		return j1.getPositionArr() == j2.getPositionArr() ? true : false;
	}

	// public boolean checkCurrentJolt(int position) {
	// Jolt jolt = getCurrentTapJolt();
	// return jolt.getPositionArr() == position ? true : false;
	// }
	//
	public boolean checkCurrentJolt(Jolt jolt) {
		if (jolt != null) {
			Jolt j = getCurrentTapJolt();
			if (j != null) {
				return j.getPositionArr() == jolt.getPositionArr() ? true : false;
			} else {
				return false;
			}
		} else {
			return false;

		}
	}

	public Jolt getCurrentTapJolt() {
		// Object object = arrItem.get(curTapItem);
		// if (object instanceof ItemizedOverlays) {
		if (curTapItem >= 0 && curTapItem < arrItem.size()) {
			ItemizedOverlays itemizedOverlays = (ItemizedOverlays) arrItem.get(curTapItem);
			return itemizedOverlays != null ? itemizedOverlays.getCurrentJolt() : null;
		} else {
			return null;
		}

		// } else {
		// return null;
		// }
	}

	public ItemizedOverlays getCurItem() {
		return curItemizedOverlays;
		// return (ItemizedOverlays) mapOverlays.get(curTapItem);
	}

	public ItemizedOverlays getItem(int id) {
		// Object object = mapOverlays.get(id);
		// if (object instanceof ItemizedOverlays) {
		if (id >= 0 && id < arrItem.size()) {
			return (ItemizedOverlays) arrItem.get(id);
		} else {
			return null;
		}
		// } else {
		// return null;
		// }

	}

	private void setLanguage(boolean language) {
		this.language = language;
		Language.setLanguage(language);

		ujooltSharedPreferences.putLanguage(language);

		btnLoginFacebook.setText(Language.connectFacebook);
		btnLoginTwitter.setText(Language.connectTwitter);
		lblLoginUser.setText(Language.userName);
		lblLoginPassword.setText(Language.password);
		txtLoginUser.setHint(Language.userName);
		btnLoginUjoolt.setText(Language.login);
		btnRegisterUjoolt.setText(Language.register);

		lblLoginTitle.setText(Language.login);
		txtSearchBar.setHint(Language.locationOrJolt);
		txtText.setHint(Language.typeYourTextHere);
		lblPush.setText(Language.push);
		lblLanguage.setText(Language.languageLabel);
		lblReportProblem.setText(Language.reportAProblem);
		lblTutorial.setText(Language.tutorial);
		lblAbout.setText(Language.about);
		lblSetting.setText(Language.setting);
		lblLoginUjooltAccount.setText(Language.accountUjoolt);

		/*
		 * filter
		 */
		lblFilterActive.setText(Language.filter);
		lblFilterInactive.setText(Language.filter);
		lblFilterMine.setText(Language.myJolt);
		lblFilterRecent.setText(Language.recent);
		lblFilterFavourite.setText(Language.favourite);

		lblShareFacebook.setText(Language.shareWithFacebook);
		lblShareTwitter.setText(Language.shareWithTwitter);
		lblLogout.setText(Language.logout);

		if (language == Language.FRANCE) {
			if (isPublicPostJolt) {
				imgPrivate.setImageResource(R.drawable.btn_amis_inactive);
			} else {
				imgPrivate.setImageResource(R.drawable.btn_amis_active);
			}
		} else {
			if (isPublicPostJolt) {
				imgPrivate.setImageResource(R.drawable.btn_friend_inactive);
			} else {
				imgPrivate.setImageResource(R.drawable.btn_friend_active);
			}
		}
	}

	public void setIconSocialNetwork() {
		if (facebookConnector == null) {
			btnShareFacebook.setEnabled(false);
			btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook_off);
			toggleButtonShareFacebook.setChecked(false);

		} else {
			if (!UtilityFacebook.mFacebook.isSessionValid()) {
				btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook_off);
				toggleButtonShareFacebook.setChecked(false);

			} else {
				if (isShareFB) {
					btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook);
					toggleButtonShareFacebook.setChecked(true);
				} else {
					btnShareFacebook.setBackgroundResource(R.drawable.ic_facebook_off);
					toggleButtonShareFacebook.setChecked(false);
				}
			}
		}

		if (twitterConnector == null) {
			btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter_off);

		} else {
			if (!twitterConnector.mTwitterAuthen.hasAccessToken()) {
				btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter_off);

			} else {
				if (isShareTW) {
					btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter);
				} else {
					btnShareTwitter.setBackgroundResource(R.drawable.ic_twitter_off);
				}
			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		if (cursor == null)
			return null;

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	public void addLocationOverlay() {
		if (myLocationOverlay == null) {
			myLocationOverlay = new MyLocationOverlay(MainScreenActivity.this, mapView);
		}

		for (int i = 0; i < mapView.getOverlays().size(); i++) {
			if (myLocationOverlay.equals(mapView.getOverlays().get(i))) {
				mapView.getOverlays().remove(i);
				break;
			}
		}

		mapView.getOverlays().add(myLocationOverlay);
	}

	public void startGPS() {
		// clearMapOverlays();

		addLocationOverlay();

		locationManager.removeUpdates(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);

		myLocationOverlay.enableMyLocation();

		GeoPoint geoPoint = myLocationOverlay.getMyLocation();
		if (geoPoint != null) {
			Log.e("Da co geopoint", "ok");

			if (isShowingdetail == false) {
				mapController.animateTo(geoPoint);
			}

			mLatitudeE6 = geoPoint.getLatitudeE6();
			mLongitudeE6 = geoPoint.getLongitudeE6();

		} else {
			Log.e("Chua co geopoint", "ok");
			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					try {
						if (!isStarted) {
							GeoPoint geoPoint = myLocationOverlay.getMyLocation();
							if (geoPoint != null) {
								Log.e("" + geoPoint.getLatitudeE6(), "" + geoPoint.getLongitudeE6());

								if (isShowingdetail == false) {
									mapController.animateTo(geoPoint);
								}

								// mc.setZoom(17);
								// if (mLati != geoPoint.getLatitudeE6()
								// && mLongi != geoPoint.getLongitudeE6()) {
								// clearMapOverlays();

								mLatitudeE6 = geoPoint.getLatitudeE6();
								mLongitudeE6 = geoPoint.getLongitudeE6();

								Log.e("" + curLati, "" + curLongi);

								posisionLast_UpdateJoltInstagram = new GeoPoint(mLatitudeE6,
										mLongitudeE6);
								posisionLast_UpdateJoltUjolt = new GeoPoint(mLatitudeE6,
										mLongitudeE6);
								posisionLast_UpdateJoltFacebook = new GeoPoint(mLatitudeE6,
										mLongitudeE6);

								if (!isUpdateJolters) {
									isUpdateJolters = true;
								}
								// }
								isStarted = true;
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 400, 1, this);
		//
		// locationManager.requestLocationUpdates(
		// LocationManager.NETWORK_PROVIDER, 400, 1, this);
		//
		// myLocationOverlay.enableMyLocation();
	}

	public void resetIconItems() {
		int size = arrItem.size();
		for (int i = 0; i < size; i++) {
			ItemizedOverlays itemizedOverlays = (ItemizedOverlays) arrItem.get(i);
			itemizedOverlays.balloonView.setIcon();
		}
	}

	public void setAccount(String userName, String loginUserId, LoginType loginType) {

		if (myUserName.equals("")) {
			setMainAccount(userName, loginUserId, loginType);
		}

		switch (loginType) {
		case UJOOLT:
			myUserNameUjoolt = userName;
			myUserIdUjoolt = loginUserId;
			UserSync.IdUjoolt = loginUserId;
			isUjooltConnected = true;
			break;

		case FACEBOOK:
			lblUserNameFacebookSettingView.setText(userName);
			myUserNameFacebook = userName;
			myUserIdFacebook = loginUserId;
			UserSync.IdFacebook = loginUserId;
			isFacebookConnected = true;
			ujooltSharedPreferences.putUserNameFaceook(userName);
			ujooltSharedPreferences.putUserIdFacebook(loginUserId);
			break;

		case TWITTER:
			lblUserNameTwitterSettingView.setText(userName);
			myUserNameTwitter = userName;
			myUserIdTwitter = loginUserId;
			UserSync.IdTwitter = loginUserId;
			isTwitterConnected = true;
			ujooltSharedPreferences.putUserNameTwitter(userName);
			ujooltSharedPreferences.putUserIdTwitter(loginUserId);
			break;

		default:
			break;
		}
	}

	public void setMainAccount(String userName, String loginUserId, LoginType loginType) {
		lblMyNickname.setText(userName);
		myUserName = userName;
		myLoginUserId = loginUserId;
		myLoginType = loginType;

		ujooltSharedPreferences.putMainUserName(myUserName);
		ujooltSharedPreferences.putMainUserId(myLoginUserId);
		ujooltSharedPreferences.putMainUserType(myLoginType);

		joltHolder.getSyncUser(myLoginUserId, new GeoPoint(mLatitudeE6, mLongitudeE6));
	}

	public void showKeyBoard(EditText editText) {
		if (editText == txtText) {
			txtText.setFocusable(true);
			txtText.setFocusableInTouchMode(true);
			txtText.requestFocus();
			txtSearchBar.setFocusable(false);
			txtSearchBar.setFocusableInTouchMode(false);
			InputMethodManager inputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
			inputMethodManager.showSoftInput(editText, 0);

			if (isTutorialMode && step == Step.ONE_HALF) {
				showStep(Step.TWO);
			}

		} else {
			txtText.setFocusable(false);
			txtText.setFocusableInTouchMode(false);
			txtSearchBar.setFocusable(true);
			txtSearchBar.setFocusableInTouchMode(true);
			txtSearchBar.requestFocus();
			InputMethodManager inputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
			inputMethodManager.showSoftInput(editText, 1);
		}
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(INPUT_METHOD_SERVICE);
		// imm.showSoftInput(editText, 1);
	}

	public void hideKeyBoard(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public void hideKeyBoardLoginView() {
		hideKeyBoard(txtLoginUser);
		hideKeyBoard(txtLoginPassword);
	}

	public void startSearch() {
		isSearch = true;
		btnGps.setVisibility(View.GONE);
		btnSetting.setVisibility(View.GONE);
		ScaleAnimation scale = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
		scale.setFillAfter(true);
		scale.setDuration(300);
		scale.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				AnimationSet animSet = new AnimationSet(true);
				AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
				alpha.setDuration(300);

				animSet.addAnimation(alpha);
				animSet.setFillEnabled(true);
				animSet.setFillAfter(true);
				alpha.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {

					}
				});
				btnDeleteText.setVisibility(View.VISIBLE);
				btnDeleteText.clearAnimation();
				btnDeleteText.startAnimation(alpha);
				viewSearchItem.moveLeft();
			}
		});

		btnDeleteText.setOnClickListener(this);
		imgBackGroundSearchBar.startAnimation(scale);
	}

	public void endSearch() {
		hideKeyBoard(txtSearchBar);
		AnimationSet animSet = new AnimationSet(true);
		AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
		alpha.setDuration(300);

		animSet.addAnimation(alpha);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		alpha.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				btnDeleteText.setVisibility(View.GONE);
			}
		});
		btnDeleteText.clearAnimation();
		btnDeleteText.startAnimation(alpha);
		viewSearchItem.moveRight();
		Animation animation_up = AnimationUtils.loadAnimation(MainScreenActivity.this,
				R.anim.move_search_view_up);
		animation_up.setDuration(1500);
		searchLayout.setAnimation(animation_up);
		searchLayout.setVisibility(View.GONE);
		layoutNumberJolt.setVisibility(View.VISIBLE);

	}

	public void loginFacebook() {
		if (!isStartFacebookConnect) {
			isLoading = true;
			isStartFacebookConnect = true;

			if (facebookConnector == null) {
				facebookConnector = new FacebookConnector(FACEBOOK_APPID, this, FACEBOOK_PERMISSION);
			}
			if (UtilityFacebook.mFacebook.isSessionValid()) {
				facebookConnector.logout();
			}
			facebookConnector.login();
		}
	}

	public void loginTwitter() {
		if (!isStartTwitterConnect) {
			isLoading = true;
			isStartTwitterConnect = true;
			if (twitterConnector == null) {
				twitterConnector = new TwitterConnector(this);
			}
			twitterConnector.connect();
		}
	}

	public void callJoltView() {
		// view_login.setOnTouchListener(null);
		// // isQuitRegister = false;
		// // isStartJolt = true;
		// view_login.isLoginning = false;
		// view_login.isLogin = false;
		// view_login.isLogined = true;
		// view_jolt.scroll = view_jolt.disappearPoint;
		// view_jolt.setVisibility(View.VISIBLE);
		// isQuitRegister = true;

	}

	public void finishLogin() {

		lblMyNickname.setText(myUserName);
		isQuitRegister = true;
		ujooltSharedPreferences.putLoginStatus(true);
		ujooltSharedPreferences.putMainUserName(myUserName);
		ujooltSharedPreferences.putMainUserId(myLoginUserId);
		ujooltSharedPreferences.putMainUserType(myLoginType);

		if (myLoginType == LoginType.FACEBOOK) {
			facebookConnector.getFacebookFriend();
		}

		joltHolder.getFavouriteFromUser(new GeoPoint(mLatitudeE6, mLongitudeE6),
				ConfigUtility.getCurTimeStamp(), true, 6000, "", JoltHolder.GET_DEFAULT, false);

		if (isFilterInstagram) {
			joltHolder.getAllJoltFromInstagram(mLatitudeE6, mLongitudeE6, JoltHolder.GET_DEFAULT,
					5000);
		}

		if (isFilterFacebook && myLoginType == LoginType.FACEBOOK) {
			joltHolder.getAllJoltFromFacebook(new GeoPoint(mLatitudeE6, mLongitudeE6));
			joltHolder.getAllJoltOfMeFromFacebook();
			joltHolder.getAllJoltOfMyEventFromFacebook();
			Log.e(TAG, "login chay vao getAllJolt FB");
		}

		resetIconItems();
		setIconSocialNetwork();
	}

	public void postToFacebook(Uri photoUri, String status, int latitude, int longitude) {
		if (isShareFB && toggleButtonShareFacebook.isChecked()) {

			try {

				if (status != null && !status.equalsIgnoreCase("")) {
					if (photoUri != null) {
						Log.i("post", "ok");
						Log.i("Thang", "" + photoUri);
						facebookConnector.postInfoOnWall(photoUri, status, latitude, longitude);
					} else {
						facebookConnector.postInfoOnWall(null, status, latitude, longitude);
					}
				}

			} catch (Exception e) {
			}
		}
	}

	// public void postToTwitter(String status, String path, int latitude,
	// int longitude) {
	// if (isShareTW && toggleButtonShareTwitter.isChecked()) {
	// String imagePath = (myPath != null) ? "" : myPath;
	// // if (isExistImage) {
	// Log.i("cane: post to twtter", "" + imagePath);
	// Log.d("cane: post to twtter", "" + imagePath);
	// twitterConnector.postInfo(latitude, longitude, status, imagePath);
	// }
	// // else {
	// // if (status != null && !status.equalsIgnoreCase("")) {
	// // twitterConnector.postInfo(latitude, longitude, status, "");
	// // } else {
	// // twitterConnector.postInfo(latitude, longitude, "", "");
	// // }
	// }

	public void postToTwitter(String status, String path, int latitude, int longitude) {
		if (isShareTW && toggleButtonShareTwitter.isChecked()) {
			// if (isExistImage) {
			// twitterConnector.postInfo(latitude, longitude, status, myPath);
			// } else {
			// if (status != null && !status.equalsIgnoreCase("")) {
			// twitterConnector.postInfo(latitude, longitude, status, "");
			// } else {
			// twitterConnector.postInfo(latitude, longitude, "", "");
			// }
			// }
			if (status == null) {
				status = "";
			}
			if (path == null) {
				path = "";
			}
			twitterConnector.postInfo(latitude, longitude, status, path);
		}
	}

	public void finishAuthen() {
		Log.e(TAG, "finish authen ok");
		isLoading = false;
		setIconSocialNetwork();

	}

	private ItemizedOverlays getItemizedOverlayContainJolt(Jolt jolt) {
		for (ItemizedOverlays itemizedOverlays : arrItem) {
			Jolt jolt2 = itemizedOverlays.getCurrentJolt();
			if (jolt.groupID == jolt2.groupID) {
				return itemizedOverlays;
			}
		}
		return null;
	}

	public void clearMapOverlays() {
		for (ItemizedOverlays itemizedOverlays : arrItem) {
			if (itemizedOverlays.balloonView != null)
				itemizedOverlays.balloonView.setVisibility(View.GONE);
		}
		arrItem.clear();
	}

	// public void showJoltDetail(Jolt jolt){
	// ItemizedOverlays itemizedOverlays = checkExistLocation(jolt);
	//
	// displayNewJolt(itemizedOverlays, jolt, JoltHolder.GET_AFTER_POST);
	//
	// }

	public void showJoltDetail(byte type) {

		if (type == JoltHolder.GET_AFTER_POST) {
			int index = joltHolder.arrAvailableJolt.size() - 1;

			Jolt jolt = joltHolder.arrAvailableJolt.get(index);

			ItemizedOverlays itemizedOverlays = getItemizedOverlayContainJolt(jolt);
			// itemizedOverlays.centerMap();

			mapController.animateTo(new GeoPoint(itemizedOverlays.arrayJolt.get(0).getLatitudeE6(),
					itemizedOverlays.arrayJolt.get(0).getLongitudeE6()));

			if (itemizedOverlays != null) {

				displayNewJolt(itemizedOverlays, jolt, type);

				itemizedOverlays.balloonView.lblNumberRejolt
						.setBackgroundResource(IconView.iconMyJolt_Fb_0);

				setShowSearchLayout(View.GONE);
				viewSearch.disappear(SearchView.JOLT);
				viewInformation.setVisibility(View.VISIBLE);
				isBubbleDetail = true;
				viewInformation.appear();

			}
		}
	}

	public void showJoltDetail(Jolt jolt) {
		ItemizedOverlays itemizedOverlays = getItemizedOverlayContainJolt(jolt);
		itemizedOverlays.centerMap();

		itemizedOverlays.currentPositionJolt = joltHolder.flagJolt;
		itemizedOverlays.balloonView.setIcon();

		if (itemizedOverlays != null) {

			displayNewJolt(itemizedOverlays, jolt, JoltHolder.GET_AFTER_POST);

			hideKeyBoard(txtSearchBar);

			setShowSearchLayout(View.GONE);
			viewSearch.disappear(SearchView.JOLT);
			viewInformation.setVisibility(View.VISIBLE);
			isBubbleDetail = true;

			if (joltHolder.isRejolt) {
				joltHolder.isRejolt = false;
				curItemizedOverlays.index = joltHolder.flagCurrentIndexJolt;
			}

			viewInformation.appear();
		}
	}

	public void displayJolts(ArrayList<Jolt> arrJoltDisplay, byte type) {
		Log.e(TAG, "Start display All Jolts ");

		if (mode != ModeScreen.JOLTS) {
			clearMapOverlays();
		}

		/*
		 * Refresh all view on map and add again locationOverlay
		 */
		mapView.removeAllViews();
		addLocationOverlay();

		mapView.resetJoltStatus(arrJoltDisplay);
		mapView.regroupJolts(arrJoltDisplay);

		/*
		 * End
		 */

		curTapItem = 0;

		for (int i = 0; i < arrJoltDisplay.size(); i++) {
			Jolt jolt = arrJoltDisplay.get(i);

			ItemizedOverlays itemizedOverlays = getItemizedOverlayContainJolt(jolt);

			if (itemizedOverlays != null) {
				if (!itemizedOverlays.checkExistJolt(jolt)) {
					itemizedOverlays.addJolt(jolt);

					itemizedOverlays.setItemOverlay();

					// displayNewJolt(itemizedOverlays, jolt, type);
					// showJoltDetail(type);

					if ((i == arrJoltDisplay.size() - 1) && (jolt.isNewCreate())
							&& (jolt.isMyJolt())) {

						// itemizedOverlays.balloonView.setOriginalIcon(jolt);
						itemizedOverlays.balloonView.setSizeForIcon(jolt);
						// itemizedOverlays.balloonView.zoomIcon();
						Log.e(TAG, "ok newest: " + jolt.getText());
						jolt.setNewCreate(false);
					}
				}
			} else {
				int s = arrItem.size();
				if (jolt.isMyJolt()) {
					if (jolt.isNewCreate()) {
						ItemizedOverlays itemizedoverlay = new ItemizedOverlays(this, mapView,
								jolt, s, Utility.CREATE);
						arrItem.add(itemizedoverlay);
						jolt.setNewCreate(false);
					} else {
						ItemizedOverlays itemizedoverlay = new ItemizedOverlays(this, mapView,
								jolt, s, Utility.GROUP);
						arrItem.add(itemizedoverlay);
					}
				} else {
					ItemizedOverlays itemizedoverlay = new ItemizedOverlays(this, mapView, jolt, s,
							Utility.GROUP);
					arrItem.add(itemizedoverlay);
				}
			}
		}

		// if (mode == Utility.MODE_JOLT_SOURCE) {
		// Log.e(TAG, "Lemon - Start Mode Jolt Source ");
		// if (arrItem.size() > 0) {
		// curItemizedOverlays = arrItem.get(0);
		// curItemizedOverlays.createAndDisplayBalloonOverlayCircle(curItemizedOverlays
		// .getCurrentJolt());
		// // curItemizedOverlays.ballooOverlayCircle.setIcon();
		// }
		// } else {
		// if (!isBubbleDetail) {
		// curItemizedOverlays = null;
		// }
		// }

		showJoltDetail(type);

		/*
		 * comeback when have notification
		 */
		if (MessageBox.whenReceivedNotification) {
			int idMax = joltHolder.getIdMAX(joltHolder.arrAvailableJolt);
			Jolt jolt = joltHolder.getJolt(joltHolder.arrAvailableJolt, idMax + "");

			showJoltDetail(jolt);
			MessageBox.whenReceivedNotification = false;
		}
		// END
	}

	public void displayNewJolt(ItemizedOverlays itemizedOverlay, Jolt jolt, byte type) {
		// jolt.isNewCreate = false;

		if (type == JoltHolder.GET_AFTER_POST) {
			if (isSearch) {
				endSearch();
			}

			if (itemizedOverlay.balloonView == null) {
				itemizedOverlay.balloonView = itemizedOverlay.createBalloonOverlayView(0,
						jolt.getRadius(), type);
			}

			// itemizedOverlay.balloonView.resetIcon();
			isTapOnItem = true;
			curTapItem = itemizedOverlay.positionOfItemizedOverlay;

			curItemizedOverlays = itemizedOverlay;
			isBubbleDetail = true;

			// if (jolt.isNewCreate) {
			/*
			 * Show new jolt icon replace for the Item
			 * 
			 * @Author: Luong
			 */
			// itemizedOverlay.balloonView.setOriginalIcon(jolt);
			itemizedOverlay.balloonView.setSizeForIcon(jolt);
			itemizedOverlay.balloonView.zoomIcon();
			// jolt.isNewCreate = false;
			// }

			int size = itemizedOverlay.arrayJolt.size();
			for (int i = 0; i < size; i++) {
				Jolt jolt1 = itemizedOverlay.arrayJolt.get(i);
				if (jolt1.getId().equalsIgnoreCase(jolt.getId())) {
					itemizedOverlay.setCurrentId(i);
				}
			}

			itemizedOverlay.setInformation();

			if (viewSearch.isAvailable) {
				viewSearch.disappear(SearchView.INFO);
			}
			curItemizedOverlays = itemizedOverlay;
		}
		countJoltNumberInsideScreen();
	}

	public void quitActivity() {

		clearUjooltFile();
		Log.e(TAG, "clear");

		handler.removeCallbacks(null);
		viewLogin.stop();
		viewJolt.stop();
		viewInformation.stop();
		viewSetting.stop();
		viewSearch.stop();
		turnOffNotification();
		onDestroy();
		joltHolder.clearCache();
		Runtime.getRuntime().gc();
		System.gc();
		System.exit(1);

		getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		finish();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);

	}

	private void showNotification() {
		NotificationManager notiManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification myNotification = new Notification(R.drawable.icon, "",
				System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, RESULT_CLOSE_ALL, new Intent(
				this, MainScreenActivity.class), 0);
		myNotification.setLatestEventInfo(this, this.getString(R.string.app_name),
				this.getString(R.string.notification_summary), contentIntent);
		myNotification.flags = Notification.FLAG_AUTO_CANCEL;
		notiManager.notify(ID_NOTIFICATION, myNotification);
	}

	private void turnOffNotification() {
		NotificationManager notiManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.cancel(ID_NOTIFICATION);
	}

	public void clearUjooltFile() {
		try {
			String dirPath = "/sdcard/Ujoolt";
			File dir = new File(dirPath);
			File[] filelist = dir.listFiles();
			if (filelist.length > 0) {
				for (int i = filelist.length - 1; i >= 0; i--) {
					filelist[i].delete();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String dirPath2 = "/sdcard/UjooltCamera";
			File dir2 = new File(dirPath2);
			File[] filelist2 = dir2.listFiles();
			if (filelist2.length > 0) {
				for (int i = filelist2.length - 1; i >= 0; i--) {
					filelist2[i].delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			SessionStore.save(UtilityFacebook.mFacebook, getApplicationContext());
		}

		public void onAuthFail(String error) {
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			SessionStore.clear(getApplicationContext());
		}
	}

	public void logout() {
		setVariableOfLoginViewToDefault();

		ujooltSharedPreferences.putLoginStatus(false);
		ujooltSharedPreferences.putMainUserName("");
		ujooltSharedPreferences.putMainUserId("");
		ujooltSharedPreferences.putMainUserType("");

		if (arrFriendsFacebook == null) {
			arrFriendsFacebook = new ArrayList<String>();
		} else {
			arrFriendsFacebook.clear();
		}

		setPostPublic(true);
		isShareFB = false;
		isShareTW = false;

		txtText.setText("");
		lblUserNameFacebookSettingView.setText("");
		lblUserNameTwitterSettingView.setText("");

		facebookConnector.logout();
		twitterConnector.logout();

		joltHolder.arrAvailableJoltFacebook.clear();
		joltHolder.arrJoltFacebook.clear();
		joltHolder.arrJoltFacebookMe.clear();
		joltHolder.arrJoltFacebookMyEvent.clear();
		clearLike();

		backDefaultArrayJolt();
		groupArrayJoltFilter();

		resetIconItems();
		toggleButtonShareFacebook.setChecked(false);
		toggleButtonShareTwitter.setChecked(false);
	}

	public void backDefaultArrayJolt() {
		for (Jolt jolt : joltHolder.arrAvailableJoltFacebook) {
			jolt.setLike(false);
		}
		for (Jolt jolt : joltHolder.arrAvailableJoltInstagram) {
			jolt.setLike(false);
		}
		for (Jolt jolt : joltHolder.arrAvailableJolt) {
			jolt.setLike(false);
		}
	}

	public void clearLike() {
		arrFavouriteId.clear();
		for (int i = 0; i < joltHolder.arrAvailableJolt.size(); i++) {
			joltHolder.arrAvailableJolt.get(i).setLike(false);
		}
	}

	// public void getLocationName(final double lati, final double longi) {
	//
	// AsyncTask<String, String, String> asyn = new AsyncTask<String, String,
	// String>() {
	//
	// @Override
	// protected String doInBackground(String... params) {
	// Geocoder geo = new Geocoder(MainScreenActivity.this.getBaseContext(),
	// Locale.getDefault());
	// try {
	//
	// List<Address> addresses = geo.getFromLocation(lati, longi, 1);
	// if (addresses != null && addresses.size() > 0) {
	// Address address = addresses.get(0);
	// myAddress = "" + address.getFeatureName() + ", "
	// + address.getSubAdminArea() + ", " + address.getAdminArea() + ", "
	// + address.getCountryName();
	// Log.e("get address sucess", "ok " + myAddress);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// };
	// asyn.execute();
	// }

	boolean isCountJolt = false;
	Handler handler_countJolt = new Handler();
	Runnable runnable_countJolt = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (isCountJolt) {
				isCountJolt = false;
				handler_countJolt.removeCallbacksAndMessages(null);
				handler_countJolt.postDelayed(this, 400);
				return;
			} else {
				countJoltNumberInsideScreen();
			}
		}
	};

	// ===============COUNT JOLT NUMBER===================//

	public static ArrayList<Jolt> arrL;

	public int countJoltNumberInsideScreen() {

		int numberJolt = 0;
		Projection proj = mapView.getProjection();
		GeoPoint topLeft = proj.fromPixels(0, 0);

		GeoPoint bottomRight = proj.fromPixels(mapView.getWidth() - 1, mapView.getHeight() - 1);
		int topLat = topLeft.getLatitudeE6();
		int topLon = topLeft.getLongitudeE6();
		int bottomLat = bottomRight.getLatitudeE6();
		int bottomLon = bottomRight.getLongitudeE6();

		for (ItemizedOverlays item : arrItem) {
			if (item != null) {
				if (item.arrayJolt.size() != 0) {
					if (item.arrayJolt.get(0).getLatitudeE6() <= topLat
							&& item.arrayJolt.get(0).getLatitudeE6() >= bottomLat
							&& item.arrayJolt.get(0).getLongitudeE6() <= bottomLon
							&& item.arrayJolt.get(0).getLongitudeE6() >= topLon) {
						numberJolt += item.arrayJolt.size();
					}
				}
			}
		}

		btnNumberJolt.setTextColor(Color.WHITE);
		if (numberJolt > 1) {
			btnNumberJolt.setText(numberJolt + " Jolts visible");
		} else {
			btnNumberJolt.setText(numberJolt + " Jolt visible");
		}

		return numberJolt;
	}

	// Runnables
	Runnable mOnMapTap = new Runnable() {
		public void run() {
			Log.i(": Tap", "ok");
		}
	};

	Runnable mOnMapZoom = new Runnable() {
		public void run() {
			// Notify
			Log.i(": Zoom", "ok");
		}
	};

	Runnable mOnMapPan = new Runnable() {
		public void run() {
			Log.i(": Pan", "ok");
			isCountJolt = true;
			handler_countJolt.post(runnable_countJolt);
		}
	};

	Runnable mOnMapZoomPan = new Runnable() {
		public void run() {
			isCountJolt = true;
			handler_countJolt.post(runnable_countJolt);
		}
	};

	private boolean isUpdatejoltInstagram = false;
	private int timeInstagram = 200;
	Handler handler_UpdatejoltInstagram = new Handler();
	Runnable runnable_UpdatejoltInstagram = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(": UPDATE", "Jolt Instagram");

			if (isUpdatejoltInstagram) {
				isUpdatejoltInstagram = false;
				handler_UpdatejoltInstagram.removeCallbacksAndMessages(null);
				handler_UpdatejoltInstagram.postDelayed(this, timeInstagram);

				return;
			} else if (numberThreadInstagram <= 1) {
				// boolean isShowDialog = MyOverlay.isChangZoom;
				joltHolder.getAllJoltFromInstagram(mapView.getMapCenter(), JoltHolder.GET_DEFAULT,
						5000, false, false);
			}
		}
	};

	public boolean isUpdatejoltNormal = false;

	public Handler handler_UpdatejoltNormal = new Handler();
	public Runnable runnable_UpdatejoltUjolt = new Runnable() {

		@Override
		public void run() {
			if (isUpdatejoltNormal) {
				isUpdatejoltNormal = false;
				handler_UpdatejoltNormal.removeCallbacksAndMessages(null);
				handler_UpdatejoltNormal.postDelayed(this, 300);

				return;
			} else if (numberThreadNomal < 1) {

				boolean isShowDialog = false;
				if (MessageBox.whenReceivedNotification)
					isShowDialog = true;

				joltHolder.getAllJoltsFromLocation(mapView.getMapCenter(),

				ConfigUtility.getCurTimeStamp(), false, 6000, "", JoltHolder.GET_DEFAULT,
						isShowDialog, false);
			}
		}
	};

	public Handler handlerUpdateAllWithCycleTime = new Handler();
	/*
	 * =========================================================================/
	 * Update normal jolt with cycle time = 5p After the last update
	 */
	public Runnable runnable_Update_AllJolt = new Runnable() {

		@Override
		public void run() {

			Log.v("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");
			Log.e("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");
			Log.v("UPDATE", "***************************");

			if (isPressHeaderBubbleDetail == false) {

				Log.i(": UPDATE", "Jolt Ujolt");
				// update location
				if (isPush) {
					postRegistrationId();
				}

				if (isFilterInstagram) {
					// joltHolder.getAllJoltFromInstagram(mapView.getMapCenter(),
					// JoltHolder.GET_DEFAULT, 5000, false);
					isUpdatejoltInstagram = true;
					timeInstagram = 300;
					handler_UpdatejoltInstagram.post(runnable_UpdatejoltInstagram);
				}
				isUpdatejoltNormal = true;
				handler_UpdatejoltNormal.post(runnable_UpdatejoltUjolt);

				if (isFilterFacebook) {
					joltHolder.isUpdateJoltFacebook = true;
					joltHolder.timeFacebook = 400;
					joltHolder.handle_updateJoltFacebook
							.post(joltHolder.runnable_updateJoltFacebook);
				}
			}
			// handlerUpdateAllWithCycleTime.postDelayed(runnable_Update_AllJolt,
			// 300000);
			handlerUpdateAllWithCycleTime.postDelayed(runnable_Update_AllJolt, 300000);
		}
	};

	public Handler handgroup = new Handler();
	public boolean delayAgain = false;
	public long newDelayTimeFromNow = 400;
	/*
	 * Start this runnable when change zoom to regroup all jolts used in class:
	 * joltHolder & MyOverLay
	 */
	public Runnable runGroup = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (delayAgain) {
				delayAgain = false;
				handgroup.removeCallbacksAndMessages(null);
				handgroup.postDelayed(this, newDelayTimeFromNow);
				return;
			} else {
				// groupArrayJolt(JoltHolder.GET_DEFAULT);
				groupArrayJoltFilter();

				Log.i("-----: Refesh MAP :-----", ">>-----OK-----<<");
			}
		}
	};

	private class MapViewChangeListener implements MyCustomMapView.OnChangeListener {

		@Override
		public void onChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom,
				int oldZoom) {
			// Check values
			if ((!newCenter.equals(oldCenter)) && (newZoom != oldZoom)) {
				mHandler.post(mOnMapZoomPan);

				/*
				 * Update Item when change map center
				 */
				// updateItem_with_radius(newCenter);

			} else if (!newCenter.equals(oldCenter)) {
				mHandler.post(mOnMapPan);

				/*
				 * Update Item when change map center
				 */
				// updateItem_with_radius(newCenter);

			} else if (newZoom != oldZoom) {
				mHandler.post(mOnMapZoom);
			}
		}
	}

	private class MapViewTapListener implements MyOverlay.OnTapListener {
		@Override
		public void onTap(MapView view, GeoPoint geoPoint) {
			mHandler.post(mOnMapTap);
		}
	}

	/**
	 * @param G1
	 *            first GeoPoint
	 * @param G2
	 *            second GeoPoint
	 * @return distance between locations in meters
	 */
	public static float getDistanceBetween(GeoPoint G1, GeoPoint G2) {
		float[] results = new float[3];

		if (G1 != null && G2 != null) {
			Location.distanceBetween(G1.getLatitudeE6() / 1E6, G1.getLongitudeE6() / 1E6,
					G2.getLatitudeE6() / 1E6, G2.getLongitudeE6() / 1E6, results);
			return results[0];
		} else
			return 0;
	}

	public boolean isUpdating_JoltInstagram = false;
	public boolean isUpdating_JoltUjoolt = false;
	public GeoPoint posisionLast_UpdateJoltInstagram;
	public GeoPoint posisionLast_UpdateJoltUjolt;
	public GeoPoint posisionLast_UpdateJoltFacebook;

	private static int countRequesDisConnectInstagram = -1;
	public int numberDisConnectInstagram;
	public int numberDisConnectFacebook;
	private int distanceLever1 = 1000;
	private int distanceLever2 = 2000;
	private int distanceLever4 = 4000;

	/*
	 * New Edit: Luong
	 */
	public void updateItem_with_radius(GeoPoint geoPoint) {

		if (isStart)
			geoPoint = mapView.getMapCenter();

		float distanceUpdateNormalJolt = getDistanceBetween(posisionLast_UpdateJoltUjolt, geoPoint);
		float distanceUpdateInstagramJolt = 0;
		float distanceUpdateFacebookJolt = 0;

		if (isFilterInstagram) {
			distanceUpdateInstagramJolt = getDistanceBetween(posisionLast_UpdateJoltInstagram,
					geoPoint);
		}

		if (isFilterFacebook) {
			distanceUpdateFacebookJolt = getDistanceBetween(posisionLast_UpdateJoltFacebook,
					geoPoint);
		}

		int sizeInstagram = joltHolder.arrAvailableJoltInstagram.size();

		Log.i(": distance moved", "Normal Jolt = " + distanceUpdateNormalJolt + "m");
		Log.i(": distance moved", "Instagram Jolt = " + distanceUpdateInstagramJolt + "m");

		// Update normal jolts
		if (distanceUpdateNormalJolt >= 5000.0) {
			posisionLast_UpdateJoltUjolt = mapView.getMapCenter();
			isUpdatejoltNormal = true;
			handler_UpdatejoltNormal.post(runnable_UpdatejoltUjolt);
		}

		// Update facebook jolts
		if (isFilterFacebook && distanceUpdateFacebookJolt >= 2500.0) {
			posisionLast_UpdateJoltFacebook = mapView.getMapCenter();
			joltHolder.isUpdateJoltFacebook = true;
			joltHolder.timeFacebook = 1000;
			joltHolder.handle_updateJoltFacebook.post(joltHolder.runnable_updateJoltFacebook);
		}

		// if (distanceUpdateNormalJolt > 7000) {
		// joltHolder.arrAvailableJolt.clear();
		// }

		/*
		 * =======================================================* Update
		 * normal jolt
		 */
		if (distanceUpdateNormalJolt >= 5000.0) {

			posisionLast_UpdateJoltUjolt = mapView.getMapCenter();
			isUpdatejoltNormal = true;
			handler_UpdatejoltNormal.post(runnable_UpdatejoltUjolt);
		}
		// End =======================================================*

		/*
		 * Update instagram jolt
		 */

		int radius = 0;
		if (numberDisConnectInstagram == 0)
			radius = distanceLever4;

		else if (numberDisConnectInstagram == 1)
			radius = distanceLever1;

		else if (numberDisConnectInstagram == 2)
			radius = distanceLever2;

		// Update Instagram jolts
		if (isFilterInstagram && (distanceUpdateInstagramJolt >= radius || sizeInstagram == 0)) {

			Log.v("Instagram Radius", "= " + radius + "m");

			if (sizeInstagram == 0)
				countRequesDisConnectInstagram++;

			if (sizeInstagram == 0 && countRequesDisConnectInstagram % 4 == 0) {
				// Start Update with step = 2 touch on screen
				posisionLast_UpdateJoltInstagram = mapView.getMapCenter();
				isUpdatejoltInstagram = true;
				timeInstagram = 1000;
				handler_UpdatejoltInstagram.post(runnable_UpdatejoltInstagram);

			} else if (sizeInstagram != 0) {
				// Start Update with step = 2 touch on screen
				posisionLast_UpdateJoltInstagram = mapView.getMapCenter();
				isUpdatejoltInstagram = true;
				timeInstagram = 1000;
				handler_UpdatejoltInstagram.post(runnable_UpdatejoltInstagram);
			}
		}
	}

	// scan source media and image in sdcard
	private void scanSdCardPhoto(Uri uri) {
		String pathName = uri.getPath();
		MyMediaConnectorClientS client = new MyMediaConnectorClientS(pathName);
		MediaScannerConnection scanner = new MediaScannerConnection(MainScreenActivity.this, client);
		client.setScanner(scanner);
		scanner.connect();
	}

	public Bitmap getBitmapOfJolt() {
		return bitmapJoltAvatar;
	}

	public void setBitmapOfJolt(Bitmap bitmap) {
		MainScreenActivity.bitmapJoltAvatar = bitmap;
	}

	public LinearLayout getShowSearchLayout() {
		return searchLayout;
	}

	public void setShowSearchLayout(int view) {
		searchLayout.setVisibility(view);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				RotateAnimation animation = null;
				if (event.values[0] < 4 && event.values[0] > -4) {
					if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL) {
						// UP
						orientation = ExifInterface.ORIENTATION_NORMAL;
						animation = getRotateAnimation(0);
						degrees = 0;
					} else if (event.values[1] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_180) {
						// UP SIDE DOWN
						orientation = ExifInterface.ORIENTATION_ROTATE_180;
						animation = getRotateAnimation(180);
						degrees = 180;
					}
				} else if (event.values[1] < 4 && event.values[1] > -4) {
					if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90) {
						// LEFT
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
						animation = getRotateAnimation(90);
						degrees = 90;
					} else if (event.values[0] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_270) {
						// RIGHT
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
						animation = getRotateAnimation(270);
						degrees = 270;
					}
				}

				if (animation != null) {
					animation.setStartOffset(400);
					animation.setDuration(450);

					animation.setFillBefore(true);
					animation.setFillAfter(true);
					animation.setFillEnabled(true);

					imageViewAvatar.setAnimation(animation);
				}
			}
		}
	}

	/**
	 * Calculating the degrees needed to rotate the image imposed on the button
	 * so it is always facing the user in the right direction
	 * 
	 * @param toDegrees
	 * @return
	 */
	private RotateAnimation getRotateAnimation(float toDegrees) {
		float compensation = 0;

		if (Math.abs(degrees - toDegrees) > 180) {
			compensation = 360;
		}

		// When the device is being held on the left side (default position for
		// a camera) we need to add, not subtract from the toDegrees.
		if (toDegrees == 0) {
			compensation = -compensation;
		}

		// Creating the animation and the RELATIVE_TO_SELF means that he image
		// will rotate on it center instead of a corner.
		RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		// Adding the time needed to rotate the image
		animation.setDuration(250);

		// Set the animation to stop after reaching the desired position. With
		// out this it would return to the original state.
		animation.setFillAfter(true);

		return animation;
	}

	private void initIconJolt() {

		/*
		 * normal jolt
		 */
		IconView.iconMyJolt_Fb_0 = R.drawable.normal_jolt_blue_fb_0;
		IconView.iconMyJolt_Fb_1 = R.drawable.normal_jolt_blue_fb_1;
		IconView.iconMyJolt_Fb_2 = R.drawable.normal_jolt_blue_fb_2;
		IconView.iconMyJolt_Fb_3 = R.drawable.normal_jolt_blue_fb_3;
		IconView.iconMyJolt_Fb_4 = R.drawable.normal_jolt_blue_fb_4;
		IconView.iconMyJolt_Fb_5 = R.drawable.normal_jolt_blue_fb_5;
		IconView.iconNormalJolt_Orange_1_Fb_0 = R.drawable.normal_jolt_orange_1_fb_0;
		IconView.iconNormalJolt_Orange_1_Fb_1 = R.drawable.normal_jolt_orange_1_fb_1;
		IconView.iconNormalJolt_Orange_1_Fb_2 = R.drawable.normal_jolt_orange_1_fb_2;
		IconView.iconNormalJolt_Orange_1_Fb_3 = R.drawable.normal_jolt_orange_1_fb_3;
		IconView.iconNormalJolt_Orange_1_Fb_4 = R.drawable.normal_jolt_orange_1_fb_4;
		IconView.iconNormalJolt_Orange_1_Fb_5 = R.drawable.normal_jolt_orange_1_fb_5;
		IconView.iconNormalJolt_Orange_2_Fb_0 = R.drawable.normal_jolt_orange_2_fb_0;
		IconView.iconNormalJolt_Orange_2_Fb_1 = R.drawable.normal_jolt_orange_2_fb_1;
		IconView.iconNormalJolt_Orange_2_Fb_2 = R.drawable.normal_jolt_orange_2_fb_2;
		IconView.iconNormalJolt_Orange_2_Fb_3 = R.drawable.normal_jolt_orange_2_fb_3;
		IconView.iconNormalJolt_Orange_2_Fb_4 = R.drawable.normal_jolt_orange_2_fb_4;
		IconView.iconNormalJolt_Orange_2_Fb_5 = R.drawable.normal_jolt_orange_2_fb_5;
		IconView.iconNormalJolt_Orange_3_Fb_0 = R.drawable.normal_jolt_orange_3_fb_0;
		IconView.iconNormalJolt_Orange_3_Fb_1 = R.drawable.normal_jolt_orange_3_fb_1;
		IconView.iconNormalJolt_Orange_3_Fb_2 = R.drawable.normal_jolt_orange_3_fb_2;
		IconView.iconNormalJolt_Orange_3_Fb_3 = R.drawable.normal_jolt_orange_3_fb_3;
		IconView.iconNormalJolt_Orange_3_Fb_4 = R.drawable.normal_jolt_orange_3_fb_4;
		IconView.iconNormalJolt_Orange_3_Fb_5 = R.drawable.normal_jolt_orange_3_fb_5;
		IconView.iconNormalJolt_Orange_4_Fb_0 = R.drawable.normal_jolt_orange_4_fb_0;
		IconView.iconNormalJolt_Orange_4_Fb_1 = R.drawable.normal_jolt_orange_4_fb_1;
		IconView.iconNormalJolt_Orange_4_Fb_2 = R.drawable.normal_jolt_orange_4_fb_2;
		IconView.iconNormalJolt_Orange_4_Fb_3 = R.drawable.normal_jolt_orange_4_fb_3;
		IconView.iconNormalJolt_Orange_4_Fb_4 = R.drawable.normal_jolt_orange_4_fb_4;
		IconView.iconNormalJolt_Orange_4_Fb_5 = R.drawable.normal_jolt_orange_4_fb_5;
		IconView.iconNormalJolt_Orange_5_Fb_0 = R.drawable.normal_jolt_orange_5_fb_0;
		IconView.iconNormalJolt_Orange_5_Fb_1 = R.drawable.normal_jolt_orange_5_fb_1;
		IconView.iconNormalJolt_Orange_5_Fb_2 = R.drawable.normal_jolt_orange_5_fb_2;
		IconView.iconNormalJolt_Orange_5_Fb_3 = R.drawable.normal_jolt_orange_5_fb_3;
		IconView.iconNormalJolt_Orange_5_Fb_4 = R.drawable.normal_jolt_orange_5_fb_4;
		IconView.iconNormalJolt_Orange_5_Fb_5 = R.drawable.normal_jolt_orange_5_fb_5;

		IconView.iconMyJoltFavourite_Fb_0 = R.drawable.normal_jolt_favourite_blue_fb_0;
		IconView.iconMyJoltFavourite_Fb_1 = R.drawable.normal_jolt_favourite_blue_fb_1;
		IconView.iconMyJoltFavourite_Fb_2 = R.drawable.normal_jolt_favourite_blue_fb_2;
		IconView.iconMyJoltFavourite_Fb_3 = R.drawable.normal_jolt_favourite_blue_fb_3;
		IconView.iconMyJoltFavourite_Fb_4 = R.drawable.normal_jolt_favourite_blue_fb_4;
		IconView.iconMyJoltFavourite_Fb_5 = R.drawable.normal_jolt_favourite_blue_fb_5;
		IconView.iconNormalJoltFavourite_Orange_1_Fb_0 = R.drawable.normal_jolt_favourite_orange_1_fb_0;
		IconView.iconNormalJoltFavourite_Orange_1_Fb_1 = R.drawable.normal_jolt_favourite_orange_1_fb_1;
		IconView.iconNormalJoltFavourite_Orange_1_Fb_2 = R.drawable.normal_jolt_favourite_orange_1_fb_2;
		IconView.iconNormalJoltFavourite_Orange_1_Fb_3 = R.drawable.normal_jolt_favourite_orange_1_fb_3;
		IconView.iconNormalJoltFavourite_Orange_1_Fb_4 = R.drawable.normal_jolt_favourite_orange_1_fb_4;
		IconView.iconNormalJoltFavourite_Orange_1_Fb_5 = R.drawable.normal_jolt_favourite_orange_1_fb_5;
		IconView.iconNormalJoltFavourite_Orange_2_Fb_0 = R.drawable.normal_jolt_favourite_orange_2_fb_0;
		IconView.iconNormalJoltFavourite_Orange_2_Fb_1 = R.drawable.normal_jolt_favourite_orange_2_fb_1;
		IconView.iconNormalJoltFavourite_Orange_2_Fb_2 = R.drawable.normal_jolt_favourite_orange_2_fb_2;
		IconView.iconNormalJoltFavourite_Orange_2_Fb_3 = R.drawable.normal_jolt_favourite_orange_2_fb_3;
		IconView.iconNormalJoltFavourite_Orange_2_Fb_4 = R.drawable.normal_jolt_favourite_orange_2_fb_4;
		IconView.iconNormalJoltFavourite_Orange_2_Fb_5 = R.drawable.normal_jolt_favourite_orange_2_fb_5;
		IconView.iconNormalJoltFavourite_Orange_3_Fb_0 = R.drawable.normal_jolt_favourite_orange_3_fb_0;
		IconView.iconNormalJoltFavourite_Orange_3_Fb_1 = R.drawable.normal_jolt_favourite_orange_3_fb_1;
		IconView.iconNormalJoltFavourite_Orange_3_Fb_2 = R.drawable.normal_jolt_favourite_orange_3_fb_2;
		IconView.iconNormalJoltFavourite_Orange_3_Fb_3 = R.drawable.normal_jolt_favourite_orange_3_fb_3;
		IconView.iconNormalJoltFavourite_Orange_3_Fb_4 = R.drawable.normal_jolt_favourite_orange_3_fb_4;
		IconView.iconNormalJoltFavourite_Orange_3_Fb_5 = R.drawable.normal_jolt_favourite_orange_3_fb_5;
		IconView.iconNormalJoltFavourite_Orange_4_Fb_0 = R.drawable.normal_jolt_favourite_orange_4_fb_0;
		IconView.iconNormalJoltFavourite_Orange_4_Fb_1 = R.drawable.normal_jolt_favourite_orange_4_fb_1;
		IconView.iconNormalJoltFavourite_Orange_4_Fb_2 = R.drawable.normal_jolt_favourite_orange_4_fb_2;
		IconView.iconNormalJoltFavourite_Orange_4_Fb_3 = R.drawable.normal_jolt_favourite_orange_4_fb_3;
		IconView.iconNormalJoltFavourite_Orange_4_Fb_4 = R.drawable.normal_jolt_favourite_orange_4_fb_4;
		IconView.iconNormalJoltFavourite_Orange_4_Fb_5 = R.drawable.normal_jolt_favourite_orange_4_fb_5;
		IconView.iconNormalJoltFavourite_Orange_5_Fb_0 = R.drawable.normal_jolt_favourite_orange_5_fb_0;
		IconView.iconNormalJoltFavourite_Orange_5_Fb_1 = R.drawable.normal_jolt_favourite_orange_5_fb_1;
		IconView.iconNormalJoltFavourite_Orange_5_Fb_2 = R.drawable.normal_jolt_favourite_orange_5_fb_2;
		IconView.iconNormalJoltFavourite_Orange_5_Fb_3 = R.drawable.normal_jolt_favourite_orange_5_fb_3;
		IconView.iconNormalJoltFavourite_Orange_5_Fb_4 = R.drawable.normal_jolt_favourite_orange_5_fb_4;
		IconView.iconNormalJoltFavourite_Orange_5_Fb_5 = R.drawable.normal_jolt_favourite_orange_5_fb_5;

		IconView.iconMyJoltTop_Fb_0 = R.drawable.normal_jolt_top_blue_fb_0;
		IconView.iconMyJoltTop_Fb_1 = R.drawable.normal_jolt_top_blue_fb_1;
		IconView.iconMyJoltTop_Fb_2 = R.drawable.normal_jolt_top_blue_fb_2;
		IconView.iconMyJoltTop_Fb_3 = R.drawable.normal_jolt_top_blue_fb_3;
		IconView.iconMyJoltTop_Fb_4 = R.drawable.normal_jolt_top_blue_fb_4;
		IconView.iconMyJoltTop_Fb_5 = R.drawable.normal_jolt_top_blue_fb_5;
		IconView.iconNormalJoltTop_Orange_1_Fb_0 = R.drawable.normal_jolt_top_orange_1_fb_0;
		IconView.iconNormalJoltTop_Orange_1_Fb_1 = R.drawable.normal_jolt_top_orange_1_fb_1;
		IconView.iconNormalJoltTop_Orange_1_Fb_2 = R.drawable.normal_jolt_top_orange_1_fb_2;
		IconView.iconNormalJoltTop_Orange_1_Fb_3 = R.drawable.normal_jolt_top_orange_1_fb_3;
		IconView.iconNormalJoltTop_Orange_1_Fb_4 = R.drawable.normal_jolt_top_orange_1_fb_4;
		IconView.iconNormalJoltTop_Orange_1_Fb_5 = R.drawable.normal_jolt_top_orange_1_fb_5;
		IconView.iconNormalJoltTop_Orange_2_Fb_0 = R.drawable.normal_jolt_top_orange_2_fb_0;
		IconView.iconNormalJoltTop_Orange_2_Fb_1 = R.drawable.normal_jolt_top_orange_2_fb_1;
		IconView.iconNormalJoltTop_Orange_2_Fb_2 = R.drawable.normal_jolt_top_orange_2_fb_2;
		IconView.iconNormalJoltTop_Orange_2_Fb_3 = R.drawable.normal_jolt_top_orange_2_fb_3;
		IconView.iconNormalJoltTop_Orange_2_Fb_4 = R.drawable.normal_jolt_top_orange_2_fb_4;
		IconView.iconNormalJoltTop_Orange_2_Fb_5 = R.drawable.normal_jolt_top_orange_2_fb_5;
		IconView.iconNormalJoltTop_Orange_3_Fb_0 = R.drawable.normal_jolt_top_orange_3_fb_0;
		IconView.iconNormalJoltTop_Orange_3_Fb_1 = R.drawable.normal_jolt_top_orange_3_fb_1;
		IconView.iconNormalJoltTop_Orange_3_Fb_2 = R.drawable.normal_jolt_top_orange_3_fb_2;
		IconView.iconNormalJoltTop_Orange_3_Fb_3 = R.drawable.normal_jolt_top_orange_3_fb_3;
		IconView.iconNormalJoltTop_Orange_3_Fb_4 = R.drawable.normal_jolt_top_orange_3_fb_4;
		IconView.iconNormalJoltTop_Orange_3_Fb_5 = R.drawable.normal_jolt_top_orange_3_fb_5;
		IconView.iconNormalJoltTop_Orange_4_Fb_0 = R.drawable.normal_jolt_top_orange_4_fb_0;
		IconView.iconNormalJoltTop_Orange_4_Fb_1 = R.drawable.normal_jolt_top_orange_4_fb_1;
		IconView.iconNormalJoltTop_Orange_4_Fb_2 = R.drawable.normal_jolt_top_orange_4_fb_2;
		IconView.iconNormalJoltTop_Orange_4_Fb_3 = R.drawable.normal_jolt_top_orange_4_fb_3;
		IconView.iconNormalJoltTop_Orange_4_Fb_4 = R.drawable.normal_jolt_top_orange_4_fb_4;
		IconView.iconNormalJoltTop_Orange_4_Fb_5 = R.drawable.normal_jolt_top_orange_4_fb_5;
		IconView.iconNormalJoltTop_Orange_5_Fb_0 = R.drawable.normal_jolt_top_orange_5_fb_0;
		IconView.iconNormalJoltTop_Orange_5_Fb_1 = R.drawable.normal_jolt_top_orange_5_fb_1;
		IconView.iconNormalJoltTop_Orange_5_Fb_2 = R.drawable.normal_jolt_top_orange_5_fb_2;
		IconView.iconNormalJoltTop_Orange_5_Fb_3 = R.drawable.normal_jolt_top_orange_5_fb_3;
		IconView.iconNormalJoltTop_Orange_5_Fb_4 = R.drawable.normal_jolt_top_orange_5_fb_4;
		IconView.iconNormalJoltTop_Orange_5_Fb_5 = R.drawable.normal_jolt_top_orange_5_fb_5;

		/*
		 * icon facebook jolt
		 */
		IconView.iconFacebookJolt_Orange_1_Fb_0 = R.drawable.facebook_jolt_orange_1_fb_0;
		IconView.iconFacebookJolt_Orange_1_Fb_1 = R.drawable.facebook_jolt_orange_1_fb_1;
		IconView.iconFacebookJolt_Orange_1_Fb_2 = R.drawable.facebook_jolt_orange_1_fb_2;
		IconView.iconFacebookJolt_Orange_1_Fb_3 = R.drawable.facebook_jolt_orange_1_fb_3;
		IconView.iconFacebookJolt_Orange_1_Fb_4 = R.drawable.facebook_jolt_orange_1_fb_4;
		IconView.iconFacebookJolt_Orange_1_Fb_5 = R.drawable.facebook_jolt_orange_1_fb_5;
		IconView.iconFacebookJolt_Orange_2_Fb_0 = R.drawable.facebook_jolt_orange_2_fb_0;
		IconView.iconFacebookJolt_Orange_2_Fb_1 = R.drawable.facebook_jolt_orange_2_fb_1;
		IconView.iconFacebookJolt_Orange_2_Fb_2 = R.drawable.facebook_jolt_orange_2_fb_2;
		IconView.iconFacebookJolt_Orange_2_Fb_3 = R.drawable.facebook_jolt_orange_2_fb_3;
		IconView.iconFacebookJolt_Orange_2_Fb_4 = R.drawable.facebook_jolt_orange_2_fb_4;
		IconView.iconFacebookJolt_Orange_2_Fb_5 = R.drawable.facebook_jolt_orange_2_fb_5;
		IconView.iconFacebookJolt_Orange_3_Fb_0 = R.drawable.facebook_jolt_orange_3_fb_0;
		IconView.iconFacebookJolt_Orange_3_Fb_1 = R.drawable.facebook_jolt_orange_3_fb_1;
		IconView.iconFacebookJolt_Orange_3_Fb_2 = R.drawable.facebook_jolt_orange_3_fb_2;
		IconView.iconFacebookJolt_Orange_3_Fb_3 = R.drawable.facebook_jolt_orange_3_fb_3;
		IconView.iconFacebookJolt_Orange_3_Fb_4 = R.drawable.facebook_jolt_orange_3_fb_4;
		IconView.iconFacebookJolt_Orange_3_Fb_5 = R.drawable.facebook_jolt_orange_3_fb_5;
		IconView.iconFacebookJolt_Orange_4_Fb_0 = R.drawable.facebook_jolt_orange_4_fb_0;
		IconView.iconFacebookJolt_Orange_4_Fb_1 = R.drawable.facebook_jolt_orange_4_fb_1;
		IconView.iconFacebookJolt_Orange_4_Fb_2 = R.drawable.facebook_jolt_orange_4_fb_2;
		IconView.iconFacebookJolt_Orange_4_Fb_3 = R.drawable.facebook_jolt_orange_4_fb_3;
		IconView.iconFacebookJolt_Orange_4_Fb_4 = R.drawable.facebook_jolt_orange_4_fb_4;
		IconView.iconFacebookJolt_Orange_4_Fb_5 = R.drawable.facebook_jolt_orange_4_fb_5;
		IconView.iconFacebookJolt_Orange_5_Fb_0 = R.drawable.facebook_jolt_orange_5_fb_0;
		IconView.iconFacebookJolt_Orange_5_Fb_1 = R.drawable.facebook_jolt_orange_5_fb_1;
		IconView.iconFacebookJolt_Orange_5_Fb_2 = R.drawable.facebook_jolt_orange_5_fb_2;
		IconView.iconFacebookJolt_Orange_5_Fb_3 = R.drawable.facebook_jolt_orange_5_fb_3;
		IconView.iconFacebookJolt_Orange_5_Fb_4 = R.drawable.facebook_jolt_orange_5_fb_4;
		IconView.iconFacebookJolt_Orange_5_Fb_5 = R.drawable.facebook_jolt_orange_5_fb_5;

		IconView.iconFacebookJoltFavourite_Orange_1_Fb_0 = R.drawable.facebook_jolt_favourite_orange_1_fb_0;
		IconView.iconFacebookJoltFavourite_Orange_1_Fb_1 = R.drawable.facebook_jolt_favourite_orange_1_fb_1;
		IconView.iconFacebookJoltFavourite_Orange_1_Fb_2 = R.drawable.facebook_jolt_favourite_orange_1_fb_2;
		IconView.iconFacebookJoltFavourite_Orange_1_Fb_3 = R.drawable.facebook_jolt_favourite_orange_1_fb_3;
		IconView.iconFacebookJoltFavourite_Orange_1_Fb_4 = R.drawable.facebook_jolt_favourite_orange_1_fb_4;
		IconView.iconFacebookJoltFavourite_Orange_1_Fb_5 = R.drawable.facebook_jolt_favourite_orange_1_fb_5;
		IconView.iconFacebookJoltFavourite_Orange_2_Fb_0 = R.drawable.facebook_jolt_favourite_orange_2_fb_0;
		IconView.iconFacebookJoltFavourite_Orange_2_Fb_1 = R.drawable.facebook_jolt_favourite_orange_2_fb_1;
		IconView.iconFacebookJoltFavourite_Orange_2_Fb_2 = R.drawable.facebook_jolt_favourite_orange_2_fb_2;
		IconView.iconFacebookJoltFavourite_Orange_2_Fb_3 = R.drawable.facebook_jolt_favourite_orange_2_fb_3;
		IconView.iconFacebookJoltFavourite_Orange_2_Fb_4 = R.drawable.facebook_jolt_favourite_orange_2_fb_4;
		IconView.iconFacebookJoltFavourite_Orange_2_Fb_5 = R.drawable.facebook_jolt_favourite_orange_2_fb_5;
		IconView.iconFacebookJoltFavourite_Orange_3_Fb_0 = R.drawable.facebook_jolt_favourite_orange_3_fb_0;
		IconView.iconFacebookJoltFavourite_Orange_3_Fb_1 = R.drawable.facebook_jolt_favourite_orange_3_fb_1;
		IconView.iconFacebookJoltFavourite_Orange_3_Fb_2 = R.drawable.facebook_jolt_favourite_orange_3_fb_2;
		IconView.iconFacebookJoltFavourite_Orange_3_Fb_3 = R.drawable.facebook_jolt_favourite_orange_3_fb_3;
		IconView.iconFacebookJoltFavourite_Orange_3_Fb_4 = R.drawable.facebook_jolt_favourite_orange_3_fb_4;
		IconView.iconFacebookJoltFavourite_Orange_3_Fb_5 = R.drawable.facebook_jolt_favourite_orange_3_fb_5;
		IconView.iconFacebookJoltFavourite_Orange_4_Fb_0 = R.drawable.facebook_jolt_favourite_orange_4_fb_0;
		IconView.iconFacebookJoltFavourite_Orange_4_Fb_1 = R.drawable.facebook_jolt_favourite_orange_4_fb_1;
		IconView.iconFacebookJoltFavourite_Orange_4_Fb_2 = R.drawable.facebook_jolt_favourite_orange_4_fb_2;
		IconView.iconFacebookJoltFavourite_Orange_4_Fb_3 = R.drawable.facebook_jolt_favourite_orange_4_fb_3;
		IconView.iconFacebookJoltFavourite_Orange_4_Fb_4 = R.drawable.facebook_jolt_favourite_orange_4_fb_4;
		IconView.iconFacebookJoltFavourite_Orange_4_Fb_5 = R.drawable.facebook_jolt_favourite_orange_4_fb_5;
		IconView.iconFacebookJoltFavourite_Orange_5_Fb_0 = R.drawable.facebook_jolt_favourite_orange_5_fb_0;
		IconView.iconFacebookJoltFavourite_Orange_5_Fb_1 = R.drawable.facebook_jolt_favourite_orange_5_fb_1;
		IconView.iconFacebookJoltFavourite_Orange_5_Fb_2 = R.drawable.facebook_jolt_favourite_orange_5_fb_2;
		IconView.iconFacebookJoltFavourite_Orange_5_Fb_3 = R.drawable.facebook_jolt_favourite_orange_5_fb_3;
		IconView.iconFacebookJoltFavourite_Orange_5_Fb_4 = R.drawable.facebook_jolt_favourite_orange_5_fb_4;
		IconView.iconFacebookJoltFavourite_Orange_5_Fb_5 = R.drawable.facebook_jolt_favourite_orange_5_fb_5;

		IconView.iconFacebookJoltTop_Orange_1_Fb_0 = R.drawable.facebook_jolt_top_orange_1_fb_0;
		IconView.iconFacebookJoltTop_Orange_1_Fb_1 = R.drawable.facebook_jolt_top_orange_1_fb_1;
		IconView.iconFacebookJoltTop_Orange_1_Fb_2 = R.drawable.facebook_jolt_top_orange_1_fb_2;
		IconView.iconFacebookJoltTop_Orange_1_Fb_3 = R.drawable.facebook_jolt_top_orange_1_fb_3;
		IconView.iconFacebookJoltTop_Orange_1_Fb_4 = R.drawable.facebook_jolt_top_orange_1_fb_4;
		IconView.iconFacebookJoltTop_Orange_1_Fb_5 = R.drawable.facebook_jolt_top_orange_1_fb_5;
		IconView.iconFacebookJoltTop_Orange_2_Fb_0 = R.drawable.facebook_jolt_top_orange_2_fb_0;
		IconView.iconFacebookJoltTop_Orange_2_Fb_1 = R.drawable.facebook_jolt_top_orange_2_fb_1;
		IconView.iconFacebookJoltTop_Orange_2_Fb_2 = R.drawable.facebook_jolt_top_orange_2_fb_2;
		IconView.iconFacebookJoltTop_Orange_2_Fb_3 = R.drawable.facebook_jolt_top_orange_2_fb_3;
		IconView.iconFacebookJoltTop_Orange_2_Fb_4 = R.drawable.facebook_jolt_top_orange_2_fb_4;
		IconView.iconFacebookJoltTop_Orange_2_Fb_5 = R.drawable.facebook_jolt_top_orange_2_fb_5;
		IconView.iconFacebookJoltTop_Orange_3_Fb_0 = R.drawable.facebook_jolt_top_orange_3_fb_0;
		IconView.iconFacebookJoltTop_Orange_3_Fb_1 = R.drawable.facebook_jolt_top_orange_3_fb_1;
		IconView.iconFacebookJoltTop_Orange_3_Fb_2 = R.drawable.facebook_jolt_top_orange_3_fb_2;
		IconView.iconFacebookJoltTop_Orange_3_Fb_3 = R.drawable.facebook_jolt_top_orange_3_fb_3;
		IconView.iconFacebookJoltTop_Orange_3_Fb_4 = R.drawable.facebook_jolt_top_orange_3_fb_4;
		IconView.iconFacebookJoltTop_Orange_3_Fb_5 = R.drawable.facebook_jolt_top_orange_3_fb_5;
		IconView.iconFacebookJoltTop_Orange_4_Fb_0 = R.drawable.facebook_jolt_top_orange_4_fb_0;
		IconView.iconFacebookJoltTop_Orange_4_Fb_1 = R.drawable.facebook_jolt_top_orange_4_fb_1;
		IconView.iconFacebookJoltTop_Orange_4_Fb_2 = R.drawable.facebook_jolt_top_orange_4_fb_2;
		IconView.iconFacebookJoltTop_Orange_4_Fb_3 = R.drawable.facebook_jolt_top_orange_4_fb_3;
		IconView.iconFacebookJoltTop_Orange_4_Fb_4 = R.drawable.facebook_jolt_top_orange_4_fb_4;
		IconView.iconFacebookJoltTop_Orange_4_Fb_5 = R.drawable.facebook_jolt_top_orange_4_fb_5;
		IconView.iconFacebookJoltTop_Orange_5_Fb_0 = R.drawable.facebook_jolt_top_orange_5_fb_0;
		IconView.iconFacebookJoltTop_Orange_5_Fb_1 = R.drawable.facebook_jolt_top_orange_5_fb_1;
		IconView.iconFacebookJoltTop_Orange_5_Fb_2 = R.drawable.facebook_jolt_top_orange_5_fb_2;
		IconView.iconFacebookJoltTop_Orange_5_Fb_3 = R.drawable.facebook_jolt_top_orange_5_fb_3;
		IconView.iconFacebookJoltTop_Orange_5_Fb_4 = R.drawable.facebook_jolt_top_orange_5_fb_4;
		IconView.iconFacebookJoltTop_Orange_5_Fb_5 = R.drawable.facebook_jolt_top_orange_5_fb_5;
		// -----------

		/*
		 * icon instagram jolt
		 */
		IconView.iconInstagramJolt_Orange_1_Fb_0 = R.drawable.instagram_jolt_orange_1_fb_0;
		IconView.iconInstagramJolt_Orange_1_Fb_1 = R.drawable.instagram_jolt_orange_1_fb_1;
		IconView.iconInstagramJolt_Orange_1_Fb_2 = R.drawable.instagram_jolt_orange_1_fb_2;
		IconView.iconInstagramJolt_Orange_1_Fb_3 = R.drawable.instagram_jolt_orange_1_fb_3;
		IconView.iconInstagramJolt_Orange_1_Fb_4 = R.drawable.instagram_jolt_orange_1_fb_4;
		IconView.iconInstagramJolt_Orange_1_Fb_5 = R.drawable.instagram_jolt_orange_1_fb_5;
		IconView.iconInstagramJolt_Orange_2_Fb_0 = R.drawable.instagram_jolt_orange_2_fb_0;
		IconView.iconInstagramJolt_Orange_2_Fb_1 = R.drawable.instagram_jolt_orange_2_fb_1;
		IconView.iconInstagramJolt_Orange_2_Fb_2 = R.drawable.instagram_jolt_orange_2_fb_2;
		IconView.iconInstagramJolt_Orange_2_Fb_3 = R.drawable.instagram_jolt_orange_2_fb_3;
		IconView.iconInstagramJolt_Orange_2_Fb_4 = R.drawable.instagram_jolt_orange_2_fb_4;
		IconView.iconInstagramJolt_Orange_2_Fb_5 = R.drawable.instagram_jolt_orange_2_fb_5;
		IconView.iconInstagramJolt_Orange_3_Fb_0 = R.drawable.instagram_jolt_orange_3_fb_0;
		IconView.iconInstagramJolt_Orange_3_Fb_1 = R.drawable.instagram_jolt_orange_3_fb_1;
		IconView.iconInstagramJolt_Orange_3_Fb_2 = R.drawable.instagram_jolt_orange_3_fb_2;
		IconView.iconInstagramJolt_Orange_3_Fb_3 = R.drawable.instagram_jolt_orange_3_fb_3;
		IconView.iconInstagramJolt_Orange_3_Fb_4 = R.drawable.instagram_jolt_orange_3_fb_4;
		IconView.iconInstagramJolt_Orange_3_Fb_5 = R.drawable.instagram_jolt_orange_3_fb_5;
		IconView.iconInstagramJolt_Orange_4_Fb_0 = R.drawable.instagram_jolt_orange_4_fb_0;
		IconView.iconInstagramJolt_Orange_4_Fb_1 = R.drawable.instagram_jolt_orange_4_fb_1;
		IconView.iconInstagramJolt_Orange_4_Fb_2 = R.drawable.instagram_jolt_orange_4_fb_2;
		IconView.iconInstagramJolt_Orange_4_Fb_3 = R.drawable.instagram_jolt_orange_4_fb_3;
		IconView.iconInstagramJolt_Orange_4_Fb_4 = R.drawable.instagram_jolt_orange_4_fb_4;
		IconView.iconInstagramJolt_Orange_4_Fb_5 = R.drawable.instagram_jolt_orange_4_fb_5;
		IconView.iconInstagramJolt_Orange_5_Fb_0 = R.drawable.instagram_jolt_orange_5_fb_0;
		IconView.iconInstagramJolt_Orange_5_Fb_1 = R.drawable.instagram_jolt_orange_5_fb_1;
		IconView.iconInstagramJolt_Orange_5_Fb_2 = R.drawable.instagram_jolt_orange_5_fb_2;
		IconView.iconInstagramJolt_Orange_5_Fb_3 = R.drawable.instagram_jolt_orange_5_fb_3;
		IconView.iconInstagramJolt_Orange_5_Fb_4 = R.drawable.instagram_jolt_orange_5_fb_4;
		IconView.iconInstagramJolt_Orange_5_Fb_5 = R.drawable.instagram_jolt_orange_5_fb_5;

		IconView.iconInstagramJoltFavourite_Orange_1_Fb_0 = R.drawable.instagram_jolt_favourite_orange_1_fb_0;
		IconView.iconInstagramJoltFavourite_Orange_1_Fb_1 = R.drawable.instagram_jolt_favourite_orange_1_fb_1;
		IconView.iconInstagramJoltFavourite_Orange_1_Fb_2 = R.drawable.instagram_jolt_favourite_orange_1_fb_2;
		IconView.iconInstagramJoltFavourite_Orange_1_Fb_3 = R.drawable.instagram_jolt_favourite_orange_1_fb_3;
		IconView.iconInstagramJoltFavourite_Orange_1_Fb_4 = R.drawable.instagram_jolt_favourite_orange_1_fb_4;
		IconView.iconInstagramJoltFavourite_Orange_1_Fb_5 = R.drawable.instagram_jolt_favourite_orange_1_fb_5;
		IconView.iconInstagramJoltFavourite_Orange_2_Fb_0 = R.drawable.instagram_jolt_favourite_orange_2_fb_0;
		IconView.iconInstagramJoltFavourite_Orange_2_Fb_1 = R.drawable.instagram_jolt_favourite_orange_2_fb_1;
		IconView.iconInstagramJoltFavourite_Orange_2_Fb_2 = R.drawable.instagram_jolt_favourite_orange_2_fb_2;
		IconView.iconInstagramJoltFavourite_Orange_2_Fb_3 = R.drawable.instagram_jolt_favourite_orange_2_fb_3;
		IconView.iconInstagramJoltFavourite_Orange_2_Fb_4 = R.drawable.instagram_jolt_favourite_orange_2_fb_4;
		IconView.iconInstagramJoltFavourite_Orange_2_Fb_5 = R.drawable.instagram_jolt_favourite_orange_2_fb_5;
		IconView.iconInstagramJoltFavourite_Orange_3_Fb_0 = R.drawable.instagram_jolt_favourite_orange_3_fb_0;
		IconView.iconInstagramJoltFavourite_Orange_3_Fb_1 = R.drawable.instagram_jolt_favourite_orange_3_fb_1;
		IconView.iconInstagramJoltFavourite_Orange_3_Fb_2 = R.drawable.instagram_jolt_favourite_orange_3_fb_2;
		IconView.iconInstagramJoltFavourite_Orange_3_Fb_3 = R.drawable.instagram_jolt_favourite_orange_3_fb_3;
		IconView.iconInstagramJoltFavourite_Orange_3_Fb_4 = R.drawable.instagram_jolt_favourite_orange_3_fb_4;
		IconView.iconInstagramJoltFavourite_Orange_3_Fb_5 = R.drawable.instagram_jolt_favourite_orange_3_fb_5;
		IconView.iconInstagramJoltFavourite_Orange_4_Fb_0 = R.drawable.instagram_jolt_favourite_orange_4_fb_0;
		IconView.iconInstagramJoltFavourite_Orange_4_Fb_1 = R.drawable.instagram_jolt_favourite_orange_4_fb_1;
		IconView.iconInstagramJoltFavourite_Orange_4_Fb_2 = R.drawable.instagram_jolt_favourite_orange_4_fb_2;
		IconView.iconInstagramJoltFavourite_Orange_4_Fb_3 = R.drawable.instagram_jolt_favourite_orange_4_fb_3;
		IconView.iconInstagramJoltFavourite_Orange_4_Fb_4 = R.drawable.instagram_jolt_favourite_orange_4_fb_4;
		IconView.iconInstagramJoltFavourite_Orange_4_Fb_5 = R.drawable.instagram_jolt_favourite_orange_4_fb_5;
		IconView.iconInstagramJoltFavourite_Orange_5_Fb_0 = R.drawable.instagram_jolt_favourite_orange_5_fb_0;
		IconView.iconInstagramJoltFavourite_Orange_5_Fb_1 = R.drawable.instagram_jolt_favourite_orange_5_fb_1;
		IconView.iconInstagramJoltFavourite_Orange_5_Fb_2 = R.drawable.instagram_jolt_favourite_orange_5_fb_2;
		IconView.iconInstagramJoltFavourite_Orange_5_Fb_3 = R.drawable.instagram_jolt_favourite_orange_5_fb_3;
		IconView.iconInstagramJoltFavourite_Orange_5_Fb_4 = R.drawable.instagram_jolt_favourite_orange_5_fb_4;
		IconView.iconInstagramJoltFavourite_Orange_5_Fb_5 = R.drawable.instagram_jolt_favourite_orange_5_fb_5;

		IconView.iconInstagramJoltTop_Orange_1_Fb_0 = R.drawable.instagram_jolt_top_orange_1_fb_0;
		IconView.iconInstagramJoltTop_Orange_1_Fb_1 = R.drawable.instagram_jolt_top_orange_1_fb_1;
		IconView.iconInstagramJoltTop_Orange_1_Fb_2 = R.drawable.instagram_jolt_top_orange_1_fb_2;
		IconView.iconInstagramJoltTop_Orange_1_Fb_3 = R.drawable.instagram_jolt_top_orange_1_fb_3;
		IconView.iconInstagramJoltTop_Orange_1_Fb_4 = R.drawable.instagram_jolt_top_orange_1_fb_4;
		IconView.iconInstagramJoltTop_Orange_1_Fb_5 = R.drawable.instagram_jolt_top_orange_1_fb_5;
		IconView.iconInstagramJoltTop_Orange_2_Fb_0 = R.drawable.instagram_jolt_top_orange_2_fb_0;
		IconView.iconInstagramJoltTop_Orange_2_Fb_1 = R.drawable.instagram_jolt_top_orange_2_fb_1;
		IconView.iconInstagramJoltTop_Orange_2_Fb_2 = R.drawable.instagram_jolt_top_orange_2_fb_2;
		IconView.iconInstagramJoltTop_Orange_2_Fb_3 = R.drawable.instagram_jolt_top_orange_2_fb_3;
		IconView.iconInstagramJoltTop_Orange_2_Fb_4 = R.drawable.instagram_jolt_top_orange_2_fb_4;
		IconView.iconInstagramJoltTop_Orange_2_Fb_5 = R.drawable.instagram_jolt_top_orange_2_fb_5;
		IconView.iconInstagramJoltTop_Orange_3_Fb_0 = R.drawable.instagram_jolt_top_orange_3_fb_0;
		IconView.iconInstagramJoltTop_Orange_3_Fb_1 = R.drawable.instagram_jolt_top_orange_3_fb_1;
		IconView.iconInstagramJoltTop_Orange_3_Fb_2 = R.drawable.instagram_jolt_top_orange_3_fb_2;
		IconView.iconInstagramJoltTop_Orange_3_Fb_3 = R.drawable.instagram_jolt_top_orange_3_fb_3;
		IconView.iconInstagramJoltTop_Orange_3_Fb_4 = R.drawable.instagram_jolt_top_orange_3_fb_4;
		IconView.iconInstagramJoltTop_Orange_3_Fb_5 = R.drawable.instagram_jolt_top_orange_3_fb_5;
		IconView.iconInstagramJoltTop_Orange_4_Fb_0 = R.drawable.instagram_jolt_top_orange_4_fb_0;
		IconView.iconInstagramJoltTop_Orange_4_Fb_1 = R.drawable.instagram_jolt_top_orange_4_fb_1;
		IconView.iconInstagramJoltTop_Orange_4_Fb_2 = R.drawable.instagram_jolt_top_orange_4_fb_2;
		IconView.iconInstagramJoltTop_Orange_4_Fb_3 = R.drawable.instagram_jolt_top_orange_4_fb_3;
		IconView.iconInstagramJoltTop_Orange_4_Fb_4 = R.drawable.instagram_jolt_top_orange_4_fb_4;
		IconView.iconInstagramJoltTop_Orange_4_Fb_5 = R.drawable.instagram_jolt_top_orange_4_fb_5;
		IconView.iconInstagramJoltTop_Orange_5_Fb_0 = R.drawable.instagram_jolt_top_orange_5_fb_0;
		IconView.iconInstagramJoltTop_Orange_5_Fb_1 = R.drawable.instagram_jolt_top_orange_5_fb_1;
		IconView.iconInstagramJoltTop_Orange_5_Fb_2 = R.drawable.instagram_jolt_top_orange_5_fb_2;
		IconView.iconInstagramJoltTop_Orange_5_Fb_3 = R.drawable.instagram_jolt_top_orange_5_fb_3;
		IconView.iconInstagramJoltTop_Orange_5_Fb_4 = R.drawable.instagram_jolt_top_orange_5_fb_4;
		IconView.iconInstagramJoltTop_Orange_5_Fb_5 = R.drawable.instagram_jolt_top_orange_5_fb_5;
		// -----------
	}
}
