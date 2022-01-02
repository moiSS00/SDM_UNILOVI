package com.example.unilovi.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.R;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.CallBack;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListaMatchesAdapter extends RecyclerView.Adapter<UsuariosViewHolder> {

    // Atributos auxiliares
    private final OnItemClickListener listener;
    private List<User> listaUsuarios;

    public ListaMatchesAdapter(List<User> listaUsuarios, OnItemClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    // --- Métodos que hay que sobrescribir al heredar de  RecyclerView.Adapter ---

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_match, parent, false);
        return new UsuariosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        // Extrae de la lista el elemento indicando por posición
        User usuario = listaUsuarios.get(position);
        Log.i("Lista", "Visualiza elemento " + usuario.getEmail());

        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(usuario, listener);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

}
