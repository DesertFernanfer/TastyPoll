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

import java.util.List;

public class BuscadorAlimentoAdapter extends RecyclerView.Adapter<BuscadorAlimentoAdapter.ViewHolder> {

    private List<Alimento> alimentos;
    private Context context;


    public BuscadorAlimentoAdapter(List<Alimento> alimentos, Context context) {
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
        int colorFondo = alimento.esSeleccionado()
                ? ContextCompat.getColor(context, R.color.item_selected)
                : Color.TRANSPARENT;
        holder.itemView.setBackgroundColor(colorFondo);

        // Click para seleccionar/deseleccionar
        holder.itemView.setOnClickListener(v -> {
            alimento.setSeleccionado(!alimento.esSeleccionado());
            notifyItemChanged(position); // Actualiza solo este item
        });
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
