package com.example.unilovi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    // Atributos auxiliares
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        String email = sharedPreferences.getString("email", "nada");
        String password = sharedPreferences.getString("password", "nada");
        if (!email.equals("nada"))
            iniciarSesion(email, password);
        else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showLogin();
        }
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

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showHome() {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void showLogin() {
        Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(mainIntent);
        finish();
    }
}