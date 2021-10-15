package com.example.unilovi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuariosWiewHolder> {

    // Atributos auxiliares
    private final OnItemClickListener listener;
    private List<User> listaUsuarios;

    public ListaUsuariosAdapter(List<User> listaUsuarios, OnItemClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    /**
     * Interfaz para menjar el evento click
     */
    public interface OnItemClickListener {
        void onItemClick(User item);
    }


    // --- Métodos que hay que sobrescribir al heredar de  RecyclerView.Adapter ---

    @NonNull
    @Override
    public UsuariosWiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_usuario, parent, false);
        return new UsuariosWiewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosWiewHolder holder, int position) {
        // Extrae de la lista el elemento indicando por posición
        User usuario = listaUsuarios.get(position);
        Log.i("Lista", "Visualiza elemento " + usuario);

        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(usuario, listener);
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    /**
     * Clase interna que define los componentes de la vista
     */
    public static class UsuariosWiewHolder extends RecyclerView.ViewHolder {

        // Atribitos que contendrán una referencia a los componentes usados
        private TextView nombre;
        private TextView facultad;
        private ImageView imagen;

        public UsuariosWiewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombreUsuarioLineaText);
            facultad = (TextView) itemView.findViewById(R.id.facultadUsuarioLineaText);
            imagen = (ImageView) itemView.findViewById(R.id.imagenUsuarioLineaImg);
        }

        public void bindUser(final User usuario, final OnItemClickListener listener) {
            nombre.setText(usuario.getName());
            facultad.setText(usuario.getSchool());
            Picasso.get().load("https://i.postimg.cc/vBx065cX/default-user-image.png").into(imagen);
            itemView.setOnClickListener((v) -> {
                listener.onItemClick(usuario);
            });
        }

    }

}
