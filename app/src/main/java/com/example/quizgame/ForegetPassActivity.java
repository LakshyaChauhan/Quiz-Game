

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
import com.google.firebase.auth.FirebaseAuth;

public class ForegetPassActivity extends AppCompatActivity {

    EditText mail;
    Button Continue;
    ProgressBar progressBar;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreget_pass);


        mail = findViewById(R.id.editTextForgetemail);
        Continue = findViewById(R.id.buttonForgetpass);

        progressBar = findViewById(R.id.progressBarForgetto);

        progressBar.setVisibility(View.INVISIBLE);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usermail = mail.getText().toString();

                resetPassFirebase(usermail);
            }
        });

    }

    public  void resetPassFirebase(String usermail){
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Continue.setClickable(false);
                    finish();
                    Toast.makeText(ForegetPassActivity.this,"We have sent you an email to reset your Password.",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForegetPassActivity.this,"Please try again later.",Toast.LENGTH_SHORT).show();
                    Continue.setClickable(true);

                }
            }
        });
    }
}