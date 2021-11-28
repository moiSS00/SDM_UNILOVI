package com.example.unilovi;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.CallBack;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        //CAMPOS PARA MARCAR EL ERROR EN ROJO
        correo_error = (TextInputLayout) findViewById(R.id.filledTextFieldCorreo);
        pass_error = (TextInputLayout) findViewById(R.id.filledTextFieldPass);
        repeatPass_error = (TextInputLayout) findViewById(R.id.filledTextFieldRepeatPass);

        //INPUTS QUE INTRODUCE EL USUARIO
        email = (TextInputEditText) findViewById(R.id.editCorreoRegistro1);
        password = (TextInputEditText) findViewById(R.id.editPasswordRegistro1);
        repeatPassword = (TextInputEditText) findViewById(R.id.editRepeatPasswordRegistro1);


        signUpButton = (Button) findViewById(R.id.btnSiguienteRegistro1);

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
                }
                else {
                    correo_error.setErrorEnabled(false);
                }
                if(passwordContent.isEmpty()) {
                    pass_error.setError("Necesita introducirse la contraseña");
                    flag=false;
                }
                else
                    pass_error.setErrorEnabled(false);

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
                    Firebase.registrarUsuario(emailAux, passwordContent, new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {
                            if ((boolean) object) { // Si hubo éxito
                                //Mandamos email de verificación
                                Firebase.getUsuarioActual().sendEmailVerification();

                                // Se pasara a la siguiente pantalla de registro
                                Intent postIntent = new Intent(SignUpActivity1.this, SignInActivity.class);

                                // Pasamos el usuario
                                User user = new User();
                                user.setEmail(emailContent);
                                user.setPassword(passwordContent);

                                postIntent.putExtra(SignInActivity.USUARIO_REGISTRADO1, user);

                                // Comenzamos siguiente parte del registro
                                startActivity(postIntent);
                                finish();
                            } else {
                                // HAY QUE HACER CONTROL DE EXCEPCIONES
                                Util.showAlert(context, "Hubo un error al registrarse");
                            }
                        }
                    });
                }
            }
        });

    }

}