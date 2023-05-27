package com.example.stopwatch;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset, getPauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //화면 꺼짐 방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("시간: %s");

        Button startBtn = findViewById(R.id.start_btn);
        Button stopBtn = findViewById(R.id.stop_btn);
        Button resetBtn = findViewById(R.id.reset_btn);

        //시작
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running){
                    chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });
        //중지
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(running){
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });
        //저장하기
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                getPauseOffset = pauseOffset;//걸린 시간 저장 받기: 1000 == 1초

                //걸린 시간 알림(text 변형해서 사용)
                Toast.makeText(getApplicationContext(), "시간 확인: "+getPauseOffset, Toast.LENGTH_LONG).show();

                pauseOffset = 0;
                chronometer.stop();
                running = false;

                //캘린더로 화면 전환-나중에 캘린더 액티비티로 변경해서 사용하세요
                //매니패스트에 DayActivity 등록한 것 캘린더 액티비티로 변경!!
                myStartActivity_nonremove(DayActivity.class);
            }
        });
    }

    //강제종료, 뒤로가기 종료에서 값 저장 방법?-채빈

    //모앱에 함수 이미 존재함(이건 임시로 생성)-기존 액티비티 무삭제 버전(뒤로가기 하면 이전 액티비티 나타남)
    private void myStartActivity_nonremove(Class c){

        Intent intent= new Intent(this,c);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}