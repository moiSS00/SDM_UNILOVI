package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.Preferences;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.CallBack;

import java.util.ArrayList;

public class SignUpActivity4 extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private SeekBar seekBarMinima;
    private SeekBar seekBarMaxima;
    private CheckBox checkHombre;
    private CheckBox checkMujer;
    private CheckBox checkOtro;
    private Spinner spinnerPreferencesFacultades;
    private Spinner spinnerPreferencesCarreras;
    private Button btnSiguiente;

    // Atributos auxiliares
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up4);

        // Obtenemos los componentes
        seekBarMinima = (SeekBar) findViewById(R.id.seekBarEdadMinimaRegistro4);
        seekBarMaxima = (SeekBar) findViewById(R.id.seekBarEdadMaximaRegistro4);
        checkHombre = (CheckBox) findViewById(R.id.checkHombreRegistro4);
        checkMujer = (CheckBox) findViewById(R.id.checkMujerRegistro4);
        checkOtro = (CheckBox) findViewById(R.id.checkOtroRegistro4);
        spinnerPreferencesFacultades = (Spinner) findViewById(R.id.spinnerFacultadesRegistro4);
        spinnerPreferencesCarreras = (Spinner) findViewById(R.id.spinnerCarrerasRegistro4);
        btnSiguiente = (Button) findViewById(R.id.btnSiguienteRegistro4);

        // Obtenemos al usuario que se esta registrando
        user = getIntent().getParcelableExtra(SignUpActivity1.USUARIO_REGISTRADO);

        // Asignamos listenes
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Recogemos los valores
                int edadMinima = seekBarMinima.getProgress();
                int edadMaxima = seekBarMaxima.getProgress();
                // String facultad = spinnerPreferencesFacultades.getSelectedItem().toString();
                // String carrera = spinnerPreferencesCarreras.getSelectedItem().toString();

                ArrayList<String> sexos = new ArrayList<String>();
                if (checkHombre.isChecked()) { sexos.add("Masculino");}
                if (checkMujer.isChecked()) { sexos.add("Femenino");}
                if (checkOtro.isChecked()) { sexos.add("Otro");}

                // Validamos
                // ...

                // Creamos las preferencias
                Preferences preferences = new Preferences();
                preferences.setEdadMinima(edadMinima);
                preferences.setEdadMaxima(edadMaxima);
                preferences.setSexos(sexos);
                // preferences.setFacultad(facultad);
                // preferences.setCarrera(carrera);

                user.setPreferences(preferences);

                // Guardamos al usuario
                Firebase.createUser(user, new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}