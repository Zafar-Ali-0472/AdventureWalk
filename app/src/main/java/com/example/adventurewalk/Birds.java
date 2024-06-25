package com.example.adventurewalk;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
public class Birds {
    public int x, y, width, height;
    Bitmap bitmap;
    Birds(int bx, int by, int w, int h, Resources res) {
        x = bx;
        y = by;
        width = w;
        height = h;
        bitmap = BitmapFactory.decodeResource(res, R.drawable.birds);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
    public void moveLeft(int screenWidth) {
        x -= 2;
    }
    public void moveDown(int screenHeight) {
        y += 2;
    }
    public void reset(int bx, int by) {
        x = bx;
        y = by;
    }
}
