package com.fernando.tastypoll.dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.adapter.BuscadorAlimentoAdapter;
import com.fernando.tastypoll.clases.Alimento;
import com.fernando.tastypoll.interfaces.OnAlimentosSeleccionados;

import java.util.ArrayList;


import Enums.CategoriaPlato;
import Enums.TipoDieta;

public class DialogBuscador extends DialogFragment {
    private Spinner spinnerTipoDieta,spinnerCategoriaPlato;

    private EditText buscador;
    private ArrayList<Alimento> listaAlimentos;
    private OnAlimentosSeleccionados listener;

    private RecyclerView recyclerView;

    // Método moderno para crear instancias
    public static DialogBuscador crearInstancia(ArrayList<Alimento> alimentos) {
        DialogBuscador fragment = new DialogBuscador();
        Bundle args = new Bundle();
        args.putParcelableArrayList("listaAlimentos", alimentos);
        fragment.setArguments(args);
        return fragment;
    }
    public void setOnAlimentosSeleccionadosListener(OnAlimentosSeleccionados listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            listaAlimentos = getArguments().getParcelableArrayList("listaAlimentos");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_buscador, container, false);
        iniciarElementos(view);
        return view;
    }

    private void iniciarElementos(View view) {
        inicializarSpinnerTipoDieta(view);
        inicializarSpinnerCaregoriaPlato(view);


        incializarBotones(view);
        buscador = view.findViewById(R.id.search);



        recyclerView = view.findViewById(R.id.recyclerView);

        BuscadorAlimentoAdapter adapter = new BuscadorAlimentoAdapter(listaAlimentos, requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

    }
    private void incializarBotones(View view){
        Button botonAtras = view.findViewById(R.id.botonAtras);

        botonAtras.setOnClickListener(v -> {
            dismiss();
        });
        Button seleccioanar = view.findViewById(R.id.botonSeleccionar);
        seleccioanar.setOnClickListener(v -> {
            //Logica enviar nuevos alimentos selecionados
            ArrayList<Alimento> alimentosSeleccionados = ((BuscadorAlimentoAdapter) recyclerView.getAdapter()).getAlimentosSeleccionados();
            if (listener != null && alimentosSeleccionados != null && !alimentosSeleccionados.isEmpty() ) {
                listener.onAlimentosSeleccionados(alimentosSeleccionados);
            }
            dismiss();
        });

        Button botonBuscar = view.findViewById(R.id.botonBuscar);
        botonBuscar.setOnClickListener(v -> {
            String query = buscador.getText().toString();
            TipoDieta dieta = obtenerTipoDietaSeleccionado();
            CategoriaPlato categoria = obtenerCategoriaSeleccionada();
            filtrarListaPorNombre(query, dieta, categoria);
        });


    }
    private void inicializarSpinnerTipoDieta(View view){
        spinnerTipoDieta = view.findViewById(R.id.spinnerTipoDieta);

        TipoDieta[] opciones = TipoDieta.values();

        ArrayAdapter<TipoDieta> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, TipoDieta.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDieta.setAdapter(adapter);

        spinnerTipoDieta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String itemSeleccionado = parent.getItemAtPosition(position).toString(); // ← Esto es lo que importa

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void inicializarSpinnerCaregoriaPlato(View view){
        spinnerCategoriaPlato = view.findViewById(R.id.spinnerCategoriaPlato);


        ArrayAdapter<CategoriaPlato> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, CategoriaPlato.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaPlato.setAdapter(adapter);
    }
    private void filtrarListaPorNombre(String query, TipoDieta dieta, CategoriaPlato categoria){
        ArrayList<Alimento> listaFiltrada = new ArrayList<>();

        if (listaAlimentos == null || listaAlimentos.isEmpty()) {
            ((BuscadorAlimentoAdapter) recyclerView.getAdapter()).actualizarLista(listaFiltrada);
            return;
        }

        for(Alimento alimento : listaAlimentos){
            // Validar que el alimento no sea null
            if (alimento == null) continue;

            boolean cumpleQuery = query.isEmpty() ||
                    (alimento.getNombre() != null &&
                            alimento.getNombre().toLowerCase().contains(query.toLowerCase()));

            boolean cumpleDieta = (dieta == TipoDieta.OMNIVORA ||
                    (alimento.getTipoDieta() != null && alimento.getTipoDieta().equals(dieta)));

            boolean cumpleCategoria = (categoria == CategoriaPlato.TODOS ||
                    (alimento.getCategoria() != null && alimento.getCategoria().equals(categoria)));

            if (cumpleQuery && cumpleDieta && cumpleCategoria) {
                listaFiltrada.add(alimento);
            }
        }

        ((BuscadorAlimentoAdapter) recyclerView.getAdapter()).actualizarLista(listaFiltrada);
    }

    private TipoDieta obtenerTipoDietaSeleccionado() {
        try {
            if (spinnerTipoDieta != null && spinnerTipoDieta.getSelectedItem() != null) {
                return TipoDieta.valueOf(spinnerTipoDieta.getSelectedItem().toString());
            }
        } catch (IllegalArgumentException e) {
            Log.e("DialogBuscador", "Error al obtener tipo de dieta: " + e.getMessage());
        }
        return TipoDieta.OMNIVORA;
    }

    // Similar para categoría:
    private CategoriaPlato obtenerCategoriaSeleccionada(){
        try {
            if (spinnerCategoriaPlato != null && spinnerCategoriaPlato.getSelectedItem() != null) {
                return CategoriaPlato.valueOf(spinnerCategoriaPlato.getSelectedItem().toString());
            }
        } catch (IllegalArgumentException e) {
            Log.e("DialogBuscador", "Error al obtener categoría: " + e.getMessage());
        }
        return CategoriaPlato.TODOS;
    }


}