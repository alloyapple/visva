package com.visva.android.app.funface.constant;

public class FunFaceConstant {

    public static final boolean DEBUG_MODE          = false;
    public static final String  TAG                 = "FunFace";
    public static final int     MAX_FACES           = 10;
    public static final int     TEXTSIZE_DEFAULT    = 35;

    //EXTRAS
    public static final String  EXTRA_IMAGE_PATH    = "extra_image_path";
    public static final String  EXTRA_BITMAP        = "extra_bitmap";
    public static final String  EXTRA_REQUEST_CODE  = "extra_request_code";

    //result
    public static final int     RESULT_FAILED       = 0;
    public static final int     RESULT_SUCCESS      = 1;

    public static final String  ACTION_SCAN_FILE    = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";

    public static final String  JPEG_FILE_PREFIX    = "IMG_";
    public static final String  JPEG_FILE_SUFFIX    = ".jpg";

    // effects
    public static final int     EFFECT_NONE         = 0;
    public static final int     EFFECT_GREY         = EFFECT_NONE + 1;
    public static final int     EFFECT_GAMMA        = EFFECT_GREY + 1;
    public static final int     EFFECT_RED          = EFFECT_GAMMA + 1;
    public static final int     EFFECT_BLUE         = EFFECT_RED + 1;
    public static final int     EFFECT_GREEN        = EFFECT_BLUE + 1;
    public static final int     EFFECT_SEPIA        = EFFECT_GREEN + 1;
    public static final int     EFFECT_DEPTH        = EFFECT_SEPIA + 1;
    public static final int     EFFECT_CONTRAST     = EFFECT_DEPTH + 1;
    public static final int     EFFECT_BRIGTHT      = EFFECT_CONTRAST + 1;
    public static final int     EFFECT_GAUSSIN      = EFFECT_BRIGTHT + 1;
    public static final int     EFFECT_SHARP        = EFFECT_GAUSSIN + 1;
    public static final int     EFFECT_MEAN_REMOVAL = EFFECT_SHARP + 1;
    public static final int     EFFECT_SMOOTH       = EFFECT_MEAN_REMOVAL + 1;
    public static final int     EFFECT_EMBOSS       = EFFECT_SMOOTH + 1;
    public static final int     EFFECT_ENGRAVE      = EFFECT_EMBOSS + 1;
    public static final int     EFFECT_BOOST        = EFFECT_ENGRAVE + 1;
    public static final int     EFFECT_ROUND_CORNER = EFFECT_BOOST + 1;
    public static final int     EFFECT_TINT         = EFFECT_ROUND_CORNER + 1;
    public static final int     EFFECT_FLEA         = EFFECT_TINT + 1;
    public static final int     EFFECT_BLACK        = EFFECT_FLEA + 1;
    public static final int     EFFECT_SNOW         = EFFECT_BLACK + 1;
    public static final int     EFFECT_SATUARATION  = EFFECT_SNOW + 1;
    public static final int     EFFECT_HUE          = EFFECT_SATUARATION + 1;

}
