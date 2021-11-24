package com.example.unilovi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unilovi.database.Firebase;
import com.example.unilovi.utils.Util;
import com.example.unilovi.utils.callbacks.CallBack;
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
    private ImageView imagenRandom;
    private SharedPreferences sharedPreferences;

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
        imagenRandom = findViewById(R.id.imagenPretendiente);
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
        nombre.setText("Bienvenido " + Firebase.getUsuarioActual().getEmail());
        email.setText(Firebase.getUsuarioActual().getEmail());
        Firebase.downloadImage(Firebase.getUsuarioActual().getEmail(), new CallbackMainFoto());
        Firebase.downloadImage("uo271397@uniovi.es", new CallbackRandomFoto());
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

    public void cargarFotoPrincipal(String string) {
        Picasso.get().load(string).into(imagen);
    }

    public void cargarFotoRandom(String string) {
        Picasso.get().load(string).into(imagenRandom);
    }

    private class CallbackMainFoto implements CallBack {

        @Override
        public void methodToCallBack(Object object) {
            if (object != null) {
                cargarFotoPrincipal((String) object);
            } else {
                Util.showAlert(getApplicationContext(), "Hubo un error al cargar las fotos");
            }
        }
    }

    private class CallbackRandomFoto implements CallBack {

        @Override
        public void methodToCallBack(Object object) {
            if (object != null) {
                cargarFotoRandom((String) object);
            } else {
                Util.showAlert(getApplicationContext(), "Hubo un error al cargar las fotos");
            }
        }
    }
}