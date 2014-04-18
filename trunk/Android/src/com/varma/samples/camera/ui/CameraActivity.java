package com.varma.samples.camera.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.varma.samples.camera.R;
import com.varma.samples.camera.callback.CameraCallback;
import com.varma.samples.camera.preview.CameraSurface;

@SuppressLint("DefaultLocale")
public class CameraActivity extends Activity implements CameraCallback {
    private FrameLayout cameraholder = null;
    private CameraSurface camerasurface = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        cameraholder = (FrameLayout) findViewById(R.id.camera_preview);

        setupPictureMode();

        ((ImageButton) findViewById(R.id.takepicture)).setOnClickListener(onButtonClick);
        ((ImageButton) findViewById(R.id.about)).setOnClickListener(onButtonClick);
        ((ImageButton) findViewById(R.id.coloreffect)).setOnClickListener(onButtonClick);
        ((ImageButton) findViewById(R.id.whitebalance)).setOnClickListener(onButtonClick);
    }

    private void setupPictureMode() {
        camerasurface = new CameraSurface(this);

        cameraholder.addView(camerasurface, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        camerasurface.setCallback(this);
    }

    @Override
    public void onJpegPictureTaken(byte[] data, Camera camera) {
        Log.d("KieuThang", "onJpegPictureTaken data=" + data.length + " camera=" + camera);
        try
        {
            Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            OutputStream outStream = new FileOutputStream(String.format(Environment.getExternalStorageDirectory().getPath() +File.separator
                   + "%d.jpg", System.currentTimeMillis()));
            outStream.write(data);
            outStream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        camerasurface.startPreview();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
    }

    @Override
    public void onRawPictureTaken(byte[] data, Camera camera) {
        //            Log.d("KieuThang", "onRawPictureTaken data="+data.length+" camera="+camera);
        //            try
        //            {
        //                    FileOutputStream outStream = new FileOutputStream(String.format(Environment.getExternalStorageDirectory().getPath()+
        //                                    "%d.jpg", System.currentTimeMillis()));
        //                    
        //                    outStream.write(data);
        //                    outStream.close();
        //            }
        //            catch(Exception e)
        //            {
        //                    e.printStackTrace();
        //            }
        //            
        //            camerasurface.startPreview();
    }

    @Override
    public void onShutter() {
    }

    @Override
    public String onGetVideoFilename() {
        String filename = String.format(Environment.getExternalStorageDirectory().getPath() +
                "%d.3gp", System.currentTimeMillis());

        return filename;
    }

    private void displayAboutDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Sample application to demonstrate the use of Camera in Android\n\nVisit www.krvarma.com for more information.");

        builder.show();
    }

    private void displayColorEffectDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.color_effect));
        builder.setSingleChoiceItems(camerasurface.getSupportedColorEffects(),
                camerasurface.getCurrentColorEffect(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        camerasurface.setColorEffect(which);

                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    private void displayWhiteBalanceDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.white_balance));
        builder.setSingleChoiceItems(camerasurface.getSupportedWhiteBalances(),
                camerasurface.getCurrentWhiteBalance(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        camerasurface.setWhiteBalance(which);

                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    private View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
            case R.id.about:
                displayAboutDialog();
                break;
            case R.id.takepicture:
                camerasurface.startTakePicture();
                break;
            case R.id.coloreffect:
                displayColorEffectDialog();
                break;
            case R.id.whitebalance:
                displayWhiteBalanceDialog();
                break;
            }
        }
    };
}