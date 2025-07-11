package com.fernando.tastypoll.clases;

import com.fernando.tastypoll.interfaces.IUsuarioLlamada;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Enums.TipoDieta;

public class Singleton {
    private static Singleton instancia = new Singleton();

    private static final String URL   = "https://tastypoll-8436d.web.app/";

    private static final String  URLMANUAL = "https://pdfhost.io/v/7dxNcczcB5_Manual_Aplicaci%C3%83%C2%B3n";
    private Usuario usuario = null;
    private FirebaseAuth mAuth;
    private FireBaseManager fireBaseManager;
    private Singleton(){
        mAuth = FirebaseAuth.getInstance();
        fireBaseManager = new FireBaseManager();

    }
    public void actualizarUsuario(){
        fireBaseManager.cargarUsuario(new IUsuarioLlamada() {
            @Override
            public void onUsuarioCargado(Usuario user) {
                usuario = user;
            }
            @Override
            public void onUsuarioNoExiste() {
                usuario = new Usuario("No existe", "No existe", TipoDieta.OMNIVORA, new ArrayList<>());
            }
            @Override
            public void onError(String mensajeError) {
            }

        });
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public static Singleton getInstancia(){

        return instancia;
    }
    public String getUrl(){
        return URL;
    }
    public void limpiarDatos() {
        this.usuario = null;
    }

    public FirebaseAuth getmAuth(){
        return mAuth;
    }
    public FireBaseManager getFireBaseManager(){
        return fireBaseManager;
    }
    public String getUrlManual(){
        return URLMANUAL;
    }

}
