package com.example.ailatrieuphu_visva.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import com.example.ailatrieuphu_visva.facebook.AsyncFacebookRunner.RequestListener;
import android.util.Log;

/**
 * Skeleton base class for RequestListeners, providing default error 
 * handling. Applications should handle these error conditions.
 *
 */
public abstract class BaseRequestListener implements RequestListener {

    public void onFacebookError(FacebookError e) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onFileNotFoundException(FileNotFoundException e) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onIOException(IOException e) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }

    public void onMalformedURLException(MalformedURLException e) {
        Log.e("Facebook", e.getMessage());
        e.printStackTrace();
    }
    
}
