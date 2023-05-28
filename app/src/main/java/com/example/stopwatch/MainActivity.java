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
    private final long finishtimeed = 1000;
    private long presstime = 0;


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

//    @Override #한번 눌렀을 때 눌렀음을 경고
//    public boolean onKeyDown(int keycode, KeyEvent event) {
//        if(keycode ==KeyEvent.KEYCODE_BACK) {
//            Toast.makeText(this, "뒤로가기버튼이 눌렸습니다",Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//        return false;
//    }

    //모앱에 함수 이미 존재함(이건 임시로 생성)-기존 액티비티 무삭제 버전(뒤로가기 하면 이전 액티비티 나타남)
    private void myStartActivity_nonremove(Class c){

        Intent intent= new Intent(this,c);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //정지 누른 후, 뒤로 가기 버튼 눌렀을 때 캘린더에 저장
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - presstime;

        if (0 <= intervalTime && finishtimeed >= intervalTime) {//130~133은 73번쩨줄에서 가져옴
            chronometer.setBase(SystemClock.elapsedRealtime());
            getPauseOffset = pauseOffset;
            //걸린 시간 알림(text 변형해서 사용)
            Toast.makeText(getApplicationContext(), "시간 확인: "+getPauseOffset, Toast.LENGTH_LONG).show();
            //81번째 줄
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            finish();
        }
        else {//그냥 2번터치 넣고 싶어서 넣은거라 빼도 됨
            presstime = tempTime;
            Toast.makeText(getApplicationContext(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }
}