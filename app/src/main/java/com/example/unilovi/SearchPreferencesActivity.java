package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchPreferencesActivity extends AppCompatActivity {

    private SeekBar seekBarMinima;
    private SeekBar seekBarMaxima;
    private TextView edadMinima;
    private TextView edadMaxima;
    private CheckBox checkHombre;
    private CheckBox checkMujer;
    private Button btnGuardarPreferencias;
    private List<String> listaFacultades;
    private Spinner spinnerFacultades;
    private List<String> listaCiudades;
    private Spinner spinnerCiudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_preferences);

        seekBarMinima = (SeekBar) findViewById(R.id.seekBarEdadMinima);
        edadMinima = findViewById(R.id.edadMinima);

        edadMinima.setText("17");

        seekBarMaxima = findViewById(R.id.seekBarEdadMaxima);
        edadMaxima = findViewById(R.id.edadMaxima);

        edadMaxima.setText("50");

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

        listaFacultades = new ArrayList<String>();
        listaFacultades.add("Sin definir");
        listaFacultades.add("Facultad de ciencias");
        listaFacultades.add("Facultad de medicina");
        listaFacultades.add("...");

        spinnerFacultades = findViewById(R.id.spinnerFacultades);

        rellenarSpinner(spinnerFacultades, listaFacultades);

        listaCiudades = new ArrayList<String>();
        listaCiudades.add("Sin definir");
        listaCiudades.add("Gijón");
        listaCiudades.add("Oviedo");
        listaCiudades.add("Avilés");
        listaCiudades.add("...");

        spinnerCiudades = findViewById(R.id.spinnerCiudades);

        rellenarSpinner(spinnerCiudades, listaCiudades);

        checkHombre = findViewById(R.id.checkHombre);
        checkMujer = findViewById(R.id.checkMujer);

        btnGuardarPreferencias = findViewById(R.id.btnGuardarPreferencias);

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

    public void rellenarSpinner(Spinner spinner, List<String> array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}