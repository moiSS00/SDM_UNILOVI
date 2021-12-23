package com.example.unilovi.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unilovi.MainActivity;
import com.example.unilovi.R;
import com.example.unilovi.SignInActivity;
import com.example.unilovi.database.Firebase;
import com.example.unilovi.databinding.FragmentHomeBinding;
import com.example.unilovi.databinding.FragmentPreferenciasBinding;
import com.example.unilovi.utils.CallBack;
import com.example.unilovi.utils.Util;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ImageView imagenPretendiente;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imagenPretendiente = root.findViewById(R.id.imagenPretendiente);

        return root;
    }


    /*
        En este método irá la funcionalidad de actualizar las búsquedas según las preferencias
    */
    @Override
    public void onResume() {
        super.onResume();
        Firebase.downloadImage("uo271397@uniovi.es", new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    Picasso.get().load((String) object).into(imagenPretendiente);
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