package com.example.multicare_indigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileView extends AppCompatActivity {

    TextView FullName;
    EditText emailID, bloodGroup, gender, phoneNum;
    Button savedcontacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Intent intentinput = getIntent();

        FullName = findViewById(R.id.full_name_text);
        emailID = findViewById(R.id.editTextEmail);
        bloodGroup = findViewById(R.id.editTextBlood);
        gender = findViewById(R.id.editTextGender);
        phoneNum = findViewById(R.id.editTextPhone);
        savedcontacts=findViewById(R.id.contactsbutton);

        String user_phone_num = intentinput.getStringExtra("phone_no");
        String user_email = intentinput.getStringExtra("email");
        String user_name = intentinput.getStringExtra("name");
        String user_blood = intentinput.getStringExtra("blood");
        String user_gender = intentinput.getStringExtra("gender");

        FullName.setText(user_name);
        emailID.setText(user_email);
        bloodGroup.setText(user_blood);
        gender.setText(user_gender);
        phoneNum.setText(user_phone_num);
        savedcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Emergency_Contacts.class);
                intent.putExtra("main_phone_num",user_phone_num);
                startActivity(intent);
            }
        });


    }
}