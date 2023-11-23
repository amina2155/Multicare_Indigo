package com.example.multicare_indigo;

import static com.example.multicare_indigo.R.id.getOTPButton;
import static com.example.multicare_indigo.R.id.phoneNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class GetOTP extends AppCompatActivity {


    EditText enetered_number ;
    Button getButton ;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);

         enetered_number = findViewById(R.id.phoneNumber);
         getButton = findViewById(R.id.getOTPButton);



        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!enetered_number.getText().toString().trim().isEmpty())
                {
                    enetered_number.setError(null);
                    if(enetered_number.getText().toString().trim().length() == 11)
                    {
                        String otpTo = "+88"+enetered_number.getText().toString().trim();
                        sendOTP(otpTo);
                    }
                    else
                    {
                        enetered_number.setError("Enter a correct phone number");
                    }
                }
                else
                {
                    enetered_number.setError("Please enter your phone number");
                }
            }

        }); 
    }

    private void sendOTP(String entered_num)
    {
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
            getButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            getButton.setVisibility(View.VISIBLE);
            Toast.makeText(GetOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            getButton.setVisibility(View.VISIBLE);
            Toast.makeText(GetOTP.this, "Code sent", Toast.LENGTH_SHORT).show();
            Intent intent  =  new Intent(getApplicationContext(),EnterOTP.class);
            Toast.makeText(GetOTP.this, enetered_number.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            intent.putExtra("phone_num", enetered_number.getText().toString().trim());
            intent.putExtra("backendotp", backendotp);
            startActivity(intent);
        }
    };

}