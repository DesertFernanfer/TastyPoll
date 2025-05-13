package com.fernando.tastypoll.clases;

import java.util.ArrayList;
import java.util.HashMap;

import Enums.TipoDieta;

public class Encuesta {
    private final String nombre, descripcion;
    private boolean esAnonima, modoSeguro, alimentosPredeterminados;
    private int tiempoVida;
    private TipoDieta tipoDieta;
    private ArrayList<Alimento> alimentos;
    private ArrayList<String> usuarios;
    private HashMap<String, Integer> votos;

    public Encuesta(String nombre, String descripcion, boolean esAnonima, int tiempoVida, TipoDieta tipoDieta, Boolean
                    modoSeguro, boolean alimentosPredeterminados, ArrayList<Alimento> alimentos,
                    ArrayList<String> usuarios, HashMap<String, Integer> votos){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.esAnonima = esAnonima;
        this.tiempoVida = tiempoVida;
        this.tipoDieta = tipoDieta;
        this.modoSeguro = modoSeguro;
        this.alimentosPredeterminados = alimentosPredeterminados;
        this.alimentos = alimentos;
        this.usuarios = usuarios;
        this.votos = votos;

    }
    public Encuesta(ArrayList<Alimento> alimentos){
        this.alimentos = alimentos;
        this.nombre = "nombre";
        this.descripcion = "descripcion";
        this.esAnonima = false;
        this.tiempoVida = 10;
        this.tipoDieta = TipoDieta.OMNIVORA;
        this.modoSeguro = true;
        this.alimentosPredeterminados = true;

    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public boolean esEsAnonima() {
        return esAnonima;
    }
    public int getTiempoVida() {
        return tiempoVida;
    }
    public TipoDieta getTipoDieta() {
        return tipoDieta;
    }
    public boolean isModoSeguro() {
        return modoSeguro;
    }
    public boolean esAlimentosPredeterminados() {
        return alimentosPredeterminados;
    }
    public ArrayList<Alimento> getAlimentos() {
        return alimentos;
    }
}
