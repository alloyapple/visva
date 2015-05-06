package com.visva.android.app.funface.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
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
import com.visva.android.app.funface.view.adapter.CustomData;
import com.visva.android.app.funface.view.widget.FaceView;
import com.visva.android.app.funface.view.widget.FaceViewGroup;
import com.visva.android.app.funface.view.widget.FaceViewGroup.ILayoutChange;
import com.visva.android.app.funface.view.widget.HorizontalListView;

public class ActivityFaceLoader extends VisvaAbstractActivity implements ILayoutChange {
    //=========================Define Constant================
    public static final int    TYPE_SHOW_ITEM_OPTIONS_LAYOUT = 0;
    public static final int    TYPE_SHOW_DELETE_FACE_LAYOUT  = 1;
    private static final int   TYPE_SHOW_ADD_FACE_LAYOUT     = 2;
    private static final int   FACE_SIZE_OFF                 = 15;
    //=========================Control Constant===============
    private RelativeLayout     mLayoutProgress;
    private RelativeLayout     mLayoutChooseEffects;
    private Button             mBtnAddEffects;
    private Animation          mContentUpAnime;
    private Animation          mContentDownAnime;
    private Animation          mDeleteFaceDownAnime;
    private RelativeLayout     mRootView;
    private RelativeLayout     mLayoutDeleteFaces;
    private ImageView          mImageDeletedFace;
    private RelativeLayout     mLayoutMain;
    private LinearLayout       mLayoutItemOptions;
    private HorizontalListView mItemOptionsListView;
    //=========================Variable Constant==============
    private FaceViewGroup      mFaceViewGroup;
    private Bitmap             mLoadedBitmap;
    private int                mScreenWidth;
    private int                mScreenHeight;
    private int                mActionBarHeight, mNotificationBarHeight;
    private int                mShowPreviousLayoutType       = TYPE_SHOW_ITEM_OPTIONS_LAYOUT;
    private int                mShowNextLayoutType           = TYPE_SHOW_ITEM_OPTIONS_LAYOUT;
    private boolean            isShowDeletedFaceLayout       = false;
    private int                mFaceSizeMargin               = FACE_SIZE_OFF;
    /*this value is used for the height of image displayed in real position of device*/
    private int                mRealImageHeight;

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
        mFaceSizeMargin = (int) getResources().getDimension(R.dimen.face_margin);
        Log.d("KieuThang", "mNotificationBarHeight:" + mNotificationBarHeight);
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
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
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
        mLayoutChooseEffects = (RelativeLayout) findViewById(R.id.layout_choose_effect_id);
        mLayoutChooseEffects.setVisibility(View.GONE);
        mLayoutItemOptions = (LinearLayout) findViewById(R.id.layout_list_item_options_id);
        mItemOptionsListView = (HorizontalListView) findViewById(R.id.list_options_item);

        setupOptionItemListCustomLists();
    }

    private void setupOptionItemListCustomLists() {
        // Make an array adapter using the built in android layout to render a list of strings
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);

        // Assign adapter to HorizontalListView
        mItemOptionsListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadedBitmap != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadedBitmap.recycle();
                    mLoadedBitmap = null;
                }
            });
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
                                                                        mLayoutItemOptions.setVisibility(View.GONE);
                                                                        break;
                                                                    case TYPE_SHOW_ITEM_OPTIONS_LAYOUT:
                                                                        mLayoutChooseEffects.setVisibility(View.GONE);
                                                                        break;
                                                                    default:
                                                                        break;
                                                                    }
                                                                    switch (mShowNextLayoutType) {
                                                                    case TYPE_SHOW_ADD_FACE_LAYOUT:
                                                                        mLayoutItemOptions.setVisibility(View.VISIBLE);
                                                                        mLayoutItemOptions.startAnimation(mContentUpAnime);
                                                                        break;
                                                                    case TYPE_SHOW_ITEM_OPTIONS_LAYOUT:
                                                                        mLayoutChooseEffects.setVisibility(View.VISIBLE);
                                                                        mLayoutChooseEffects.startAnimation(mContentUpAnime);
                                                                        break;
                                                                    default:
                                                                        break;
                                                                    }

                                                                    mShowPreviousLayoutType = mShowNextLayoutType;
                                                                }
                                                            };

    @SuppressWarnings("deprecation")
    @Override
    public void onShowDeleteFaces(boolean isShowDeletedFace, FaceView selectedFace) {
        if (isShowDeletedFace) {
            mImageDeletedFace.setBackgroundResource(selectedFace.getResId());
        } else
            mImageDeletedFace.setBackgroundDrawable(null);
    }

    @Override
    public void onLayoutChange(int showLayoutType, boolean isShow) {
        Log.d("KieuThang", "mShowNextLayoutType:" + mShowNextLayoutType + ",mShowPreviousLayoutType:" + mShowPreviousLayoutType + ",showLayoutType:"
                + showLayoutType);
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
        case TYPE_SHOW_ITEM_OPTIONS_LAYOUT:
            mLayoutChooseEffects.setVisibility(View.VISIBLE);
            mLayoutChooseEffects.startAnimation(mContentUpAnime);
            break;
        case TYPE_SHOW_ADD_FACE_LAYOUT:
            mLayoutChooseEffects.startAnimation(mContentDownAnime);
            break;
        default:
            mLayoutChooseEffects.setVisibility(View.VISIBLE);
            mLayoutChooseEffects.startAnimation(mContentUpAnime);
            break;
        }
    }

    private class AsyncTaskFaceDetection extends AsyncTask<Void, Void, Integer> {
        private Face[]       faces;
        private FaceDetector mFaceDetector;
        private int          mBitmapWidth;
        private int          mBitmapHeight;
        private Canvas       mCanvas;
        private Bitmap       mResultBitmap;
        private float        mRatioX = 1.0F, mRatioY = 1.0F;

        public AsyncTaskFaceDetection(Context context) {
            mBitmapWidth = mLoadedBitmap.getWidth();
            mBitmapHeight = mLoadedBitmap.getHeight();
            int heightOfLayoutEffect = (int) getResources().getDimensionPixelSize(R.dimen.layout_effect_height);
            Log.d("KieuThang", "heightOfLayoutEffect:" + heightOfLayoutEffect + ",mActionBarHeight:" + mActionBarHeight);

            mRatioX = (float) mScreenWidth / (float) mBitmapWidth;
            //if (mBitmapWidth > mBitmapHeight)
            //  mRatioY = (float) (mScreenHeight - mActionBarHeight - mNotificationBarHeight) / (float) mBitmapHeight;
            //            else
            mRatioY = (float) (mScreenHeight - heightOfLayoutEffect - mActionBarHeight - mNotificationBarHeight) / (float) mBitmapHeight;

            //the height of image displayed on the device
            mRealImageHeight = mScreenHeight - heightOfLayoutEffect - mActionBarHeight;
            mFaceViewGroup.setRealImageHeight(mRealImageHeight);
            AIOLog.d(FunFaceConstant.TAG, "ratioX:" + mRatioX + ",ratioY:" + mRatioY + ",mRealImageHeight:" + mRealImageHeight);

            mFaceDetector = new FaceDetector(mBitmapWidth, mBitmapHeight, FunFaceConstant.MAX_FACES);
            faces = new Face[FunFaceConstant.MAX_FACES];
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
            int facesFound = mFaceDetector.findFaces(mResultBitmap, faces);

            AIOLog.d(FunFaceConstant.TAG, "Number of faces found: " + facesFound);
            return facesFound;

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mLayoutProgress.setVisibility(View.GONE);
            mLayoutChooseEffects.setVisibility(View.VISIBLE);
            mLayoutChooseEffects.startAnimation(mContentUpAnime);

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
                faces[index].getMidPoint(midPoint);
                eyeDistance = faces[index].eyesDistance();
                confidence = faces[index].confidence();

                Log.d("KieuThang", "Confidence: " + confidence + ", Eye distance: " + eyeDistance + ", Mid Point: (" + midPoint.x + ", "
                        + midPoint.y + ")");
                int faceSize = (int) (2 * eyeDistance * mRatioX);
                if (faceSize > mScreenWidth / 2)
                    faceSize = (int) (1.5 * eyeDistance * mRatioX);
                FaceView faceView = new FaceView(R.drawable.pic1, ActivityFaceLoader.this, faceSize);

                float scaleX = 1.0F;
                float scaleY = 1.0F;
                float centerX = midPoint.x * mRatioX;
                float centerY = midPoint.y * mRatioY;
                Log.d("KieuThang", "mBitmapWidth:" + mBitmapWidth + ", mBitmapHeight:" + mBitmapHeight);
                if (mBitmapWidth >= mBitmapHeight)
                    centerY = getCenterYOfFace(midPoint);
                float angle = Utils.getInitAngle();
                Log.d("KieuThang", "ratioX:" + mRatioX + ",ratioY:" + mRatioY + ",centerX:" + centerX + ",centerY:" + centerY);
                faceView.setPos(centerX, centerY, scaleX, scaleY, angle);
                mFaceViewGroup.addFace(faceView, ActivityFaceLoader.this);
            }
            mFaceViewGroup.setImageBitmap(mResultBitmap);
        }

        private float getCenterYOfFace(PointF midPoint) {
            PointF resizedMidPoint = new PointF();
            AIOLog.d(FunFaceConstant.TAG, "heightOfImage:" + mRealImageHeight);
            float ratio = (float) mBitmapWidth / mScreenWidth;
            float displayedImageHeight = (float) mBitmapHeight / ratio;
            resizedMidPoint.x = midPoint.x / ratio;
            resizedMidPoint.y = midPoint.y / ratio;
            return ((float) mRealImageHeight - displayedImageHeight) / 2 + resizedMidPoint.y - mFaceSizeMargin;
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
    public void onClickAddOtherFaceOption(View v) {
        Toast.makeText(this, "onClickAddOtherFaceOption", Toast.LENGTH_SHORT).show();
    }

    public void onClickAddAnimalFaceOption(View v) {
        Toast.makeText(this, "onClickAddAnimalFaceOption", Toast.LENGTH_SHORT).show();
    }

    public void onClickAddLOLFaceOption(View v) {
        Toast.makeText(this, "onClickAddLOLFaceOption", Toast.LENGTH_SHORT).show();
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
            mSavedBitmap = Utils.loadBitmapFromView(mLayoutMain);
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

    private CustomData[] mCustomData = new CustomData[] {
                                     new CustomData(Color.RED, "Red"),
                                     new CustomData(Color.DKGRAY, "Dark Gray"),
                                     new CustomData(Color.GREEN, "Green"),
                                     new CustomData(Color.LTGRAY, "Light Gray"),
                                     new CustomData(Color.WHITE, "White"),
                                     new CustomData(Color.RED, "Red"),
                                     new CustomData(Color.BLACK, "Black"),
                                     new CustomData(Color.CYAN, "Cyan"),
                                     new CustomData(Color.DKGRAY, "Dark Gray"),
                                     new CustomData(Color.GREEN, "Green"),
                                     new CustomData(Color.RED, "Red"),
                                     new CustomData(Color.LTGRAY, "Light Gray"),
                                     new CustomData(Color.WHITE, "White"),
                                     new CustomData(Color.BLACK, "Black"),
                                     new CustomData(Color.CYAN, "Cyan"),
                                     new CustomData(Color.DKGRAY, "Dark Gray"),
                                     new CustomData(Color.GREEN, "Green"),
                                     new CustomData(Color.LTGRAY, "Light Gray"),
                                     new CustomData(Color.RED, "Red"),
                                     new CustomData(Color.WHITE, "White"),
                                     new CustomData(Color.DKGRAY, "Dark Gray"),
                                     new CustomData(Color.GREEN, "Green"),
                                     new CustomData(Color.LTGRAY, "Light Gray"),
                                     new CustomData(Color.WHITE, "White"),
                                     new CustomData(Color.RED, "Red"),
                                     new CustomData(Color.BLACK, "Black"),
                                     new CustomData(Color.CYAN, "Cyan"),
                                     new CustomData(Color.DKGRAY, "Dark Gray"),
                                     new CustomData(Color.GREEN, "Green"),
                                     new CustomData(Color.LTGRAY, "Light Gray"),
                                     new CustomData(Color.RED, "Red"),
                                     new CustomData(Color.WHITE, "White"),
                                     new CustomData(Color.BLACK, "Black"),
                                     new CustomData(Color.CYAN, "Cyan"),
                                     new CustomData(Color.DKGRAY, "Dark Gray"),
                                     new CustomData(Color.GREEN, "Green"),
                                     new CustomData(Color.LTGRAY, "Light Gray")
                                     };
}
