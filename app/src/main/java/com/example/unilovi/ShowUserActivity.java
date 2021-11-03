package com.example.unilovi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.unilovi.model.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unilovi.databinding.ActivityShowUserBinding;
import com.squareup.picasso.Picasso;

public class ShowUserActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout toolBarLayout;

    private TextView facultad;
    private TextView carrera;
    private TextView sobreMi;
    private ImageView imagenPerfil;
    private SharedPreferences sharedPreferences;

    // Atributos auxiliares
    private ActivityShowUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        // -- Código de configuración autogenerado por Android Studio al crear una ScrollActivity--
        binding = ActivityShowUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtenemos referencias a los componentes
        facultad = (TextView) findViewById(R.id.facultadUsuarioText);
        carrera = (TextView) findViewById(R.id.carreraUsuarioText);
        sobreMi = (TextView) findViewById(R.id.sobreMiUsuarioText);
        imagenPerfil = (ImageView) findViewById(R.id.imagenPerfilUsuario);

        toolbar = binding.toolbar;
        toolBarLayout = binding.toolbarLayout;
        fab = binding.fab;

        // -- Código de configuración autogenerado por Android Studio al crear una ScrollActivity--
        setSupportActionBar(toolbar);

        // Recepción de datos
        Intent intentUser = getIntent();
        User user = intentUser.getParcelableExtra(UsersRecyclerActivity.USUARIO_SELECCIONADO);
        if (user != null)
            abrirModoConsulta(user);


        // Asignamos listeners

        // --- Acción para el botón fab ---
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    /**
     * Muestra la información de un usuario en concreto
     * @param user Usuario concretc del que queremos saber
     */
    private void abrirModoConsulta(User user) {

        // Cargamos la imagen de perfil del usuario usando la librería Picasso
        // (Pendiente revisar dimensiones para que se expanda por toda la superficie)
        // Picasso.get().load("https://i.postimg.cc/vBx065cX/default-user-image.png").into(imagenPerfil);

        // Actualizar componentes con valores de la película específica
        toolBarLayout.setTitle(user.getName() + ", " + user.getAge());
        facultad.setText(user.getSchool());
        carrera.setText(user.getCareer());
        sobreMi.setText(user.getAboutMe());

    }

    private void showDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(ShowUserActivity.this);
        LayoutInflater inflater=getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog_redes_sociales,null);

        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button volver= view.findViewById(R.id.volverRRSS);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Volver", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
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