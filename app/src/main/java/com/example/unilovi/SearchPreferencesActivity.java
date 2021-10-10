package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchPreferencesActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private SeekBar seekBarMinima;
    private SeekBar seekBarMaxima;
    private TextView edadMinima;
    private TextView edadMaxima;
    private CheckBox checkHombre;
    private CheckBox checkMujer;
    private Button btnGuardarPreferencias;
    private Spinner spinnerFacultades;
    private Spinner spinnerCarreras;
    private Spinner spinnerCiudades;

    // Atributos auxiliares
    private List<String> listaFacultades;
    private HashMap<String, List<String>> tablaCarreras;
    private List<String> listaCiudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_preferences);

        // Inicializa el modelo de datos
        listaFacultades = new ArrayList<String>();
        listaFacultades.add("Sin definir");
        listaFacultades.add("Escuela de ingeniería informática");
        listaFacultades.add("Facultad de ciencias");
        listaFacultades.add("Facultad de derecho");

        tablaCarreras = new HashMap<>();
        tablaCarreras.put("Sin definir", new ArrayList<String>(Arrays.asList("Sin definir")));
        tablaCarreras.put("Escuela de ingeniería informática", new ArrayList<String>(Arrays.asList("Ingeniería Informática")));
        tablaCarreras.put("Facultad de ciencias", new ArrayList<String>(Arrays.asList("Sin definir", "Matemáticas", "Física", "...")));
        tablaCarreras.put("Facultad de derecho", new ArrayList<String>(Arrays.asList("Sin definir", "Derecho", "Derecho y Economía", "...")));

        listaCiudades = new ArrayList<String>();
        listaCiudades.add("Sin definir");
        listaCiudades.add("Gijón");
        listaCiudades.add("Oviedo");
        listaCiudades.add("Avilés");
        listaCiudades.add("...");


        // Obtenemos referencias a los componentes
        edadMaxima = (TextView) findViewById(R.id.edadMaxima);
        edadMinima = (TextView) findViewById(R.id.edadMinima);
        seekBarMaxima = (SeekBar) findViewById(R.id.seekBarEdadMaxima);
        seekBarMinima = (SeekBar) findViewById(R.id.seekBarEdadMinima);
        spinnerFacultades = (Spinner) findViewById(R.id.spinnerFacultades);
        spinnerCarreras = (Spinner) findViewById(R.id.spinnerCarreras);
        spinnerCiudades = (Spinner) findViewById(R.id.spinnerCiudades);
        checkHombre = (CheckBox) findViewById(R.id.checkHombre);
        checkMujer = (CheckBox) findViewById(R.id.checkMujer);
        btnGuardarPreferencias = (Button) findViewById(R.id.btnGuardarPreferencias);

        // Asignamos valores por defecto
        edadMinima.setText("17");
        edadMaxima.setText("50");
        rellenarSpinner(spinnerFacultades, listaFacultades);
        rellenarSpinner(spinnerCarreras, tablaCarreras.get("Sin definir"));
        rellenarSpinner(spinnerCiudades, listaCiudades);

        // Asignamos listeners

        // -- Para la barra de edad mínima --
        seekBarMinima.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int edad = i + 17;
                edadMinima.setText(edad + "");
                if (Integer.parseInt(edadMinima.getText().toString()) >= Integer.parseInt(edadMaxima.getText().toString())) {
                    int edad2 = Integer.parseInt(edadMinima.getText().toString()) + 1;
                    edadMaxima.setText(edad2 + "");
                    seekBarMaxima.setProgress(seekBarMinima.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        // -- Para la barra de edad máxima --
        seekBarMaxima.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int edad = i + 18;
                edadMaxima.setText(edad + "");
                if (Integer.parseInt(edadMaxima.getText().toString()) <= Integer.parseInt(edadMinima.getText().toString())) {
                    int edad2 = Integer.parseInt(edadMaxima.getText().toString()) - 1;
                    edadMinima.setText(edad2 + "");
                    seekBarMinima.setProgress(seekBarMaxima.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // -- Para el spinner de facultades --
        spinnerFacultades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rellenarSpinner(spinnerCarreras, tablaCarreras.get(spinnerFacultades.getItemAtPosition(i).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // -- Asignamos una acción al botón de guardar referencias --
        btnGuardarPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkHombre.isChecked() || checkMujer.isChecked()) {
                    Intent intentResultado = new Intent();
                    setResult(RESULT_OK, intentResultado);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.seekBarMinima), R.string.no_guardado,
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
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