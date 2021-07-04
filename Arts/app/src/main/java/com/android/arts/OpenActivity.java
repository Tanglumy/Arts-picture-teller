package com.android.arts;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class OpenActivity extends AppCompatActivity {
    private Button in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        in = findViewById(R.id.in);
        CountDownTimer timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                in.setText(millisUntilFinished / 1000 + "秒");
            }

            public void onFinish() {
                Intent intent = new Intent(OpenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.start();
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(OpenActivity.this,LoginActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
}