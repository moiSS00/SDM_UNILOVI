package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity3 extends AppCompatActivity {

    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        btnSiguiente = (Button) findViewById(R.id.btnSiguienteRegistro3);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se pasara a la siguiente pantalla de registro
                Intent postIntent = new Intent(SignUpActivity3.this, SignUpActivity4.class);

                // Comenzamos siguiente parte del registro
                startActivity(postIntent);
                finish();
            }
        });
    }
}