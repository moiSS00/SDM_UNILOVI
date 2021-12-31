package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.Preferences;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.CallBack;
import com.example.unilovi.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity4 extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private TextView edadMinima;
    private TextView edadMaxima;
    private SeekBar seekBarMinima;
    private SeekBar seekBarMaxima;
    private CheckBox checkHombre;
    private CheckBox checkMujer;
    private CheckBox checkOtro;
    private Button btnSiguiente;

    //Spinners
    private AutoCompleteTextView editTextFilledExposedDropdownFacultades;
    private AutoCompleteTextView editTextFilledExposedDropdownCarreras;

    // Atributos auxiliares
    private Context context = this;
    public static final String URI_FOTO4 = "URI_FOTO4";
    private User user;
    private String uriFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up4);

        // Obtenemos los componentes
        edadMaxima = (TextView) findViewById(R.id.edadMaximaRegistro4);
        edadMinima = (TextView) findViewById(R.id.edadMinimaRegistro4);
        seekBarMinima = (SeekBar) findViewById(R.id.seekBarEdadMinimaRegistro4);
        seekBarMaxima = (SeekBar) findViewById(R.id.seekBarEdadMaximaRegistro4);
        checkHombre = (CheckBox) findViewById(R.id.checkHombreRegistro4);
        checkMujer = (CheckBox) findViewById(R.id.checkMujerRegistro4);
        checkOtro = (CheckBox) findViewById(R.id.checkOtroRegistro4);
        editTextFilledExposedDropdownFacultades = (AutoCompleteTextView) findViewById(R.id.cbxFacultadRegistro4);
        editTextFilledExposedDropdownCarreras = (AutoCompleteTextView) findViewById(R.id.cbxCarreraRegistro4);
        btnSiguiente = (Button) findViewById(R.id.btnSiguienteRegistro4);

        //Iniciamos valores de spinners
        iniciarSpinners();

        // Asignamos valores por defecto a los seekBar
        edadMinima.setText("18");
        edadMaxima.setText("50");

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

        // Obtenemos al usuario que se esta registrando
        user = getIntent().getParcelableExtra(SignUpActivity3.USUARIO_REGISTRADO3);
        uriFoto = getIntent().getStringExtra(SignUpActivity3.URI_FOTO3);

        // Asignamos listenes
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recogemos los valores
                int edadMinima = seekBarMinima.getProgress() + 18;
                int edadMaxima = seekBarMaxima.getProgress() + 19;
                String facultad = editTextFilledExposedDropdownFacultades.getText().toString();
                String carrera = editTextFilledExposedDropdownCarreras.getText().toString();

                ArrayList<String> sexos = new ArrayList<String>();
                if (checkHombre.isChecked()) { sexos.add("M");}
                if (checkMujer.isChecked()) { sexos.add("F");}
                if (checkOtro.isChecked()) { sexos.add("O");}

                // Validamos
                if (sexos.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debes elegir uno o más sexos preferentes", Toast.LENGTH_SHORT).show();
                } else {

                    // Creamos las preferencias
                    Preferences preferences = new Preferences();
                    preferences.setEdadMinima(edadMinima);
                    preferences.setEdadMaxima(edadMaxima);
                    preferences.setSexos(sexos);
                    preferences.setFacultad(facultad);
                    preferences.setCarrera(carrera);

                    // Para obtener el radio boton del radioGroup seleccionado

                    // Guardamos al usuario
                    Firebase.createUser(user, preferences, uriFoto, new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {
                            if (object != null) {
                                Firebase.iniciarSesion(user.getEmail(), user.getPassword(), new CallBack() {
                                    @Override
                                    public void methodToCallBack(Object object) {
                                        if ((boolean) object) {
                                            Intent mainIntent = new Intent(SignUpActivity4.this, MainActivity.class);
                                            mainIntent.putExtra(URI_FOTO4, uriFoto);
                                            startActivity(mainIntent);
                                            finish();
                                        } else {
                                            Util.showAlert(context, "Hubo algún fallo al iniciar sesión. " +
                                                    "Comprueba las credenciales introducidas");
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void iniciarSpinners() {

        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                ArrayAdapter<String> adapterFacultades =
                        new ArrayAdapter<String>(
                                SignUpActivity4.this,
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
                                        SignUpActivity4.this,
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