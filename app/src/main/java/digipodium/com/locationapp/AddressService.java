package digipodium.com.locationapp;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddressService extends IntentService {

    private ResultReceiver receiver;

    public AddressService() {
        super("address_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // get data from intent i.e., latitude,longitude
        if (intent != null) {
            Bundle extras = intent.getExtras();
            double lat = extras.getDouble("latitude", -1);
            double lng = extras.getDouble("longitude", -1);
            receiver = extras.getParcelable("receiver");
            if (lat != -1 && lng != -1) {
                Geocoder geocoder = new Geocoder(this,
                        Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(lat, lng, 2);
                    if (addressList != null) {
                        sendResults("found the address",addressList);
                    } else {
                        sendResults("failed to get address", null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    sendResults(e.getMessage(),null);
                }
            }

        }
    }

    @SuppressLint("RestrictedApi")
    private void sendResults(String msg, List<Address> addresses) {
        if (addresses!=null){
            Bundle data = new Bundle();
            data.putParcelable("address1", addresses.get(0));
            data.putParcelable("address2", addresses.get(1));
            receiver.send(RESULT_OK,data);
        }else{
            Bundle data = new Bundle();
            data.putString("error", msg);
            receiver.send(RESULT_CANCELED,data);
        }
    }
}
