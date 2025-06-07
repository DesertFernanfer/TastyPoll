package com.fernando.tastypoll.clases;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Enums.TipoDieta;

public class Encuesta implements Serializable {
    private  String id;
    private String nombre, descripcion;
    private boolean tieneEmail, estaActiva, tieneAlimentosPredeterminados;
    private Date fechaCreacion, fechaVencimiento;
    private TipoDieta tipoDieta;
    private ArrayList<Alimento> alimentos;
    private ArrayList<String> emails;
    private HashMap<String, Integer> votos;


    public Encuesta(
            String nombre,
            String descripcion,
            boolean tieneEmail,
            int tiempoVida, boolean estaActiva,
            TipoDieta tipoDieta,
            boolean tienealimentosPredeterminados,
            ArrayList<Alimento> alimentos,
            ArrayList<String> emails, HashMap<String, Integer> votos){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tieneEmail = tieneEmail;
        this.estaActiva = estaActiva;
        this.tipoDieta = tipoDieta;
        this.tieneAlimentosPredeterminados = tienealimentosPredeterminados;
        this.alimentos = Objects.requireNonNullElseGet(alimentos, ArrayList::new);


        this.emails = Objects.requireNonNullElseGet(emails, ArrayList::new);
        this.votos = Objects.requireNonNullElseGet(votos, HashMap::new);
        this.id = "generic";
        instanciarFechas(tiempoVida);

    }

    public Encuesta(
            String id,
            String nombre,
            String descripcion,
            boolean tieneEmail,
            int tiempoVida, boolean estaActiva,
            TipoDieta tipoDieta,
            boolean tienealimentosPredeterminados,
            ArrayList<Alimento> alimentos,
            ArrayList<String> emails, HashMap<String, Integer> votos){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tieneEmail = tieneEmail;
        this.estaActiva = estaActiva;
        this.tipoDieta = tipoDieta;
        this.tieneAlimentosPredeterminados = tienealimentosPredeterminados;
        this.alimentos = Objects.requireNonNullElseGet(alimentos, ArrayList::new);


        this.emails = Objects.requireNonNullElseGet(emails, ArrayList::new);
        this.votos = Objects.requireNonNullElseGet(votos, HashMap::new);
        this.id = id;
        instanciarFechas(tiempoVida);

    }

    //COnstructor de prueba

    public Encuesta(ArrayList<Alimento> alimentos){
        this.nombre = "nombre";
        this.descripcion = "descripcion";
        this.tieneEmail = true;
        this.estaActiva = true;
        this.emails = new ArrayList<>();
        this.tipoDieta = TipoDieta.OMNIVORA;
        this.tieneAlimentosPredeterminados = false;
        this.alimentos = alimentos;
        this.votos = new HashMap<>();


        instanciarFechas(10);


    }
    public  boolean getEstaActiva(){
        return estaActiva;
    }


    public String getDescripcion(){
        return descripcion;
    }
    private void instanciarFechas(int tiempoVidaEnMinutos){
        this.fechaCreacion = new Date();
        // Cálculo correcto para minutos
        long milisegundos = tiempoVidaEnMinutos * 60L * 1000L;
        this.fechaVencimiento = new Date(fechaCreacion.getTime() + milisegundos);
    }
    public boolean tieneEmail(){
        return tieneEmail;
    }
    public String getNombre() {
        return nombre;
    }
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("descripcion", descripcion);
        map.put("activa", estaActiva);
        map.put("tieneEmail", tieneEmail);
        map.put("emails", emails);
        
        map.put("fechaCreacion",new Timestamp(fechaCreacion));
        map.put("fechaVencimiento", new Timestamp(fechaVencimiento));
        
        map.put("tipoDieta", tipoDieta.toString());
        
        map.put("tienealimentosPredeterminados", tieneAlimentosPredeterminados);
        map.put("alimentos", sacarIdsAlimentosPredeterminados());
     
        map.put("votos", votos);
        return map;
    }

    private ArrayList<String> sacarIdsAlimentosPredeterminados(){
        ArrayList<String> ids = new ArrayList<>();
        for (Alimento a : alimentos) {
            ids.add(a.getId());
        }
        return ids;
    }

    public TipoDieta getTipoDieta() {
        return tipoDieta;
    }
    public boolean getTienealimentosPredeterminados() {
        return tieneAlimentosPredeterminados;
    }
    public ArrayList<Alimento> getAlimentos() {
        return alimentos;
    }
    public ArrayList<String> getEmails() {
        return emails;
    }
    public HashMap<String, Integer> getVotos() {
        return votos;
    }
    public Timestamp getFechaCreacion() {
        return new Timestamp(fechaCreacion);
    }
    public Timestamp getFechaVencimiento() {
        return new Timestamp(fechaVencimiento);

    }
    public String getId(){
        return id;
    }
    public boolean getTieneEmail(){
        return tieneEmail;

    }


    public void setEstaActiva(boolean estaActiva){
        this.estaActiva = estaActiva;
    }

}
