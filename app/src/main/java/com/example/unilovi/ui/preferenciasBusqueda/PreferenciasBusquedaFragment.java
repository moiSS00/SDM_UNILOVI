package com.example.unilovi.ui.preferenciasBusqueda;

import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unilovi.R;
import com.example.unilovi.SignUpActivity4;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentPreferenciasBinding;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.CallBack;

import java.util.List;
import java.util.Map;


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
    private AutoCompleteTextView editTextFilledExposedDropdownFacultades;
    private AutoCompleteTextView editTextFilledExposedDropdownCarreras;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPreferenciasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtenemos referencias a los componentes
        edadMaxima = (TextView) root.findViewById(R.id.edadMaxima);
        edadMinima = (TextView) root.findViewById(R.id.edadMinima);
        seekBarMaxima = (SeekBar) root.findViewById(R.id.seekBarEdadMaxima);
        seekBarMinima = (SeekBar) root.findViewById(R.id.seekBarEdadMinima);
        editTextFilledExposedDropdownFacultades = (AutoCompleteTextView) root.findViewById(R.id.cbxFacultadPreferencias);
        editTextFilledExposedDropdownCarreras = (AutoCompleteTextView) root.findViewById(R.id.cbxCarreraPreferencias);
        checkHombre = (CheckBox) root.findViewById(R.id.checkHombre);
        checkMujer = (CheckBox) root.findViewById(R.id.checkMujer);
        checkNoBinario = (CheckBox) root.findViewById(R.id.checkNoBinario);

        // Asignamos valores por defecto
        edadMinima.setText("18");
        edadMaxima.setText("50");

        iniciarSpinners();

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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Sacamos las preferencias del usuario
        Firebase.getPreferencesByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    Map<String, Object> preferencias = (Map<String, Object>) object;
                    preferencias.get("carrera");
                    preferencias.get("facultad");
                    // Recuperamos las edades
                    String edadm = preferencias.get("edadMinima").toString();
                    String edadM = preferencias.get("edadMaxima").toString();
                    int progressm = Integer.parseInt(preferencias.get("edadMinima").toString()) - 18;
                    int progressM = Integer.parseInt(preferencias.get("edadMaxima").toString()) - 19;
                    seekBarMinima.setProgress(progressm);
                    seekBarMaxima.setProgress(progressM);

                    // Recuperamos los sexos de búsqueda
                    List<String> sexos = (List<String>) preferencias.get("sexoBusqueda");
                    for (String sexo : sexos){
                        if (sexo.equals("M"))
                            checkHombre.setChecked(true);
                        else if (sexo.equals("F"))
                            checkMujer.setChecked(true);
                        else
                            checkNoBinario.setChecked(true);
                    }

                } else {
                    Toast.makeText(getContext(), "No se pudieron recuperar las preferencias del usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void iniciarSpinners() {

        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                ArrayAdapter<String> adapterFacultades =
                        new ArrayAdapter<String>(
                                getContext(),
                                R.layout.dropdown_menu_popup_item,
                                R.id.prueba,
                                (List<String>) object);
                editTextFilledExposedDropdownFacultades.setAdapter(adapterFacultades);
            }
        });

        editTextFilledExposedDropdownFacultades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String facultad = editTextFilledExposedDropdownFacultades.getText().toString();
                Firebase.getCarrerasByFacultad(facultad, new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        ArrayAdapter<String> adapterCarreras =
                                new ArrayAdapter<String>(
                                        getContext(),
                                        R.layout.dropdown_menu_popup_item,
                                        R.id.prueba,
                                        (List<String>) object);
                        editTextFilledExposedDropdownCarreras.setAdapter(adapterCarreras);
                    }
                });
                editTextFilledExposedDropdownCarreras.setText("");
            }
        });
    }

}