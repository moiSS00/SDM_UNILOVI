package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.CallBack;

public class SignInActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private Button signInButton;
    private Button goToSignUpButton;
    private EditText editEmail;
    private EditText editPassword;

    // Atributos auxiliares
    public static final String USUARIO_REGISTRADO1 = "usuario_registrado1";
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

                    Firebase.iniciarSesion(emailContent, passwordContent, new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {
                            if ((boolean) object && Firebase.getUsuarioActual().isEmailVerified()) {
                                // Comprobamos si es la primera vez
                                Firebase.existUser(new CallBack() {
                                    @Override
                                    public void methodToCallBack(Object object) {
                                        if ((Boolean) object) { // No es la primera vez
                                            Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();
                                        }
                                        else { // Es la primera vez

                                            // Se pasara a la siguiente pantalla de registro
                                            Intent postIntent = new Intent(SignInActivity.this, SignUpActivity2.class);

                                            User user = new User();
                                            user.setEmail(emailContent);
                                            user.setPassword(passwordContent);
                                            postIntent.putExtra(USUARIO_REGISTRADO1, user);

                                            // Comenzamos siguiente parte del registro
                                            startActivity(postIntent);
                                        }
                                    }
                                });
                            }
                            else {
                                Util.showAlert(context, "Hubo algún fallo al iniciar sesión. " +
                                        "Comprueba las credenciales introducidas");
                            }
                        }
                    });
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

    @Override
    protected void onResume() {
        super.onResume();
        if (Firebase.getUsuarioActual() != null) {
            Firebase.cerrarSesion();
        }
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
        Intent mainIntent = new Intent(SignInActivity.this, SignUpActivity1.class);
        startActivity(mainIntent);
    }
}