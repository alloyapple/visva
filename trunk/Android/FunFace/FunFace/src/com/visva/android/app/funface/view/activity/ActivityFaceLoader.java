package com.visva.android.app.funface.view.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.log.AIOLog;
import com.visva.android.app.funface.utils.StringUtility;
import com.visva.android.app.funface.utils.Utils;
import com.visva.android.app.funface.view.widget.FaceView;
import com.visva.android.app.funface.view.widget.FaceViewGroup;

@SuppressLint("ClickableViewAccessibility")
public class ActivityFaceLoader extends VisvaAbstractActivity {
    //=========================Define Constant================
    //=========================Control Constant===============
    private ImageView      mImageViewLoadedBitmap;
    private RelativeLayout mLayoutProgress;
    private RelativeLayout mLayoutChooseEffects;
    private Button         mBtnAddEffects;
    private Animation      mContentUpAnime;
    private Animation      mContentDownAnime;
    private RelativeLayout mRootView;
    //=========================Variable Constant==============
    private FaceViewGroup  mFaceViewController;
    private Bitmap         mLoadedBitmap;
    private int            mScreenWidth;
    private int            mScreenHeight;
    private int            mActionBarHeight;
    private int            mOrientation;

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
            Log.d(AIOLog.TAG, "File is not existed!");
            Toast.makeText(this, getString(R.string.image_load_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Uri uri = Uri.fromFile(file);
        mOrientation = Utils.checkOrientation(uri);
        mLoadedBitmap = Utils.decodeBitmapFromCameraIntent(imagePath);

        mFaceViewController = new FaceViewGroup(this);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;

        Log.d("KieuThang", "mOrientation:" + mOrientation);
        if (getActionBar() != null) {
            mActionBarHeight = getActionBar().getHeight();
        }
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
            mFaceViewController.trackballClicked();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initLayout() {
        initLayoutChooseEffects();
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
        mImageViewLoadedBitmap = (ImageView) findViewById(R.id.image_view);
        mLayoutProgress = (RelativeLayout) findViewById(R.id.layout_progress);
        mFaceViewController = (FaceViewGroup) findViewById(R.id.face_view_group);
        mImageViewLoadedBitmap.setVisibility(View.GONE);
        mLayoutProgress.setVisibility(View.VISIBLE);
        if (mLoadedBitmap == null) {
            Toast.makeText(this, getString(R.string.image_load_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AsyncTaskFaceDetection asyncTaskFaceDetection = new AsyncTaskFaceDetection(this);
        asyncTaskFaceDetection.execute();
    }

    private void initLayoutChooseEffects() {
        mContentUpAnime = AnimationUtils.loadAnimation(this, R.anim.layout_content_up);
        mContentDownAnime = AnimationUtils.loadAnimation(this, R.anim.layout_content_down);
        mLayoutChooseEffects = (RelativeLayout) findViewById(R.id.layout_choose_effect_id);
        mLayoutChooseEffects.setVisibility(View.GONE);
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
            int heightOfLayoutEffect = (int) getResources().getDimension(R.dimen.layout_effect_height);
            int heightOfLayoutEffectToPixel = Utils.dpToPixels(ActivityFaceLoader.this, heightOfLayoutEffect);
            Log.d("KieuThang", "heightOfLayoutEffectToPixel:" + heightOfLayoutEffectToPixel + ",heightOfLayoutEffect:" + heightOfLayoutEffect
                    + ",mActionBarHeight:" + mActionBarHeight);

            mRatioX = (float) mScreenWidth / (float) mBitmapWidth;
           // if (mBitmapWidth > mBitmapHeight)
            //    mRatioY = (float) (mScreenHeight - mActionBarHeight) / (float) mBitmapHeight;
           // else
                mRatioY = (float) (mScreenHeight - heightOfLayoutEffectToPixel - mActionBarHeight) / (float) mBitmapHeight;
            AIOLog.d(FunFaceConstant.TAG, "ratioX:" + mRatioX + ",ratioY:" + mRatioY);

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
            mImageViewLoadedBitmap.setVisibility(View.VISIBLE);
            mLayoutProgress.setVisibility(View.GONE);
            mLayoutChooseEffects.setVisibility(View.VISIBLE);
            mLayoutChooseEffects.startAnimation(mContentUpAnime);

            switch (result) {
            case FunFaceConstant.RESULT_FAILED:
                mImageViewLoadedBitmap.setImageBitmap(mLoadedBitmap);
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

                AIOLog.d(FunFaceConstant.TAG, "Confidence: " + confidence + ", Eye distance: " + eyeDistance + ", Mid Point: (" + midPoint.x + ", "
                        + midPoint.y + ")");
                FaceView faceView = new FaceView(R.drawable.pic1, ActivityFaceLoader.this.getResources(), (int) (2 * eyeDistance * mRatioX));

                float scaleX = 1.0F;
                float scaleY = 1.0F;
                float centerX = midPoint.x * mRatioX;
                float centerY = midPoint.y * mRatioY;
                float angle = 0.0F;

                AIOLog.d(FunFaceConstant.TAG, "ratioX:" + mRatioX + ",ratioY:" + mRatioY + ",centerX:" + centerX + ",centerY:" + centerY);
                faceView.setPos(centerX, centerY, scaleX, scaleY, angle);
                mFaceViewController.addFace(faceView, ActivityFaceLoader.this);
            }
            mImageViewLoadedBitmap.setImageBitmap(mResultBitmap);
            Log.d("KieuThang", "mScreenWidth:" + mScreenWidth + ",mScreenHeight:" + mScreenHeight);
            Log.d("KieuThang", "mImageViewLoadedBitmap width:" + mImageViewLoadedBitmap.getWidth() + ",height:" + mImageViewLoadedBitmap.getHeight());
            Log.d("KieuThang", "mResultBitmap width:" + mResultBitmap.getWidth() + ", mResultBitmap height:"
                    + mResultBitmap.getHeight());
            Log.d("KieuThang", "mScreenWidth:" + mImageViewLoadedBitmap.getDrawable().getIntrinsicWidth() + ",mScreenHeight:"
                    + mImageViewLoadedBitmap.getDrawable().getIntrinsicHeight());
        }
    }

    private void saveImageToSdCard(Bitmap bitmap) {
        //      String filepath = Environment.getExternalStorageDirectory()
        //              + "/facedetect" + System.currentTimeMillis() + ".png";
        //
        //      try {
        //          FileOutputStream fos = new FileOutputStream(filepath);
        //
        //          bitmap.compress(CompressFormat.PNG, 100, fos);
        //
        //          fos.flush();
        //          fos.close();
        //      } catch (FileNotFoundException e) {
        //          e.printStackTrace();
        //      } catch (IOException e) {
        //          e.printStackTrace();
        //      }

        ImageView imageView = (ImageView) findViewById(R.id.image_view);

        imageView.setImageBitmap(bitmap);
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
}
