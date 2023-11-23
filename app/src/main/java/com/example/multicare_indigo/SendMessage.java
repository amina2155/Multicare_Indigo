package com.example.multicare_indigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SendMessage extends AppCompatActivity {

    Button sendButton;
    String finalMessage ;

    EditText customised;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Intent intentinput = getIntent();

        String gotMessage = intentinput.getStringExtra("emergencyMessage");

        sendButton = (Button) findViewById(R.id.sendMessageButton);
        customised = (EditText) findViewById(R.id.messageId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customised_message = customised.getText().toString();
                if(!customised_message.isEmpty())
                {
                    finalMessage = customised_message + "\n" + "Here's my location\n" + gotMessage;
                }
                else
                {
                    finalMessage = "Help me, I am in danger. Here's my location\n" + gotMessage;
                }
                Intent intentInput = getIntent();
                String numberFrom = intentInput.getStringExtra("numberfrom");

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("userdata");

                Query check_phone = databaseReference.orderByChild("phone_no").equalTo(numberFrom);

                check_phone.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()) {

                            for(int i = 1 ; i < 5 ; i++)
                            {
                                String emergency = snapshot.child(numberFrom).child(String.valueOf(i)).child("number").getValue(String.class);
                                System.out.println(emergency);
                                sendMessage(emergency);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void sendMessage(String em_ph) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ActivityCompat.requestPermissions(SendMessage.this, new String[] {android.Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
            ActivityCompat.requestPermissions(SendMessage.this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
            smsManager.sendTextMessage(em_ph, null, finalMessage, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(getApplicationContext(), HomeMenu.class);
                startActivity(intent2);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}