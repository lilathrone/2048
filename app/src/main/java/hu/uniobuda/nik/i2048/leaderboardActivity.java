package hu.uniobuda.nik.i2048;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.andras.i2048.R;

import java.io.File;
import java.io.FileInputStream;


public class leaderboardActivity extends ActionBarActivity {

    ListView LV;
    Button Del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        LV = (ListView) findViewById(R.id.rslt);

        String[] NoScores = new String[] {"No scores to display", "-----"};
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, NoScores);
        LV.setAdapter(adapter2);

        Del = (Button) findViewById(R.id.del);
        Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eredmenyek torlese, activity ujratoltes
                String filePath = getFilesDir().toString() + "/scores.txt";
                File file = new File(filePath);
                boolean deleted = file.delete();
                if (deleted)
                {
                    LV.setAdapter(adapter2);
                    LV.invalidate();
                }
            }
        });

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
