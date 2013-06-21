package com.japanappstudio.IDxPassword.activities;

import it.sephiroth.demo.slider.widget.MultiDirectionSlidingDrawer;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import com.japanappstudio.IDxPassword.activities.R;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.idxpwdatabase.ElementID;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.Password;

@SuppressWarnings("unused")
public class EditIdPasswordActivity extends BaseActivity implements
		OnItemClickListener, android.content.DialogInterface.OnClickListener {
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
	private IDxPWDataBaseHandler mDataBaseHandler;
	private static ArrayList<Item> mItems;
	// private MultiDirectionSlidingDrawer mSlidingDrawer;
	// =========================Variable Define =================
	public static String DEFAULT_NAME_ITEM[] = { "ID", "Password", "", "", "" };
	public static int MAX_ITEM = 5;
	private int modeBundle;

	// id password info
	private static int currentFolderId;
	private static boolean isCreatNew;
	private static int currentElementId;
	private static int isLike;
	private static String titleRecord;
	private static byte[] icon;
	private static String url;
	private static String note;
	private static byte[] imageMemo;
	private static Drawable mDrawableIcon;
	public static Drawable mDrawableMemo;
	private static boolean isUpdateIcon;
	private static boolean isUpdateMemo;
	public static String mUrlItem;
	public static String mStringOfSelectItem;
	private Button btn_memo;
	private ImageButton img_memo;

	public static int itemSelect = -1;
	private IdManagerPreference mIdManagerPreference;
	private static final String DEFAULT_URL = "http://google.com";
	private boolean isButtonPress = false;
	private static boolean isSetUrl;
	// private int
	public static final String IS_INTENT_CREATE_NEW_ID = "IS_INTENT_CREATE_NEW_ID";
	private static int modeFrom;
	private static int widthMemo;
	private static float ratioMemo;

	private ImageView controlView;
	private LinearLayout view1, view2;
	private LinearLayout lnParent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_id_pass2);
		modeBundle = getIntent().getExtras().getInt(
				Contants.IS_INTENT_CREATE_NEW_ID);
		if (modeBundle != 2)
			modeFrom = getIntent().getExtras().getInt(Contants.IS_SRC_ACTIVITY);
		initSlideView();
		initViewItem();
		btn_memo = (Button) findViewById(R.id.button_img_memo);
		img_memo = (ImageButton) findViewById(R.id.btn_img_memo);
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		currentFolderId = mIdManagerPreference.getCurrentFolderId();
		if (modeBundle == 0) {
			currentElementId = getIntent().getExtras().getInt(
					Contants.CURRENT_PASSWORD_ID);
			icon = new byte[] {};
			imageMemo = new byte[] {};
		} else if (modeBundle == 1) {
			currentElementId = -1;
			icon = new byte[] {};
			imageMemo = new byte[] {};
		}

		/* initialize database */
		initDataBase();

		/* initialize control */
		initControl();
		if (modeBundle == 0) {
			isCreatNew = false;
			isUpdateIcon = false;
			isUpdateMemo = false;
			mDrawableIcon = getIconDataBase(icon);
			mDrawableMemo = getMemoDataBase(imageMemo);

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (modeFrom == 1)
				CopyItemActivity.startActivity(this, currentElementId);
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initAdmod() {
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	public Drawable getIconDataBase(byte[] data) {
		if (data == null || data.length == 0) {
			return getResources().getDrawable(R.drawable.default_icon);
		}

		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		@SuppressWarnings("deprecation")
		BitmapDrawable result = new BitmapDrawable(bMap);
		return result;
	}

	public Drawable getMemoDataBase(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}

		Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		@SuppressWarnings("deprecation")
		BitmapDrawable result = new BitmapDrawable(bMap);
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mDrawableIcon != null)
			((ImageButton) findViewById(R.id.img_avatar))
					.setBackgroundDrawable(mDrawableIcon);
		if (isSetUrl)
			((EditText) findViewById(R.id.edit_text_url)).setText(mUrlItem);
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

	public static void setRatioMemo(float k) {
		ratioMemo = k;
	}

	/**
	 * initialize database
	 */
	private void initDataBase() {
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
		// mListView = (ListView) findViewById(R.id.id_listview_item_add);
		/* load data for list item id */
		if (modeBundle <= 1) {
			mItems = loadDataForListItem();
		}
		updateItems(mItems);
		// mSlidingDrawer = (MultiDirectionSlidingDrawer)
		// findViewById(R.id.drawer);
		// mSlidingDrawer.open();
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

	public static void startActivity(Activity activity, int valueExtra) {
		Intent i = new Intent(activity, EditIdPasswordActivity.class);
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
		Intent intentBrowser = new Intent(EditIdPasswordActivity.this,
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
		if (btn_memo.getVisibility() == View.VISIBLE)
			widthMemo = btn_memo.getWidth();
		else
			widthMemo = img_memo.getWidth();
		Intent intentMemo = new Intent(EditIdPasswordActivity.this,
				ImageMemoActivity.class);
		intentMemo.putExtra("modeBundleMemo", 1);
		startActivityForResult(intentMemo, Contants.INTENT_IMG_MEMO);
		finish();
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
		if (modeFrom == 1)
			CopyItemActivity.startActivity(this, currentElementId);
		finish();
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
			mIdManagerPreference
					.setNumberItem(
							IdManagerPreference.NUMBER_ITEMS,
							mIdManagerPreference
									.getNumberItems(IdManagerPreference.NUMBER_ITEMS) + 1);
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
