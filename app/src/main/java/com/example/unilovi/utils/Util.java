package com.example.unilovi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.unilovi.R;

import java.util.List;

public class Util {

    /**
     * Rellena un spinner dado con una lista de strings dada
     * @param spinner Spinner concreto a rellenar
     * @param list Lista de strings
     */
    public static void rellenarSpinner(Context context, Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Muestra un mensaje de error personalizado
     * @param context Contexto del activity / fragmento
     * @param message Mensaje de error
     */
    public static void showErrorDialog(Context context, String message) {
        if (context != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_error_dialog,
                    (ConstraintLayout) ((Activity) context).findViewById(R.id.layoutDialogContainer));
            builder.setView(view);

            ((TextView) view.findViewById(R.id.edTitulo)).setText(" ¡ UPS !");
            ((TextView) view.findViewById(R.id.edMensaje)).setText(message);
            ((Button) view.findViewById(R.id.edButton)).setText("OK");
            ((ImageView) view.findViewById(R.id.edIcon)).setImageResource(R.drawable.icono_432);

            final AlertDialog alertDialog = builder.create();

            view.findViewById(R.id.edButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            alertDialog.show();

        }
    }


}
