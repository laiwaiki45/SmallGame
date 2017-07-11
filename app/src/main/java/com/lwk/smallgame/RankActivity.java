package com.lwk.smallgame;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        int currentScore = getIntent().getIntExtra("score",1);
        SharedPreferences sharedPreferences = getSharedPreferences("highest" , MODE_PRIVATE);
        int highest = sharedPreferences.getInt("highest", 0);

        TextView currentText = (TextView) findViewById(R.id.curScore);
        TextView highestText = (TextView) findViewById(R.id.highestScore);
        TextView distance = (TextView) findViewById(R.id.distance);

        currentText.setText(String.valueOf(currentScore));
        highestText.setText(String.valueOf(highest));
        distance.setText("You still need "+String.valueOf(highest-currentScore)+" more scores to be the highest");
    }
}
