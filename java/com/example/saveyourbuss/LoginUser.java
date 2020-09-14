package com.example.saveyourbuss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUser extends AppCompatActivity {

    Button login;
    TextView  signup;
    EditText usrEmail,password;
    String mail,pass;
    private FirebaseAuth mFirebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        mFirebaseAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.button_Login);
        signup = findViewById(R.id.textView_Signup);

        usrEmail = findViewById(R.id.editText_Log_email);
        password = findViewById(R.id.editText_Log_password);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUser.this,RegisterUser.class));
            }
        });


    }

    private void checkValidation() {

        mail = usrEmail.getText().toString();
        pass = password.getText().toString();

        if (mail.isEmpty()){
            usrEmail.setError("required!");
            return;
        }else if (pass.isEmpty()){
            password.setError("required!");
            return;
        }else{
            progressBar.setVisibility(View.VISIBLE);
            loginUser();
        }
    }

    private void loginUser() {


        mFirebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(LoginUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(LoginUser.this,MapsActivity.class));

                }else if (!task.isSuccessful()){
                    Toast.makeText(LoginUser.this, "Error Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
