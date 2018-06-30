package g.rezza.moch.unileverapp.lib;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;
import java.util.Locale;


/**
 * Created by Rezza on 2/1/2017.
 */

public class    GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    Location location;

    double latitude; // latitude
    double longitude; // longitude
    String mAddress = "Lokasi tidak diketahui";
    String city    = "Lokasi tidak diketahui";

    private static final String TAG = "DEBUG";

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    protected LocationManager locationManager;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {
        Log.d(TAG, "Start getLocation");
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // Get Status GPS enable
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Get Network Status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled) {
                Log.d(TAG, "Disabled isNetworkEnabled & isGPSEnabled");
                showAlertSetting();
            } else {
                Log.d(TAG, "isNetworkEnabled & isGPSEnabled is Enabled");
                this.canGetLocation = true;
                Log.d(TAG, "isNetworkEnabled : "+ isNetworkEnabled);

                ActivityCompat.requestPermissions(((Activity)mContext),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                ActivityCompat.requestPermissions(((Activity)mContext),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                        }
                    }
                }
                Log.d(TAG, "isGPSEnabled : "+ isGPSEnabled);
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            location.getLongitude();
        }
        return longitude;
    }

    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public void showAlertSetting() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS Setting");
        alertDialog.setMessage("GPS is not enabled. Do you want to setting?");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public String getAddress() {
        if (location != null ){
            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
            List<Address> address;
            try {
                address = gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(address.size()> 0){
                    mAddress = address.get(0).getAddressLine(0);
                }

            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }
        }
        return mAddress;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public String getCity() {
        if (location != null ){
            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
            List<Address> address;
            try {
                address = gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(address.size()> 0){
                    city = address.get(0).getLocality();
                }

            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }
        }
        return city;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"LocationChanged -> Accuracy : " +location.getAccuracy()+" |Speed :" +location.getSpeed() );
        if (pListenr != null){
            pListenr.onChange(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (location != null){
            try {
                Log.d(TAG,"onStatusChanged : " + location.getLongitude());
            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }

        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG,"onProviderEnabled : " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    LocationChangeListenr pListenr;
    public void setLocationChangeListenr(LocationChangeListenr mListenr){
        pListenr = mListenr;
    }
    public interface LocationChangeListenr{
        public void onChange(Location location);
    }
}
