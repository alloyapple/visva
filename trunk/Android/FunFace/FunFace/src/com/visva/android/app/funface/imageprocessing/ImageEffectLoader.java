package com.visva.android.app.funface.imageprocessing;

import com.visva.android.app.funface.constant.FunFaceConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageEffectLoader {
    private static ImageEffectLoader mInstance;
    private Context                  mContext;

    public static ImageEffectLoader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImageEffectLoader(context);
        }
        return mInstance;
    }

    public ImageEffectLoader(Context context) {
        this.mContext = context;
    }

    public void displayImage(ImageView imageView, int position, Bitmap baseBitmap) {
        ImageEffectLoaderAsyncTask imageEffectLoaderAsyncTask = new ImageEffectLoaderAsyncTask(mContext, imageView, position, baseBitmap);
        imageEffectLoaderAsyncTask.execute();
    }

    class ImageEffectLoaderAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        private ImageView mImageView;
        private int       mPosition;
        private Bitmap    mBaseBitmap;

        public ImageEffectLoaderAsyncTask(Context context, ImageView imageView, int position, Bitmap baseBitmap) {
            this.mImageView = imageView;
            this.mPosition = position;
            this.mBaseBitmap = baseBitmap;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return onLoadEffectedBitmap(mPosition, mBaseBitmap);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null)
                mImageView.setImageBitmap(result);
        }
    }

    private Bitmap onLoadEffectedBitmap(int position, Bitmap baseBitmap) {
        switch (position) {
        case FunFaceConstant.EFFECT_NONE:
            return baseBitmap;

        case FunFaceConstant.EFFECT_GREY:
            return ImageProcessor.doGreyscale(baseBitmap);

        case FunFaceConstant.EFFECT_GAMMA:
            return ImageProcessor.doGamma(baseBitmap, 0.6, 0.6, 0.6);// (1.8, 1.8, 1.8)

        case FunFaceConstant.EFFECT_RED:
            return ImageProcessor.doColorFilter(baseBitmap, 1, 0, 0);

        case FunFaceConstant.EFFECT_BLUE:
            return ImageProcessor.doColorFilter(baseBitmap, 0, 1, 0);

        case FunFaceConstant.EFFECT_GREEN:
            return ImageProcessor.doColorFilter(baseBitmap, 0.5, 0.5, 0.5);

        case FunFaceConstant.EFFECT_SEPIA:
            return ImageProcessor.createSepiaToningEffect(baseBitmap, 50, 0, 1, 0);

        case FunFaceConstant.EFFECT_DEPTH:
            return ImageProcessor.decreaseColorDepth(baseBitmap, 32);//64/128

        case FunFaceConstant.EFFECT_CONTRAST:
            return ImageProcessor.createContrast(baseBitmap, 50);//100

        case FunFaceConstant.EFFECT_BRIGTHT:
            return ImageProcessor.doBrightness(baseBitmap, 50);//100

        case FunFaceConstant.EFFECT_GAUSSIN:
            return ImageProcessor.applyGaussianBlur(baseBitmap);

        case FunFaceConstant.EFFECT_SHARP:
            return ImageProcessor.sharpen(baseBitmap, 1);

        case FunFaceConstant.EFFECT_MEAN_REMOVAL:
            return ImageProcessor.applyMeanRemoval(baseBitmap);

        case FunFaceConstant.EFFECT_SMOOTH:
            return ImageProcessor.smooth(baseBitmap, 5);

        case FunFaceConstant.EFFECT_EMBOSS:
            return ImageProcessor.emboss(baseBitmap);

        case FunFaceConstant.EFFECT_ENGRAVE:
            return ImageProcessor.engrave(baseBitmap);

        case FunFaceConstant.EFFECT_BOOST:
            return ImageProcessor.boost(baseBitmap, 1, 150);

        case FunFaceConstant.EFFECT_ROUND_CORNER:
            return ImageProcessor.roundCorner(baseBitmap, 5);

        case FunFaceConstant.EFFECT_TINT:
            return ImageProcessor.tintImage(baseBitmap, 50);

        case FunFaceConstant.EFFECT_FLEA:
            return ImageProcessor.applyFleaEffect(baseBitmap);

        case FunFaceConstant.EFFECT_BLACK:
            return ImageProcessor.applyBlackFilter(baseBitmap);

        case FunFaceConstant.EFFECT_SNOW:
            return ImageProcessor.applySnowEffect(baseBitmap);

        case FunFaceConstant.EFFECT_SATUARATION:
            return ImageProcessor.applySaturationFilter(baseBitmap, 1);

        case FunFaceConstant.EFFECT_HUE:
            return ImageProcessor.applyHueFilter(baseBitmap, 5);

        default:
            return baseBitmap;

        }
    }
}
