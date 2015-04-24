package com.visva.android.app.funface.view.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenuItem;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.log.AIOLog;
import com.visva.android.app.funface.utils.StringUtility;
import com.visva.android.app.funface.utils.Utils;

public class ActivityFaceLoader extends VisvaAbstractActivity {
    //=========================Define Constant================
    //=========================Control Constant===============
    private ImageView      mImageViewLoadedBitmap;
    private RelativeLayout mLayoutProgress;
    private RelativeLayout mLayoutChooseEffects;
    private SatelliteMenu  mSatelliteMenu;
    private Animation      mContentUpAnime;
    private Animation      mContentDownAnime;
    //=========================Variable Constant==============
    private Bitmap         mLoadedBitmap;

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
    }

    private void initLayout() {
        initLayoutChooseEffects();

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
        mSatelliteMenu = (SatelliteMenu) findViewById(R.id.satelite_menu);
        mSatelliteMenu.setCloseItemsOnClick(true);
//        mSatelliteMenu.setMainImage(R.drawable.ic_launcher);
        mSatelliteMenu.setExpandDuration(500);
        mSatelliteMenu.setSatelliteDistance(150);
        mSatelliteMenu.setTotalSpacingDegree(85);
        mLayoutChooseEffects.setVisibility(View.GONE);

        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.emoji_1f602_32));
        items.add(new SatelliteMenuItem(2, R.drawable.pic1));
        items.add(new SatelliteMenuItem(3, R.drawable.emoji_1f1fa_1f1f8_32));
        items.add(new SatelliteMenuItem(4, R.drawable.emoji_1f47f_32));
        mSatelliteMenu.addItems(items);

        mSatelliteMenu.setOnItemClickedListener(new SateliteClickedListener() {

            public void eventOccured(int id) {
                Log.i("sat", "Clicked on " + id);
            }
        });
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

            mCanvas.setBitmap(mResultBitmap);
            mCanvas.drawBitmap(mLoadedBitmap, 0, 0, ditherPaint);

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
                mCanvas.drawBitmap(bitmap, src, dst, drawPaint);
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

}
