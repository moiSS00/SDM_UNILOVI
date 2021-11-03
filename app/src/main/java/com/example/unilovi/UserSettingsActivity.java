package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.unilovi.database.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        // Inicializar la base de datos
        database.init();

        //Referencias a componentes Settings
        spinnerSettingsFacultades = (Spinner) findViewById(R.id.spinnerSettingsFacultades);
        spinnerSettingsCarreras = (Spinner) findViewById(R.id.spinnerSettingsCarreras);
        spinnerSettingsCiudades = (Spinner) findViewById(R.id.spinnerSettingsCiudades);
        switchTema = (Switch) findViewById(R.id.switchTema);

        //Rellenamos con valores de la database
        rellenarSpinner(spinnerSettingsFacultades, database.getListaFacultades());
        rellenarSpinner(spinnerSettingsCarreras, database.getTablaCarreras().get("Sin definir"));
        rellenarSpinner(spinnerSettingsCiudades, database.getListaCiudades());

        // -- Para el spinner de facultades --
        spinnerSettingsFacultades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rellenarSpinner(spinnerSettingsCarreras, database.getTablaCarreras().get(spinnerSettingsFacultades.getItemAtPosition(i).toString()));
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

    /**
     * Rellena un spinner dado con una lista de strings dada
     * @param spinner Spinner concreto a rellenar
     * @param list Lista de strings
     */
    private void rellenarSpinner(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
}