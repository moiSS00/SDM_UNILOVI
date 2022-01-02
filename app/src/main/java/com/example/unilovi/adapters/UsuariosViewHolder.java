package com.example.unilovi.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.R;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.CallBack;
import com.squareup.picasso.Picasso;

public class UsuariosViewHolder extends RecyclerView.ViewHolder {

    // Atribitos que contendrán una referencia a los componentes usados
    private TextView nombre;
    private TextView facultad;
    private ImageView imagen;

    public UsuariosViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = (TextView) itemView.findViewById(R.id.nombreUsuarioLineaText);
        facultad = (TextView) itemView.findViewById(R.id.facultadUsuarioLineaText);
        imagen = (ImageView) itemView.findViewById(R.id.imagenUsuarioLineaImg);
    }

    public void bindUser(final User usuario, final OnItemClickListener listener) {
        nombre.setText(usuario.getNombre());
        facultad.setText(usuario.getFacultad());
        Firebase.downloadImage(usuario.getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    Picasso.get().load((String) object).fit().into(imagen);
                }
                else {
                    Picasso.get().load(R.drawable.default_user_image).fit().into(imagen);
                }
                itemView.setOnClickListener((v) -> {
                    listener.onItemClick(usuario);
                });
            }
        });
    }
}
