package com.japanappstudio.IDxPassword.activities.homescreen;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.idmanager.BillingHelper;
import com.idmanager.BillingService;
import com.japanappstudio.IDxPassword.activities.BaseActivity;
import com.japanappstudio.IDxPassword.activities.BrowserActivity;
import com.japanappstudio.IDxPassword.activities.ChoiceCSVImportType;
import com.japanappstudio.IDxPassword.activities.CopyItemActivity;
import com.japanappstudio.IDxPassword.activities.EnterOldPasswordActivity;
import com.japanappstudio.IDxPassword.activities.ImageMemoActivity;
import com.japanappstudio.IDxPassword.activities.Item;
import com.japanappstudio.IDxPassword.activities.ListIconActivity;
import com.japanappstudio.IDxPassword.activities.PasswordGeneratorActivity;
import com.japanappstudio.IDxPassword.activities.R;
import com.japanappstudio.IDxPassword.activities.RegisterEmailActivity;
import com.japanappstudio.IDxPassword.activities.SelectFileActivity;
import com.japanappstudio.IDxPassword.activities.SettingURLActivity;
import com.japanappstudio.IDxPassword.activities.SetupRemoveDataActivity;
import com.japanappstudio.IDxPassword.activities.SetupSecurityModeActivity;
import com.japanappstudio.IDxPassword.activities.slide_activity.Panel;
import com.japanappstudio.IDxPassword.activities.slide_activity.Panel.OnPanelListener;
import com.japanappstudio.IDxPassword.activities.syncloud.DropboxSettingActivity;
import com.japanappstudio.IDxPassword.activities.syncloud.GGDriveSettingActivity;
import com.japanappstudio.IDxPassword.activities.syncloud.SyncCloudActivity;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.exportcontroller.dropbox.DropBoxController;
import com.japanappstudio.IDxPassword.exportcontroller.dropbox.ReadFileViaDropBox;
import com.japanappstudio.IDxPassword.exportcontroller.ggdrive.DownloadCSVViaGGDrive;
import com.japanappstudio.IDxPassword.exportcontroller.ggdrive.GGDriveUploadCSVController;
import com.japanappstudio.IDxPassword.exportcontroller.ggdrive.ReadCSVViaGGDrive;
import com.japanappstudio.IDxPassword.idletime.ControlApplication;
import com.japanappstudio.IDxPassword.idxpwdatabase.ElementID;
import com.japanappstudio.IDxPassword.idxpwdatabase.GroupFolder;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.Password;
import com.japanappstudio.IDxPassword.util.NetworkUtility;

import exp.mtparet.dragdrop.adapter.FolderListViewAdapter;
import exp.mtparet.dragdrop.adapter.ItemAdapter;
import exp.mtparet.dragdrop.view.DndListViewFolder;
import exp.mtparet.dragdrop.view.ListViewDragDrop;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
@SuppressWarnings("deprecation")
public class HomeScreeenActivity extends BaseActivity implements
		OnClickListener {

	// ==========================Control define ====================
	public static int HOME_SETTING = 0;
	public static int HOME_INFO = 2;
	public static int HOME_EDIT_IDx_PASS = 2;
	private FrameLayout mainRootLayout;
	// private LinearLayout mainRootLayout;

	private ListViewDragDrop idListView;
	private DndListViewFolder folderListView;

	/* layout drag */
	private RelativeLayout layoutDrag;
	private ImageView imageDrag;
	private TextView txtIdName;
	private TextView txtIdUrl;

	private Button btnSetting;
	private Button btnAddNewFolder;
	private Button btnAddNewId;
	private Button btnEdit;
	private Button btnReturn;
	private Button btnSync;
	private Button btnInfo;
	private Button btnSearch;
	private Button btnClearTextSearch;
	private EditText mEditTextSearch;

	private Panel panelSetting;
	private Panel panelInfo;
	private Panel panelEditIdxpass;
	private boolean isPanelVisiable = false;

	// ===========================Class Define =====================
	private ItemAdapter itemAdapter;
	private FolderListViewAdapter folderListViewAdapter;
	private ElementID oneItemSelected;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	private ArrayList<GroupFolder> mFolderListItems = new ArrayList<GroupFolder>();
	private ArrayList<ElementID> mIdListItems = new ArrayList<ElementID>();
	private IdManagerPreference mPref;
	// ============================Variable Define ==================

	private Context context;
	private boolean isEdit = false;
	private boolean isSearchMode = false;
	private int positionReturnedByHandler;
	private int currentFolderItem = 0;
	private int currentFolderId;
	private int mCurrentFolderPosition;
	private int mCurrentFirstVisibleFolderItem;
	private int mCurrentId;
	private String mTextSearch = "";
	private boolean isDnd = false;
	private boolean isDndElement = false;
	private AdView adview;
	// private Drawable mDrawableIcon;

	private Handler mMainHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.arg1 == Contants.DIALOG_DELETE_FOLDER) {
				showDialog(Contants.DIALOG_DELETE_FOLDER);
			} else if (msg.arg1 == Contants.DIALOG_EDIT_FOLDER)
				showDialog(Contants.DIALOG_EDIT_FOLDER);
			else if (msg.arg1 == Contants.DIALOG_DELETE_ID)
				showDialog(Contants.DIALOG_DELETE_ID);
			else if (msg.arg1 == Contants.DIALOG_EDIT_ID)
				showDialog(Contants.DIALOG_EDIT_ID);
			else if (msg.arg1 == Contants.IS_SEARCH_MODE) {
				isSearchMode = false;
				currentFolderItem = 0;
			}
			positionReturnedByHandler = msg.arg2;
		};
	};

	// variable of setting class

	private static final String ID_ITEMS_PAYMENT_TO_UN_LIMIT = "0000000000000001";
	private static final String ID_ITEMS_PAYMENT_TO_NO_AD = "0000000000000002";
	private static final String ID_ITEMS_PAYMENT_TO_EXPORT = "0000000000000003";
	private CharSequence[] mListDataChoice;
	private CharSequence mSelectedFile = "";
	private CharSequence[] mListDataChoiceTemp;
	private IDxPWDataBaseHandler mDataBaseHandler;
	private boolean isExportData;
	// private float x0, y0;
	// private float xTouch, yTouch;
	private int widthScreen;
	// private LinearLayout lnSetting;

	// /////////////////////////////////////////////////////////////////////////
	// Your app-specific settings. //
	// /////////////////////////////////////////////////////////////////////////

	// Replace this with your app key and secret assigned by Dropbox.
	// Note that this is a really insecure way to do this, and you shouldn't
	// ship code which contains your key & secret in such an obvious way.
	// Obfuscation is good.
	// final static private String APP_KEY = "667sgm6m2mdu384";
	// final static private String APP_SECRET = "0ozy2rvw6ktyrnt";

	// If you'd like to change the access type to the full Dropbox instead of
	// an app folder, change this value.
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

	// You don't need to change these, leave them alone.
	final static private String ACCOUNT_PREFS_NAME = "prefs";
	final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";

	private DropboxAPI<AndroidAuthSession> mApi;

	/* gg drive */
	private static Drive service;
	private GoogleAccountCredential credential;

	private String fileExportName;
	private static final int PAYMENT_TO_UNLIMIT_ITEMS = 0;
	private static final int PAYMENT_TO_NO_AD = 1;
	private static final int PAYMENT_TO_EXPORT = 2;
	public int modePayment;
	private List<GroupFolder> mGList;
	private List<ElementID> mEList;
	private List<Password> mPList;
	private int sizeOfGList;
	private int sizeOfEList;
	private int sizeOfPList;
	private String mGGAccountName;

	private ImageView mImgGGDrive;
	private ImageView mImgDropbox;
	public static String sercurity_mode;
	private String modes[] = { "Off", "", "1 ", "3 ", "5 ", "10 " };
	private TextView textModeSercurity;

	private static final String TAG = "BillingService";
	public Handler mTransactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "Transaction complete");
			Log.i(TAG, "Transaction status: "
					+ BillingHelper.latestPurchase.purchaseState);
			Log.i(TAG, "Item purchased is: "
					+ BillingHelper.latestPurchase.productId);

			if (BillingHelper.latestPurchase.isPurchased()) {
				if (modePayment == PAYMENT_TO_UNLIMIT_ITEMS) {
					mPref.setIsPaymentUnlimit(true);
				} else if (modePayment == PAYMENT_TO_NO_AD) {
					mPref.setIsPaymentNoAd(true);
				} else if (modePayment == PAYMENT_TO_EXPORT) {
					mPref.setIsPaymentExport(true);
				}
			}
		};

	};
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			Log.e("dfkhdf", "dfdhf " + msg.arg1);
			if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_FAILED)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_FAILED);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_SUCCESS)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_SUCCESS);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED);
			else if (msg.arg1 == Contants.DIALOG_NO_DATA_CLOUD)
				showDialog(Contants.DIALOG_NO_DATA_CLOUD);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_LIST_DATA) {
				Object object = msg.obj;

				ArrayList<String> mFileList = (ArrayList<String>) object;
				mListDataChoice = new String[mFileList.size()];
				mListDataChoiceTemp = new String[mFileList.size()];
				for (int i = 0; i < mFileList.size(); i++) {
					mListDataChoice[i] = mFileList.get(i);
					mListDataChoiceTemp[i] = mFileList.get(i);
				}

				Intent intent = new Intent(HomeScreeenActivity.this,
						SelectFileActivity.class);
				intent.putExtra("listFile", mFileList);
				startActivity(intent);

				if (mListDataChoiceTemp.length > 0)
					showDialog(Contants.DIALOG_MESSAGE_CHOICE_DATA_READ);
			} else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD) {
				showDialog(Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD);
			} else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED) {
				Intent intent = new Intent(HomeScreeenActivity.this,
						ChoiceCSVImportType.class);
				intent.putExtra(Contants.KEY_CHOICE_CSV_FILE, mSelectedFile);
				startActivity(intent);
				// showDialog(Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED);
			} else if (msg.arg1 == Contants.DIALOG_MESSAGE_FOLDER_EXISTED)
				showDialog(Contants.DIALOG_MESSAGE_FOLDER_EXISTED);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_FOLDER_ERROR)
				showDialog(Contants.DIALOG_MESSAGE_FOLDER_ERROR);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR)
				showDialog(Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR);
		};
	};

	// variable info activity
	private WebView webViewInfo;
	private String urlInfo;
	private EditText editTextInfo;

	// variable edit idxpassword activity
	public static final int ELEMENT_FLAG_TRUE = 1;
	public static final int ELEMENT_FLAG_FALSE = 0;
	// =========================Control Define ==================
	// private ListView mListView;
	private CheckBox mCheckBoxLike;

	private EditText mEditTextUrlId;
	private EditText mEditTextNameId;
	private EditText[] mEditTitle;
	private EditText[] mEditContent;
	private ImageButton[] btnGenerator;
	private LinearLayout lnRowItem;
	private EditText mEditTextNote;
	// =========================Class Define ====================
	private static ArrayList<Item> mItems;
	// private MultiDirectionSlidingDrawer mSlidingDrawer;
	// =========================Variable Define =================
	public static String DEFAULT_NAME_ITEM[] = { "ID", "Password", "", "", "" };
	public static int MAX_ITEM = 5;
	public int modeBundle = 1;

	// id password info
	public static boolean isCreatNew;
	public static int currentElementId;
	private static int isLike;
	private static String titleRecord;
	private static byte[] icon;
	private static String url;
	private static String note;
	private static byte[] imageMemo;
	private static Drawable mDrawableIcon;
	public static Drawable mDrawableMemo;
	public static String mUrlItem;
	public static String mStringOfSelectItem;
	private Button btn_memo;
	private ImageButton img_memo, img_avata;

	public static int itemSelect = -1;
	private static final String DEFAULT_URL = "http://google.com";
	private boolean isButtonPress = false;
	private static boolean isSetUrl;
	// private int
	public static final String IS_INTENT_CREATE_NEW_ID = "IS_INTENT_CREATE_NEW_ID";
	private static int widthMemo;
	private static float ratioMemo;

	private ImageView controlView;
	private LinearLayout view1, view2;
	private LinearLayout lnParent;
	private float weightItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		/* init database */
		initDataBase();

		/* init control */
		initControl();

		/* init admob */
		initAdmod();

		/* initialize variable */
		initializeVariable();

		onCreateSetting();

		onCreateInfo();

		initViewEditIdxPass();

	}

	/**
	 * initialize variable
	 */
	private void initializeVariable() {
		new GroupFolder(Contants.SEARCH_FOLDER_ID, "", 0,
				Contants.MASTER_PASSWORD_ID, Contants.SEARCH_FOLDER_ID);
	}

	public void initAdmod() {
		adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			if (!mPref.getIsPaymentNoAd())
				adview.setVisibility(View.VISIBLE);
			else
				adview.setVisibility(View.GONE);
		}
	}

	/**
	 * initialize control and view
	 */
	private void initControl() {
		/* init layout */
		// mainRootLayout = (LinearLayout) LinearLayout.inflate(context,
		// R.layout.page_home_screen, null);
		mainRootLayout = (FrameLayout) LinearLayout.inflate(context,
				R.layout.page_home_screen_slide, null);

		/* init layout drag */
		initLayoutDrag();

		/* init listview folder */
		initListViewFolder();

		/* init ListView Id */
		initListViewId();

		/* init button */
		btnAddNewFolder = (Button) mainRootLayout
				.findViewById(R.id.btn_add_new_folder);
		btnAddNewFolder.setOnClickListener(this);

		btnAddNewId = (Button) mainRootLayout.findViewById(R.id.btn_plus);
		btnAddNewId.setOnClickListener(this);

		btnEdit = (Button) mainRootLayout.findViewById(R.id.btn_edit);
		btnEdit.setOnClickListener(this);
		btnReturn = (Button) mainRootLayout.findViewById(R.id.btn_return);
		btnReturn.setOnClickListener(this);
		btnSetting = (Button) mainRootLayout.findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(this);

		btnSync = (Button) mainRootLayout.findViewById(R.id.btn_sync);
		btnSync.setOnClickListener(this);

		btnInfo = (Button) mainRootLayout.findViewById(R.id.btn_main_info);
		btnInfo.setOnClickListener(this);

		btnSearch = (Button) mainRootLayout.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(this);

		btnClearTextSearch = (Button) mainRootLayout
				.findViewById(R.id.btn_close);
		btnClearTextSearch.setOnClickListener(this);
		btnClearTextSearch.setVisibility(View.GONE);

		panelSetting = (Panel) mainRootLayout
				.findViewById(R.id.l_home_panel_setting);
		panelSetting.setOnPanelListener(new OnPanelListener() {

			@Override
			public void onPanelOpened(Panel panel) {
				// TODO Auto-generated method stub
				isPanelVisiable = true;
			}

			@Override
			public void onPanelClosed(Panel panel) {
				// TODO Auto-generated method stub
				isPanelVisiable = false;
			}
		});
		LinearLayout touchViewSetting = (LinearLayout) mainRootLayout
				.findViewById(R.id.setting_touchView);
		panelSetting.setTouchView(touchViewSetting);

		// panelSetting.setHandle(btnSetting, HOME_SETTING, this);
		panelInfo = (Panel) mainRootLayout.findViewById(R.id.l_home_panel_info);
		panelInfo.setOnPanelListener(new OnPanelListener() {

			@Override
			public void onPanelOpened(Panel panel) {
				// TODO Auto-generated method stub
				isPanelVisiable = true;
			}

			@Override
			public void onPanelClosed(Panel panel) {
				// TODO Auto-generated method stub
				isPanelVisiable = false;
			}
		});
		LinearLayout touchViewInfo = (LinearLayout) mainRootLayout
				.findViewById(R.id.page_browser_ln_webview);
		panelInfo.setTouchView(touchViewInfo);
		// panelInfo.setHandle(btnInfo, HOME_INFO, this);

		panelEditIdxpass = (Panel) mainRootLayout
				.findViewById(R.id.l_home_panel_edit_idxpass);
		panelEditIdxpass.setOnPanelListener(new OnPanelListener() {

			@Override
			public void onPanelOpened(Panel panel) {
				// TODO Auto-generated method stub
				isPanelVisiable = true;
			}

			@Override
			public void onPanelClosed(Panel panel) {
				// TODO Auto-generated method stub
				isPanelVisiable = false;
			}
		});
		LinearLayout touchViewIdxpass = (LinearLayout) mainRootLayout
				.findViewById(R.id.edit_idxpass_touch_ln);
		panelEditIdxpass.setTouchView(touchViewIdxpass);
		// panelEditIdxpass.setHandle(null, HOME_EDIT_IDx_PASS, this);

		/* init editText */
		mEditTextSearch = (EditText) mainRootLayout
				.findViewById(R.id.edit_text_search);
		mEditTextSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							mgr.hideSoftInputFromWindow(
									mEditTextSearch.getWindowToken(), 0);
							if ("".equals(mEditTextSearch.getText().toString())) {
								// showToast("Type to edit text to search");
							} else {
								mTextSearch = mEditTextSearch.getText()
										.toString();
								/* clear text */
								mEditTextSearch.setText("");

								mIdListItems = startSearch(mTextSearch);
								/* reset id adapter */
								currentFolderId = mFolderListItems.get(
										currentFolderItem).getgId();
								itemAdapter.setIdItemList(mIdListItems,
										currentFolderItem, currentFolderId);
								btnClearTextSearch.setVisibility(View.GONE);

								/* reset folder adapter */
								isSearchMode = true;
								// GroupFolder folder = new GroupFolder(1000,
								// "", 0,
								// Contants.MASTER_PASSWORD_ID, 0);
								folderListViewAdapter.updateSearchMode(
										isSearchMode, currentFolderItem);
							}
							return true;
						}
						return false;
					}
				});
		mEditTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				mTextSearch = mEditTextSearch.getText().toString();
				/* clear text */
				// mEditTextSearch.setText("");

				mIdListItems = startSearch(mTextSearch);
				/* reset folder adapter */
				isSearchMode = true;
				// GroupFolder folder = new GroupFolder(1000, "", 0,
				// Contants.MASTER_PASSWORD_ID, 0);
				folderListViewAdapter.updateSearchMode(isSearchMode,
						currentFolderItem);
				btnClearTextSearch.setVisibility(View.VISIBLE);
				if (null != mIdListItems && mIdListItems.size() > 0) {
					/* reset id adapter */
					currentFolderId = mFolderListItems.get(currentFolderItem)
							.getgId();
					itemAdapter.setIdItemList(mIdListItems, currentFolderItem,
							currentFolderId);
					// btnClearTextSearch.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		/* set contentView for home screen layout */
		setContentView(mainRootLayout);

	}

	private void initListViewId() {
		/* init listview */
		idListView = (ListViewDragDrop) mainRootLayout
				.findViewById(R.id.list_view_item);

		/* init adapter for listview */

		// mIdListItems = constructSearchList();
		currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		int currentFolderOrder = mFolderListItems.get(currentFolderItem)
				.getgOrder();
		// set for search item list
		mIdListItems = constructList(currentFolderId);
		itemAdapter = new ItemAdapter(this, mIdListItems, false, mMainHandler,
				idListView, currentFolderId, currentFolderOrder);
		idListView.setAdapter(itemAdapter);

		/**
		 * Set selected Listener to know what item must be drag
		 */
		idListView.setOnItemSelectedListener(mOnItemSelectedListener);

		/**
		 * set on item click listener to id listview
		 */
		idListView.setOnItemClickListener(mOnIdListItemClickListener);

		/**
		 * Set an touch listener to know what is the position when item are move
		 * out of the listview
		 */
		idListView.setOnItemMoveListener(mOnItemMoveListener);

		/**
		 * Listener to know if the item is droped out of this origin ListView
		 */
		idListView.setOnItemUpOutListener(mOnItemUpOutListener);
		/**
		 * listener for folder to move item folder
		 */
		idListView.setDragListener(mOnDragElementListener);
		idListView.setDropListener(mOnDropElementListener);
	}

	private void initListViewFolder() {
		// TODO Auto-generated method stub
		folderListView = (DndListViewFolder) mainRootLayout
				.findViewById(R.id.list_view_folder);
		folderListViewAdapter = new FolderListViewAdapter(this,
				mFolderListItems, false, mMainHandler, folderListView,
				currentFolderItem, isSearchMode);
		folderListView.setAdapter(folderListViewAdapter);

		/**
		 * listener to click item folder
		 */
		folderListView.setOnItemClickListener(listenerClickFolderItem);

		/**
		 * Listener to know on what position the new item must be insert
		 */
		folderListView.setOnItemReceiverListener(listenerReceivePicture);

		/**
		 * listener for folder listvie to know position id listview sent to
		 * which folder
		 */
		folderListView.setOnScrollListener(onFolderScrollListener);
		/**
		 * listener for folder to move item folder
		 */
		folderListView.setDragListener(mOnDragFolderListener);
		folderListView.setDropListener(mOnDropFolderListener);
	}

	private void initLayoutDrag() {
		/* init layout drag */
		layoutDrag = (RelativeLayout) mainRootLayout
				.findViewById(R.id.layoutDrag);
		imageDrag = (ImageView) mainRootLayout.findViewById(R.id.imageView1);
		txtIdName = (TextView) mainRootLayout.findViewById(R.id.txt_id_name);
		txtIdUrl = (TextView) mainRootLayout.findViewById(R.id.txt_id_url);
	}

	/**
	 * initialize database
	 */
	private void initDataBase() {
		/* share preference */
		mPref = IdManagerPreference.getInstance(this);
		SQLiteDatabase.loadLibs(this);
		// mDataBaseHandler = new DataBaseHandler(this);
		mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
		// get data from folder database
		loadDataFromFolderDataBase();
	}

	/**
	 * load data from folder database to display on listview group
	 */
	private void loadDataFromFolderDataBase() {
		List<GroupFolder> folderList = mIDxPWDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();
		for (int i = (sizeOfFolder - 1); i >= 0; i--) {
			GroupFolder folder = new GroupFolder(folderList.get(i).getgId(),
					folderList.get(i).getgName(), folderList.get(i).getgType(),
					folderList.get(i).getgUserId(), folderList.get(i)
							.getgOrder());
			mFolderListItems.add(folder);
		}
		// add folder favourite and history
		GroupFolder favouriteFolder = new GroupFolder(
				Contants.FAVOURITE_FOLDER_ID, Contants.NAME_FAVOURITE_FOLDER,
				0, Contants.MASTER_PASSWORD_ID, -1);
		mFolderListItems.add(favouriteFolder);

		// add folder history
		GroupFolder historyFolder = new GroupFolder(Contants.HISTORY_FOLDER_ID,
				Contants.NAME_HISTORY_FOLDER, 0, Contants.MASTER_PASSWORD_ID,
				-2);
		mFolderListItems.add(historyFolder);
	}

	/**
	 * Save selected item
	 */
	private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			/**
			 * retrieve selected item from adapterview
			 */
			oneItemSelected = (ElementID) arg0.getItemAtPosition(arg2);

			imageDrag.setImageDrawable(getImageDataBase(oneItemSelected
					.geteIconData()));
			txtIdName.setText(oneItemSelected.geteTitle());
			txtIdUrl.setText(oneItemSelected.geteUrl());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	/**
	 * on item click listener
	 */
	private OnItemClickListener mOnIdListItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (!isEdit)
				CopyItemActivity.startActivity(HomeScreeenActivity.this,
						mIdListItems.get(arg2).geteId());
			else {
				modeBundle = 0;
				currentElementId = mIdListItems.get(arg2).geteId();
				mPref.setCurrentFolderId(currentFolderId);
				slidePanelEditIdxPass();
			}
		}
	};
	private OnTouchListener mOnItemUpOutListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return folderListView.onUpReceive(event);
		}
	};

	/**
	 * on folder item click listener
	 */
	private OnItemClickListener listenerClickFolderItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			currentFolderItem = position;

			currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
			int curentFolderOrder = mFolderListItems.get(currentFolderItem)
					.getgOrder();
			/* refresh folder list */
			folderListViewAdapter.setFolderSelected(currentFolderItem);

			if (position == mFolderListItems.size() - 1) {
				// folder history
				mIdListItems = constructHistoryList();
			} else if (position == mFolderListItems.size() - 2) {
				// folder favourite
				mIdListItems = constructFavouriteList();

			} else
				mIdListItems = constructList(currentFolderId);
			if (isSearchMode && position != 0) {
				isSearchMode = false;
				folderListViewAdapter.updateSearchMode(isSearchMode,
						currentFolderItem);
			}
			itemAdapter.setIdItemList(mIdListItems, curentFolderOrder,
					currentFolderId);
		}
	};
	private OnItemClickListener listenerReceivePicture = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (oneItemSelected != null) {
				// receverAdapter.addPicture(oneItemSelected, arg2);
				mCurrentFolderPosition = mCurrentFirstVisibleFolderItem + arg2;
				int currentFolderId = 0;
				if (mCurrentFolderPosition < mFolderListItems.size()) {
					currentFolderId = mFolderListItems.get(
							mCurrentFolderPosition).getgId();
					mCurrentId = oneItemSelected.geteId();
					ElementID elementId = mIDxPWDataBaseHandler
							.getElementID(mCurrentId);
					int sizeFolder = mIDxPWDataBaseHandler.getFoldersCount();
					if (isEdit && mCurrentFolderPosition < sizeFolder
							&& elementId.geteGroupId() != currentFolderId) {
						showDialog(Contants.DIALOG_MOVE_ID_TO_FOLDER);
					}
				}
			}
		}

	};

	public OnScrollListener onFolderScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			mCurrentFirstVisibleFolderItem = firstVisibleItem;

		}
	};
	private OnTouchListener mOnItemMoveListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) layoutDrag
					.getLayoutParams();
			if (isEdit)
				layoutDrag.setVisibility(RelativeLayout.VISIBLE);
			else
				layoutDrag.setVisibility(RelativeLayout.GONE);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				layoutDrag.bringToFront();
				return true;
			}

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				layout.leftMargin = (int) event.getX();
				layout.topMargin = (int) event.getY() - layoutDrag.getHeight()
						/ 2;
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				layoutDrag.setVisibility(View.GONE);
			}

			// set params
			layoutDrag.setLayoutParams(layout);

			return true;
		}

	};

	private ArrayList<ElementID> constructList(int currentFolderItem) {

		List<ElementID> elementList = mIDxPWDataBaseHandler
				.getAllElementIdByGroupFolderId(currentFolderItem);
		ArrayList<ElementID> al = new ArrayList<ElementID>();
		int idListSize = elementList.size();
		for (int i = 0; i < idListSize; i++) {
			ElementID item = new ElementID(elementList.get(i).geteId(),
					elementList.get(i).geteGroupId(), elementList.get(i)
							.geteTitle(), elementList.get(i).geteIconData(),
					elementList.get(i).geteTimeStamp(), elementList.get(i)
							.geteFavourite(), elementList.get(i).geteFlag(),
					elementList.get(i).geteUrl(),
					elementList.get(i).geteNote(), elementList.get(i)
							.geteMemoData(), elementList.get(i).geteOrder());
			al.add(item);
		}
		return al;
	}

	private DndListViewFolder.DragListener mOnDragFolderListener = new DndListViewFolder.DragListener() {

		@Override
		public void drag(int from, int to) {
			// TODO Auto-generated method stub
			if (!isDnd && isEdit) {
				isDnd = true;
			}
		}
	};

	private DndListViewFolder.DropListener mOnDropFolderListener = new DndListViewFolder.DropListener() {

		@Override
		public void drop(int from, int to) {
			// TODO Auto-generated method stub
			if (isDnd) {
				if (from == to)
					return;
				// String item = list2.remove(from);
				if (to < mFolderListItems.size() - 2
						&& from < mFolderListItems.size() - 2) {
					GroupFolder groupFolder = mFolderListItems.remove(from);
					folderListViewAdapter.add(to, groupFolder);
					folderListView.checkfordrop(to);
				}
				isDnd = false;
			}
		}
	};

	private ListViewDragDrop.DragListener mOnDragElementListener = new ListViewDragDrop.DragListener() {

		@Override
		public void drag(int from, int to) {
			// TODO Auto-generated method stub
			if (!isDndElement && isEdit) {
				isDndElement = true;
			}
		}
	};

	private ListViewDragDrop.DropListener mOnDropElementListener = new ListViewDragDrop.DropListener() {

		@Override
		public void drop(int from, int to) {
			// TODO Auto-generated method stub
			if (isDndElement) {
				if (from == to)
					return;
				ElementID element = mIdListItems.remove(from);
				itemAdapter.add(to, element);
				idListView.checkfordrop(to);
				isDndElement = false;
			}
		}
	};

	/**
	 * is favourite folder items
	 * 
	 * @param currentFolderItem
	 * @return
	 */
	private ArrayList<ElementID> constructFavouriteList() {

		List<ElementID> elementList = mIDxPWDataBaseHandler.getAllElmentIds();
		ArrayList<ElementID> al = new ArrayList<ElementID>();
		int idListSize = elementList.size();
		for (int i = 0; i < idListSize; i++) {
			if (elementList.get(i).geteFavourite() == Contants.IS_FAVOURITE) {
				ElementID item = new ElementID(elementList.get(i).geteId(),
						elementList.get(i).geteGroupId(), elementList.get(i)
								.geteTitle(),
						elementList.get(i).geteIconData(), elementList.get(i)
								.geteTimeStamp(), elementList.get(i)
								.geteFavourite(),
						elementList.get(i).geteFlag(), elementList.get(i)
								.geteUrl(), elementList.get(i).geteNote(),
						elementList.get(i).geteMemoData(), elementList.get(i)
								.geteOrder());
				al.add(item);
			}
		}
		return al;
	}

	/**
	 * search folder items
	 * 
	 * @return
	 */
	private ArrayList<ElementID> constructHistoryList() {
		// TODO Auto-generated method stub
		ArrayList<ElementID> allItem = new ArrayList<ElementID>();
		List<ElementID> idList = mIDxPWDataBaseHandler.getAllElmentIds();

		int size = idList.size();
		long timeStamp[] = new long[size];
		for (int i = 0; i < size; i++) {
			timeStamp[i] = idList.get(i).geteTimeStamp();
		}
		/* sort array */
		for (int i = 0; i < size - 1; i++) {
			for (int j = i; j < size; j++) {
				long timeStampId1 = idList.get(i).geteTimeStamp();
				long timeStampId2 = idList.get(j).geteTimeStamp();
				if (timeStampId1 < timeStampId2) {
					swap(idList, i, j);
				}
			}
		}
		int numberToView = 0;
		if (size < Contants.NUMBER_ELEMENT_SHOW_IN_HISTORY)
			numberToView = size;
		else
			numberToView = Contants.NUMBER_ELEMENT_SHOW_IN_HISTORY;
		for (int i = 0; i < numberToView; i++) {
			ElementID item = new ElementID(idList.get(i).geteId(), idList
					.get(i).geteGroupId(), idList.get(i).geteTitle(), idList
					.get(i).geteIconData(), idList.get(i).geteTimeStamp(),
					idList.get(i).geteFavourite(), idList.get(i).geteFlag(),
					idList.get(i).geteUrl(), idList.get(i).geteNote(), idList
							.get(i).geteMemoData(), idList.get(i).geteOrder());
			allItem.add(item);
		}
		return allItem;
	}

	private void swap(List<ElementID> idList, int i, int j) {
		// TODO Auto-generated method stub
		ElementID idTemp = idList.get(i);
		ElementID idTemp2 = idList.get(j);
		idList.set(i, idTemp2);
		idList.set(j, idTemp);
	}

	@Override
	public void onClick(View v) {
		/* add new folder */
		if (v == btnAddNewFolder) {
			if (isSearchMode) {
				isSearchMode = false;
				refreshListView();
			}
			addNewFolder();
		}

		/* add new id */
		else if (v == btnAddNewId) {
			// EditIdPasswordActivity.startActivity(this);
			if (currentFolderItem < mFolderListItems.size() - 2) {
				int number_items = mPref
						.getNumberItems(IdManagerPreference.NUMBER_ITEMS);
				if (number_items >= Contants.MAX_ELEMENT)
					showDialog(Contants.DIALOG_CREATE_ID);
				else {
					if (isSearchMode) {
						isSearchMode = false;
						refreshListView();
					} else
						startIntentCreateNewIds();
				}
			} else {
				showDialog(Contants.DIALOG_MESSAGE_FOLDER_INVALID);
			}
		}

		/* edit listview */
		else if (v == btnEdit) {
			if (isSearchMode) {
				isSearchMode = false;
				refreshListView();
			}

			btnSync.setVisibility(View.GONE);
			// btnEdit.setBackgroundResource(R.drawable.return_back);
			btnEdit.setVisibility(View.GONE);
			btnReturn.setVisibility(View.VISIBLE);
			isEdit = true;
			mPref.setEditMode(true);
			/* set folder and id listview in edit mode */
			setEditModeForFolderAndIdListView(isEdit);
		} else if (v == btnReturn) {
			if (isSearchMode) {
				isSearchMode = false;
				refreshListView();
			}
			btnSync.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.VISIBLE);
			btnReturn.setVisibility(View.GONE);
			isEdit = false;
			setEditModeForFolderAndIdListView(isEdit);
			mPref.setEditMode(false);
		}

		/* setting */
		else if (v == btnSetting) {
			// Intent intentSeting = new Intent(HomeScreeenActivity.this,
			// SettingActivity.class);
			// startActivity(intentSeting);
			panelSetting.slidePanel();
		}

		/* sync data to cloud */
		else if (v == btnSync) {
			Intent intentSync = new Intent(HomeScreeenActivity.this,
					SyncCloudActivity.class);
			startActivity(intentSync);
		}

		/* go to a browser */
		else if (v == btnInfo) {
			// Intent intentBrowser = new Intent(HomeScreeenActivity.this,
			// BrowserActivity.class);
			// intentBrowser.putExtra(Contants.KEY_TO_BROWSER,
			// Contants.INFO_TO);
			// startActivity(intentBrowser);
			panelInfo.slidePanel();
		} else if (v == btnSearch) {
			if ("".equals(mEditTextSearch.getText().toString())) {
				// showToast("Type to edit text to search");
			} else {
				mTextSearch = mEditTextSearch.getText().toString();
				/* clear text */
				mEditTextSearch.setText("");

				mIdListItems = startSearch(mTextSearch);
				/* reset id adapter */
				currentFolderId = mFolderListItems.get(currentFolderItem)
						.getgId();
				itemAdapter.setIdItemList(mIdListItems, currentFolderItem,
						currentFolderId);
				btnClearTextSearch.setVisibility(View.GONE);

				/* reset folder adapter */
				isSearchMode = true;
				// GroupFolder folder = new GroupFolder(1000, "", 0,
				// Contants.MASTER_PASSWORD_ID, 0);
				folderListViewAdapter.updateSearchMode(isSearchMode,
						currentFolderItem);
				// folderListView.setSelection(0);
				// if (!mFolderListItems.contains(searchFolder))
				// mFolderListItems.add(searchFolder);
			}
		} else if (v == btnClearTextSearch) {
			mEditTextSearch.setText("");
			btnClearTextSearch.setVisibility(View.GONE);
		}
	}

	/**
	 * start search
	 * 
	 * @param textSearch
	 */
	private ArrayList<ElementID> startSearch(String textSearch) {
		if (!"".equals(textSearch)) {
			List<ElementID> elementList = mIDxPWDataBaseHandler
					.getAllElmentIds();
			ArrayList<ElementID> searchItems = new ArrayList<ElementID>();
			int size = elementList.size();
			for (int i = 0; i < size; i++) {
				String idName = elementList.get(i).geteTitle().toString()
						.toUpperCase();
				String idNote = elementList.get(i).geteNote().toString()
						.toUpperCase();
				boolean isFoundId = idName.indexOf(textSearch.toUpperCase()) != -1;
				boolean isFoundNote = idNote.indexOf(textSearch.toUpperCase()) != -1;

				if (isFoundId || isFoundNote) {
					ElementID item = new ElementID(elementList.get(i).geteId(),
							elementList.get(i).geteGroupId(), elementList
									.get(i).geteTitle(), elementList.get(i)
									.geteIconData(), elementList.get(i)
									.geteTimeStamp(), elementList.get(i)
									.geteFavourite(), elementList.get(i)
									.geteFlag(), elementList.get(i).geteUrl(),
							elementList.get(i).geteNote(), elementList.get(i)
									.geteMemoData(), elementList.get(i)
									.geteOrder());
					searchItems.add(item);
				}
			}
			return searchItems;
		} else
			return null;
	}

	/**
	 * set mode edit for folder and id list view
	 */
	private void setEditModeForFolderAndIdListView(boolean isEdit) {
		/* set mode edit for folder */
		folderListViewAdapter.updateModeForListView(isEdit);

		/* set mode edit for id */
		currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		int currentFolderOrder = mFolderListItems.get(currentFolderItem)
				.getgOrder();
		itemAdapter.updateModeForListView(isEdit, currentFolderOrder);
	}

	/**
	 * Called to create a dialog to be shown.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case Contants.DIALOG_ADD_NEW_FOLDER:
			return createExampleDialog(Contants.DIALOG_ADD_NEW_FOLDER);
		case Contants.DIALOG_DELETE_FOLDER:
			return createExampleDialog(Contants.DIALOG_DELETE_FOLDER);
		case Contants.DIALOG_EDIT_FOLDER:
			return createExampleDialog(Contants.DIALOG_EDIT_FOLDER);
		case Contants.DIALOG_DELETE_ID:
			return createExampleDialog(Contants.DIALOG_DELETE_ID);
		case Contants.DIALOG_EDIT_ID:
			return createExampleDialog(Contants.DIALOG_EDIT_ID);
		case Contants.DIALOG_CREATE_ID:
			return createExampleDialog(Contants.DIALOG_CREATE_ID);
		case Contants.DIALOG_EXIT:
			return createExampleDialog(Contants.DIALOG_EXIT);
		case Contants.DIALOG_MOVE_ID_TO_FOLDER:
			return createExampleDialog(Contants.DIALOG_MOVE_ID_TO_FOLDER);

		case Contants.DIALOG_NO_NET_WORK:
			return createExampleDialog(Contants.DIALOG_NO_NET_WORK);
		case Contants.DIALOG_CHOICE_CLOUD_TYPE:
			return createExampleDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
		case Contants.DIALOG_NO_CLOUD_SETUP:
			return createExampleDialog(Contants.DIALOG_NO_CLOUD_SETUP);
		case Contants.DIALOG_EXPORT_DATA:
			return createExampleDialog(Contants.DIALOG_EXPORT_DATA);
		case Contants.DIALOG_MESSAGE_SYNC_FAILED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_FAILED);
		case Contants.DIALOG_MESSAGE_SYNC_SUCCESS:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_SUCCESS);
		case Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED);
		case Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE);
		case Contants.DIALOG_NO_DATA_CLOUD:
			return createExampleDialog(Contants.DIALOG_NO_DATA_CLOUD);
		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:
			return createExampleDialog(Contants.DIALOG_MESSAGE_CHOICE_DATA_READ);
		case Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED);
		case Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD:
			return createExampleDialog(Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD);

		case Contants.DIALOG_MESSAGE_FOLDER_INVALID:
			return createExampleDialog(Contants.DIALOG_MESSAGE_FOLDER_INVALID);
		case Contants.DIALOG_MESSAGE_FOLDER_EXISTED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_FOLDER_EXISTED);
		case Contants.DIALOG_MESSAGE_FOLDER_ERROR:
			return createExampleDialog(Contants.DIALOG_MESSAGE_FOLDER_ERROR);
		case Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR:
			return createExampleDialog(Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR);

		default:
			return null;
		}
	}

	/**
	 * If a dialog has already been created, this is called to reset the dialog
	 * before showing it a 2nd time. Optional.
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case Contants.DIALOG_ADD_NEW_FOLDER:
			// Clear the input box.
			EditText text = (EditText) dialog.findViewById(Contants.TEXT_ID);
			text.setText("");
			break;
		case Contants.DIALOG_MOVE_ID_TO_FOLDER:
			((AlertDialog) dialog).setMessage("Do you want to move this id to "
					+ mFolderListItems.get(mCurrentFolderPosition).getgName()
					+ "?");
		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:

			if (mListDataChoiceTemp != null && mListDataChoiceTemp.length > 0) {
				mListDataChoice = new String[mListDataChoiceTemp.length];
				mListDataChoice = mListDataChoiceTemp;
			}

			break;
		}
	}

	/**
	 * add new folder to group
	 */
	private void addNewFolder() {
		showDialog(Contants.DIALOG_ADD_NEW_FOLDER);
	}

	/**
	 * Create and return an example alert dialog with an edit text box.
	 */
	private Dialog createExampleDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case Contants.DIALOG_ADD_NEW_FOLDER:
			builder.setTitle(getResources()
					.getString(R.string.title_add_folder));
			// builder.setMessage("Type the name of new folder:");
			builder.setIcon(R.drawable.icon);
			// Use an EditText view to get user input.
			final EditText input = new EditText(this);
			input.setId(Contants.TEXT_ID);
			input.setText("");
			builder.setView(input);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String folderName = input.getText().toString();
							/* add new folder to database */
							addNewFolderToDatabase(folderName);
							return;
						}

					});

			builder.setNegativeButton(
					getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			return builder.create();

		case Contants.DIALOG_DELETE_FOLDER:
			builder.setTitle(getResources().getString(R.string.title_delete));
			// builder.setMessage("Do you want to delete this folder?");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							deleteFolder(positionReturnedByHandler);
							return;
						}

					});

			builder.setNegativeButton(
					getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_EDIT_FOLDER:
			builder.setTitle(getResources().getString(
					R.string.title_change_folder));
			// builder.setMessage("Type the name of folder to edit :");
			builder.setIcon(R.drawable.icon);

			// Use an EditText view to get user input.
			final EditText inputEdit = new EditText(this);
			inputEdit.setId(Contants.TEXT_ID);
			builder.setView(inputEdit);
			inputEdit.setText("");
			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String folderName = inputEdit.getText().toString();
							/* edit folder to database */
							editFolderToDatabase(folderName,
									positionReturnedByHandler);
							return;
						}

					});

			builder.setNegativeButton(
					getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			return builder.create();

		case Contants.DIALOG_DELETE_ID:
			builder.setTitle(getResources().getString(
					R.string.title_delete_items));
			// builder.setMessage("Do you want to delete this ID?");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_delete),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							deleteID(positionReturnedByHandler);
							return;
						}
					});

			builder.setNegativeButton(
					getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_CREATE_ID:
			builder.setTitle(getResources().getString(R.string.title_add_items));
			builder.setMessage(getResources().getString(
					R.string.message_add_item));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_MESSAGE_FOLDER_INVALID:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources().getString(R.string.InvalidFolder));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_MESSAGE_FOLDER_EXISTED:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources().getString(
					R.string.GroupNameExisted));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}
					});

			return builder.create();

		case Contants.DIALOG_MESSAGE_FOLDER_ERROR:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources()
					.getString(R.string.GroupNameError));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources().getString(
					R.string.InsertGroupError));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_EXIT:
			builder.setTitle("IDManager");
			builder.setMessage("Are you sure to want to exit?");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							finish();
							// finishApp();
							return;
						}
					});

			builder.setNegativeButton(
					getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MOVE_ID_TO_FOLDER:
			AlertDialog.Builder builderMoveId = new AlertDialog.Builder(this);
			builderMoveId.setTitle("IDManager");
			builderMoveId.setMessage("Are you sure to want to move this id to "
					+ mFolderListItems.get(mCurrentFolderPosition).getgName()
					+ " folder?");
			builderMoveId.setIcon(R.drawable.icon);
			builderMoveId.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							int passwordId = mCurrentId;
							// passwordId = m
							int folderId = mFolderListItems.get(
									mCurrentFolderPosition).getgId();
							mCurrentFirstVisibleFolderItem = 0;
							moveIdToFolder(passwordId, folderId);
							return;
						}
					});

			builderMoveId.setNegativeButton(
					getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			return builderMoveId.create();
		case Contants.DIALOG_NO_NET_WORK:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.internet_not_use));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_NO_CLOUD_SETUP:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.no_cloud_serivce_set_up));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_SUCCESS:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.sync_finish));
			builder.setCancelable(false);
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_FAILED:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.sync_failed));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_NO_DATA_CLOUD:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.no_data_on_cloud));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.data_rewritten));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							if (mApi.getSession().isLinked())
								startReadFileViaCloud(mSelectedFile, true);
							else if (!"".equals(mGGAccountName))
								saveFileToDevice(mGGAccountName, mSelectedFile);
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.sync_data_duplicate_msg));
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							startSyncToCloud(fileExportName, false);
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED:
			builder.setCancelable(false);
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.sync_interrupt));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED:
			builder.setCancelable(false);
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.success));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String file = mSelectedFile.toString();
							importFileCSVToDatabase(file);
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle(getResources().getString(R.string.item_sync));
			alertBuilder.setIcon(R.drawable.icon);
			alertBuilder.setItems(mListDataChoice,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							Toast.makeText(getApplicationContext(),
									mListDataChoice[item], Toast.LENGTH_SHORT)
									.show();
							mSelectedFile = mListDataChoice[item];
							if (mApi.getSession().isLinked())
								startReadFileViaCloud(mSelectedFile, false);
							else if (!"".equals(mGGAccountName))
								saveFileToDevice(mGGAccountName, mSelectedFile);
						}
					});
			return alertBuilder.create();
		case Contants.DIALOG_EXPORT_DATA: {
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.item_export_data));
			builder.setIcon(R.drawable.icon);
			final EditText input2 = new EditText(this);
			input2.setId(Contants.TEXT_ID);
			input2.setText("idxp.idp");
			builder.setView(input2);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							fileExportName = input2.getText().toString()
									+ ".csv";
							/* start export file */
							if (!"".equals(fileExportName)) {
								/* gen file csv */
								generateCsvFile(Contants.PATH_ID_FILES + "/"
										+ fileExportName);
								if (mApi.getSession().isLinked())
									startSyncToCloud(fileExportName, true);
								else if (!"".equals(mGGAccountName)) {
									/* gg drive api */
									credential = GoogleAccountCredential
											.usingOAuth2(
													HomeScreeenActivity.this,
													DriveScopes.DRIVE);
									credential
											.setSelectedAccountName(mGGAccountName);
									service = getDriveService(credential);

									// save file to gg drive
									boolean isCheckTime = false;
									saveFileToDrive(mGGAccountName,
											isCheckTime, Contants.PATH_ID_FILES
													+ "/" + fileExportName);
								}

							} else
								return;
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
		}
			return builder.create();

		default:
			return null;
		}
	}

	private void moveIdToFolder(int passwordId, int currentFolderId) {
		/* update to database */
		ElementID elment = mIDxPWDataBaseHandler.getElementID(passwordId);
		elment.seteGroupId(currentFolderId);
		mIDxPWDataBaseHandler.updateElement(elment);
		/* refresh id listview */
		for (int i = 0; i < mIdListItems.size(); i++) {
			if (mIdListItems.get(i).geteId() == passwordId)
				mIdListItems.remove(i);
		}
		// this.currentFolderId =
		// mFolderListItems.get(currentFolderId).getgId();
		itemAdapter.setIdItemList(mIdListItems, currentFolderId,
				currentFolderId);
		itemAdapter.updateModeForListView(true, currentFolderId);
	}

	private void startIntentCreateNewIds() {
		modeBundle = 1;
		mPref.setCurrentFolderId(mFolderListItems.get(currentFolderItem)
				.getgId());
		slidePanelEditIdxPass();
	}

	/**
	 * delete id
	 * 
	 * @param positionReturnedByHandler
	 */
	private void deleteID(int positionReturnedByHandler) {
		// TODO Auto-generated method stub
		/* delete in database */
		mIDxPWDataBaseHandler.deleteElementId(mIdListItems.get(
				positionReturnedByHandler).geteId());
		/* reset id list view */
		mIdListItems.remove(positionReturnedByHandler);
		currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		itemAdapter.setIdItemList(mIdListItems, currentFolderItem,
				currentFolderId);
		mPref.setNumberItem(IdManagerPreference.NUMBER_ITEMS,
				mPref.getNumberItems(IdManagerPreference.NUMBER_ITEMS) - 1);
	}

	/**
	 * edit folder to database
	 * 
	 * @param folderName
	 * @param positionReturnedByHandler
	 */

	private void editFolderToDatabase(String folderName,
			int positionReturnedByHandler) {
		if (folderName == null || folderName.equals(""))
			return;
		List<GroupFolder> folderList = mIDxPWDataBaseHandler.getAllFolders();
		for (int i = 0; i < folderList.size(); i++) {
			if (folderName.equals(folderList.get(i).getgName()))
				return;
		}
		// TODO Auto-generated method stub
		GroupFolder folderItem = mFolderListItems
				.get(positionReturnedByHandler);
		GroupFolder folder = new GroupFolder(folderItem.getgId(), folderName,
				folderItem.getgType(), folderItem.getgUserId(),
				folderItem.getgOrder());
		mIDxPWDataBaseHandler.updateFolder(folder);

		/* reset folder list */
		mFolderListItems.get(positionReturnedByHandler).setgName(folderName);
		folderListViewAdapter.updateFolderList(mFolderListItems);
		// showToast("Folder " + folderName + " is updated");
	}

	/**
	 * delete folder
	 * 
	 * @param positionReturnedByHandler
	 */
	private void deleteFolder(int positionReturnedByHandler) {
		// TODO Auto-generated method stub
		/* delete folder in database */
		mIDxPWDataBaseHandler.deleteFolder(mFolderListItems.get(
				positionReturnedByHandler).getgId());

		List<ElementID> elementIdList = mIDxPWDataBaseHandler
				.getAllElementIdByGroupFolderId(mFolderListItems.get(
						positionReturnedByHandler).getgId());

		/* delete all ids in this folder */
		mIDxPWDataBaseHandler.deleteElementIdInFolderId(mFolderListItems.get(
				positionReturnedByHandler).getgId());

		for (ElementID element : elementIdList) {
			/* delete all password also */
			mIDxPWDataBaseHandler.deletePasswordByElementId(element.geteId());
		}

		/* refresh id list view */
		mIdListItems = constructList(positionReturnedByHandler);
		currentFolderId = mFolderListItems.get(positionReturnedByHandler)
				.getgId();
		itemAdapter.setIdItemList(mIdListItems, positionReturnedByHandler, 0);
		/* refresh folder listview */
		folderListViewAdapter.removeItem(positionReturnedByHandler);
	}

	/**
	 * add new folder to database
	 */
	private void addNewFolderToDatabase(String folderName) {
		if (folderName == null || folderName.equals("")) {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = Contants.DIALOG_MESSAGE_FOLDER_ERROR;
			mHandler.sendMessage(msg);
			return;
		}
		List<GroupFolder> folderList = mIDxPWDataBaseHandler.getAllFolders();
		for (int i = 0; i < folderList.size(); i++) {
			if (folderName.equals(folderList.get(i).getgName())) {
				Message msg = mHandler.obtainMessage();
				msg.arg1 = Contants.DIALOG_MESSAGE_FOLDER_EXISTED;
				mHandler.sendMessage(msg);
				return;
			}
		}
		int sizeOfFolder = folderList.size();
		int sizeTemp = 0;
		for (int i = 0; i < sizeOfFolder; i++) {
			if (sizeTemp < folderList.get(i).getgId())
				sizeTemp = folderList.get(i).getgId();
		}
		int folderId = sizeOfFolder;
		if (sizeOfFolder > sizeTemp)
			folderId = sizeOfFolder;
		else
			folderId = sizeTemp;
		folderId++;
		GroupFolder folder = new GroupFolder(folderId, folderName, 0,
				Contants.MASTER_PASSWORD_ID, 0);
		int size1 = mIDxPWDataBaseHandler.getFoldersCount();
		mIDxPWDataBaseHandler.addNewFolder(folder);
		int size2 = mIDxPWDataBaseHandler.getFoldersCount();
		if (size2 <= size1) {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR;
			mHandler.sendMessage(msg);
			return;
		}

		folderListViewAdapter.addNewFolder(folder, folder.getgOrder());
		mIdListItems = constructList(folderId);
		itemAdapter.setIdItemList(mIdListItems, currentFolderItem, folderId);
		folderListView.invalidate();
	}

	public void onResume() {
		super.onResume();
		startService(new Intent(this, BillingService.class));
		BillingHelper.setCompletedHandler(mTransactionHandler);
		if (!mPref.getIsPaymentNoAd())
			adview.setVisibility(View.VISIBLE);
		else
			adview.setVisibility(View.GONE);
		/* refresh 2 list view */
		refreshListView();
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		mGGAccountName = mPref.getGoogleAccNameSession();
		if (mApi.getSession().isLinked()) {
			mImgDropbox.setBackgroundResource(R.drawable.logo_dropbox_selected);
			mImgGGDrive.setBackgroundResource(R.drawable.logo_google);
		} else if (!"".equals(mGGAccountName)) {
			mImgDropbox.setBackgroundResource(R.drawable.logo_dropbox);
			mImgGGDrive
					.setBackgroundResource(R.drawable.logo_google_drive_selected);
		} else {
			mImgDropbox.setBackgroundResource(R.drawable.logo_dropbox);
			mImgGGDrive.setBackgroundResource(R.drawable.logo_google);
		}
		textModeSercurity.setText(sercurity_mode);

		// resume method of edit idxpassword activity
		onResumeEditIdxPass();

		/* show dialog sync cloud success */
		if (mPref.isCloudSettingSuccess()) {
			mPref.setCloudSettingSuccess(false);
			showDialog(Contants.DIALOG_MESSAGE_SYNC_SUCCESS);
		}

	}

	public void onResumeEditIdxPass() {
		if (mDrawableIcon != null)
			img_avata.setBackgroundDrawable(mDrawableIcon);
		else {
			img_avata.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.default_icon));
			if (isSetUrl)
				((EditText) findViewById(R.id.edit_text_url)).setText(mUrlItem);
		}
		isSetUrl = false;
		if (mDrawableMemo != null) {

			img_memo.setLayoutParams(new FrameLayout.LayoutParams(widthMemo,
					(int) (widthMemo * ratioMemo)));
			img_memo.requestLayout();
			img_memo.setBackgroundDrawable(mDrawableMemo);
			btn_memo.setVisibility(View.GONE);
		} else {
			img_memo.setVisibility(View.GONE);
		}
		if (itemSelect >= 0) {
			mItems.get(itemSelect).mContentItem = mStringOfSelectItem;
			updateItems(mItems);
			itemSelect = -1;
		}
		if (mDrawableMemo != null) {
			img_memo.setBackgroundDrawable(mDrawableMemo);
			img_memo.setVisibility(View.VISIBLE);
			btn_memo.setVisibility(View.GONE);
		} else {
			img_memo.setBackgroundDrawable(mDrawableMemo);
			img_memo.setVisibility(View.GONE);
			btn_memo.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * refresh folder and element listview after change
	 * 
	 * @param string
	 */
	private void refreshListView() {
		if (null != mIDxPWDataBaseHandler)
			mIDxPWDataBaseHandler.close();
		mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
		/* reset folder adapter */
		mFolderListItems.clear();
		if (null != mIdListItems) {
			mIdListItems.clear();
			// get data from folder database
			loadDataFromFolderDataBase();
			//
			/* reset adapter */
			folderListViewAdapter.updateFolderList(mFolderListItems);
			currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
			mIdListItems = constructList(currentFolderId);
			itemAdapter.setIdItemList(mIdListItems, currentFolderItem,
					currentFolderId);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (!isPanelVisiable) {
				if (mPref.getSecurityMode() == Contants.KEY_OFF) {
//					getApp().stop();
					finish();
//					finishApp();
				} else
					showDialog(Contants.DIALOG_EXIT);

			} else {
				return false;
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public ControlApplication getApp() {
		return (ControlApplication) this.getApplication();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		getApp().touch();
	}

	public void finishApp() {
		getApp().stop();
		BillingHelper.stopService();
		mPref.setEditMode(false);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		getApp().stop();
		mPref.setEditMode(false);
		super.onDestroy();
		BillingHelper.stopService();
		android.os.Process.killProcess(android.os.Process.myPid());

	}

	public Drawable getImageDataBase(byte[] data) {
		if (data == null || data.length == 0) {
			return getResources().getDrawable(R.drawable.default_icon);
		}

		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		BitmapDrawable result = new BitmapDrawable(bMap);
		return result;
	}

	// // method of setting

	private void onCreateSetting() {
		modes[0] = getResources().getString(R.string.text_security_off);
		modes[1] = getResources().getString(R.string.text_security_immediately);
		for (int i = 2; i < modes.length; i++) {
			modes[i] = modes[i] + getResources().getString(R.string.text_min);
		}
		initDatabase();
		textModeSercurity = (TextView) findViewById(R.id.id_text_mode);
		// lnSetting = (LinearLayout) findViewById(R.id.ln_setting);
		mPref = IdManagerPreference.getInstance(this);
		initAdmodSetting();

		mImgDropbox = (ImageView) findViewById(R.id.img_dropbox_logo);
		mImgGGDrive = (ImageView) findViewById(R.id.img_gg_drive_logo);
		sercurity_mode = modes[mPref.getSecurityMode()];
		Display d = getWindowManager().getDefaultDisplay();
		setWidthScreen(d.getWidth());

	}

	public void initAdmodSetting() {
		adview = (AdView) findViewById(R.id.main_adView_setting);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			if (!mPref.getIsPaymentNoAd())
				adview.setVisibility(View.VISIBLE);
			else
				adview.setVisibility(View.GONE);
		}
	}

	private void initDatabase() {
		// TODO Auto-generated method stub
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new IDxPWDataBaseHandler(this);
		mGList = mDataBaseHandler.getAllFolders();
		mEList = mDataBaseHandler.getAllElmentIds();
		mPList = mDataBaseHandler.getAllPasswords();
		sizeOfGList = mGList.size();
		sizeOfEList = mEList.size();
		sizeOfPList = mPList.size();
	}

	public void onChangeMasterPass(View v) {
		Intent intentChangePW = new Intent(this, EnterOldPasswordActivity.class);
		intentChangePW.putExtra(EnterOldPasswordActivity.KEY_MODE,
				EnterOldPasswordActivity.FROM_SETTING);
		startActivity(intentChangePW);
		// finish();
	}

	public static void startActivity(Activity activity, int valueExtra) {
		Intent i = new Intent(activity, HomeScreeenActivity.class);
		i.putExtra("modeBundleSetting", valueExtra);
		activity.startActivity(i);
	}

	public void onSecurityMode(View v) {
		SetupSecurityModeActivity.startActivity(this);
	}

	public void onRemoveData(View v) {
		SetupRemoveDataActivity.startActivity(this);
	}

	public void onGoogle(View v) {
		Intent intentDropbox = new Intent(this, GGDriveSettingActivity.class);
		startActivity(intentDropbox);
	}

	public void onRegisterEmailAddress(View v) {
		Intent intentRegisterEmail = new Intent(this,
				RegisterEmailActivity.class);
		intentRegisterEmail.putExtra(Contants.CREATE_NEW_EMAIL, false);
		startActivity(intentRegisterEmail);
	}

	public void onDropbox(View v) {
		Intent intentDropbox = new Intent(this, DropboxSettingActivity.class);
		startActivity(intentDropbox);
	}

	public void onUnlimitedItems(View v) {
		modePayment = PAYMENT_TO_UNLIMIT_ITEMS;
		if (!mPref.getIsPaymentUnlimit())
			showDialogRequestPayment(getResources().getString(
					R.string.message_pay_to_unlimit_item));
	}

	public void onNoAdmod(View v) {
		modePayment = PAYMENT_TO_NO_AD;
		if (!mPref.getIsPaymentNoAd())
			showDialogRequestPayment(getResources().getString(
					R.string.message_pay_to_no_ad));
	}

	public void showDialogRequestPayment(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon);
		builder.setTitle(getResources().getString(R.string.item_payment));
		builder.setMessage(message);
		builder.setPositiveButton(
				getResources().getString(R.string.confirm_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (modePayment == PAYMENT_TO_UNLIMIT_ITEMS) {
							BillingHelper.requestPurchase(
									HomeScreeenActivity.this,
									ID_ITEMS_PAYMENT_TO_UN_LIMIT);
						} else if (modePayment == PAYMENT_TO_NO_AD) {
							// mPref.setIsPaymentUnlimit(IdManagerPreference.IS_PAYMENT_UNLIMIT,
							// true);
							if (BillingHelper.isBillingSupported()) {
								BillingHelper.requestPurchase(
										HomeScreeenActivity.this,
										ID_ITEMS_PAYMENT_TO_NO_AD);
							} else {
								Log.i(TAG, "Can't purchase on this device");

							}

						} else if (modePayment == PAYMENT_TO_EXPORT) {
							BillingHelper.requestPurchase(
									HomeScreeenActivity.this,
									ID_ITEMS_PAYMENT_TO_EXPORT);
						}
					}
				});
		builder.setNegativeButton(
				getResources().getString(R.string.confirm_cancel),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		builder.create().show();
	}

	/**
	 * read file via cloud
	 * 
	 * @param v
	 */

	public void onReadFileviaCloud(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			if (mApi.getSession().isLinked()) {
				isExportData = false;
				String fileName = "";
				startReadFileViaCloud(fileName, false);
			} else if (!"".equals(mGGAccountName)) {
				startReadFileViaGGDrive();
			} else
				showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
		} else
			showDialog(Contants.DIALOG_NO_NET_WORK);
	}

	private void startReadFileViaGGDrive() {
		// TODO Auto-generated method stub
		/* gg drive api */
		credential = GoogleAccountCredential.usingOAuth2(
				HomeScreeenActivity.this, DriveScopes.DRIVE);
		credential.setSelectedAccountName(mGGAccountName);
		service = getDriveService(credential);

		ReadCSVViaGGDrive readFCsvViaGGDrive = new ReadCSVViaGGDrive(
				HomeScreeenActivity.this, service, mHandler);
		readFCsvViaGGDrive.execute();

	}

	/**
	 * export data to cloud
	 * 
	 * @param v
	 */
	public void onExportData(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			modePayment = PAYMENT_TO_EXPORT;
			if (!mPref.getIsPaymentExport())
				showDialogRequestPayment(getResources().getString(
						R.string.message_pay_to_export));
			else {
				if (mApi.getSession().isLinked()
						|| (!"".equals(mGGAccountName) && null != mGGAccountName)) {
					isExportData = true;
					showDialog(Contants.DIALOG_EXPORT_DATA);
				} else {
					showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
				}
			}

		} else
			showDialog(Contants.DIALOG_NO_NET_WORK);
	}

	protected void importFileCSVToDatabase(String mSelectedFile) {
		// TODO Auto-generated method stub
		// File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(Contants.PATH_ID_FILES + mSelectedFile);
		ArrayList<PasswordItem> mItems = new ArrayList<PasswordItem>();
		String group = null, title = null, icon = null, url = null, note = null, image = null;
		String[] id = new String[Contants.MAX_ITEM_PASS_ID];
		String[] password = new String[Contants.MASTER_PASSWORD_ID];
		int fav = 0;
		if (file.exists()) {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(file));
				String reader = "";
				int row = 0;
				try {
					while ((reader = in.readLine()) != null) {
						if (row > 0) {
							Log.e("xem chay may kan", "xem chay may kab " + row);
							String[] rowData = reader.split(",");
							ArrayList<String> rowDataList = new ArrayList<String>();
							// for(int )

							for (int i = 0; i < rowData.length; i++) {
								rowDataList.add(rowData[i]);
							}

							int size = rowDataList.size();
							if (size < 17)
								for (int i = size; i < 17; i++) {
									rowDataList.add("");
								}

							for (int i = 0; i < rowDataList.size(); i++) {
								Log.e("adjhfkshdf",
										"adsfkjh " + rowDataList.get(i));
								group = rowDataList.get(0);
								title = rowDataList.get(1);
								icon = rowDataList.get(2);
								fav = Integer.parseInt(rowDataList.get(3));
								url = rowDataList.get(4);
								note = rowDataList.get(5);
								image = rowDataList.get(6);

								id[0] = rowDataList.get(7);
								password[0] = rowDataList.get(8);
								id[1] = rowDataList.get(9);
								password[1] = rowDataList.get(10);
								id[2] = rowDataList.get(11);
								password[2] = rowDataList.get(12);
								id[3] = rowDataList.get(13);
								password[3] = rowDataList.get(14);
								id[4] = rowDataList.get(15);
								password[4] = rowDataList.get(16);
							}

							/* update to database */
							boolean isGExist = false;
							boolean isEExist = false;
							/* insert folder */
							if (sizeOfGList > 0)
								for (int i = 0; i < sizeOfGList; i++) {
									if (!group.equals(mGList.get(i).getgName()))
										isGExist = false;
									else
										isGExist = true;
									if (i == (sizeOfGList - 1) && !isGExist) {
										int gId = 0;
										for (int j = 0; j < sizeOfGList; j++)
											if (gId < mGList.get(j).getgId())
												gId = mGList.get(j).getgId();
										gId++;
										GroupFolder folder = new GroupFolder(
												gId, group, 0,
												Contants.MASTER_PASSWORD_ID, 0);
										mDataBaseHandler.addNewFolder(folder);
										mGList.add(folder);
										sizeOfGList++;
									}
								}
							else if (!isGExist) {
								isGExist = true;
								int gId = 0;
								for (int j = 0; j < sizeOfGList; j++)
									if (gId < mGList.get(j).getgId())
										gId = mGList.get(j).getgId();
								gId++;
								GroupFolder folder = new GroupFolder(gId,
										group, 0, Contants.MASTER_PASSWORD_ID,
										0);
								mDataBaseHandler.addNewFolder(folder);
								mGList.add(folder);
								sizeOfGList++;
							}

							/* insert element */
							for (int i = 0; i < sizeOfGList; i++) {
								List<ElementID> elementList = mDataBaseHandler
										.getAllElementIdByGroupFolderId(mGList
												.get(i).getgId());
								if (elementList.size() > 0)
									for (int j = 0; j < elementList.size(); j++) {
										if (title.equals(elementList.get(j)
												.geteTitle()))
											isEExist = true;

										if (j == sizeOfEList - 1 && !isEExist) {
											int eId = sizeOfEList;
											long timeStamp = System
													.currentTimeMillis();
											for (int k = 0; k < mEList.size(); k++)
												if (eId < mEList.get(k)
														.geteId())
													eId = mEList.get(k)
															.geteId();
											eId++;
											ElementID element = new ElementID(
													eId,
													mGList.get(i).getgId(),
													title, new byte[] {},
													timeStamp, fav, 0, url,
													note, new byte[] {}, 0);
											mDataBaseHandler
													.addElement(element);

											mEList.add(element);
											sizeOfEList++;
										}
									}
								else if (!isEExist) {
									isEExist = true;
									int eId = sizeOfEList;
									long timeStamp = System.currentTimeMillis();
									eId++;
									ElementID element = new ElementID(eId,
											mGList.get(i).getgId(), title,
											new byte[] {}, timeStamp, fav, 0,
											url, note, new byte[] {}, 0);
									mDataBaseHandler.addElement(element);
									mEList.add(element);
									sizeOfEList++;
								}

							}

							/* insert to password */
							int elementId = 0;
							for (int i = 0; i < sizeOfEList; i++) {
								if (title.equals(mEList.get(i).geteTitle()))
									elementId = mEList.get(i).geteId();
							}
							mDataBaseHandler
									.deletePasswordByElementId(elementId);
							Log.e("item", "item.size " + mItems.size());
							for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
								if (!"".equals(id[i])
										|| !"".equals(password[i])) {
									int pwId = sizeOfPList;
									for (int k = 0; k < sizeOfPList; k++)
										if (pwId < mPList.get(k)
												.getPasswordId())
											pwId = mPList.get(k)
													.getPasswordId();
									Password passWord = new Password(pwId,
											elementId, id[i], password[i]);
									mDataBaseHandler.addNewPassword(passWord);
									mPList.add(passWord);
									sizeOfPList++;
								}
							}
						}
						row++;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * delete file csv after upload to cloud
	 */
	private void deleteFileAfterUpload() {
		// TODO Auto-generated method stub
		File file = new File(Contants.PATH_ID_FILES + "/" + fileExportName);
		if (file.exists())
			file.delete();
	}

	private void generateCsvFile(String sFileName) {
		List<GroupFolder> groupList = mDataBaseHandler.getAllFolders();
		List<ElementID> elementList = mDataBaseHandler.getAllElmentIds();

		int sizeOfElementList = elementList.size();
		int sizeOfgroupList = groupList.size();
		try {
			FileWriter writer = new FileWriter(sFileName);
			writer.append("group");
			writer.append(",");
			writer.append("title");
			writer.append(",");
			writer.append("icon");
			writer.append(",");
			writer.append("fav");
			writer.append(",");
			writer.append("url");
			writer.append(",");
			writer.append("note");
			writer.append(",");
			writer.append("image");
			writer.append(",");

			writer.append("id1");
			writer.append(",");
			writer.append("pa1");
			writer.append(",");

			writer.append("id2");
			writer.append(",");
			writer.append("pa2");
			writer.append(",");

			writer.append("id3");
			writer.append(",");
			writer.append("pa3");
			writer.append(",");

			writer.append("id4");
			writer.append(",");
			writer.append("pa4");
			writer.append(",");

			writer.append("id5");
			writer.append(",");
			writer.append("pa5");
			writer.append(",");

			writer.append("\n");
			// generate whatever data you want
			for (int i = 0; i < sizeOfElementList; i++) {
				GroupFolder groupFolder = null;
				int groupFolderId = elementList.get(i).geteGroupId();
				for (int j = 0; j < sizeOfgroupList; j++)
					if (groupFolderId == groupList.get(j).getgId())
						groupFolder = groupList.get(j);
				writer.append("" + groupFolder.getgName());
				writer.append(",");
				writer.append("" + elementList.get(i).geteTitle());
				writer.append(",");
				writer.append("");
				writer.append(",");
				writer.append("" + elementList.get(i).geteFavourite());
				writer.append(",");
				writer.append("" + elementList.get(i).geteUrl());
				writer.append(",");
				writer.append("" + elementList.get(i).geteNote());
				writer.append(",");
				writer.append("");
				writer.append(",");

				List<Password> passwordList = mDataBaseHandler
						.getAllPasswordByElementId(elementList.get(i).geteId());
				int sizeOfPassWordList = passwordList.size();
				for (int k = 0; k < sizeOfPassWordList; k++) {
					writer.append("" + passwordList.get(k).getTitleNameId());
					writer.append(",");
					writer.append("" + passwordList.get(k).getPassword());
					writer.append(",");
				}

				writer.append("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(Contants.APP_KEY,
				Contants.APP_SECRET);
		AndroidAuthSession session;

		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE,
					accessToken);
		} else {
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
		}

		return session;
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a
	 * local store, rather than storing user name & password, and
	 * re-authenticating each time (which is not to be done, ever).
	 * 
	 * @return Array of [access_key, access_secret], or null if none stored
	 */
	private String[] getKeys() {
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(ACCESS_KEY_NAME, null);
		String secret = prefs.getString(ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		} else {
			return null;
		}
	}

	private void startSyncToCloud(String fileExportName,
			boolean isCheckDuplicated) {
		// TODO Auto-generated method stub
		File fileExport = new File(Contants.PATH_ID_FILES + "/"
				+ fileExportName);
		if (fileExport.exists()) {
			DropBoxController newFile = new DropBoxController(
					HomeScreeenActivity.this, mApi,
					Contants.FOLDER_ON_DROPBOX_CSV, fileExport, mHandler,
					isCheckDuplicated);
			newFile.execute();
		} else {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * start read file via cloud
	 */
	private void startReadFileViaCloud(CharSequence fileName,
			boolean isCheckFile) {
		Log.e("isCheckFile", "isCheckFile " + isCheckFile);
		File file = new File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();
		String mFilePath = file.getAbsolutePath();
		ReadFileViaDropBox readFile = new ReadFileViaDropBox(
				HomeScreeenActivity.this, mApi, Contants.FOLDER_ON_DROPBOX_CSV,
				mFilePath, mHandler, fileName, isCheckFile);
		readFile.execute();
	}

	class PasswordItem {
		String titleId;
		String password;
	}

	public void setWidthScreen(int width) {
		this.widthScreen = width;
	}

	public int getWidthScreen() {
		return widthScreen;
	}

	/**
	 * save file csv to gg drive in idpw file
	 */

	private void saveFileToDrive(String accountName, boolean isCheckedTime,
			String filePath) {
		java.io.File csvFile = new File(filePath);

		GGDriveUploadCSVController drive = new GGDriveUploadCSVController(this,
				service, csvFile, mHandler, accountName);
		drive.execute();
	}

	/**
	 * get authen to access gg drive
	 */
	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).build();
	}

	private void saveFileToDevice(String accountName, CharSequence mSeletedFile) {
		String fileName = (String) mSeletedFile;
		String cachePath = Contants.PATH_ID_FILES + fileName;
		java.io.File file = new java.io.File(cachePath);
		DownloadCSVViaGGDrive drive = new DownloadCSVViaGGDrive(this, service,
				file, mHandler, fileName, true);
		drive.execute();
	}

	// /
	// ////methods of info actvity
	public void onCreateInfo() {
		urlInfo = "http://www.japanappstudio.com/home.html";
		initControlInfo();
		initAdmodInfo();
		editTextInfo = (EditText) findViewById(R.id.id_edit_browser);

	}

	public void initAdmodInfo() {
		adview = (AdView) findViewById(R.id.main_adView_browser);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			if (!mPref.getIsPaymentNoAd())
				adview.setVisibility(View.VISIBLE);
			else
				adview.setVisibility(View.GONE);
		}
	}

	public void onReload(View v) {
		webViewInfo.reload();
	}

	public void onBack(View v) {
		webViewInfo.goBack();
	}

	public void onNext(View v) {
		try {
			KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_SHIFT_RIGHT, 0, 0);
			shiftPressEvent.dispatch(webViewInfo);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	private void initControlInfo() {
		// TODO Auto-generated method stub
		webViewInfo = (WebView) findViewById(R.id.web_view);
		webViewInfo.loadUrl(urlInfo);
		webViewInfo.setContentDescription("application/pdf");
		WebSettings webSettings = webViewInfo.getSettings();
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webViewInfo.invokeZoomPicker();
		this.webViewInfo.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				editTextInfo.setText(webViewInfo.getUrl());
				super.onPageFinished(view, url);
			}
		});

	}

	// methods of edit idxpassword activity
	public void onCreateIdxPassword() {
		updateViewEditIdxpass();
		currentFolderId = mPref.getCurrentFolderId();
		icon = new byte[] {};
		imageMemo = new byte[] {};
		if (modeBundle == 1) {
			currentElementId = -1;
		}

		/* initialize database */

		weightItem = mPref.getWeightSlideItem();
		/* initialize control */
		if (modeBundle <= 1) {
			mItems = loadDataForListItem();
		}
		updateItems(mItems);
		if (modeBundle == 0) {
			isCreatNew = false;
			mDrawableIcon = getIconDataBase(icon);
			mDrawableMemo = getMemoDataBase(imageMemo);

		} else if (modeBundle == 1) {
			isCreatNew = true;
			mDrawableIcon = null;
			mDrawableMemo = null;
		} else {
			mEditTextNameId.setText(titleRecord);
			mEditTextNote.setText(note);
			mEditTextUrlId.setText(mUrlItem);
		}

	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		if (weightItem > 0) {
			view1.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 0, weightItem));
			view2.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 0, 1 - weightItem));
			lnParent.invalidate();
		}

	}

	public void initAdmodEditIdxPass() {
		AdView adview = (AdView) findViewById(R.id.main_adView_edit_idxpass);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	public void initViewEditIdxPass() {
		initSlideView();
		initViewItem();
		initControlEditIdxPassword();
		initAdmodEditIdxPass();
	}

	public void initSlideView() {
		controlView = (ImageView) findViewById(R.id.img);
		view1 = (LinearLayout) findViewById(R.id.ln_1);
		view2 = (LinearLayout) findViewById(R.id.ln_2);
		lnParent = (LinearLayout) findViewById(R.id.id_parent);
		controlView.setOnTouchListener(new OnTouchListener() {
			float yFirst, yMove;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					yMove = yFirst = event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_MOVE:
					yFirst = yMove;
					yMove = event.getRawY();
					move(yMove - yFirst);
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	public void initViewItem() {
		lnRowItem = (LinearLayout) findViewById(R.id.id_ln_rowItem);
		mEditTitle = new EditText[MAX_ITEM];
		mEditTitle[0] = (EditText) findViewById(R.id.id_txt_nameItem1);
		mEditTitle[1] = (EditText) findViewById(R.id.id_txt_nameItem2);
		mEditTitle[2] = (EditText) findViewById(R.id.id_txt_nameItem3);
		mEditTitle[3] = (EditText) findViewById(R.id.id_txt_nameItem4);
		mEditTitle[4] = (EditText) findViewById(R.id.id_txt_nameItem5);
		for (int i = 0; i < mEditTitle.length; i++) {
			final int pos = i;

			mEditTitle[i].addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (mItems != null)
						mItems.get(pos).mNameItem = s.toString();
				}
			});
		}
		mEditContent = new EditText[MAX_ITEM];
		mEditContent[0] = (EditText) findViewById(R.id.id_txt_detailItem1);
		mEditContent[1] = (EditText) findViewById(R.id.id_txt_detailItem2);
		mEditContent[2] = (EditText) findViewById(R.id.id_txt_detailItem3);
		mEditContent[3] = (EditText) findViewById(R.id.id_txt_detailItem4);
		mEditContent[4] = (EditText) findViewById(R.id.id_txt_detailItem5);
		for (int i = 0; i < mEditTitle.length; i++) {
			final int pos = i;

			mEditContent[i].addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (mItems != null)
						mItems.get(pos).mContentItem = s.toString();
				}
			});
		}
		btnGenerator = new ImageButton[MAX_ITEM];
		btnGenerator[0] = (ImageButton) findViewById(R.id.id_btn_generator1);
		btnGenerator[1] = (ImageButton) findViewById(R.id.id_btn_generator2);
		btnGenerator[2] = (ImageButton) findViewById(R.id.id_btn_generator3);
		btnGenerator[3] = (ImageButton) findViewById(R.id.id_btn_generator4);
		btnGenerator[4] = (ImageButton) findViewById(R.id.id_btn_generator5);
		for (int i = 0; i < btnGenerator.length; i++) {
			final int pos = i;
			btnGenerator[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onToGenerator(pos);
				}
			});
		}
	}

	public void updateViewEditIdxpass() {
		for (int i = 0; i < mEditTitle.length; i++) {
			mEditTitle[i].setText("");
			mEditContent[i].setText("");
		}
		mEditTextNameId.setText("");
		mEditTextNote.setText("");
		mEditTextUrlId.setText("");
		mCheckBoxLike.setChecked(false);
		isButtonPress = false;
	}

	public void updateItems(ArrayList<Item> mItems) {
		if (mItems == null || mItems.size() < MAX_ITEM)
			return;
		for (int i = 0; i < MAX_ITEM; i++) {
			mEditTitle[i].setText(mItems.get(i).mNameItem);
			mEditContent[i].setText(mItems.get(i).mContentItem);
		}
	}

	public void move(float delta) {
		int hRow = lnRowItem.getHeight();
		int h3 = controlView.getHeight();
		int h = lnParent.getHeight();
		float weightMin = (2.1f * hRow) / (float) (h - h3);
		float weightMax = (5.1f * hRow) / (float) (h - h3);
		float detalWeight = delta / (h - h3);
		if (delta == 0)
			return;
		else {
			float weight1 = ((LinearLayout.LayoutParams) view1
					.getLayoutParams()).weight;
			weight1 = weight1 + detalWeight;
			if (weight1 < weightMin)
				weight1 = weightMin;
			else if (weight1 > weightMax)
				weight1 = weightMax;
			view1.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 0, weight1));
			view2.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 0, 1 - weight1));
			lnParent.invalidate();

		}
	}

	public Drawable getIconDataBase(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}

		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		BitmapDrawable result = new BitmapDrawable(bMap);
		return result;
	}

	public Drawable getMemoDataBase(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}

		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);

		BitmapDrawable result = new BitmapDrawable(bMap);
		return result;
	}

	public static void setRatioMemo(float k) {
		ratioMemo = k;
	}

	/**
	 * initialize database
	 */

	/**
	 * initialize control
	 */
	private void initControlEditIdxPassword() {
		// TODO Auto-generated method stub
		btn_memo = (Button) findViewById(R.id.button_img_memo);
		img_memo = (ImageButton) findViewById(R.id.btn_img_memo);
		img_avata = ((ImageButton) findViewById(R.id.img_avatar));
		mEditTextNameId = (EditText) findViewById(R.id.id_name_id_pass);

		mEditTextNote = (EditText) findViewById(R.id.edit_text_note);

		mEditTextUrlId = (EditText) findViewById(R.id.edit_text_url);

		mCheckBoxLike = (CheckBox) findViewById(R.id.id_like);

		mCheckBoxLike.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked)
					isLike = 1;
				else
					isLike = 0;
			}
		});
		/* load data for list item id */
	}

	/**
	 * load data for list item
	 * 
	 * @return
	 */
	private ArrayList<Item> loadDataForListItem() {
		ArrayList<Item> itemList = new ArrayList<Item>();

		/* is create new id */
		if (modeBundle == 1) {
			for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
				Item item = new Item();
				item.mNameItem = DEFAULT_NAME_ITEM[i];
				item.mContentItem = "";
				itemList.add(item);

			}
			mUrlItem = DEFAULT_URL;
			((ImageButton) findViewById(R.id.btn_img_memo))
					.setVisibility(View.GONE);
		} else if (modeBundle == 0) {
			ElementID element = mDataBaseHandler.getElementID(currentElementId);
			mEditTextNote.setText(element.geteNote());
			mUrlItem = element.geteUrl();
			imageMemo = element.geteMemoData();
			mEditTextNameId.setText(element.geteTitle());
			mEditTextUrlId.setText(mUrlItem);
			icon = element.geteIconData();
			if (element.geteFavourite() == 0)
				mCheckBoxLike.setChecked(false);
			else if (element.geteFavourite() == 1)
				mCheckBoxLike.setChecked(true);
			List<Password> listPass = mDataBaseHandler
					.getAllPasswordByElementId(currentElementId);
			for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
				Item item = new Item();
				if (i < listPass.size()) {
					item.mNameItem = listPass.get(i).getTitleNameId();
					item.mContentItem = listPass.get(i).getPassword();
				} else {
					item.mNameItem = DEFAULT_NAME_ITEM[i];
					item.mContentItem = "";
				}
				itemList.add(item);
			}
		}
		return itemList;
	}

	public void onImgAvatar(View v) {
		ListIconActivity.startActivity(this);
		saveInput();
	}

	public void onToGenerator(int i) {
		itemSelect = i;
		mStringOfSelectItem = mItems.get(itemSelect).mContentItem;
		saveInput();
		PasswordGeneratorActivity.startActivity(this);

	}

	public void saveInput() {
		titleRecord = mEditTextNameId.getText().toString();
		mUrlItem = mEditTextUrlId.getText().toString();
		note = mEditTextNote.getText().toString();
	}

	public void onReturn(View v) {
		float weight1 = ((LinearLayout.LayoutParams) view1.getLayoutParams()).weight;
		mPref.setWeightSlideItem(weight1);
		if (isButtonPress)
			return;
		isButtonPress = true;
		createOrUpdateId();
		onResume();
	}

	public void onInfo(View v) {
		saveInput();
		Intent intentBrowser = new Intent(this, BrowserActivity.class);
		intentBrowser.putExtra(Contants.KEY_TO_BROWSER, Contants.INFO_TO);
		startActivity(intentBrowser);
	}

	public void onGoogleHome(View v) {
		isSetUrl = true;
		saveInput();
		SettingURLActivity.startActivity(this);

	}

	public void onMemoImage(View v) {
		if (btn_memo.getVisibility() == View.VISIBLE)
			widthMemo = btn_memo.getWidth();
		else
			widthMemo = img_memo.getWidth();
		Intent intentMemo = new Intent(this, ImageMemoActivity.class);
		intentMemo.putExtra("modeBundleMemo", 1);
		startActivityForResult(intentMemo, Contants.INTENT_IMG_MEMO);
	}

	/**
	 * create new id password
	 */
	private void createOrUpdateId() {
		addNewIdValuesToDataBase();
		panelEditIdxpass.slidePanel();
	}

	public byte[] getImageData(Drawable pDrawable) {
		if (pDrawable == null)
			return new byte[] {};
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		Bitmap bMap = drawableToBitmap(pDrawable);
		bMap.compress(Bitmap.CompressFormat.PNG, 60, outStream);
		byte[] convertToByte = outStream.toByteArray();
		return convertToByte;
	}

	/**
	 * add new id password to database
	 */
	private void addNewIdValuesToDataBase() {
		titleRecord = mEditTextNameId.getText().toString();
		url = mEditTextUrlId.getText().toString();
		if (titleRecord.equals(""))
			return;
		note = mEditTextNote.getText().toString();
		int elementId = -1;

		if (isCreatNew) {
			List<ElementID> elementList = mDataBaseHandler.getAllElmentIds();
			int sizeOfElementId = elementList.size();
			elementId = sizeOfElementId;
			for (int i = 0; i < sizeOfElementId; i++) {
				if (elementId < elementList.get(i).geteId())
					elementId = elementList.get(i).geteId();
			}
			elementId++;
		} else
			elementId = currentElementId;

		ElementID newElement = new ElementID(elementId, currentFolderId,
				titleRecord, getImageData(mDrawableIcon),
				System.currentTimeMillis(), isLike, ELEMENT_FLAG_FALSE, url,
				note, getImageData(mDrawableMemo),
				mDataBaseHandler.getElementsCountFromFolder(currentFolderId));

		// create id int normal folder
		if (isCreatNew) {
			mDataBaseHandler.addElement(newElement);
			mPref.setNumberItem(IdManagerPreference.NUMBER_ITEMS,
					mPref.getNumberItems(IdManagerPreference.NUMBER_ITEMS) + 1);
		} else
			mDataBaseHandler.updateElement(newElement);

		/* update password */
		mDataBaseHandler.deletePasswordByElementId(elementId);
		List<Password> passWordList = mDataBaseHandler.getAllPasswords();
		int sizeOfPasswordList = passWordList.size();
		int passId = 0;
		passId = mDataBaseHandler.getPasswordsCount();
		for (int i = 0; i < sizeOfPasswordList; i++) {
			if (passId < passWordList.get(i).getPasswordId())
				passId = passWordList.get(i).getPasswordId();
		}
		passId++;
		for (int i = 0; i < mItems.size(); i++) {
			Password newPass = new Password(passId, elementId,
					mItems.get(i).mNameItem, mItems.get(i).mContentItem);
			mDataBaseHandler.addNewPassword(newPass);
			passId++;
		}
		/* return home */

	}

	public static void updateIcon(Drawable pDrawable) {
		mDrawableIcon = pDrawable;
	}

	public static void updateMemo(Drawable pDrawable) {
		mDrawableMemo = pDrawable;
	}

	public static Drawable getIcon() {
		return mDrawableIcon;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		int width = drawable.getIntrinsicWidth();
		width = width > 0 ? width : 1;
		int height = drawable.getIntrinsicHeight();
		height = height > 0 ? height : 1;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public void slidePanelEditIdxPass() {
		panelEditIdxpass.slidePanel();
		onCreateIdxPassword();
		onResumeEditIdxPass();
		onAttachedToWindow();
		mPref.setEditIDxPassHome(true);
	}

	public void closeEditIdxPass() {
		if (panelEditIdxpass.getVisibility() != View.VISIBLE) {
			float weight1 = ((LinearLayout.LayoutParams) view1
					.getLayoutParams()).weight;
			mPref.setWeightSlideItem(weight1);
		}
	}
}
