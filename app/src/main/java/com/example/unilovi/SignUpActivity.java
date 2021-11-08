package com.example.unilovi;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.callbacks.CallBack;

public class SignUpActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private EditText editEmail;
    private EditText editPassword;
    private Button signUpButton;

    // Atriutos auxiliares
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Obtenemos referencias a los componentes
        editEmail = (EditText) findViewById(R.id.emailSignUpEdit);
        editPassword = (EditText) findViewById(R.id.passwordSignUpEdit);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        // Asignamos listeners

        // -- Botón para registrarse --
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validacionEntrada()) {
                    String emailContent = editEmail.getText().toString();
                    String passwordContent = editPassword.getText().toString();
                    Firebase.registrarUsuario(emailContent, passwordContent, new CallackSignUp());
                }
            }
        });

    }

    private boolean validacionEntrada() {
        String emailContent = editEmail.getText().toString();
        String passwordContent = editPassword.getText().toString();
        if (emailContent.isEmpty() || passwordContent.isEmpty()) {
            Util.showAlert(context, "Debe rellenar todos los campos para iniciar sesión");
            return false;
        }
        return true;
    }

    private class CallackSignUp implements CallBack {

        @Override
        public void methodToCallBack(Object object) {
            if ((boolean) object) {
                Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(mainIntent);
            } else {
                Util.showAlert(context, "Hubo un error al registrarse");
            }
        }


    }



}