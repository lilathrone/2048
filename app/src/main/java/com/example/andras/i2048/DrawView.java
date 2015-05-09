package com.example.andras.i2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class DrawView extends View {

    int[][] gameBoard = new int[4][4]; //jatekter
    int[][] mask = new int[4][4]; //jatekterre maszk, ahhoz kell, hogy egy lépésben ne lehessen több összevonás
    int[][] canStep = new int[4][4]; //Léptetéshez
    int score = 0; //pontok
    boolean gameOver = false; //vege van?
    boolean start = true; //start - restart


    //jatek inicializalasa
    public void InitTable()
    {
        gameOver = false;
        score = 0;
        gameBoard = new int[4][4];

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                gameBoard[i][j] = 0;
            }
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                canStep[i][j] = 0;
            }
        }
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                mask[i][j] = 0;
            }
        }
        //2 random kezdomezo
        Spawn();
        Spawn();
    }

    //Maszk tömbök defaultra állítása
    private void ClearMask_capStep()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                mask[i][j] = 0;
            }
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                canStep[i][j] = 1;
            }
        }
    }

    //A DoLeft, DoDown, DoUp, DoRight metódusok nagyjából ugyanazt csinálják, a különbség, hogy
    //a tömböt milyen irányból járják be és hogy melyik irányú mozgatást végrehajtó metódust hívnak meg
    //a DoDown-nál van kikommentezve a működés
    public boolean DoLeft()
    {
        boolean voltMozgas = false;
        boolean isFinished = false;
        while (!isFinished)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    voltMozgas = LeftStep(i, j, voltMozgas);
                }
            }
            //Draw();
            isFinished = true;
            for (int[] array : canStep)
            {
                for (int item : array)
                if (item == 1)
                    isFinished = false;
            }
        }
        ClearMask_capStep();
        return voltMozgas;
    }

    public boolean DoUp()
    {
        boolean voltMozgas = false;
        boolean isFinished = false;
        while (!isFinished)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    voltMozgas = UpStep(i, j,voltMozgas);
                }
            }
            //Draw();
            isFinished = true;
            for (int[] array : canStep)
            {
                for (int item : array)
                    if (item == 1)
                        isFinished = false;
            }
        }
        ClearMask_capStep();
        return voltMozgas;
    }

    //A játékos a lefele gombot nyomja, vagy lefele dönti a készüléket
    public boolean DoDown()
    {
        boolean voltMozgas = false; //Annak vizsgálatára, hogy történik e bármilyen mozgás a játéktéren
        boolean isFinished = false; //Van e még mozgás a játéktéren
        while (!isFinished) // Amíg még van / lehet mozgás
        {
            //Tömbbejárás
            for (int i = 3; i > -1; i--)
            {
                for (int j = 0; j < 4; j++)
                {
                    voltMozgas = DownStep(i, j, voltMozgas); //Elemek egyenkénti mozgatása
                }
            }
            //Draw();
            isFinished = true;
            //canStep tömb bejárása, ha valahol nem nulla (1), akkor újra be kell járni a tömböt
            for (int[] array : canStep)
            {
                for (int item : array)
                    if (item == 1)
                        isFinished = false; //Ha volt mozgás, akkor megint be kell járni a tömböt
            }
        }
        ClearMask_capStep(); //Maszk tömbök defaultra állítása
        return voltMozgas;
    }

    //Tömb egy elemének mozgatása
    //Rightstep, Upstep, Leftstep ugyanezt csinálja, csak más irányban
     private boolean DownStep(int i, int j, boolean voltMozgas)
    {
        //Ha még nincs a pálya szélén, a szomszédos mező megegyezik vele és ez a mező, illetve a szomszédos mező
        //még nem lett felülírva ebben az iterációban
        if (i < 3 && gameBoard[i + 1][j] == gameBoard[i][j] && mask[i + 1][j] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            voltMozgas = true; //Van mozgás, tehát majd érvényes volt a lépés, spawnolni kell új mezőt majd az iteráció végén
            gameBoard[i + 1][j] += gameBoard[i][j]; //összevonás
            gameBoard[i][j] = 0; //Jelenlegi mező kinullázása
            mask[i + 1][j] = -1; //Maszk tömbön jelezni kell, hogy a szomszédos elemet már nem lehet felülírni mégegyszer
            canStep[i][j] = 1; //Lehet még mozgás a tömbben ezen lépésen belül
        } //Egyébként ha nincs a pálya szélén, de nem nulla értékű a tömbelem és mellette nulla van
        else if (i < 3 && gameBoard[i + 1][j] == 0 && gameBoard[i][j] != 0)
        {
            voltMozgas = true; //Van mozgás
            gameBoard[i + 1][j] = gameBoard[i][j]; // Elem odébb shiftelése
            gameBoard[i][j] = 0; //jelenlegi pozíció kinullázása
            canStep[i][j] = 1; //Lehet még mozgás
            mask[i + 1][j] = mask[i][j]; //Ha ez már egy összevont elem volt, odébb kell menteni a maszkolt értékét
        }
        else
        {
            canStep[i][j] = 0; //Ha nem volt mozgatás
        }
        return voltMozgas;
    }
    public boolean DoRight()
    {
        boolean voltMozgas = false;
        boolean isFinished = false;
        while (!isFinished)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 3; j > -1; j--)
                {
                    voltMozgas =  RightStep(i, j, voltMozgas);
                }
            }
            //Draw();
            isFinished = true;
            for (int[] array : canStep)
            {
                for (int item : array)
                    if (item == 1)
                        isFinished = false;
            }
        }
        ClearMask_capStep();
        return voltMozgas;
    }

    private boolean RightStep(int i, int j, boolean voltMozgas)
    {
        if (j < 3 && gameBoard[i][j + 1] == gameBoard[i][j] && mask[i][j + 1] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            voltMozgas = true;
            gameBoard[i][j + 1] += gameBoard[i][j];
            gameBoard[i][j] = 0;
            mask[i][j + 1] = -1;
            canStep[i][j] = 1;
        }
        else if (j < 3 && gameBoard[i][j + 1] == 0 && gameBoard[i][j] != 0)
        {
            voltMozgas = true;
            gameBoard[i][j + 1] = gameBoard[i][j];
            gameBoard[i][j] = 0;
            canStep[i][j] = 1;
            mask[i][j + 1] = mask[i][j];
        }
        else
        {
            canStep[i][j] = 0;
        }
        return voltMozgas;
    }

    private boolean UpStep(int i, int j, boolean voltMozgas)
    {
        //ha mellette összevonás van
        if (i > 0 && gameBoard[i-1][j] == gameBoard[i][j] && mask[i-1][j] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            voltMozgas = true;
            gameBoard[i-1][j] += gameBoard[i][j];
            gameBoard[i][j] = 0;
            mask[i-1][j] = -1;
            canStep[i][j] = 1;
        }
        else if (i > 0 && gameBoard[i-1][j] == 0 && gameBoard[i][j] != 0)
        {
            voltMozgas = true;
            gameBoard[i-1][j] = gameBoard[i][j];
            gameBoard[i][j] = 0;
            canStep[i][j] = 1;
            mask[i - 1][j] = mask[i][j];
        }
        else
        {
            canStep[i][j] = 0;
        }
        return voltMozgas;
    }
    public boolean LeftStep(int i, int j, boolean voltMozgas)
    {
        //ha mellette összevonás van
        if (j > 0 && gameBoard[i][j-1] == gameBoard[i][j] && mask[i][j-1] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            voltMozgas = true;
            gameBoard[i][j-1] += gameBoard[i][j];
            gameBoard[i][j] = 0;
            mask[i][j-1] = -1;
            canStep[i][j] = 1;
        }
        else if (j > 0 && gameBoard[i][j-1] == 0 && gameBoard[i][j] != 0)
        {
            voltMozgas = true;
            gameBoard[i][j-1] = gameBoard[i][j];
            gameBoard[i][j] = 0;
            canStep[i][j] = 1;
            mask[i][j - 1] = mask[i][j];
        }
        else
        {
            canStep[i][j] = 0;
        }
        return voltMozgas;
    }

    public void Spawn()
    {
        //ures mezok megkeresese es listaba toltese
        ArrayList<Integer> emptyX = new ArrayList<Integer>();
        ArrayList<Integer> emptyY = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if(gameBoard[i][j] == 0)
                {
                    emptyX.add(i);
                    emptyY.add(j);
                }
            }
        }

        Random r = new Random();
        int randomPoz = r.nextInt(emptyX.size()); //random pozicio a listabol
        int randomChance = r.nextInt(10); //random szam az eselyhez (2 v 4)

        //ures mezok kozul a random helyre beszuras
        gameBoard[emptyX.get(randomPoz)][emptyY.get(randomPoz)] = randomChance < 6 ? 2 : 4; //60% valoszinuseggel 2, 40% 4 spawnol
    }

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //InitTable();
        if (start)
        {
            InitTable();
            start = false;
        }

        //jatektabla - hatter
        Rect board = new Rect();
        board.set(0,0,canvas.getWidth(),canvas.getHeight()/2 + 100);
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(board, p);

        //gameBoard[0][0] = 2;
        //gameBoard[1][1] = 16;
        //gameBoard[2][2] = 512;
        //gameBoard[3][3] = 2048;

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++) {
                //slotok hattere
                Rect slot = new Rect();
                slot.set(j*(canvas.getWidth()/4),i*(canvas.getHeight()/8),j*(canvas.getWidth()/4)+((canvas.getWidth()/4)-5),i*(canvas.getHeight()/8)+((canvas.getHeight()/8)-5));
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawRect(slot,paint);

                if (gameBoard[i][j] > 0) {
                    //ertekkes mezok kirajzolasa
                    int power = (int) (Math.log((double) gameBoard[i][j]) / Math.log(2)); // hatvany nagysaga
                    slot.set(j * (canvas.getWidth() / 4), i * (canvas.getHeight() / 8), j * (canvas.getWidth() / 4) + ((canvas.getWidth() / 4) - 5), i * (canvas.getHeight() / 8) + ((canvas.getHeight() / 8) - 5));
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.rgb(255 - power * 20, 255 - power * 10, 0));
                    canvas.drawRect(slot, paint);

                    //arra a szoveg
                    paint.setColor(Color.WHITE);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                    paint.setTextSize(50); //hardcode lehet kene finomitani

                    canvas.drawText("" + gameBoard[i][j], slot.centerX(), slot.centerY()+20, paint);
                }
            }
        }
        //score kiirasa
        Paint paintScore = new Paint();
        paintScore.setColor(Color.WHITE);
        paintScore.setTypeface(Typeface.DEFAULT_BOLD);
        paintScore.setTextSize(50);
        canvas.drawText("Score: " + score, 0, canvas.getHeight()/2 + 50, paintScore);
        //jatek vege - kep elhalvanyul - score ki
        if (gameOver)
        {
            p.setColor(Color.WHITE);
            p.setAlpha(100);
            paintScore.setColor(Color.BLACK);
            paintScore.setTextAlign(Paint.Align.CENTER);
            paintScore.setTextSize(20);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight() / 2 + 100, p);
            canvas.drawText("Game Over, your score:" + score, canvas.getWidth()/2 , canvas.getHeight()/4, paintScore);

            String filePath = getContext().getFilesDir().toString() + "/scores.txt";
            try
            {
                FileOutputStream outputStream = new FileOutputStream(filePath, true);
                String Score = Integer.toString(score) + " ";
                outputStream.write(Score.getBytes());
                outputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
