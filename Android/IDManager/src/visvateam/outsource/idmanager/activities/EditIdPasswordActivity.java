package visvateam.outsource.idmanager.activities;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class EditIdPasswordActivity extends Activity implements OnItemClickListener,
		android.content.DialogInterface.OnClickListener {
	// =========================Control Define ==================
	private ListView mListView;
	private CheckBox mCheckBoxLike;

	private EditText mEditTextUrlId;
	private EditText mEditTextNameId;
	private EditText mEditTextNote;

	private Button mBtnImageMemo;
	// =========================Class Define ====================
	private DataBaseHandler mDataBaseHandler;
	private ArrayList<Item> mItems;
	private MultiDirectionSlidingDrawer mSlidingDrawer;
	// =========================Variable Define =================
	private String nameItem[] = { "ID1", "Pass1", "ID2", "Pass2", "ID3", "Pass3", "ID4", "Pass4",
			"ID5", "Pass5", "ID6", "Pass6", "ID7", "Pass7", "ID8", "Pass8", "ID9", "Pass9", "ID10",
			"Pass10", "ID11", "Pass11", "ID12", "Pass12" };
	private boolean isCreateNewId;

	// id password info
	private int currentFolderId;
	private int currentPasswordId;

	private int numberOfId;
	private String titleRecord;
	private String icon;
	private String favouriteGroup;
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
	public ArrayList<ViewHolder> viewHolder = new ArrayList<EditIdPasswordActivity.ViewHolder>();
	public int itemSelect = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_id_pass);

		isCreateNewId = getIntent().getExtras().getBoolean(Contants.IS_INTENT_CREATE_NEW_ID);
		currentFolderId = getIntent().getExtras().getInt(Contants.CURRENT_FOLDER_ID);
		if (!isCreateNewId)
			currentPasswordId = getIntent().getExtras().getInt(Contants.CURRENT_PASSWORD_ID);
		else
			currentPasswordId = -1;

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
		((ImageButton) findViewById(R.id.img_avatar)).setBackgroundDrawable(mDrawableIcon);
		((EditText) findViewById(R.id.edit_text_url)).setText(mUrlItem);

		if (itemSelect >= 0) {
			mItems.get(itemSelect).mContentItem = mStringOfSelectItem;
			mListView.setAdapter(new ItemAddAdapter(this, mItems));
			itemSelect = -1;
		}
	}

	/**
	 * initialize database
	 */
	private void initDataBase() {
		// TODO Auto-generated method stub
		mDataBaseHandler = new DataBaseHandler(this);
		List<IDDataBase> idList = mDataBaseHandler.getAllIDs();
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
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub

			}
		});

		mBtnImageMemo = (Button) findViewById(R.id.btn_img_memo);
		mListView = (ListView) findViewById(R.id.id_listview_item_add);
		/* load data for list item id */
		mItems = loadDataForListItem();
		mListView.setAdapter(new ItemAddAdapter(this, mItems));

		mSlidingDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		if (isCreateNewId)
			mSlidingDrawer.close();
		else
			mSlidingDrawer.open();
	}

	/**
	 * load data for list item
	 * 
	 * @return
	 */
	private ArrayList<Item> loadDataForListItem() {
		ArrayList<Item> itemList = new ArrayList<EditIdPasswordActivity.Item>();

		/* is create new id */
		if (isCreateNewId) {
			for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
				Item item = new Item();
				item.mNameItem = nameItem[i];
				item.mContentItem = "";
				itemList.add(item);
			}

			/* is edit id */
		} else {
			if (currentPasswordId == -1) {
				showToast("Edit Id error");
				finish();
			}
			IDDataBase id = mDataBaseHandler.getId(currentPasswordId);

			mEditTextNameId.setText(id.getTitleRecord());
			mEditTextNote.setText(id.getNote());
			mEditTextUrlId.setText(id.getUrl());
			// load info from database
			Item item1 = new Item();
			item1.mNameItem = id.getTitleId1();
			item1.mContentItem = id.getDataId1();
			itemList.add(item1);

			Item item2 = new Item();
			item2.mNameItem = id.getTitleId2();
			item2.mContentItem = id.getDataId2();
			itemList.add(item2);

			Item item3 = new Item();
			item3.mNameItem = id.getTitleId3();
			item3.mContentItem = id.getDataId3();
			itemList.add(item3);

			Item item4 = new Item();
			item4.mNameItem = id.getTitleId4();
			item4.mContentItem = id.getDataId4();
			itemList.add(item4);

			Item item5 = new Item();
			item5.mNameItem = id.getTitleId5();
			item5.mContentItem = id.getDataId5();
			itemList.add(item5);

			Item item6 = new Item();
			item6.mNameItem = id.getTitleId6();
			item6.mContentItem = id.getDataId6();
			itemList.add(item6);

			Item item7 = new Item();
			item7.mNameItem = id.getTitleId7();
			item7.mContentItem = id.getDataId7();
			itemList.add(item7);

			Item item8 = new Item();
			item8.mNameItem = id.getTitleId8();
			item8.mContentItem = id.getDataId8();
			itemList.add(item8);

			Item item9 = new Item();
			item9.mNameItem = id.getTitleId9();
			item9.mContentItem = id.getDataId9();
			itemList.add(item9);

			Item item10 = new Item();
			item10.mNameItem = id.getTitleId10();
			item10.mContentItem = id.getDataId10();
			itemList.add(item10);

			Item item11 = new Item();
			item11.mNameItem = id.getTitleId11();
			item11.mContentItem = id.getDataId11();
			itemList.add(item11);

			Item item12 = new Item();
			item12.mNameItem = id.getTitleId12();
			item12.mContentItem = id.getDataId12();
			itemList.add(item12);
		}
		return itemList;
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
		Intent intentMemo = new Intent(EditIdPasswordActivity.this, ImageMemoActivity.class);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			final int pos = position;
			LayoutInflater inflater = mActivity.getLayoutInflater();
			// if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_id_pass_add, null);
			holder = new ViewHolder();
			holder.nameItem = (EditText) convertView.findViewById(R.id.id_txt_nameItem);
			holder.nameItem.setText(mItems.get(position).mNameItem);
			holder.nameItem.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					EditIdPasswordActivity.this.mItems.get(position).mNameItem = s.toString();
				}
			});
			holder.contentItem = (EditText) convertView.findViewById(R.id.id_txt_detailItem);
			holder.contentItem.setText(mItems.get(position).mContentItem);
			holder.contentItem.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					EditIdPasswordActivity.this.mItems.get(position).mContentItem = s.toString();
				}
			});
			((ImageButton) convertView.findViewById(R.id.id_btn_generator))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							onToGenerator(pos);
						}
					});
			viewHolder.add(holder);
			// convertView.setTag(holder);
			// } else {
			// holder = (ViewHolder) convertView.getTag();
			// }
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

			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					// if (isCreateNewId)
					// createNewId();
					// else
					// updateId();
					createOrUpdateId();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

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
	private void createOrUpdateId() {
		// TODO Auto-generated method stub
		if ("".equals(mEditTextNameId.getText().toString()))
			showToast("Type the name of this Id before creating");
		else if ("".equals(mEditTextUrlId.getText().toString()))
			showToast("Type the link url of this Id before creating");
		else if (!checkValidataUrl(mEditTextUrlId.getText().toString()))
			showToast("Your url is not validate");
		else {
			/* start create new Id */

			addNewIdValuesToDataBase();
		}
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
		int passWordId = -1;
		if (isCreateNewId)
			passWordId = numberOfId;
		else
			passWordId = currentPasswordId;
		/* add new id */
		IDDataBase id = new IDDataBase(passWordId, currentFolderId, titleRecord, icon,
				favouriteGroup, mItems.get(0).mNameItem, mItems.get(0).mContentItem,
				mItems.get(1).mNameItem, mItems.get(1).mContentItem, mItems.get(2).mNameItem,
				mItems.get(2).mContentItem, mItems.get(3).mNameItem, mItems.get(3).mContentItem,
				mItems.get(4).mNameItem, mItems.get(4).mContentItem, mItems.get(5).mNameItem,
				mItems.get(5).mContentItem, mItems.get(6).mNameItem, mItems.get(6).mContentItem,
				mItems.get(7).mNameItem, mItems.get(7).mContentItem, mItems.get(8).mNameItem,
				mItems.get(8).mContentItem, mItems.get(9).mNameItem, mItems.get(9).mContentItem,
				mItems.get(10).mNameItem, mItems.get(10).mContentItem, mItems.get(11).mNameItem,
				mItems.get(11).mContentItem, url, note, imageMemo, flag, timeStamp, isEncrypted,
				userId);

		if (isCreateNewId)
			mDataBaseHandler.addNewID(id);
		else
			mDataBaseHandler.updateId(id);
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
				Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		switch (requestCode) {
		case Contants.INTENT_IMG_MEMO:
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getExtras().getString(Contants.FIlE_PATH_IMG_MEMO);
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
