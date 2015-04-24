package com.visva.android.app.funface.constant;

public class FunFaceConstant {

    public static final boolean DEBUG_MODE         = true;
    public static final String  TAG                = "FunFace";
    public static final int     MAX_FACES          = 10;

    //EXTRAS
    public static final String  EXTRA_IMAGE_PATH   = "extra_image_path";
    public static final String  EXTRA_BITMAP       = "extra_bitmap";
    public static final String  EXTRA_REQUEST_CODE = "extra_request_code";

    //result
    public static final int     RESULT_FAILED      = 0;
    public static final int     RESULT_SUCCESS     = 1;

    public static final String  ACTION_SCAN_FILE   = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";

    public static final String  JPEG_FILE_PREFIX   = "IMG_";
    public static final String  JPEG_FILE_SUFFIX   = ".jpg";
}
