package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.unilovi.adapters.matches.ListaMatchesAdapter;
import com.example.unilovi.adapters.OnItemClickListener;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_recycler);

        // Inicializa el modelo de datos
        listaMatches = new ArrayList<User>();
        listaSolicitudes = new ArrayList<User>();

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
        ListaMatchesAdapter lmAdater = new ListaMatchesAdapter(listaMatches,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(User usuario) {
                        clickonItem(usuario);
                    }
                });

        ListaMatchesAdapter lsAdater = new ListaMatchesAdapter(listaSolicitudes,
                new OnItemClickListener() {
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
        Log.i("Click adpater", "Item clicked " + usuario.getNombre());
        // Paso al modo de apertura
        Intent intent = new Intent(UsersRecyclerActivity.this, ShowUserActivity.class);
        intent.putExtra(USUARIO_SELECCIONADO, usuario);
        startActivity(intent);
    }
}