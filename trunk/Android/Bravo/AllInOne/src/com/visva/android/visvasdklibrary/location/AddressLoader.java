package com.visva.android.visvasdklibrary.location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

public class AddressLoader {
    private Context          mContext;
    private IAddressListener iAddressListener;

    public AddressLoader(Context context) {
        mContext = context;
    }

    public void loadAddress(Location location, IAddressListener iAddressListener) {
        this.iAddressListener = iAddressListener;
        new AddressLoaderTask(mContext).execute(location);
    }

    protected class AddressLoaderTask extends AsyncTask<Location, Void, Address> {
        private Context mLocalContext;

        public AddressLoaderTask(Context context) {
            super();
            mLocalContext = context;
        }

        @Override
        protected Address doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(mLocalContext, Locale.getDefault());
            Location loc = params[0];
            List<Address> listAdd = null;
            // Try to get an address for the current location. Catch IO or
            // network problems.
            try {
                listAdd = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } catch (IllegalArgumentException illegalException) {
                illegalException.printStackTrace();
                return null;
            }

            if (listAdd != null && listAdd.size() > 0) {
                Address address = listAdd.get(0);
                if (address == null)
                    return null;
                return address;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            iAddressListener.onResponse(address);
        }

    }
}
