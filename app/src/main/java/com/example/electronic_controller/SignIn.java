package com.example.electronic_controller;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class SignIn extends AppCompatActivity {
    EditText emailInput,passwordInput;
    TextView signUp;
    Button submit;
    ImageView eye;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email,password;
    String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        signUp = findViewById(R.id.signup);
        submit = findViewById(R.id.submit);
        eye = findViewById(R.id.eye);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),RegisterPage.class);
            startActivity(intent);
            finish();
        });
        submit.setOnClickListener(view -> {
            Toast.makeText(SignIn.this, "Completed", Toast.LENGTH_SHORT).show();
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();
            if(checkCredential()) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignIn.this, "Successfully login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Userid or password is invalid", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        eye.setOnClickListener(view -> {
            if(passwordInput.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eye.setImageResource(R.drawable.lock);
            }else{
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                eye.setImageResource(R.drawable.lock_open);
            }
        });
    }
    public boolean checkCredential(){
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        if(email.matches(emailRegex)){
            if(password.matches(passwordRegex)){
                return true;
            }
            else{
                passwordInput.setError("Error");
                return false;
            }
        }
        emailInput.setError("Error");
        return false;
    }
}