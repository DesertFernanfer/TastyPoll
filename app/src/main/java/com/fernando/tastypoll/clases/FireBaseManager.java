package com.fernando.tastypoll.clases;


import android.util.Log;

import com.fernando.tastypoll.interfaces.IUsuarioLlamada;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enums.TipoDieta;

public class FireBaseManager  {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    public FireBaseManager() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void guardarUsuario(String nombre, String email, String password, TipoDieta tipoDieta, String uid) {
        Map<String,Object> usuario = new HashMap<>();

        usuario.put("nombre",nombre);
        usuario.put("email",email);
        usuario.put("tipoDieta",tipoDieta.toString());
        usuario.put("encuestas",new ArrayList<Encuesta>());
        firestore.collection("usuarios").document(uid).set(usuario).addOnSuccessListener(
                aVoid -> {
                    Log.d("Firebase", "Usuario guardado correctamente");

                }
        ).addOnSuccessListener( onFailure -> {
            Log.d("Firebase", "Fallo al guardar al usuario");

        });

    }
    public void cargarUsuario(String uid, IUsuarioLlamada llamada){

        firestore.collection("usuarios")
                .document(uid)
                .get().addOnSuccessListener( documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();



                        String nombre = data.containsKey("nombre") ? data.get("nombre").toString() : "";
                        String email = data.containsKey("email") ? data.get("email").toString() : "";
                        TipoDieta tipoDieta = data.containsKey("tipoDieta")
                                ? TipoDieta.valueOf(data.get("tipoDieta").toString())
                                : TipoDieta.OMNIVORA; // Valor por defecto

                        List<Encuesta> encuestas = data.containsKey("encuestas")
                                ? (List<Encuesta>) data.get("encuestas")
                                : Collections.emptyList();

                        Usuario usuario = new Usuario(nombre, email, tipoDieta, encuestas);

                        llamada.onUsuarioCargado(usuario);
                    } else {
                        llamada.onUsuarioNoExiste();
                    }

                }).addOnFailureListener(e -> {
                    llamada.onError("Ha ha ocurrido un error al cargar el usuario");
                });


    }




}
