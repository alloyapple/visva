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
import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.idxpwdatabase.ElementID;
import visvateam.outsource.idmanager.idxpwdatabase.IDxPWDataBaseHandler;
import visvateam.outsource.idmanager.idxpwdatabase.Password;
import visvateam.outsource.idmanager.sercurity.CipherUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class EditIdPasswordActivity2 extends BaseActivity implements
		OnItemClickListener, android.content.DialogInterface.OnClickListener {
	public static final int ELEMENT_FLAG_TRUE = 1;
	public static final int ELEMENT_FLAG_FALSE = 0;
	// =========================Control Define ==================
	private ListView mListView;
	private CheckBox mCheckBoxLike;

	private EditText mEditTextUrlId;
	private EditText mEditTextNameId;
	private EditText mEditTextNote;
	// =========================Class Define ====================
	private IDxPWDataBaseHandler mDataBaseHandler;
	private static ArrayList<Item> mItems;
	private MultiDirectionSlidingDrawer mSlidingDrawer;
	// =========================Variable Define =================
	public static String DEFAULT_NAME_ITEM[] = { "ID", "Password", "", "", "" };
	private int modeBundle;

	// id password info
	private static int currentFolderId;
	private static boolean isCreatNew;
	private static int currentElementId;
	private static int isLike;
	private static String titleRecord;
	private static String icon;
	private static String url;
	private static String note;
	private static String imageMemo;
	private static Drawable mDrawableIcon;
	public static Drawable mDrawableMemo;
	private static boolean isUpdateIcon;
	private static boolean isUpdateMemo;
	public static String mUrlItem;
	public static String mStringOfSelectItem;
	public ArrayList<ViewHolder> viewHolder = new ArrayList<EditIdPasswordActivity2.ViewHolder>();
	public static int itemSelect = -1;
	private IdManagerPreference mIdManagerPreference;
	private static final String DEFAULT_URL = "http://google.com";
	private boolean isButtonPress = false;
	private static boolean isSetUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_id_pass);
		modeBundle = getIntent().getExtras().getInt(
				Contants.IS_INTENT_CREATE_NEW_ID);

		currentFolderId = getIntent().getExtras().getInt(
				Contants.CURRENT_FOLDER_ID);
		if (modeBundle == 0) {
			currentElementId = getIntent().getExtras().getInt(
					Contants.CURRENT_PASSWORD_ID);
			icon = "";
			imageMemo = "";
		} else if (modeBundle == 1) {
			currentElementId = -1;
			icon = "";
			imageMemo = "";
		}

		/* initialize database */
		initDataBase();

		/* initialize control */
		initControl();
		if (modeBundle == 0) {
			isCreatNew = false;
			isUpdateIcon = false;
			isUpdateMemo = false;
			mDrawableIcon = getIconDatabase(icon);
			if (!imageMemo.equals("")) {
				mDrawableMemo = getIconDatabase(imageMemo);
				mDrawableMemo.setFilterBitmap(true);
			}

			else {
				mDrawableMemo = null;
			}
		} else if (modeBundle == 1) {
			isCreatNew = true;
			isUpdateIcon = true;
			isUpdateMemo = false;
			mDrawableIcon = getResources().getDrawable(R.drawable.default_icon);
			mDrawableMemo = null;
		} else {
			mEditTextNameId.setText(titleRecord);
			mEditTextNote.setText(note);
			mEditTextUrlId.setText(mUrlItem);
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
	@SuppressLint("NewApi")
	public static Drawable getIconDatabase(String icon) {
		File dir = new File(Contants.PATH_ID_FILES);
		if (!dir.exists())
			dir.mkdirs();
		File inputFile = new File(dir, icon);

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
		Bitmap bmp = BitmapFactory.decodeByteArray(decryptBytes, 0,
				decryptBytes.length);
		return (Drawable) new BitmapDrawable(bmp);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mDrawableIcon != null)
			Log.i("abc", " mDrawableIcon != null");
		((ImageButton) findViewById(R.id.img_avatar))
				.setBackgroundDrawable(mDrawableIcon);
		Log.i("isSetUrl", "" + itemSelect);
		if (isSetUrl)
			((EditText) findViewById(R.id.edit_text_url)).setText(mUrlItem);
		isSetUrl = false;
		if (mDrawableMemo != null) {
			((ImageButton) findViewById(R.id.btn_img_memo))
					.setBackgroundDrawable(mDrawableMemo);
			((Button) findViewById(R.id.button_img_memo))
					.setVisibility(View.GONE);
		} else {
			((ImageButton) findViewById(R.id.btn_img_memo))
					.setVisibility(View.GONE);
		}
		Log.i("itemSelect", "" + itemSelect);
		if (itemSelect >= 0) {
			mItems.get(itemSelect).mContentItem = mStringOfSelectItem;
			mListView.setAdapter(new ItemAddAdapter(this, mItems));
			itemSelect = -1;
		}
		if (mDrawableMemo != null) {
			((ImageButton) findViewById(R.id.btn_img_memo))
					.setBackgroundDrawable(mDrawableMemo);
			((ImageButton) findViewById(R.id.btn_img_memo))
					.setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.button_img_memo))
					.setVisibility(View.GONE);
		} else {
			((ImageButton) findViewById(R.id.btn_img_memo))
					.setBackgroundDrawable(mDrawableMemo);
			((ImageButton) findViewById(R.id.btn_img_memo))
					.setVisibility(View.GONE);
			((Button) findViewById(R.id.button_img_memo))
					.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * initialize database
	 */
	private void initDataBase() {
		// TODO Auto-generated method stub
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new IDxPWDataBaseHandler(this);

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
				if (isChecked)
					isLike = 1;
				else
					isLike = 0;
			}
		});
		mListView = (ListView) findViewById(R.id.id_listview_item_add);
		/* load data for list item id */
		if (modeBundle <= 1) {
			mItems = loadDataForListItem();
		}
		mListView.setAdapter(new ItemAddAdapter(this, mItems));
		mSlidingDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
		mSlidingDrawer.open();
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
			if (currentElementId == -1)
				finish();
			ElementID element = mDataBaseHandler.getElementID(currentElementId);
			mEditTextNote.setText(element.geteNote());
			mUrlItem = element.geteUrl();
			imageMemo = element.geteImage();
			mEditTextNameId.setText(element.geteTitle());
			mEditTextUrlId.setText(mUrlItem);
			icon = element.geteIcon();
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
			// set icon for image icon

		}
		return itemList;
	}

	public static void startActivity(Activity activity, int valueExtra) {
		Intent i = new Intent(activity, EditIdPasswordActivity2.class);
		i.putExtra(Contants.IS_INTENT_CREATE_NEW_ID, valueExtra);
		activity.startActivity(i);
	}

	public void onImgAvatar(View v) {
		ListIconActivity.startActivity(this);
		saveInput();
		finish();

	}

	public void onToGenerator(int i) {
		itemSelect = i;
		saveInput();
		PasswordGeneratorActivity.startActivity(this);
		finish();

	}

	public void saveInput() {
		titleRecord = mEditTextNameId.getText().toString();
		mUrlItem = mEditTextUrlId.getText().toString();
		note = mEditTextNote.getText().toString();
	}

	public void onReturn(View v) {
		if (isButtonPress)
			return;
		isButtonPress = true;
		createOrUpdateId();
	}

	public void onInfo(View v) {
		saveInput();
		Intent intentBrowser = new Intent(EditIdPasswordActivity2.this,
				BrowserActivity.class);
		intentBrowser.putExtra(Contants.KEY_TO_BROWSER, Contants.EDIT_TO);
		startActivity(intentBrowser);
		finish();
	}

	public void onGoogleHome(View v) {
		isSetUrl = true;
		saveInput();
		SettingURLActivity.startActivity(this);
		finish();
	}

	public void onMemoImage(View v) {
		// ImageMemoActivity.startActivity(this);
		Intent intentMemo = new Intent(EditIdPasswordActivity2.this,
				ImageMemoActivity2.class);
		intentMemo.putExtra("modeBundleMemo", 1);
		startActivityForResult(intentMemo, Contants.INTENT_IMG_MEMO);
		finish();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			final int pos = position;
			LayoutInflater inflater = mActivity.getLayoutInflater();
			// if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_id_pass_add, null);
			holder = new ViewHolder();
			holder.nameItem = (EditText) convertView
					.findViewById(R.id.id_txt_nameItem);
			holder.nameItem.setText(mItems.get(position).mNameItem);
			holder.nameItem.addTextChangedListener(new TextWatcher() {

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
					EditIdPasswordActivity2.mItems.get(position).mNameItem = s
							.toString();
				}
			});
			holder.contentItem = (EditText) convertView
					.findViewById(R.id.id_txt_detailItem);
			holder.contentItem.setText(mItems.get(position).mContentItem);
			holder.contentItem.addTextChangedListener(new TextWatcher() {

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
					EditIdPasswordActivity2.mItems.get(position).mContentItem = s
							.toString();
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

			return convertView;
		}

	}

	private class ViewHolder {
		EditText nameItem;
		EditText contentItem;

	}

	public void onAvatarClick(View v) {
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
		switch (id) {
		case Contants.DIALOG_CREATE_ID:
			break;
		default:
			return null;
		}
		return null;
	}

	/**
	 * create new id password
	 */
	private void createOrUpdateId() {
		addNewIdValuesToDataBase();
		finish();
	}

	public String encyptAndSaveIcon(Drawable pDrawable, String icon) {
		String namString = String.valueOf(System.currentTimeMillis());
		String name = null;
		name = namString + ".dat";
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
		FileOutputStream fileOutputStream = null;
		File dir = new File(Contants.PATH_ID_FILES);
		if (!dir.exists())
			dir.mkdirs();

		try {
			fileOutputStream = new FileOutputStream(new File(dir, name));

			try {
				fileOutputStream.write(resultEncrypt, 0, resultEncrypt.length);
				return name;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null)
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return "";
	}

	@SuppressWarnings("unused")
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
		titleRecord = mEditTextNameId.getText().toString();
		url = mEditTextUrlId.getText().toString();
		boolean b = false;
		for (int i = 0; i < mItems.size(); i++) {
			if (!mItems.get(i).mContentItem.equals("")) {
				b = true;
				break;
			}
		}
		if (!b || titleRecord.equals("") || url.equals(""))
			return;
		if (isUpdateIcon) {
			isUpdateIcon=false;
			File fileIcon = new File(Contants.PATH_ID_FILES, icon);
			if (fileIcon != null && fileIcon.exists())
				fileIcon.delete();
			icon = encyptAndSaveIcon(mDrawableIcon, icon);
		}
		if (isUpdateMemo) {
			isUpdateMemo=false;
			File fileMemo = new File(Contants.PATH_ID_FILES, imageMemo);
			if (fileMemo != null && fileMemo.exists())
				fileMemo.delete();
			imageMemo = encyptAndSaveIcon(mDrawableMemo, imageMemo);
		}
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
				titleRecord, icon, System.currentTimeMillis(), isLike,
				ELEMENT_FLAG_FALSE, url, note, imageMemo,
				mDataBaseHandler.getElementsCountFromFolder(currentFolderId));

		// create id int normal folder
		if (isCreatNew) {
			mDataBaseHandler.addNewElementId(newElement);
			mIdManagerPreference
					.setNumberItem(
							IdManagerPreference.NUMBER_ITEMS,
							mIdManagerPreference
									.getNumberItems(IdManagerPreference.NUMBER_ITEMS) + 1);
		} else
			mDataBaseHandler.updateElementId(newElement);

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

	public static void updateIcon(Drawable pDrawable) {

		mDrawableIcon = pDrawable;
		if (pDrawable != null)
			Log.i("abc", "null");
		isUpdateIcon = true;
	}

	public static void updateMemo(Drawable pDrawable) {
		mDrawableMemo = pDrawable;
		isUpdateMemo = true;
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
