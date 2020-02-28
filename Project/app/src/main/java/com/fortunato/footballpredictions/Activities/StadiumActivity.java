package com.fortunato.footballpredictions.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Geocoder;
import android.widget.Toast;


import com.fortunato.footballpredictions.Networks.NetworkStadium;
import com.fortunato.footballpredictions.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class StadiumActivity extends AppCompatActivity implements SensorEventListener {

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




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        Intent intent = getIntent();
        String home = intent.getStringExtra("home_id");

        NetworkStadium netTools = new NetworkStadium(this, 0, home, null);
        Thread tNet = new Thread(netTools);
        tNet.start();

        arrow = (ImageView)  findViewById(R.id.arrow);
        rose = (ImageView) findViewById(R.id.imageView_compass);
        text_cmp = (TextView) findViewById(R.id.text_degree);
        text_dist = (TextView) findViewById(R.id.text_distance);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        setLocationList();
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

    public void askPosition(){
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
            /*if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(rMat, event.values);
                //Log.i("only rotation", "sensore");
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }*/

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                mLastAccelerometer[0] = alpha * mLastAccelerometer[0] + (1-alpha) * event.values[0];
                mLastAccelerometer[1] = alpha * mLastAccelerometer[1] + (1-alpha) * event.values[1];
                mLastAccelerometer[2] = alpha * mLastAccelerometer[2] + (1-alpha) * event.values[2];

                mLastAccelerometerSet = true;
                //Log.i("accelerometer", String.valueOf(mLastAccelerometerSet));
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                //System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                mLastMagnetometer[0] = alpha * mLastMagnetometer[0] + (1-alpha) * event.values[0];
                mLastMagnetometer[1] = alpha * mLastMagnetometer[1] + (1-alpha) * event.values[1];
                mLastMagnetometer[2] = alpha * mLastMagnetometer[2] + (1-alpha) * event.values[2];
                mLastMagnetometerSet = true;
                //Log.i("magnetic", String.valueOf(mLastMagnetometerSet));
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(rMat, orientation);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
                //Log.i("errore", "azimuth "+ mAzimuth);
            }

            askPosition();

            if(current != null && stadium != null){
                rose.setRotation(-mAzimuth);
                mAzimuth -= ((current.bearingTo(stadium) + 360)% 360);
                mAzimuth = (mAzimuth + 360)%360;
                //mAzimuth -= bearing(current.getLatitude(), current.getLongitude(), stadium.getLatitude(), stadium.getLongitude());
                distance = String.valueOf(current.distanceTo(stadium) / 1000) + " km";
                text_cmp.setText("The current displacement angle is: "+ String.valueOf(360-mAzimuth) + "° ");
                arrow.setRotation(-mAzimuth);

                Log.i("distance", distance);
            }
            else if(current != null && stadium == null){
                mAzimuth = 0;
                rose.setRotation(-mAzimuth);
                text_cmp.setText("Can't find stadium location");
                arrow.setRotation(-mAzimuth);
            }
            else{
                mAzimuth = Math.round(mAzimuth);
                text_cmp.setText("The current displacement angle is: "+mAzimuth + "° ");
                arrow.setRotation(-mAzimuth);
                rose.setRotation(-mAzimuth);
                //Log.i("current_position", String.valueOf(current.getLatitude()) + " " + String.valueOf(current.getLongitude()));
            }

            text_dist.setText("The current distance to stadium is: "+ distance);


        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
       //if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
       // }
       /*else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }*/
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }
}
