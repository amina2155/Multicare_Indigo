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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Emergency_Contacts extends AppCompatActivity {
    private EditText fullname,phone,blood;
    private Button save,display;
    Task<Void> reference;
    int contact_serial = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        fullname = (EditText)findViewById(R.id.name);
        blood = (EditText)findViewById(R.id.blood);
        phone = (EditText)findViewById(R.id.phone);
        save=(Button) findViewById(R.id.save);
        display=(Button) findViewById(R.id.display);

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
                if(!name.isEmpty())
                {
                    fullname.setError(null);
                    if(!blood_grp.isEmpty())
                    {
                        phone.setError(null);
                        if(phone_no.length()<11)
                        {
                            phone.setError("Enter a valid phone number");
                        }
                        else {
                            if(!blood_grp.isEmpty())
                            {
                                blood.setError(null);

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("userdata");

                                Query check_phone = databaseReference.orderByChild("phone_no").equalTo(main_phone);

                                check_phone.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists())
                                        {
                                            //System.out.println(main_phone);
                                            String counting = snapshot.child(main_phone).child("emergency_count").getValue(String.class);
                                            System.out.println(counting);
                                            contact_serial = Integer.parseInt(counting) + 1;
                                            System.out.println(main_phone + "   demhkwejhk    " + contact_serial);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                                String serial_con = String.valueOf(contact_serial);

                                Contacts c = new Contacts(serial_con, blood_grp, name, phone_no);

                                firebaseDatabase = FirebaseDatabase.getInstance();
                                reference = firebaseDatabase.getReference("userdata").child(main_phone).child(serial_con).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Contact added successfully!", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                firebaseDatabase = FirebaseDatabase.getInstance();
                                reference = firebaseDatabase.getReference("userdata").child(main_phone).child("emergency_count").setValue(serial_con).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else
                            {
                                blood.setError("Fillup Blood Group");
                            }
                        }
                    }
                    else
                    {
                        phone.setError("Fill up phone number");
                    }
                }

            }
        });




    }
}