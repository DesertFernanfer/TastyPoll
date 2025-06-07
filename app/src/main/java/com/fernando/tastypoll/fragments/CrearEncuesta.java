package com.fernando.tastypoll.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.adapter.CrearEncuestaAdapter;
import com.fernando.tastypoll.clases.Alimento;
import com.fernando.tastypoll.clases.Encuesta;
import com.fernando.tastypoll.clases.FireBaseManager;
import com.fernando.tastypoll.clases.Singleton;
import com.fernando.tastypoll.dialog.DialogBuscador;
import com.fernando.tastypoll.interfaces.OnAlimentosCargados;
import com.fernando.tastypoll.interfaces.OnAlimentosSeleccionados;

import java.util.ArrayList;

import Enums.TipoDieta;

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
    private RadioGroup radioGroup;

    private CheckBox alimentosPredeterminadoCheckBox;
    private Spinner spinnerDieta;
    private EditText inputNombreEncuesta, inputDescripcionEncuesta, inputTiempoVida;

    private Button botonAnhadirAlimento, botonEliminar, botonCrear;
    private RecyclerView recyclerView;
    private boolean tieneAlimentosPredeterminados, tieneEmail;







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
        tieneAlimentosPredeterminados = false;

        referenciarElementos(view);


        CrearEncuestaAdapter adapter = inicializarRecyclerView();





        inicilizarBotones(view,adapter);

        inicializarCheckbox();
        inicializarRadioGroup();
        inicializarSpinner();
    }
    public void referenciarElementos(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        botonAnhadirAlimento = view.findViewById(R.id.botonAnhadirAlimento);
        botonCrear = view.findViewById(R.id.botonCrear);
        botonEliminar = view.findViewById(R.id.botonEliminar);
        radioGroup = view.findViewById(R.id.radioGroupEmailRequerido);
        inputNombreEncuesta = view.findViewById(R.id.inputNombreEncuesta);
        inputDescripcionEncuesta = view.findViewById(R.id.inputDescripcionEncuesta);
        inputTiempoVida = view.findViewById(R.id.inputTiempoVida);
        alimentosPredeterminadoCheckBox = view.findViewById(R.id.checkboxAlimentosPrdeterminados);
        spinnerDieta = view.findViewById(R.id.spinnerDieta);

    }
    private void inicializarSpinner(){
        TipoDieta[] opciones = TipoDieta.values();

        ArrayAdapter<TipoDieta> adapter = new ArrayAdapter<>(
        requireContext(), android.R.layout.simple_spinner_item, TipoDieta.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDieta.setAdapter(adapter);
    }
    private void inicilizarBotones(View view,CrearEncuestaAdapter adapter){



        Button botonAtras = view.findViewById(R.id.botonAtras);
        botonAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });



        botonEliminar = view.findViewById(R.id.botonEliminar);
        botonEliminar.setOnClickListener(v -> {
            adapter.borrarAlimentos();
        });
        botonAnhadirAlimento.setOnClickListener(v -> {
            //Logica para abrir el buscador de alimentos

            if (alimentos != null && !alimentos.isEmpty() && tieneAlimentosPredeterminados) {
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



        botonCrear.setOnClickListener(v -> {
            if(camposValidos()){
                botonCrear.setEnabled(false);

                int tiempoVida = Integer.parseInt(inputTiempoVida.getText().toString().trim());
                String nombre = inputNombreEncuesta.getText().toString().trim();
                String descripcion = inputDescripcionEncuesta.getText().toString().trim();

                Encuesta encuesta;
                if(tieneAlimentosPredeterminados){
                    encuesta = new Encuesta(nombre,descripcion,tieneEmail,tiempoVida,true,TipoDieta.OMNIVORA,tieneAlimentosPredeterminados,listaAlimentosRecyclerView,null,null);
                } else {
                    TipoDieta tipoDieta = TipoDieta.valueOf(spinnerDieta.getSelectedItem().toString());
                    encuesta = new Encuesta(nombre,descripcion,tieneEmail,tiempoVida,true,tipoDieta,tieneAlimentosPredeterminados,null,null,null);
                }

                singleton.getFireBaseManager().crearEncuesta(encuesta, new FireBaseManager.OnEncuestaCreada() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(),getString(R.string.create_survey_sucessfully),Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                        botonCrear.setEnabled(true);
                    }
                });
            }
        });


        cambiarDeEstadoAlimentosPredeterminados();
    }
    private void inicializarCheckbox(){
        alimentosPredeterminadoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Está marcado
                    tieneAlimentosPredeterminados = true;
                } else {
                    tieneAlimentosPredeterminados = false;
                }
                cambiarDeEstadoAlimentosPredeterminados();
            }
        });
    }
    private void inicializarRadioGroup(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonEmailRequired) {
                    tieneEmail = true;
                } else if (checkedId == R.id.radioButtonEmailNotRequired) {
                    tieneEmail = false;
                }
            }
        });
    }
    private void cambiarDeEstadoAlimentosPredeterminados(){
        botonEliminar.setEnabled(tieneAlimentosPredeterminados);
        botonAnhadirAlimento.setEnabled(tieneAlimentosPredeterminados);
        recyclerView.setEnabled(tieneAlimentosPredeterminados);
        spinnerDieta.setEnabled(!tieneAlimentosPredeterminados);
    }
    private boolean camposValidos(){
        boolean camposValidos = true;
        if (inputNombreEncuesta.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.error_empty_name), Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (inputDescripcionEncuesta.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.error_empty_description), Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), getString(R.string.error_no_radio_selected), Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (listaAlimentosRecyclerView.isEmpty() && tieneAlimentosPredeterminados) {
            Toast.makeText(getContext(), getString(R.string.error_no_food_items), Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (!esNumero(inputTiempoVida.getText().toString().trim())) {
            camposValidos = false;
        }




        return  camposValidos;
    }
    private CrearEncuestaAdapter  inicializarRecyclerView(){
        CrearEncuestaAdapter adapter = new CrearEncuestaAdapter(listaAlimentosRecyclerView,requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        return adapter;
    }
    private boolean esNumero(String value){
        boolean esNumero = true;

        try {
            int numero = Integer.parseInt(value);
            if(numero <= 0){
                esNumero = false;
                Toast.makeText(getContext(), getString(R.string.error_negative_number), Toast.LENGTH_SHORT).show();

            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), getString(R.string.error_invalid_lifetime), Toast.LENGTH_SHORT).show();

            esNumero = false;
        }
        return esNumero;
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