package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.unilovi.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersRecyclerActivity extends AppCompatActivity {

    // Constantes para la navegación
    public static final String USUARIO_SELECCIONADO = "usuario_seleccionado";

    // Atribitos que contendrán una referencia a los componentes usados
    private RecyclerView listaMatchesView;

    // Atributos auxiliares
    private List<User> listaMatches; // Usuarios con los que tenemos me gusta mutuo
    private List<User> listaSolicitudes; // Usuarios que nos dan me gusta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_recycler);

        // Inicializa el modelo de datos
        listaMatches = new ArrayList<User>();
        listaSolicitudes = new ArrayList<User>();
        listaMatches.add(new User("Nombre1", "email1@email.com",20,
                "Facultad1", "Carrera1", "foto1"));
        listaMatches.add(new User("Nombre2", "email2@email.com",21,
                "Facultad2", "Carrera2", "foto2"));
        listaSolicitudes.add(new User("Nombre3", "email3@email.com",23,
                "Facultad3", "Carrera3", "foto3"));
        listaSolicitudes.add(new User("Nombre4", "email4@email.com",22,
                "Facultad4", "Carrera4", "foto4"));

        // Obtenemos referencias a los componentes
        listaMatchesView = (RecyclerView) findViewById(R.id.matchesRecyclerView);

        // Configuramos el RecyclerView con la lista de matches
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaMatchesView.setHasFixedSize(true);
        listaMatchesView.setLayoutManager(layoutManager);

        // Creamos el adapter
        ListaUsuariosAdapter luAdater = new ListaUsuariosAdapter(listaMatches,
                new ListaUsuariosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(User usuario) {
                        clickonItem(usuario);
                    }
                });


        // Asignamos el adapter creado
        listaMatchesView.setAdapter(luAdater);
    }

    /**
     * Click del item del adapter
     * @param usuario Usuario al que se ha dado click
     */
    public void clickonItem(User usuario) {
        Log.i("Click adpater", "Item clicked " + usuario.getName());
        // Paso al modo de apertura
        Intent intent = new Intent(UsersRecyclerActivity.this, UserDetailsActivity.class);
        intent.putExtra(USUARIO_SELECCIONADO, usuario);
        startActivity(intent);
    }
}