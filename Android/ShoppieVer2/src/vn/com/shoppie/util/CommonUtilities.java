package vn.com.shoppie.util;

import vn.com.shoppie.constant.GlobalValue;
import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	public static String regId="";
	public static String name="";
	public static String email="";
	
	
	// give your server registration url here
//    static final String SERVER_URL = "http://192.168.71.1/gcm_server_php/register.php"; 

    // Google project id
//    static final String SENDER_ID1 = "622843213634"; 
//    static final String REG_ID1="APA91bHm4TwVO18ZTW5ZlC9TPjw7Gggo1gBzbP1Z6O8wMer_kcJvPc5xmg-leU2pICu87YuLtkCZK9axjDSs2ZtdNVyMeOIRIGFv9ty7f8Itkztgw0qp-CJQQtWzg5_O4zf4n62FQikQFIMK3QJLliHaaBsvarppXbQNiFGb8cgbtgUvreoQJ74";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";


    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(GlobalValue.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(GlobalValue.EXTRA_TYPE, message);
        context.sendBroadcast(intent);
    }
    public static void displayMessage(Context context, Intent intent) {
//        Intent intent = new Intent(GlobalValue.DISPLAY_MESSAGE_ACTION);
        intent.setAction(GlobalValue.DISPLAY_MESSAGE_ACTION);
//        intent.putExtra(GlobalValue.EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
