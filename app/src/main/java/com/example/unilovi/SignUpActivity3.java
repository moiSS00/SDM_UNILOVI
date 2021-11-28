package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.unilovi.model.User;

public class SignUpActivity3 extends AppCompatActivity {

    private EditText sobreMi;
    private EditText contacto;
    private Button btnSiguiente;

    // Atributos auxiliares
    public static final String USUARIO_REGISTRADO3 = "usuario_registrado3";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        sobreMi = (EditText) findViewById(R.id.editSobreMiRegistro3);
        contacto = (EditText) findViewById(R.id.editContactoRegistro3);
        btnSiguiente = (Button) findViewById(R.id.btnSiguienteRegistro3);

        user = getIntent().getParcelableExtra(SignUpActivity2.USUARIO_REGISTRADO2);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sobreMiContent = sobreMi.getText().toString();
                String contactoContent = contacto.getText().toString();

                // Se pasara a la siguiente pantalla de registro
                Intent postIntent = new Intent(SignUpActivity3.this, SignUpActivity4.class);

                user.setSobreMi(sobreMiContent);
                user.setFormaContacto(contactoContent);

                postIntent.putExtra(USUARIO_REGISTRADO3, user);

                // Comenzamos siguiente parte del registro
                startActivity(postIntent);
            }
        });
    }
}