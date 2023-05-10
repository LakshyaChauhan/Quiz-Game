package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText mail,pass;
    ProgressBar progressBar;
    Button signuup;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mail = findViewById(R.id.editTextSignUpEmail);
        pass = findViewById(R.id.editTextTextSignUpPass);

        signuup = findViewById(R.id.btSignUp);
        progressBar = findViewById(R.id.progressBarSignup);

        progressBar.setVisibility(View.INVISIBLE);

        signuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signuup.setClickable(false);

                String email = mail.getText().toString(), password = pass.getText().toString();
                signUpFirebase(email,password);


            }
        });
    }

    void signUpFirebase(String email,String password){
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this,"Your account is created.",Toast.LENGTH_SHORT).show();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(SignUpActivity.this,"Please try after sometime.",Toast.LENGTH_SHORT).show();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });
    }
}