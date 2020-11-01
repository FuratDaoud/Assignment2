package edu.cs.assignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    private EditText edtText;
    private TextView txtView;
    private Button btnset;
    private Button btnPause;
    private Button btnstop;
    private Button btnstart;
    private CountDownTimer countTime;
    private boolean timeRunning;
    private long startTime;
    private long leftTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();

        edtText = findViewById(R.id.edtTxt);
        txtView = findViewById(R.id.txtview);
        btnset = findViewById(R.id.button_set);
        btnPause = findViewById(R.id.btnstart);
        btnstop = findViewById(R.id.btnstop);
        btnstart = findViewById(R.id.btnstart);
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edtText.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(edu.cs.assignment2.MainActivity2.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(edu.cs.assignment2.MainActivity2.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                edtText.setText("");
                btnPause.setEnabled(true);

            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();

            }
        });

    }

    private void setTime(long milliseconds) {
        startTime = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        endTime = System.currentTimeMillis() + leftTime;
        countTime = new CountDownTimer(leftTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                leftTime = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeRunning = false;
            }
        }.start();
        timeRunning = true;
    }

    private void pauseTimer() {
        countTime.cancel();
        timeRunning = false;
        //  updateWatchInterface();
    }

    private void resetTimer() {
        leftTime = startTime;
        updateCountDownText();
    }

    private void updateCountDownText() {
        int hours = (int) (leftTime / 1000) / 3600;
        int minutes = (int) ((leftTime / 1000) % 3600) / 60;
        int seconds = (int) (leftTime / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        txtView.setText(timeLeftFormatted);
    }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager i = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            i.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", startTime);
        editor.putLong("millisLeft", leftTime);
        editor.putBoolean("timerRunning", timeRunning);
        editor.putLong("endTime", endTime);
        editor.apply();
        if (countTime != null) {
            countTime.cancel();

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        startTime = prefs.getLong("startTimeInMillis", 600000);
        leftTime = prefs.getLong("millisLeft", startTime);
        timeRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        if (timeRunning) {
            endTime = prefs.getLong("endTime", 0);
            leftTime = endTime - System.currentTimeMillis();
            if (leftTime < 0) {
                leftTime = 0;
                timeRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }

    }

    public void onClickStart(View view) {
        startTimer();

        btnstop.setEnabled(true);
        btnstart.setEnabled(true);
        btnPause.setEnabled(true);

    }

    public void onClickPause(View view) {
        btnstop.setEnabled(true);
        btnstart.setEnabled(false);
        btnPause.setEnabled(true);
        pauseTimer();

    }

}
