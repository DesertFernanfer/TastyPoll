package com.fernando.tastypoll.clases;

import android.widget.Toast;

import com.fernando.tastypoll.interfaces.IUsuarioLlamada;
import com.fernando.tastypoll.model.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
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

        usuario.put("nombre",nombre);
        usuario.put("email",email);
        usuario.put("tipoDieta",tipoDieta.toString());
        usuario.put("encuestas",new ArrayList<Encuesta>());
        firestore.collection("usuarios").document(uid).set(usuario);

    }
    public void cargarUsuario(String uid, IUsuarioLlamada llamada){

        firestore.collection("usuarios")
                .document(uid)
                .get().addOnSuccessListener( documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();

                        Usuario usuario = new Usuario(data.get("nombre").toString(),data.get("email").toString(),
                                TipoDieta.valueOf(data.get("tipoDieta").toString()),

                                (ArrayList<Encuesta>) data.get("encuestas"));
                        llamada.onUsuarioCargado(usuario);
                    } else {
                        llamada.onUsuarioCargado(null);
                    }

                }).addOnFailureListener(e -> {
                    System.out.println("Error al cargar el usuario");
                });



    }


}
