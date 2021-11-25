package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.CallBack;

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
    private ProgressBar progressBar;

    // Atributos auxiliares
    private SharedPreferences sharedPreferences;
    private List<String> listaCiudades;
    private List<String> listaFacultades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        //Referencias a componentes Settings
        spinnerSettingsFacultades = (Spinner) findViewById(R.id.spinnerUserSettingsFacultades);
        spinnerSettingsCarreras = (Spinner) findViewById(R.id.spinnerUserSettingsCarreras);
        spinnerSettingsCiudades = (Spinner) findViewById(R.id.spinnerUserSettingsCiudades);
        btnCancelarAjustes = (Button) findViewById(R.id.btnCancelarUserSettings);
        btnGuardarAjustes = (Button) findViewById(R.id.btnGuardarUserSettings);
        radioGroupSettings = (RadioGroup) findViewById(R.id.radioGroupGeneroUserSettings);
        switchTema = (Switch) findViewById(R.id.switchTemaUserSettings);
        progressBar = (ProgressBar) findViewById(R.id.progressBarUserSettings);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        //Comprobamos si está puesto o no el tema oscuro para cambiar el switch
        int tema = sharedPreferences.getInt("tema", 1);
        if (tema == 0)
            switchTema.setChecked(true);
        else
            switchTema.setChecked(false);


        // Asignamos listeners

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
    }

    @Override
    public void onResume() {
        super.onResume();

        //Rellenamos con valores de la base de datos el spinner de ciudades
        Firebase.getCiudades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    listaCiudades = (List<String>) object;
                    // Rellenamos con valores de la base de datos el spinner de facultades
                    Firebase.getFacultades(new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {
                            if (object != null) {
                                listaFacultades = (List<String>) object;
                                Util.rellenarSpinner(getApplicationContext(),
                                        spinnerSettingsFacultades, (List<String>) object);
                                Util.rellenarSpinner(getApplicationContext(),
                                        spinnerSettingsCiudades, (List<String>) object);
                                updateDayNight();
                                enableView();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Pone la vista en modo normal
     */
    private void enableView() {
        // Desactivamos la barra de carga
        findViewById(R.id.progressBarUserSettings).setVisibility(View.INVISIBLE);

        // Activamos los demás componentes
        findViewById(R.id.txtGeneroUserSettings).setVisibility(View.VISIBLE);
        findViewById(R.id.txtFacultadCarreraUserSettings).setVisibility(View.VISIBLE);
        findViewById(R.id.spinnerUserSettingsFacultades).setVisibility(View.VISIBLE);
        findViewById(R.id.spinnerUserSettingsCarreras).setVisibility(View.VISIBLE);
        findViewById(R.id.txtCiudadUserSettings).setVisibility(View.VISIBLE);
        findViewById(R.id.spinnerUserSettingsFacultades).setVisibility(View.VISIBLE);
        findViewById(R.id.spinnerUserSettingsCiudades).setVisibility(View.VISIBLE);
        findViewById(R.id.txtGeneroUserSettings).setVisibility(View.VISIBLE);
        findViewById(R.id.radioGroupGeneroUserSettings).setVisibility(View.VISIBLE);
        findViewById(R.id.switchTemaUserSettings).setVisibility(View.VISIBLE);
        findViewById(R.id.btnCancelarUserSettings).setVisibility(View.VISIBLE);
        findViewById(R.id.btnGuardarUserSettings).setVisibility(View.VISIBLE);
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

}