package com.fernando.tastypoll.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import Enums.CategoriaPlato;
import Enums.TipoDieta;

public class Alimento  implements Parcelable {

    private String id;
    private String nombre;
    private String urlImagen;

    private CategoriaPlato categoria;
    private TipoDieta tipoDieta;

    public Alimento(String id,String nombre, String urlImagen, CategoriaPlato categoria, TipoDieta tipoDieta) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.tipoDieta = tipoDieta;
        this.urlImagen = urlImagen;
    }
    public Alimento(Parcel in){
        id = in.readString();
        nombre = in.readString();
        urlImagen = in.readString();
        categoria = CategoriaPlato.values()[in.readInt()];
        tipoDieta = TipoDieta.values()[in.readInt()];
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaPlato getCategoria() {
        return categoria;
    }

    public TipoDieta getTipoDieta() {
        return tipoDieta;
    }

    public boolean setSeleccionado(boolean value) {
        return value;

    }
    public String getUrlImagen() {
        return urlImagen;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nombre);
        dest.writeString(urlImagen);
        dest.writeInt(categoria.ordinal());
        dest.writeInt(tipoDieta.ordinal());
    }

    public static final Creator<Alimento> CREATOR = new Creator<Alimento>() {
        @Override
        public Alimento createFromParcel(Parcel in) {
            return new Alimento(in);
        }

        @Override
        public Alimento[] newArray(int size) {
            return new Alimento[size];
        }
    };
}
