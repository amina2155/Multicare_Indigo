package com.example.multicare_indigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int timer = 4000;
    ImageView imageViewLogo;
    TextView AppName;
    Animation upperAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        imageViewLogo = findViewById(R.id.imageViewLogo);
        AppName = findViewById(R.id.AppName);

        upperAnimation = AnimationUtils.loadAnimation(this, R.anim.upper_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageViewLogo.setAnimation(upperAnimation);
        AppName.setAnimation(bottomAnimation);

        new Handler().postDelayed
        (
            () -> {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                  },timer
        );

    }
}