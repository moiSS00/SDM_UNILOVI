package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

    //Inicializmos la bd
    private Database database=new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        database.init();

        //Referencias a componentes Settings
        spinnerSettingsFacultades = (Spinner) findViewById(R.id.spinnerSettingsFacultades);
        spinnerSettingsCarreras = (Spinner) findViewById(R.id.spinnerSettingsCarreras);
        spinnerSettingsCiudades = (Spinner) findViewById(R.id.spinnerSettingsCiudades);

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
}