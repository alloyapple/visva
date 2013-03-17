package visvateam.outsource.idmanager.activities.homescreen;

import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import net.sqlcipher.database.SQLiteDatabase;

import visvateam.outsource.idmanager.activities.BaseActivity;
import visvateam.outsource.idmanager.activities.BrowserActivity;
import visvateam.outsource.idmanager.activities.CopyItemActivity;
import visvateam.outsource.idmanager.activities.EditIdPasswordActivity;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.activities.SettingActivity;
import visvateam.outsource.idmanager.activities.synccloud.SyncCloudActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.idletime.ControlApplication;
import visvateam.outsource.idmanager.idxpwdatabase.ElementID;
import visvateam.outsource.idmanager.idxpwdatabase.GroupFolder;
import visvateam.outsource.idmanager.idxpwdatabase.IDxPWDataBaseHandler;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import exp.mtparet.dragdrop.adapter.FolderListViewAdapter;
import exp.mtparet.dragdrop.adapter.ItemAdapter;
import exp.mtparet.dragdrop.data.OneItem;
import exp.mtparet.dragdrop.view.ListViewDragDrop;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class HomeScreeenActivity extends BaseActivity implements OnClickListener {
	private final static int MAX_ITEMS = 12;
	// ==========================Control define ====================
	private LinearLayout mainRelativeLayout;
	private ListViewDragDrop idListView;
	private ListViewDragDrop folderListView;

	/* layout drag */
	private RelativeLayout layoutDrag;
	private ImageView imageDrag;
	private TextView txtIdName;
	private TextView txtIdUrl;

	private Button btnSetting;
	private Button btnAddNewFolder;
	private Button btnAddNewId;
	private Button btnEdit;
	private Button btnSync;
	private Button btnInfo;
	private Button btnSearch;
	private Button btnClearTextSearch;

	private EditText mEditTextSearch;

	// ===========================Class Define =====================
	private ItemAdapter itemAdapter;
	private FolderListViewAdapter folderListViewAdapter;
	private OneItem oneItemSelected;
	// private DataBaseHandler mDataBaseHandler;
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

	private Handler mMainHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			Log.e("get msg", "get mass " + msg.arg1);
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
		initAdmod();
	}

	public void initAdmod() {
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * initialize control and view
	 */
	private void initControl() {
		/* init layout */
		mainRelativeLayout = (LinearLayout) LinearLayout.inflate(context,
				R.layout.page_home_screen, null);

		/* init layout drag */
		initLayoutDrag();

		/* init listview folder */
		initListViewFolder();

		/* init ListView Id */
		initListViewId();

		/* init button */
		btnAddNewFolder = (Button) mainRelativeLayout.findViewById(R.id.btn_add_new_folder);
		btnAddNewFolder.setOnClickListener(this);

		btnAddNewId = (Button) mainRelativeLayout.findViewById(R.id.btn_plus);
		btnAddNewId.setOnClickListener(this);

		btnEdit = (Button) mainRelativeLayout.findViewById(R.id.btn_edit);
		btnEdit.setOnClickListener(this);

		btnSetting = (Button) mainRelativeLayout.findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(this);

		btnSync = (Button) mainRelativeLayout.findViewById(R.id.btn_sync);
		btnSync.setOnClickListener(this);

		btnInfo = (Button) mainRelativeLayout.findViewById(R.id.btn_main_info);
		btnInfo.setOnClickListener(this);

		btnSearch = (Button) mainRelativeLayout.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(this);

		btnClearTextSearch = (Button) mainRelativeLayout.findViewById(R.id.btn_close);
		btnClearTextSearch.setOnClickListener(this);
		btnClearTextSearch.setVisibility(View.GONE);

		/* init editText */
		mEditTextSearch = (EditText) mainRelativeLayout.findViewById(R.id.edit_text_search);
		mEditTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				btnClearTextSearch.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		/* set contentView for home screen layout */
		setContentView(mainRelativeLayout);

	}

	private void initListViewId() {
		/* init listview */
		idListView = (ListViewDragDrop) mainRelativeLayout.findViewById(R.id.list_view_item);

		/* init adapter for listview */
		// set for search item list
		mIdListItems = constructList(currentFolderItem);
		// mIdListItems = constructSearchList();
		currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		itemAdapter = new ItemAdapter(context, mIdListItems, false, mMainHandler, idListView,
				currentFolderItem, currentFolderId);
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
	}

	private void initListViewFolder() {
		// TODO Auto-generated method stub
		folderListView = (ListViewDragDrop) mainRelativeLayout.findViewById(R.id.list_view_folder);
		folderListViewAdapter = new FolderListViewAdapter(this, mFolderListItems, false,
				mMainHandler, folderListView, currentFolderItem, isSearchMode);
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
	}

	private void initLayoutDrag() {
		/* init layout drag */
		layoutDrag = (RelativeLayout) mainRelativeLayout.findViewById(R.id.layoutDrag);
		imageDrag = (ImageView) mainRelativeLayout.findViewById(R.id.imageView1);
		txtIdName = (TextView) mainRelativeLayout.findViewById(R.id.txt_id_name);
		txtIdUrl = (TextView) mainRelativeLayout.findViewById(R.id.txt_id_url);
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
		// check size of folder database
		// checkSizeFolderDataBase();
		// get data from folder database
		loadDataFromFolderDataBase();
	}

	/**
	 * load data from folder database to display on listview group
	 */
	private void loadDataFromFolderDataBase() {
		List<GroupFolder> folderList = mIDxPWDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();

		for (int i = 0; i < sizeOfFolder; i++) {
			GroupFolder folder = new GroupFolder(folderList.get(i).getgId(), folderList.get(i)
					.getgName(), folderList.get(i).getgType(), folderList.get(i).getgUserId(),
					folderList.get(i).getgOrder());
			mFolderListItems.add(folder);
		}
		// add folder favourite and history
		GroupFolder favouriteFolder = new GroupFolder(-1, Contants.NAME_FAVOURITE_FOLDER, 0,
				Contants.MASTER_PASSWORD_ID, -1);
		mFolderListItems.add(favouriteFolder);

		// add folder history
		GroupFolder historyFolder = new GroupFolder(-2, Contants.NAME_HISTORY_FOLDER, 0,
				Contants.MASTER_PASSWORD_ID, -2);
		mFolderListItems.add(historyFolder);
	}

	/**
	 * Save selected item
	 */
	private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			/**
			 * retrieve selected item from adapterview
			 */
			oneItemSelected = (OneItem) arg0.getItemAtPosition(arg2);
			imageDrag.setImageDrawable(EditIdPasswordActivity.getIconDatabase(oneItemSelected
					.getIconId()));
			txtIdName.setText(oneItemSelected.getName());
			txtIdUrl.setText(oneItemSelected.getUrl());
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
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			if (!isEdit)
				CopyItemActivity.startActivity(HomeScreeenActivity.this, mIdListItems.get(arg2)
						.geteId());
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
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			// TODO Auto-generated method stub
			Log.e("aaaaaaaa", "bbbbbbbb " + position);
			currentFolderItem = position;
			currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
			/* refresh folder list */
			folderListViewAdapter.setFolderSelected(currentFolderItem);

			/* reset adapter id */
			if (position == 0) {
				// folder search
				if (!"".equals(mTextSearch))
					mIdListItems = startSearch(mTextSearch);
				else
					mIdListItems.clear();
			} else if (position == 1) {
				// folder favourite
				mIdListItems = constructFavouriteList();
			} else if (position == 2) {
				// folder history
				mIdListItems = constructHistoryList();

			} else
				mIdListItems = constructList(currentFolderId);
			itemAdapter.setIdItemList(mIdListItems, currentFolderItem, currentFolderId);
		}
	};
	private OnItemClickListener listenerReceivePicture = new OnItemClickListener() {

		@SuppressWarnings("deprecation")
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (oneItemSelected != null) {
				// receverAdapter.addPicture(oneItemSelected, arg2);
				mCurrentFolderPosition = mCurrentFirstVisibleFolderItem + arg2;
				mCurrentId = oneItemSelected.getPasswordId();
				ElementID elementId = mIDxPWDataBaseHandler.getElementID(mCurrentId);

				Log.e("mCurrentFolderPostion " + mCurrentFolderPosition, "mCurrentFirtsVisible "
						+ mCurrentFirstVisibleFolderItem);
				if (isEdit && mCurrentFolderPosition > 0
						&& elementId.geteGroupId() != mCurrentFolderPosition) {
					Log.e("item selected", "item " + oneItemSelected.getPasswordId());
					showDialog(Contants.DIALOG_MOVE_ID_TO_FOLDER);

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
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
				int totalItemCount) {
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
				layout.topMargin = (int) event.getY() - layoutDrag.getHeight() / 2;
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
			Log.e("name", "name " + elementList.get(i).geteTitle());
			ElementID item = new ElementID(elementList.get(i).geteId(), elementList.get(i)
					.geteGroupId(), elementList.get(i).geteTitle(), elementList.get(i).geteIcon(),
					elementList.get(i).geteTimeStamp(), elementList.get(i).geteFavourite(),
					elementList.get(i).geteFlag(), elementList.get(i).geteUrl(), elementList.get(i)
							.geteNote(), elementList.get(i).geteImage(), elementList.get(i)
							.geteOrder());
			al.add(item);  
		}
		return al;
	}

	/**
	 * is favourite folder items
	 * 
	 * @param currentFolderItem
	 * @return
	 */
	private ArrayList<ElementID> constructFavouriteList() {

		List<ElementID> idList = mIDxPWDataBaseHandler.getAllElmentIds();
		ArrayList<ElementID> al = new ArrayList<ElementID>();
		// int idListSize = idList.size();
		// Log.e("idSize ", "id size" + idListSize);
		// for (int i = 0; i < idListSize; i++) {
		// Log.e("sdfasd " + i, "adfdf " + idList.get(i).getLike());
		// if (idList.get(i).getLike() == Contants.IS_FAVOURITE) {
		// OneItem item = new OneItem(idList.get(i).getPassWordId(),
		// idList.get(i).getIcon(),
		// idList.get(i).getTitleRecord(), idList.get(i).getUrl());
		// al.add(item);
		// }
		// }
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
		Log.e("size ", "size " + size);
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
		Log.e("size ", "size2 " + idList.size());
		int numberToView = 0;
		if (size < 10)
			numberToView = size;
		else
			numberToView = 10;
		for (int i = 0; i < numberToView; i++) {
			ElementID item = new ElementID(idList.get(i).geteId(), idList.get(i).geteGroupId(),
					idList.get(i).geteTitle(), idList.get(i).geteIcon(), idList.get(i)
							.geteTimeStamp(), idList.get(i).geteFavourite(), idList.get(i)
							.geteFlag(), idList.get(i).geteUrl(), idList.get(i).geteNote(), idList
							.get(i).geteImage(), idList.get(i).geteOrder());
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
			addNewFolder();
		}

		/* add new id */
		else if (v == btnAddNewId) {
			// EditIdPasswordActivity.startActivity(this);
			if (currentFolderItem > 2) {
				if (mIdManagerPreference
						.getIsPaymentUnlimit(IdManagerPreference.IS_PAYMENT_UNLIMIT)
						&& mIdManagerPreference.getNumberItems(IdManagerPreference.NUMBER_ITEMS) >= MAX_ITEMS)
					showDialog(Contants.DIALOG_CREATE_ID);
				else {
					startIntentCreateNewIds();
				}
			} else
				showToast("Choose another folder to add idxpassword");
		}

		/* edit listview */
		else if (v == btnEdit) {
			if (isEdit) {
				btnEdit.setBackgroundResource(R.drawable.edit);
				btnSync.setVisibility(View.VISIBLE);
				isEdit = false;
			} else {
				btnSync.setVisibility(View.GONE);
				btnEdit.setBackgroundResource(R.drawable.return_back);
				isEdit = true;
			}

			/* set folder and id listview in edit mode */
			setEditModeForFolderAndIdListView(isEdit);
		}

		/* setting */
		else if (v == btnSetting) {
			Intent intentSeting = new Intent(HomeScreeenActivity.this, SettingActivity.class);
			startActivity(intentSeting);
		}

		/* sync data to cloud */
		else if (v == btnSync) {
			Intent intentSync = new Intent(HomeScreeenActivity.this, SyncCloudActivity.class);
			startActivity(intentSync);
		}

		/* go to a browser */
		else if (v == btnInfo) {
			Intent intentBrowser = new Intent(HomeScreeenActivity.this, BrowserActivity.class);
			intentBrowser.putExtra(Contants.KEY_TO_BROWSER, Contants.INFO_TO);
			startActivity(intentBrowser);
		} else if (v == btnSearch) {
			if ("".equals(mEditTextSearch.getText().toString())) {
				showToast("Type to edit text to search");
			} else {
				showToast("Start search");
				mTextSearch = mEditTextSearch.getText().toString();
				/* clear text */
				mEditTextSearch.setText("");

				mIdListItems = startSearch(mTextSearch);
				/* reset id adapter */
				currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
				itemAdapter.setIdItemList(mIdListItems, currentFolderItem, currentFolderId);
				btnClearTextSearch.setVisibility(View.GONE);

				/* reset folder adapter */
				isSearchMode = true;
				folderListViewAdapter.updateSearchMode(isSearchMode);
				folderListView.setSelection(0);
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
		// if (!"".equals(textSearch)) {
		// List<ElementID> idListDb = mIDxPWDataBaseHandler.getAllElmentIds();
		// ArrayList<ElementID> searchItems = new ArrayList<ElementID>();
		// int size = idListDb.size();
		// for (int i = 0; i < size; i++) {
		// String idName = idListDb.get(i).geteTitle().toString().toUpperCase();
		// String idNote = idListDb.get(i).geteNote().toString().toUpperCase();
		// boolean isFoundId = idName.indexOf(textSearch.toUpperCase()) != -1;
		// boolean isFoundNote = idNote.indexOf(textSearch.toUpperCase()) != -1;
		//
		// if (isFoundId || isFoundNote) {
		// OneItem item = new OneItem(idListDb.get(i).getPassWordId(),
		// idListDb.get(i)
		// .getIcon(), idListDb.get(i).getTitleRecord(),
		// idListDb.get(i).getUrl());
		// searchItems.add(item);
		// }
		// }
		// Log.e("textSearch " + mTextSearch, "size " + searchItems.size());
		// return searchItems;
		// } else
		// return null;
		return null;
	}

	/**
	 * set mode edit for folder and id list view
	 */
	private void setEditModeForFolderAndIdListView(boolean isEdit) {
		/* set mode edit for folder */
		folderListViewAdapter.updateModeForListView(isEdit);

		/* set mode edit for id */
		itemAdapter.updateModeForListView(isEdit);
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
					+ mFolderListItems.get(mCurrentFolderPosition).getgName() + "?");
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
			builder.setTitle(getResources().getString(R.string.title_add_folder));
			// builder.setMessage("Type the name of new folder:");
			builder.setIcon(R.drawable.icon);
			// Use an EditText view to get user input.
			final EditText input = new EditText(this);
			input.setId(Contants.TEXT_ID);
			input.setText("");
			builder.setView(input);

			builder.setPositiveButton(getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							String folderName = input.getText().toString();
							/* add new folder to database */
							addNewFolderToDatabase(folderName);
							return;
						}

					});

			builder.setNegativeButton(getResources().getString(R.string.confirm_cancel),
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

			builder.setPositiveButton(getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							deleteFolder(positionReturnedByHandler);
							return;
						}

					});

			builder.setNegativeButton(getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_EDIT_FOLDER:
			builder.setTitle(getResources().getString(R.string.title_change_folder));
			// builder.setMessage("Type the name of folder to edit :");
			builder.setIcon(R.drawable.icon);

			// Use an EditText view to get user input.
			final EditText inputEdit = new EditText(this);
			inputEdit.setId(Contants.TEXT_ID);
			builder.setView(inputEdit);
			inputEdit.setText("");
			builder.setPositiveButton(getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							String folderName = inputEdit.getText().toString();
							/* edit folder to database */
							editFolderToDatabase(folderName, positionReturnedByHandler);
							return;
						}

					});

			builder.setNegativeButton(getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			return builder.create();

		case Contants.DIALOG_DELETE_ID:
			builder.setTitle(getResources().getString(R.string.title_delete_items));
			// builder.setMessage("Do you want to delete this ID?");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(getResources().getString(R.string.confirm_delete),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							deleteID(positionReturnedByHandler);
							return;
						}
					});

			builder.setNegativeButton(getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_CREATE_ID:
			builder.setTitle(getResources().getString(R.string.title_add_items));
			builder.setMessage(getResources().getString(R.string.message_add_item));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							startIntentCreateNewIds();
							return;
						}
					});

			return builder.create();
		case Contants.DIALOG_EXIT:
			builder.setTitle("IDManager");
			builder.setMessage("Are you sure to want to exit?");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							finish();
							return;
						}
					});

			builder.setNegativeButton(getResources().getString(R.string.confirm_cancel),
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
					+ mFolderListItems.get(mCurrentFolderPosition).getgName() + " folder?");
			builderMoveId.setIcon(R.drawable.icon);
			builderMoveId.setPositiveButton(getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							int passwordId = mCurrentId;
							int folderId = mCurrentFolderPosition;
							mCurrentFirstVisibleFolderItem = 0;
							moveIdToFolder(passwordId, folderId);
							return;
						}
					});

			builderMoveId.setNegativeButton(getResources().getString(R.string.confirm_cancel),
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

	private void moveIdToFolder(int passwordId, int currentFolderPosition) {
		/* update to database */
		// IDDataBase id = mDataBaseHandler.getId(passwordId);
		// id.setFolderId(currentFolderPosition);
		// mDataBaseHandler.updateId(id);
		// /* refresh id listview */
		// Log.e("password ", "pww id " + passwordId);
		// for (int i = 0; i < mIdListItems.size(); i++) {
		// if (mIdListItems.get(i).gete() == passwordId)
		// mIdListItems.remove(i);
		// }
		// OneItem item = mIdListItems.get(passwordId);
		// mIdListItems.remove(item);
		currentFolderId = mFolderListItems.get(currentFolderPosition).getgId();
		itemAdapter.setIdItemList(mIdListItems, currentFolderPosition, currentFolderId);
	}

	private void startIntentCreateNewIds() {
		Log.e("mIdListItem position " + currentFolderItem,
				"folder Id" + mFolderListItems.get(currentFolderItem).getgId());
		int currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		Intent newIdIntent = new Intent(HomeScreeenActivity.this, EditIdPasswordActivity.class);
		newIdIntent.putExtra(Contants.IS_INTENT_CREATE_NEW_ID, true);
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
		// mIDxPWDataBaseHandler.deleteIDPassword(mIdListItems.get(positionReturnedByHandler)
		// .getPasswordId());
		mIDxPWDataBaseHandler.deleteElementId(mIdListItems.get(positionReturnedByHandler).geteId());
		/* reset id list view */
		mIdListItems.remove(positionReturnedByHandler);
		currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		itemAdapter.setIdItemList(mIdListItems, currentFolderItem, currentFolderId);
	}

	/**
	 * edit folder to database
	 * 
	 * @param folderName
	 * @param positionReturnedByHandler
	 */

	private void editFolderToDatabase(String folderName, int positionReturnedByHandler) {
		// TODO Auto-generated method stub
		GroupFolder folderItem = mFolderListItems.get(positionReturnedByHandler);
		GroupFolder folder = new GroupFolder(folderItem.getgId(), folderName,
				folderItem.getgType(), folderItem.getgUserId(), folderItem.getgOrder());
		mIDxPWDataBaseHandler.updateFolder(folder);

		/* reset folder list */
		mFolderListItems.get(positionReturnedByHandler).setgName(folderName);
		folderListViewAdapter.updateFolderList(mFolderListItems);
		showToast("Folder " + folderName + " is updated");
	}

	/**
	 * delete folder
	 * 
	 * @param positionReturnedByHandler
	 */
	private void deleteFolder(int positionReturnedByHandler) {
		// TODO Auto-generated method stub
		/* delete folder in database */
		mIDxPWDataBaseHandler
				.deleteFolder(mFolderListItems.get(positionReturnedByHandler).getgId());
		/* delete all ids in this folder */
		mIDxPWDataBaseHandler.deleteElementId(positionReturnedByHandler);
		/* refresh id list view */
		mIdListItems = constructList(positionReturnedByHandler);
		currentFolderId = mFolderListItems.get(positionReturnedByHandler).getgId();
		itemAdapter.setIdItemList(mIdListItems, positionReturnedByHandler, 0);
		/* refresh folder listview */
		folderListViewAdapter.removeItem(positionReturnedByHandler);
		Log.e("size of folder", "size of folder " + mFolderListItems.size());
	}

	/**
	 * add new folder to database
	 */
	private void addNewFolderToDatabase(String folderName) {

		List<GroupFolder> folderList = mIDxPWDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();
		int imgFolderIconId = R.drawable.btn_edit;
		int imgFolderId = R.drawable.folder_common;
		// mIDxPWDataBaseHandler.addNewFolder(new GroupFolder(sizeOfFolder, 1,
		// folderName, imgFolderId, imgFolderIconId,
		// Contants.TYPE_FOLDER_NORMAL));
		mIDxPWDataBaseHandler.addNewFolder(new GroupFolder(sizeOfFolder, folderName, 0,
				Contants.MASTER_PASSWORD_ID, 0));

		// /* refresh listview folder */
		// FolderItem folder = new FolderItem(sizeOfFolder, imgFolderId,
		// imgFolderIconId, folderName, Contants.TYPE_FOLDER_NORMAL);
		// folderListViewAdapter.addNewFolder(folder);
		GroupFolder folder = new GroupFolder(sizeOfFolder, folderName, 0,
				Contants.MASTER_PASSWORD_ID, 0);
		folderListViewAdapter.addNewFolder(folder);
		Log.e("size of folder", "size of folder " + mFolderListItems.size());
		folderListView.invalidate();
	}

	public void onResume() {
		super.onResume();
		// if (null != mIDxPWDataBaseHandler)
		// mIDxPWDataBaseHandler.close();
		// mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
		// /* reset folder adapter */
		// mFolderListItems.clear();
		// // get data from folder database
		// loadDataFromFolderDataBase();
		//
		// /* reset adapter */
		// currentFolderId = mFolderListItems.get(currentFolderItem).getgId();
		// mIdListItems = constructList(currentFolderId);
		// itemAdapter.setIdItemList(mIdListItems, currentFolderItem,
		// currentFolderId);
	}

	private void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
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
		Log.d("onUserInteraction", "User interaction to " + this.toString());
	}

}
