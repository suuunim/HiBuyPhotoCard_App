package com.example.hbp_app;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private TextView textView_signIn;
    private TextView textView_find;
    private Button login_btn;
    private FirebaseAuth mAuth;
    private EditText textView_passwd;
    private EditText textView_id;
    private ArrayList<SearchItemList> item;
    private ArrayList<SearchItemList> itemList2;
    SearchItemList sellItemList;

    private HashMap sell1 = new HashMap();
    private DatabaseReference SellItemList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        textView_id = findViewById(R.id.textView_id);
        textView_passwd = findViewById(R.id.textView_passwd);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        DatabaseReference mData = FirebaseDatabase.getInstance().getReference(); //firebase 연결
        SellItemList = mData.child("Sell");

        item = new ArrayList<>(); //검색 결과화면으로 넘길 리스트
        item.clear();


        SellItemList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshoted) {
                sell1 = (HashMap) snapshoted.getValue();
                sell1.size();
                for(int i= sell1.size();i>0;i--){
                    for(DataSnapshot snap :snapshoted.getChildren() ){
                        String sell = snap.getKey();
                        String num = sell.replace("sell","");
                        Integer num2= Integer.valueOf(num);
                        System.out.println(num);
                        System.out.println(i);
                        System.out.println("----------");
                        Log.d("ㅇㅇ","ㄴㅁㅇ");
                        if(num2 == i){

                            sellItemList = snap.getValue(SearchItemList.class);
                            item.add(sellItemList);
                            break;

                        }
                    }
                }
//                                     for(DataSnapshot snap :snapshoted.getChildren() ){
////
//                                         String member = snap.child("memberTag").getValue(String.class);
////                                   if(member.equals(((TextView)view).getText().toString())){
//
//                                             sellItemList = snap.getValue(SearchItemList.class);
////                                             item.add(sellItemList);
//                                             item.add(0,sellItemList); //모든 판매글 저장 //최애 맴버의 글만 보여줌
//
//
//
////
//                    }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(textView_id.getText().toString(),
                        textView_passwd.getText().toString());
            }
        });

        // textview에 underline 긋는 방법 (setPaintFlags 사용하여 underline text flag 쓰기)
        textView_signIn = findViewById(R.id.textView_signIn);
        textView_find = findViewById(R.id.textView_find);

        textView_signIn.setPaintFlags(textView_signIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView_find.setPaintFlags(textView_find.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        textView_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textView_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindInfoActivity.class);
                startActivity(intent);

            }
        });


    }

    private void logIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("itemList",item);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

}
