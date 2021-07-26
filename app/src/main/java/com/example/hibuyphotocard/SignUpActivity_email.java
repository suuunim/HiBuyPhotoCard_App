package com.example.hibuyphotocard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity_email extends AppCompatActivity {
    SignInButton Google_Login;
    private Button SignButton;
    private static final int RC_SIGN_IN = 1000;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide(); // actionBar 숨기기


        mAuth = FirebaseAuth.getInstance();
        SignButton=findViewById(R.id.button_signbutton);
        SignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


    }


    private void signUp() {
        // 이메일
        EditText emailEditText = findViewById(R.id.signup_email);
        String email = emailEditText.getText().toString();
        // 비밀번호
        EditText passwordEditText = findViewById(R.id.signup_password);
        String password = passwordEditText.getText().toString();

        EditText passwordEditText2 = findViewById(R.id.signup_password_c);
        String password2=passwordEditText2.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignUpActivity_email.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignUpActivity_email.this, "등록 완료", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignUpActivity_email.this, "등록 에러", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }

}