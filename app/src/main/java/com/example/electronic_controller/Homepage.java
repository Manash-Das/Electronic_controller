package com.example.electronic_controller;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Homepage extends AppCompatActivity {
    TextView name,change;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Button pushBtn;
    String data = "1";
    ImageView status,logoutBtn;
    ProgressBar progress;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        name = findViewById(R.id.Name);
        pushBtn = findViewById(R.id.pushMe);
        progress = findViewById(R.id.progress);
        change = findViewById(R.id.change);
        status = findViewById(R.id.button_status);
        logoutBtn = findViewById(R.id.logoutBtn);
        sp = getSharedPreferences("loginPage",MODE_PRIVATE);
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        DatabaseReference firebaseReference = firebaseDatabase.getReference("UsersData");
        String currentFirebaseUser = FirebaseAuth.getInstance().getUid() ;
        assert currentFirebaseUser != null;
        firebaseReference.child(currentFirebaseUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String,Object>map = (Map<String,Object>)snapshot.getValue();
                    assert map != null;
                    String id = Objects.requireNonNull(map.get("Name")).toString();
                    name.setText(id);
                    data = Objects.requireNonNull(map.get("Button")).toString();

                    if(data.equals("1")){
                        change.setText(R.string.buttonOn);
                        status.setImageResource(R.drawable.on);

                    }
                    else if(data.equals("0")){
                        change.setText(R.string.ButtonOff);
                        status.setImageResource(R.drawable.off);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pushBtn.setOnClickListener(view -> {
            HashMap<String, Object> hashmap = new HashMap<>();
            vb.vibrate(100);
            if(Objects.equals(data, "1")){

                hashmap.put("Button","0");
                data = "0";
                change.setText(R.string.ButtonOff);
                status.setImageResource(R.drawable.off);
            }
            else{
                hashmap.put("Button","1");
                data ="1";
                change.setText(R.string.buttonOn);
                status.setImageResource(R.drawable.on);
            }
            firebaseReference.child(currentFirebaseUser).updateChildren(hashmap);

        });
        logoutBtn.setOnClickListener(view -> {
            sp.edit().putBoolean("login",false).apply();
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            finish();
        });
    }
}