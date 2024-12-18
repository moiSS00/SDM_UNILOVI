package com.example.unilovi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unilovi.R;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentPreferenciasBinding;
import com.example.unilovi.model.Preferences;
import com.example.unilovi.database.CallBack;

import java.util.ArrayList;
import java.util.List;


public class PreferenciasBusquedaFragment extends Fragment {

    private FragmentPreferenciasBinding binding;
    View root;

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
        root = binding.getRoot();

        // Obtenemos referencias a los componentes
        edadMaxima = (TextView) root.findViewById(R.id.edadMaxima);
        edadMinima = (TextView) root.findViewById(R.id.edadMinima);
        seekBarMaxima = (SeekBar) root.findViewById(R.id.psEdadMaximaSb);
        seekBarMinima = (SeekBar) root.findViewById(R.id.pEdadMinimaSb);
        editTextFilledExposedDropdownFacultades = (AutoCompleteTextView) root.findViewById(R.id.pFacultadPreferenciasCbx);
        editTextFilledExposedDropdownCarreras = (AutoCompleteTextView) root.findViewById(R.id.pCarreraPreferenciasCbx);
        checkHombre = (CheckBox) root.findViewById(R.id.pHombreCheck);
        checkMujer = (CheckBox) root.findViewById(R.id.pMujerCheck);
        checkNoBinario = (CheckBox) root.findViewById(R.id.pNoBinarioCheck);

        // Asignamos valores por defecto
        edadMinima.setText("18");
        edadMaxima.setText("50");

        // Asignamos listeners
        checkHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkHombre.isChecked() && !checkMujer.isChecked() && !checkNoBinario.isChecked()) {
                    checkHombre.setChecked(true);
                    Toast.makeText(getContext(), "Debes elegir un sexo como mínimo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkHombre.isChecked() && !checkMujer.isChecked() && !checkNoBinario.isChecked()) {
                    checkMujer.setChecked(true);
                    Toast.makeText(getContext(), "Debes elegir un sexo como mínimo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkNoBinario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkHombre.isChecked() && !checkMujer.isChecked() && !checkNoBinario.isChecked()) {
                    checkNoBinario.setChecked(true);
                    Toast.makeText(getContext(), "Debes elegir un sexo como mínimo", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                ((List<String>) object).add("Sin definir");
                ArrayAdapter<String> adapterFacultades =
                        new ArrayAdapter<String>(
                                getContext(),
                                R.layout.dropdown_menu_popup_item,
                                R.id.prueba,
                                (List<String>) object);
                editTextFilledExposedDropdownFacultades.setAdapter(adapterFacultades);

                // Sacamos las preferencias del usuario
                Firebase.getPreferencesByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        if (object != null) {

                            Preferences preferencias = (Preferences) object;

                            // Recuperamos el valor de la facultad de preferencia
                            String facultad = preferencias.getFacultad();

                            // Si hay una facultad en las preferencias
                            if (!facultad.isEmpty()) {
                                int numeroFacultad = -1;

                                // Buscamos en el spinner la que coincide, cogemos su indice y ponemos ese texto en el spinner
                                for (int i = 0; i < editTextFilledExposedDropdownFacultades.getAdapter().getCount(); i++) {
                                    if (editTextFilledExposedDropdownFacultades.getAdapter().getItem(i).toString().equals(facultad)) {
                                        numeroFacultad = i;
                                    }
                                }
                                editTextFilledExposedDropdownFacultades.setText(editTextFilledExposedDropdownFacultades.getAdapter().getItem(numeroFacultad).toString(), false);

                                String carrera = preferencias.getCarrera();

                                // Le ponemos adapter al spinner de carreras según la facultad que sacamos de las preferencias
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

                                        // Si hay una carrera en las preferencias
                                        if (!carrera.isEmpty()) {
                                            int numeroCarrera = -1;

                                            // Buscamos en el spinner la que coincide, cogemos su indice y ponemos ese texto en el spinner
                                            if (editTextFilledExposedDropdownCarreras.getAdapter() != null) {
                                                for (int i = 0; i < editTextFilledExposedDropdownCarreras.getAdapter().getCount(); i++) {
                                                    if (editTextFilledExposedDropdownCarreras.getAdapter().getItem(i).toString().equals(carrera)) {
                                                        numeroCarrera = i;
                                                    }
                                                }
                                                editTextFilledExposedDropdownCarreras.setText(editTextFilledExposedDropdownCarreras.getAdapter().getItem(numeroCarrera).toString(), false);
                                            }
                                        }
                                    }
                                });
                            }

                            // Recuperamos las edades
                            int edadm = preferencias.getEdadMinima();
                            int edadM = preferencias.getEdadMaxima();
                            int progressm = edadm - 18;
                            int progressM = edadM - 19;
                            seekBarMinima.setProgress(progressm);
                            seekBarMaxima.setProgress(progressM);

                            // Recuperamos los sexos de búsqueda
                            List<String> sexos = preferencias.getSexos();
                            for (String sexo : sexos){
                                if (sexo.equals("M"))
                                    checkHombre.setChecked(true);
                                else if (sexo.equals("F"))
                                    checkMujer.setChecked(true);
                                else
                                    checkNoBinario.setChecked(true);
                            }

                            root.findViewById(R.id.layoutLoadPreferencias).setVisibility(View.GONE);

                        } else {
                            Toast.makeText(getContext(), "No se pudieron recuperar las preferencias del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Añadimos el listener al spinner de facultades
        editTextFilledExposedDropdownFacultades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String facultad = editTextFilledExposedDropdownFacultades.getText().toString();
                if (facultad.equals("Sin definir")) {
                    ArrayAdapter<String> adapterCarreras =
                            new ArrayAdapter<String>(
                                    getContext(),
                                    R.layout.dropdown_menu_popup_item,
                                    R.id.prueba,
                                    new ArrayList<String>());
                    editTextFilledExposedDropdownCarreras.setAdapter(adapterCarreras);
                    editTextFilledExposedDropdownCarreras.setText("");
                } else {
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
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        // Se controla que el usuario aún siga en sesión
        if (Firebase.getUsuarioActual() != null) {
            // Inicializamos las nuevas preferencias
            Preferences preferences = new Preferences();

            preferences.setEdadMinima(Integer.parseInt(edadMinima.getText().toString()));
            preferences.setEdadMaxima(Integer.parseInt(edadMaxima.getText().toString()));

            // Opcionalidad de la facultad
            if (editTextFilledExposedDropdownFacultades.getText().toString().equals("Sin definir"))
                preferences.setFacultad("");
            else
                preferences.setFacultad(editTextFilledExposedDropdownFacultades.getText().toString());

            preferences.setCarrera(editTextFilledExposedDropdownCarreras.getText().toString());

            ArrayList<String> sexos = new ArrayList<>();
            if (checkHombre.isChecked())
                sexos.add("M");
            if (checkMujer.isChecked())
                sexos.add("F");
            if (checkNoBinario.isChecked())
                sexos.add("O");
            preferences.setSexos(sexos);

            Firebase.updatePreferences(Firebase.getUsuarioActual().getEmail(), preferences, new CallBack() {
                @Override
                public void methodToCallBack(Object object) {
                }
            });
        }

    }

}