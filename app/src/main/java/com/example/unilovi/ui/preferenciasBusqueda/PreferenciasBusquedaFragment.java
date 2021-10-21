package com.example.unilovi.ui.preferenciasBusqueda;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unilovi.R;
import com.example.unilovi.database.Database;
import com.example.unilovi.databinding.FragmentPreferenciasBinding;

import java.util.List;

public class PreferenciasBusquedaFragment extends Fragment {

    private FragmentPreferenciasBinding binding;

    // Atribitos que contendrán una referencia a los componentes usados
    private SeekBar seekBarMinima;
    private SeekBar seekBarMaxima;
    private TextView edadMinima;
    private TextView edadMaxima;
    private CheckBox checkHombre;
    private CheckBox checkMujer;
    private CheckBox checkNoBinario;
    private Button btnGuardarPreferencias;
    private Spinner spinnerFacultades;
    private Spinner spinnerCarreras;
    private Spinner spinnerCiudades;

    // Atributos auxiliares
    private Database database = new Database();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPreferenciasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa el modelo de datos
        database.init();


        // Obtenemos referencias a los componentes
        edadMaxima = (TextView) root.findViewById(R.id.edadMaxima);
        edadMinima = (TextView) root.findViewById(R.id.edadMinima);
        seekBarMaxima = (SeekBar) root.findViewById(R.id.seekBarEdadMaxima);
        seekBarMinima = (SeekBar) root.findViewById(R.id.seekBarEdadMinima);
        spinnerFacultades = (Spinner) root.findViewById(R.id.spinnerFacultades);
        spinnerCarreras = (Spinner) root.findViewById(R.id.spinnerCarreras);
        spinnerCiudades = (Spinner) root.findViewById(R.id.spinnerCiudades);
        checkHombre = (CheckBox) root.findViewById(R.id.checkHombre);
        checkMujer = (CheckBox) root.findViewById(R.id.checkMujer);
        checkNoBinario = (CheckBox) root.findViewById(R.id.checkNoBinario);
        btnGuardarPreferencias = (Button) root.findViewById(R.id.btnGuardarPreferencias);

        // Asignamos valores por defecto
        edadMinima.setText("18");
        edadMaxima.setText("51");
        rellenarSpinner(spinnerFacultades, database.getListaFacultades());
        rellenarSpinner(spinnerCarreras, database.getTablaCarreras().get("Sin definir"));
        rellenarSpinner(spinnerCiudades, database.getListaCiudades());

        // Asignamos listeners

        // -- Para la barra de edad mínima --
        seekBarMinima.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int edad = i + 18;
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
                int edad = i + 19;
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
                rellenarSpinner(spinnerCarreras, database.getTablaCarreras().get(spinnerFacultades.getItemAtPosition(i).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Rellena un spinner dado con una lista de strings dada
     * @param spinner Spinner concreto a rellenar
     * @param list Lista de strings
     */
    private void rellenarSpinner(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}