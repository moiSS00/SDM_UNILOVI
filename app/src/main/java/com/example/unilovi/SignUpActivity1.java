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

    private TextInputEditText correo;
    private TextInputEditText pass;
    private TextInputEditText repeatPass;

    private Button signUpButton;

    // Atriutos auxiliares
    public static final String USUARIO_REGISTRADO = "usuario_registrado";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //CAMPOS PARA MARCAR EL ERROR EN ROJO
        correo_error = (TextInputLayout) findViewById(R.id.filledTextFieldCorreo);
        pass_error = (TextInputLayout) findViewById(R.id.filledTextFieldPass);
        repeatPass_error = (TextInputLayout) findViewById(R.id.filledTextFieldRepeatPass);

        //INPUTS QUE INTRODUCE EL USUARIO
        correo = (TextInputEditText) findViewById(R.id.correo);
        pass = (TextInputEditText) findViewById(R.id.pass);
        repeatPass = (TextInputEditText) findViewById(R.id.repeatPass);

        signUpButton = (Button) findViewById(R.id.signUpButton);

        // Asignamos listeners

        // -- Botón para registrarse --
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Recogemos datos
                String email = correo.getText().toString().trim();
                String password = pass.getText().toString();
                String repeatPassword = repeatPass.getText().toString();

                //VALIDACIONES
                boolean flag=true;
                if(email.isEmpty()) {
                    correo_error.setError("Necesita introducirse el correo");
                    flag=false;
                }
                else {
                    correo_error.setErrorEnabled(false);
                }
                if(password.isEmpty()) {
                    pass_error.setError("Necesita introducirse la contraseña");
                    flag=false;
                }
                else
                    pass_error.setErrorEnabled(false);

                if(flag && !password.equals(repeatPassword)) {
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

                    String emailAux = email + "@uniovi.es";

                    // Registramos al usuario
                    Firebase.registrarUsuario(emailAux, password, new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {
                            if ((boolean) object) { // Si hubo éxito

                                //Mandamos email de verificación
                                Firebase.getUsuarioActual().sendEmailVerification();

                                // Se pasara a la siguiente pantalla de registro
                                Intent postIntent = new Intent(SignUpActivity1.this, SignUpActivity2.class);

                                // Se crea un usuario y se le va asignando la información del registro
                                User user = new User();
                                user.setEmail(emailAux);
                                postIntent.putExtra(USUARIO_REGISTRADO, user);

                                // Comenzamos siguiente parte del registro
                                startActivity(postIntent);
                                finish();

                            } else {
                                Util.showAlert(context, "Hubo un error al registrarse");
                            }
                        }
                    });
                }
            }
        });

    }

}