package com.devst.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.widget.Button;

import androidx.core.content.ContextCompat;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbarConfig);
        setSupportActionBar(toolbar);

        //Refrencias
        Button btnConfiguracionWifi = findViewById(R.id.btnConfiguracionWifi);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Atras");
        }

        //Cambiar color del texto del título
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        //Cambiar color de la flecha "Atrás"
        final Drawable upArrow = ContextCompat.getDrawable(this, androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        //Acción del botón atrás
        toolbar.setNavigationOnClickListener(v -> {
            // Cierra la Activity manualmente
            finish();
            //Trancision personalizada
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        //Evento implicito para abrir Configuracion "Wifi"
        btnConfiguracionWifi.setOnClickListener(v -> {
            Intent wifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(wifiSettings);
            //Animación de entrada/salida
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    //Agrega la animación al volver atrás
    @Override
    public void onBackPressed() {
        // Cierra la Activity
        finish();
        //Trancision personalizada
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
