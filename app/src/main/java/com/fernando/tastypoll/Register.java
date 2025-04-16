package com.fernando.tastypoll;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText entryEmail, entryPassword, entryPasswordConfirm;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ///  Inicializar ///
        mAuth = FirebaseAuth.getInstance();
        referenciarElementos();
        inicializarBotones();
    }
    private void referenciarElementos(){
        entryEmail = findViewById(R.id.inputEmail);
        entryPassword = findViewById(R.id.inputPassword);
        entryPasswordConfirm = findViewById(R.id.inputPasswordConfirm);
    }
    private void inicializarBotones(){
        ///// Atras ////
        findViewById(R.id.botonAtras).setOnClickListener(v -> finish());
        ///// Registrar ////
        findViewById(R.id.botonRegistrar).setOnClickListener(v -> {
            String email = entryEmail.getText().toString().trim();
            String password = entryPassword.getText().toString();
            String passwordConfirm = entryPasswordConfirm.getText().toString();

            Toast.makeText(Register.this, "Boton register pulsado", Toast.LENGTH_SHORT).show();


            if(validarCampos(email,password,passwordConfirm)){
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,  task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private boolean validarCampos(String email, String password, String passwordConfirm)  {
        boolean esCorrecto = true;
        if(!password.equals(passwordConfirm)){
            esCorrecto = false;
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            esCorrecto = false;
            Toast.makeText(this, "Email incorrecto", Toast.LENGTH_SHORT).show();

        } else if(password.length() < 6){
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
        }
        return esCorrecto;
    }

}