package com.example.electronic_controller;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView name;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Button pushBtn;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        name = findViewById(R.id.Name);
        pushBtn = findViewById(R.id.pushMe);
        DatabaseReference firebaseReference = firebaseDatabase.getReference("UsersData");
        String currentFirebaseUser = FirebaseAuth.getInstance().getUid() ;
        Toast.makeText(getApplicationContext(), currentFirebaseUser, Toast.LENGTH_SHORT).show();
        assert currentFirebaseUser != null;
        firebaseReference.child(currentFirebaseUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(Homepage.this, String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();

                    Map<String,Object>map = (Map<String,Object>)snapshot.getValue();
                    assert map != null;
                    String id = Objects.requireNonNull(map.get("Name")).toString();
                    Toast.makeText(Homepage.this, id, Toast.LENGTH_SHORT).show();
                    String phone = Objects.requireNonNull(map.get("Phone")).toString();
                    data = Objects.requireNonNull(map.get("Button")).toString();
                    name.setText(id+"\n\n"+phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pushBtn.setOnClickListener(view -> {
            HashMap<String, Object> hashmap = new HashMap<>();
            if(Objects.equals(data, "1")){
                hashmap.put("Button","0");
                data = "0";
            }
            else{
                hashmap.put("Button","1");
                data ="1";
            }
            firebaseReference.child(currentFirebaseUser).updateChildren(hashmap);

        });
    }
}