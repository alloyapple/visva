package visvateam.outsource.idmanager.activities.homescreen;

import java.util.ArrayList;
import java.util.List;

import visvateam.outsource.idmanager.activities.BrowserActivity;
import visvateam.outsource.idmanager.activities.CopyItemActivity;
import visvateam.outsource.idmanager.activities.EditIdPasswordActivity;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.activities.SettingActivity;
import visvateam.outsource.idmanager.activities.synccloud.SyncCloudActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.FolderDataBaseHandler;
import visvateam.outsource.idmanager.database.FolderDatabase;
import visvateam.outsource.idmanager.database.IDDataBase;
import visvateam.outsource.idmanager.database.IDDataBaseHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import exp.mtparet.dragdrop.adapter.FolderListViewAdapter;
import exp.mtparet.dragdrop.adapter.ItemAdapter;
import exp.mtparet.dragdrop.data.FolderItem;
import exp.mtparet.dragdrop.data.OneItem;
import exp.mtparet.dragdrop.view.ListViewDragDrop;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class HomeScreeenActivity extends Activity implements OnClickListener {
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
	private FolderDataBaseHandler folderDataBaseHandler;
	private IDDataBaseHandler mIDataBaseHandler;
	private ArrayList<FolderItem> mFolderListItems = new ArrayList<FolderItem>();
	private ArrayList<OneItem> mIdListItems = new ArrayList<OneItem>();

	// ============================Variable Define ==================

	private Context context;
	private boolean isEdit = false;
	private int positionReturnedByHandler;
	private int currentFolderItem = 0;

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
		mIdListItems = constructList(currentFolderItem);
		itemAdapter = new ItemAdapter(context, mIdListItems, false, mMainHandler, idListView,
				currentFolderItem);
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
				mMainHandler, folderListView, currentFolderItem);
		folderListView.setAdapter(folderListViewAdapter);

		/**
		 * listener to click item folder
		 */
		folderListView.setOnItemClickListener(listenerClickFolderItem);

		/**
		 * Listener to know on what position the new item must be insert
		 */
		folderListView.setOnItemReceiverListener(listenerReceivePicture);
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
		folderDataBaseHandler = new FolderDataBaseHandler(this);
		mIDataBaseHandler = new IDDataBaseHandler(this);
		// check size of folder database
		checkSizeFolderDataBase();
		// get data from folder database
		loadDataFromFolderDataBase();
	}

	/**
	 * load data from folder database to display on listview group
	 */
	private void loadDataFromFolderDataBase() {
		List<FolderDatabase> folderList = folderDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();

		for (int i = Contants.NUMBER_FOLDER_DEFALT; i < sizeOfFolder; i++) {
			FolderItem folder = new FolderItem(folderList.get(i).getFolderId(), folderList.get(i)
					.getImgFolderId(), folderList.get(i).getImgFolderIconId(), folderList.get(i)
					.getFolderName(), folderList.get(i).getTypeOfFolder());
			mFolderListItems.add(0, folder);
		}
	}

	/**
	 * check size folder database to add folder favourite and history
	 */
	private void checkSizeFolderDataBase() {
		List<FolderDatabase> folderList = folderDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();
		if (sizeOfFolder < Contants.NUMBER_FOLDER_DEFALT) {

			/* add history table to folder db */
			FolderDatabase folderHistory = new FolderDatabase(0, 1, Contants.NAME_HISTORY_FOLDER,
					R.drawable.folder_s_common, R.drawable.history, Contants.TYPE_FOLDER_NON_NORMAL);
			folderDataBaseHandler.addNewFolder(folderHistory);

			/* add favourite table to folder db */
			FolderDatabase folderFavourite = new FolderDatabase(1, 1,
					Contants.NAME_FAVOURITE_FOLDER, R.drawable.folder_s_common,
					R.drawable.favorite, Contants.TYPE_FOLDER_NON_NORMAL);
			folderDataBaseHandler.addNewFolder(folderFavourite);

		}

		// add 2 folder favourite and history

		FolderItem folderItemHistory = new FolderItem(0, R.drawable.folder_s_common,
				R.drawable.history, "", Contants.TYPE_FOLDER_NON_NORMAL);
		mFolderListItems.add(0, folderItemHistory);

		FolderItem folderItemFavourite = new FolderItem(1, R.drawable.folder_s_common,
				R.drawable.favorite, "", Contants.TYPE_FOLDER_NON_NORMAL);
		mFolderListItems.add(0, folderItemFavourite);

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
			imageDrag.setImageDrawable(context.getResources().getDrawable(
					oneItemSelected.getIconId()));
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
				CopyItemActivity.startActivity(HomeScreeenActivity.this);
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

			/* refresh folder list */
			folderListViewAdapter.setFolderSelected(currentFolderItem);

			/* reset adapter id */
			mIdListItems = constructList(currentFolderItem);
			itemAdapter.setIdItemList(mIdListItems, currentFolderItem);
		}
	};
	private OnItemClickListener listenerReceivePicture = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (oneItemSelected != null)
				// receverAdapter.addPicture(oneItemSelected, arg2);
				Log.e("ongetitemreceiver", "item name " + oneItemSelected.getName() + "position "
						+ mFolderListItems.get(arg2).getImgFolderType());
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

	private ArrayList<OneItem> constructList(int currentFolderItem) {

		List<IDDataBase> idList = mIDataBaseHandler.getAllIDsFromFolderId(currentFolderItem);
		ArrayList<OneItem> al = new ArrayList<OneItem>();
		int idListSize = idList.size();
		for (int i = 0; i < idListSize; i++) {
			Log.e("name", "name " + idList.get(i).getTitleRecord());
			OneItem item = new OneItem(idList.get(i).getPassWordId(), R.drawable.bank_of_china,
					idList.get(i).getTitleRecord(), idList.get(i).getUrl());
			al.add(item);
		}
		return al;
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
			showDialog(Contants.DIALOG_CREATE_ID);
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
			startActivity(intentBrowser);
		} else if (v == btnSearch) {
			if ("".equals(mEditTextSearch.getText().toString())) {
				showToast("Type to edit text to search");
			} else {
				showToast("Start search");
				String textSearch = mEditTextSearch.getText().toString();
				/* clear text */
				mEditTextSearch.setText("");

				mIdListItems = startSearch(textSearch);
				/* reset adapter */
				itemAdapter.setIdItemList(mIdListItems, currentFolderItem);
				btnClearTextSearch.setVisibility(View.GONE);
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
	private ArrayList<OneItem> startSearch(String textSearch) {
		List<IDDataBase> idListDb = mIDataBaseHandler.getAllIDs();
		ArrayList<OneItem> searchItems = new ArrayList<OneItem>();
		int size = idListDb.size();
		for (int i = 0; i < size; i++) {
			String idName = idListDb.get(i).getTitleRecord().toString().toUpperCase();
			String idNote = idListDb.get(i).getNote().toString().toUpperCase();
			boolean isFoundId = idName.indexOf(textSearch.toUpperCase()) != -1;
			boolean isFoundNote = idNote.indexOf(textSearch.toUpperCase()) != -1;

			if (isFoundId || isFoundNote) {
				OneItem item = new OneItem(idListDb.get(i).getPassWordId(),
						R.drawable.bank_of_china, idListDb.get(i).getTitleRecord(), idListDb.get(i)
								.getUrl());
				searchItems.add(item);
			}
		}
		return searchItems;
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
			builder.setTitle("Add New Folder");
			builder.setMessage("Type the name of new folder:");
			builder.setIcon(R.drawable.icon);

			// Use an EditText view to get user input.
			final EditText input = new EditText(this);
			input.setId(Contants.TEXT_ID);
			input.setText("");
			builder.setView(input);

			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					String folderName = input.getText().toString();
					/* add new folder to database */
					addNewFolderToDatabase(folderName);
					return;
				}

			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			return builder.create();

		case Contants.DIALOG_DELETE_FOLDER:
			builder.setTitle("Delete Folder");
			builder.setMessage("Do you want to delete this folder?");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					Log.e("ok delete", "delete");
					deleteFolder(positionReturnedByHandler);
					return;
				}

			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});

			return builder.create();
		case Contants.DIALOG_EDIT_FOLDER:
			builder.setTitle("Edit A Folder");
			builder.setMessage("Type the name of folder to edit :");
			builder.setIcon(R.drawable.icon);

			// Use an EditText view to get user input.
			final EditText inputEdit = new EditText(this);
			inputEdit.setId(Contants.TEXT_ID);
			builder.setView(inputEdit);
			inputEdit.setText("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					String folderName = inputEdit.getText().toString();
					/* edit folder to database */
					editFolderToDatabase(folderName, positionReturnedByHandler);
					return;
				}

			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			return builder.create();

		case Contants.DIALOG_DELETE_ID:
			builder.setTitle("Delete Folder");
			builder.setMessage("Do you want to delete this ID?");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					Log.e("ok delete", "delete");
					deleteID(positionReturnedByHandler);
					return;
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});

			return builder.create();
		case Contants.DIALOG_CREATE_ID:
			builder.setTitle("Create New Id");
			builder.setMessage("Users can register up to 12 free Ids.Type the key code to cancel the limit ids:");
			builder.setIcon(R.drawable.icon);

			// Use an EditText view to get user input.
			final EditText inputkeyCode = new EditText(this);
			inputkeyCode.setId(Contants.TEXT_ID);
			builder.setView(inputkeyCode);
			inputkeyCode.setText("");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					String folderName = inputkeyCode.getText().toString();
					startIntentCreateNewIds();
					return;
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startIntentCreateNewIds();
					return;
				}
			});
			return builder.create();

		default:
			return null;
		}
	}

	private void startIntentCreateNewIds() {
		Intent newIdIntent = new Intent(HomeScreeenActivity.this, EditIdPasswordActivity.class);
		newIdIntent.putExtra(Contants.IS_INTENT_CREATE_NEW_ID, true);
		newIdIntent.putExtra(Contants.CURRENT_FOLDER_ID, currentFolderItem);
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
		mIDataBaseHandler.deleteIDPassword(mIdListItems.get(positionReturnedByHandler)
				.getPasswordId());

		/* reset id list view */
		mIdListItems.remove(positionReturnedByHandler);
		itemAdapter.setIdItemList(mIdListItems, currentFolderItem);
	}

	/**
	 * edit folder to database
	 * 
	 * @param folderName
	 * @param positionReturnedByHandler
	 */

	private void editFolderToDatabase(String folderName, int positionReturnedByHandler) {
		// TODO Auto-generated method stub
		FolderItem folderItem = mFolderListItems.get(positionReturnedByHandler);
		FolderDatabase folder = new FolderDatabase(folderItem.getFolderId(), 1, folderName,
				folderItem.getFolderImgid(), folderItem.getFolderIconId(),
				Contants.TYPE_FOLDER_NORMAL);
		folderDataBaseHandler.updateFolder(folder);

		/* reset folder list */
		mFolderListItems.get(positionReturnedByHandler).setTextFolderName(folderName);
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
		folderDataBaseHandler.deleteFolder(mFolderListItems.get(positionReturnedByHandler)
				.getFolderId());
		/* delete all ids in this folder */
		mIDataBaseHandler.deleteIDPasswordFromFolderId(positionReturnedByHandler);
		/* refresh id list view */
		mIdListItems = constructList(positionReturnedByHandler);
		itemAdapter.setIdItemList(mIdListItems, positionReturnedByHandler);
		/* refresh folder listview */
		folderListViewAdapter.removeItem(positionReturnedByHandler);
	}

	/**
	 * add new folder to database
	 */
	private void addNewFolderToDatabase(String folderName) {

		List<FolderDatabase> folderList = folderDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();
		sizeOfFolder++;
		int imgFolderIconId = R.drawable.btn_edit;
		int imgFolderId = R.drawable.folder_common;
		folderDataBaseHandler.addNewFolder(new FolderDatabase(sizeOfFolder, 1, folderName,
				imgFolderId, imgFolderIconId, Contants.TYPE_FOLDER_NORMAL));

		// /* refresh listview folder */
		FolderItem folder = new FolderItem(sizeOfFolder, imgFolderId, imgFolderIconId, folderName,
				Contants.TYPE_FOLDER_NORMAL);
		folderListViewAdapter.addNewFolder(folder);
		folderListView.invalidate();

	}

	public void onResume() {
		super.onResume();
		/* reset adapter */
		mIdListItems = constructList(currentFolderItem);
		itemAdapter.setIdItemList(mIdListItems, currentFolderItem);
	}

	private void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
}
