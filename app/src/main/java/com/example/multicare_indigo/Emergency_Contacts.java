package com.example.multicare_indigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Emergency_Contacts extends AppCompatActivity {
    private EditText fullname, phone, blood;
    private Button save, display;
    Task<Void> reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        fullname = findViewById(R.id.name);
        blood = findViewById(R.id.blood);
        phone = findViewById(R.id.phone);
        save = findViewById(R.id.save);
        display = findViewById(R.id.display);

        Intent intent = getIntent();
        String main_phone = intent.getStringExtra("main_phone_num");

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayContacts.class);
                intent.putExtra("phone_no", main_phone);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullname.getText().toString().trim();
                String phone_no = phone.getText().toString().trim();
                String blood_grp = blood.getText().toString().trim();

                if (!name.isEmpty()) {
                    fullname.setError(null);

                    if (!blood_grp.isEmpty()) {
                        blood.setError(null);

                        if (phone_no.length() < 11) {
                            phone.setError("Enter a valid phone number");
                        } else {
                            phone.setError(null);

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("userdata");

                            Query check_phone = databaseReference.orderByChild("phone_no").equalTo(main_phone);

                            check_phone.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String counting = snapshot.child(main_phone).child("emergency_count").getValue(String.class);
                                        int contactCount = Integer.parseInt(counting);

                                        if (contactCount < 5) {
                                            String serial_con = String.valueOf(contactCount + 1);

                                            Contacts c = new Contacts(serial_con, blood_grp, name, phone_no);

                                            // Save the contact
                                            firebaseDatabase.getReference("userdata")
                                                    .child(main_phone)
                                                    .child(serial_con)
                                                    .setValue(c)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Update success, show toast
                                                                Toast.makeText(getApplicationContext(), "Contact added successfully!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Update failed, show toast
                                                                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                            // Update emergency_count in the database
                                            firebaseDatabase.getReference("userdata")
                                                    .child(main_phone)
                                                    .child("emergency_count")
                                                    .setValue(String.valueOf(contactCount + 1))
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // Handle completion
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            e.printStackTrace();
                                                            // Handle failure
                                                        }
                                                    });
                                        } else {
                                            // Notify user about contact limit
                                            Toast.makeText(getApplicationContext(), "You cannot add more than 5 emergency contacts.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        }
                    } else {
                        blood.setError("Fill up Blood Group");
                    }
                } else {
                    fullname.setError("Fill up Name");
                }
            }
        });
    }
}
