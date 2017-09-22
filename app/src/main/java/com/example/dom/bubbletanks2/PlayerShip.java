package com.example.dom.bubbletanks2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

/**
 * Created by Dom on 7/29/2017.
 */

public class PlayerShip {
    RectF rect;
    private Bitmap bitmap;
    private float length, height, x, y, nextX, nextY;



    public PlayerShip(Context context, int screenX, int screenY){
        rect = new RectF();
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

        //size of ship
        length = screenX / 12;
        height = length;
        //height = (float) (screenY / 4.5);

        //initial location of ship
        x = screenX / 2;
        y = screenY / 5;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship3);
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

    public void setDestination(float x, float y){
        //this.x = x;
        //this.y = y;

        nextX = x;
        nextY = y;
    }

    public void update(long fps){
        x = nextX - length/2;
        y = nextY - height/2;

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLength() {
        return length;
    }

    public float getHeight() {
        return height;
    }
}
