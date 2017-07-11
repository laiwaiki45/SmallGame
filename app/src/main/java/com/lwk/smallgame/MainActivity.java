package com.lwk.smallgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button start;
    TextView timeleftText;
    TextView scoreText;
    ArrayList<ImageButton> imageButtons;
    Timer[] timers;
    int score = 0;
    TimerTask taskTrigger;
    final Handler myHandler = new Handler();
    boolean stop_timer = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.startBtn);
        timeleftText = (TextView) findViewById(R.id.timeText);
        scoreText = (TextView) findViewById(R.id.scoreText);

        imageButtons = new ArrayList<ImageButton>();
        imageButtons.add((ImageButton) findViewById(R.id.id1));
        imageButtons.add((ImageButton) findViewById(R.id.id2));
        imageButtons.add((ImageButton) findViewById(R.id.id3));
        imageButtons.add((ImageButton) findViewById(R.id.id4));
        imageButtons.add((ImageButton) findViewById(R.id.id5));
        imageButtons.add((ImageButton) findViewById(R.id.id6));
        imageButtons.add((ImageButton) findViewById(R.id.id7));
        imageButtons.add((ImageButton) findViewById(R.id.id8));
        imageButtons.add((ImageButton) findViewById(R.id.id9));
        final SharedPreferences sharedPreferences = getSharedPreferences("highest" , MODE_PRIVATE);
        final int highest = sharedPreferences.getInt("highest", 0);


        timers = new Timer[imageButtons.size()];
        for (int i = 0; i < timers.length; i++)
            timers[i] = new Timer(true);

        View.OnClickListener imageOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageButton clickedImage = (ImageButton) v;
                if (clickedImage.getVisibility() == View.VISIBLE) {
                    //The user clicked the right image, you can add marks
                    clickedImage.setVisibility(View.INVISIBLE);
                    score += 100;
                    scoreText.setText(String.valueOf(score));
                    //Make a new timer for appear the image again
                    final int index = Integer.parseInt(getResources().
                            getResourceName(clickedImage.getId()).substring(getResources().getResourceName(clickedImage.getId()).indexOf("/") + 3)) - 1;

                    //timers[index].cancel();
                    timers[index].schedule(new myTimerTask(clickedImage, timers[index]), 3000);
                }
            }
        };

        for (int i = 0; i < imageButtons.size(); i++)
            imageButtons.get(i).setOnClickListener(imageOnClick);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score = 0;
                scoreText.setText(String.valueOf(score));
                stop_timer = false;
                for (int i = 0; i < timers.length; i++) {
                    imageButtons.get(i).setVisibility(View.INVISIBLE);
                    timers[i].schedule(new myTimerTask(imageButtons.get(i), timers[i]), 3000);
                }
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timeleftText.setText("Time remaining: " + millisUntilFinished / 1000 + " seconds");
                    }

                    public void onFinish() {
                        timeleftText.setText("Time remaining: 0 seconds");
                        stop_timer = true;
                        for (int i = 0; i < timers.length; i++) {
                            imageButtons.get(i).setVisibility(View.INVISIBLE);
                            timers[i].cancel();
                            timers[i] = new Timer(true);

                            if(score > highest){
                                sharedPreferences.edit().putInt("highest", score).apply();
                            }
                        }
                    }
                }.start();

            }
        });

        Button rank = (Button) findViewById(R.id.rankBtn);
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
            }
        });
    }

    class myTimerTask extends TimerTask {
        ImageButton targetimage;
        Timer timer;

        myTimerTask(ImageButton iv, Timer t) {
            this.targetimage = iv;
            this.timer = t;
        }

        public void run() {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!stop_timer) {
                        if (targetimage.getVisibility() == View.VISIBLE)
                            targetimage.setVisibility(View.INVISIBLE);
                        else
                            targetimage.setVisibility(View.VISIBLE);
                        //Timer t = new Timer(true);
                        //t.cancel();

                        timer.schedule(new myTimerTask(targetimage, timer), new Random().nextInt(30 + 1) * 100);
                    }


                    //timer.purge();
                    //timer.schedule(myTimerTask.this, new Random().nextInt(30 + 1) * 100);
                }
            });
        }

    }


}
