package com.example.unilovi.adapters.solicitudes;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilovi.R;
import com.example.unilovi.adapters.OnItemClickListener;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.model.User;
import com.example.unilovi.database.CallBack;
import com.squareup.picasso.Picasso;

public class SolicitudesViewHolder extends RecyclerView.ViewHolder {

    // Atribitos que contendrán una referencia a los componentes usados
    private TextView nombre;
    private TextView facultad;
    private ImageView imagen;
    private ImageButton btnAceptarSolicitud;
    private Button btnRechazarSolicitud;

    public SolicitudesViewHolder(@NonNull View itemView) {
        super(itemView);
        nombre = (TextView) itemView.findViewById(R.id.lsNombreUsuario);
        facultad = (TextView) itemView.findViewById(R.id.lsFacultadUsuario);
        imagen = (ImageView) itemView.findViewById(R.id.lsImagenUsuario);
        btnAceptarSolicitud = (ImageButton) itemView.findViewById(R.id.lsAceptarSolicitudButton);
        btnRechazarSolicitud = (Button) itemView.findViewById(R.id.lsRechazarSolicitudButton);
    }

    public void bindUser(final User usuario, final OnItemClickListener listener) {
        // Asignamos listeners

        // -- Aceptar solicitud --
        btnAceptarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.aceptarSolicitud(usuario.getEmail());
                Toast.makeText(v.getContext(),
                        usuario.getNombre() + " añadid@ a matches",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // -- Rechazar solicitud --
        btnRechazarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.rechazarSolicitud(usuario.getEmail());
                Toast.makeText(v.getContext(),
                        usuario.getNombre() + " rechazad@",
                        Toast.LENGTH_SHORT).show();
            }
        });


        // Recogemos la información del usuario
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
