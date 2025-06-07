package com.fernando.tastypoll.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.clases.Singleton;
import com.fernando.tastypoll.clases.Usuario;

import java.util.Locale;

import Enums.TipoDieta;

public class Ajustes extends Fragment {

    private Spinner spinnerTipoDieta;
    private EditText nombre;
    private Usuario usuario;
    private RadioGroup radioGroup;
    private RadioButton radioEN, radioES;
    private Singleton singleton;

    public Ajustes(){

    }
    public static Ajustes  newInstance(){
        Ajustes fragment = new Ajustes();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = Singleton.getInstancia();
        usuario = singleton.getUsuario();

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ajustes, container, false);

        iniciarlizarElementos(view);
        incializarRadioGroup(view);
        return view;
    }

    private void escribirDatosUsuario(){
        spinnerTipoDieta.setSelection(usuario.getTipoDieta().ordinal());
        nombre.setText(usuario.getNombre());
    }
    private void iniciarlizarElementos(View view){

        nombre = view.findViewById(R.id.nombreUsuario);
        inicializarSpinnerTipoDieta(view);
        Button botonAtras = view.findViewById(R.id.botonAtras);
        botonAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        Button guardar = view.findViewById(R.id.botonGuardar);
        guardar.setOnClickListener(v -> {
            String inputNombre = nombre.getText().toString();
            if(!inputNombre.isEmpty() && !inputNombre.trim().equals(usuario.getNombre().trim())){
                   singleton.getFireBaseManager().actualizarNombreUsuario(inputNombre);
            }
            if(spinnerTipoDieta.getSelectedItemPosition() != usuario.getTipoDieta().ordinal()){
                singleton.getFireBaseManager().actualizarTipoDieta((TipoDieta) spinnerTipoDieta.getSelectedItem());
            }
            singleton.actualizarUsuario();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        escribirDatosUsuario();
    }
    /*
    private void incializarRadioGroup(View view){
        radioGroup = view.findViewById(R.id.radioGroup);
        radioEN = view.findViewById(R.id.radioButtonEN);
        radioES = view.findViewById(R.id.radioButtonES);

        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String idioma = prefs.getString("idioma", "en"); // valor por defecto: español

        // seleccionar por defecto
        if (idioma.equals("es")){
            radioES.setChecked(true);
        } else {
            radioEN.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String idiomaInput = "es";
            if(checkedId == R.id.radioButtonEN){
                idiomaInput="en";
            }
            prefs.edit().putString("idioma", idiomaInput).apply();
            Intent intent = requireActivity().getPackageManager()
                    .getLaunchIntentForPackage(requireActivity().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();

        });
    }*/
    private void inicializarSpinnerTipoDieta(View view){
        spinnerTipoDieta = view.findViewById(R.id.spinnerTipoDieta);


        ArrayAdapter<TipoDieta> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, TipoDieta.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDieta.setAdapter(adapter);

        Log.d("dieta",usuario.getTipoDieta().toString());

    }

    private void incializarRadioGroup(View view) {
        // 1. Inicialización de vistas con verificación de nulos
        radioGroup = view.findViewById(R.id.radioGroup);
        radioEN = view.findViewById(R.id.radioButtonEN);
        radioES = view.findViewById(R.id.radioButtonES);



        // 2. Obtener preferencias de idioma
        SharedPreferences prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String idioma = prefs.getString("idioma", "en"); // Valor por defecto: inglés

        String currentLanguage = Locale.getDefault().getLanguage();  // Obtiene el idioma actual del sistema

// 3. Co
        if (idioma.equals("es") || currentLanguage.equals("es")) {
            radioES.setChecked(true);
            radioEN.setChecked(false);
        } else {
            radioEN.setChecked(true);
            radioES.setChecked(false);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String nuevoIdioma;
            if(checkedId == R.id.radioButtonEN){
                nuevoIdioma = "en";
            } else{
                nuevoIdioma = "es";
            }

            prefs.edit().putString("idioma", nuevoIdioma).apply();

            // Reiniciar la actividad de forma más eficiente
           // cambiarIdioma(idioma);
            reiniciarActividad();
        });
    }

    private void reiniciarActividad() {
        Intent intent = new Intent(requireActivity(), requireActivity().getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        requireActivity().finish();
    }


}

