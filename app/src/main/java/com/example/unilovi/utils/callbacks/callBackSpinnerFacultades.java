package com.example.unilovi.utils.callbacks;

import android.content.Context;
import android.widget.Spinner;

import com.example.unilovi.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class callBackSpinnerFacultades implements CallBack {

    private Context context;
    private Spinner spinner;

    public callBackSpinnerFacultades(Context context, Spinner spinner) {
        this.context = context;
        this.spinner = spinner;
    }

    @Override
    public void methodToCallBack(Object object) {
        if (object != null) {
            List<String> facultades = new ArrayList<String>();
            facultades.add("Sin definir");
            facultades.addAll((List<String>) object);
            Util.rellenarSpinner(context, spinner, facultades);
        }
    }
}
