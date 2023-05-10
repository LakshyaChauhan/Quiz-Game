package com.example.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {

    TextView signUpp, forgetto;
    EditText mail,pass;
    Button sigin;
    SignInButton google;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ProgressBar progressBar;

    GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Intent>  activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpp = findViewById(R.id.textViewloginSignUp);
        forgetto = findViewById(R.id.textViewLoginForget);

        mail = findViewById(R.id.editTextloginmaail);
        pass = findViewById(R.id.editTextTextloginPassword);

        sigin = findViewById(R.id.buttonLoginSignIn);
        google = findViewById(R.id.buttonLoginGooglesin);

        progressBar =findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE
        );


        registerActivityforGSignin();
        signUpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(loginActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

        forgetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(loginActivity.this,ForegetPassActivity.class);
                    startActivity(i);
            }
        });

        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usermail = mail.getText().toString(), password = pass.getText().toString();

                siginFirebase(usermail, password);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signGoogle();
            }
        });
    }

    public void signGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("693916083427-4e7l7tq15kg4evj1o8qkks9on60joer5.apps.googleusercontent.com")
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signin();
    }

    public void registerActivityforGSignin(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode ==RESULT_OK && data!=null){
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                            firebaseSigninWithGoogle(task);
                        }
                    }
                });
    }

    private void firebaseSigninWithGoogle(Task<GoogleSignInAccount>  task){
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String photo_url = Objects.requireNonNull(account.getPhotoUrl()).toString();

            Toast.makeText(this,"Sigin Successful.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(loginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseGoogleAccount(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    FirebaseUser user = auth.getCurrentUser();
                 }else{

                }
            }
        });
    }
    public void signin(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }
    public void siginFirebase(String usermail,String password){
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(usermail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(loginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(loginActivity.this,"Signin Successful.",Toast.LENGTH_SHORT).show();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(loginActivity.this,"Try again later.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user =auth.getCurrentUser();

        if(user!=null){
            Intent i = new Intent(loginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }
}