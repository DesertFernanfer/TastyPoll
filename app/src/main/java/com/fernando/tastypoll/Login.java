package com.fernando.tastypoll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText entryEmail, entryPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /////// Inicializar /////////
        mAuth = FirebaseAuth.getInstance();
        inicializarBotones();
        referenciarElementos();
    }
    private void inicializarBotones(){
        findViewById(R.id.botonLogin).setOnClickListener(v -> {
            String email = entryEmail.getText().toString().trim();
            String password = entryPassword.getText().toString();
            if(validarCampos(email,password)) {
                iniciarSesion(entryEmail.getText().toString(), entryPassword.getText().toString());
            }
        });
        findViewById(R.id.botonResgister).setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
        findViewById(R.id.botonRecoverPassword).setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RecoverPassword.class);
            startActivity(intent);
        });
    }


private void referenciarElementos(){
    entryEmail = findViewById(R.id.editTextTextEmailAddress);
    entryPassword = findViewById(R.id.editTextTextPassword);
}
private void iniciarSesion(String email, String password){
    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Toast.makeText(Login.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Login.this, "Email/conntraseña no válidos", Toast.LENGTH_SHORT).show();
            }
        }
    });

}

public  boolean validarCampos(String email, String password) {

    boolean correcto = true;
    if(email.isEmpty() || password.isEmpty()){
        correcto = false;
        Toast.makeText(this, "El email y contraseña son obligatorios", Toast.LENGTH_SHORT).show();

    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        correcto = false;
        Toast.makeText(this, "Email incorrecto", Toast.LENGTH_SHORT).show();

    }
    return correcto;
}
}
