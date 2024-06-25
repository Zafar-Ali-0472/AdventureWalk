package com.example.adventurewalk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastLocation = null;
    private double speed;
    final int leftTreesX = 10, rightTreesX = 510;
    int score = 0, health = 100;
    public static boolean gameover = false;
    List<Tree> leftTrees = new ArrayList<>();
    List<Tree> rightTrees = new ArrayList<>();
    Bhaijan bhaijan;
    Candy candy;
    boolean walking = false;
    Birds birds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);

//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                Location newLocation = locationResult.getLastLocation();
//                speed= newLocation.getSpeed();
//                Log.d("LocationUpdate", "Speed: " + speed);
//                if (speed > 0.1) {
//                    walking = true;
//                } else {
//                    walking = false;
//                }
//                lastLocation = newLocation;
//            }
//        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

//        fusedLocationClient.requestLocationUpdates(locationRequest,
//                locationCallback,
//                Looper.getMainLooper());

        walking = true;

        for (int i = 0; i < 11; i++) {
            leftTrees.add(new Tree(leftTreesX, 200*i, 200, 200, getResources()));
            rightTrees.add(new Tree(rightTreesX, 200*i, 200, 200, getResources()));
        }

        bhaijan = new Bhaijan(200,1000, 250, 300, getResources());
        birds = new Birds(850, new Random().nextInt(1000), 700, 400, getResources());
        candy = new Candy(new Random().nextInt(800), 0, 250, 250, getResources());

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        /// Sensor accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float ax = event.values[0];
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                if(ax >= 0 )
                {
                    bhaijan.moveLeft();
                }
                else if(ax < 0)
                {
                    bhaijan.moveRight(screenWidth);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        MyView mv = new MyView(getApplicationContext());
        setContentView(mv);

        Thread t = new Thread(){
            public void run()
            {
                while(true)
                {
                    mv.invalidate();

                    if(health <= 0) {
                        gameover = true;
                    }

                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                    if (bhaijan.collidesWith(birds)) {
                        health -= 5;
                        birds.reset(1500, new Random().nextInt(1000));
                    }

                    if (bhaijan.collidesWith(candy)) {
                        score += 1;
                        candy.reset(new Random().nextInt(800), 0);
                    }

                    for (Tree tree : leftTrees) {
                        if(walking)
                            tree.update(screenHeight, screenWidth, leftTreesX);
                    }

                    for (Tree tree : rightTrees) {
                        if(walking)
                            tree.update(screenHeight, screenWidth, rightTreesX);
                    }

                    if (walking) {
                        bhaijan.updateFrame();
                        candy.moveDown(screenHeight);
                    } else {
                        bhaijan.resetFrame();
                    }

                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (walking) {
                        birds.moveLeft(screenWidth);
                        birds.moveDown(screenHeight);
                    } else {
                        birds.moveLeft(screenWidth);
                    }

                    if (birds.x + birds.width  +1000+ new Random().nextInt(5000) < 0) {
                        birds.reset(850, new Random().nextInt(1000));
                    }

                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        };
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates when the activity is no longer active
        fusedLocationClient.removeLocationUpdates(locationCallback);
        // If the location is not updating, set walking to false
        walking = false;
    }

    class MyView extends View
    {
        MyView(Context cont)
        {
            super(cont);
            setBackgroundColor(Color.rgb(135,206,235));
            gameover = false;
        }
        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);
            bhaijan.draw(canvas);

            if(gameover) {
                Paint boxPaint = new Paint();
                boxPaint.setColor(Color.argb(200, 0, 0, 0));

                Paint textPaint = new Paint();
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(200f); // Increase text size
                textPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", 350, 500, textPaint);
            } else {
                bhaijan.draw(canvas);
                birds.draw(canvas);
                candy.draw(canvas);

                for (Tree tree : leftTrees) {
                    tree.draw(canvas);
                }

                for (Tree tree : rightTrees) {
                    tree.draw(canvas);
                }
                Paint p = new Paint();
                p.setColor(Color.RED);
                p.setTextSize(50f);
                canvas.drawText("Score:" + score, 450, 100, p);
                canvas.drawText("Health:" + health + "%", 100, 100, p);
            }
        }
    }
}