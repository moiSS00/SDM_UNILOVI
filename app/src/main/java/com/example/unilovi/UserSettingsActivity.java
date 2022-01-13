package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.database.CallBack;

import java.util.List;

public class UserSettingsActivity extends AppCompatActivity {


    private AutoCompleteTextView editTextFilledExposedDropdownFacultades;
    private AutoCompleteTextView editTextFilledExposedDropdownCarreras;
    private RadioGroup radioGroupSettings;
    private RadioButton radioButtonMasc;
    private RadioButton radioButtonFem;
    private RadioButton radioButtonNoBinario;
    private Switch switchTema;
    private EditText sobreMi;

    // Atributos auxiliares
    private SharedPreferences sharedPreferences;
    private List<String> listaCiudades;
    private List<String> listaFacultades;
    private User usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        //Referencias a componentes Settings

        editTextFilledExposedDropdownFacultades = (AutoCompleteTextView) findViewById(R.id.cbxFacultadAjustes);
        editTextFilledExposedDropdownCarreras = (AutoCompleteTextView) findViewById(R.id.cbxCarreraAjustes);
        radioGroupSettings = (RadioGroup) findViewById(R.id.radioGroupGeneroUserSettings);
        switchTema = (Switch) findViewById(R.id.switchTemaUserSettings);
        radioButtonMasc = (RadioButton) findViewById(R.id.radioMasculinoUserSettings);
        radioButtonFem = (RadioButton) findViewById(R.id.radioFemeninoUserSettings);
        radioButtonNoBinario = (RadioButton) findViewById(R.id.radioOtroUserSettings);
        sobreMi = (EditText) findViewById(R.id.editarSobreMi);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        //Comprobamos si está puesto o no el tema oscuro para cambiar el switch
        int tema = sharedPreferences.getInt("tema", 1);
        if (tema == 0)
            switchTema.setChecked(true);
        else
            switchTema.setChecked(false);

        // Asignamos listener del tema
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

        // Cargamos las facultades en el spinner de facultades
        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                ArrayAdapter<String> adapterFacultades =
                        new ArrayAdapter<String>(
                                UserSettingsActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                R.id.prueba,
                                (List<String>) object);
                editTextFilledExposedDropdownFacultades.setAdapter(adapterFacultades);

                //Cogemos los datos del usuario actual
                Firebase.getUsuarioByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        usuarioActual = (User) object;

                        String sexo = usuarioActual.getSexo();

                        if (sexo.equals("M"))
                            radioButtonMasc.setChecked(true);
                        else if (sexo.equals("F"))
                            radioButtonFem.setChecked(true);
                        else
                            radioButtonNoBinario.setChecked(true);

                        // Cogemos la descripcion del usuario
                        sobreMi.setText(usuarioActual.getSobreMi());

                        // Cogemos la facultad del usuario
                        String facultad = usuarioActual.getFacultad();

                        if (!facultad.isEmpty()) {
                            int numeroFacultad = -1;

                            // Buscamos en el spinner la que coincide, cogemos su indice y ponemos ese texto en el spinner
                            for (int i = 0; i < editTextFilledExposedDropdownFacultades.getAdapter().getCount(); i++) {
                                if (editTextFilledExposedDropdownFacultades.getAdapter().getItem(i).toString().equals(facultad)) {
                                    numeroFacultad = i;
                                }
                            }
                            editTextFilledExposedDropdownFacultades.setText(editTextFilledExposedDropdownFacultades.getAdapter().getItem(numeroFacultad).toString(), false);

                            String carrera = usuarioActual.getCarrera();

                            // Le ponemos adapter al spinner de carreras según la facultad que sacamos de las preferencias
                            Firebase.getCarrerasByFacultad(facultad, new CallBack() {
                                @Override
                                public void methodToCallBack(Object object) {
                                    ArrayAdapter<String> adapterCarreras =
                                            new ArrayAdapter<String>(
                                                    UserSettingsActivity.this,
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
                                    updateDayNight();
                                    findViewById(R.id.layoutLoad).setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
            }
        });

        // Añadimos listener a spinner de facultades
        editTextFilledExposedDropdownFacultades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String facultad = editTextFilledExposedDropdownFacultades.getText().toString();
                Firebase.getCarrerasByFacultad(facultad, new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        ArrayAdapter<String> adapterCarreras =
                                new ArrayAdapter<String>(
                                        UserSettingsActivity.this,
                                        R.layout.dropdown_menu_popup_item,
                                        R.id.prueba,
                                        (List<String>) object);
                        editTextFilledExposedDropdownCarreras.setAdapter(adapterCarreras);
                        editTextFilledExposedDropdownCarreras.setText(editTextFilledExposedDropdownCarreras.getAdapter().getItem(0).toString(), false);
                    }
                });
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        actualizarUsuario();
        Firebase.updateUser(usuarioActual.getEmail(), usuarioActual, new CallBack() {
            @Override
            public void methodToCallBack(Object object) {

            }
        });
    }

    /*
       Método para cambiar el modo de modo claro a modo oscuro
    */
    public void updateDayNight() {
        actualizarUsuario();
        Firebase.updateUser(usuarioActual.getEmail(), usuarioActual, new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                int theme = sharedPreferences.getInt("tema", 1);
                if (theme == 0)
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    public void actualizarUsuario() {
        if (radioButtonFem.isChecked())
            usuarioActual.setSexo("F");
        else if (radioButtonMasc.isChecked())
            usuarioActual.setSexo("M");
        else {
            usuarioActual.setSexo("O");
        }

        usuarioActual.setFacultad(editTextFilledExposedDropdownFacultades.getText().toString());
        usuarioActual.setCarrera(editTextFilledExposedDropdownCarreras.getText().toString());
        usuarioActual.setSobreMi(sobreMi.getText().toString());
    }

}