package com.example.scorekeeper;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final long TICKINTERVAL = 1000;
    private static final String FINISHED = "00:00";
    EditText countdown; //Countdown field
    CountDownTimer cdt;
    TextView pointsTeamOne;
    TextView pointsTeamTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdown = findViewById(R.id.countdown);
        pointsTeamOne = findViewById(R.id.pointsTeamOne);
        pointsTeamTwo = findViewById(R.id.pointsTeamTwo);
    }

    public void startCountdown(View view) {
        String timeString = countdown.getText().toString();
        long millis = convertTimestringToMillis(timeString);
        countdown.setEnabled(false);
        cdt = new CountDownTimer(millis, TICKINTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String ms = String.format("%02d:%02d",
                                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                countdown.setText(ms);
            }

            @Override
            public void onFinish() {
                countdown.setText(FINISHED);
                countdown.setEnabled(true);
            }
        }.start();
    }

    public long convertTimestringToMillis(String timeString) {
        String[] minutesSeconds = timeString.split(":");
        return Integer.parseInt(minutesSeconds[0]) * 60 * 1000 + Integer.parseInt(minutesSeconds[1]) * 1000;
    }

    public void resetCountdown(View view) {
        countdown.setEnabled(true);
        countdown.setText("");
        cdt.cancel();
    }

    public void setPointTeamOne(View view) {
        int points = Integer.parseInt(pointsTeamOne.getText().toString());
        points++;
        pointsTeamOne.setText(String.valueOf(points));
    }

    public void setPointTeamTwo(View view) {
        int points = Integer.parseInt(pointsTeamTwo.getText().toString());
        points++;
        pointsTeamTwo.setText(String.valueOf(points));
    }
}
