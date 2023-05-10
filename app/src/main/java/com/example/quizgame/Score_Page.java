package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Score_Page extends AppCompatActivity {

    TextView correct , wrong;

    Button playagain,exit;
    String correctAns, wrongAns;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("score");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);

        correct = findViewById(R.id.textViewCorrectAns);
        wrong  = findViewById(R.id.textViewWrongAns);

        playagain = findViewById(R.id.buttonplayAgain);
        exit = findViewById(R.id.buttonExit);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String uid = user.getUid();
                    correctAns = Objects.requireNonNull(snapshot.child(uid).child("correct").getValue()).toString();
                    wrongAns = Objects.requireNonNull(snapshot.child(uid).child("wrong").getValue()).toString();

                    correct.setText(correctAns);
                    wrong.setText(wrongAns);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Score_Page.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}