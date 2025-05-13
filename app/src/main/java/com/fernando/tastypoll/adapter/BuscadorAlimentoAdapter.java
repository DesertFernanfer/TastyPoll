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

public class BuscadorAlimentoAdapter extends RecyclerView.Adapter<BuscadorAlimentoAdapter.ViewHolder> {

    private ArrayList<Alimento> alimentos;
    private ArrayList<Alimento> alimentosSeleccionados = new ArrayList<>();
    private Context context;


    public BuscadorAlimentoAdapter(ArrayList<Alimento> alimentos, Context context) {
        this.alimentos = alimentos;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alimento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuscadorAlimentoAdapter.ViewHolder holder, int position) {
        Alimento alimento = alimentos.get(position);

        // Mostrar datos
        holder.textNombre.setText(alimento.getNombre());
        Glide.with(context)
                .load(alimento.getUrlImagen())
                .placeholder(R.drawable.placeholder_imagen)
                .into(holder.imagenView);

        // Cambiar color de fondo si está seleccionado
        gestionarSeleccion(holder,alimento,position);

    }
    private void gestionarSeleccion(BuscadorAlimentoAdapter.ViewHolder holder, Alimento alimento, int position) {

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
    }
    public void actualizarLista(ArrayList<Alimento> nuevaLista) {
        alimentos = nuevaLista;
        notifyDataSetChanged();
    }

    public ArrayList<Alimento> getAlimentosSeleccionados() {
        return alimentosSeleccionados;
    }
    @Override
    public int getItemCount() {
        return alimentos.size();

    }

    // ViewHolder para el buscador
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenView;
        TextView textNombre;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenView = itemView.findViewById(R.id.imagenAlimento);
            textNombre = itemView.findViewById(R.id.nombreAlimento);
        }
    }
}
