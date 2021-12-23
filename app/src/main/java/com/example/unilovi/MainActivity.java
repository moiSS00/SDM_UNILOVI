package com.example.unilovi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.CallBack;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unilovi.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private TextView email;
    private TextView nombre;
    private ImageView imagen;
    private SharedPreferences sharedPreferences;

    // Atributos auxiliares
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        sharedPreferences = getSharedPreferences("SP", MODE_PRIVATE);

        setTheme(R.style.Theme_Unilovi_NoActionBar);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);

        imagen = headerView.findViewById(R.id.imageViewMenu);
        nombre = headerView.findViewById(R.id.nombreMenuID);
        email = headerView.findViewById(R.id.emailText);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_solicitudes, R.id.nav_preferencias)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.btnCerrarSesion) {
            Firebase.cerrarSesion();
            Intent settingsIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(settingsIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        String uriFoto = getIntent().getStringExtra(SignUpActivity4.URI_FOTO4);

        Firebase.getNombreByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                nombre.setText("Bienvenido/a " + object.toString());
            }
        });
        email.setText(Firebase.getUsuarioActual().getEmail());
        if (uriFoto != null && !uriFoto.isEmpty()) {
            Picasso.get().load(uriFoto).into(imagen);
        } else if (Firebase.getUsuarioActual() != null) {
            Firebase.downloadImage(Firebase.getUsuarioActual().getEmail(), new CallBack() {
                @Override
                public void methodToCallBack(Object object) {
                    if (object != null) {
                        Picasso.get().load((String) object).into(imagen);

                    }
                }
            });
        }
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