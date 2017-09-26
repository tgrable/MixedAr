package com.ar.trekk.markerlessar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;

import java.io.IOException;

public class MainARActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final String TAG = "TrekkAdmin";
    ArchitectView mArchitectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ar);

        mArchitectView = (ArchitectView) findViewById(R.id.architectView);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainARActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainARActivity.this, Manifest.permission.CAMERA)) {
                Log.d(TAG, "1");

            } else {
                ActivityCompat.requestPermissions(MainARActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                Log.d(TAG, "2");
            }
        }

        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey("PKEsuNcLgH6OUCEk3fGue3UFD4H6SA99OMsCqXSozZo6PUcvE8F1LX9AQS1M6xAa8V0w+9l/g8m/F0mRCyz6JWk684/NN0HvQIYqy6eb5dX312AO9o8bLpZAA8cPd6S0AN+alQgHnXVdlvWHSU9pOW0CVxejRP6ck2A3p4b9351TYWx0ZWRfXz3Azq7EUTaqzdFT18XPXg2kClHoEfjE829wNe4vnPDpzy68Do6NmwunF/V+bwFRoCKKPFnJxKWvNynIKdDZGVYyeeTzqLeq8R4oPurllGjPgHigcXOjfDsb+qScPQMLU7cG9hM2LExpk1aqqI4djDv1b7zg+YtsYZcx1eopCNL1wanz21oeRlV5FckAvQcdVsQQniB2P6JjTyczNdrBhiSEqec+SQd7sUhG3a01dGMLKcLSr0J1TWGs2na0IZXBq2rGS2F+G5pIFgMQd4krH08D4nsD0KHsjh6h3OgBprjOVPRETbgSyR3dKfIMYsk364qMZc+g9yjmhb7snVozQwv5njTMsYuK8ndIPc/gZx9w6zNQ8SCBkN2JxhYj79Vw1t750RnEzQRgk1qsQx4/Ip0wPDx9HEEdzA4lnCxGONEWdnsJ9CQl1ElBNWa2SlV6l6pb+vi2AeC5Gc83LSq1EBb6ne9XAUN2AI6U+dNhms5HYRctfm7nndA=");


        try {
//            this.mArchitectView.onCreate(startupConfiguration);
            this.mArchitectView.onCreate( config );
        } catch (RuntimeException e) {
            Toast.makeText(getApplicationContext(), "can't create Architect View", Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), "Exception in ArchitectView.onCreate()", e);
        }


        mArchitectView.registerUrlListener(new ArchitectView.ArchitectUrlListener() {
            @Override
            public boolean urlWasInvoked(String url) {
                Log.d("TimGrable url ", url);
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if ( mArchitectView != null ) {
            try {
                mArchitectView.onPostCreate();
                if (haveNetworkConnection()) {
                    mArchitectView.load("index.html");
//                mArchitectView.load("assets/index.html");
                }
                else {
                    Toast.makeText(getApplicationContext(), "You must have an internet connection to use this feature.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if ( mArchitectView != null ) {
            mArchitectView.onResume();

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ( mArchitectView != null ) {
            mArchitectView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( mArchitectView != null ) {
            mArchitectView.onDestroy();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
