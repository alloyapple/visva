package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbObject;
import vn.com.shoppie.util.SUtilBitmap;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.graphics.Bitmap;

@SuppressWarnings("serial")
public class ShoppieObject extends adbObject {
	public Object tag;
	public ShoppieObject(int index, String[] values) {
		super(index, values);
	}

	public Bitmap getBitmap(Context context, String link) {
		if (link == null)
			return null;
		if (!link.contains("http://")) {
			link = WebServiceConfig.HEAD_IMAGE + link;
		}

		// return SUtil.getInstance().getImageAsync(context, link);
		return SUtilBitmap.getInstance(context).getBitmap(context, link);
	}
	
	public Bitmap getBitmap(Context context,String link,int width,int height){
		if (link == null)
			return null;
		if (!link.contains("http://")) {
			link = WebServiceConfig.HEAD_IMAGE + link;
		}

		// return SUtil.getInstance().getImageAsync(context, link);
		return SUtilBitmap.getInstance(context).getBitmap(context, link,width,height);
	}
	
	public interface ShoppieObjectColumn {
		public static final String _ID = "_id";
	}

	@Override
	public String[] getValues() {
		return values;
	}
}
