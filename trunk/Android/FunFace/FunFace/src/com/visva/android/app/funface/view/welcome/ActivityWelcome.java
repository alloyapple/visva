package com.visva.android.app.funface.view.welcome;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.utils.FileUtils;
import com.visva.android.app.funface.view.activity.ActivityFaceLoader;

public class ActivityWelcome extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final int         SLIDING_TIME         = 3000;
    private static final int         REQUEST_CODE_CAMERA  = 100;
    private static final int         REQUEST_CODE_GALLERY = 101;
    private String                   mCurrentPhotoPath;
    private SliderLayout             mIntrodutionSlider;
    private HashMap<String, Integer> mWelcomeScreenMap    = new HashMap<String, Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mIntrodutionSlider = (SliderLayout) findViewById(R.id.slider);
        initSlider();
    }

    private void initSlider() {
        mWelcomeScreenMap.put("Hannibal", R.drawable.s01);
        mWelcomeScreenMap.put("Big Bang Theory", R.drawable.s02);
        mWelcomeScreenMap.put("House of Cards", R.drawable.s03);
        mWelcomeScreenMap.put("Game of Thrones", R.drawable.s04);

        for (String name : mWelcomeScreenMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView.description(name).image(mWelcomeScreenMap.get(name)).setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mIntrodutionSlider.addSlider(textSliderView);
        }
        mIntrodutionSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mIntrodutionSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mIntrodutionSlider.setCustomAnimation(new DescriptionAnimation());
        mIntrodutionSlider.setDuration(SLIDING_TIME);
        mIntrodutionSlider.addOnPageChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_CAMERA == requestCode) {
            FileUtils.galleryAddPic(ActivityWelcome.this, mCurrentPhotoPath);
            sendIntentToActivityFaceLoader(mCurrentPhotoPath, REQUEST_CODE_CAMERA);
        } else if (REQUEST_CODE_GALLERY == requestCode) {
            if (data == null || resultCode == RESULT_CANCELED)
                return;
            Uri uri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(imagePath);
            if (file.exists()) {
                sendIntentToActivityFaceLoader(imagePath, REQUEST_CODE_GALLERY);
            }
        }
    }

    private void sendIntentToActivityFaceLoader(String imagePath, int requestCode) {
        Intent intent = new Intent(this, ActivityFaceLoader.class);
        intent.putExtra(FunFaceConstant.EXTRA_IMAGE_PATH, imagePath);
        startActivity(intent);
    }

    public void onClickOpenCamera(View v) {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            f = FileUtils.setUpPhotoFile(this, mCurrentPhotoPath);
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }
        startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
    }

    public void onClickOpenGallery(View v) {
        // when user click gallery to get image
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mIntrodutionSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIntrodutionSlider != null)
            mIntrodutionSlider.startAutoCycle();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
