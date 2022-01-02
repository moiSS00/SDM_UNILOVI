package com.example.unilovi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.R;
import com.example.unilovi.ShowUserActivity;
import com.example.unilovi.UsersRecyclerActivity;
import com.example.unilovi.adapters.ListaUsuariosAdapter;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentSolicitudesBinding;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.CallBack;

import java.util.ArrayList;
import java.util.List;

public class SolicitudesMatchesFragment extends Fragment {

    private FragmentSolicitudesBinding binding;

    // Atribitos que contendrán una referencia a los componentes usados
    private RecyclerView listaMatchesView;
    private RecyclerView listaSolicitudesView;

    // Atributos auxiliares
    private List<User> listaMatches; // Usuarios con los que tenemos me gusta mutuo
    private List<User> listaSolicitudes; // Usuarios que nos dan me gusta

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSolicitudesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtenemos referencias a los componentes
        listaMatchesView = (RecyclerView) root.findViewById(R.id.matchesRecyclerView);
        listaSolicitudesView = (RecyclerView) root.findViewById(R.id.solicitudesRecyclerView);

        // Configuramos el RecyclerView con la lista de matches
        LinearLayoutManager layoutManagerMatches = new LinearLayoutManager(root.getContext());
        LinearLayoutManager layoutManagerSolicitudes = new LinearLayoutManager(root.getContext());
        listaMatchesView.setHasFixedSize(true);
        listaMatchesView.setLayoutManager(layoutManagerMatches);
        listaSolicitudesView.setHasFixedSize(true);
        listaSolicitudesView.setLayoutManager(layoutManagerSolicitudes);

        // Inicializa el modelo de datos y creamos los adapters
        Firebase.getMatches(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                listaMatches = (List<User>) object;
                ListaUsuariosAdapter lmAdater = new ListaUsuariosAdapter(listaMatches,
                        new ListaUsuariosAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(User usuario) {
                                clickonItem(usuario);
                            }
                        });
                // Asignamos los adapters
                listaMatchesView.setAdapter(lmAdater);
            }
        });
        listaSolicitudes = new ArrayList<User>();


        ListaUsuariosAdapter lsAdater = new ListaUsuariosAdapter(listaSolicitudes,
                new ListaUsuariosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(User usuario) {
                        clickonItem(usuario);
                    }
                });



        listaSolicitudesView.setAdapter(lsAdater);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Click del item del adapter
     * @param usuario Usuario al que se ha dado click
     */
    public void clickonItem(User usuario) {
        Log.i("Click adpater", "Item clicked " + usuario.getEmail());
        // Paso al modo de apertura
        Intent intent = new Intent(getContext(), ShowUserActivity.class);
        intent.putExtra(ShowUserActivity.USUARIO_EMAIL, usuario.getEmail());
        startActivity(intent);
    }
}