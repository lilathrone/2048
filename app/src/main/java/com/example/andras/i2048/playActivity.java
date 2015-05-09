package com.example.andras.i2048;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class playActivity extends ActionBarActivity implements SensorEventListener {

    int End;
    DrawView DW;
    Button UP, DOWN, RIGHT, LEFT, ONOFF;
    boolean on = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a game mode")
                .setItems(R.array.mode_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        String text = "";

                        switch (which) {
                            case 0:
                                text = "1024";
                                End = 1024;
                                break;
                            case 1:
                                text = "2048";
                                End = 2048;
                                break;
                            case 2:
                                text = "Endless";
                                End = -1;
                                break;
                        }

                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        builder.create();
        builder.show();

        DW = (DrawView) findViewById(R.id.GameView);

        ONOFF = (Button) findViewById(R.id.OnOff);
        ONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (on)
                {
                    on = false;
                    ONOFF.setText("Off");
                    ONOFF.setBackgroundColor(Color.RED);
                }
                else
                {
                    on = true;
                    ONOFF.setText("On");
                    ONOFF.setBackgroundColor(Color.GREEN);
                }
            }
        });

        UP = (Button) findViewById(R.id.fel);
        UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DW.DoUp();
                DW.invalidate();
                DW.Spawn();
            }
        });

        DOWN = (Button) findViewById(R.id.le);
        DOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DW.DoDown();
                DW.Spawn();
                DW.invalidate();
            }
        });

        RIGHT = (Button) findViewById(R.id.jobbra);
        RIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DW.DoRight();
                DW.Spawn();
                DW.invalidate();
            }
        });

        LEFT = (Button) findViewById(R.id.balra);
        LEFT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DW.DoLeft();
                DW.Spawn();
                DW.invalidate();
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //sensorManager.registerListener(this, gyro, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this, gyro, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyro;

    private long lastUpdate = 0;
    private float last_x, last_y;
    boolean Xback = false;
    boolean Yback = false;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        /*
        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            float x = event.values[0];
            float y = event.values[1];

            if (x != last_x)
            {
                DW.DoLeft();
                DW.Spawn();
                DW.invalidate();
            }

            last_x = x;
        }
        */

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && on) {
            float x = event.values[0];
            float y = event.values[1];

            long curTime = System.currentTimeMillis();

            //100msec elteltevel nezzuk csak ujra, allando ellenorzes helyett
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                if (Math.abs(last_x - x) > 3)
                {
                    if (last_x - x < -3 && !Xback)
                    {
                        DW.DoLeft();
                        DW.Spawn();
                        DW.invalidate();
                        Xback = true;
                    }
                    else if (last_x - x > 3 && !Xback)
                    {
                        DW.DoRight();
                        DW.Spawn();
                        DW.invalidate();
                        Xback = true;
                    }
                    else if (Xback)
                        Xback = false;
                }
                else if (Math.abs(last_y - y) > 3)
                {
                    if (last_y - y < -3)
                    {
                        DW.DoDown();
                        DW.Spawn();
                        DW.invalidate();
                        Yback = true;
                    }
                    else if (last_y - y > 3)
                    {
                        DW.DoUp();
                        DW.Spawn();
                        DW.invalidate();
                        Yback = true;
                    }
                    else
                        Yback = false;
                }

                last_x = x;
                last_y = y;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
