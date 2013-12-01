package vn.com.shoppie.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.content.Loader;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Utils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	public static String formatMoney(int money) {
		if(money <= 0)
			return "0";
		String result = "";
		int size = 0;
		int temp = money;
		while (temp > 0) {
			if(size > 0 && size % 3 == 0)
				result = "." + result;
			result = (temp % 10) + result;
			temp /= 10;
			size ++;
		}
		return result;
	}
	
	public static double haversine(
	        double lat1, double lng1, double lat2, double lng2) {
	    int r = 6371; // average radius of the earth in km
	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lng2 - lng1);
	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	       Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
	      * Math.sin(dLon / 2) * Math.sin(dLon / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double d = r * c;
	    return d;
	}
	
	public static double calculationByDistance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        float results[] = new float[2];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] / 1000;
     }
}