package com.example.unilovi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.database.CallBack;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
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
    private User user;
    private String userEmail;
    public static final String USUARIO_EMAIL = "usuario_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        // -- Código de configuración autogenerado por Android Studio al crear una ScrollActivity--
        binding = ActivityShowUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtenemos referencias a los componentes
        facultad = (TextView) findViewById(R.id.facultadUsuarioText);
        carrera = (TextView) findViewById(R.id.carreraUsuarioText);
        sobreMi = (TextView) findViewById(R.id.sobreMiUsuarioText);
        imagenPerfil = (ImageView) findViewById(R.id.suImagenUsuario);

        toolbar = binding.toolbar;
        toolBarLayout = binding.toolbarLayout;
        fab = binding.suMessageFab;

        // -- Código de configuración autogenerado por Android Studio al crear una ScrollActivity--
        setSupportActionBar(toolbar);

        // Recepción de datos
        Intent intentUser = getIntent();
        userEmail = intentUser.getStringExtra(USUARIO_EMAIL);
        Firebase.getUsuarioByEmail(userEmail, new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                user = (User) object;
                showUser();
            }
        });

        // Asignamos listeners

        // --- Acción para el botón fab ---
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Se crea la activity de email
                String[] to = {userEmail};
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, userEmail);

                // Se lanza la activity de email
                try {
                    startActivity(Intent.createChooser(emailIntent, "Elija el cliente para mandar su correo"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "No hay clientes de correo instalados",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Comprobamos si es macth
        fab.setVisibility(View.INVISIBLE);
        Firebase.getUsuarioByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    User usuarioActual = (User) object;
                    if (usuarioActual.getMatches().contains(userEmail)) {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void showUser() {
        // Cargamos imagen
        Firebase.downloadImage(userEmail, new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    Picasso.get().load((String) object).fit().into(imagenPerfil);
                }
                else {
                    Picasso.get().load(R.drawable.default_user_image).fit().into(imagenPerfil);
                }
            }
        });
        // Cargamos información
        toolBarLayout.setTitle(user.getNombre() + ", " + user.getEdad());
        facultad.setText(user.getFacultad());
        carrera.setText(user.getCarrera());
        sobreMi.setText(user.getSobreMi());
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