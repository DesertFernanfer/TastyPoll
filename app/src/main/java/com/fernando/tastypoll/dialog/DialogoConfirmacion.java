package com.fernando.tastypoll.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoConfirmacion extends DialogFragment {

    private static final String ARG_TITULO = "titulo";
    private static final String ARG_MENSAJE = "mensaje";
    private static final String ARG_TEXTO_POSITIVO = "texto_positivo";
    private static final String ARG_TEXTO_NEGATIVO = "texto_negativo";

    private OnConfirmacionListener listener;

    public interface OnConfirmacionListener {
        void onConfirmar();
        void onCancelar();
    }

    public static DialogoConfirmacion newInstance(String titulo, String mensaje,
                                                  String textoPositivo, String textoNegativo) {
        DialogoConfirmacion dialog = new DialogoConfirmacion();
        Bundle args = new Bundle();
        args.putString(ARG_TITULO, titulo);
        args.putString(ARG_MENSAJE, mensaje);
        args.putString(ARG_TEXTO_POSITIVO, textoPositivo);
        args.putString(ARG_TEXTO_NEGATIVO, textoNegativo);
        dialog.setArguments(args);
        return dialog;
    }

    // Sobrecarga para usar textos por defecto
    public static DialogoConfirmacion newInstance(String titulo, String mensaje) {
        return newInstance(titulo, mensaje, "Sí", "No");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            dismiss();
            return super.onCreateDialog(savedInstanceState);
        }

        String titulo = args.getString(ARG_TITULO, "Confirmación");
        String mensaje = args.getString(ARG_MENSAJE, "¿Estás seguro?");
        String textoPositivo = args.getString(ARG_TEXTO_POSITIVO, "Sí");
        String textoNegativo = args.getString(ARG_TEXTO_NEGATIVO, "No");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(textoPositivo, (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirmar();
                    }
                })
                .setNegativeButton(textoNegativo, (dialog, which) -> {
                    if (listener != null) {
                        listener.onCancelar();
                    }
                });

        return builder.create();
    }

    public void setOnConfirmacionListener(OnConfirmacionListener listener) {
        this.listener = listener;
    }
}