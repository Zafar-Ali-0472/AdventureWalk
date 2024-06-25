package com.example.adventurewalk;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
public class Candy {
    public int x, y, width, height;
    Bitmap bitmap;
    Candy(int bx, int by, int w, int h, Resources res) {
        x = bx;
        y = by;
        width = w;
        height = h;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.candy);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
    public void moveDown(int screenHeight) {
        if(y + height + 2 <= screenHeight){
            y += 2;
        }else{
            y = 0;
        }
    }
    public void reset(int bx, int by) {
        x = bx;
        y = by;
    }
}

