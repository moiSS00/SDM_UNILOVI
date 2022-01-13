package com.example.unilovi;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.Util;
import com.example.unilovi.database.CallBack;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private Button signInButton;
    private Button goToSignUpButton;
    private Button changePasswordButton;
    private EditText editEmail;
    private EditText editPassword;
    private TextInputLayout filledTextFieldCorreo;

    // Atributos auxiliares
    public static final String USUARIO_REGISTRADO1 = "usuario_registrado1";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTheme(R.style.Theme_Unilovi_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Obtenemos referencias a los componentes
        signInButton = (Button) findViewById(R.id.siSendEmailButton);
        goToSignUpButton = (Button) findViewById(R.id.siGoToSignUpButton);
        changePasswordButton = (Button) findViewById(R.id.siChangePasswordButton);
        editEmail = (EditText) findViewById(R.id.siEmailEdit);
        editPassword = (EditText) findViewById(R.id.siPasswordEdit);
        filledTextFieldCorreo = (TextInputLayout) findViewById(R.id.filledTextFieldCorreoChangePassword);

        // Asignamos listeners

        // -- Botón para iniciar sesión --
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validacionEntrada()) { // Si las entradas son validas
                    //Concatenamos el usuario con el sufijo "@uniovi.es"
                    String emailContent = editEmail.getText().toString().concat(filledTextFieldCorreo.getSuffixText().toString());
                    String passwordContent = editPassword.getText().toString();

                    Firebase.iniciarSesion(emailContent.toLowerCase(), passwordContent, new CallBack() {
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
                            else if (Firebase.getUsuarioActual() != null && !Firebase.getUsuarioActual().isEmailVerified()){
                                Util.showErrorDialog(context, "Debes verificar el correo que hemos mandado a tu cuenta: " + emailContent);
                            } else {
                                Util.showErrorDialog(context, "Hubo algún fallo al iniciar sesión. " +
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

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePasswordIntent = new Intent(SignInActivity.this, ChangePasswordActivity.class);
                changePasswordActivityResultLauncher.launch(changePasswordIntent);
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

    ActivityResultLauncher<Intent> iniciarRegistroActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            User user = intent.getParcelableExtra(USUARIO_REGISTRADO1);
                            editEmail.setText(user.getEmail());
                            editPassword.setText(user.getPassword());
                            Toast.makeText(context, "Te hemos mandado un correo de verificación a tu email, por favor verifica el email antes de continuar", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> changePasswordActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(context, "Te hemos mandado un correo para cambiar tu contraseña, por favor compruebe su correo", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    private boolean validacionEntrada() {
        String emailContent = editEmail.getText().toString();
        String passwordContent = editPassword.getText().toString();
        if (emailContent.isEmpty() || passwordContent.isEmpty()) {
            Util.showErrorDialog(context,"Debe rellenar todos los campos para iniciar sesión");
            return false;
        }
        return true;
    }

    private void showSignUp() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity1.class);
        iniciarRegistroActivityResultLauncher.launch(intent);
    }
}