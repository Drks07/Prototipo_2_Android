package com.devst.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    // Encapsulamiento
    private TextView tvCorreoPerfil, tvContrasenaPerfil, tvNombrePerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        // Recibir datos
        String emailRecibido = getIntent().getStringExtra("email_usuario");
        String contrasenaRecibido = getIntent().getStringExtra("contrasena_usuario");
        String nombreRecibido = getIntent().getStringExtra("nombre_usuario");
        if (emailRecibido == null) emailRecibido = "";

        // Referencias
        tvCorreoPerfil = findViewById(R.id.tvCorreoPerfil);
        tvContrasenaPerfil = findViewById(R.id.tvContrasenaPerfil);
        tvNombrePerfil = findViewById(R.id.tvNombrePefil);

        // Mostrar datos
        tvCorreoPerfil.setText(emailRecibido);
        tvContrasenaPerfil.setText(contrasenaRecibido);
        tvNombrePerfil.setText(nombreRecibido);

        // Botón para volver al menú principal
        Button btnPerfilVolver = findViewById(R.id.btnPerfilVolver);
        btnPerfilVolver.setOnClickListener(v -> {
            finishWithAnimation();
        });

        //Manejar el botón físico "Atrás"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishWithAnimation();
            }
        });
    }

    //Método centralizado para aplicar animación al cerrar
    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
