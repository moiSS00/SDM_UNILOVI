package com.example.unilovi;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.Util;
import com.example.unilovi.database.CallBack;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity1 extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private TextInputLayout correo_error;
    private TextInputLayout pass_error;
    private TextInputLayout repeatPass_error;

    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText repeatPassword;

    private Button signUpButton;

    // Atriutos auxiliares
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        //CAMPOS PARA MARCAR EL ERROR EN ROJO
        correo_error = (TextInputLayout) findViewById(R.id.filledTextFieldCorreoChangePassword);
        pass_error = (TextInputLayout) findViewById(R.id.filledTextFieldPass);
        repeatPass_error = (TextInputLayout) findViewById(R.id.filledTextFieldRepeatPass);

        //INPUTS QUE INTRODUCE EL USUARIO
        email = (TextInputEditText) findViewById(R.id.su1CorreoEdit);
        password = (TextInputEditText) findViewById(R.id.su1PasswordEdit);
        repeatPassword = (TextInputEditText) findViewById(R.id.su1RepeatPasswordEdit);


        signUpButton = (Button) findViewById(R.id.su1SiguienteButton);

        // Asignamos listeners

        // -- Botón para registrarse --
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Recogemos datos
                String emailContent = email.getText().toString().trim();
                String passwordContent = password.getText().toString();
                String repeatPasswordContent = repeatPassword.getText().toString();

                //VALIDACIONES
                boolean flag=true;
                if(emailContent.isEmpty()) {
                    correo_error.setError("Necesita introducirse el correo");
                    flag=false;
                } else {
                    correo_error.setErrorEnabled(false);
                }
                if(passwordContent.isEmpty()) {
                    pass_error.setError("Necesita introducirse la contraseña");
                    flag=false;
                } else if (passwordContent.length() < 6) {
                    pass_error.setError("La contraseña debe tener 6 caracteres o más");
                    flag=false;
                } else {
                    pass_error.setErrorEnabled(false);
                }

                if(flag && !passwordContent.equals(repeatPasswordContent)) {
                    repeatPass_error.setError("La contraseña no coincide");
                    flag=false;
                }
                else
                    repeatPass_error.setErrorEnabled(false);

                if(flag) {
                    /**
                     * 1- Comprobar a la hora de enviar el correo que no tiene espacios en blanco
                     * String correo=correo.getText().toString().trim();
                     * 2- Comprobar tambien que la cadena del correo no está formada por espacios en blanco
                     * if(correo.length()>0)
                     *    Entonces envia el correo
                     * 3- Añadir el sufijo de uniovi.es
                     */

                    String emailAux = emailContent + "@uniovi.es";

                    // Registramos al usuario
                    Firebase.registrarUsuario(emailAux.toLowerCase(), passwordContent, new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {

                            String code = (String) object;

                            switch (code) {
                                case "OK":
                                    //Mandamos email de verificación
                                    Firebase.getUsuarioActual().sendEmailVerification();

                                    // Se pasara a la siguiente pantalla de registro
                                    Intent postIntent = new Intent(SignUpActivity1.this, SignInActivity.class);

                                    // Pasamos el usuario
                                    User user = new User();
                                    user.setEmail(emailContent);
                                    user.setPassword(passwordContent);

                                    postIntent.putExtra(SignInActivity.USUARIO_REGISTRADO1, user);

                                    // Devolvemos los datos a la pantalla de inicio de sesión
                                    setResult(RESULT_OK, postIntent);
                                    finish();

                                case "ERROR_INVALID_EMAIL":
                                    Util.showErrorDialog(context, "El email tiene un formato incorrecto");
                                    correo_error.setError("Formato incorrecto");
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Util.showErrorDialog(context, "El email proporcionado ya esta en uso");
                                    correo_error.setError("Email ya en uso");
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Util.showErrorDialog(context, "La contraseña es demasiado débil");
                                    pass_error.setError("Contraseña demasiado débil");
                                    break;

                                default:
                                    Util.showErrorDialog(context, "Hubo un error inesperado");
                                    break;
                            }

                        }
                    });
                }
            }
        });

    }

}