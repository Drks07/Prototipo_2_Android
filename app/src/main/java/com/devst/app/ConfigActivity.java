package com.devst.app;

import androidx.activity.OnBackPressedCallback;
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

        //Referencias
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

        //Acción del botón "Atrás" en la Toolbar
        toolbar.setNavigationOnClickListener(v -> finishWithAnimation());

        //Evento implícito para abrir Configuración "Wi-Fi"
        btnConfiguracionWifi.setOnClickListener(v -> {
            Intent wifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(wifiSettings);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        //Capturar el botón físico "Atrás"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishWithAnimation();
            }
        });
    }

    //Método con animación
    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
