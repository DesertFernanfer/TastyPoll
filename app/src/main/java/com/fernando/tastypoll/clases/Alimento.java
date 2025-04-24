package com.fernando.tastypoll.clases;

import Enums.CategoriaPlato;
import Enums.TipoDieta;

public class Alimento {

    private String id;
    private String nombre;
    private CategoriaPlato categoria;
    private TipoDieta tipoDieta;

    public Alimento(String id,String nombre, CategoriaPlato categoria, TipoDieta tipoDieta) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.tipoDieta = tipoDieta;
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
}
