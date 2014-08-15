
package com.samsung.android.alwayssocial.receiver;

import com.samsung.android.alwayssocial.service.AlwaysService;

import sstream_new.lib.constants.StreamIds;
import sstream_new.lib.constants.StreamProviderConstants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlwaysBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "AlwaysBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action != null) {

            if ("sstream.app.broadcast.SEND_UPDATE".equals(action)) {
                if (intent.getExtras() != null){
                	String extra = intent.getExtras().getString(StreamProviderConstants.BROADCAST_EXTRA_STREAM_ID);
					Log.d(TAG, "sstream.app.broadcast.SEND_UPDATE, streamId : " + extra);
					if(StreamIds.SAMSUNG_SOCIAL.equals(extra)) {
			            Intent serviceIntent = new Intent(context, AlwaysService.class); 
			            serviceIntent.putExtra("action", "update");
                        serviceIntent.putExtra("stream_id", extra);
			            context.startService(serviceIntent);
					}
                } 
                else {
                    Log.d(TAG, "sstream.app.broadcast.SEND_UPDATE : no extra field, Do nothing!!");
                }
            }
        } else {
            Log.e(TAG, "action is NULL");
        }
    }
}
