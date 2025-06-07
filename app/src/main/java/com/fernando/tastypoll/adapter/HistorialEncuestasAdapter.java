package com.fernando.tastypoll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.clases.Encuesta;

import java.util.ArrayList;

public class HistorialEncuestasAdapter extends RecyclerView.Adapter<HistorialEncuestasAdapter.EncuestaViewHolder> {
    private ArrayList<Encuesta> encuestas;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Encuesta encuesta);
        void onMenuClick(Encuesta encuesta, View menuView);
    }

    public HistorialEncuestasAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.encuestas = new ArrayList<>();
        this.listener = listener;
    }

    public void setEncuestas(ArrayList<Encuesta> newEncuestas) {
        this.encuestas.clear();
        this.encuestas.addAll(newEncuestas);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EncuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encuesta, parent, false);
        return new EncuestaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EncuestaViewHolder holder, int position) {
        Encuesta encuesta = encuestas.get(position);
        holder.bind(encuesta, listener);

        if (encuesta.getEstaActiva()) {
            holder.cardEncuesta.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightGreen));
        } else {
            holder.cardEncuesta.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightRed));
        }
    }

    @Override
    public int getItemCount() {
        return encuestas.size();
    }

    public static class EncuestaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreEncuesta;
        ImageView imageViewMenu;
        CardView cardEncuesta;

        public EncuestaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreEncuesta = itemView.findViewById(R.id.textViewNombreEncuesta);
            imageViewMenu = itemView.findViewById(R.id.imageViewMenu);
            cardEncuesta = (CardView) itemView;
        }

        public void bind(final Encuesta encuesta, final OnItemClickListener listener) {
            textViewNombreEncuesta.setText(encuesta.getNombre());

            imageViewMenu.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMenuClick(encuesta, v);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(encuesta);
                }
            });
        }
    }
}
