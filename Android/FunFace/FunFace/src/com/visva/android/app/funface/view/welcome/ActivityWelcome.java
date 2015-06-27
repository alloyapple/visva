package com.visva.android.app.funface.view.welcome;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.log.AIOLog;
import com.visva.android.app.funface.photointent.AlbumStorageDirFactory;
import com.visva.android.app.funface.photointent.BaseAlbumDirFactory;
import com.visva.android.app.funface.photointent.FroyoAlbumDirFactory;
import com.visva.android.app.funface.view.activity.ActivityFaceLoader;

public class ActivityWelcome extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final int       REQUEST_CODE_CAMERA     = 100;
    private static final int       REQUEST_CODE_GALLERY    = 101;
    private String                 mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private SliderLayout           mIntrodutionSlider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        mIntrodutionSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.hannibal);
        file_maps.put("Big Bang Theory", R.drawable.bigbang);
        file_maps.put("House of Cards", R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.bigbang);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView.description(name).image(file_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mIntrodutionSlider.addSlider(textSliderView);
        }
        mIntrodutionSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mIntrodutionSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mIntrodutionSlider.setCustomAnimation(new DescriptionAnimation());
        mIntrodutionSlider.setDuration(3000);
        mIntrodutionSlider.addOnPageChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("KieuThang", "onActivityResult:" + requestCode + ",data:" + data);
        if (REQUEST_CODE_CAMERA == requestCode) {
            galleryAddPic();
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
        Log.d("KieuThang", "requestCode:" + requestCode);
        Log.d("KieuThang", "mCurrentPhotoPath:" + imagePath);
        intent.putExtra(FunFaceConstant.EXTRA_IMAGE_PATH, imagePath);
        startActivity(intent);
    }

    public void onClickOpenCamera(View v) {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }
        startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = FunFaceConstant.JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, FunFaceConstant.JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        AIOLog.d(FunFaceConstant.TAG, "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            AIOLog.d(FunFaceConstant.TAG, "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(FunFaceConstant.ACTION_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
