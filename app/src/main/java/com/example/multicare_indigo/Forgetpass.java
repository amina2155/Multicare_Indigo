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
public class Forgetpass extends AppCompatActivity {

    EditText newp, confirmp;
    Button change;

    Task<Void> reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        newp = findViewById(R.id.setNew);
        confirmp = findViewById(R.id.confirm);
        change = findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasscode();
            }
        });
    }

    private void changePasscode() {
        String pass = newp.getText().toString();
        String confirmPassword = confirmp.getText().toString();
        Intent intentInput = getIntent();
        String phone_num = intentInput.getStringExtra("phone_number_current");

        if (!pass.isEmpty()) {
            if (pass.length() < 6) {
                newp.setError("Too short!");
            } else {
                newp.setError(null);
                if (!confirmPassword.isEmpty()) {
                    if (confirmPassword.equals(pass)) {
                        confirmp.setError(null);

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        reference = firebaseDatabase.getReference("userdata").child(phone_num).child("password").setValue(pass)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish(); // Optional: Finish the current activity
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        confirmp.setError("Passwords do not match");
                    }
                } else {
                    confirmp.setError("Please confirm your password");
                }
            }
        } else {
            newp.setError("Enter something");
        }
    }
}
