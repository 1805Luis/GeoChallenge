package es.practicacumn.geochallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificarCuenta extends AppCompatActivity {
    private TextView tvMensaje;
    private Button btnReenviar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_cuenta);
        tvMensaje = findViewById(R.id.tvMensaje);
        Bundle parametros=this.getIntent().getExtras();
        if ((parametros!=null)) {
             String correo = parametros.getString("email");
            tvMensaje.setText("Se le ha enviado un correo de verificacion al correo: "+ correo);
        }
        btnReenviar = findViewById(R.id.btnReenviar);
        btnReenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Se ha enviado un nuevo correo electrónico de verificación", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se ha podido enviar el correo electrónico de verificación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Verificar si el correo electrónico del usuario ha sido verificado
        FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // El correo electrónico del usuario ha sido verificado, redirigir al usuario a la pantalla de crar perfil
                    Intent intent = new Intent(getApplicationContext(), Perfil.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}