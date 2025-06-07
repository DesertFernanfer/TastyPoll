package com.fernando.tastypoll.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.clases.Encuesta;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DialogoInformacionEncuesta extends DialogFragment {

    private static final String ARG_ENCUESTA = "encuesta";
    private Encuesta encuesta;

    // Views
    private TextView nameEncuesta;
    private TextView descriptionSurvey;
    private TextView tieneEmailEncuesta;
    private TextView tieneAlimentosPredeterminadosEncuesta;
    private TextView estaActivaEncuesta;
    private TextView creacionEncuesta;
    private TextView caducidadEncuesta;
    private TextView votos;
    private Button botonAtras;

    public static DialogoInformacionEncuesta newInstance(Encuesta encuesta) {
        DialogoInformacionEncuesta dialog = new DialogoInformacionEncuesta();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ENCUESTA, encuesta);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            encuesta = (Encuesta) getArguments().getSerializable(ARG_ENCUESTA);
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
        View view = inflater.inflate(R.layout.dialog_informacion_encuesta, container, false);
        initUI(view);
        setupData();
        setupListeners();
        return view;
    }

    private void initUI(View view) {
        // Inicializar las vistas
        nameEncuesta = view.findViewById(R.id.nameEncuesta);
        descriptionSurvey = view.findViewById(R.id.descriptionSurvey);
        tieneEmailEncuesta = view.findViewById(R.id.tieneEmailEncuesta);
        tieneAlimentosPredeterminadosEncuesta = view.findViewById(R.id.tieneAlimentosPredeterminadosEncuesta);
        estaActivaEncuesta = view.findViewById(R.id.estaActivaEncuesta);
        creacionEncuesta = view.findViewById(R.id.creacionEncuesta);
        caducidadEncuesta = view.findViewById(R.id.caducidadEncuesta);
        votos = view.findViewById(R.id.votos);
        botonAtras = view.findViewById(R.id.botonAtras);
    }

    private void setupData() {
        if (encuesta == null) {
            dismiss();
            return;
        }

        // Formato de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        // Configurar los datos
        nameEncuesta.setText(encuesta.getNombre());
        descriptionSurvey.setText(encuesta.getDescripcion());

        // Configurar valores booleanos con texto legible
        tieneEmailEncuesta.setText(encuesta.getTieneEmail() ?
                getString(R.string.yes) : getString(R.string.no));

        tieneAlimentosPredeterminadosEncuesta.setText(encuesta.getTienealimentosPredeterminados() ?
                getString(R.string.yes) : getString(R.string.no));

        estaActivaEncuesta.setText(encuesta.getEstaActiva() ?
                getString(R.string.yes) : getString(R.string.no));

        // Configurar fechas
        try {
            creacionEncuesta.setText(dateFormat.format(encuesta.getFechaCreacion().toDate()));
            caducidadEncuesta.setText(dateFormat.format(encuesta.getFechaVencimiento().toDate()));
        } catch (Exception e) {
            creacionEncuesta.setText(getString(R.string.error_date));
            caducidadEncuesta.setText(getString(R.string.error_date));
        }

        // Configurar votos totales
        int totalVotos = 0;
        if (encuesta.getVotos() != null) {
            for (Integer voto : encuesta.getVotos().values()) {
                totalVotos += voto != null ? voto : 0;
            }
        }
        //votos.setText(String.valueOf(totalVotos));
        votos.setText(formatearVotos(encuesta.getVotos()));

    }

    private void setupListeners() {
        botonAtras.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar referencias para evitar memory leaks
        nameEncuesta = null;
        descriptionSurvey = null;
        tieneEmailEncuesta = null;
        tieneAlimentosPredeterminadosEncuesta = null;
        estaActivaEncuesta = null;
        creacionEncuesta = null;
        caducidadEncuesta = null;
        votos = null;
        botonAtras = null;
    }
    private String formatearVotos(HashMap<String, Integer> votos) {
        if (votos == null || votos.isEmpty()) {
            return "Sin votos";
        }

        StringBuilder resultado = new StringBuilder();
        for (Map.Entry<String, Integer> voto : votos.entrySet()) {
            if (resultado.length() > 0) {
                resultado.append("\n");
            }
            resultado.append(voto.getKey()).append(": ").append(voto.getValue()).append(" votos");
        }

        return resultado.toString();
    }
}