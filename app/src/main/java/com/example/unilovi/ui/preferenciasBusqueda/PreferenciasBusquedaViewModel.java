package com.example.unilovi.ui.preferenciasBusqueda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PreferenciasBusquedaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PreferenciasBusquedaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}