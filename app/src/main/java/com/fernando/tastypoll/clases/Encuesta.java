package com.fernando.tastypoll.clases;

import java.util.ArrayList;
import java.util.HashMap;

import Enums.TipoDieta;

public class Encuesta {
    private final String nombre, descripcion;
    private boolean esAnonima, modoSeguro, alimentosPredterminados;
    private int tiempoVida;
    private TipoDieta tipoDieta;
    private ArrayList<Alimento> alimentos;
    private ArrayList<String> usuarios;
    private HashMap<String, Integer> votos;

    public Encuesta(String nombre, String descripcion, boolean esAnonima, int tiempoVida, TipoDieta tipoDieta, Boolean
                    modoSeguro, boolean alimentosPredterminados, ArrayList<Alimento> alimentos,
                    ArrayList<String> usuarios, HashMap<String, Integer> votos){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.esAnonima = esAnonima;
        this.tiempoVida = tiempoVida;
        this.tipoDieta = tipoDieta;
        this.modoSeguro = modoSeguro;
        this.alimentosPredterminados = alimentosPredterminados;
        this.alimentos = alimentos;
        this.usuarios = usuarios;
        this.votos = votos;

    }
}
