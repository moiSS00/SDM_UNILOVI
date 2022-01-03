package com.example.unilovi.adapters.solicitudes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.R;
import com.example.unilovi.adapters.OnItemClickListener;
import com.example.unilovi.model.User;

import java.util.List;

public class ListaSolicitudesAdapter extends RecyclerView.Adapter<SolicitudesViewHolder> {

    // Atributos auxiliares
    private final OnItemClickListener listener;
    private List<User> listaSolicitudes;

    public ListaSolicitudesAdapter(List<User> listaSolicitudes, OnItemClickListener listener) {
        this.listaSolicitudes = listaSolicitudes;
        this.listener = listener;
    }

    public void swap(List<User> nuevalistaSolicitudes) {
        listaSolicitudes.clear();
        listaSolicitudes.addAll(nuevalistaSolicitudes);
        notifyDataSetChanged();
    }

    // --- Métodos que hay que sobrescribir al heredar de  RecyclerView.Adapter ---

    @NonNull
    @Override
    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_solicitud, parent, false);
        return new SolicitudesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesViewHolder holder, int position) {

        // Extrae de la lista el elemento indicando por posición
        User usuario = listaSolicitudes.get(position);
        Log.i("Lista", "Visualiza elemento " + usuario.getEmail());

        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(usuario, listener);
    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public List<User> getListaSolicitudes() {
        return listaSolicitudes;
    }
}
