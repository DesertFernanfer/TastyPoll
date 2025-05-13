package com.fernando.tastypoll.clases;


import android.util.Log;

import com.fernando.tastypoll.interfaces.IUsuarioLlamada;
import com.fernando.tastypoll.interfaces.OnAlimentosCargados;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Enums.CategoriaPlato;
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
        usuario.put("encuestas",new ArrayList<String>());
        firestore.collection("usuarios").document(uid).set(usuario).addOnSuccessListener(
                aVoid -> {
                    Log.d("Firebase", "Usuario guardado correctamente");

                }
        ).addOnSuccessListener( onFailure -> {
            Log.d("Firebase", "Fallo al guardar al usuario");

        });

    }
    public void cargarUsuario(IUsuarioLlamada llamada){

        String uid = mAuth.getUid();
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

                        ArrayList<Encuesta> encuestas = data.containsKey("encuestas")
                                ? (ArrayList<Encuesta>) data.get("encuestas")
                                : new ArrayList<>();

                        Usuario usuario = new Usuario(nombre, email, tipoDieta, encuestas);

                        llamada.onUsuarioCargado(usuario);
                        Log.d("FireBaseManager", "Usuario cargado correctamente");
                    } else {
                        llamada.onUsuarioNoExiste();
                        Log.d("FireBaseManager", "Usuario no existe");
                    }

                }).addOnFailureListener(e -> {
                    llamada.onError("Ha ha ocurrido un error al cargar el usuario");
                    Log.e("FireBaseManager", "Error al cargar el usuario", e);
                });


    }

    public void getListaAlimentos(OnAlimentosCargados call){
        ArrayList<Alimento> listaAlimentos = new ArrayList<>();

        firestore.collection("alimentos").get().addOnSuccessListener(querySnapshot -> {

            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                String id = doc.getId();
                String nombre = doc.getString("nombre");
                String url = doc.getString("urlImagen");

                CategoriaPlato categoria = extraerCategoria(doc.getString("tipo"));
                TipoDieta tipoDieta = extraerDieta(doc.getString("dieta"));

                Alimento a = new Alimento(id, nombre, url, categoria, tipoDieta);
                listaAlimentos.add(a);
            }
        }).addOnFailureListener(e -> {
            Log.e("FireBaseManager", "Error al cargar los alimentos");
                }
        );
        call.onAlimentosCargados(listaAlimentos);
    }
    private TipoDieta extraerDieta(String dieta){
        TipoDieta tipoDieta = TipoDieta.OMNIVORA;

        if (dieta != null && !dieta.isEmpty()) {
            try {
                tipoDieta = TipoDieta.valueOf(dieta.toUpperCase(Locale.ROOT).trim());
            } catch (IllegalArgumentException e) {
                Log.w("FireBaseManager", "Tipo de dieta no válido: " + dieta);
            }
        }
        return tipoDieta;
    }
    private CategoriaPlato extraerCategoria(String categoria){
        CategoriaPlato categoriaPlato = CategoriaPlato.TODOS;

        if (categoria != null && !categoria.isEmpty()) {
            try {
                categoriaPlato = CategoriaPlato.valueOf(categoria.toUpperCase(Locale.ROOT).trim());
            } catch (IllegalArgumentException e) {
                Log.w("FireBaseManager", "Tipo de dieta no válido: " + categoria);
            }
        }
        return categoriaPlato;
    }
    public void crearEncuesta(Encuesta encuesta){
        String uid = mAuth.getUid();

        Map<String,Object> documento = new HashMap<>();

        documento.put("nombre",encuesta.getNombre());
        documento.put("creadorUid",uid);
        documento.put("descripcion",encuesta.getDescripcion());
        documento.put("publica",encuesta.esEsAnonima());
        documento.put("tiempoVida",encuesta.getTiempoVida());
        documento.put("tipoDieta",encuesta.getTipoDieta().toString());
        documento.put("modoSeguro",encuesta.isModoSeguro());
        documento.put("alimentosPredeterminados",encuesta.esAlimentosPredeterminados());


        // Usuario UID - ID comida
        documento.put("votos",new HashMap<String,String>());

        firestore.collection("encuestas")
                .add(documento)
                .addOnSuccessListener(documentReference -> {

                    String id = documentReference.getId();
                    Log.d("Firestore", "Encuesta creada con ID: " + id);

                    agregarEncuestaSUsuario(id,uid);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al crear encuesta", e);
                });

    }
    private void agregarEncuestaSUsuario(String idEncuesta, String uid){

        firestore.collection("usuarios").document(uid).update("encuestas", FieldValue.arrayUnion(idEncuesta));
    }
    public void actualizarNombreUsuario(String nombre){
        String uid = mAuth.getUid();
        firestore.collection("usuarios")
                .document(uid)
                .update("nombre",nombre)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Nombre actualizado correctamente");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error al actualizar el nombre", e);
                });

    }
    public void actualizarTipoDieta(TipoDieta tipoDieta){
        String uid = mAuth.getUid();
        firestore.collection("usuarios")
                .document(uid)
                .update("tipoDieta",tipoDieta.toString())
                .addOnSuccessListener(aVoid ->{
                    Log.d("Firebase", "Tipo de dieta actualizado correctamente " + tipoDieta.toString());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error al actualizar el tipo de dieta", e);
                        }
                );
    }

}
