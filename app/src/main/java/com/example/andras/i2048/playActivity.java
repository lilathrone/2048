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
                            case 0: text="1024";
                                break;
                            case 1: text="2024";
                                break;
                            case 2: text="Endless";
                                break;
                        }

                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
        builder.create();
        builder.show();

    }
}
