package vn.com.shoppie.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SBroastcastProvider {
	Context context;
	public SBroastcastProvider(Context context){
		this.context=context;
	}
	/**
	 * Receiving push messages
	 * */
	public BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(_listener!=null){
				_listener.onReceiveBroastcast(context, intent);
			}
		}
	};
	public BroadcastReceiver getBroastcast(){
		return mHandleMessageReceiver;
	}
	BroastcastListener _listener;
	public void setOnBroastcastListener(BroastcastListener listener){
		this._listener=listener;
	}
	public interface BroastcastListener{
		public void onReceiveBroastcast(Context context,Intent intent);
	}
}
