package com.example.unilovi;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

public class PostSignUpActivity extends AppCompatActivity {

    //Componentes básicos de android studio
    private ImageView imagen;
    private Uri dataImagen;
    private Button btnSubirFoto;
    private Button btnSiguiente;

    //Layouts de errores
    private TextInputLayout name_error;
    private TextInputLayout surnanme_error;

    //Campos de texto
    private TextInputEditText textNombre;
    private TextInputEditText textApellido;

    //HashMap de meses y días
    private HashMap<Integer,Integer> tablaDias = new HashMap<>();

    //Spinners de fechas
    AutoCompleteTextView editTextFilledExposedDropdownYears;
    AutoCompleteTextView editTextFilledExposedDropdownMonths;
    AutoCompleteTextView editTextFilledExposedDropdownDays;

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
        editTextFilledExposedDropdownYears = (AutoCompleteTextView) findViewById(R.id.outlined_exposed_dropdown_years);
        editTextFilledExposedDropdownMonths = (AutoCompleteTextView) findViewById(R.id.outlined_exposed_dropdown_months);
        editTextFilledExposedDropdownDays = (AutoCompleteTextView) findViewById(R.id.outlined_exposed_dropdown_days);

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCargarFotoActivityForResult();
            }
        });

        iniciarSpinners();

        //Cambiar la lógica de debajo
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

        //Rellenamos la tabla con los meses y los días de cada mes
        for (int i = 1; i < 13; i++) {
            if (i <= 7) {
                if (i == 2)
                    tablaDias.put(i, 28);
                else if (i % 2 == 0)
                    tablaDias.put(i, 30);
                else
                    tablaDias.put(i, 31);
            } else {
                if (i % 2 == 0)
                    tablaDias.put(i, 31);
                else
                    tablaDias.put(i, 30);
            }
        }

        //Cogemos el calendario y rellenamos el array de años según el año actual y el array de meses
        GregorianCalendar calendar = new GregorianCalendar();

        Integer[] years = new Integer[33];
        Integer[] months = new Integer[12];
        Integer[] days = new Integer[31];


        int j = 0;
        for (int i = calendar.get(Calendar.YEAR) - 18; i > calendar.get(Calendar.YEAR) - 51; i--) {
            //Aquí rellenamos el array de months aprovechando el bucle
            if (j < 12)
                months[j] = j+1;
            //Aquí rellenamos el array de days aprovechando el bucle (por defecto 31 días)
            if (j < 31)
                days[j] = j+1;

            years[j] = i;
            j++;
        }

        //Creamos los adapter de años, meses y días
        ArrayAdapter<Integer> adapterYears =
                new ArrayAdapter<Integer>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        R.id.prueba,
                        years);

        ArrayAdapter<Integer> adapterMonths =
                new ArrayAdapter<Integer>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        R.id.prueba,
                        months);
        ArrayAdapter<Integer> adapterDays =
                new ArrayAdapter<Integer>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        R.id.prueba,
                        days);

        //Añadimos los adapters
        editTextFilledExposedDropdownYears.setAdapter(adapterYears);
        editTextFilledExposedDropdownMonths.setAdapter(adapterMonths);
        editTextFilledExposedDropdownDays.setAdapter(adapterDays);

        //Añadimos OnItemClickListener para años bisiestos y cambio de número de días según el mes
        editTextFilledExposedDropdownYears.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Solo entra aquí si hay algún mes seleccionado, el mes es el 2 y el año seleccionado es bisiesto
                if (!editTextFilledExposedDropdownMonths.getText().toString().isEmpty() && Integer.parseInt(editTextFilledExposedDropdownMonths.getText().toString()) == 2 && calendar.isLeapYear(Integer.parseInt(editTextFilledExposedDropdownYears.getText().toString()))) {
                    Integer[] days = new Integer[29];

                    for (int j = 0; j < 29; j++) {
                        days[j] = j + 1;
                    }

                    ArrayAdapter<Integer> adapterDays =
                            new ArrayAdapter<Integer>(
                                    PostSignUpActivity.this,
                                    R.layout.dropdown_menu_popup_item,
                                    R.id.prueba,
                                    days);
                    editTextFilledExposedDropdownDays.setAdapter(adapterDays);
                    editTextFilledExposedDropdownDays.setText("");
                }

                //Entra aquí cuando tenemos el mes 2 seleccionado y cambiamos a un año no bisiesto
                if (!editTextFilledExposedDropdownMonths.getText().toString().isEmpty() && Integer.parseInt(editTextFilledExposedDropdownMonths.getText().toString()) == 2 && !calendar.isLeapYear(Integer.parseInt(editTextFilledExposedDropdownYears.getText().toString()))) {
                    Integer[] days = new Integer[28];

                    for (int j = 0; j < 28; j++) {
                        days[j] = j + 1;
                    }

                    ArrayAdapter<Integer> adapterDays =
                            new ArrayAdapter<Integer>(
                                    PostSignUpActivity.this,
                                    R.layout.dropdown_menu_popup_item,
                                    R.id.prueba,
                                    days);
                    editTextFilledExposedDropdownDays.setAdapter(adapterDays);
                    editTextFilledExposedDropdownDays.setText("");
                }
            }
        });

        editTextFilledExposedDropdownMonths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int dias = tablaDias.get(Integer.parseInt(editTextFilledExposedDropdownMonths.getText().toString()));
                if (dias == 28 && !editTextFilledExposedDropdownYears.getText().toString().isEmpty() && calendar.isLeapYear(Integer.parseInt(editTextFilledExposedDropdownYears.getText().toString())))
                    dias++;
                Integer[] days = new Integer[dias];

                for (int j = 0; j < dias; j++) {
                    days[j] = j + 1;
                }

                ArrayAdapter<Integer> adapterDays =
                        new ArrayAdapter<Integer>(
                                PostSignUpActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                R.id.prueba,
                                days);
                editTextFilledExposedDropdownDays.setAdapter(adapterDays);
                editTextFilledExposedDropdownDays.setText("");
            }
        });






        // FALTA INICIAR LOS SPINNERS DE FACULTADES Y CARRERAS, SE INICIA DESDE FIREBASE
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