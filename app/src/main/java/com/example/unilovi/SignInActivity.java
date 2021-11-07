package com.example.unilovi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.callbacks.CallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import okhttp3.Call;

public class SignInActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private Button signInButton;
    private Button goToSignUpButton;
    private EditText editEmail;
    private EditText editPassword;

    // Atributos auxiliares
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_Unilovi_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Obtenemos referencias a los componentes
        signInButton = (Button) findViewById(R.id.signInButton);
        goToSignUpButton = (Button) findViewById(R.id.goToSignUpButton);
        editEmail = (EditText) findViewById(R.id.emailSignInEdit);
        editPassword = (EditText) findViewById(R.id.passwordSignInEdit);

        // Asignamos listeners

        // -- Botón para iniciar sesión --
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validacionEntrada()) { // Si las entradas son validas
                    String emailContent = editEmail.getText().toString();
                    String passwordContent = editPassword.getText().toString();
                    Firebase.iniciarSesion(emailContent, passwordContent, new CallBackSignIn());
                }
            }
        });

        // -- Botón para ir a la pantalla de registro  --
        goToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showSignUp();
            }
        });
    }

    private boolean validacionEntrada() {
        String emailContent = editEmail.getText().toString();
        String passwordContent = editPassword.getText().toString();
        if (emailContent.isEmpty() || passwordContent.isEmpty()) {
            Util.showAlert(context,"Debe rellenar todos los campos para iniciar sesión");
            return false;
        }
        return true;
    }

    private void showSignUp() {
        Intent mainIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(mainIntent);
    }


    private class CallBackSignIn implements CallBack {

        @Override
        public void methodToCallBack(Object object) {
            if ((boolean) object) {
                Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            else {
                Util.showAlert(context, "Hubo algún fallo al iniciar sesión. " +
                        "Comprueba las credenciales introducidas");
            }
        }
    }
}