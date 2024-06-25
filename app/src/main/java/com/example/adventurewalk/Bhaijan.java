package com.example.adventurewalk;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
public class Bhaijan {

    public int x, y, width, height;
    Bitmap bitmap;
    Bitmap[] walkFrames;
    int currentFrame;
    int counter = 0;
    Bhaijan(int bx, int by, int w, int h, Resources res) {
        x = bx;
        y = by;
        width = w;
        height = h;

        walkFrames = new Bitmap[4];
        walkFrames[0] = BitmapFactory.decodeResource(res, R.drawable.walk1);
        walkFrames[1] = BitmapFactory.decodeResource(res, R.drawable.walk2);
        walkFrames[2] = BitmapFactory.decodeResource(res, R.drawable.walk3);
        walkFrames[3] = BitmapFactory.decodeResource(res, R.drawable.walk4);

        for (int i = 0; i < walkFrames.length; i++) {
            walkFrames[i] = Bitmap.createScaledBitmap(walkFrames[i], width, height, false);
        }

        currentFrame = 0;
        bitmap = walkFrames[currentFrame];
    }
    public boolean collidesWith(Birds bird) {
        return x < bird.x + bird.width && x + width > bird.x && y < bird.y + bird.height && y + height > bird.y;
    }
    public boolean collidesWith(Candy candy) {
        return x < candy.x + candy.width && x + width > candy.x && y < candy.y + candy.height && y + height > candy.y;
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
    public void moveLeft() {
        if (x - 2 >= 0) {
            x -= 2;
        }
    }
    public void moveRight(int screenWidth) {
        if (x + width + 2 <= screenWidth) {
            x += 2;
        }
    }
    public void updateFrame() {
        // add delay
        if (counter % 60 == 0) {
            currentFrame = (currentFrame + 1) % walkFrames.length;
            bitmap = walkFrames[currentFrame];
        }
        counter++;

    }
    public void resetFrame() {
        currentFrame = 0;
        bitmap = walkFrames[currentFrame];
    }
}