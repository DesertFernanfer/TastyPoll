package com.fernando.tastypoll.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.adapter.BuscadorAlimentoAdapter;
import com.fernando.tastypoll.adapter.CrearEncuestaAdapter;
import com.fernando.tastypoll.clases.Alimento;
import com.fernando.tastypoll.clases.Encuesta;
import com.fernando.tastypoll.clases.Singleton;
import com.fernando.tastypoll.dialog.DialogBuscador;
import com.fernando.tastypoll.interfaces.OnAlimentosCargados;
import com.fernando.tastypoll.interfaces.OnAlimentosSeleccionados;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearEncuesta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearEncuesta extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Singleton singleton;
    private ArrayList<Alimento> alimentos;
    private ArrayList<Alimento> listaAlimentosRecyclerView = new ArrayList<>();
    private CrearEncuesta fragment = this;


    private RecyclerView recyclerView;

    public CrearEncuesta() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CrearEncuesta newInstance() {
        CrearEncuesta fragment = new CrearEncuesta();
        Bundle args = new Bundle();

        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_encuesta, container, false);
        iniciarlizarElementos(view);

        return view;
    }

    private void iniciarlizarElementos(View view){
        singleton = Singleton.getInstancia();
        singleton.getFireBaseManager().getListaAlimentos(new OnAlimentosCargados() {
            @Override
            public void onAlimentosCargados(ArrayList<Alimento> alimentos) {
                fragment.alimentos = alimentos;
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);

       CrearEncuestaAdapter adapter = inicializarRecyclerView();


        Button botonAnhadirAlimento = view.findViewById(R.id.botonAnhadirAlimento);
        botonAnhadirAlimento.setOnClickListener(v -> {
            //Logica para abrir el buscador de alimentos

            if (alimentos != null && !alimentos.isEmpty()) {
                DialogBuscador dialog = DialogBuscador.crearInstancia(alimentos);
                dialog.setOnAlimentosSeleccionadosListener(new OnAlimentosSeleccionados() {
                    @Override
                    public void onAlimentosSeleccionados(ArrayList<Alimento> alimentosSeleccionados) {

                        Log.d("CrearEncuesta", "Alimentos seleccionados: " + alimentosSeleccionados.size());
                        //listaAlimentosRecyclerView.clear();
                        actualizarListaAlimentos(alimentosSeleccionados,adapter);

                    }
                });
                dialog.show(getParentFragmentManager(), "DialogBuscador");
            } else {
                Toast.makeText(getContext(), "Cargando alimentos, intenta en un momento", Toast.LENGTH_SHORT).show();
            }




        });

        inicilizarBotones(view,adapter);



    }
    private void inicilizarBotones(View view,CrearEncuestaAdapter adapter){
        Button botonAtras = view.findViewById(R.id.botonAtras);
        botonAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        Button crearEncuesta = view.findViewById(R.id.botonCrear);
        crearEncuesta.setOnClickListener(v -> {
            Encuesta enceusta = new Encuesta(listaAlimentosRecyclerView);
            singleton.getFireBaseManager().crearEncuesta(enceusta);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        Button borrarAlimentos = view.findViewById(R.id.botonEliminar);
        borrarAlimentos.setOnClickListener(v -> {
            adapter.borrarAlimentos();
        });

    }
    private CrearEncuestaAdapter  inicializarRecyclerView(){
        CrearEncuestaAdapter adapter = new CrearEncuestaAdapter(listaAlimentosRecyclerView,requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        return adapter;
    }
    private void actualizarListaAlimentos(ArrayList<Alimento> alimentosSeleccionados, CrearEncuestaAdapter adapter){
        for(Alimento alimentoNuevo : alimentosSeleccionados){

                if(!listaAlimentosRecyclerView.contains(alimentoNuevo)){
                    listaAlimentosRecyclerView.add(alimentoNuevo);
                    adapter.notifyItemChanged(listaAlimentosRecyclerView.size());
                }

        }
    }


}