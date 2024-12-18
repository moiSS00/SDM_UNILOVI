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
import com.example.unilovi.adapters.matches.ListaMatchesAdapter;
import com.example.unilovi.adapters.solicitudes.ListaSolicitudesAdapter;
import com.example.unilovi.adapters.OnItemClickListener;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentSolicitudesBinding;
import com.example.unilovi.model.User;

import java.util.ArrayList;

public class SolicitudesMatchesFragment extends Fragment {

    private FragmentSolicitudesBinding binding;

    // Atribitos que contendrán una referencia a los componentes usados
    private RecyclerView listaMatchesView;
    private RecyclerView listaSolicitudesView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSolicitudesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtenemos referencias a los componentes
        listaMatchesView = (RecyclerView) root.findViewById(R.id.lsmMatchesRv);
        listaSolicitudesView = (RecyclerView) root.findViewById(R.id.lsmSolicitudesRv);

        // Configuramos el RecyclerView con la lista de matches
        LinearLayoutManager layoutManagerMatches = new LinearLayoutManager(root.getContext());
        LinearLayoutManager layoutManagerSolicitudes = new LinearLayoutManager(root.getContext());
        listaMatchesView.setHasFixedSize(true);
        listaMatchesView.setLayoutManager(layoutManagerMatches);
        listaSolicitudesView.setHasFixedSize(true);
        listaSolicitudesView.setLayoutManager(layoutManagerSolicitudes);

        // Creamos adpaters y activamos listeners
        ListaMatchesAdapter lmAdater = new ListaMatchesAdapter(new ArrayList<User>(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(User usuario) {
                        clickonItem(usuario);
                    }
                });

        // Asignamos los adapters
        Firebase.addListenerToMatchesRecycler(lmAdater);
        listaMatchesView.setAdapter(lmAdater);

        ListaSolicitudesAdapter lsAdater = new ListaSolicitudesAdapter(new ArrayList<>(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(User usuario) {
                        clickonItem(usuario);
                    }
                });

        // Asignamos los adapters
        Firebase.addListenerToSolicitudesRecycler(lsAdater, root);
        listaSolicitudesView.setAdapter(lsAdater);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        // Desactivamos listeners
        Firebase.removeListenerToSolicitudesRecycler();
        Firebase.removeListenerToMatchesRecycler();
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