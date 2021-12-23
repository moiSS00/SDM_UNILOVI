package com.example.unilovi.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unilovi.R;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentHomeBinding;
import com.example.unilovi.model.Preferences;
import com.example.unilovi.model.User;
import com.example.unilovi.utils.CallBack;
import com.example.unilovi.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // Atribitos que contendrán una referencia a los componentes usados
    private FragmentHomeBinding binding;
    private ImageView imagenPretendiente;
    private TextView nombreApellidosPretendiente;
    private TextView facultadPretendiente;
    private TextView edadPretendiente;
    private Button btnAceptar;
    private Button btnRechazar;

    // Atributos auxiliares
    private ArrayList<String> emailsPretendientes;
    private int indexPretendiente;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtenemos referencias a los componentes
        imagenPretendiente = root.findViewById(R.id.imagenPretendiente);
        nombreApellidosPretendiente = root.findViewById(R.id.nombrePretendiente);
        facultadPretendiente = root.findViewById(R.id.facultadPretendiente);
        edadPretendiente = root.findViewById(R.id.edadPretendiente);
        btnAceptar = root.findViewById(R.id.btnAceptar);
        btnRechazar = root.findViewById(R.id.btnRechazar);

        // Asignamos listeners

        // -- Aceptar pretendiente --
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // -- Rechazar pretendiente --
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Inicializamos
        emailsPretendientes = new ArrayList<String>();
        indexPretendiente = 0;

        // Buscamos pretendientes
        Firebase.getPreferencesByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    Preferences preferences = (Preferences) object;
                    Firebase.getPretendientesByPreferences(preferences, new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {
                            emailsPretendientes = (ArrayList<String>) object;
                            if (!emailsPretendientes.isEmpty()) {
                                String emailPretendiente = emailsPretendientes.get(indexPretendiente);
                                Firebase.getUsuarioByEmail(emailPretendiente, new CallBack() {
                                    @Override
                                    public void methodToCallBack(Object object) {
                                        if (object != null) {
                                            User pretendiente = (User) object;
                                            nombreApellidosPretendiente.setText(pretendiente.getNombre()
                                                    + " " + pretendiente.getApellidos());
                                            facultadPretendiente.setText(pretendiente.getFacultad());
                                            edadPretendiente.setText("" + pretendiente.getEdad());
                                            // !!OJO!! A SUSTITUIR POR LA IMAGEN DEL USUARIO
                                            Firebase.downloadImage("uo271397@uniovi.es", new CallBack() {
                                                @Override
                                                public void methodToCallBack(Object object) {
                                                    if (object != null) {
                                                        Picasso.get().load((String) object)
                                                                .into(imagenPretendiente);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            else { // Si no hay pretendientes
                                btnAceptar.setEnabled(false);
                                btnRechazar.setEnabled(false);
                                Util.showAlert(getContext(), "No hay pretendientes disponibles " +
                                        "con sus preferencias");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}