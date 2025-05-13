package com.fernando.tastypoll.clases;

import java.util.ArrayList;
import java.util.List;

import Enums.TipoDieta;

public class Usuario {

    private  String nombre, email;
    private TipoDieta tipoDieta;
    private ArrayList<Encuesta> encuestas;

    public Usuario(String nombre, String email, TipoDieta tipoDieta, ArrayList<Encuesta> encuestas) {
        this.nombre = nombre;
        this.email = email;
        this.tipoDieta = tipoDieta;
        this.encuestas = encuestas;
    }
}
