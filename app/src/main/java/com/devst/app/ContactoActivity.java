package com.devst.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactoActivity extends AppCompatActivity {

    private static final int PICK_CONTACT = 1;
    private TextView tvNombreContacto, tvNumeroContacto;

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
            // Intent implícito para abrir contactos
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK && data != null) {
            Uri contactUri = data.getData();
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
    }
}

