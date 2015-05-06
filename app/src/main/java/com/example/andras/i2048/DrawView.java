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

import java.util.ArrayList;
import java.util.Random;

public class DrawView extends View {

    int[][] gameBoard = new int[4][4]; //jatekter
    int[][] mask = new int[4][4]; //jatekterre maszk, ahhoz kell, hogy egy lépésben ne lehessen több összevonás
    int[][] canStep = new int[4][4]; //Léptetéshez
    int score = 0; //pontok
    boolean gameOver = false; //vege van?
    boolean start = true; //start - restart

    //szia Robi!!

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

    public void DoLeft()
    {
        boolean isFinished = false;
        while (!isFinished)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    LeftStep(i, j);
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
    }

    public void DoUp()
    {
        boolean isFinished = false;
        while (!isFinished)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    UpStep(i, j);
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
    }

    public void DoDown()
    {
        boolean isFinished = false;
        while (!isFinished)
        {
            for (int i = 3; i > -1; i--)
            {
                for (int j = 0; j < 4; j++)
                {
                    DownStep(i, j);
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
    }

    private void DownStep(int i, int j)
    {
        if (i < 3 && gameBoard[i + 1][j] == gameBoard[i][j] && mask[i + 1][j] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            gameBoard[i + 1][j] += gameBoard[i][j];
            gameBoard[i][j] = 0;
            mask[i + 1][j] = -1;
            canStep[i][j] = 1;
        }
        else if (i < 3 && gameBoard[i + 1][j] == 0 && gameBoard[i][j] != 0)
        {
            gameBoard[i + 1][j] = gameBoard[i][j];
            gameBoard[i][j] = 0;
            canStep[i][j] = 1;
            mask[i + 1][j] = mask[i][j];
        }
        else
        {
            canStep[i][j] = 0;
        }
    }
    public void DoRight()
    {
        boolean isFinished = false;
        while (!isFinished)
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 3; j > -1; j--)
                {
                    RightStep(i, j);
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
    }

    private void RightStep(int i, int j)
    {
        if (j < 3 && gameBoard[i][j + 1] == gameBoard[i][j] && mask[i][j + 1] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            gameBoard[i][j + 1] += gameBoard[i][j];
            gameBoard[i][j] = 0;
            mask[i][j + 1] = -1;
            canStep[i][j] = 1;
        }
        else if (j < 3 && gameBoard[i][j + 1] == 0 && gameBoard[i][j] != 0)
        {
            gameBoard[i][j + 1] = gameBoard[i][j];
            gameBoard[i][j] = 0;
            canStep[i][j] = 1;
            mask[i][j + 1] = mask[i][j];
        }
        else
        {
            canStep[i][j] = 0;
        }
    }

    private void UpStep(int i, int j)
    {
        //ha mellette összevonás van
        if (i > 0 && gameBoard[i-1][j] == gameBoard[i][j] && mask[i-1][j] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            gameBoard[i-1][j] += gameBoard[i][j];
            gameBoard[i][j] = 0;
            mask[i-1][j] = -1;
            canStep[i][j] = 1;
        }
        else if (i > 0 && gameBoard[i-1][j] == 0 && gameBoard[i][j] != 0)
        {
            gameBoard[i-1][j] = gameBoard[i][j];
            gameBoard[i][j] = 0;
            canStep[i][j] = 1;
            mask[i - 1][j] = mask[i][j];
        }
        else
        {
            canStep[i][j] = 0;
        }
    }
    public void LeftStep(int i, int j)
    {
        //ha mellette összevonás van
        if (j > 0 && gameBoard[i][j-1] == gameBoard[i][j] && mask[i][j-1] != -1 && gameBoard[i][j] != 0 && mask[i][j] != -1)
        {
            gameBoard[i][j-1] += gameBoard[i][j];
            gameBoard[i][j] = 0;
            mask[i][j-1] = -1;
            canStep[i][j] = 1;
        }
        else if (j > 0 && gameBoard[i][j-1] == 0 && gameBoard[i][j] != 0)
        {
            gameBoard[i][j-1] = gameBoard[i][j];
            gameBoard[i][j] = 0;
            canStep[i][j] = 1;
            mask[i][j - 1] = mask[i][j];
        }
        else
        {
            canStep[i][j] = 0;
        }
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
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight()/2 + 100, p);
            canvas.drawText("Game Over, your score:" + score, canvas.getWidth()/2 , canvas.getHeight()/4, paintScore);

        }
    }
}
