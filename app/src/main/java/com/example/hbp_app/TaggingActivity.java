package com.example.hbp_app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class TaggingActivity extends AppCompatActivity {
    ImageButton tagging_next_btn;
    ImageButton tagging_store_btn;
    Dialog dilaog01; // 커스텀 다이얼로그
    Dialog dilaog02; // 임시저장

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagging_first);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tagging_next_btn  = findViewById(R.id.tagging_next_btn);
//        tagging_store_btn = findViewById(R.id.tagging_store_btn);



            dilaog01 = new Dialog(TaggingActivity.this);       // Dialog 초기화
            dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
            dilaog01.setContentView(R.layout.dilaog01);             // xml 레이아웃 파일과 연결

            // 버튼: 커스텀 다이얼로그 띄우기
            findViewById(R.id.tagging_next_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog01(); // 아래 showDialog01() 함수 호출
                }
            });






            }






    // dialog01을 디자인하는 함수
    public void showDialog01(){
        dilaog01.show(); // 다이얼로그 띄우기

        // 아니오 직접수정하기버튼
        Button noBtn = dilaog01.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                SearchDialogActivity dialog = new SearchDialogActivity(TaggingActivity.this);
                dialog.showDialog();


                dilaog01.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        dilaog01.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 원하는 기능 구현
                Intent intent = new Intent(TaggingActivity.this,WritingActivity.class );
                startActivity(intent);
                finish();           // 앱 종료
            }
        });
    }









}
