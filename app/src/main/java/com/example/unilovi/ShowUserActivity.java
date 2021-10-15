package com.example.unilovi;

import android.content.Intent;
import android.os.Bundle;

import com.example.unilovi.model.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    // Atributos auxiliares
    private ActivityShowUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

}