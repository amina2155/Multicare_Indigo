package com.example.multicare_indigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class EnterOTP extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    TextView sendAgain;

    String tempo_num;
    String getOTPbackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        String phone_number;

         tempo_num = getIntent().getStringExtra("phone_num");
         getOTPbackend = getIntent().getStringExtra("backendotp");

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        final Button submitbutton = findViewById(R.id.submitButton);
        sendAgain = findViewById(R.id.sendAgainEditText);
        final ProgressBar  progressbarverifyOTP = findViewById(R.id.progressBar2);


        phone_number = String.format("+88-%s" , tempo_num);


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!otp1.getText().toString().trim().isEmpty() &&
                   !otp2.getText().toString().trim().isEmpty() &&
                   !otp3.getText().toString().trim().isEmpty() &&
                   !otp4.getText().toString().trim().isEmpty() &&
                   !otp5.getText().toString().trim().isEmpty() &&
                   !otp6.getText().toString().trim().isEmpty() )
                {
                    //Toast.makeText(EnterOTP.this, "OTP verify", Toast.LENGTH_SHORT).show();
                    String entercodeotp =   otp1.getText().toString().trim() +
                                            otp2.getText().toString().trim() +
                                            otp3.getText().toString().trim() +
                                            otp4.getText().toString().trim() +
                                            otp5.getText().toString().trim() +
                                            otp6.getText().toString().trim() ;

                    if(getOTPbackend != null)
                    {
                        progressbarverifyOTP.setVisibility(View.VISIBLE);
                        submitbutton.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getOTPbackend, entercodeotp
                        );

                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressbarverifyOTP.setVisibility(View.VISIBLE);
                                submitbutton.setVisibility(View.INVISIBLE);

                                if(task.isSuccessful())
                                {
                                    System.out.println(tempo_num);

                                    Toast.makeText(EnterOTP.this, "Successful !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Forgetpass.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("phone_number_current", tempo_num);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(EnterOTP.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(EnterOTP.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(EnterOTP.this, "Please enter all numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });
        numberotpmove();
    }

    //This part of code is for moving after filling up one box
    private void numberotpmove() {

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    otp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    otp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    otp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    otp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}