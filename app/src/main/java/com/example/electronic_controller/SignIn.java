package com.example.electronic_controller;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SignIn extends AppCompatActivity {
    EditText emailInput,passwordInput;
    TextView signUp;
    Button submit;
    ImageView eye;
    String email,password;
    String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    ProgressBar progressBar;
    CheckBox checkbox;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        signUp = findViewById(R.id.signup);
        submit = findViewById(R.id.submit);
        eye = findViewById(R.id.eye);
        progressBar = findViewById(R.id.progress);
        checkbox = findViewById(R.id.checkbox);
        sp = getSharedPreferences("loginPage",MODE_PRIVATE);
        signUp.setOnClickListener(view -> signUpCLicked());
        submit.setOnClickListener(view -> submitClicked());
        eye.setOnClickListener(view -> eyeCLicked());
        if(sp.getBoolean("login",false)){
            autoSignIn();
        }
    }
    private void autoSignIn(){
            startActivity(new Intent(getApplicationContext(), Homepage.class));
            finish();
    }
    private void signUpCLicked() {
        Intent intent = new Intent(getApplicationContext(),RegisterPage.class);
        startActivity(intent);
        finish();
    }
    private void eyeCLicked() {
        if(passwordInput.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
            passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eye.setImageResource(R.drawable.lock);
        }else{
            passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eye.setImageResource(R.drawable.lock_open);
        }
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
    public void submitClicked(){
        if(checkCredential()) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if(checkbox.isChecked()){
                        sp.edit().putBoolean("login",true).apply();
                        sp.edit().putString("username",email).apply();
                        sp.edit().putString("password",password).apply();
                    }
                    Toast.makeText(SignIn.this, "Successfully login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Homepage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            });
            progressBar.setVisibility(View.GONE);
        }
    }
}