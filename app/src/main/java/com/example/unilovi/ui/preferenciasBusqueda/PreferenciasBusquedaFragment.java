package com.example.unilovi.ui.preferenciasBusqueda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unilovi.R;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentPreferenciasBinding;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.CallBack;

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
    private Spinner spinnerPreferencesFacultades;
    private Spinner spinnerPreferencesCarreras;
    private Spinner spinnerPreferencesCiudades;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPreferenciasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtenemos referencias a los componentes
        edadMaxima = (TextView) root.findViewById(R.id.edadMaxima);
        edadMinima = (TextView) root.findViewById(R.id.edadMinima);
        seekBarMaxima = (SeekBar) root.findViewById(R.id.seekBarEdadMaxima);
        seekBarMinima = (SeekBar) root.findViewById(R.id.seekBarEdadMinima);
        spinnerPreferencesFacultades = (Spinner) root.findViewById(R.id.spinnerFacultades);
        spinnerPreferencesCarreras = (Spinner) root.findViewById(R.id.spinnerCarreras);
        spinnerPreferencesCiudades = (Spinner) root.findViewById(R.id.spinnerCiudades);
        checkHombre = (CheckBox) root.findViewById(R.id.checkHombre);
        checkMujer = (CheckBox) root.findViewById(R.id.checkMujer);
        checkNoBinario = (CheckBox) root.findViewById(R.id.checkNoBinario);

        // Asignamos valores por defecto
        edadMinima.setText("18");
        edadMaxima.setText("51");

        //Rellenamos con valores de la base de datos el spinner de ciudades
        Firebase.getCiudades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                Util.rellenarSpinner(getContext(),
                        spinnerPreferencesCiudades, (List<String>) object);
            }
        });

        // Rellenamos con valores de la base de datos el spinner de facultades
        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                Util.rellenarSpinner(getContext(), spinnerPreferencesFacultades,
                        (List<String>) object);
            }
        });

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

        spinnerPreferencesFacultades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Obtenemos la facultad seleccionada
                String facultad = spinnerPreferencesFacultades.getItemAtPosition(i).toString();
                Firebase.getCarrerasByFacultad(facultad, new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        Util.rellenarSpinner(getContext(),
                                spinnerPreferencesCarreras, (List<String>) object);
                    }
                });
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

}