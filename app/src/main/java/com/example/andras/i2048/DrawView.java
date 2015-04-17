package com.example.andras.i2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Andras on 2015.04.17..
 */
public class DrawView extends View {
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
        Rect board = new Rect();
        board.set(0,0,canvas.getWidth(),canvas.getHeight()/2);
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(board, p);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++) {
                Rect slot = new Rect();
                slot.set(j*(canvas.getWidth()/4),i*(canvas.getHeight()/8),j*(canvas.getWidth()/4)+((canvas.getWidth()/4)-5),i*(canvas.getHeight()/8)+((canvas.getHeight()/8)-5));
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawRect(slot,paint);
            }
        }
    }
}
