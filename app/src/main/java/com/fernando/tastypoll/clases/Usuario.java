package com.fernando.tastypoll.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Enums.TipoDieta;

public class Usuario  implements Serializable {

    private  String nombre, email;
    private TipoDieta tipoDieta;
    private List<String> alimentos;
    private ArrayList<Encuesta> encuestas;

    public Usuario(String nombre, String email, TipoDieta tipoDieta,ArrayList<Encuesta> encuestas) {
        this.nombre = nombre;
        this.email = email;
        this.tipoDieta = tipoDieta;
        this.encuestas = Objects.requireNonNullElseGet(encuestas, ArrayList::new);
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
    public Map<String, Object> toHashMap() {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nombre",nombre);
        usuario.put("email",email);
        usuario.put("tipoDieta",tipoDieta.toString());
        usuario.put("encuestas",new ArrayList<String>());
        return usuario;
    }
    public void eliminarEncuesta(Encuesta encuesta){
        encuestas.remove(encuesta);
    }

}
