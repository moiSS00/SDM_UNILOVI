package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.unilovi.model.User;

public class SignUpActivity3 extends AppCompatActivity {

    private EditText sobreMi;
    private Button btnSiguiente;
    private RadioGroup rdgSexo;

    // Atributos auxiliares
    public static final String USUARIO_REGISTRADO3 = "usuario_registrado3";
    public static final String URI_FOTO3 = "uri_foto3";
    private User user;
    private String uriFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        sobreMi = (EditText) findViewById(R.id.editSobreMiRegistro3);
        btnSiguiente = (Button) findViewById(R.id.btnSiguienteRegistro3);
        rdgSexo = (RadioGroup) findViewById(R.id.rdgSexoRegistro3);

        user = getIntent().getParcelableExtra(SignUpActivity2.USUARIO_REGISTRADO2);
        uriFoto = getIntent().getStringExtra(SignUpActivity2.URI_FOTO2);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sobreMiContent = sobreMi.getText().toString();

                // Para obtener el radio boton del radioGroup seleccionado
                int selectedId = rdgSexo.getCheckedRadioButtonId();
                RadioButton rdSeleccionado = (RadioButton) findViewById(selectedId);

                if (rdSeleccionado == null) {
                    Toast.makeText(getApplicationContext(), "Debes seleccionar un sexo", Toast.LENGTH_SHORT).show();
                } else {
                    // Se pasara a la siguiente pantalla de registro
                    Intent postIntent = new Intent(SignUpActivity3.this, SignUpActivity4.class);

                    user.setSobreMi(sobreMiContent);

                    String sexo = rdSeleccionado.getText().toString();
                    user.setSexo(sexo);

                    postIntent.putExtra(USUARIO_REGISTRADO3, user);
                    postIntent.putExtra(URI_FOTO3, uriFoto);

                    // Comenzamos siguiente parte del registro
                    startActivity(postIntent);
                }
            }
        });
    }
}