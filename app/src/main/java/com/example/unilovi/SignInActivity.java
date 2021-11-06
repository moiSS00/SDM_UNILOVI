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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private Button signInButton;
    private Button goToSignUpButton;
    private EditText editEmail;
    private EditText editPassword;

    // Atributos auxiliares
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    iniciarSesion(emailContent, passwordContent);
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
            showAlert("Debe rellenar todos los campos para iniciar sesión");
            return false;
        }
        return true;
    }

    private void iniciarSesion(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // Se inicia sesión correctamente
                            showHome();
                        } else { // Hubo algún fallo
                            showAlert("Hubo algún fallo al iniciar sesión. " +
                                    "Comprueba las credenciales introducidas");
                        }
                    }
                });
    }

    private void showHome() {
        Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void showSignUp() {
        Intent mainIntent = new Intent(SignInActivity.this, SignUpActivity.class);
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