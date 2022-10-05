package com.example.electronic_controller;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    EditText emailInput,passwordInput;
    TextView signUp;
    Button submit;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        signUp = findViewById(R.id.signup);
        submit = findViewById(R.id.submit);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),RegisterPage.class);
            startActivity(intent);
            finish();
        });
        submit.setOnClickListener(view -> {
            Toast.makeText(SignIn.this, "Completed", Toast.LENGTH_SHORT).show();
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(SignIn.this, "Successfully login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Homepage.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Userid or password is invalid", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}