package com.example.unilovi.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

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

    public static void showAlert(Context context, String message) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage(message);
            builder.setPositiveButton("Aceptar", null);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
