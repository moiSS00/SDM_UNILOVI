package com.example.unilovi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout layoutInfo;
    private LinearLayout layoutBotones;

    // Atributos auxiliares
    public static final String USUARIO_PRETENDIENTE = "usuario_pretendiente";
    private User pretendiente;
    private User usuarioActual;
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
        layoutInfo = root.findViewById(R.id.layoutHomeInfo);
        layoutBotones = root.findViewById(R.id.layoutHomeBotones);

        // Recogemos los datos del usuario actual
        Firebase.getUsuarioByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                usuarioActual = (User) object;
            }
        });

        // Asignamos listeners

        // -- Mostrar información del pretendiente --
        imagenPretendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Se pasa el email del usuario a comprobar
                Intent detailIntent = new Intent(getActivity(), ShowUserActivity.class);
                detailIntent.putExtra(USUARIO_PRETENDIENTE, pretendiente);

                // Se muestra la activity con los detalles
                detail = true;
                startActivity(detailIntent);

            }
        });

        // -- Aceptar pretendiente --
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Modificamos las lista de ambos usuarios
                usuarioActual.getRechazados().add(pretendiente.getEmail());
                pretendiente.getSolicitudes().add(usuarioActual.getEmail());

                Firebase.updateUser(usuarioActual.getEmail(), usuarioActual, new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        Firebase.updateUser(pretendiente.getEmail(), pretendiente, new CallBack() {
                            @Override
                            public void methodToCallBack(Object object) {
                                getNextPretendiente();
                            }
                        });
                    }
                });

            }
        });

        // -- Rechazar pretendiente --
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Modificamos las lista de ambos usuarios
                usuarioActual.getRechazados().add(pretendiente.getEmail());
                pretendiente.getRechazados().add(usuarioActual.getEmail());

                Firebase.updateUser(usuarioActual.getEmail(), usuarioActual, new CallBack() {
                    @Override
                    public void methodToCallBack(Object object) {
                        Firebase.updateUser(pretendiente.getEmail(), pretendiente, new CallBack() {
                            @Override
                            public void methodToCallBack(Object object) {
                                getNextPretendiente();
                            }
                        });
                    }
                });

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!detail) {
            getNextPretendiente();
        }
        else {
            detail = false;
        }

    }

    private void getNextPretendiente() {
        Firebase.getPreferencesByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    Preferences preferences = (Preferences) object;
                    Firebase.getPretendientesByPreferences(preferences, new CallBack() {
                        @Override
                        public void methodToCallBack(Object object) {
                            if (object != null) {
                                layoutBotones.setVisibility(View.VISIBLE);
                                layoutInfo.setVisibility(View.VISIBLE);
                                pretendiente = (User) object;
                                nombreApellidosPretendiente.setText(pretendiente.getNombre()
                                        + " " + pretendiente.getApellidos());
                                facultadPretendiente.setText(pretendiente.getFacultad());
                                edadPretendiente.setText("" + pretendiente.getEdad());
                                // !!OJO!! A SUSTITUIR POR LA IMAGEN DEL USUARIO
                                Firebase.downloadImage("uo270824@uniovi.es", new CallBack() {
                                    @Override
                                    public void methodToCallBack(Object object) {
                                        Picasso.get().load((String) object).into(imagenPretendiente);
                                    }
                                });
                            }
                            else {
                                // Se cargaria una imagen por defecto
                                layoutBotones.setVisibility(View.INVISIBLE);
                                layoutInfo.setVisibility(View.INVISIBLE);
                                Util.showAlert(getContext(), "No hay usuarios que coincidan con tus preferencias");
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