package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThreeButtonActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private Button btnSolicitudes;
    private Button btnPreferencias;
    private Button btnAjustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_button);

        // Obtenemos referencias a los componentes
        btnSolicitudes = (Button) findViewById(R.id.btnSolicitudes);
        btnPreferencias = (Button) findViewById(R.id.btnPreferencias);
        btnAjustes = (Button) findViewById(R.id.btnAjustes);

        // Asignamos listeners

        // -- Botón para acceder a la activity de solicitudes y matches --
        btnSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent usersListIntent = new Intent(ThreeButtonActivity.this, UsersRecyclerActivity.class);
                startActivity(usersListIntent);
            }
        });

        // -- Botón para acceder a la activity de edición de preferencias --
        btnPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferencesIntent = new Intent(ThreeButtonActivity.this, SearchPreferencesActivity.class);
                startActivity(preferencesIntent);
            }
        });

        // -- Botón para acceder a la activity de ajustes de usuario --
        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(ThreeButtonActivity.this, UserSettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }
}