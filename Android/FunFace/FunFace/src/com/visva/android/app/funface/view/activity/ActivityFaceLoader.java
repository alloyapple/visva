package com.visva.android.app.funface.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import com.visva.android.app.funface.view.widget.FaceViewController;

@SuppressLint("ClickableViewAccessibility")
public class ActivityFaceLoader extends VisvaAbstractActivity {
    //=========================Define Constant================
    //=========================Control Constant===============
    private ImageView          mImageViewLoadedBitmap;
    private RelativeLayout     mLayoutProgress;
    private RelativeLayout     mLayoutChooseEffects;
    private Button             mBtnAddEffects;
    private Animation          mContentUpAnime;
    private Animation          mContentDownAnime;
    private RelativeLayout     mRootView;
    //=========================Variable Constant==============
    private FaceViewController mFaceViewController;
    private Bitmap             mLoadedBitmap;

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
        mLoadedBitmap = Utils.decodeBitmapFromCameraIntent(imagePath);
        mFaceViewController = new FaceViewController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFaceViewController.loadImages(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFaceViewController.unloadImages();
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
        private FaceDetector detector;
        private int          mBitmapWidth;
        private int          mBitmapHeight;
        private Canvas       mCanvas;
        private Bitmap       mResultBitmap;

        public AsyncTaskFaceDetection(Context context) {
            mBitmapWidth = mLoadedBitmap.getWidth();
            mBitmapHeight = mLoadedBitmap.getHeight();

            detector = new FaceDetector(mBitmapWidth, mBitmapHeight, FunFaceConstant.MAX_FACES);
            faces = new Face[FunFaceConstant.MAX_FACES];

            mCanvas = new Canvas();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            mResultBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.RGB_565);
            Paint ditherPaint = new Paint();
            Paint drawPaint = new Paint();

            ditherPaint.setDither(true);
            drawPaint.setColor(Color.RED);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(2);
            if (mResultBitmap != null && !mResultBitmap.isRecycled()) {
                mCanvas.setBitmap(mResultBitmap);
                mCanvas.drawBitmap(mLoadedBitmap, 0, 0, ditherPaint);
            }
            int facesFound = detector.findFaces(mResultBitmap, faces);

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

                AIOLog.d(FunFaceConstant.TAG, "Confidence: " + confidence
                        + ", Eye distance: " + eyeDistance
                        + ", Mid Point: (" + midPoint.x + ", " + midPoint.y
                        + ")");
                FaceView faceView = new FaceView(R.drawable.pic1, getResources());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
                Paint drawPaint = new Paint();
                drawPaint.setAntiAlias(true);

                Log.d("FaceDetector", "bitmap:" + bitmap);
                Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect dst = new Rect(
                        (int) (midPoint.x - eyeDistance * 2.0f),
                        (int) (midPoint.y - eyeDistance * 2.0f),
                        (int) (midPoint.x + eyeDistance * 2.0f),
                        (int) (midPoint.y + eyeDistance * 2.0f));
                faceView.setPos(dst);
                mFaceViewController.addFace(faceView);
            }
            if (result > 0) {
                mFaceViewController.draw(mCanvas);
            }
            mImageViewLoadedBitmap.setImageBitmap(mResultBitmap);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mFaceViewController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
