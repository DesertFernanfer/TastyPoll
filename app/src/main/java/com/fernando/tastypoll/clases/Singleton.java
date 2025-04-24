package com.fernando.tastypoll.clases;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Singleton {
    private static Singleton instancia = new Singleton();

    private FirebaseAuth mAuth;
    private FireBaseManager fireBaseManager;
    private Singleton(){
        mAuth = FirebaseAuth.getInstance();
        fireBaseManager = new FireBaseManager();

    }
    public static Singleton getInstancia(){

        return instancia;
    }
    public FirebaseAuth getmAuth(){
        return mAuth;
    }
    public FireBaseManager getFireBaseManager(){
        return fireBaseManager;
    }
}
