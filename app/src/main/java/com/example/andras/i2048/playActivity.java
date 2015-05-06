package com.example.andras.i2048;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class playActivity extends ActionBarActivity {

    int End;
    DrawView DW;
    Button UP, DOWN, RIGHT, LEFT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a game mode")
                .setItems(R.array.mode_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Context context = getApplicationContext();
                        String text="";

                        switch (which)
                        {
                            case 0: text = "1024";
                                End = 1024;
                                break;
                            case 1: text = "2024";
                                End = 2048;
                                break;
                            case 2: text = "Endless";
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

        UP = (Button) findViewById(R.id.fel);
        UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DW.Spawn();
                DW.invalidate();
            }
        });

        DOWN = (Button) findViewById(R.id.le);
        DOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DW.gameOver = true;

                DW.invalidate();
            }
        });

        RIGHT = (Button) findViewById(R.id.jobbra);
        RIGHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DW.score += 10;
                DW.invalidate();
            }
        });

        LEFT = (Button) findViewById(R.id.balra);
        LEFT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DW.score != 0)
                {
                    DW.DoLeft();
                    //DW.score -= 10;
                    DW.invalidate();
                }
            }
        });
    }
}
