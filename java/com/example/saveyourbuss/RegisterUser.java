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

public class RegisterUser extends AppCompatActivity {
    Button signUp;
    EditText email,pass;
    ProgressBar progressBar;
    TextView signIn;

    String mail,password;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mFirebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar_UsrReg);
        signIn = findViewById(R.id.textView_Logme);
        signUp = findViewById(R.id.button_Reg);

        email = findViewById(R.id.editText_UserEmail);
        pass = findViewById(R.id.editText_Password);



        progressBar.setVisibility(View.INVISIBLE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterUser.this,LoginUser.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {

        mail = email.getText().toString();
        password = pass.getText().toString();
        if (mail.isEmpty()){
            email.setError("required!");
            return;
        }else if (password.isEmpty()){
            pass.setError("required!");
            return;
        }else if(password.length() < 6 ) {
            Toast.makeText(this, "Password is too short Enter minimum 6 char", Toast.LENGTH_SHORT).show();
            pass.setError("make sure length is 6 or above");
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            registerUser();
        }
    }
    private void registerUser() {
        mFirebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterUser.this, "User is created!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterUser.this,LoginUser.class));
                    finish();
                }else if (!task.isSuccessful()){
                    Toast.makeText(RegisterUser.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
