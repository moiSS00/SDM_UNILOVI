package com.example.unilovi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserSettingsActivity extends AppCompatActivity {

    private Spinner spinnerSettingsFacultades;
    private Spinner spinnerSettingsCarreras;
    private Spinner spinnerSettingsCiudades;
    private Button btnGuardarAjustes;
    private Button btnCancelarAjustes;
    private RadioGroup radioGroupSettings;
    private RadioButton radioButtonMasc;
    private RadioButton radioButtonFem;
    private RadioButton radioButtonNoBinario;
    private Switch switchTema;
    private SharedPreferences sharedPreferences;

    // Atributos auxiliares
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        //Referencias a componentes Settings
        spinnerSettingsFacultades = (Spinner) findViewById(R.id.spinnerSettingsFacultades);
        spinnerSettingsCarreras = (Spinner) findViewById(R.id.spinnerSettingsCarreras);
        spinnerSettingsCiudades = (Spinner) findViewById(R.id.spinnerSettingsCiudades);
        switchTema = (Switch) findViewById(R.id.switchTema);

        //Rellenamos con valores de la database
        db.collection("ciudades").document("ciudadesFormularioUsuario")
            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> ciudades = new ArrayList<String>();
                    ciudades.add("Sin definir");
                    ciudades.addAll((Collection<? extends String>) documentSnapshot.getData().get("Ciudades")) ;
                    rellenarSpinner(spinnerSettingsCiudades, ciudades);
                }
            }
        });

        db.collection("facultades")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> facultades = new ArrayList<String>();
                            facultades.add("Sin definir");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                facultades.add(document.getId());
                            }
                            rellenarSpinner(spinnerSettingsFacultades, facultades);
                        }
                    }
                });

        // -- Para el spinner de facultades --
        spinnerSettingsFacultades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String facultad = spinnerSettingsFacultades.getItemAtPosition(i).toString();
                db.collection("facultades").document(facultad)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> carreras = new ArrayList<String>();
                        if (documentSnapshot.exists()) {
                            carreras.addAll((Collection<? extends String>) documentSnapshot.getData().get("carreras"));
                            rellenarSpinner(spinnerSettingsCarreras, carreras);
                            spinnerSettingsCarreras.setEnabled(true);
                        }
                        else {
                            List<String> defaultList = new ArrayList<String>();
                            defaultList.add("Sn definir");
                            rellenarSpinner(spinnerSettingsCarreras, defaultList);
                            spinnerSettingsCarreras.setEnabled(false);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        //Comprobamos si está puesto o no el tema oscuro para cambiar el switch
        int tema = sharedPreferences.getInt("tema", 1);
        if (tema == 0)
            switchTema.setChecked(true);
        else
            switchTema.setChecked(false);

    }

    /**
     * Rellena un spinner dado con una lista de strings dada
     * @param spinner Spinner concreto a rellenar
     * @param list Lista de strings
     */
    private void rellenarSpinner(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDayNight();
    }

    /*
        Método para cambiar el modo de modo claro a modo oscuro
    */
    public void updateDayNight() {
        int theme = sharedPreferences.getInt("tema", 1);
        if (theme == 0)
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
}