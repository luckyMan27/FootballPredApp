package com.fortunato.footballpredictions.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Geocoder;
import android.widget.Toast;


import com.fortunato.footballpredictions.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class StadiumActivity extends AppCompatActivity implements SensorEventListener {
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private Location current;
    private Location stadium = null;

    private TextView text_cmp;
    private TextView text_dist;
    private ImageView arrow;
    private ImageView rose;

    private String distance = "Wait to calculate";

    private Address stadium_addr;

    private FusedLocationProviderClient fusedLocationClient;

    private String venue;
    LocationRequest locationRequest;

    LocationCallback call;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        Intent intent = getIntent();

        venue = intent.getStringExtra("venue");

        arrow = (ImageView)  findViewById(R.id.arrow);
        rose = (ImageView) findViewById(R.id.imageView_compass);
        text_cmp = (TextView) findViewById(R.id.text_degree);
        text_dist = (TextView) findViewById(R.id.text_distance);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setLocationList();
        requestLocation();
        setCallback();
        notify_address(venue);
        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setLocationList(){

        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, 1);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("permission","not granted");
            return;
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.i("location", "wait to listen");
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.i("location", location.toString());
                    current = location;
                }
            }
        });
    }
    /////////////////////////////////////////////////////
    protected void createLocationRequest(int value) {
        if(value == 0){
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(0);
            locationRequest.setFastestInterval(0);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setNumUpdates(1);
        }
        else if(value == 1){
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

    }

    private void requestLocation(){
        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        createLocationRequest(0);
        builder.addLocationRequest(locationRequest);

        Log.i("Location","request location");
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i("Location", "location gained");
                createLocationRequest(1);
                fusedLocationClient.requestLocationUpdates(locationRequest,
                        call,
                        Looper.getMainLooper());

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(StadiumActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

    ////////////////////////////////////////////////////

    public void setCallback(){
        call = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    current = location;
                }
            };
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.i("on result","ok");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                createLocationRequest(1);
                fusedLocationClient.requestLocationUpdates(locationRequest,
                        call,
                        Looper.getMainLooper());
            }
        }
    }


                @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if ( location != null){
                            current = location;
                        }
                    }
                });
            }

        } else {
            Toast.makeText(this, "Permissions not yet granted", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void notify_address(String address){

        Geocoder geo = new Geocoder(this);
        ArrayList<Address> list = new ArrayList<Address>();
        try {
            list = (ArrayList<Address>) geo.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size() > 0) {
            stadium_addr = list.get(0);
            Log.i("stadio", String.valueOf(stadium_addr.getLatitude()) + " " + String.valueOf(stadium_addr.getLongitude()));

            stadium = new Location("stadium");

            stadium.setLatitude(stadium_addr.getLatitude());
            stadium.setLongitude(stadium_addr.getLongitude());

        }
        else{

            distance = "Can't find stadium address";
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.90f;

        synchronized(this){

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mLastAccelerometer[0] = alpha * mLastAccelerometer[0] + (1-alpha) * event.values[0];
                mLastAccelerometer[1] = alpha * mLastAccelerometer[1] + (1-alpha) * event.values[1];
                mLastAccelerometer[2] = alpha * mLastAccelerometer[2] + (1-alpha) * event.values[2];

                mLastAccelerometerSet = true;

            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                mLastMagnetometer[0] = alpha * mLastMagnetometer[0] + (1-alpha) * event.values[0];
                mLastMagnetometer[1] = alpha * mLastMagnetometer[1] + (1-alpha) * event.values[1];
                mLastMagnetometer[2] = alpha * mLastMagnetometer[2] + (1-alpha) * event.values[2];
                mLastMagnetometerSet = true;

            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(rMat, orientation);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;

            }

            if(current != null && stadium != null){
                rose.setRotation(-mAzimuth);
                mAzimuth -= ((current.bearingTo(stadium) + 360)% 360);
                mAzimuth = (mAzimuth + 360)%360;

                DecimalFormat precision = new DecimalFormat("#.###");
                distance = precision.format(current.distanceTo(stadium) / 1000) + " km";

                text_cmp.setText("The current displacement angle is: \n"+ String.valueOf(360-mAzimuth) + "° ");
                arrow.setRotation(-mAzimuth);

            }
            else if(current != null && stadium == null){
                mAzimuth = 0;
                rose.setRotation(-mAzimuth);
                text_cmp.setText("Can't find stadium location");
                arrow.setRotation(-mAzimuth);
            }
            else{
                mAzimuth = Math.round(mAzimuth);
                text_cmp.setText("The current displacement angle is: \n"+mAzimuth + "° ");
                arrow.setRotation(-mAzimuth);
                rose.setRotation(-mAzimuth);
            }

            text_dist.setText("The current distance to stadium is: \n"+ distance);


        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
    }

    public void noSensorsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
        fusedLocationClient.removeLocationUpdates(call);


    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
        fusedLocationClient.requestLocationUpdates(locationRequest, call, Looper.getMainLooper());
    }
}
