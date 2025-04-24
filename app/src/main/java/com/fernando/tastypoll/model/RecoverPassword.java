package com.fernando.tastypoll.model;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fernando.tastypoll.R;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText entryEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recover_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /// Inicializar ///
        mAuth = FirebaseAuth.getInstance();
        entryEmail = findViewById(R.id.entryEmail);
        incializarBototnes();
    }

    private void incializarBototnes(){
        findViewById(R.id.botonAtras).setOnClickListener(v -> finish());
        findViewById(R.id.botonEnvio).setOnClickListener(v -> {
            String email = entryEmail.getText().toString().trim();
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Se enviara un email de recuperación si existe el email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RecoverPassword.this, "Error al enviar el email", Toast.LENGTH_SHORT).show();
                    }

                });
            } else{
                Toast.makeText(RecoverPassword.this, "Email no válido", Toast.LENGTH_SHORT).show();
            }
        });
    }

}