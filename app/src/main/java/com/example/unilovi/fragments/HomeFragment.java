package com.example.unilovi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unilovi.MainActivity;
import com.example.unilovi.R;
import com.example.unilovi.ShowUserActivity;
import com.example.unilovi.SignInActivity;
import com.example.unilovi.SignUpActivity1;
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
    private ImageButton imagenPretendiente;
    private TextView nombreApellidosPretendiente;
    private TextView facultadPretendiente;
    private TextView edadPretendiente;
    private Button btnAceptar;
    private Button btnRechazar;

    // Atributos auxiliares
    public static final String EMAIL_DETALLE = "email_detalle";
    private ArrayList<String> emailsPretendientes;
    private int indexPretendiente;
    private boolean detail = false;

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

        // -- Mostrar información del pretendiente --
        imagenPretendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Se pasa el email del usuario a comprobar
                Intent detailIntent = new Intent(getActivity(), ShowUserActivity.class);
                detailIntent.putExtra(EMAIL_DETALLE, emailsPretendientes.get(indexPretendiente));

                // Se muestra la activity con los detalles
                detail = true;
                startActivity(detailIntent);

            }
        });

        // -- Aceptar pretendiente --
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexPretendiente + 1 < emailsPretendientes.size()) {
                    indexPretendiente++;
                    loadEmail(emailsPretendientes.get(indexPretendiente));
                    // Aqui se acualizaria lista de pretendientes
                }
            }
        });

        // -- Rechazar pretendiente --
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexPretendiente + 1 > emailsPretendientes.size()) {
                    indexPretendiente++;
                    loadEmail(emailsPretendientes.get(indexPretendiente));
                    // Aqui se acualizaria lista de pretendientes
                }
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Inicializamos
        if (!detail) {
            emailsPretendientes = new ArrayList<String>();
            indexPretendiente = 0;
        }
        else {
            detail = false;
        }

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
                                loadEmail(emailPretendiente);
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

    private void loadEmail(String email) {
        Firebase.getUsuarioByEmail(email, new CallBack() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}