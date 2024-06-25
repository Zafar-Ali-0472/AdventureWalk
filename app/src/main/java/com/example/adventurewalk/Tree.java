package com.example.adventurewalk;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Tree {
    public int x, y, width, height;
    Bitmap bitmap;
    Tree(int bx, int by, int w, int h, Resources res) {
        x = bx;
        y = by;
        width = w;
        height = h;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.treex);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update(int screenHeight, int screenWidth, int xVal) {
        y += 1;
        if (y > screenHeight) {
            y = 0;
            x = xVal;
        }
    }
}