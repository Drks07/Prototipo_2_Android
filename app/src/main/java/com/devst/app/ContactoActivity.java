package com.devst.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ContactoActivity extends AppCompatActivity {

    private TextView tvNombreContacto, tvNumeroContacto;

    private final ActivityResultLauncher<Intent> pickContactLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri contactUri = result.getData().getData();
                    Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

                    if (cursor != null && cursor.moveToFirst()) {
                        // Obtener nombre y número directamente
                        String nombre = cursor.getString(
                                cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String numero = cursor.getString(
                                cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        // Mostrar en los TextView
                        tvNombreContacto.setText(nombre);
                        tvNumeroContacto.setText(numero);

                        cursor.close();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        // Referencias a los TextView
        tvNombreContacto = findViewById(R.id.tvNombreContacto);
        tvNumeroContacto = findViewById(R.id.tvNumeroContacto);

        // Botón para seleccionar contacto
        Button btnSeleccionar = findViewById(R.id.btnSeleccionarContacto);
        btnSeleccionar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            pickContactLauncher.launch(intent);
        });

        //Funcion para el boton de volver al menu de inicio que esta en la vista de contacto
        Button btnContactoVolver = findViewById(R.id.btnContactoVolver);
        btnContactoVolver.setOnClickListener(v -> {
            finish();
        });
    }
}


