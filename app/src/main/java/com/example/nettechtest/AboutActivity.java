package com.example.nettechtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class AboutActivity extends AppCompatActivity {
    private Button about_to_menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity); //установка отображаемого слоя
        getSupportActionBar().hide();
        about_to_menu = findViewById(R.id.about_to_menu);
        about_to_menu.setOnClickListener(new View.OnClickListener() { //смена активити
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}