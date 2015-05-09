package com.example.andras.i2048;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class leaderboardActivity extends ActionBarActivity {

    ListView LV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        //fajlbol olvasas
        try{
            FileInputStream fileInputStream = openFileInput("scores.txt");
            StringBuffer buffer = new StringBuffer();
            int read = -1;
            while ((read = fileInputStream.read()) != -1)
            {
                buffer.append((char)read);
            }

            String temp = buffer.toString();

            //szetvagva a "space"-eknel
            String[] scores = temp.split(" ");


            //beilleszteses rendezes - csokkeno
            for (int i = 0; i < scores.length; i++)
            {
                int x = Integer.parseInt(scores[i]);
                int j = i;
                while (j > 0 && Integer.parseInt(scores[j-1]) < x)
                {
                    scores[j] = scores[j-1];
                    j = j - 1;
                }
                scores[j] = Integer.toString(x);
            }

            //x. score - pl 1. 2500 formatum
            for (int i = 0; i < scores.length; i++)
                scores[i] = (i + 1) + ". " + scores[i];





            LV = (ListView) findViewById(R.id.rslt);

            // ha a lista meg ures
            String[] emptyList = new String[] { "Még nincsenek eredmények!" };
            if (scores.length == 0)
            {
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emptyList);
                LV.setAdapter(adapter2);
            }

            //score-ok betoltese a listview-ba
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scores);
            LV.setAdapter(adapter);





            fileInputStream.close();

        } catch(OutOfMemoryError om){
            om.printStackTrace();
        } catch(Exception ex){
            ex.printStackTrace();
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
