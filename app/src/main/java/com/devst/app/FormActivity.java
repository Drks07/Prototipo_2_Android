package com.devst.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    private EditText etData;
    private Button btnSend;

    // Launcher para abrir ConfirmActivity y recibir resultado
    private ActivityResultLauncher<Intent> confirmLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                if (data != null) {
                                    String confirmMessage = data.getStringExtra("confirm_result");
                                    Toast.makeText(FormActivity.this,
                                            "Respuesta, " + confirmMessage,
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(FormActivity.this,
                                        "Respuesta, Cancelado",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etData = findViewById(R.id.etData);
        Button btnEnviarFormulario = findViewById(R.id.btnEnviarFormulario);

        btnEnviarFormulario.setOnClickListener(v -> {
            String text = etData.getText().toString().trim();

            // 🔹 Validación: campo obligatorio
            if (TextUtils.isEmpty(text)) {
                etData.setError("Este campo no puede estar vacío");
                etData.requestFocus();
                return; // no continúa si está vacío
            }

            // Si pasa la validación, lanza ConfirmActivity
            Intent intent = new Intent(FormActivity.this, ConfirmActivity.class);
            intent.putExtra("form_data", text);
            confirmLauncher.launch(intent);
        });

        // Botón para volver al menú principal
        Button btnFormularioVolver = findViewById(R.id.btnFormularioVolver);
        btnFormularioVolver.setOnClickListener(v -> {
            finish();
        });
    }
}

