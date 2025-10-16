package com.devst.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {

    //Variables
    private String emailUsuario, contrasenaUsuario, nombreUsuario = "";
    private TextView tvBienvenida, tvContrasenaHome, tvNombreHome;

    //Variables para la cámara
    private Button btnLinterna;
    private CameraManager camara;
    private String camaraID = null;
    private boolean luz = false;

    //Activity Result (para recibir datos de PerfilActivity)
    private final ActivityResultLauncher<Intent> editarPerfilLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String nombre = result.getData().getStringExtra("nombre_editado");
                    String contra = result.getData().getStringExtra("contraseña_editada");
                    String user = result.getData().getStringExtra("username_editado");
                    if (nombre != null && contra != null && user != null) {
                        tvBienvenida.setText("Hola, " + nombre);
                        tvContrasenaHome.setText(contra);
                        tvNombreHome.setText(user);
                    }
                }
            });

    //Launcher para pedir permiso de cámara
    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    alternarluz();
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Referencias
        tvNombreHome = findViewById(R.id.tvNombreHome);
        tvContrasenaHome = findViewById(R.id.tvContrasenaHome);
        tvBienvenida = findViewById(R.id.tvBienvenida);
        Button btnIrPerfil = findViewById(R.id.btnIrPerfil);
        Button btnAbrirWeb = findViewById(R.id.btnAbrirWeb);
        Button btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);
        Button btnCompartir = findViewById(R.id.btnCompartir);
        btnLinterna = findViewById(R.id.btnLinterna);
        Button btnCamara = findViewById(R.id.btnCamara);
        Button btnUbicacion = findViewById(R.id.btnUbicacion);
        Button btnVerContactos = findViewById(R.id.btnVerContactos);
        Button btnConfiguracionInterna = findViewById(R.id.btnConfiguracionInterna);
        Button btnFormulario = findViewById(R.id.btnFormulario);

        //Recibir datos del Login
        emailUsuario = getIntent().getStringExtra("email_usuario");
        contrasenaUsuario = getIntent().getStringExtra("contrasena_usuario");
        nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        if (emailUsuario == null) emailUsuario = "";
        tvBienvenida.setText("Bienvenido: " + emailUsuario);
        tvContrasenaHome.setText(contrasenaUsuario);
        tvNombreHome.setText(nombreUsuario);

        //Evento explícito → PerfilActivity
        btnIrPerfil.setOnClickListener(v -> {
            Intent perfil = new Intent(HomeActivity.this, PerfilActivity.class);
            perfil.putExtra("email_usuario", emailUsuario);
            perfil.putExtra("contrasena_usuario", contrasenaUsuario);
            perfil.putExtra("nombre_usuario", nombreUsuario);
            editarPerfilLauncher.launch(perfil);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        //Evento explícito → ContactoActivity
        btnVerContactos.setOnClickListener(v -> {
            Intent contacto = new Intent(HomeActivity.this, ContactoActivity.class);
            startActivity(contacto);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        //Evento explícito → FormActivity
        btnFormulario.setOnClickListener(v -> {
            Intent formulario = new Intent(HomeActivity.this, FormActivity.class);
            startActivity(formulario);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        //Evento implícito → abrir web
        btnAbrirWeb.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://git-scm.com/");
            Intent viewWeb = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(viewWeb);
        });

        //Evento implícito → enviar correo
        btnEnviarCorreo.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:"));
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailUsuario});
            email.putExtra(Intent.EXTRA_SUBJECT, "Prueba desde la app");
            email.putExtra(Intent.EXTRA_TEXT, "Hola, esto es un intento de correo.");
            startActivity(Intent.createChooser(email, "Enviar correo con:"));
        });

        //Evento implícito → compartir texto
        btnCompartir.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hola desde mi app Android 😎");
            startActivity(Intent.createChooser(share, "Compartir usando:"));
        });

        //Evento implícito → ver ubicación (Maps)
        btnUbicacion.setOnClickListener(v -> {
            Uri ubicacion = Uri.parse("geo:-33.452922,-70.662307?q=" + Uri.encode("Vergara 165, 8370014 Santiago, Región Metropolitana"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, ubicacion);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        //Evento explícito → ConfigActivity
        btnConfiguracionInterna.setOnClickListener(v -> {
            Intent configuracionInterna = new Intent(HomeActivity.this, ConfigActivity.class);
            startActivity(configuracionInterna);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        //Linterna
        camara = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            for (String id : camara.getCameraIdList()) {
                CameraCharacteristics cc = camara.getCameraCharacteristics(id);
                Boolean disponibleFlash = cc.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer lensFacing = cc.get(CameraCharacteristics.LENS_FACING);
                if (Boolean.TRUE.equals(disponibleFlash)
                        && lensFacing != null
                        && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    camaraID = id;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, "No se puede acceder a la cámara", Toast.LENGTH_SHORT).show();
        }

        btnLinterna.setOnClickListener(v -> {
            if (camaraID == null) {
                Toast.makeText(this, "Este dispositivo no tiene flash disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean camGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;

            if (camGranted) {
                alternarluz();
            } else {
                permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        btnCamara.setOnClickListener(v -> {
            startActivity(new Intent(this, CamaraActivity.class));
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

    //Método que centraliza el cierre con animación
    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Linterna
    private void alternarluz() {
        try {
            luz = !luz;
            camara.setTorchMode(camaraID, luz);
            btnLinterna.setText(luz ? "Apagar Linterna" : "Encender Linterna");
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error al controlar la linterna", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camaraID != null && luz) {
            try {
                camara.setTorchMode(camaraID, false);
                luz = false;
                if (btnLinterna != null) btnLinterna.setText("Encender Linterna");
            } catch (CameraAccessException ignored) {}
        }
    }

    // Menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_perfil) {
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("email_usuario", emailUsuario);
            editarPerfilLauncher.launch(i);
            return true;
        } else if (id == R.id.action_web) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com")));
            return true;
        } else if (id == R.id.action_salir) {
            finishWithAnimation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}