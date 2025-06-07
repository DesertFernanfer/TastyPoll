package com.fernando.tastypoll.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.adapter.HistorialEncuestasAdapter;
import com.fernando.tastypoll.clases.Encuesta;
import com.fernando.tastypoll.clases.FireBaseManager;
import com.fernando.tastypoll.clases.Singleton;
import com.fernando.tastypoll.dialog.DialogCompartirEncuesta;
import com.fernando.tastypoll.dialog.DialogoConfirmacion;
import com.fernando.tastypoll.dialog.DialogoInformacionEncuesta;

import java.util.ArrayList;
import java.util.List;


public class BaulEncuestas extends Fragment implements HistorialEncuestasAdapter.OnItemClickListener{

    private RecyclerView recyclerViewEncuestas;
    private HistorialEncuestasAdapter encuestaAdapter;
    private ArrayList<Encuesta> listaDeEncuestas;
    private Singleton singleton;

    public BaulEncuestas() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = Singleton.getInstancia();
        //singleton.getUsuario()
        listaDeEncuestas = singleton.getUsuario().getEncuestas();
        //listaDeEncuestas = generarEncuestasDePrueba();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baul_encuestas, container, false);

        recyclerViewEncuestas = view.findViewById(R.id.recyclerView);
        recyclerViewEncuestas.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa el adaptador y asigna el listener
        encuestaAdapter = new HistorialEncuestasAdapter(getContext(), this);
        recyclerViewEncuestas.setAdapter(encuestaAdapter);

        // Pasa los datos al adaptador
        encuestaAdapter.setEncuestas(listaDeEncuestas);

        return view;
    }
    @Override
    public void onItemClick(Encuesta encuesta) {
       // Toast.makeText(getContext(), "Has hecho clic en: " + encuesta.getNombre(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMenuClick(Encuesta encuesta, View menuView) {

        PopupMenu popup = new PopupMenu(getContext(), menuView);
        popup.getMenuInflater().inflate(R.menu.menu_encuestas_acciones, popup.getMenu());
        // Necesitarás crear este menú XML
        MenuItem finishItem = popup.getMenu().findItem(R.id.action_finish);
        if (finishItem != null) {
            finishItem.setEnabled(encuesta.getEstaActiva());
        }
        popup.setOnMenuItemClickListener(item -> {
            boolean eventoManejado = true;
            int id = item.getItemId();
            if (id == R.id.action_info) {
                mostrarInfo(encuesta);
            } else if (id == R.id.action_share) {
                generarDialogoCompartiEncuesta(encuesta);

            } else if (id == R.id.action_finish) {
                mostrarConfirmacionFinalizar(encuesta);
            } else if (id == R.id.action_delete) {

                mostrarConfirmacionEliminar(encuesta);
            } else {
                eventoManejado = false;
            }
            return eventoManejado;

        });
        popup.show();


    }
    private void mostrarInfo(Encuesta encuesta){

        DialogoInformacionEncuesta dialog = DialogoInformacionEncuesta.newInstance(encuesta);
        dialog.show(getParentFragmentManager(), "DialogoInformacionEncuesta");
    }


    private void eliminarEncuesta(Encuesta encuesta) {
        singleton.getFireBaseManager().eliminarEncuesta(encuesta.getId(), new FireBaseManager.OnEncuestaEliminadaListener() {
            @Override
            public void onExito() {
                Toast.makeText(getContext(), getString(R.string.delete_survey_success), Toast.LENGTH_SHORT).show();

                singleton.actualizarUsuario(); // Esto recarga las encuestas
                int posicion = listaDeEncuestas.indexOf(encuesta);
                listaDeEncuestas.remove(posicion);

                encuestaAdapter.notifyItemRemoved(posicion);



            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), getString(R.string.delete_survey_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarConfirmacionEliminar(Encuesta encuesta) {
        String titulo = getString(R.string.delete_survey);
        String mensaje = getString(R.string.delete_survey_advice);
        mensaje = " " + encuesta.getNombre() + getString(R.string.action_cannot_undo);

        DialogoConfirmacion dialog = DialogoConfirmacion.newInstance(titulo, mensaje, "Eliminar", "Cancelar");
        dialog.setOnConfirmacionListener(new DialogoConfirmacion.OnConfirmacionListener() {
            @Override
            public void onConfirmar() {
                eliminarEncuesta(encuesta);
            }

            @Override
            public void onCancelar() {
                // No hacer nada, el diálogo se cierra automáticamente
            }
        });

        dialog.show(getParentFragmentManager(), "DialogoConfirmacionEliminar");
    }
    private void generarDialogoCompartiEncuesta(Encuesta encuesta){

        String url = singleton.getUrl() + "/"+encuesta.getId();
        new DialogCompartirEncuesta(url).show(getParentFragmentManager(), "qr_dialog");

    }
    private void mostrarConfirmacionFinalizar(Encuesta encuesta) {

        String titulo = getString(R.string.finish_survey);
        String mensaje = getString(R.string.finish_survey_advice);
        mensaje = " " + encuesta.getNombre() + getString(R.string.action_cannot_undo);

        DialogoConfirmacion dialog = DialogoConfirmacion.newInstance(titulo, mensaje, "Finalizar", "Cancelar");
        dialog.setOnConfirmacionListener(new DialogoConfirmacion.OnConfirmacionListener() {
            @Override
            public void onConfirmar() {
                finalizarEncuesta(encuesta);
            }

            @Override
            public void onCancelar() {
                // No hacer nada
            }
        });

        dialog.show(getParentFragmentManager(), "DialogoConfirmacionFinalizar");
    }

    private void finalizarEncuesta(Encuesta encuesta) {
        singleton.getFireBaseManager().desactivarEncuesta(encuesta.getId(), new FireBaseManager.OnEncuestaDesactivada() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), getString(R.string.finish_survey_success), Toast.LENGTH_SHORT).show();

                // AGREGAR: Actualizar la encuesta en la lista local
                encuesta.setEstaActiva(false);
                encuestaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), getString(R.string.finish_survey_failed) + ": " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }






}