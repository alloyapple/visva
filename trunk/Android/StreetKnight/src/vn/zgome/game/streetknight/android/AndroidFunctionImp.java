package vn.zgome.game.streetknight.android;

import vn.zgome.game.streetknight.core.AndroidFunction;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.Toast;

public class AndroidFunctionImp extends AndroidFunction{
	Context ctx;
	Vibrator v;
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Toast.makeText(ctx,toast, Toast.LENGTH_LONG).show();
					break;
				case 1:
							new AlertDialog.Builder(ctx)
									.setMessage(title)
									.setPositiveButton("Đồng ý", clickYes)
									.setNegativeButton("Hủy bỏ",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,int which) {
													dialog.cancel();
												}
											}).create().show();
										
					break;
				}
		}
	};
	
	SharedPreferences sp;
	
	public AndroidFunctionImp(Context ctx)
	{
		this.ctx = ctx;
		v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
		sp = ctx.getSharedPreferences("streetkungfu", 0);
	}
	
	@Override
	public void showToast(String info) {
		// TODO Auto-generated method stub		
		toast = info;
		myHandler.sendEmptyMessage(0);
	}
	String toast,title;
	OnClickListener clickYes;

	@Override
	public void showDialog(String title, final Action yes) {
		// TODO Auto-generated method stub
		this.title = title;
		this.clickYes = new OnClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				yes.action();
			}
		};
		myHandler.sendEmptyMessage(1);
	}

	

	@Override
	public void vibrate(int delay) {
		// TODO Auto-generated method stub
		v.vibrate(delay);
	}

	
	public String getString(String key, String df)
	{
		return sp.getString(key, df);
	}
	
	public boolean getBoolean(String key, boolean df)
	{
		return sp.getBoolean(key, df);
	}
	
	public int getInteger(String key, int df)
	{
		return sp.getInt(key, df);
	}

	@Override
	public void putInteger(String key, int put) {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, put);
		editor.commit();
	}

	@Override
	public void putBoolean(String key, boolean put) {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, put);
		editor.commit();
	}

	@Override
	public void putString(String key, String put) {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, put);
		editor.commit();
	}
}
