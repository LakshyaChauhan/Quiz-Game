package com.example.quizgame;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz_Game extends AppCompatActivity {

    TextView time, correct, wrong, question , a,b,c,d;
    Button next , finish;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("Questions");

    String Quizquestions,Qanswer, qa,qb,qc,qd;
    int questionsCount, questionNumber =1, correct1 , wrong1 ;

    CountDownTimer countDownTimer;
    private  static final long TOTAL_TIME = 10000;
    Boolean timerContinue;
    long timerLeft =TOTAL_TIME;


    FirebaseAuth auth  = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

   DatabaseReference reference2 = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);

        time = findViewById(R.id.textViewTime);
        correct = findViewById(R.id.textViewCorrectAns);
        wrong = findViewById(R.id.textViewWrongAns);
        question = findViewById(R.id.textViewQuestion);
        a = findViewById(R.id.textViewa);
        b = findViewById(R.id.textViewb);
        c = findViewById(R.id.textViewc);
        d = findViewById(R.id.textViewd);
        finish = findViewById(R.id.finish);
        next = findViewById(R.id.buttonnext);

        game();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetTimer();
                game();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendScore();
                Intent i = new Intent(Quiz_Game.this,Score_Page.class);
                startActivity(i);
                finish();
            }
        });

      correct1 = 0;
      wrong1 = 0;

        a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pauseTimer();
                if(Qanswer.equals("a")){
                    a.setBackgroundColor(GREEN);
                    correct1 = correct1 +1;
                    correct.setText(""+correct1);
                }else{
                    a.setBackgroundColor(RED);
                    wrong1++;
                    wrong.setText(""+wrong1);
                    setCorrect();
                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                if(Qanswer.equals("b")){
                    b.setBackgroundColor(GREEN);
                    correct1 = correct1 +1;
                    correct.setText(""+correct1);
                }else{
                    b.setBackgroundColor(RED);
                    wrong1++;
                    wrong.setText(""+wrong1);
                    setCorrect();
                }
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                if(Qanswer.equals("c")){
                    c.setBackgroundColor(GREEN);
                    correct1 = correct1 +1;
                    correct.setText(""+correct1);
                }else{
                    c.setBackgroundColor(RED);
                    wrong1++;
                    wrong.setText(""+wrong1);
                    setCorrect();
                }
            }
        });

        d.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pauseTimer();
                if(Qanswer.equals("d")){
                    d.setBackgroundColor(GREEN);
                    correct1 = correct1 +1;
                    correct.setText(""+correct1);
                }else{
                    d.setBackgroundColor(RED);
                    wrong1++;
                    wrong.setText(""+wrong1);
                    setCorrect();
                }
            }
        });


    }

    public void game(){



        a.setBackgroundColor(WHITE);
        b.setBackgroundColor(WHITE);
        c.setBackgroundColor(WHITE);
        d.setBackgroundColor(WHITE);

        starTimer();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                questionsCount = (int)snapshot.getChildrenCount();

                Quizquestions = snapshot.child(String.valueOf(questionNumber)).child("q").getValue().toString();
                Qanswer= snapshot.child(String.valueOf(questionNumber)).child("answer").getValue().toString();
                qa = snapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                qb = snapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                qc = snapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                qd = snapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();;

                question.setText(Quizquestions);
                a.setText(qa);
                b.setText(qb);
                c.setText(qc);
                d.setText(qd);

                if(questionNumber<questionsCount){
                    questionNumber++;
                }else{
                    Toast.makeText(Quiz_Game.this,"You completed all the questions.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Quiz_Game.this,"There is an error.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCorrect(){
        if(Qanswer.equals("a")){
            a.setBackgroundColor(GREEN);
        } else if (Qanswer.equals("b")) {
            b.setBackgroundColor(GREEN);
        }else if (Qanswer.equals("c")) {
            c.setBackgroundColor(GREEN);
        }else{
            d.setBackgroundColor(GREEN);
        }
    }

    public void starTimer(){
        countDownTimer = new CountDownTimer(timerLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerLeft = millisUntilFinished;
                updateCountDownText();

            }


            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                question.setText("Time is Up!");
            }
        }.start();

        timerContinue = true;
    }

    public  void resetTimer(){
        timerLeft = TOTAL_TIME;
        updateCountDownText();
    }

    public void updateCountDownText(){
        int seceond =((int) timerLeft /1000)%60;
        time.setText(""+seceond);
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerContinue = false;
    }

    public  void sendScore(){
        String userUid = user.getUid();
        reference2.child("score").child(userUid).child("correct").setValue(correct1);
        reference2.child("score").child(userUid).child("wrong").setValue(wrong1);
    }
}