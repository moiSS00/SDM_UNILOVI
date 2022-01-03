package com.example.unilovi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.R;
import com.example.unilovi.ShowUserActivity;
import com.example.unilovi.adapters.ListaMatchesAdapter;
import com.example.unilovi.adapters.ListaSolicitudesAdapter;
import com.example.unilovi.adapters.OnItemClickListener;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentSolicitudesBinding;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.CallBack;

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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Inicializa el modelo de datos y creamos los adapters
        Firebase.getMatches(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                listaMatches = (List<User>) object;
                ListaMatchesAdapter lmAdater = new ListaMatchesAdapter(listaMatches,
                        new OnItemClickListener() {
                            @Override
                            public void onItemClick(User usuario) {
                                clickonItem(usuario);
                            }
                        });
                // Asignamos los adapters
                listaMatchesView.setAdapter(lmAdater);
            }
        });

        Firebase.getSolicitudes(new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                listaSolicitudes = (List<User>) object;
                ListaSolicitudesAdapter lsAdater = new ListaSolicitudesAdapter(listaSolicitudes,
                        new OnItemClickListener() {
                            @Override
                            public void onItemClick(User usuario) {
                                clickonItem(usuario);
                            }
                        });
                // Asignamos los adapters
                listaSolicitudesView.setAdapter(lsAdater);
            }
        });

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