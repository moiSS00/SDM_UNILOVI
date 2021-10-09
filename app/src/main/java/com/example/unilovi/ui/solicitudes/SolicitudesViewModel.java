package com.example.unilovi.ui.solicitudes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SolicitudesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SolicitudesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}