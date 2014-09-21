package com.sharebravo.bravo.view.lib.gifanimation;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sharebravo.bravo.R;

public class ActivityGIFAnimation extends Activity {
    private static final long TIME_TO_FINISH = 14500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GIFView(this));
    }

    private class GIFView extends View {

        private Movie       mMovie;
        private InputStream mInputStream = null;
        private long        mMovieStart;

        public GIFView(Context context) {
            super(context);
            mInputStream = context.getResources().openRawResource(R.drawable.bravo_jump);
            mMovie = Movie.decodeStream(mInputStream);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            long now = android.os.SystemClock.uptimeMillis();
            if (mMovieStart == 0) {
                mMovieStart = now;
            }
            int relTime = (int) ((now - mMovieStart) % mMovie.duration());
            Log.d("KieuThang", "relTime1:" + relTime + ", movie1.duration():"
                    + mMovie.duration() + ", moviestart1:" + mMovieStart);
            if (relTime > TIME_TO_FINISH) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                // finish();
            }
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, 80);
            this.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}