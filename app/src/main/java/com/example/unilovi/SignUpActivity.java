package com.example.unilovi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.unilovi.firebase.DBUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private EditText editEmail;
    private EditText editPassword;
    private Button signUpButton;

    // Atributos auxiliares
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

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
                    registrarUsuario(emailContent, passwordContent);
                }
            }
        });

    }

    private boolean validacionEntrada() {
        String emailContent = editEmail.getText().toString();
        String passwordContent = editPassword.getText().toString();
        if (emailContent.isEmpty() || passwordContent.isEmpty()) {
            showAlert("Debe rellenar todos los campos para iniciar sesión");
            return false;
        }
        return true;
    }

    private void registrarUsuario(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // El registro fue un exito
                            showHome();
                        } else { // Hubo algun error
                            showAlert("Hubo un error al registrarse");
                        }
                    }
                });
        DBUtils.createUser(email);
    }

    private void showHome() {
        Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }
}