package com.example.hbp_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class FindInfoActivity extends AppCompatActivity{


    private EditText mail;
    private Button buttonFind;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        mail=findViewById(R.id.find_PW);
        buttonFind=findViewById(R.id.button_find_PW);
        firebaseAuth = FirebaseAuth.getInstance();

       buttonFind.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               firebaseAuth.sendPasswordResetEmail(mail.getText().toString())
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText(FindInfoActivity.this, "비밀번호 재설정 메일을 보냈습니다", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });



           }
       });


    }




}