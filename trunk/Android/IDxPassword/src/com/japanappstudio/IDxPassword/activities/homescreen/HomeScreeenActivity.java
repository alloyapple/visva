package com.japanappstudio.IDxPassword.activities.homescreen;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.japanappstudio.IDxPassword.activities.BaseActivity;
import com.japanappstudio.IDxPassword.activities.BrowserActivity;
import com.japanappstudio.IDxPassword.activities.CopyItemActivity;
import com.japanappstudio.IDxPassword.activities.EditIdPasswordActivity;
import com.japanappstudio.IDxPassword.activities.EnterOldPasswordActivity;
import com.japanappstudio.IDxPassword.activities.R;
import com.japanappstudio.IDxPassword.activities.RegisterEmailActivity;
import com.japanappstudio.IDxPassword.activities.SettingActivity;
import com.japanappstudio.IDxPassword.activities.SetupRemoveDataActivity;
import com.japanappstudio.IDxPassword.activities.SetupSecurityModeActivity;
import com.japanappstudio.IDxPassword.activities.slide_activity.Panel;
import com.japanappstudio.IDxPassword.activities.syncloud.GGDriveSettingActivity;
import com.japanappstudio.IDxPassword.activities.syncloud.SyncCloudActivity;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.idletime.ControlApplication;
import com.japanappstudio.IDxPassword.idxpwdatabase.ElementID;
import com.japanappstudio.IDxPassword.idxpwdatabase.GroupFolder;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;

import exp.mtparet.dragdrop.adapter.FolderListViewAdapter;
import exp.mtparet.dragdrop.adapter.ItemAdapter;
import exp.mtparet.dragdrop.view.DndListViewFolder;
import exp.mtparet.dragdrop.view.ListViewDragDrop;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class HomeScreeenActivity extends BaseActivity implements
		OnClickListener {

	// ==========================Control define ====================
	// private FrameLayout mainRootLayout;
	private LinearLayout mainRootLayout;
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

	// ===========================Class Define =====================
	private ItemAdapter itemAdapter;
	private FolderListViewAdapter folderListViewAdapter;
	private ElementID oneItemSelected;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	private ArrayList<GroupFolder> mFolderListItems = new ArrayList<GroupFolder>();
	private ArrayList<ElementID> mIdListItems = new ArrayList<ElementID>();
	private IdManagerPreference mIdManagerPreference;
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
		@SuppressWarnings("deprecation")
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
			if (!mIdManagerPreference.getIsPaymentNoAd())
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
		mainRootLayout = (LinearLayout) LinearLayout.inflate(context,
				R.layout.page_home_screen, null);
		// mainRootLayout = (FrameLayout) LinearLayout.inflate(context,
		// R.layout.page_home_screen_slide, null);
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

		// panelSetting = (Panel) mainRootLayout
		// .findViewById(R.id.l_home_panel_setting);
		// panelSetting.setHandle(btnSetting);
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
		itemAdapter = new ItemAdapter(context, mIdListItems, false,
				mMainHandler, idListView, currentFolderId, currentFolderOrder);
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
		mIdManagerPreference = IdManagerPreference.getInstance(this);
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

		@SuppressWarnings("deprecation")
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

	@SuppressWarnings("deprecation")
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
				// List<ElementID> elementList = mIDxPWDataBaseHandler
				// .getAllElementIdByGroupFolderId(currentFolderId);
				int number_items = mIdManagerPreference
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
			mIdManagerPreference.setEditMode(true);
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
			mIdManagerPreference.setEditMode(false);
		}

		/* setting */
		else if (v == btnSetting) {
			Intent intentSeting = new Intent(HomeScreeenActivity.this,
					SettingActivity.class);
			startActivity(intentSeting);
		}

		/* sync data to cloud */
		else if (v == btnSync) {
			Intent intentSync = new Intent(HomeScreeenActivity.this,
					SyncCloudActivity.class);
			startActivity(intentSync);
		}

		/* go to a browser */
		else if (v == btnInfo) {
			Intent intentBrowser = new Intent(HomeScreeenActivity.this,
					BrowserActivity.class);
			intentBrowser.putExtra(Contants.KEY_TO_BROWSER, Contants.INFO_TO);
			startActivity(intentBrowser);
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
			break;
		}
	}

	/**
	 * add new folder to group
	 */
	@SuppressWarnings("deprecation")
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
		int currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		Intent newIdIntent = new Intent(HomeScreeenActivity.this,
				EditIdPasswordActivity.class);
		newIdIntent.putExtra(Contants.IS_INTENT_CREATE_NEW_ID, 1);
		mIdManagerPreference.setCurrentFolderId(currentFolderId);
		newIdIntent.putExtra(Contants.CURRENT_FOLDER_ID, currentFolderId);
		startActivity(newIdIntent);
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
		mIdManagerPreference.setNumberItem(IdManagerPreference.NUMBER_ITEMS,
				mIdManagerPreference
						.getNumberItems(IdManagerPreference.NUMBER_ITEMS) - 1);
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
		if (!mIdManagerPreference.getIsPaymentNoAd())
			adview.setVisibility(View.VISIBLE);
		else
			adview.setVisibility(View.GONE);
		/* refresh 2 list view */
		refreshListView();
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

	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mIdManagerPreference.getSecurityMode() == Contants.KEY_OFF) {
				finish();
			} else
				showDialog(Contants.DIALOG_EXIT);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		getApp().stop();
		super.onDestroy();
		mIdManagerPreference.setEditMode(false);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public Drawable getImageDataBase(byte[] data) {
		if (data == null || data.length == 0) {
			return getResources().getDrawable(R.drawable.default_icon);
		}

		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		@SuppressWarnings("deprecation")
		BitmapDrawable result = new BitmapDrawable(bMap);
		return result;
	}

	// // method of setting

	public void onReturn(View v) {
		finish();
	}

	public void onChangeMasterPass(View v) {
		Intent intentChangePW = new Intent(this, EnterOldPasswordActivity.class);
		intentChangePW.putExtra(EnterOldPasswordActivity.KEY_MODE,
				EnterOldPasswordActivity.FROM_SETTING);
		startActivity(intentChangePW);
		finish();
	}

	public static void startActivity(Activity activity, int valueExtra) {
		Intent i = new Intent(activity, SettingActivity.class);
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

	private Handler mHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			Log.e("adkjfh", "adskfjhd " + msg.arg1);
			if (msg.arg1 == Contants.DIALOG_MESSAGE_FOLDER_EXISTED)
				showDialog(Contants.DIALOG_MESSAGE_FOLDER_EXISTED);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_FOLDER_ERROR)
				showDialog(Contants.DIALOG_MESSAGE_FOLDER_ERROR);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR)
				showDialog(Contants.DIALOG_MESSAGE_FOLDER_INSERT_ERROR);
		};
	};
}
