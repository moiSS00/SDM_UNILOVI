package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThreeButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_button);

        Button btnSolicitudes = findViewById(R.id.btnSolicitudes);

        btnSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btnPreferencias = findViewById(R.id.btnPreferencias);

        btnPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferencesIntent = new Intent(ThreeButtonActivity.this, SearchPreferencesActivity.class);
                startActivity(preferencesIntent);
            }
        });

        Button btnAjustes = findViewById(R.id.btnAjustes);

        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}