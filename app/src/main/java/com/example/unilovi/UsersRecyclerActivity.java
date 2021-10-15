package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.unilovi.adapters.ListaUsuariosAdapter;
import com.example.unilovi.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersRecyclerActivity extends AppCompatActivity {

    // Constantes para la navegación
    public static final String USUARIO_SELECCIONADO = "usuario_seleccionado";

    // Atribitos que contendrán una referencia a los componentes usados
    private RecyclerView listaMatchesView;
    private RecyclerView listaSolicitudesView;

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
        listaMatches.add(new User("Moises00", "moi@email.com",20,
                "Escuela de ingeniería informática", "Ingeniería informática",
                "https://ibb.co/NLZcc1h", null, User.OTRO, "Piedras Blancas", "Descripción de prueba"));
        listaMatches.add(new User("Dani32", "dan@email.com",21,
                "Facultad 2", "Física",
                "https://ibb.co/R0dY94V", null, User.MASCULINO, "Piedras Negras", "Descripción de prueba"));
        listaSolicitudes.add(new User("Ruben_xx", "r0en@email.com",23,
                "Facultad 3", "Matemáticas",
                "https://ibb.co/NxQXygY", null, User.MASCULINO, "Piedras Azules", "Descripción de prueba"));
        listaSolicitudes.add(new User("PradLove", "lovep@email.com",22,
                "Facultad 4", "Biología",
                "enlaceErroneo", null, User.FEMENINO, "Piedras Rojas", "Descripción de prueba con varias " +
                "lineas para probar si hay scrol prueba de scroll"));

        // Obtenemos referencias a los componentes
        listaMatchesView = (RecyclerView) findViewById(R.id.matchesRecyclerView);
        listaSolicitudesView = (RecyclerView) findViewById(R.id.solicitudesRecyclerView);

        // Configuramos el RecyclerView con la lista de matches
        LinearLayoutManager layoutManagerMatches = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager layoutManagerSolicitudes = new LinearLayoutManager(getApplicationContext());
        listaMatchesView.setHasFixedSize(true);
        listaMatchesView.setLayoutManager(layoutManagerMatches);
        listaSolicitudesView.setHasFixedSize(true);
        listaSolicitudesView.setLayoutManager(layoutManagerSolicitudes);

        // Creamos los adapters
        ListaUsuariosAdapter lmAdater = new ListaUsuariosAdapter(listaMatches,
                new ListaUsuariosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(User usuario) {
                        clickonItem(usuario);
                    }
                });

        ListaUsuariosAdapter lsAdater = new ListaUsuariosAdapter(listaSolicitudes,
                new ListaUsuariosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(User usuario) {
                        clickonItem(usuario);
                    }
                });


        // Asignamos los adapters
        listaMatchesView.setAdapter(lmAdater);
        listaSolicitudesView.setAdapter(lsAdater);
    }

    /**
     * Click del item del adapter
     * @param usuario Usuario al que se ha dado click
     */
    public void clickonItem(User usuario) {
        Log.i("Click adpater", "Item clicked " + usuario.getName());
        // Paso al modo de apertura
        Intent intent = new Intent(UsersRecyclerActivity.this, ShowUserActivity.class);
        intent.putExtra(USUARIO_SELECCIONADO, usuario);
        startActivity(intent);
    }
}