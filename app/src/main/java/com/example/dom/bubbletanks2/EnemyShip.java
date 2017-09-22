package com.example.dom.bubbletanks2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

/**
 * Created by Dom on 8/1/2017.
 */

class EnemyShip {
    RectF rect;
    private Bitmap bitmap;
    private float length, height, x, y, speed;
    private boolean isAlive;
    float speedPerSecond =  300;


    public EnemyShip(Context context, int screenX, int screenY){
        rect = new RectF();
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

        Random rand = new Random();
        isAlive = true;

        //size of ship
        length = screenX / 12;
        height = length/1.897f;
        //height = (float) (screenY / 4.5);

        //initial location of ship
        x = screenX;
        y = rand.nextInt((int)((screenY - 3*height) + 3*height));
        //y = rand.nextInt(100) * screenY/95 - height;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien1);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
    }

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void update(long fps){
        float speed = speedPerSecond / fps;

        //Log.e("ship", "x is " + x + " speed is " + speed);

        x -= speed;

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

        if (x < -length){
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void kill() {
        isAlive = false;
    }
}
