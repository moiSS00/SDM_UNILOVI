package com.example.unilovi.adapters.matches;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.R;
import com.example.unilovi.adapters.OnItemClickListener;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.database.CallBack;
import com.squareup.picasso.Picasso;

public class MatchesViewHolder extends RecyclerView.ViewHolder {

    // Atribitos que contendrán una referencia a los componentes usados
    private TextView nombre;
    private TextView facultad;
    private ImageView imagen;

    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = (TextView) itemView.findViewById(R.id.lmNombreUsuario);
        facultad = (TextView) itemView.findViewById(R.id.lmFacultadUsuario);
        imagen = (ImageView) itemView.findViewById(R.id.lmImagenUsuario);
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
