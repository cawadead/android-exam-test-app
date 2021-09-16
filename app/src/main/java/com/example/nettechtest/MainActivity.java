package com.example.nettechtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button mode1;
    private Button mode2;
    private Button mode3;
    private Button about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); //установка отображаемого слоя

        mode1 = findViewById(R.id.mode1); //"соединение" элемента в java и xml
        mode1.setOnClickListener(new View.OnClickListener() { //смена активити
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("mode", 1);
                startActivity(intent);
            }
        });

        mode2 = findViewById(R.id.mode2); //"соединение" элемента в java и xml
        mode2.setOnClickListener(new View.OnClickListener() { //смена активити
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("mode", 2);
                startActivity(intent);
            }
        });

        mode3 = findViewById(R.id.mode3); //"соединение" элемента в java и xml
        mode3.setOnClickListener(new View.OnClickListener() { //смена активити
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("mode", 3);
                startActivity(intent);
            }
        });

        about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() { //смена активити
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}