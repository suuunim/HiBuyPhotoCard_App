package com.example.hibuyphotocard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;


public class FindInfoActivity  extends AppCompatActivity{


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