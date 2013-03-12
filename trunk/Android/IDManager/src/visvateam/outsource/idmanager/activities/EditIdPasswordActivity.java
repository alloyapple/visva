package visvateam.outsource.idmanager.activities;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;
import visvateam.outsource.idmanager.sercurity.CipherUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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

	public static String nameItem[] = { "ID1", "Pass1", "ID2", "Pass2", "ID3", "Pass3", "ID4",
			"Pass4", "ID5", "Pass5", "ID6", "Pass6", "ID7", "Pass7", "ID8", "Pass8", "ID9",
			"Pass9", "ID10", "Pass10", "ID11", "Pass11", "ID12", "Pass12" };
	private boolean isCreateNewId;

	// id password info
	private int currentFolderId;
	private int currentPasswordId;

	private boolean isLike;
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
	private static Drawable mDrawableIcon;
	private static boolean isUpdateIcon;
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

		/* initialize database */
		initDataBase();

		/* initialize control */
		initControl();
		if (!isCreateNewId) {
			isUpdateIcon = false;
			mDrawableIcon = getIconDatabase(icon);
			// mDrawableIcon =
			// getResources().getDrawable(R.drawable.default_icon);
		} else {
			isUpdateIcon = true;
			mDrawableIcon = getResources().getDrawable(R.drawable.default_icon);
		}
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

	@SuppressWarnings("deprecation")
	public static Drawable getIconDatabase(String icon) {
		File inputFile = new File(Environment.getExternalStorageDirectory() + "/" + icon);
		byte[] cipherBytes = new byte[(int) inputFile.length()];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(inputFile);
			fis.read(cipherBytes, 0, cipherBytes.length);
			fis.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] decryptBytes = null;
		try {
			decryptBytes = CipherUtil.decrypt(cipherBytes);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bmp = BitmapFactory.decodeByteArray(decryptBytes, 0, decryptBytes.length);
		return (Drawable) new BitmapDrawable(bmp);
	}

	@SuppressWarnings("deprecation")
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
		SQLiteDatabase.loadLibs(this);
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
				if (isChecked)
					isLike = true;
				else
					isLike = false;
				Log.e("isChecked", "isCheck " + isChecked);
			}
		});

		mBtnImageMemo = (Button) findViewById(R.id.btn_img_memo);
		mListView = (ListView) findViewById(R.id.id_listview_item_add);
		/* load data for list item id */
		mItems = loadDataForListItem();
		mListView.setAdapter(new ItemAddAdapter(this, mItems));

		mSlidingDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		// if (isCreateNewId)
		// mSlidingDrawer.close();
		// else
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
			for (int i = 0; i < Contants.MAX_ITEM_PASS_ID * 2; i++) {
				Item item = new Item();
				item.mNameItem = nameItem[i];
				item.mContentItem = "";
				itemList.add(item);
			}
			mUrlItem = "http://google.com";

			/* is edit id */
		} else {
			if (currentPasswordId == -1) {
				showToast("Edit Id error");
				finish();
			}
			IDDataBase id = mDataBaseHandler.getId(currentPasswordId);

			/* set values for item of edit idpw activity */
			mEditTextNameId.setText(id.getTitleRecord());
			mEditTextNote.setText(id.getNote());
			mUrlItem = id.getUrl();
			mEditTextUrlId.setText(id.getUrl());
			if (id.getLike() == 0)
				mCheckBoxLike.setChecked(false);
			else if (id.getLike() == 1)
				mCheckBoxLike.setChecked(true);

			// load info from database
			for (int i = 0; i < Contants.MAX_ITEM_PASS_ID * 2; i++) {
				Item item = new Item();
				item.mNameItem = nameItem[i];
				int temp;
				if (i % 2 == 0) {
					temp = (i+2) / 2;
					item.mContentItem = id.getTitleId(temp);
				} else {
					temp = (i + 1) / 2;
					item.mContentItem = id.getDataId(temp);
				}
				itemList.add(item);
			}
			// set icon for image icon
			icon = id.getIcon();
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
		Intent intentBrowser = new Intent(EditIdPasswordActivity.this, BrowserActivity.class);
		intentBrowser.putExtra(Contants.KEY_TO_BROWSER, Contants.INFO_TO);
		startActivity(intentBrowser);
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
				builder.setTitle(getResources().getString(R.string.title_creat_item));
				builder.setMessage("Do you want to create new Id?");
			} else {
				builder.setTitle("Edit Id Password ");
				builder.setMessage("Are you sure to save ?");
			}
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

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
			builder.setNegativeButton(getResources().getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
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

	@SuppressWarnings("resource")
	public String encyptAndSaveIcon(Drawable pDrawable, String icon) {
		String namString = String.valueOf(System.currentTimeMillis());
		String path = null;
		if (isCreateNewId)
			path = namString + ".dat";
		else
			path = icon;
		byte[] resultEncrypt = null;
		try {
			resultEncrypt = CipherUtil.encrypt(drawableToBitmap(pDrawable));
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidParameterSpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (resultEncrypt == null) {
			return null;
		}
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(
					Environment.getExternalStorageDirectory() + "/" + path);
			try {
				fileOutputStream.write(resultEncrypt, 0, resultEncrypt.length);
				return path;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		if (isUpdateIcon) {
			icon = encyptAndSaveIcon(mDrawableIcon, icon);
			if (icon != null)
				Log.i("<-------------Success-------->", icon);
		}
		url = mEditTextUrlId.getText().toString();
		note = mEditTextNote.getText().toString();
		int passWordId = -1;
		long currentTime = System.currentTimeMillis();
		timeStamp = String.valueOf(currentTime);
		if (isCreateNewId)
			passWordId = numberOfId;
		else
			passWordId = currentPasswordId;

		/* add new id */
		IDDataBase id = new IDDataBase(passWordId, currentFolderId, titleRecord, icon,
				favouriteGroup, mItems.get(0).mContentItem, mItems.get(1).mContentItem,
				mItems.get(2).mContentItem, mItems.get(3).mContentItem, mItems.get(4).mContentItem,
				mItems.get(5).mContentItem, mItems.get(6).mContentItem, mItems.get(7).mContentItem,
				mItems.get(8).mContentItem, mItems.get(9).mContentItem,
				mItems.get(10).mContentItem, mItems.get(11).mContentItem,
				mItems.get(12).mContentItem, mItems.get(13).mContentItem,
				mItems.get(14).mContentItem, mItems.get(15).mContentItem,
				mItems.get(16).mContentItem, mItems.get(17).mContentItem,
				mItems.get(18).mContentItem, mItems.get(19).mContentItem,
				mItems.get(20).mContentItem, mItems.get(21).mContentItem,
				mItems.get(22).mContentItem, mItems.get(23).mContentItem, url, note, imageMemo,
				flag, timeStamp, isEncrypted, userId, 0);

		/* if user click like, add id to folder history */
		if (isLike) {
			id.setLike(Contants.IS_FAVOURITE);
		} else
			id.setLike(Contants.NOT_FAVORITE);

		// create id int normal folder
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

	public static void updateIcon(Drawable pDrawable) {
		mDrawableIcon = pDrawable;
		isUpdateIcon = true;
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
}
