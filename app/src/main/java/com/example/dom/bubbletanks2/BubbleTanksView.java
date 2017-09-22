package com.example.dom.bubbletanks2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Dom on 7/29/2017.
 */

class BubbleTanksView extends SurfaceView implements Runnable {

    Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    private long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;
    PlayerShip playerTank;
    float lowestFPS = 9999;
    long time;
    long bulletTimeDiff = 100;

    Random rand = new Random();
    private Bullet[] bullets = new Bullet[50];
    private int bulletCount = 0;

    private EnemyShip[] enemies = new EnemyShip[200];
    private int enemyCount = 0;

    int score = 0;
    int difficulty = 30;


    public BubbleTanksView(Context context, int x, int y){
        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        playerTank = new PlayerShip(context, screenX, screenY);

        time = System.currentTimeMillis();

    }


    @Override
    public void run() {
        while (playing) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void update(){
        if (lowestFPS > fps){
            lowestFPS = fps;
        }


        //update positions of things
        playerTank.update(fps);

        for (Bullet b : bullets){
            if (b != null && b.isAlive()) {
                b.update(fps);
            }
        }

        for (EnemyShip e : enemies){
            if (e != null && e.isAlive()) {
                e.update(fps);
                if (RectF.intersects(e.getRect(), playerTank.getRect())){
                    //k well i cant toast from here for some reason
                    Log.e("ship", "has been hit");
                    //Toast toast = Toast.makeText(context, "yu lose", Toast.LENGTH_LONG);
                    //toast.show();

                    e.kill();
                    score -= 100;
                }
            }
        }

        //check collisions
        for (EnemyShip e : enemies){
            for (Bullet b : bullets){
                if (e != null && e.isAlive()) {
                    if (b != null && b.isAlive()) {
                        if (RectF.intersects(e.getRect(), b.getRect())){
                            Log.e("ship", "its a hit");
                            e.kill();
                            b.kill();
                            score += 10;
                        }
                    }
                }
            }
        }

        if (rand.nextInt((int)(10)) == 5){
            enemies[enemyCount] = new EnemyShip(context, screenX, screenY);
            enemyCount++;
            if (enemyCount > 199){
                enemyCount = 0;
            }

            if (difficulty > 5){
                difficulty--;
            }
        }



    }

    public void draw(){
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Draw the player spaceship
            canvas.drawBitmap(playerTank.getBitmap(), playerTank.getX(), playerTank.getY(), paint);

            //draw fps
            paint.setTextSize(45);
            canvas.drawText("FPS:" + fps, 20, 50, paint);
            canvas.drawText("lowest FPS:" + lowestFPS, 20, 90, paint);
            canvas.drawText("Score:" + score, (int)(screenX * 0.9), 50, paint);

            for (Bullet b : bullets){
                if (b != null && b.isAlive()) {
                    canvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), paint);
                }
            }

            for (EnemyShip e : enemies){
                if (e != null && e.isAlive()) {
                    canvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), paint);
                }
            }

            ourHolder.unlockCanvasAndPost(canvas);

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;
        int fingerNumber = motionEvent.getPointerCount();

        switch (action) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                paused = false;
                playerTank.setDestination(motionEvent.getX(), motionEvent.getY());
                break;

            //player has dragged
            case MotionEvent.ACTION_MOVE:
                //if its first finger, move the ship, if its second then shoot
                if (fingerNumber == 1) {
                    paused = false;
                    playerTank.setDestination(motionEvent.getX(), motionEvent.getY());
                    break;
                } else if (fingerNumber == 2){
                    paused = false;
                    //getX(1) means get the X coordinate of finger 1, should really be fingerNumber-1 but this makes sure it always looks at the second finger
                    playerTank.setDestination(motionEvent.getX(), motionEvent.getY());

                    long newTime = System.currentTimeMillis();
                    if (time + bulletTimeDiff < newTime) {
                        bullets[bulletCount] = new Bullet(context, playerTank.getX() + playerTank.getLength() - 15, playerTank.getY() + (playerTank.getHeight() / 2 - 14), motionEvent.getX(1), motionEvent.getY(1), screenX, screenY);
                        bulletCount++;
                        if (bulletCount > 49) {
                            bulletCount = 0;
                        }
                        time = System.currentTimeMillis();
                    }
                    break;
                }

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e("finger ", "" + motionEvent.getPointerCount());
                paused = false;
                bullets[bulletCount] = new Bullet(context, playerTank.getX(), playerTank.getY() ,motionEvent.getX(), motionEvent.getY(), screenX, screenY);
                bulletCount++;
                if (bulletCount > 49){
                    bulletCount = 0;
                }
                break;


            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }



    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }
}
