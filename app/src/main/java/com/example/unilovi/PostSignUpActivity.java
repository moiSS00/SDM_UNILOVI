package com.example.unilovi;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.CallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class PostSignUpActivity extends AppCompatActivity {

    //Componentes básicos de android studio
    private ImageView imagen;
    private Uri dataImagen;
    private Button btnSubirFoto;
    private Button btnSiguiente;

    //Layouts de errores
    private TextInputLayout name_error;
    private TextInputLayout surnanme_error;
    private TextInputLayout date_error;

    //Campos de texto
    private TextInputEditText textNombre;
    private TextInputEditText textApellido;
    private TextInputEditText textFecha;

    DatePickerDialog dialogDatePicker;


    //HashMap de meses y días
    private HashMap<Integer,Integer> tablaDias = new HashMap<>();

    //Spinners
    AutoCompleteTextView editTextFilledExposedDropdownFacultades;
    AutoCompleteTextView editTextFilledExposedDropdownCarreras;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sign_up);

        imagen = (ImageView) findViewById(R.id.imagenPostSignUp);
        btnSubirFoto = (Button) findViewById(R.id.btnSubirFoto);
        btnSiguiente = (Button) findViewById(R.id.btnSeguirRegistro);
        textNombre = (TextInputEditText) findViewById(R.id.editTextTextPersonName);
        textApellido = (TextInputEditText) findViewById(R.id.editTextTextPersonSurname);
        textFecha = (TextInputEditText) findViewById(R.id.editTextTextPersonFecha);
        date_error = (TextInputLayout) findViewById(R.id.filledTextFieldFecha);
        editTextFilledExposedDropdownFacultades = (AutoCompleteTextView) findViewById(R.id.outlined_exposed_dropdown_facultades);
        editTextFilledExposedDropdownCarreras = (AutoCompleteTextView) findViewById(R.id.outlined_exposed_dropdown_carreras);

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCargarFotoActivityForResult();
            }
        });

        iniciarSpinners();

        textFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    GregorianCalendar calendar = new GregorianCalendar();

                    calendar.add(Calendar.YEAR, -18);
                    Date fechaMaxima = calendar.getTime();

                    calendar.add(Calendar.YEAR, -32);
                    Date fechaMinima = calendar.getTime();

                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);


                    dialogDatePicker = new DatePickerDialog(PostSignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String fecha = year + "/" + month + "/" + day;
                            textFecha.setText(fecha);
                        }
                    }, day, month, year);
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
                        if (dataImagen != null && !textNombre.getText().toString().isEmpty() && !textApellido.getText().toString().isEmpty() && Firebase.getUsuarioActual().isEmailVerified()) {
                            Firebase.createUser(Firebase.getUsuarioActual().getEmail(), textNombre.getText().toString(), textApellido.getText().toString(), dataImagen, new CallBack() {
                                @Override
                                public void methodToCallBack(Object object) {
                                    Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "No se ha podido registrar al usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void iniciarSpinners() {
        // FALTA INICIAR LOS SPINNERS DE FACULTADES Y CARRERAS, SE INICIA DESDE FIREBASE
        Firebase.getFacultades(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                ArrayAdapter<String> adapterFacultades =
                        new ArrayAdapter<String>(
                                PostSignUpActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                R.id.prueba,
                                (List<String>) object);
                editTextFilledExposedDropdownFacultades.setAdapter(adapterFacultades);
            }
        });
    }

    public void openCargarFotoActivityForResult() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        cargarFotoActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> cargarFotoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            dataImagen = intent.getData();
                            Picasso.get().load(intent.getData()).into(imagen);
                        }
                    }
                }
            }
    );

}