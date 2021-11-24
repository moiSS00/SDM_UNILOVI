package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.CallBack;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserSettingsActivity extends AppCompatActivity {

    private Spinner spinnerSettingsFacultades;
    private Spinner spinnerSettingsCarreras;
    private Spinner spinnerSettingsCiudades;
    private Button btnGuardarAjustes;
    private Button btnCancelarAjustes;
    private RadioGroup radioGroupSettings;
    private RadioButton radioButtonMasc;
    private RadioButton radioButtonFem;
    private RadioButton radioButtonNoBinario;
    private Switch switchTema;
    private SharedPreferences sharedPreferences;

    // Atributos auxiliares
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Base de datos
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        //Referencias a componentes Settings
        spinnerSettingsFacultades = (Spinner) findViewById(R.id.spinnerSettingsFacultades);
        spinnerSettingsCarreras = (Spinner) findViewById(R.id.spinnerSettingsCarreras);
        spinnerSettingsCiudades = (Spinner) findViewById(R.id.spinnerSettingsCiudades);
        switchTema = (Switch) findViewById(R.id.switchTema);

        //Rellenamos con valores de la base de datos el spinner de ciudades
        Firebase.getCiudades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                Util.rellenarSpinner(getApplicationContext(),
                        spinnerSettingsCiudades, (List<String>) object);
            }
        });

        // Rellenamos con valores de la base de datos el spinner de facultades
        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                Util.rellenarSpinner(getApplicationContext(),
                        spinnerSettingsFacultades, (List<String>) object);
            }
        });

        //  Rellenamos con valores de la base de datos el spinner de carreras para una facultad
        spinnerSettingsFacultades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Obtenemos la facultad seleccionada
                String facultad = spinnerSettingsFacultades.getItemAtPosition(i).toString();
                Firebase.getCarrerasByFacultad(facultad, new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        Util.rellenarSpinner(getApplicationContext(),
                                spinnerSettingsCarreras, (List<String>) object);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        switchTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (switchTema.isChecked())
                    editor.putInt("tema", 0);
                else
                    editor.putInt("tema", 1);
                editor.apply();
                updateDayNight();

            }
        });

        //Comprobamos si está puesto o no el tema oscuro para cambiar el switch
        int tema = sharedPreferences.getInt("tema", 1);
        if (tema == 0)
            switchTema.setChecked(true);
        else
            switchTema.setChecked(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateDayNight();
    }

    /*
        Método para cambiar el modo de modo claro a modo oscuro
    */
    public void updateDayNight() {
        int theme = sharedPreferences.getInt("tema", 1);
        if (theme == 0)
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    // Tareas asíncronas

}