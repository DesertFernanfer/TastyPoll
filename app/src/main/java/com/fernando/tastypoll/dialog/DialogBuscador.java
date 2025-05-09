package com.fernando.tastypoll.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Spinner;

import com.fernando.tastypoll.R;

public class DialogBuscador extends DialogFragment {
    private Spinner spinnerTipoDieta;
    private Spinner spinnerCategoriaPlato;

    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_buscador, container, false);
        iniciarlizarElementos(view);
        return view;
    }

    private void iniciarlizarElementos(View view){
        spinnerCategoriaPlato = view.findViewById(R.id.spinnerCategoriaPlato);
        spinnerTipoDieta = view.findViewById(R.id.spinnerTipoDieta);
        searchView = view.findViewById(R.id.searchView);


    }

}
