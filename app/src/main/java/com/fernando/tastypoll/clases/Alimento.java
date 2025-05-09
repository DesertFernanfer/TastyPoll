package com.fernando.tastypoll.clases;

import Enums.CategoriaPlato;
import Enums.TipoDieta;

public class Alimento {

    private String id;
    private String nombre;
    private String urlImagen;
    private boolean esSeleccionado;

    private CategoriaPlato categoria;
    private TipoDieta tipoDieta;

    public Alimento(String id,String nombre, String urlImagen, CategoriaPlato categoria, TipoDieta tipoDieta) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.tipoDieta = tipoDieta;
        this.urlImagen = urlImagen;
        this.esSeleccionado = false;
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
    public boolean esSeleccionado() {
        return esSeleccionado;

    }
    public boolean setSeleccionado(boolean value) {
        return value;

    }
    public String getUrlImagen() {
        return urlImagen;

    }
}
