package com.example.multicare_indigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button signupButton, loginButton;

    EditText loginPhone, password;
    TextView forgetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupButton = findViewById(R.id.signup);
        loginButton = findViewById(R.id.login);

        forgetpass = findViewById(R.id.forgotPassword);

        loginPhone = findViewById(R.id.signInPhoneNumberId);
        password = findViewById(R.id.password);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GetOTP.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_id = loginPhone.getText().toString();
                String password_ = password.getText().toString();

                if(!phone_id.isEmpty())
                {
                        loginPhone.setError(null);
                        if(!password_.isEmpty())
                        {
                            final String userPhoneNumber = loginPhone.getText().toString();
                            final String userPassword = password.getText().toString();

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("userdata");

                            Query check_phone = databaseReference.orderByChild("phone_no").equalTo(userPhoneNumber);

                            check_phone.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists())
                                    {
                                       loginPhone.setError(null);
                                       String password_check = snapshot.child(userPhoneNumber).child("password").getValue(String.class);
                                       if(password_check.equals(userPassword))
                                       {
                                           password.setError(null);
                                           Toast.makeText(getApplicationContext(), "Login successful",Toast.LENGTH_LONG);

                                           String phone_no_fromDB = userPhoneNumber;
                                           String email_fromDB = snapshot.child(userPhoneNumber).child("email").getValue(String.class);
                                           String name_fromDB = snapshot.child(userPhoneNumber).child("name").getValue(String.class);
                                           String blood_fromDB = snapshot.child(userPhoneNumber).child("blood").getValue(String.class);
                                           String gender_fromDB = snapshot.child(userPhoneNumber).child("gender").getValue(String.class);

                                           Intent intent = new Intent(getApplicationContext(), HomeMenu.class);

                                           intent.putExtra("name", name_fromDB);
                                           intent.putExtra("phone_no", phone_no_fromDB);
                                           intent.putExtra("email", email_fromDB);
                                           intent.putExtra("blood", blood_fromDB);
                                           intent.putExtra("gender", gender_fromDB);

                                           startActivity(intent);
                                           finish();
                                       }
                                       else
                                       {
                                           password.setError("Wrong Password");
                                       }
                                    }
                                    else
                                    {
                                        loginPhone.setError("User does not exist");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else
                        {
                            password.setError("Please enter the password");
                        }
                    }

                else
                {
                    loginPhone.setError("Please enter your phone number ");
                }
            }
        });
    }

}