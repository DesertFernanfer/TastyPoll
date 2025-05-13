package com.fernando.tastypoll.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fernando.tastypoll.R;
import com.fernando.tastypoll.clases.Alimento;

import java.util.ArrayList;
import java.util.Objects;

public class CrearEncuestaAdapter extends RecyclerView.Adapter<CrearEncuestaAdapter.ViewHolder> {

    private final ArrayList<Alimento> alimentos;
    private final ArrayList<Alimento> alimentosSeleccionados;
    private final Context context;
    //Cuando se crea un nuevo ViewHolder, infla el layaout
    public CrearEncuestaAdapter(ArrayList<Alimento> alimentos, Context context){

        this.alimentos = Objects.requireNonNullElseGet(alimentos, ArrayList::new);
        this.context = context;
        this.alimentosSeleccionados = new ArrayList<>();
    }
    @NonNull
    @Override
    public CrearEncuestaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alimento, parent, false);
        return new ViewHolder(view);
    }
    //Asigna los datos al viewHolder

    @Override
    public void onBindViewHolder(@NonNull CrearEncuestaAdapter.ViewHolder holder, int position) {

        /*
        Alimento alimento = alimentos.get(position);

        // Mostrar datos
        holder.textNombre.setText(alimento.getNombre());
        if (context != null) {
            Glide.with(context)
                    .load(alimento.getUrlImagen())
                    .placeholder(R.drawable.placeholder_imagen)
                    .into(holder.imagenView);
        }
        gestionarSeleccion(holder,alimento,position);
         */

        Alimento alimento = alimentos.get(position);

        // Resetear el estado visual ANTES de asignar datos
        holder.rootView.setBackgroundColor(Color.TRANSPARENT);

        // Asignar datos normales
        holder.textNombre.setText(alimento.getNombre());
        Glide.with(context).load(alimento.getUrlImagen())
                .load(alimento.getUrlImagen())
                .placeholder(R.drawable.placeholder_imagen)
                .into(holder.imagenView);

        // Forzar estado de selección
        if (alimentosSeleccionados.contains(alimento)) {
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.item_selected));
        } else {
            holder.rootView.setBackgroundColor(Color.TRANSPARENT);
        }

        // Click listener mejorado
        holder.rootView.setOnClickListener(v -> {
            toggleSeleccion(alimento, position);
        });

    }
    private void toggleSeleccion(Alimento alimento, int position) {
        if (alimentosSeleccionados.contains(alimento)) {
            alimentosSeleccionados.remove(alimento);
        } else {
            alimentosSeleccionados.add(alimento);
        }
        notifyItemChanged(position);
    }
    public void borrarAlimentos(){
        /*
        int posicion;
        for(Alimento alimento : alimentosSeleccionados){
            posicion = alimentosSeleccionados.indexOf(alimento);
            if(posicion != -1 && posicion < alimentos.size()){
                alimentos.remove(posicion);
                notifyItemRemoved(posicion);
            } else if(alimentos.size() == 1){
                alimentos.remove(0);
                notifyItemRemoved(posicion);
            }

        }*/
        for (int i = alimentosSeleccionados.size() - 1; i >= 0; i--) {
            Alimento alimento = alimentosSeleccionados.get(i);
            int posicionEnListaPrincipal = alimentos.indexOf(alimento);

            if (posicionEnListaPrincipal != -1) {
                alimentos.remove(posicionEnListaPrincipal);
                notifyItemRemoved(posicionEnListaPrincipal);
            }
        }
        alimentosSeleccionados.clear();
    }

    private void gestionarSeleccion(ViewHolder holder, Alimento alimento, int position) {

        /*
        holder.itemView.setBackgroundColor(
                alimentosSeleccionados.contains(alimento)
                        ? ContextCompat.getColor(context, R.color.item_selected)
                        : Color.TRANSPARENT
        );


        holder.itemView.setOnClickListener(v -> {
            boolean estabaSeleccionado = alimentosSeleccionados.contains(alimento);

            if (estabaSeleccionado) {
                alimentosSeleccionados.remove(alimento);
            } else {
                alimentosSeleccionados.add(alimento);

            }

            // Actualizar solo este item
            notifyItemChanged(position);
        });

         */
        // Actualiza el color basado en la selección ACTUAL
        holder.itemView.setBackgroundColor(
                alimentosSeleccionados.contains(alimento)
                        ? ContextCompat.getColor(context, R.color.item_selected)
                        : Color.TRANSPARENT
        );

        holder.itemView.setOnClickListener(v -> {
            if (alimentosSeleccionados.contains(alimento)) {
                alimentosSeleccionados.remove(alimento);
            } else {
                alimentosSeleccionados.add(alimento);
            }
            notifyItemChanged(position); // Actualiza solo este item
        });
    }



    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    //Almacena las vistas de un item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenView;
        TextView textNombre;
        View rootView;  // Añade esta referencia



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;  // Guarda la vista principal
            imagenView = itemView.findViewById(R.id.imagenAlimento);
            textNombre = itemView.findViewById(R.id.nombreAlimento);
        }
    }

}
