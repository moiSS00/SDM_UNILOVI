package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.database.CallBack;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class SignUpActivity2 extends AppCompatActivity {

    //Componentes básicos de android studio
    private ImageView imagen;
    private Uri dataImagen;
    private Button btnSubirFoto;
    private Button btnSiguiente;

    //Layouts de errores
    private TextInputLayout name_error;
    private TextInputLayout surnanme_error;
    private TextInputLayout date_error;
    private TextInputLayout facultad_error;
    private TextInputLayout carrera_error;

    //Campos de texto
    private TextInputEditText textNombre;
    private TextInputEditText textApellidos;
    private TextInputEditText textFecha;

    DatePickerDialog dialogDatePicker;


    //HashMap de meses y días
    private HashMap<Integer,Integer> tablaDias = new HashMap<>();

    //Spinners
    private AutoCompleteTextView editTextFilledExposedDropdownFacultades;
    private AutoCompleteTextView editTextFilledExposedDropdownCarreras;

    // Atributos auxiliares
    private static final int GALLERY_INTENT = 1;
    public static final String USUARIO_REGISTRADO2 = "usuario_registrado2";
    public static final String URI_FOTO2 = "uri_foto2";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        // Recogemos componentes
        imagen = (ImageView) findViewById(R.id.su2ImagenUsuario);
        btnSubirFoto = (Button) findViewById(R.id.su2SubirFotoButton);
        textFecha = (TextInputEditText) findViewById(R.id.su2FechaNacimientoEdit);
        textNombre = (TextInputEditText) findViewById(R.id.su2NombreEdit);
        textApellidos = (TextInputEditText) findViewById(R.id.su2ApellidosEdit);

        editTextFilledExposedDropdownFacultades = (AutoCompleteTextView) findViewById(R.id.su2FacultadCbx);
        editTextFilledExposedDropdownCarreras = (AutoCompleteTextView) findViewById(R.id.su2CarreraCbx);

        btnSiguiente = (Button) findViewById(R.id.su2SiguienteButton);
        name_error = (TextInputLayout) findViewById(R.id.filledTextFieldNombre);
        surnanme_error = (TextInputLayout) findViewById(R.id.filledTextFieldApellidos);
        date_error = (TextInputLayout) findViewById(R.id.filledTextFieldFecha);
        facultad_error = (TextInputLayout) findViewById(R.id.filledTextFieldFacultad);
        carrera_error = (TextInputLayout) findViewById(R.id.filledTextFieldCarrera);

        // Obtenemos el usuario
        user = getIntent().getParcelableExtra(SignInActivity.USUARIO_REGISTRADO1);

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCargarFotoActivityForResult();
            }
        });

        iniciarSpinners();

        // Desactivamos el teclado para el text de la fecha
        textFecha.setInputType(InputType.TYPE_NULL);

        textFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Si el teclado está activado por haber entrado a otro text, lo desactivamos para no tener problemas
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                if (hasFocus) {
                    GregorianCalendar calendar = new GregorianCalendar();

                    calendar.add(Calendar.YEAR, -18);
                    Date fechaMaxima = calendar.getTime();

                    calendar.add(Calendar.YEAR, -32);
                    Date fechaMinima = calendar.getTime();

                    int day;
                    int month;
                    int year;

                    if (!textFecha.getText().toString().isEmpty()) {
                        String[] texto = textFecha.getText().toString().split("/");
                        year = Integer.parseInt(texto[0]);
                        month = Integer.parseInt(texto[1]);
                        day = Integer.parseInt(texto[2]);
                    } else {
                        //Volvemos a añadir 32 años para que el año que cojamos como inicial sea el más cercano posible para registrarse
                        calendar.add(Calendar.YEAR, 32);

                        day = calendar.get(Calendar.DAY_OF_MONTH);
                        month = calendar.get(Calendar.MONTH);
                        year = calendar.get(Calendar.YEAR);
                    }


                    dialogDatePicker = new DatePickerDialog(SignUpActivity2.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String fecha = year + "/" + (month + 1) + "/" + day;
                            textFecha.setText(fecha);
                        }
                    }, year, month, day);
                    dialogDatePicker.getDatePicker().setMinDate(fechaMinima.getTime());
                    dialogDatePicker.getDatePicker().setMaxDate(fechaMaxima.getTime());
                    dialogDatePicker.show();
                }
            }
        });


        //Cambiar la lógica de debajo para comprobar si el usuario puede seguir adelante o no
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firebase.getUsuarioActual().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        // Recogemos inputs
                        String nombre = textNombre.getText().toString();
                        String apellidos = textApellidos.getText().toString();
                        String fecha = textFecha.getText().toString();
                        String facultad = editTextFilledExposedDropdownFacultades
                                .getText().toString();
                        String carrera = editTextFilledExposedDropdownCarreras
                                .getText().toString();

                        boolean flag = true;

                        if (nombre.isEmpty()) {
                            name_error.setError("Debes introducir tu nombre");
                            flag = false;
                        }

                        if (apellidos.isEmpty()) {
                            surnanme_error.setError("Debes introducir tus apellidos");
                            flag = false;
                        }

                        if (dataImagen == null) {
                            Toast.makeText(getApplicationContext(), "Debes introducir una foto", Toast.LENGTH_SHORT).show();
                            flag = false;
                        }

                        if (fecha.isEmpty()) {
                            date_error.setError("Debes introducir tu fecha de nacimiento");
                            flag = false;
                        }

                        if (facultad.isEmpty()) {
                            facultad_error.setError("Debes introducir tu fecha facultad");
                            flag = false;
                        }

                        if (carrera.isEmpty()) {
                            carrera_error.setError("Debes introducir tu carrera");
                            flag = false;
                        }

                        if (flag) {

                            // Se pasara a la siguiente pantalla de registro
                            Intent postIntent = new Intent(SignUpActivity2.this, SignUpActivity3.class);

                            // Se le va asignando la información del registro
                            user.setNombre(nombre);
                            user.setApellidos(apellidos);
                            user.setFechaNacimiento(fecha);
                            user.setFacultad(facultad);
                            user.setCarrera(carrera);

                            postIntent.putExtra(USUARIO_REGISTRADO2, user);
                            postIntent.putExtra(URI_FOTO2, dataImagen.toString());

                            // Comenzamos siguiente parte del registro
                            startActivity(postIntent);
                        }
                    }
                });
            }
        });
    }

    private void iniciarSpinners() {

        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                ArrayAdapter<String> adapterFacultades =
                        new ArrayAdapter<String>(
                                SignUpActivity2.this,
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
                                        SignUpActivity2.this,
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

    public void openCargarFotoActivityForResult() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(1080, 1080)
                .setMaxCropResultSize(2080, 2080)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                dataImagen = result.getUri();
                Picasso.get().load(dataImagen).into(imagen);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}