package visvateam.outsource.idmanager.activities.homescreen;

import java.util.ArrayList;
import java.util.List;

import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.activities.SettingActivity;
import visvateam.outsource.idmanager.database.FolderDataBaseHandler;
import visvateam.outsource.idmanager.database.FolderDatabase;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import exp.mtparet.dragdrop.adapter.FolderListViewAdapter;
import exp.mtparet.dragdrop.adapter.ItemAdapter;
import exp.mtparet.dragdrop.data.OneItem;
import exp.mtparet.dragdrop.view.ListViewDragDrop;

public class HomeScreeenActivity extends Activity implements OnClickListener {
	// ==========================Control define ====================
	private LinearLayout mainRelativeLayout;
	private ListViewDragDrop lvPicture;
	private ListView folderListView;
	private RelativeLayout layoutDrag;
	private ImageView imageDrag;

	private Button btnSetting;
	private Button btnAddNewFolder;
	private Button btnAddNewId;
	private Button btnEdit;
	private Button btnSync;

	private EditText editTextSearch;

	// ===========================Class Define =====================
	private ItemAdapter itemAdapter;
	private FolderListViewAdapter folderListViewAdapter;
	private OneItem oneItemSelected;
	private FolderDataBaseHandler folderDataBaseHandler;

	// ============================Variable Define ==================
	private static final int DIALOG_ADD_NEW_FOLDER = 0;
	private static final int NUMBER_FOLDER_DEFALT = 2;
	private static final String NAME_HISTORY_FOLDER = "history";
	private static final String NAME_FAVOURITE_FOLDER = "favourite";
	private static final int TEXT_ID = 0;
	private Context context;
	private int[] imgFolderId;
	private int[] imgFolderIconId;
	private int[] imgFolderType;
	private boolean isEdit = false;

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

		/* init listview */
		lvPicture = (ListViewDragDrop) mainRelativeLayout
				.findViewById(R.id.list_view_item);
		folderListView = (ListView) mainRelativeLayout
				.findViewById(R.id.list_view_folder);

		/**/
		imageDrag = (ImageView) mainRelativeLayout
				.findViewById(R.id.imageView1);
		layoutDrag = (RelativeLayout) mainRelativeLayout
				.findViewById(R.id.layoutDrag);

		/* init adapter for listview */
		itemAdapter = new ItemAdapter(context, constructList());
		folderListViewAdapter = new FolderListViewAdapter(this, imgFolderId,
				imgFolderIconId, imgFolderType, false);
		lvPicture.setAdapter(itemAdapter);

		// lvRecever.setAdapter(receverAdapter);
		folderListView.setAdapter(folderListViewAdapter);

		/**
		 * Set selected Listener to know what item must be drag
		 */
		lvPicture.setOnItemSelectedListener(mOnItemSelectedListener);

		/**
		 * Set an touch listener to know what is the position when item are move
		 * out of the listview
		 */
		lvPicture.setOnItemMoveListener(mOnItemMoveListener);

		/**
		 * Listener to know if the item is droped out of this origin ListView
		 */
		lvPicture.setOnItemUpOutListener(mOnItemUpOutListener);
		folderListView.setOnItemClickListener(listenerFolderListView);

		/* init button */
		btnAddNewFolder = (Button) mainRelativeLayout
				.findViewById(R.id.btn_add_new_folder);
		btnAddNewFolder.setOnClickListener(this);

		btnAddNewId = (Button) mainRelativeLayout
				.findViewById(R.id.btn_add_new_id);
		btnAddNewId.setOnClickListener(this);

		btnEdit = (Button) mainRelativeLayout.findViewById(R.id.btn_edit);
		btnEdit.setOnClickListener(this);

		btnSetting = (Button) mainRelativeLayout.findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(this);

		btnSync = (Button) mainRelativeLayout.findViewById(R.id.btn_sync);
		btnSync.setOnClickListener(this);

		/* init editText */
		editTextSearch = (EditText) mainRelativeLayout
				.findViewById(R.id.editText_search);

		/* set contentView for home screen layout */
		setContentView(mainRelativeLayout);

	}

	/**
	 * initialize database
	 */
	private void initDataBase() {
		folderDataBaseHandler = new FolderDataBaseHandler(this);
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
		Log.e("size", "size " + sizeOfFolder);
		imgFolderIconId = new int[sizeOfFolder];
		imgFolderId = new int[sizeOfFolder];
		imgFolderType = new int[sizeOfFolder];
		for (int i = 0; i < sizeOfFolder; i++) {
			imgFolderIconId[i] = folderList.get(i).getImgFolderIconId();
			imgFolderId[i] = folderList.get(i).getImgFolderId();
			imgFolderType[i] = folderList.get(i).getTypeOfFolder();
			Log.e("adskjfhskdjh", "asdfjh " + imgFolderType[i]);
		}
	}

	/**
	 * check size folder database to add folder favourite and history
	 */
	private void checkSizeFolderDataBase() {
		List<FolderDatabase> folderList = folderDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();
		if (sizeOfFolder < NUMBER_FOLDER_DEFALT) {
			/* add favourite table to folder db */
			FolderDatabase folderFavourite = new FolderDatabase(0, 1,
					NAME_FAVOURITE_FOLDER, R.drawable.folder_s_common,
					R.drawable.favorite, 0);
			folderDataBaseHandler.addNewFolder(folderFavourite);

			/* add history table to folder db */
			FolderDatabase folderHistory = new FolderDatabase(1, 1,
					NAME_HISTORY_FOLDER, R.drawable.folder_s_common,
					R.drawable.history, 0);
			folderDataBaseHandler.addNewFolder(folderHistory);
		}
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
			oneItemSelected = (OneItem) arg0.getItemAtPosition(arg2);
			imageDrag.setImageDrawable(context.getResources().getDrawable(
					oneItemSelected.getId()));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	};

	private OnTouchListener mOnItemUpOutListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return true;
		}
	};

	// private OnItemClickListener listenerReceivePicture = new
	// OnItemClickListener() {
	//
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long
	// arg3) {
	// if (oneItemSelected != null)
	// receverAdapter.addPicture(oneItemSelected, arg2);
	// }
	//
	// };
	private OnItemClickListener listenerFolderListView = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// if (oneItemSelected != null)
			// receverAdapter.addPicture(oneItemSelected, arg2);
		}

	};

	// private OnItemLongClickListener listenerRemoveItem = new
	// OnItemLongClickListener() {
	//
	// @Override
	// public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// receverAdapter.removeItem(arg2);
	// return false;
	// }
	//
	// };

	private OnTouchListener mOnItemMoveListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) imageDrag
					.getLayoutParams();
			imageDrag.setVisibility(ImageView.VISIBLE);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				imageDrag.bringToFront();
				return true;
			}

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				layout.leftMargin = (int) event.getX() + imageDrag.getWidth()
						/ 2;
				layout.topMargin = (int) event.getY() - imageDrag.getHeight()
						/ 2;
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				imageDrag.setVisibility(View.GONE);
			}
			// set params
			imageDrag.setLayoutParams(layout);

			return true;
		}

	};

	private ArrayList<OneItem> constructList() {
		ArrayList<OneItem> al = new ArrayList<OneItem>();

		OneItem op = new OneItem(R.drawable.unionpay2, "unionpay2");
		al.add(op);

		OneItem op2 = new OneItem(R.drawable.bank_of_china, "bank_of_china");
		al.add(op2);

		OneItem op3 = new OneItem(R.drawable.bank_of_shanghai,
				"bank_of_shanghai");
		al.add(op3);

		OneItem op4 = new OneItem(
				R.drawable.china_construction_bank_corporation,
				"china_construction_bank_corporation");
		al.add(op4);

		OneItem op5 = new OneItem(R.drawable.agricultural_bank_of_china,
				"agricultural_bank_of_china");
		al.add(op5);

		OneItem op6 = new OneItem(
				R.drawable.china_construction_bank_corporation,
				"china_construction_bank_corporation");
		al.add(op6);

		OneItem op7 = new OneItem(R.drawable.bank_of_shanghai,
				"bank_of_shanghai");
		al.add(op7);
		return al;
	}

	@Override
	public void onClick(View v) {
		/* add new folder */
		if (v == btnAddNewFolder) {
			addNewFolder();
		}

		/* add new id */
		else if (v == btnAddNewId) {

		}

		/* edit listview */
		else if (v == btnEdit) {
			if (isEdit) {
				btnEdit.setBackgroundResource(R.drawable.edit);
				isEdit = false;

				/* set folder and id listview in normal mode */
				setNormalModeForFolderAndIdListView();
			} else {
				btnEdit.setBackgroundResource(R.drawable.return_back);
				isEdit = true;

				/* set folder and id listview in edit mode */
				setEditModeForFolderAndIdListView();
			}
		}

		/* setting */
		else if (v == btnSetting) {
			Intent intentSeting = new Intent(HomeScreeenActivity.this,
					SettingActivity.class);
			startActivity(intentSeting);
		}

		/* sync data to cloud */
		else if (v == btnSync) {

		}
	}

	/**
	 * set mode normal for folder and id list view
	 */
	private void setNormalModeForFolderAndIdListView() {
		/* set mode edit for folder */
		folderListViewAdapter = new FolderListViewAdapter(this, imgFolderId,
				imgFolderIconId, imgFolderType, false);
		folderListView.setAdapter(folderListViewAdapter);
		folderListViewAdapter.notifyDataSetChanged();
		folderListView.invalidate();

	}

	/**
	 * set mode edit for folder and id list view
	 */
	private void setEditModeForFolderAndIdListView() {
		/* set mode edit for folder */
		folderListViewAdapter = new FolderListViewAdapter(this, imgFolderId,
				imgFolderIconId, imgFolderType, true);
		folderListView.setAdapter(folderListViewAdapter);
		folderListViewAdapter.notifyDataSetChanged();
		folderListView.invalidate();
	}

	/**
	 * Called to create a dialog to be shown.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DIALOG_ADD_NEW_FOLDER:
			return createExampleDialog();
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
		case DIALOG_ADD_NEW_FOLDER:
			// Clear the input box.
			EditText text = (EditText) dialog.findViewById(TEXT_ID);
			text.setText("");
			break;
		}
	}

	/**
	 * add new folder to group
	 */
	private void addNewFolder() {
		showDialog(DIALOG_ADD_NEW_FOLDER);
	}

	/**
	 * Create and return an example alert dialog with an edit text box.
	 */
	private Dialog createExampleDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add New Folder");
		builder.setMessage("Type the name of new folder:");
		builder.setIcon(R.drawable.icon);

		// Use an EditText view to get user input.
		final EditText input = new EditText(this);
		input.setId(TEXT_ID);
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

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});

		return builder.create();
	}

	/**
	 * add new folder to database
	 */
	private void addNewFolderToDatabase(String folderName) {

		List<FolderDatabase> folderList = folderDataBaseHandler.getAllFolders();
		int sizeOfFolder = folderList.size();
		sizeOfFolder++;
		int imgFolderIconId = R.drawable.jog_note_push;
		int imgFolderId = R.drawable.folder_common;
		folderDataBaseHandler.addNewFolder(new FolderDatabase(sizeOfFolder, 1,
				folderName, imgFolderId, imgFolderIconId, 1));

		/* refresh listview folder */
		refreshFolderListView();
	}

	public void onResume() {
		super.onResume();

		/* refresh listview id */
		refreshIdListView();
	}

	/**
	 * refresh id listview after change
	 */
	private void refreshIdListView() {
		// TODO Auto-generated method stub

	}

	/**
	 * refresh folder list view after change
	 */
	private void refreshFolderListView() {

		/* load data from folder database */
		loadDataFromFolderDataBase();

		/* refresh list view */
		folderListViewAdapter = new FolderListViewAdapter(this, imgFolderId,
				imgFolderIconId, imgFolderType, false);
		folderListView.setAdapter(folderListViewAdapter);
		folderListViewAdapter.notifyDataSetChanged();
		folderListView.invalidate();
	}
}
