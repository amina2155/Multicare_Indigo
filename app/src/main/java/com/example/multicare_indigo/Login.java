package com.example.multicare_indigo;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isUserLoggedIn()) {
            // Retrieve saved information
            String phone_no_fromPrefs = getSavedPreference("phone_no");
            String email_fromPrefs = getSavedPreference("email");
            String name_fromPrefs = getSavedPreference("name");
            String blood_fromPrefs = getSavedPreference("blood");
            String gender_fromPrefs = getSavedPreference("gender");

            // Redirect to HomeMenu
            redirectToHomeMenu(phone_no_fromPrefs, email_fromPrefs, name_fromPrefs, blood_fromPrefs, gender_fromPrefs);
        }

        signupButton = findViewById(R.id.signup);
        loginButton = findViewById(R.id.login);
        forgetpass = findViewById(R.id.forgotPassword);
        loginPhone = findViewById(R.id.signInPhoneNumberId);
        password = findViewById(R.id.password);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetOTP.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_id = loginPhone.getText().toString();
                String password_ = password.getText().toString();

                if (!phone_id.isEmpty()) {
                    loginPhone.setError(null);
                    if (!password_.isEmpty()) {
                        final String userPhoneNumber = loginPhone.getText().toString();
                        final String userPassword = password.getText().toString();

                        performLogin(userPhoneNumber, userPassword);

                    } else {
                        password.setError("Please enter the password");
                    }
                } else {
                    loginPhone.setError("Please enter your phone number ");
                }
            }
        });
    }

    private void performLogin(String userPhoneNumber, String userPassword) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("userdata");

        Query check_phone = databaseReference.orderByChild("phone_no").equalTo(userPhoneNumber);

        check_phone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    loginPhone.setError(null);
                    String password_check = snapshot.child(userPhoneNumber).child("password").getValue(String.class);
                    if (password_check.equals(userPassword)) {
                        password.setError(null);
                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();

                        String phone_no_fromDB = userPhoneNumber;
                        String email_fromDB = snapshot.child(userPhoneNumber).child("email").getValue(String.class);
                        String name_fromDB = snapshot.child(userPhoneNumber).child("name").getValue(String.class);
                        String blood_fromDB = snapshot.child(userPhoneNumber).child("blood").getValue(String.class);
                        String gender_fromDB = snapshot.child(userPhoneNumber).child("gender").getValue(String.class);

                        // Save login credentials
                        saveLoginCredentials(phone_no_fromDB, email_fromDB, name_fromDB, blood_fromDB, gender_fromDB);

                        // Redirect to HomeMenu
                        redirectToHomeMenu(phone_no_fromDB, email_fromDB, name_fromDB, blood_fromDB, gender_fromDB);
                    } else {
                        password.setError("Wrong Password");
                    }
                } else {
                    loginPhone.setError("User does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void redirectToHomeMenu(String phone_no, String email, String name, String blood, String gender) {
        Intent intent = new Intent(getApplicationContext(), HomeMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra("name", name);
        intent.putExtra("phone_no", phone_no);
        intent.putExtra("email", email);
        intent.putExtra("blood", blood);
        intent.putExtra("gender", gender);

        startActivity(intent);
        finish();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.contains("phone_no");
    }

    private void saveLoginCredentials(String phone_no, String email, String name, String blood, String gender) {
        // Save user information to SharedPreferences
        savePreference("phone_no", phone_no);
        savePreference("email", email);
        savePreference("name", name);
        savePreference("blood", blood);
        savePreference("gender", gender);
    }

    private void savePreference(String key, String value) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getSavedPreference(String key) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
