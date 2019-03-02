package com.example.scorekeeper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final long TICKINTERVAL = 1000;
    private static final String FINISHED = "00:00";

    /**** Main Activity Scorekeeper ****/
    EditText countdown; //Countdown field
    CountDownTimer cdt;
    TextView pointsTeamOne;
    TextView pointsTeamTwo;
    TextView teamOne, teamTwo; //names of the teams
    TextView gamePeriod; //actual Period of the game
    int period;

    SharedPreferences sharedPreferences;
    private static final String MYPREFERENCES = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialize MainActivity Components */
        countdown = findViewById(R.id.countdown);
        pointsTeamOne = findViewById(R.id.pointsTeamOne);
        pointsTeamTwo = findViewById(R.id.pointsTeamTwo);
        teamOne = findViewById(R.id.team1);
        teamTwo = findViewById(R.id.team2);
        gamePeriod = findViewById(R.id.gamePeriod);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, MODE_PRIVATE);
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
                pointsTeamOne.setText("0");
                pointsTeamTwo.setText("0");
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

    //Options for setting team names and game period
    public void openOptions(View view) {
        View v = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.options, null);
        AlertDialog.Builder alertBuilder;
        alertBuilder =  new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setView(v);

        //fields in option view
        final EditText periods = v.findViewById(R.id.periods);
        final EditText nameTeamOne = v.findViewById(R.id.nameTeam1);
        final EditText nameTeamTwo = v.findViewById(R.id.nameTeam2);

        //check if teams and period are already set and if so, set the values in the options view
        if(!(teamOne.getText().toString().equals("Team 1"))) {
            nameTeamOne.setText(teamOne.getText().toString());
        }
        if(!(teamTwo.getText().toString().equals("Team 2"))) {
            nameTeamTwo.setText(teamTwo.getText().toString());
        }
        if(!(gamePeriod.getText().toString() == "")) {
            periods.setText(gamePeriod.getText().toString());
        }

        alertBuilder.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* get data from dialog */
                int per = Integer.parseInt(periods.getText().toString());
                String teamO = nameTeamOne.getText().toString();
                String teamT = nameTeamTwo.getText().toString();

                period = per; //For the countdown -> save int value in class attribute

                //Set Input Values in Main Activity
                gamePeriod.setText(String.valueOf(per));
                teamOne.setText(teamO);
                teamTwo.setText(teamT);
            }
        });
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
}
