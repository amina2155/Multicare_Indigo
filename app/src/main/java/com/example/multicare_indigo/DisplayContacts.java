package com.example.multicare_indigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DisplayContacts extends AppCompatActivity {

    EditText c1, c2, c3, c4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        Intent intent = getIntent();
        String main_phone = intent.getStringExtra("phone_no");

        c1 = findViewById(R.id.contact1);
        c2 = findViewById(R.id.contact2);
        c3 = findViewById(R.id.contact3);
        c4 = findViewById(R.id.contact4);

        System.out.println(main_phone);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("userdata");

        Query check_phone = databaseReference.orderByChild("phone_no").equalTo(main_phone);

        check_phone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    String emergency1 = snapshot.child(main_phone).child(String.valueOf(1)).child("number").getValue(String.class);
                    c1.setText(emergency1);
                    String emergency2 = snapshot.child(main_phone).child(String.valueOf(2)).child("number").getValue(String.class);
                    c2.setText(emergency2);
                    String emergency3 = snapshot.child(main_phone).child(String.valueOf(3)).child("number").getValue(String.class);
                    c3.setText(emergency3);
                    String emergency4 = snapshot.child(main_phone).child(String.valueOf(4)).child("number").getValue(String.class);
                    c4.setText(emergency4);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}