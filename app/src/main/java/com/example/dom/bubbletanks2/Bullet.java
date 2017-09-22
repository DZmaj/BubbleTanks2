package com.example.dom.bubbletanks2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by Dom on 7/30/2017.
 */

class Bullet {

    private float x, y, endX, endY, xDiff, yDiff, xCoef, yCoef;

    private RectF rect;
    Bitmap bitmap;

    float speedPerSecond =  650;

    private int length = 30;
    private int height = 30;
    private int screenX, screenY;

    private boolean isAlive;

    public Bullet (Context context, float startX, float startY, float endX, float endY, int screenX, int screenY){
        rect = new RectF();
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

        x = startX;
        y = startY;
        this.screenX = screenX;
        this.screenY = screenY;

        xDiff = endX - x;
        yDiff = endY - y;
        isAlive = true;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet2);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

    }

    public RectF getRect(){
        return  rect;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void kill(){
        isAlive = false;
    }

    public void update(long fps){
        if (isAlive){
            float speed = speedPerSecond / fps;

            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                xCoef = Math.abs(yDiff) / (Math.abs(xDiff) + Math.abs(yDiff));
                yCoef = 1 - xCoef;
            } else {
                yCoef = Math.abs(xDiff) / (Math.abs(xDiff) + Math.abs(yDiff));
                xCoef = 1 - yCoef;
            }

            if (xDiff < 0){
                x -= (speed * yCoef);
            } else {
                x += (speed * yCoef);
            }

            if (yDiff < 0){
                y -= (speed * xCoef);
            } else {
                y += (speed * xCoef);
            }

            if (x > screenX || x < 0 || y > screenY || y < 0){
                kill();
            }

            rect.top = y;
            rect.bottom = y + height;
            rect.left = x;
            rect.right = x + length;
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
