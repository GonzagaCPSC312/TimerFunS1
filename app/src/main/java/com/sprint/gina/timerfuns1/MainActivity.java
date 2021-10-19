package com.sprint.gina.timerfuns1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// TIMERS
// there are several options for timers in android...
// java.util.Timer (not recommended)
// we are going to use android.os.Handler
// (can also look at AlarmManager...)

// a Handler is used to execute "code later"
// "code later" refers to a Runnable
// Runnable is an interface with one method in it: run()
// 2 ways to set up Handler
// 1. have the Runnable run immediately (handler.post(Runnable))
// 2. have the Runnable run with a delay (handler.postDelayed(Runnable, int milliSeconds));

public class MainActivity extends AppCompatActivity {
    Handler handler = null; // null means the "timer" is not running (like running = false)
    int seconds = 0; // number of seconds that have elapsed

    Button startButton;
    Button pauseButton;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up our Runnable that will run on our "timer" tick
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // put our logic for a timer tick here
                // 1. update the seconds by 1
                updateSeconds(seconds + 1);
                // 2. schedule this Runnable to run again in 1000 ms
                handler.postDelayed(this, 1000); // will drift over time
            }
        };

        // start the handler in the startButton's onClick
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler == null) {
                    // the "timer" is not currently running... safe to set one up
                    handler = new Handler();
                    handler.postDelayed(runnable, 1000);
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                }
            }
        });

        // "stop" the handler in the stopButton's onClick
        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setEnabled(false);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer(runnable);
            }
        });

        // task: finish the stopwatch app
        // 1. add the reset logic
        // (set seconds to 0 and doesn't affect timer state unless it is running, then stop it)
        // 2. set up the buttons to appropriately enabled/disabled
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSeconds(0);
                stopTimer(runnable);
            }
        });
    }

    private void stopTimer(Runnable runnable) {
        // remove the Runnable from the Handler's schedule queue
        if (handler != null) {
            // the "timer" is running, stop it...
            handler.removeCallbacks(runnable);
            handler = null; // running = false
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        }
    }

    private void updateSeconds(int newSeconds) {
        seconds = newSeconds;
        TextView secondsTextView = findViewById(R.id.secondsTextView);
        secondsTextView.setText("" + seconds);
    }
}