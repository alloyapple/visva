package visvateam.outsource.idmanager.activities;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IDDataBase;
import visvateam.outsource.idmanager.database.IDDataBaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class EditIdPasswordActivity extends Activity implements
		OnItemClickListener, android.content.DialogInterface.OnClickListener {
	// =========================Control Define ==================
	private ListView mListView;
	private ListView listViewId;
	private CheckBox mCheckBoxLike;

	private EditText mEditTextUrlId;
	private EditText mEditTextNameId;
	private EditText mEditTextNote;

	private Button mBtnImageMemo;
	// =========================Class Define ====================
	private IDDataBaseHandler mIdDataBaseHandler;
	// =========================Variable Define =================

	private static int MAX_ITEM = 15;
	private String nameItem[] = { "ID1", "Pass1", "ID2", "Pass2", "ID3",
			"Pass3", "ID4", "Pass4", "ID5", "Pass5", "ID6", "Pass6", "ID7",
			"Pass7", "ID8", "Pass8", "ID9", "Pass9", "ID10", "Pass10", "ID11",
			"Pass11", "ID12", "Pass12", "ID13", "Pass13", "ID14", "Pass14",
			"ID15", "Pass15" };
	private boolean isCreateNewId;

	// id password info
	private int currentFolderId;
	private int numberOfId;
	private int passWordId;
	private String titleRecord;
	private String icon;
	private String favouriteGroup;
	private String titleId1;
	private String dataId1;
	private String titleId2;
	private String dataId2;
	private String titleId3;
	private String dataId3;
	private String titleId4;
	private String dataId4;
	private String titleId5;
	private String dataId5;
	private String titleId6;
	private String dataId6;
	private String titleId7;
	private String dataId7;
	private String titleId8;
	private String dataId8;
	private String titleId9;
	private String dataId9;
	private String titleId10;
	private String dataId10;
	private String titleId11;
	private String dataId11;
	private String titleId12;
	private String dataId12;
	private String url;
	private String note;
	private String imageMemo;
	private String flag;
	private String timeStamp;
	private boolean isEncrypted;
	private int userId;

	public static Drawable mDrawableIcon;
	public static String mUrlItem;
	public static String mStringOfSelectItem = "";
	public ArrayList<ViewHolder> viewHolder= new ArrayList<EditIdPasswordActivity.ViewHolder>();
	public int itemSelect = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_id_pass);

		isCreateNewId = getIntent().getExtras().getBoolean(
				Contants.IS_INTENT_CREATE_NEW_ID);
		currentFolderId = getIntent().getExtras().getInt(
				Contants.CURRENT_FOLDER_ID);
		Log.e("currentFOlder id", "currentFolderId " + currentFolderId);
		if (mDrawableIcon == null) {
			mDrawableIcon = getResources().getDrawable(R.drawable.default_icon);
		}
		if (mUrlItem == null) {
			mUrlItem = "http://google.com";
		}
		/* initialize database */
		initDataBase();

		/* initialize control */
		initControl();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((ImageButton) findViewById(R.id.img_avatar))
				.setBackgroundDrawable(mDrawableIcon);
		((EditText) findViewById(R.id.edit_text_url)).setText(mUrlItem);
		if (itemSelect >= 0) {
			mItems.get(itemSelect).mContentItem = mStringOfSelectItem;
			mListView.setAdapter(new ItemAddAdapter(this, mItems));
			itemSelect = -1;
		}
	}

	private ArrayList<Item> mItems;

	/**
	 * initialize database
	 */
	private void initDataBase() {
		// TODO Auto-generated method stub
		mIdDataBaseHandler = new IDDataBaseHandler(this);
		List<IDDataBase> idList = mIdDataBaseHandler.getAllIDs();
		numberOfId = idList.size();
	}

	/**
	 * initialize control
	 */
	private void initControl() {
		// TODO Auto-generated method stub
		mEditTextNameId = (EditText) findViewById(R.id.id_name_id_pass);
		mEditTextNote = (EditText) findViewById(R.id.edit_text_note);
		mEditTextUrlId = (EditText) findViewById(R.id.edit_text_url);

		mCheckBoxLike = (CheckBox) findViewById(R.id.id_like);
		mCheckBoxLike.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

			}
		});

		mBtnImageMemo = (Button) findViewById(R.id.btn_img_memo);
		mListView = (ListView) findViewById(R.id.id_listview_item_add);
		mItems = new ArrayList<EditIdPasswordActivity.Item>();
		for (int i = 0; i < MAX_ITEM * 2; i++) {
			Item item = new Item();
			item.mNameItem = nameItem[i];
			item.mContentItem = "";
			mItems.add(item);

		}
		mListView.setAdapter(new ItemAddAdapter(this, mItems));
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, EditIdPasswordActivity.class);
		activity.startActivity(i);
	}

	public void onImgAvatar(View v) {
		ListIconActivity.startActivity(this);

	}

	public void onToGenerator(int i) {
		itemSelect = i;
		PasswordGeneratorActivity.startActivity(this);

	}

	@SuppressWarnings("deprecation")
	public void onReturn(View v) {
		showDialog(Contants.DIALOG_CREATE_ID);
	}

	public void onInfo(View v) {
		BrowserActivity.startActivity(this);
	}

	public void onGoogleHome(View v) {
		SettingURLActivity.startActivity(this);
	}

	public void onMemoImage(View v) {
		// ImageMemoActivity.startActivity(this);
		Intent intentMemo = new Intent(EditIdPasswordActivity.this,
				ImageMemoActivity.class);
		startActivityForResult(intentMemo, Contants.INTENT_IMG_MEMO);
	}

	class ItemAddAdapter extends BaseAdapter {
		Activity mActivity;
		ArrayList<Item> mItems;

		public ItemAddAdapter(Activity pActivity, ArrayList<Item> pItems) {
			super();
			mActivity = pActivity;
			mItems = pItems;
			viewHolder.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			final int pos = position;
			LayoutInflater inflater = mActivity.getLayoutInflater();
//			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_id_pass_add, null);
				holder = new ViewHolder();
				holder.nameItem = (EditText) convertView
						.findViewById(R.id.id_txt_nameItem);
				holder.nameItem.setText(mItems.get(position).mNameItem);
				holder.contentItem = (EditText) convertView
						.findViewById(R.id.id_txt_detailItem);
				holder.contentItem.setText(mItems.get(position).mContentItem);
				((ImageButton) convertView.findViewById(R.id.id_btn_generator))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								onToGenerator(pos);
							}
						});
				viewHolder.add(holder);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
			return convertView;
		}

	}

	private class ViewHolder {
		EditText nameItem;
		EditText contentItem;

	}

	private class Item {
		public String mNameItem;
		public String mContentItem;
	}

	public void onAvatarClick(View v) {
		Log.e("avatar", "avatar");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case Contants.DIALOG_CREATE_ID:
			return createDialog(Contants.DIALOG_CREATE_ID);
		default:
			return null;
		}
	}

	private Dialog createDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case Contants.DIALOG_CREATE_ID:
			if (isCreateNewId) {
				builder.setTitle("Create New Id Password");
				builder.setMessage("Do you want to create new Id?");
			} else {
				builder.setTitle("Edit Id Password ");
				builder.setMessage("Are you sure to save ?");
			}
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (isCreateNewId)
								createNewId();
							else
								updateId();
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
			return builder.create();
		default:
			return null;
		}
	}

	/**
	 * create new id password
	 */
	private void createNewId() {
		// TODO Auto-generated method stub
		if ("".equals(mEditTextNameId.getText().toString()))
			showToast("Type the name of this Id before creating");
		else if ("".equals(mEditTextUrlId.getText().toString()))
			showToast("Type the link url of this Id before creating");
		else if (!checkValidataUrl(mEditTextUrlId.getText().toString()))
			showToast("Your url is not validate");
		else
			/* start create new Id */
			addNewIdValuesToDataBase();
	}

	private static boolean checkValidataUrl(String pUrl) {
		URL u = null;
		try {
			u = new URL(pUrl);
		} catch (MalformedURLException e) {
			return false;
		}
		try {
			u.toURI();
		} catch (URISyntaxException e) {
			return false;
		}
		return true;
	}

	/**
	 * update id password to database
	 */
	private void updateId() {
		// TODO Auto-generated method stub

	}

	/**
	 * add new id password to database
	 */
	private void addNewIdValuesToDataBase() {
		/* set values for id database */
		numberOfId++;
		Log.e("numer of id", "number of id " + numberOfId);
		titleRecord = mEditTextNameId.getText().toString();
		icon = getString(R.drawable.bank_of_shanghai);
		url = mEditTextUrlId.getText().toString();
		note = mEditTextNote.getText().toString();
		Log.e("currnentFolderId", "currentFolderId " + currentFolderId);

		IDDataBase id = new IDDataBase(numberOfId, currentFolderId,
				titleRecord, icon, favouriteGroup, titleId1, dataId1, titleId2,
				dataId2, titleId3, dataId3, titleId4, dataId4, titleId5,
				dataId5, titleId6, dataId6, titleId7, dataId7, titleId8,
				dataId8, titleId9, dataId9, titleId10, dataId10, titleId11,
				dataId11, titleId12, dataId12, url, note, imageMemo, flag,
				timeStamp, isEncrypted, userId);
		/* add new */
		mIdDataBaseHandler.addNewID(id);
		/* return home */
		finish();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case Contants.INTENT_IMG_MEMO:
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getExtras().getString(
						Contants.FIlE_PATH_IMG_MEMO);
				Log.e("file path", "file path " + filePath);
				if (!"".equals(filePath)) {
					imageMemo = filePath;
					mBtnImageMemo.setText("Image " + imageMemo);
				}
			}
		default:
			return;
		}
	}
}
