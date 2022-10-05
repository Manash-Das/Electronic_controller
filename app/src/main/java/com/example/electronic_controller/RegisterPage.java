package com.example.electronic_controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    TextView signIn;
    EditText emailInput,passwordInput,confirmPasswordInput;
    Button submitBtn;
    ProgressBar progressBar;
    ImageView eye;
    EditText nameInput,phoneInput;
    String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    String email,password,confirmPassword,name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.passwd);
        submitBtn = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressbar);
        confirmPasswordInput = findViewById(R.id.conf_passwd);
        phoneInput = findViewById(R.id.phoneNumber);
        nameInput = findViewById(R.id.name);
        signIn = findViewById(R.id.signIn);
        eye = findViewById(R.id.eye);
        submitBtn.setOnClickListener(view -> {
            if(checkCredential()){
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String currentFirebaseUser = FirebaseAuth.getInstance().getUid() ;
                        DatabaseReference reference = firebaseDatabase.getReference("UsersData");
                        HashMap<String, String> hashmap = new HashMap<>();
                        hashmap.put("Name",name);
                        hashmap.put("Phone",phone);
                        hashmap.put("Button","0");
                        assert currentFirebaseUser != null;
                        reference.child(currentFirebaseUser).setValue(hashmap);
                        Intent intent = new Intent(getApplicationContext(),Homepage.class);
//                            Toast.makeText(RegisterPage.this, currentFirebaseUser, Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();

                    }
                    else{
                        Toast.makeText(RegisterPage.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),SignIn.class);
            startActivity(intent);
            finish();
        });
        eye.setOnClickListener(view -> {
            if(passwordInput.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }else{
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                confirmPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
    }
    public boolean checkCredential(){
        name = nameInput.getText().toString();
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        confirmPassword = confirmPasswordInput.getText().toString();
        phone = phoneInput.getText().toString();
        if(email.matches(emailRegex)){
            if(password.matches(passwordRegex)){
                if(Objects.equals(password, confirmPassword)) {
                    return true;
                }
                else{
                    confirmPasswordInput.setError("Doesn't matches with password");
                    return false;
                }
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