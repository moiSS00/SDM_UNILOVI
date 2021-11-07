package com.example.unilovi.utils.callbacks;

import android.content.Context;
import android.widget.Spinner;

import com.example.unilovi.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class callBackSpinnerCarreras implements CallBack {

    private Context context;
    private Spinner spinner;

    public callBackSpinnerCarreras(Context context, Spinner spinner) {
        this.context = context;
        this.spinner = spinner;
    }

    @Override
    public void methodToCallBack(Object object) {
        if (object != null) {
            Util.rellenarSpinner(context, spinner, (List<String>) object);
            spinner.setEnabled(true);
        }
        else {
            List<String> defaultList = new ArrayList<String>();
            defaultList.add("Sn definir");
            Util.rellenarSpinner(context, spinner, defaultList);
            spinner.setEnabled(false);
        }
    }
}
