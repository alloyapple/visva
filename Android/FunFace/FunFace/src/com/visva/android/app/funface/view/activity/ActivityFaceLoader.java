package com.visva.android.app.funface.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.log.AIOLog;
import com.visva.android.app.funface.utils.StringUtility;
import com.visva.android.app.funface.utils.Utils;
import com.visva.android.app.funface.view.adapter.CustomArrayAdapter;
import com.visva.android.app.funface.view.widget.FaceView;
import com.visva.android.app.funface.view.widget.FaceViewGroup;
import com.visva.android.app.funface.view.widget.FaceViewGroup.ILayoutChange;
import com.visva.android.app.funface.view.widget.HorizontalListView;

public class ActivityFaceLoader extends VisvaAbstractActivity implements ILayoutChange {
    //=========================Define Constant================
    public static final int     TYPE_SHOW_LAYOUT_CHOOSE_OPTION = 0;
    public static final int     TYPE_SHOW_DELETE_FACE_LAYOUT   = 1;
    private static final int    TYPE_SHOW_ADD_FACE_LAYOUT      = 2;

    private static final int    LOL_FACE_TYPE                  = 0;
    private static final int    ANIMAL_FACE_TYPE               = 1;
    private static final int    FACEBOOK_FACE_TYPE             = 2;

    private static final int    SIZE_ANIMAL_FACE               = 30;
    private static final int    SIZE_FACEBOOK_FACE             = 33;
    private static final int    SIZE_RAGE_FACE                 = 31;
    //=========================Control Constant===============
    private RelativeLayout      mLayoutProgress;
    private RelativeLayout      mLayoutChooseOptions;
    private Button              mBtnAddEffects;
    private Animation           mContentUpAnime;
    private Animation           mContentDownAnime;
    private Animation           mDeleteFaceDownAnime;
    private RelativeLayout      mLayoutDeleteFaces;
    private ImageView           mImageDeletedFace;
    private RelativeLayout      mLayoutMain;
    private LinearLayout        mLayoutListEffectItems;
    private HorizontalListView  mItemOptionsListView;
    //=========================Variable Constant==============
    private FaceViewGroup       mFaceViewGroup;
    private Bitmap              mLoadedBitmap;
    private Bitmap              mResultBitmap;
    private Bitmap              mDeletedFaceBitmap;
    private int                 mBitmapWidth;
    private int                 mBitmapHeight;
    private float               mRatioX                        = 1.0F, mRatioY = 1.0F;

    private int                 mScreenWidth;
    private int                 mScreenHeight;
    private int                 mActionBarHeight, mNotificationBarHeight;
    private int                 mShowPreviousLayoutType        = TYPE_SHOW_LAYOUT_CHOOSE_OPTION;
    private int                 mShowNextLayoutType            = TYPE_SHOW_LAYOUT_CHOOSE_OPTION;
    private boolean             isShowDeletedFaceLayout        = false;
    /*this value is used for the height of image displayed in real position of device*/
    private int                 mRealImageHeight;
    private int                 mShowFaceType                  = ANIMAL_FACE_TYPE;
    private Face[]              mDetectedFaces;
    private ArrayList<FaceView> mChoiceFacesList               = new ArrayList<FaceView>();
    private ArrayList<FaceView> mDetectedFacesList             = new ArrayList<FaceView>();
    private int                 mMaxFaceId                     = 0;

    @Override
    public int contentView() {
        return R.layout.activity_face_loader;
    }

    @Override
    public void onCreate() {
        initData();
        initLayout();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String imagePath = bundle.getString(FunFaceConstant.EXTRA_IMAGE_PATH);
        if (StringUtility.isEmpty(imagePath)) {
            Toast.makeText(this, getString(R.string.image_load_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        File file = new File(imagePath);
        if (!file.exists()) {
            AIOLog.d(FunFaceConstant.TAG, "File is not existed!");
            Toast.makeText(this, getString(R.string.image_load_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Uri uri = Uri.fromFile(file);
        Utils.checkOrientation(uri);
        mLoadedBitmap = Utils.decodeBitmapFromCameraIntent(imagePath);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.mScreenWidth = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
        this.mScreenHeight = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);

        // Calculate ActionBar,Notification Bar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            mActionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        mNotificationBarHeight = getNotificationBarHeight();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            mFaceViewGroup.trackballClicked();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initLayout() {
        initLayoutChooseEffects();
        initLayoutDeleteFaces();
        mLayoutMain = (RelativeLayout) findViewById(R.id.layout_main);
        mLayoutProgress = (RelativeLayout) findViewById(R.id.layout_progress);
        mFaceViewGroup = (FaceViewGroup) findViewById(R.id.face_view_group);
        mFaceViewGroup.setListener(this);
        mFaceViewGroup.setScreenHeight(mScreenHeight);
        mFaceViewGroup.setScreenWidth(mScreenWidth);

        mLayoutProgress.setVisibility(View.VISIBLE);
        if (mLoadedBitmap == null) {
            Toast.makeText(this, getString(R.string.image_load_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        AsyncTaskFaceDetection asyncTaskFaceDetection = new AsyncTaskFaceDetection(this);
        asyncTaskFaceDetection.execute();
    }

    private void initLayoutDeleteFaces() {
        mLayoutDeleteFaces = (RelativeLayout) findViewById(R.id.layout_delete_face_id);
        mImageDeletedFace = (ImageView) findViewById(R.id.img_delete_face);
    }

    private void initLayoutChooseEffects() {
        mContentUpAnime = AnimationUtils.loadAnimation(this, R.anim.layout_content_up);
        mContentDownAnime = AnimationUtils.loadAnimation(this, R.anim.layout_content_down);
        mContentDownAnime.setAnimationListener(contentEffectDownAnimListener);
        mDeleteFaceDownAnime = AnimationUtils.loadAnimation(this, R.anim.layout_content_down);
        mDeleteFaceDownAnime.setAnimationListener(deleteFaceDownAnimListener);
        mLayoutChooseOptions = (RelativeLayout) findViewById(R.id.layout_choose_option_id);
        mLayoutChooseOptions.setVisibility(View.GONE);
        mLayoutListEffectItems = (LinearLayout) findViewById(R.id.layout_list_effect_item_id);
        mItemOptionsListView = (HorizontalListView) findViewById(R.id.list_options_item);

        setupOptionItemListCustomLists(mShowFaceType);

        mItemOptionsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position >= mChoiceFacesList.size())
                    return;

                int resId = mChoiceFacesList.get(position).getResId();
                FaceView faceView = null;
                int addPosition = -1;
                for (int i = 0; i < mDetectedFacesList.size(); i++) {
                    if (mDetectedFacesList.get(i).getFaceId() == -1) {
                        faceView = mDetectedFacesList.get(i);
                        addPosition = i;
                        break;
                    }
                }

                PointF midPoint = new PointF();
                int eyeDistance;
                mMaxFaceId++;
                if (faceView == null) {
                    Random random = new Random();
                    float x = random.nextInt(mScreenWidth - 100);
                    float y = random.nextInt(mRealImageHeight - 100);
                    midPoint.x = x;
                    midPoint.y = y;
                    eyeDistance = 100;
                    faceView = new FaceView(ActivityFaceLoader.this, resId, eyeDistance, mMaxFaceId);
                } else {
                    if (addPosition < mDetectedFaces.length) {
                        mDetectedFaces[addPosition].getMidPoint(midPoint);
                    }
                    faceView.setFaceId(mMaxFaceId);
                    faceView.load(ActivityFaceLoader.this, resId);
                    mDetectedFacesList.set(addPosition, faceView);
                }

                faceView.setPosition(midPoint, mRatioX, mRatioY, mBitmapWidth, mBitmapHeight, mRealImageHeight);
                faceView.setVisible(View.VISIBLE);
                mFaceViewGroup.addFace(faceView, ActivityFaceLoader.this);
                onLayoutChange(TYPE_SHOW_LAYOUT_CHOOSE_OPTION, false);
            }
        });
    }

    private void setupOptionItemListCustomLists(int showFaceType) {
        // Make an array adapter using the built in android layout to render a list of strings
        mChoiceFacesList = getListItem(showFaceType);
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, mChoiceFacesList);

        // Assign adapter to HorizontalListView
        mItemOptionsListView.setAdapter(adapter);
    }

    private ArrayList<FaceView> getListItem(int showFaceType) {
        ArrayList<FaceView> imageItems = new ArrayList<FaceView>();
        switch (showFaceType) {
        case ANIMAL_FACE_TYPE:
            for (int i = 1; i <= SIZE_ANIMAL_FACE; i++) {
                String resId = "animal" + i;
                mMaxFaceId++;
                FaceView imageItem = new FaceView(ActivityFaceLoader.this, Utils.getResId(ActivityFaceLoader.this, resId), 100, mMaxFaceId);
                imageItems.add(imageItem);
            }
            break;
        case LOL_FACE_TYPE:
            for (int i = 1; i <= SIZE_RAGE_FACE; i++) {
                String resId = "rage" + i;
                mMaxFaceId++;
                FaceView imageItem = new FaceView(ActivityFaceLoader.this, Utils.getResId(ActivityFaceLoader.this, resId), 100, mMaxFaceId);
                imageItems.add(imageItem);
            }
            break;
        case FACEBOOK_FACE_TYPE:
            for (int i = 1; i <= SIZE_FACEBOOK_FACE; i++) {
                String resId = "facebook" + i;
                mMaxFaceId++;
                FaceView imageItem = new FaceView(ActivityFaceLoader.this, Utils.getResId(ActivityFaceLoader.this, resId), 100, mMaxFaceId);
                imageItems.add(imageItem);
            }
            break;
        default:
            break;
        }
        return imageItems;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // release memory
        if (mLoadedBitmap != null) {
            mLoadedBitmap.recycle();
            mLoadedBitmap = null;
        }
        if (mResultBitmap != null) {
            mResultBitmap.recycle();
            mResultBitmap = null;
        }
        if (mDeletedFaceBitmap != null) {
            mDeletedFaceBitmap.recycle();
            mDeletedFaceBitmap = null;
        }
        mFaceViewGroup.unloadImages();
    }

    @Override
    public void onShowDeleteFaces(boolean isShowDeletedFace, final FaceView selectedFace) {
        Log.d("KieuThang", "selectedFace:" + selectedFace.getResId() + "isShowDeletedFace:" + isShowDeletedFace);
        int resId = selectedFace.getResId();
        if (resId == 0)
            return;
        if (isShowDeletedFace) {
            mImageDeletedFace.setVisibility(View.VISIBLE);
            mDeletedFaceBitmap = BitmapFactory.decodeResource(this.getResources(), resId);
            Log.d("KieuThang", "bitmap:" + mDeletedFaceBitmap);
            //            mIconDeleteFace.setImageBitmap(bitmap);
            mImageDeletedFace.setImageBitmap(mDeletedFaceBitmap);
        } else {
            mImageDeletedFace.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLayoutChange(int showLayoutType, boolean isShow) {
        if (TYPE_SHOW_DELETE_FACE_LAYOUT == showLayoutType) {
            if (isShowDeletedFaceLayout == isShow)
                return;
            if (isShow) {
                mLayoutDeleteFaces.setVisibility(View.VISIBLE);
                mLayoutDeleteFaces.startAnimation(mContentUpAnime);
                isShowDeletedFaceLayout = true;
            } else {
                mLayoutDeleteFaces.startAnimation(mDeleteFaceDownAnime);
            }
            return;
        }
        mShowNextLayoutType = showLayoutType;

        //show next layout
        switch (mShowNextLayoutType) {
        case TYPE_SHOW_LAYOUT_CHOOSE_OPTION:
            mLayoutListEffectItems.startAnimation(mContentDownAnime);
            break;
        case TYPE_SHOW_ADD_FACE_LAYOUT:
            mLayoutChooseOptions.startAnimation(mContentDownAnime);
            break;
        default:
            break;
        }
    }

    private AnimationListener deleteFaceDownAnimListener    = new AnimationListener() {

                                                                @Override
                                                                public void onAnimationStart(Animation animation) {

                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animation animation) {

                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animation animation) {
                                                                    mLayoutDeleteFaces.setVisibility(View.GONE);
                                                                    isShowDeletedFaceLayout = false;
                                                                }
                                                            };

    private AnimationListener contentEffectDownAnimListener = new AnimationListener() {

                                                                @Override
                                                                public void onAnimationStart(Animation animation) {
                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animation animation) {
                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animation animation) {
                                                                    switch (mShowPreviousLayoutType) {
                                                                    case TYPE_SHOW_ADD_FACE_LAYOUT:
                                                                        mLayoutListEffectItems.setVisibility(View.GONE);
                                                                        break;
                                                                    case TYPE_SHOW_LAYOUT_CHOOSE_OPTION:
                                                                        mLayoutChooseOptions.setVisibility(View.GONE);
                                                                        break;
                                                                    default:
                                                                        break;
                                                                    }
                                                                    switch (mShowNextLayoutType) {
                                                                    case TYPE_SHOW_ADD_FACE_LAYOUT:
                                                                        mLayoutListEffectItems.setVisibility(View.VISIBLE);
                                                                        mLayoutListEffectItems.startAnimation(mContentUpAnime);
                                                                        break;
                                                                    case TYPE_SHOW_LAYOUT_CHOOSE_OPTION:
                                                                        mLayoutChooseOptions.setVisibility(View.VISIBLE);
                                                                        mLayoutChooseOptions.startAnimation(mContentUpAnime);
                                                                        break;
                                                                    default:
                                                                        break;
                                                                    }

                                                                    mShowPreviousLayoutType = mShowNextLayoutType;
                                                                }
                                                            };

    @Override
    public void onDeletedFace(FaceView faceView) {
        Log.d("KieuThang", "faceView " + faceView.getCenterX() + ",getCenterY:" + faceView.getCenterY());
        if (mDetectedFaces == null || mDetectedFaces.length == 0)
            return;
        int removedPosition = -1;
        if (mFaceViewGroup.getFaceViewList().size() == 0) {
            removedPosition = 0;
            return;
        }
        Log.d("KieuThang", "mDetectedFacesList.size():" + mDetectedFacesList.size());
        for (int i = 0; i < mDetectedFacesList.size(); i++) {
            Log.d("KieuThang", "faceView.getFaceId():" + faceView.getFaceId() + ",mDetectedFacesList.get(i):" + mDetectedFacesList.get(i).getFaceId());
            if (faceView.getFaceId() == mDetectedFacesList.get(i).getFaceId()) {
                removedPosition = i;
                break;
            }
        }
        if (removedPosition == -1)
            return;
        FaceView _faceView = mDetectedFacesList.get(removedPosition);
        _faceView.setFaceId(-1);
        mDetectedFacesList.set(removedPosition, _faceView);
    }

    private class AsyncTaskFaceDetection extends AsyncTask<Void, Void, Integer> {
        private FaceDetector mFaceDetector;
        private Canvas       mCanvas;

        public AsyncTaskFaceDetection(Context context) {
            mBitmapWidth = mLoadedBitmap.getWidth();
            mBitmapHeight = mLoadedBitmap.getHeight();
            int heightOfLayoutEffect = (int) getResources().getDimensionPixelSize(R.dimen.layout_effect_height);

            mRatioX = (float) mScreenWidth / (float) mBitmapWidth;
            mRatioY = (float) (mScreenHeight - heightOfLayoutEffect - mActionBarHeight - mNotificationBarHeight) / (float) mBitmapHeight;

            //the height of image displayed on the device
            mRealImageHeight = mScreenHeight - heightOfLayoutEffect - mActionBarHeight;
            mFaceViewGroup.setRealImageHeight(mRealImageHeight);
            AIOLog.d(FunFaceConstant.TAG, "ratioX:" + mRatioX + ",ratioY:" + mRatioY + ",mRealImageHeight:" + mRealImageHeight);

            mFaceDetector = new FaceDetector(mBitmapWidth, mBitmapHeight, FunFaceConstant.MAX_FACES);
            mDetectedFaces = new Face[FunFaceConstant.MAX_FACES];
            mCanvas = new Canvas();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            mResultBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.RGB_565);
            // Paint ditherPaint = new Paint();
            // Paint drawPaint = new Paint();

            // ditherPaint.setDither(true);
            //  drawPaint.setColor(Color.RED);
            // drawPaint.setStyle(Paint.Style.STROKE);
            //  drawPaint.setStrokeWidth(2);
            if (mResultBitmap != null && !mResultBitmap.isRecycled()) {
                mCanvas.setBitmap(mResultBitmap);
                mCanvas.drawBitmap(mLoadedBitmap, 0, 0, null);
            }
            int facesFound = mFaceDetector.findFaces(mResultBitmap, mDetectedFaces);
            AIOLog.d(FunFaceConstant.TAG, "Number of faces found: " + facesFound);
            return facesFound;

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mLayoutProgress.setVisibility(View.GONE);
            mLayoutChooseOptions.setVisibility(View.VISIBLE);
            mLayoutChooseOptions.startAnimation(mContentUpAnime);
            switch (result) {
            case FunFaceConstant.RESULT_FAILED:
                mFaceViewGroup.setImageBitmap(mLoadedBitmap);
                break;

            default:
                updateFaceDetectionResult(result);
                break;
            }
        }

        private void updateFaceDetectionResult(Integer result) {
            PointF midPoint = new PointF();
            float eyeDistance = 0.0f;
            float confidence = 0.0f;

            for (int index = 0; index < result; ++index) {
                mDetectedFaces[index].getMidPoint(midPoint);
                eyeDistance = mDetectedFaces[index].eyesDistance();
                confidence = mDetectedFaces[index].confidence();

                Log.d("KieuThang", "Confidence: " + confidence + ", Eye distance: " + eyeDistance + ", Mid Point: (" + midPoint.x + ", "
                        + midPoint.y + ")");
                int faceSize = (int) (2 * eyeDistance * mRatioX);
                if (faceSize > mScreenWidth / 2)
                    faceSize = (int) (1.5 * eyeDistance * mRatioX);
                int resId = getRandomResIdFromResource(mShowFaceType);
                //TODO need check resId is 0x00
                mMaxFaceId++;
                FaceView faceView = new FaceView(ActivityFaceLoader.this, resId, faceSize, mMaxFaceId);
                faceView.setPosition(midPoint, mRatioX, mRatioY, mBitmapWidth, mBitmapHeight, mRealImageHeight);
                try {
                    mDetectedFacesList.add((FaceView) faceView.clone());
                } catch (CloneNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mFaceViewGroup.addFace(faceView, ActivityFaceLoader.this);
            }
            mFaceViewGroup.setImageBitmap(mResultBitmap);
        }

        private int getRandomResIdFromResource(int showFaceType) {
            Random random = new Random();
            int position = 0;
            String resIdName = "animal" + 0;
            switch (random.nextInt(2)) {
            case ANIMAL_FACE_TYPE:
                position = random.nextInt(30);
                resIdName = "animal" + position;
                break;
            case LOL_FACE_TYPE:
                position = random.nextInt(35);
                resIdName = "rage" + position;
            default:
                break;
            }
            return Utils.getResId(ActivityFaceLoader.this, resIdName);
        }

    }

    private int getNotificationBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int notificationBarHeight = 0;
        Log.d("KieuThang", "metrics.densityDpi:" + metrics.densityDpi);
        switch (metrics.densityDpi) {
        case DisplayMetrics.DENSITY_XHIGH:
            Log.d("KieuThang", "xhigh");
            notificationBarHeight = 50;
            break;
        case DisplayMetrics.DENSITY_HIGH:
            Log.d("KieuThang", "high");
            notificationBarHeight = 48;
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            Log.d("KieuThang", "medium/default");
            notificationBarHeight = 32;
            break;
        case DisplayMetrics.DENSITY_LOW:
            Log.d("KieuThang", "low");
            notificationBarHeight = 24;
            break;
        default:
            notificationBarHeight = 32;
            break;

        }
        return notificationBarHeight;
    }

    /*on click options tabs listener*/
    public void onClickAddFaceTab(View v) {
        onLayoutChange(TYPE_SHOW_ADD_FACE_LAYOUT, true);
    }

    public void onClickAddFrameTab(View v) {

    }

    public void onClickAddEffectTab(View v) {

    }

    public void onClickSettingTab(View v) {
        Toast.makeText(this, "onClickSettingTab", Toast.LENGTH_SHORT).show();
        AsyncTaskSaveImageToSdCard asyncTaskSaveImageToSdCard = new AsyncTaskSaveImageToSdCard();
        asyncTaskSaveImageToSdCard.execute();
    }

    /*on click add face layout events handler*/
    public void onClickAddFacebookFaceOption(View v) {
        Toast.makeText(this, "onClickAddOtherFaceOption", Toast.LENGTH_SHORT).show();
        mShowFaceType = FACEBOOK_FACE_TYPE;
        setupOptionItemListCustomLists(mShowFaceType);
    }

    public void onClickAddAnimalFaceOption(View v) {
        Toast.makeText(this, "onClickAddAnimalFaceOption", Toast.LENGTH_SHORT).show();
        mShowFaceType = ANIMAL_FACE_TYPE;
        setupOptionItemListCustomLists(mShowFaceType);
    }

    public void onClickAddLOLFaceOption(View v) {
        Toast.makeText(this, "onClickAddLOLFaceOption", Toast.LENGTH_SHORT).show();
        mShowFaceType = LOL_FACE_TYPE;
        setupOptionItemListCustomLists(mShowFaceType);
    }

    /**
     * THIS IS USED FOR SAVING THE IMAGE INTO THE SDCARD AFTER EFFECTING AND DETECTING
     */
    private class AsyncTaskSaveImageToSdCard extends AsyncTask<Void, Void, Void> {
        private Bitmap mSavedBitmap;

        public AsyncTaskSaveImageToSdCard() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            mSavedBitmap = Utils.loadBitmapFromView(mFaceViewGroup);
            String filepath = Environment.getExternalStorageDirectory() + "/funface_" + System.currentTimeMillis() + ".png";
            try {
                FileOutputStream fos = new FileOutputStream(filepath);
                mSavedBitmap.compress(CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mSavedBitmap != null) {
                mSavedBitmap.recycle();
                mSavedBitmap = null;
            }
        }
    }

}
