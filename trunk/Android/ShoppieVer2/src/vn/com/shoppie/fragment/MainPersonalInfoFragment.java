package vn.com.shoppie.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import vn.com.shoppie.R;
import vn.com.shoppie.adapter.FavouriteAdapter;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShopieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.HistoryTransactionItem;
import vn.com.shoppie.database.sobject.HistoryTransactionList;
import vn.com.shoppie.object.FacebookUser;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.object.HorizontalListView;
import vn.com.shoppie.object.MyCircleImageView;
import vn.com.shoppie.object.ShoppieUserInfo;
import vn.com.shoppie.util.ImageLoader;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainPersonalInfoFragment extends FragmentBasic {

	// ==========================Constant Define=================
	private static final int REQUEST_CODE_CAMERA = 1001;
	private static final int REQUEST_CODE_GALLERY = 1002;
	// ===========================Control Define==================
	private LinearLayout mLayoutPersonalInfo;
	private LinearLayout mLayoutFavouriteProduct;
	private LinearLayout mLayoutFrvouriteCategory;
	private LinearLayout mLayoutFriend;
	private LinearLayout mLayoutFeedback;
	private LinearLayout mLayoutHelp;
	private LinearLayout mLayoutHistoryTrade;
	private MyCircleImageView mImgAvatar;
	private ImageView mImgCover;
	private TextView mTxtUserName;
	private TextView mTxtUserId;
	private TextView mTxtUserNumberPie;
	private HorizontalListView mFavouriteBrandList;
	private HorizontalListView mFavouriteProductList;
	// =========================Class Define --------------------
	private MainPersonalInfoListener mListener;
	private ImageLoader mImageLoader;
	private ShopieSharePref mShopieSharePref;
	private FavouriteAdapter mFavouriteProductAdapter;
	private FavouriteAdapter mFavouriteBrandAdapter;
	private ShoppieDBProvider mShoppieDBProvider;
	// =========================Variable Define==================
	private boolean isShowFavouriteProduct = false;
	private boolean isShowFavouriteBrand = false;
	private boolean isPickToAvatar = true;
	private ArrayList<FavouriteDataObject> mFavouriteProductObjects = new ArrayList<FavouriteDataObject>();
	private ArrayList<FavouriteDataObject> mFavouriteBrandObjects = new ArrayList<FavouriteDataObject>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_main_personal_info, null);

		mImageLoader = new ImageLoader(getActivity());
		mShopieSharePref = new ShopieSharePref(getActivity());
		initData();
		initialize(root);
		return root;
	}

	private void initData() {
		// TODO Auto-generated method stub
		mShoppieDBProvider = new ShoppieDBProvider(getActivity());
		mFavouriteProductObjects = mShoppieDBProvider
				.getFavouriteData(GlobalValue.TYPE_FAVOURITE_PRODUCT);
		mFavouriteBrandObjects = mShoppieDBProvider
				.getFavouriteData(GlobalValue.TYPE_FAVOURITE_BRAND);
		Log.e("mFavouriteProductObjects + "+mFavouriteProductObjects.size(), "mFavouriteBrandObjects "+mFavouriteBrandObjects.size());
	}

	private void initialize(View v) {
		// TODO Auto-generated method stub

		mTxtUserId = (TextView) v.findViewById(R.id.txt_user_id);
		mTxtUserName = (TextView) v.findViewById(R.id.txt_personal_name);
		mTxtUserNumberPie = (TextView) v.findViewById(R.id.txt_user_number_pie);
		mImgAvatar = (MyCircleImageView) v.findViewById(R.id.img_avatar);
		mImgCover = (ImageView) v.findViewById(R.id.img_cover);
		mLayoutFavouriteProduct = (LinearLayout) v
				.findViewById(R.id.layout_fravourite_product);
		mLayoutFeedback = (LinearLayout) v.findViewById(R.id.layout_feedback);
		mLayoutFriend = (LinearLayout) v
				.findViewById(R.id.layout_personal_people);
		mLayoutFrvouriteCategory = (LinearLayout) v
				.findViewById(R.id.layout_fravourite_category);
		mLayoutHelp = (LinearLayout) v.findViewById(R.id.layout_help);
		mLayoutHistoryTrade = (LinearLayout) v
				.findViewById(R.id.layout_history_trade);
		mLayoutPersonalInfo = (LinearLayout) v.findViewById(R.id.layout_info);

		mFavouriteBrandList = (HorizontalListView) v
				.findViewById(R.id.favourite_brand_list);
		mFavouriteProductList = (HorizontalListView) v
				.findViewById(R.id.favourite_product_list);
		mFavouriteProductAdapter = new FavouriteAdapter(getActivity(),
				mFavouriteProductObjects);
		mFavouriteBrandAdapter = new FavouriteAdapter(getActivity(),
				mFavouriteBrandObjects);
		mFavouriteBrandList.setAdapter(mFavouriteBrandAdapter);
		mFavouriteProductList.setAdapter(mFavouriteProductAdapter);
		mLayoutFavouriteProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowFavouriteProduct)
					mFavouriteProductList.setVisibility(View.GONE);
				else {
					mFavouriteProductList.setVisibility(View.VISIBLE);
				}
				isShowFavouriteProduct = !isShowFavouriteProduct;
			}
		});

		mLayoutFrvouriteCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowFavouriteBrand)
					mFavouriteBrandList.setVisibility(View.GONE);
				else
					mFavouriteBrandList.setVisibility(View.VISIBLE);
				isShowFavouriteBrand = !isShowFavouriteBrand;
			}
		});

		mLayoutFeedback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast(getActivity().getString(R.string.feedback_content));
				initShareItent(
						getActivity().getString(R.string.feedback_subject),
						getActivity().getString(R.string.feedback_content2),
						getActivity().getString(R.string.feedback_send_to));
			}
		});
		mLayoutFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickFriend();
			}
		});
		mLayoutHelp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickHelp();
			}
		});
		mLayoutHistoryTrade.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickHistoryTrade();
			}
		});
		mLayoutPersonalInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickPersonalInfo();
			}
		});

		mImgAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isPickToAvatar = true;
				pickImage();
			}
		});

		mImgCover.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isPickToAvatar = false;
				pickImage();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void onClickMainPersonalInfo(View v) {
		Log.e("click ", "adfdfd ");
	}

	public interface MainPersonalInfoListener {
		public void onClickPersonalInfo();

		public void onClickHelp();

		public void onClickHistoryTrade();

		public void onClickFriend();

		public void onClickFeedback();
	}

	public void setListener(MainPersonalInfoListener listener) {
		this.mListener = listener;
	}

	public void updateUserInfo(FacebookUser user) {
		// TODO Auto-generated method stub
		mImageLoader.DisplayImage(user.getPicture().getData().getUrl(),
				mImgAvatar);
		mTxtUserName.setText(user.getName());
		mTxtUserId.setText("ID: " + mShopieSharePref.getCustId());
	}

	public void updateShoppieUser(ShoppieUserInfo userInfo) {
		mTxtUserName.setText(userInfo.getName());
		mTxtUserId.setText("ID: " + userInfo.getId());
		if (userInfo.getAvatar() != "") {
			File file = new File(userInfo.getAvatar());
			if (file.exists()) {
				Uri uri = Uri.fromFile(file);
				int orientation = checkOrientation(uri);
				Bitmap bmp;
				bmp = decodeSampledBitmapFromFile(userInfo.getAvatar(), 100,
						100, orientation);
				mImgAvatar.setImageBitmap(bmp);

			} else {
				Toast.makeText(getActivity(), "Load avatar error",
						Toast.LENGTH_SHORT).show();
				mImgAvatar.setBackgroundResource(R.drawable.bg_personal_avatar);
			}
		} else
			mImgAvatar.setBackgroundResource(R.drawable.bg_personal_avatar);
		if (userInfo.getCover() != "") {
			File file = new File(userInfo.getCover());
			if (file.exists()) {
				Uri uri = Uri.fromFile(file);
				int orientation = checkOrientation(uri);
				Bitmap bmp;
				bmp = decodeSampledBitmapFromFile(userInfo.getCover(), 200,
						200, orientation);
				mImgCover.setImageBitmap(bmp);

			} else {
				Toast.makeText(getActivity(), "Load avatar error",
						Toast.LENGTH_SHORT).show();
				mImgCover.setBackgroundResource(R.drawable.bg_personal_avatar);
			}
		} else
			mImgCover.setBackgroundResource(R.drawable.bg_personal_cover);
	}

	public void updatePie(HistoryTransactionList historyTransactionList) {
		// TODO Auto-generated method stub
		HistoryTransactionItem historyTransactionItem = historyTransactionList
				.getResult().get(0);
		mTxtUserNumberPie.setText("Điểm tích lũy: "
				+ historyTransactionItem.getCurrentBal());
	}

	private void showToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
	}

	private void initShareItent(String subject, String content, String email) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, "" + subject);
		share.putExtra(Intent.EXTRA_TEXT, "" + content);
		share.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		// if (pFilePath != null)
		// share.putExtra(Intent.EXTRA_STREAM,
		// Uri.fromFile(new File(pFilePath)));
		share.setType("vnd.android.cursor.dir/email");
		startActivity(Intent.createChooser(share, "Select"));

	}

	private void pickImage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pick_image);
		builder.setItems(R.array.image_array,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// The 'which' argument contains the index position
						// of the selected item
						switch (which) {
						case 0:
							// when user click camera to get image
							Intent cameraIntent = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							// request code
							startActivityForResult(cameraIntent,
									REQUEST_CODE_GALLERY);
							break;

						case 1:
							// when user click camera to get image
							// mListener.goToGalleryOfPhoneToGetImage();
							Intent galleryIntent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(galleryIntent,
									REQUEST_CODE_CAMERA);
							break;

						default:
							break;
						}
					}
				});
		builder.show();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_CAMERA:
			if (resultCode == getActivity().RESULT_OK) {
				Uri uri = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(uri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imagePath = cursor.getString(columnIndex);
				cursor.close();
				if (uri != null) {
					File file = new File(imagePath);
					String imageName = file.getName();
					if (file.exists()) {
						Uri fileUri = Uri.fromFile(file);
						int orientation = checkOrientation(fileUri);
						Bitmap bmp;
						if (isPickToAvatar) {
							mShopieSharePref.setImageAvatar(imagePath);
							bmp = decodeSampledBitmapFromFile(imagePath, 100,
									100, orientation);
							mImgAvatar.setImageBitmap(bmp);
						} else {
							mShopieSharePref.setImageCover(imagePath);
							bmp = decodeSampledBitmapFromFile(imagePath, 200,
									200, orientation);
							mImgCover.setImageBitmap(bmp);
						}

					} else {
						// Log.d(tag, "file don't exist !");
					}
				}
			}
			break;
		case REQUEST_CODE_GALLERY:
			if (resultCode == getActivity().RESULT_OK) {
				Log.d("data", "c " + data);
				Uri uri = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(uri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imagePath = cursor.getString(columnIndex);
				cursor.close();

				// save original avatar image
				// originalAvatarPath = imagePath;
				// Log.i(tag, "onActivityResult - originalAvatarPath: "
				// + originalAvatarPath);

				File file = new File(imagePath);
				String imageName = file.getName();
				Uri fileUri = null;
				if (file.exists()) {
					fileUri = Uri.fromFile(file);
					int orientation = checkOrientation(fileUri);
					Bitmap bmp;
					if (isPickToAvatar) {
						mShopieSharePref.setImageAvatar(imagePath);
						bmp = decodeSampledBitmapFromFile(imagePath, 100, 100,
								orientation);
						mImgAvatar.setImageBitmap(bmp);
					} else {
						mShopieSharePref.setImageCover(imagePath);
						bmp = decodeSampledBitmapFromFile(imagePath, 200, 200,
								orientation);
						mImgCover.setImageBitmap(bmp);
					}

				} else {
					Log.d("test", "file don't exist !");
				}
				if (fileUri == null) {
					// imgBound.setVisibility(View.GONE);
				} else {
					// imgBound.setVisibility(View.VISIBLE);
				}
				// imageView.setImageBitmap(null);
				// imageView.setImageURI(fileUri);
			}
			break;
		default:
			return;
		}
	}

	// rotate image to have the exact orientation
	private int checkOrientation(Uri fileUri) {
		int rotate = 0;
		String imagePath = fileUri.getPath();
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(imagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Since API Level 5
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = 90;
			break;
		}
		return rotate;
	}

	private Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth,
			int reqHeight, int orientation) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Matrix mtx = new Matrix();
		mtx.postRotate(orientation);
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		int width = options.outWidth;
		int height = options.outHeight;
		Log.d("width " + height, "width " + width);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Log.d("orientation ", "orientation " + orientation);
		// return Bitmap.createBitmap(BitmapFactory.decodeFile(filePath,
		// options), 0, 0, reqHeight,
		// reqWidth, mtx, true);

		return decodeBitmap(BitmapFactory.decodeFile(filePath, options),
				orientation);

	}

	private Bitmap decodeBitmap(Bitmap bitmap, int orientation) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(orientation);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}
