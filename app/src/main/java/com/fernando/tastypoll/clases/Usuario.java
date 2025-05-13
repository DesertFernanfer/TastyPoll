package com.fernando.tastypoll.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Enums.TipoDieta;

public class Usuario  implements Serializable {

    private  String nombre, email;
    private TipoDieta tipoDieta;
    private ArrayList<Encuesta> encuestas;

    public Usuario(String nombre, String email, TipoDieta tipoDieta, ArrayList<Encuesta> encuestas) {
        this.nombre = nombre;
        this.email = email;
        this.tipoDieta = tipoDieta;
        this.encuestas = encuestas;
    }
    public String getNombre(){
        return nombre;
    }
    public String getEmail(){
        return email;
    }
    public TipoDieta getTipoDieta(){
        return tipoDieta;
    }
    public ArrayList<Encuesta> getEncuestas(){
        return encuestas;
    }

}
