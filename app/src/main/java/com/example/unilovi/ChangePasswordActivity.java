package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import javax.security.auth.callback.Callback;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button sendEmailChangePAssword;
    private EditText emailChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sendEmailChangePAssword = (Button) findViewById(R.id.sendEmailChangePAssword);
        emailChangePassword = (EditText) findViewById(R.id.emailChangePassword);

        sendEmailChangePAssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailChangePassword.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Introduce un correo para continuar", Toast.LENGTH_LONG).show();
                } else {
                    String correo = emailChangePassword.getText().toString() + "@uniovi.es";
                    FirebaseAuth.getInstance().sendPasswordResetEmail(correo);

                    Intent intentIniciarSesion = new Intent(ChangePasswordActivity.this, SignInActivity.class);
                    startActivity(intentIniciarSesion);
                    finish();
                }
            }
        });

    }
}