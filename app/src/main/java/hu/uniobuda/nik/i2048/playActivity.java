package hu.uniobuda.nik.i2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andras.i2048.R;


public class playActivity extends ActionBarActivity implements SensorEventListener {

    DrawView DW;
    Button UP, DOWN, RIGHT, LEFT, ONOFF;
    boolean on = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //jatekmod valaszto dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a game mode")
                .setItems(R.array.mode_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        String text = "";

                        //valasztas alapjan scorehatar beallitasa + ki toast-ba a vlasztas
                        switch (which) {
                            case 0:
                                text = "1024";
                                DW.gamemode = 1024;
                                break;
                            case 1:
                                text = "2048";
                                DW.gamemode = 2048;
                                break;
                            case 2:
                                text = "Endless";
                                DW.gamemode = -1;
                                break;
                        }

                        Toast toast = Toast.makeText(context, text + " mode", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        builder.create();
        builder.show();

        DW = (DrawView) findViewById(R.id.GameView);

        //szenzoros iranyitas ki-be kapcsolasa - default bekapcsolva
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

        //gombokkal valo iranyitas: adott iranyba leptet -> volt lepes? ha igen spawn es ujrarajzolas
        //ha win/gameover nem tudunk lepni
        UP = (Button) findViewById(R.id.fel);
        UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DW.gameOver && !DW.win)
                    if(DW.DoUp())
                    {
                        DW.Spawn();
                        DW.invalidate();
                    }
            }
        });

        DOWN = (Button) findViewById(R.id.le);
        DOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DW.gameOver && !DW.win)
                    if(DW.DoDown())
                    {
                        DW.Spawn();
                        DW.invalidate();
                    }
            }
        });

        RIGHT = (Button) findViewById(R.id.jobbra);
        RIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DW.gameOver && !DW.win)
                    if(DW.DoRight())
                    {
                        DW.Spawn();
                        DW.invalidate();
                    }
            }
        });

        LEFT = (Button) findViewById(R.id.balra);
        LEFT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DW.gameOver && !DW.win)
                    if(DW.DoLeft())
                    {
                        DW.Spawn();
                        DW.invalidate();
                    }
            }
        });

        //szenzor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
    private float last_x, last_y, last_z;
    boolean Xback = false;
    boolean Yback = false;

    int TimeToWait = 100;

    //szenzorertek valtozas eseten
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && on) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long curTime = System.currentTimeMillis(); //aktualis idopillanat

            //100msec elteltevel nezzuk csak ujra, allando ellenorzes helyett (aktualis - elozo)
            //ha volt osszevonas 1,5 mp-kent nezzek ujra
            if ((curTime - lastUpdate) > TimeToWait) {
                TimeToWait = 100;
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                if (Math.abs(x) > 3) {
                    if ( x > -3 && !Xback) {
                        if (!DW.gameOver && !DW.win)
                            if(DW.DoLeft())
                            {
                                DW.Spawn();
                                DW.invalidate();
                            }
                        Xback = true;
                        TimeToWait = 1500;
                    }
                    else if ( x < 3 && !Xback) {
                        if (!DW.gameOver && !DW.win)
                            if(DW.DoRight())
                            {
                                DW.Spawn();
                                DW.invalidate();
                            }
                        Xback = true;
                        TimeToWait = 1500;
                    }
                    else if (Xback)
                        Xback = false;
                }
                else if (Math.abs(y) > 3) {
                    if (y > -3) {
                        if (!DW.gameOver && !DW.win)
                            if(DW.DoDown())
                            {
                                DW.Spawn();
                                DW.invalidate();
                            }
                        Yback = true;
                        TimeToWait = 1500;
                    }
                    else if (y < 3) {
                        if (!DW.gameOver && !DW.win)
                            if(DW.DoUp())
                            {
                                DW.Spawn();
                                DW.invalidate();
                            }
                        Yback = true;
                        TimeToWait = 1500;
                    }
                    else if (Yback)
                        Yback = false;
                }
                lastUpdate = System.currentTimeMillis(); //elozo = aktualis
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
