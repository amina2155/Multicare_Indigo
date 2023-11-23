package com.example.multicare_indigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {

    private EditText fullname,signup_email,password,confirm_pass,phone,blood;
    private Button signup;
    private RadioButton male,female;
    private LinearLayout gender_button;
    private String gender;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullname = (EditText)findViewById(R.id.name);
        signup_email = (EditText)findViewById(R.id.signUpEmailEditTextId);
        password = (EditText)findViewById(R.id.signUpPassword);
        confirm_pass = (EditText)findViewById(R.id.confirmPassword);
        phone = (EditText)findViewById(R.id.phone);
        blood = (EditText)findViewById(R.id.blood);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        gender_button = (LinearLayout) findViewById(R.id.radio);
        signup = (Button)findViewById(R.id.signUp);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = signup_email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String confirmPassword = confirm_pass.getText().toString().trim();
                String name = fullname.getText().toString().trim();
                String phone_no = phone.getText().toString().trim();
                String blood_grp = blood.getText().toString().trim();

                if (male.isChecked())
                    gender = "Male";
                else if (female.isChecked())
                    gender = "Female";
                else
                    gender = "Other";

                if(!name.isEmpty())
                {
                    fullname.setError(null);
                    if(!email.isEmpty())
                    {
                        if(email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"))
                        {
                            signup_email.setError(null);
                            if(!pass.isEmpty())
                            {
                                if(pass.length() < 6)
                                {
                                    password.setError("Password Length must not be less than 6 digits");
                                }
                                else
                                {
                                    password.setError(null);
                                    if(!confirmPassword.isEmpty())
                                    {
                                        if(confirmPassword.equals(pass))
                                        {
                                            confirm_pass.setError(null);
                                            if(!phone_no.isEmpty())
                                            {
                                                if(phone_no.length() < 11)
                                                {
                                                    phone.setError("Enter a valid phone number");
                                                }
                                                else
                                                {
                                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("userdata");
                                                    Query check_phone = rootRef.orderByChild("phone_no").equalTo(phone_no);

                                                    check_phone.addListenerForSingleValueEvent(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                // Exist! Do whatever.
                                                                phone.setError("Phone number already exists!");

                                                            } else {
                                                                phone.setError(null);
                                                                if(!blood_grp.isEmpty())
                                                                {
                                                                    blood.setError(null);
                                                                    String otpTo = "+88"+phone.getText().toString().trim();
                                                                    sendOTP(otpTo);

                                                                }
                                                                else
                                                                {
                                                                    blood.setError("Please enter your blood group");
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            phone.setError("Database error");
                                                        }
                                                    });
                                                }
                                            }
                                            else
                                            {
                                                phone.setError("Please enter your phone number");
                                            }
                                        }
                                        else
                                        {
                                            confirm_pass.setError("Passwords do not match");
                                        }
                                    }
                                    else
                                    {
                                        confirm_pass.setError("Please confirm your password");
                                    }
                                }

                            }
                            else
                            {
                                password.setError("Please enter your password");
                            }
                        }
                        else
                        {
                            signup_email.setError("Enter a valid email address");
                        }
                    }
                    else
                    {
                        signup_email.setError("Please enter your mail address");
                    }
                }
                else
                {
                    fullname.setError("Please enter your full name");
                }
            }
        });
    }

    private void sendOTP(String entered_num) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(entered_num)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            signup.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            signup.setVisibility(View.VISIBLE);
            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            signup.setVisibility(View.VISIBLE);
            Toast.makeText(SignupActivity.this, "Code sent", Toast.LENGTH_SHORT).show();

            String FullName = fullname.getText().toString();
            String SignUpMail = signup_email.getText().toString();
            String Password = confirm_pass.getText().toString();
            String PhoneNumber = phone.getText().toString();
            String BloodGroup = blood.getText().toString();
            String Gender = gender;

            Intent intentToOTP = new Intent(getApplicationContext(), EnterOTP_AfterSignUp.class);


            intentToOTP.putExtra("phone_num", phone.getText().toString().trim());
            intentToOTP.putExtra("backendotp", backendotp);

            intentToOTP.putExtra("name", FullName);
            intentToOTP.putExtra("phone_no", PhoneNumber);
            intentToOTP.putExtra("email", SignUpMail);
            intentToOTP.putExtra("blood", BloodGroup);
            intentToOTP.putExtra("gender", Gender);
            intentToOTP.putExtra("passcode", Password);

            startActivity(intentToOTP);
        }
    };

}