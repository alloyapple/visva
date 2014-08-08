package com.visva.android.visvasdklibrary.util.location.svc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;

import com.visva.android.visvasdklibrary.R;
import com.visva.android.visvasdklibrary.log.AIOLog;

public class AddressLoader {
    private Context mContext;
    private TextView mAddress;

    public AddressLoader(Context context) {
        mContext = context;
    }

    public void loadAddress(Location location, TextView txtView) {
        mAddress = txtView;
        new AddressLoaderTask(mContext).execute(location);
    }

    protected class AddressLoaderTask extends AsyncTask<Location, Void, String> {
        private Context mLocalContext;

        public AddressLoaderTask(Context context) {
            super();
            mLocalContext = context;
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(mLocalContext, Locale.getDefault());
            Location loc = params[0];
            List<Address> listAdd = null;
            // Try to get an address for the current location. Catch IO or
            // network problems.
            try {
                listAdd = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
            } catch (IOException ex) {
                AIOLog.d("IOException occured when loading the address");
                ex.printStackTrace();
                return (mLocalContext.getString(R.string.IO_Exception_getFromLocation));
            } catch (IllegalArgumentException illegalException) {
                AIOLog.d("Illegal Exception occured");
                String errorString = mLocalContext.getString(R.string.illegal_argument_exception, loc.getLatitude(),
                        loc.getLongitude());
                illegalException.printStackTrace();
                return errorString;
            }

            if (listAdd != null && listAdd.size() > 0) {
                Address add = listAdd.get(0);
                String addressText = mLocalContext.getString(
                        R.string.address_output_string,
                        add.getMaxAddressLineIndex() > 0 ? add
                                .getAddressLine(0) : "", add.getLocality(), add
                                .getCountryName());
                return addressText;
            } else {
                return mLocalContext.getString(R.string.no_address_found);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mAddress.setText(result);
        }

    }
}
