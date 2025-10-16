package com.devst.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmActivity extends AppCompatActivity {

    private TextView tvReceived;
    private Button btnConfirm;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        tvReceived = findViewById(R.id.tvReceived);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        String receivedData = getIntent().getStringExtra("form_data");
        tvReceived.setText(receivedData);

        //Botón CONFIRMAR
        btnConfirm.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("confirm_result", "Confirmado: " + receivedData);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        //Botón CANCELAR
        btnCancel.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    //Transición botón físico "Atrás"
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}